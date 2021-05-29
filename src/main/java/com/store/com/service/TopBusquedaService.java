package com.store.com.service;

import com.store.com.domain.TopBusqueda;
import com.store.com.repository.TopBusquedaRepository;
import com.store.com.service.dto.ArchivoDTO;
import com.store.com.service.dto.ProductoDTO;
import com.store.com.service.mapper.ProductoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link TopBusqueda}.
 */
@Service
@Transactional
public class TopBusquedaService {

    private final Logger log = LoggerFactory.getLogger(TopBusquedaService.class);

    private final TopBusquedaRepository topBusquedaRepository;

    private final ArchivoService archivoService;

    private final ProductoMapper productoMapper;

    /**
     * Instantiates a new Top busqueda service.
     *
     * @param topBusquedaRepository the top busqueda repository
     * @param archivoService
     * @param productoMapper
     */
    public TopBusquedaService(TopBusquedaRepository topBusquedaRepository, ArchivoService archivoService, ProductoMapper productoMapper) {
        this.topBusquedaRepository = topBusquedaRepository;
        this.archivoService = archivoService;
        this.productoMapper = productoMapper;
    }

    /**
     * Gets all top productos.
     *
     * @return the all top productos
     */
    public List<ProductoDTO> getAllTopProductos() {
        List<ProductoDTO> list = new ArrayList<>();
        topBusquedaRepository.getAllTopProductos().forEach(p -> {
            ProductoDTO producto = new ProductoDTO();
            producto.setId(p.getId());
            producto.setDescripcion(p.getNombre());
            producto.setPorcentaje(p.getPorcentaje());
            producto.setPrecio(p.getPrecio());
            producto.setPrecioDescuento(p.getPrecioDescuento());
            producto.setFotos(new HashSet<>(archivoService.getAllArchivoProductos(productoMapper
                    .toEntity(producto))));
            list.add(producto);
        });
        return list;
    }

}
