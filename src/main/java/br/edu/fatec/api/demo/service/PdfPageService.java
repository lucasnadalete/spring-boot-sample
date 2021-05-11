package br.edu.fatec.api.demo.service;

import br.edu.fatec.api.demo.entity.PageRevision;
import br.edu.fatec.api.demo.entity.PdfPage;
import br.edu.fatec.api.demo.enums.ProcessStatus;
import br.edu.fatec.api.demo.extractor.PdfHelper;
import br.edu.fatec.api.demo.repository.PdfPageRepository;
import br.edu.fatec.api.demo.vo.ProcessPdfResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class PdfPageService {
    private Pattern revisionPattern = Pattern.compile("(revision \\d{1,5}|original)", Pattern.CASE_INSENSITIVE);
    private Pattern codePattern = Pattern.compile("code \\d{1,5}", Pattern.CASE_INSENSITIVE);
    private Pattern pagePattern = Pattern.compile("page \\d{1,5}", Pattern.CASE_INSENSITIVE);

    @Autowired
    private PdfPageRepository pdfPageRepository;
    @Autowired
    private PdfHelper pdfHelper;
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
        String extractedContent = pdfHelper.extractFooterData(page);
        String[] splittedData = extractedContent.trim().split("\n");
        // Creating PdfPage
        String strPage = pdfHelper.getPageValue(splittedData[1], "-1")
                .replaceFirst("(?i)page", "").trim();
        PdfPage pdfPage = PdfPage.builder()
                .block(splittedData[0].trim())
                .page(new Long(strPage))
                .code(pdfHelper.getCodeValue(splittedData[1], "-1"))
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
                .revision(pdfHelper.getRevisionValue(splittedData[1], null))
                .pdfPage(pdfPage)
                .build();
        revision.setFileName(revision.getPathStorageFile());
        if(!pdfPage.getRevisions().contains(revision)) {
            pdfPage.getRevisions().add(revision);
        }
        pdfHelper.extractPageInNewPdfFile(page, rootPdfPath + revision.getFileName());
    }
}
