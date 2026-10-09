package com.evaluacion.gestion_solicitudes.exception;

public class ExternalServiceException  extends RuntimeException{

     public ExternalServiceException(String mensaje) {
        super(mensaje);
    }

    public ExternalServiceException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }


}
