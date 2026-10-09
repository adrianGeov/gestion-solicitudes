package com.evaluacion.gestion_solicitudes.mapper;

import org.springframework.stereotype.Component;

import com.evaluacion.gestion_solicitudes.dto.CentroCostoRequest;
import com.evaluacion.gestion_solicitudes.dto.CentroCostoResponse;
import com.evaluacion.gestion_solicitudes.entity.CentroCosto;

@Component
public class CentroCostoMapper {

    public CentroCosto toEntity(CentroCostoRequest request) {
        CentroCosto entity = new CentroCosto();
        updateEntity(entity, request);
        return entity;
    }

    public void updateEntity(CentroCosto entity, CentroCostoRequest request) {
        entity.setCodigo(request.codigo().trim().toUpperCase());
        entity.setNombre(request.nombre().trim());
        entity.setDescripcion(request.descripcion());
    }

    public CentroCostoResponse toResponse(CentroCosto entity) {
        return new CentroCostoResponse(
                entity.getId(),
                entity.getCodigo(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.isActivo(),
                entity.getFechaCreacion(),
                entity.getFechaActualizacion(),
                entity.getCreadoPor(),
                entity.getModificadoPor()
            
            );
    }

}
