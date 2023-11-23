package com.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_libro")
    private Integer id;

    @NotBlank
    private String titulo;

    @NotBlank
    private String sinopsis;

    @NotNull
    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE)
    private LocalDate fechaPublicacion;

    @NotNull
    private String reviewId;

    private String rutaPortada;

    @NotEmpty
    @ManyToMany(fetch = FetchType.LAZY)//para que solo se realice la carga cuando se requiera
    @JoinTable(name="genero_libro",
            joinColumns = @JoinColumn(name = "id_libro"),
            inverseJoinColumns = @JoinColumn(name = "id_genero"))
    private List<Genero> generos;

    @Transient
    private MultipartFile portada;
}
