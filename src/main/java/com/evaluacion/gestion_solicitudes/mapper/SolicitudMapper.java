package com.evaluacion.gestion_solicitudes.mapper;

import org.springframework.stereotype.Component;

import com.evaluacion.gestion_solicitudes.dto.SolicitudRequest;
import com.evaluacion.gestion_solicitudes.dto.SolicitudResponse;
import com.evaluacion.gestion_solicitudes.entity.CentroCosto;
import com.evaluacion.gestion_solicitudes.entity.Solicitud;

@Component
public class SolicitudMapper {

    
    public Solicitud toEntity(SolicitudRequest request, CentroCosto centroCosto) {
        Solicitud entity = new Solicitud();
        updateEntity(entity, request, centroCosto);
        return entity;
    }

    public void updateEntity(Solicitud entity, SolicitudRequest request, CentroCosto centroCosto) {
        entity.setTitulo(request.titulo().trim());
        entity.setDescripcion(request.descripcion());
        entity.setMonto(request.monto());
        entity.setFechaSolicitud(request.fechaSolicitud());
        entity.setCentroCosto(centroCosto);
        if (request.estatus() != null) {
            entity.setEstatus(request.estatus());
        }
    }

    public SolicitudResponse toResponse(Solicitud entity) {
        CentroCosto cc = entity.getCentroCosto();
        return new SolicitudResponse(
                entity.getId(),
                entity.getTitulo(),
                entity.getDescripcion(),
                entity.getMonto(),
                entity.getEstatus(),
                entity.getFechaSolicitud(),
                entity.isActivo(),
                cc.getId(),
                cc.getCodigo(),
                cc.getNombre(),
                entity.getFechaCreacion(),
                entity.getFechaActualizacion(),
                entity.getCreadoPor(),
                entity.getModificadoPor()
        );
    }

}
