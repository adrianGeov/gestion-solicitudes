package com.evaluacion.gestion_solicitudes.exception;

public class ResourceNotFoundException  extends  RuntimeException{

     public ResourceNotFoundException(String recurso, Long id) {
        super(String.format("%s con id %d no encontrado", recurso, id));
    }

}
