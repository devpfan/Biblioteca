package com.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Genero {

    @Id
    @Column(name="id_genero")
    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    private String titulo;
}
