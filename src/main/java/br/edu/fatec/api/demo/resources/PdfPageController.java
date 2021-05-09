package br.edu.fatec.api.demo.resources;

import br.edu.fatec.api.demo.entity.PdfPage;
import br.edu.fatec.api.demo.enums.ProcessStatus;
import br.edu.fatec.api.demo.repository.PdfPageRepository;
import br.edu.fatec.api.demo.service.PdfExtractorService;
import br.edu.fatec.api.demo.vo.ProcessPdfResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("pdfpage")
@Api(value = "PDF Page")
public class PdfPageController {
    @Autowired
    private PdfPageRepository pdfPageRepository;
    @Autowired
    private PdfExtractorService pdfExtractorService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List all pdf pages registered and your revisions")
    public ResponseEntity<List<PdfPage>> listAll() {
        List<PdfPage> pdfPageList = (List<PdfPage>) pdfPageRepository.findAll();
        if (!pdfPageList.isEmpty())
            return new ResponseEntity<>(pdfPageList, HttpStatus.OK);
        else
            return new ResponseEntity<>(pdfPageList, HttpStatus.NOT_FOUND);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Process all PDFs storage in pdf directory")
    public ResponseEntity<ProcessPdfResponse> process() {
        try {
            ProcessPdfResponse response = pdfExtractorService.extractPagesOfPdfs();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(new ProcessPdfResponse(), HttpStatus.BAD_REQUEST);
        }
    }

}
