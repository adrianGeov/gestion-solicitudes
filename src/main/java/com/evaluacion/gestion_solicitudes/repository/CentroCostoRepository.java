package com.evaluacion.gestion_solicitudes.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.evaluacion.gestion_solicitudes.entity.CentroCosto;

public interface CentroCostoRepository  extends  JpaRepository <CentroCosto, Long>{

    Optional<CentroCosto> findByIdAndActivoTrue(Long id);

    Page<CentroCosto> findByActivoTrue(Pageable pageable);

    Page<CentroCosto> findByActivoTrueAndNombreContainingIgnoreCase(String nombre, Pageable pageable);

    boolean existsByCodigo(String codigo);

    boolean existsByCodigoAndIdNot(String codigo, Long id);

}
