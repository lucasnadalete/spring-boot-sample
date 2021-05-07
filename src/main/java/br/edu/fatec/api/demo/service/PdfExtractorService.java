package br.edu.fatec.api.demo.service;

import br.edu.fatec.api.demo.enums.ProcessStatus;
import br.edu.fatec.api.demo.repository.PdfPageRepository;
import br.edu.fatec.api.demo.vo.ProcessPdfResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PdfExtractorService {
    @Autowired
    private PdfPageRepository pdfPageRepository;

    public ProcessPdfResponse extractPagesOfPdfs() {
        // TODO Extracting operation
        ProcessPdfResponse response = ProcessPdfResponse.builder()
                .numberOfPages(0L)
                .status(ProcessStatus.SUCCESS)
                .build();
        return response;
    }
}
