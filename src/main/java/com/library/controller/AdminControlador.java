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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        return new ModelAndView("admin/index").addObject("libros", libros);
    }

    @GetMapping("/libros/nuevo")
    public ModelAndView mostrarFormularioDeNuevoLibro(){
        List<Genero> generos = generoRepository.findAll(Sort.by("titulo"));

        return new ModelAndView("admin/nuevo-libro")
                .addObject("libro",new Libro())
                .addObject("generos",generos);
    }

    @PostMapping("/libros/nuevo")
    public ModelAndView registrarLibro(@Validated Libro libro, BindingResult bindingResult){
        if(bindingResult.hasErrors() || libro.getPortada().isEmpty()){
            if (libro.getPortada().isEmpty()){
                bindingResult.rejectValue("portada","MultipartNotEmpty");
            }
            List<Genero> generos = generoRepository.findAll(Sort.by("titulo"));
            return new ModelAndView("admin/nuevo-libro")
                    .addObject("libro",libro)
                    .addObject("generos",generos);
        }

        String rutaPortada = servicio.almacenarArchivo(libro.getPortada());
        libro.setRutaPortada(rutaPortada);

        libroRepository.save(libro);
        return new ModelAndView("redirect:/admin");
    }

    @GetMapping("/libros/{id}/editar")
    public ModelAndView mostrarFormularioEditarLibro(@PathVariable Integer id){
        Libro libro = libroRepository.getOne(id);
        List<Genero> generos = generoRepository.findAll(Sort.by("titulo"));

        return new ModelAndView("admin/editar-libro")
                .addObject("libro",libro)
                .addObject("generos",generos);
    }

    @PostMapping("/libros/{id}/editar")
    public ModelAndView actualizarLibro(@PathVariable Integer id, @Validated Libro libro, BindingResult bindingResult){
            if(bindingResult.hasErrors()){
                List<Genero> generos = generoRepository.findAll(Sort.by("titulo"));
                return new ModelAndView("admin/editar-libro")
                        .addObject("libro",libro)
                        .addObject("generos",generos);
             }
            Libro libroDB = libroRepository.getOne(id);
            libroDB.setTitulo(libro.getTitulo());
            libroDB.setSinopsis(libro.getSinopsis());
            libroDB.setFechaPublicacion(libro.getFechaPublicacion());
            libroDB.setReviewId(libro.getReviewId());
            libroDB.setGeneros(libro.getGeneros());

            if (!libro.getPortada().isEmpty()){
                servicio.eliminarArchivo(libroDB.getRutaPortada());
                String rutaPortada = servicio.almacenarArchivo(libro.getPortada());
                libroDB.setRutaPortada(rutaPortada);
        }
            libroRepository.save(libroDB);
        return new ModelAndView("redirect:/admin");
    }

    @PostMapping("/libros/{id}/eliminar")
    public String eliminarLibro(@PathVariable Integer id){
        Libro libro = libroRepository.getOne(id);
        libroRepository.delete(libro);
        servicio.eliminarArchivo(libro.getRutaPortada());

        return "redirect:/admin";
    }

}
