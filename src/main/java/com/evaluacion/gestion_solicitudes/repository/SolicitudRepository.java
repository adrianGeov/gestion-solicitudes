package com.evaluacion.gestion_solicitudes.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evaluacion.gestion_solicitudes.entity.Solicitud;

public interface SolicitudRepository  extends JpaRepository<Solicitud, Long>, JpaSpecificationExecutor<Solicitud>{

   
    @Query("""
            SELECT s FROM Solicitud s
            JOIN FETCH s.centroCosto
            WHERE s.id = :id AND s.activo = true
            """)
    Optional<Solicitud> findActivaById(@Param("id") Long id);

  
    @Override
    @EntityGraph(attributePaths = "centroCosto")
    Page<Solicitud> findAll(Specification<Solicitud> spec, Pageable pageable);

    
    boolean existsByCentroCostoIdAndActivoTrue(Long centroCostoId);

}
