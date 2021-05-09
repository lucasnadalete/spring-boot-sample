package br.edu.fatec.api.demo.service;

import br.edu.fatec.api.demo.entity.PageRevision;
import br.edu.fatec.api.demo.entity.PdfPage;
import br.edu.fatec.api.demo.enums.ProcessStatus;
import br.edu.fatec.api.demo.repository.PdfPageRepository;
import br.edu.fatec.api.demo.vo.ProcessPdfResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class PdfExtractorService {
    private Pattern revisionPattern = Pattern.compile("(revision \\d{1,5}|original)", Pattern.CASE_INSENSITIVE);
    private Pattern codePattern = Pattern.compile("code \\d{1,5}", Pattern.CASE_INSENSITIVE);
    private Pattern pagePattern = Pattern.compile("page \\d{1,5}", Pattern.CASE_INSENSITIVE);

    @Autowired
    private PdfPageRepository pdfPageRepository;
    @Value("classpath:pdf/*")
    private Resource[] pdfResources;
    @Value("${pdf.pages.dir}")
    private String rootPdfPath;

    @Transactional
    public ProcessPdfResponse extractPagesOfPdfs() throws IOException {
        List<PdfPage> pdfPageList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (final Resource res : pdfResources) {
            sb.append(res.getURI().getPath());
            sb.append(" | ");
            extractPageDataAllPages(res.getURI().getPath(), pdfPageList);
        }
        int lastPipeIndex = sb.toString().lastIndexOf('|');
        pdfPageRepository.saveAll(pdfPageList);
        ProcessPdfResponse response = ProcessPdfResponse.builder()
                .numberOfPages(new Long(pdfPageList.size()))
                .status(ProcessStatus.SUCCESS)
                .files(sb.substring(0, lastPipeIndex) + ']')
                .build();
        return response;
    }

    private void extractPageDataAllPages(String pdfPath, List<PdfPage> pdfPageList) throws IOException {
        PDDocument document = null;
        try {
            document = PDDocument.load(new File(pdfPath));
            int pageIndex = 2;
            while (pageIndex < document.getNumberOfPages()) {
                extractPageDataEachPage(document.getPage(pageIndex++), pdfPageList);
            }
        } catch (IOException e) {
            log.error("Fail loading PDF file", e);
        } finally {
            if(document != null)
                document.close();
        }
    }

    private void extractPageDataEachPage(PDPage page, List<PdfPage> pdfPageList) throws IOException {
        PDFTextStripperByArea stripper = new PDFTextStripperByArea();
        stripper.setSortByPosition(true);
        Rectangle2D rect = new Rectangle2D.Float( 0f, 570f, 400f, 60f );
        stripper.addRegion( "footer_data", rect );
        stripper.extractRegions(page);
        // Data treatment
        String extractedContent = stripper.getTextForRegion("footer_data");
        String[] splittedData = extractedContent.trim().split("\n");
        // Creating PdfPage
        String strPage = extractValueByPattern(getPagePattern(), splittedData[1], "-1")
                .replaceFirst("(?i)page", "").trim();
        PdfPage pdfPage = PdfPage.builder()
                .block(splittedData[0].trim())
                .page(new Long(strPage))
                .code(extractValueByPattern(getCodePattern(), splittedData[1], null))
                .revisions(new ArrayList<>())
                .build();
        log.info(pdfPage.toString());
        // Verifying if PdfPage already exists in List<PdfPage>
        PdfPage pdfPageFound = pdfPageRepository
                .findFirstByBlockAndAndCodeAndPage(pdfPage.getBlock(), pdfPage.getCode(), pdfPage.getPage());
        if(pdfPageList.contains(pdfPage)) {
            pdfPage = pdfPageList.get(pdfPageList.indexOf(pdfPage));
        } else {
            if(pdfPageFound != null)
                pdfPage = pdfPageFound;
            pdfPageList.add(pdfPage);
        }
        // Creating PageRevision and adding in PdfPage revisions
        PageRevision revision = PageRevision.builder()
                .revision(extractValueByPattern(getRevisionPattern(), splittedData[1], null))
                .pdfPage(pdfPage)
                .build();
        revision.setFileName(revision.getPathStorageFile());
        if(!pdfPage.getRevisions().contains(revision)) {
            pdfPage.getRevisions().add(revision);
        }
        extractPageInNewPdfFile(page, rootPdfPath + revision.getFileName());
    }

    private String extractValueByPattern(Pattern pattern, String content, String defaultValue) {
        Matcher matcher = pattern.matcher(content);
        return matcher.find() ? matcher.group() : defaultValue;
    }

    private void extractPageInNewPdfFile(PDPage page, String path) {
        // Creating folders structure
        int indexLastSeparator = path.lastIndexOf(File.separator);
        File folderPath = new File(path.substring(0, indexLastSeparator));
        folderPath.mkdirs();
        // Saving a new PDF
        PDDocument newPdfDocument = new PDDocument();
        newPdfDocument.addPage(page);
        try {
            newPdfDocument.save(path);
            newPdfDocument.close();
        } catch(IOException ex) {
            log.error("Fail saving a new PDF file with single page", ex);
        }
    }
}
