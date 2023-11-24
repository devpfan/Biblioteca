package com.library.controller;

import com.library.model.Genero;
import com.library.model.Libro;
import com.library.repository.GeneroRepository;
import com.library.repository.LibroRepository;
import com.library.service.AlmacenServicioImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private GeneroRepository generoRepository;

    @Autowired
    private AlmacenServicioImpl servicio;

    @GetMapping("")
    public ModelAndView verPaginaDeInicio(@PageableDefault(sort ="titulo", size = 5)Pageable pageable){
        Page<Libro> libros = libroRepository.findAll(pageable);
        return new ModelAndView("index").addObject("libros", libros);
    }

    @GetMapping("/libros/nuevo")
    public ModelAndView mostrarFormularioDeNuevoLibro(){
        List<Genero> generos = generoRepository.findAll(Sort.by("titulo"));
        System.out.println("Entró en mostrarFormularioDeNuevoLibro");
        return new ModelAndView("admin/nuevo-libro")
                .addObject("libro",new Libro())
                .addObject("generos",generos);
    }
}
