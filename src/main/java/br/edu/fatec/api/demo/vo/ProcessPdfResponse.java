package br.edu.fatec.api.demo.vo;

import br.edu.fatec.api.demo.enums.ProcessStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProcessPdfResponse {
    private Long numberOfPages;
    private ProcessStatus status;

    public ProcessPdfResponse() {
        numberOfPages = -1L;
        status = ProcessStatus.UNSUCCESS;
    }
}
