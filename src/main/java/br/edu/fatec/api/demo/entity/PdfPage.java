package br.edu.fatec.api.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "pdf_pages_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PdfPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String block;
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private Long page;
    @OneToMany(mappedBy="page", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PageRevision> revisions;
}
