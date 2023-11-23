package com.library.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.NOT_FOUND)
public class FilaNotFoundException extends RuntimeException{
    public FilaNotFoundException (String mensaje){
        super(mensaje);
    }

    public FilaNotFoundException(String mensaje, Throwable excepcion){
        super(mensaje,excepcion);
    }
}
