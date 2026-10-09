package com.evaluacion.gestion_solicitudes.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evaluacion.gestion_solicitudes.dto.CentroCostoRequest;
import com.evaluacion.gestion_solicitudes.dto.CentroCostoResponse;
import com.evaluacion.gestion_solicitudes.entity.CentroCosto;
import com.evaluacion.gestion_solicitudes.exception.BusinessException;
import com.evaluacion.gestion_solicitudes.exception.ResourceNotFoundException;
import com.evaluacion.gestion_solicitudes.mapper.CentroCostoMapper;
import com.evaluacion.gestion_solicitudes.repository.CentroCostoRepository;
import com.evaluacion.gestion_solicitudes.repository.SolicitudRepository;
import com.evaluacion.gestion_solicitudes.service.CentroCostoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CentroCostoServiceImpl implements CentroCostoService {

    private static final String RECURSO = "Centro de costo";

    private final CentroCostoRepository centroCostoRepository;
    private final SolicitudRepository solicitudRepository;
    private final CentroCostoMapper mapper;

    @Override
    @Transactional
    public CentroCostoResponse crear(CentroCostoRequest request) {
        String codigo = request.codigo().trim().toUpperCase();
        if (centroCostoRepository.existsByCodigo(codigo)) {
            log.warn("Intento de crear centro de costo con código duplicado: {}", codigo);
            throw new BusinessException("Ya existe un centro de costo con el código " + codigo);
        }

        CentroCosto guardado = centroCostoRepository.save(mapper.toEntity(request));
        log.info("Centro de costo creado con id {}", guardado.getId());
        return mapper.toResponse(guardado);
    }

    @Override
    public CentroCostoResponse obtenerPorId(Long id) {
        return mapper.toResponse(buscarActivo(id));
    }

    @Override
    public Page<CentroCostoResponse> listar(String nombre, Pageable pageable) {
        Page<CentroCosto> pagina = (nombre == null || nombre.isBlank())
                ? centroCostoRepository.findByActivoTrue(pageable)
                : centroCostoRepository.findByActivoTrueAndNombreContainingIgnoreCase(nombre.trim(), pageable);

        return pagina.map(mapper::toResponse);
    }

    @Override
    @Transactional
    public CentroCostoResponse actualizar(Long id, CentroCostoRequest request) {
        CentroCosto entity = buscarActivo(id);

        String codigo = request.codigo().trim().toUpperCase();
        if (centroCostoRepository.existsByCodigoAndIdNot(codigo, id)) {
            log.warn("Intento de actualizar centro de costo {} con código duplicado: {}", id, codigo);
            throw new BusinessException("Ya existe otro centro de costo con el código " + codigo);
        }

        mapper.updateEntity(entity, request);
        CentroCosto actualizado = centroCostoRepository.saveAndFlush(entity);
        log.info("Centro de costo {} actualizado", id);
        return mapper.toResponse(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        CentroCosto entity = buscarActivo(id);

        if (solicitudRepository.existsByCentroCostoIdAndActivoTrue(id)) {
            log.warn("No se puede eliminar el centro de costo {}: tiene solicitudes activas", id);
            throw new BusinessException("No se puede eliminar el centro de costo porque tiene solicitudes activas");
        }

        entity.setActivo(false); // Eliminación lógica
        log.info("Centro de costo {} eliminado lógicamente", id);
    }

    private CentroCosto buscarActivo(Long id) {
        return centroCostoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(RECURSO, id));
    }

}
