package com.evaluacion.gestion_solicitudes.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evaluacion.gestion_solicitudes.dto.SolicitudFiltro;
import com.evaluacion.gestion_solicitudes.dto.SolicitudRequest;
import com.evaluacion.gestion_solicitudes.dto.SolicitudResponse;
import com.evaluacion.gestion_solicitudes.entity.CentroCosto;
import com.evaluacion.gestion_solicitudes.entity.EstatusSolicitud;
import com.evaluacion.gestion_solicitudes.entity.Solicitud;
import com.evaluacion.gestion_solicitudes.exception.BusinessException;
import com.evaluacion.gestion_solicitudes.exception.ResourceNotFoundException;
import com.evaluacion.gestion_solicitudes.mapper.SolicitudMapper;
import com.evaluacion.gestion_solicitudes.repository.CentroCostoRepository;
import com.evaluacion.gestion_solicitudes.repository.SolicitudRepository;
import com.evaluacion.gestion_solicitudes.repository.SolicitudSpecification;
import com.evaluacion.gestion_solicitudes.service.SolicitudService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SolicitudServiceImpl implements SolicitudService {

    private static final String RECURSO = "Solicitud";

    private final SolicitudRepository solicitudRepository;
    private final CentroCostoRepository centroCostoRepository;
    private final SolicitudMapper mapper;

    @Override
    @Transactional
    public SolicitudResponse crear(SolicitudRequest request) {
        CentroCosto centroCosto = buscarCentroCostoActivo(request.centroCostoId());

        Solicitud entity = mapper.toEntity(request, centroCosto);
        if (request.estatus() == null) {
            entity.setEstatus(EstatusSolicitud.PENDIENTE);
        }

        Solicitud guardada = solicitudRepository.save(entity);
        log.info("Solicitud creada con id {} en centro de costo {}", guardada.getId(), centroCosto.getId());
        return mapper.toResponse(guardada);
    }

    @Override
    public SolicitudResponse obtenerPorId(Long id) {
        return mapper.toResponse(buscarActiva(id));
    }

    @Override
    public Page<SolicitudResponse> listar(SolicitudFiltro filtro, Pageable pageable) {
        if (filtro.fechaDesde() != null && filtro.fechaHasta() != null
                && filtro.fechaDesde().isAfter(filtro.fechaHasta())) {
            throw new BusinessException("La fecha desde no puede ser mayor a la fecha hasta");
        }

        return solicitudRepository
                .findAll(SolicitudSpecification.conFiltros(filtro), pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional
    public SolicitudResponse actualizar(Long id, SolicitudRequest request) {
        Solicitud entity = buscarActiva(id);

        if (entity.getEstatus() != EstatusSolicitud.PENDIENTE) {
            log.warn("Intento de modificar la solicitud {} con estatus {}", id, entity.getEstatus());
            throw new BusinessException("Solo se pueden modificar solicitudes en estatus PENDIENTE");
        }

        CentroCosto centroCosto = entity.getCentroCosto().getId().equals(request.centroCostoId())
                ? entity.getCentroCosto()
                : buscarCentroCostoActivo(request.centroCostoId());

        mapper.updateEntity(entity, request, centroCosto);
        Solicitud actualizada = solicitudRepository.saveAndFlush(entity);
        log.info("Solicitud {} actualizada", id);
        return mapper.toResponse(actualizada);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Solicitud entity = buscarActiva(id);
        entity.setActivo(false); // Eliminación lógica
        log.info("Solicitud {} eliminada lógicamente", id);
    }

    private Solicitud buscarActiva(Long id) {
        return solicitudRepository.findActivaById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RECURSO, id));
    }

    private CentroCosto buscarCentroCostoActivo(Long id) {
        return centroCostoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Centro de costo", id));
    }

}
