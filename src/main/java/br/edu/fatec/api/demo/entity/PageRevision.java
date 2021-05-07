package br.edu.fatec.api.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "revision_pdf_pages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRevision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JsonIgnore
    @JoinColumn(name="pdf_page_id", insertable = false, updatable = false, nullable = false)
    private PdfPage page;
    @Column(name = "file_name", nullable = false)
    private String fileName;
    private String revision;
}
