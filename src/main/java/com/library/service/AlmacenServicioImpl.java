package com.library.service;

import com.library.exceptions.AlmacenException;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;


@Service
public class AlmacenServicioImpl implements AlmacenServicio{

    @Value("${storage.location}")
    private String storageLocation;

    @Override
    @PostConstruct //para indicar que este metodo a ejecutar cada vez que hay una nueva instancia de esta clase
    public void iniciarAlmacenDeArchivos() {
        try {
            Files.createDirectories(Paths.get(storageLocation));
        }catch(IOException excepcion){
            throw new AlmacenException("Error al inicializar la ubicación en el almacen de archivos");
        }
    }

    @Override
    public String almacenarArchivo(MultipartFile archivo) {
        String nombreArchivo = archivo.getOriginalFilename();
        if (archivo.isEmpty()){
            throw new AlmacenException("No se puede almacenar un archivo vacio!");
        }
        try {
            InputStream inputStream = archivo.getInputStream();
            Files.copy(inputStream, Paths.get(storageLocation).resolve(nombreArchivo), StandardCopyOption.REPLACE_EXISTING);//si hay un archivo con el mismo nombre lo reemplaza
        }catch (IOException excepcion){
            throw new AlmacenException("Error al almacenar el archivo" + nombreArchivo,excepcion);
        }
        return nombreArchivo;
    }

    @Override
    public Path cargarArchivo(String nombreArchivo) {
        return Paths.get(storageLocation).resolve(nombreArchivo);
    }

    @Override
    public Resource cargarComoRecurso(String nombreArchivo) {
        try {
            Path archivo = cargarArchivo(nombreArchivo);
            Resource recurso = new UrlResource(archivo.toUri());
            if (recurso.exists()|| recurso.isReadable()){
                return recurso;
            }else {
                throw new RuntimeException("no se pudo encontrar el archivo");
            }
        }catch (MalformedURLException excepcion){
            throw new RuntimeException("No se pudo encontrar el archivo" +nombreArchivo,excepcion);
        }
    }

    @Override
    public void eliminarArchivo(String nombreArchivo) {

        Path archivo = cargarArchivo(nombreArchivo);
        try {
            FileSystemUtils.deleteRecursively(archivo);
        }catch (Exception excepcion){
            System.out.println(excepcion);
        }

    }
}
