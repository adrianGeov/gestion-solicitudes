package com.evaluacion.gestion_solicitudes.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import com.evaluacion.gestion_solicitudes.dto.SolicitudFiltro;
import com.evaluacion.gestion_solicitudes.entity.Solicitud;

public final class SolicitudSpecification {

     private SolicitudSpecification() {
       
    }


     public static Specification<Solicitud> conFiltros(SolicitudFiltro filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicados = new ArrayList<>();
            
            predicados.add(cb.isTrue(root.get("activo")));

            if (filtro.titulo() != null && !filtro.titulo().isBlank()) {
                predicados.add(cb.like(
                        cb.lower(root.get("titulo")),
                        "%" + filtro.titulo().trim().toLowerCase() + "%"));
            }
            if (filtro.estatus() != null) {
                predicados.add(cb.equal(root.get("estatus"), filtro.estatus()));
            }
            if (filtro.centroCostoId() != null) {
                predicados.add(cb.equal(root.get("centroCosto").get("id"), filtro.centroCostoId()));
            }
            if (filtro.fechaDesde() != null) {
                predicados.add(cb.greaterThanOrEqualTo(root.get("fechaSolicitud"), filtro.fechaDesde()));
            }
            if (filtro.fechaHasta() != null) {
                predicados.add(cb.lessThanOrEqualTo(root.get("fechaSolicitud"), filtro.fechaHasta()));
            }

            return cb.and(predicados.toArray(new Predicate[0]));
        };
    }


}
