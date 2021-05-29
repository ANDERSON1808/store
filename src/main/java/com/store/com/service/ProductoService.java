package com.store.com.service;

import com.store.com.domain.Producto;
import com.store.com.domain.TopBusqueda;
import com.store.com.repository.ProductoRepository;
import com.store.com.repository.TopBusquedaRepository;
import com.store.com.service.dto.ArchivoDTO;
import com.store.com.service.dto.ProductoDTO;
import com.store.com.service.dto.TopBusquedaDTO;
import com.store.com.service.mapper.ProductoMapper;
import com.store.com.service.mapper.TopBusquedaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Producto}.
 */
@Service
@Transactional
public class ProductoService {

    private final Logger log = LoggerFactory.getLogger(ProductoService.class);

    private final ProductoRepository productoRepository;

    private final ProductoMapper productoMapper;

    private final TopBusquedaRepository topBusquedaRepository;

    private final TopBusquedaMapper topBusquedaMapper;

    private final ArchivoService archivoService;

    /**
     * Instantiates a new Producto service.
     *
     * @param productoRepository    the producto repository
     * @param productoMapper        the producto mapper
     * @param topBusquedaRepository
     * @param topBusquedaMapper
     * @param archivoService
     */
    public ProductoService(ProductoRepository productoRepository, ProductoMapper productoMapper,
                           TopBusquedaRepository topBusquedaRepository, TopBusquedaMapper topBusquedaMapper, ArchivoService archivoService) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
        this.topBusquedaRepository = topBusquedaRepository;
        this.topBusquedaMapper = topBusquedaMapper;
        this.archivoService = archivoService;
    }

    /**
     * Save a producto.
     *
     * @param productoDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductoDTO save(ProductoDTO productoDTO) {
        log.debug("Request to save Producto : {}", productoDTO);
        Producto producto = productoMapper.toEntity(productoDTO);
        producto = productoRepository.save(producto);
        if (!productoDTO.getFotos().isEmpty()) {
            archivoService.createArchivoList(new ArrayList<>(productoDTO.getFotos()),
                    producto);
        }
        return productoMapper.toDto(producto);
    }

    /**
     * Get all the productos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Productos");
        return productoRepository.findAll(pageable)
                .map(productoMapper::toDto);
    }

    /**
     * Create top producto.
     *
     * @param dtoOptional the dto optional
     */
    @Async
    public void createTopProducto(Page<ProductoDTO> dtoOptional) {
        List<TopBusqueda> list = new ArrayList<>();
        dtoOptional.forEach(l -> {
            TopBusquedaDTO dto = new TopBusquedaDTO();
            dto.setProductoId(l.getId());
            list.add(topBusquedaMapper.toEntity(dto));
        });
        topBusquedaRepository.saveAll(list);
    }

    /**
     * Find one optional.
     *
     * @param id the id
     * @return the optional
     */
    public Optional<ProductoDTO> findOne(Long id) {
        return productoRepository.findById(id)
                .map(productoMapper::toDto);
    }
}
