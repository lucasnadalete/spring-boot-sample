package br.edu.fatec.api.demo.repository;

import br.edu.fatec.api.demo.entity.PdfPage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PdfPageRepository extends CrudRepository<PdfPage, Long> {
    PdfPage findFirstByBlockAndAndCodeAndPage(String block, String code, Long page);
}
