package com.store.com.service;

import com.store.com.domain.Archivo;
import com.store.com.domain.Producto;
import com.store.com.repository.ArchivoRepository;
import com.store.com.service.dto.ArchivoDTO;
import com.store.com.service.dto.ProductoDTO;
import com.store.com.service.mapper.ArchivoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Archivo}.
 */
@Service
@Transactional
public class ArchivoService {

    private final Logger log = LoggerFactory.getLogger(ArchivoService.class);

    private final ArchivoRepository archivoRepository;

    private final ArchivoMapper archivoMapper;

    /**
     * Instantiates a new Archivo service.
     *
     * @param archivoRepository the archivo repository
     * @param archivoMapper     the archivo mapper
     */
    public ArchivoService(ArchivoRepository archivoRepository, ArchivoMapper archivoMapper) {
        this.archivoRepository = archivoRepository;
        this.archivoMapper = archivoMapper;
    }

    /**
     * Save a archivo.
     *
     * @param archivoDTO the entity to save.
     * @return the persisted entity.
     */
    public ArchivoDTO save(ArchivoDTO archivoDTO) {
        log.debug("Request to save Archivo : {}", archivoDTO);
        Archivo archivo = archivoMapper.toEntity(archivoDTO);
        archivo = archivoRepository.save(archivo);
        return archivoMapper.toDto(archivo);
    }

    /**
     * Get all the archivos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ArchivoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Archivos");
        return archivoRepository.findAll(pageable)
                .map(archivoMapper::toDto);
    }


    /**
     * Create archivo list.
     *
     * @param list the list
     * @param pro  the pro
     */
    public void createArchivoList(List<ArchivoDTO> list, Producto pro) {
        list.forEach(p -> {
                    p.setProductoId(pro.getId());
                    save(p);
                }
        );
    }

    /**
     * Gets all archivo productos.
     *
     * @param producto the producto
     * @return the all archivo productos
     */
    public List<ArchivoDTO> getAllArchivoProductos(Producto producto) {
        List<ArchivoDTO> list = archivoRepository.getAllByProducto(producto)
                .map(archivoMapper::toDto).get();
        return list;
    }
}
