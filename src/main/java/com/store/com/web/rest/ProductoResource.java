package com.store.com.web.rest;

import com.store.com.service.ArchivoService;
import com.store.com.service.ProductoService;
import com.store.com.service.TopBusquedaService;
import com.store.com.service.dto.ArchivoDTO;
import com.store.com.service.mapper.ProductoMapper;
import com.store.com.web.rest.errors.BadRequestAlertException;
import com.store.com.service.dto.ProductoDTO;
import com.store.com.service.dto.ProductoCriteria;
import com.store.com.service.ProductoQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.store.com.domain.Producto}.
 */
@RestController
@RequestMapping("/api")
public class ProductoResource {

    private final Logger log = LoggerFactory.getLogger(ProductoResource.class);

    private static final String ENTITY_NAME = "storeProducto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductoService productoService;

    private final ProductoQueryService productoQueryService;

    private final TopBusquedaService topBusquedaService;

    private final ArchivoService archivoService;

    private final ProductoMapper productoMapper;

    /**
     * Instantiates a new Producto resource.
     *
     * @param productoService      the producto service
     * @param productoQueryService the producto query service
     * @param topBusquedaService   the top busqueda service
     * @param archivoService
     * @param productoMapper
     */
    public ProductoResource(ProductoService productoService, ProductoQueryService productoQueryService, TopBusquedaService topBusquedaService, ArchivoService archivoService, ProductoMapper productoMapper) {
        this.productoService = productoService;
        this.productoQueryService = productoQueryService;
        this.topBusquedaService = topBusquedaService;
        this.archivoService = archivoService;
        this.productoMapper = productoMapper;
    }

    /**
     * {@code POST  /productos} : Create a new producto.
     *
     * @param productoDTO the productoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productoDTO, or with status {@code 400 (Bad Request)} if the producto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/productos")
    public ResponseEntity<ProductoDTO> createProducto(@Valid @RequestBody ProductoDTO productoDTO) throws URISyntaxException {
        log.debug("REST request to save Producto : {}", productoDTO);
        if (productoDTO.getId() != null) {
            throw new BadRequestAlertException("A new producto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductoDTO result = productoService.save(productoDTO);
        return ResponseEntity.created(new URI("/api/productos/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /productos} : get all the productos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productos in body.
     */
    @GetMapping("/productos")
    public ResponseEntity<List<ProductoDTO>> getAllProductos(ProductoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Productos by criteria: {}", criteria);
        Page<ProductoDTO> page = productoQueryService.findByCriteria(criteria, pageable);
        if (!page.isEmpty() || criteria.getNombre() != null) {
            try {
                productoService.createTopProducto(page);
            } catch (Exception e) {
                log.error("Error al salvar busqueda exitosa -> {}", e);
            }
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /productos/:id} : get the "id" producto.
     *
     * @param id the id of the productoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/productos/{id}")
    public ResponseEntity<ProductoDTO> getProducto(@PathVariable Long id) {
        log.debug("REST request to get Producto : {}", id);
        Optional<ProductoDTO> productoDTO = productoService.findOne(id);
        List<ArchivoDTO> list = new ArrayList<>();
        productoDTO.ifPresent(dto -> list.addAll(archivoService.getAllArchivoProductos(productoMapper
                .toEntity(dto))));
        productoDTO.get().setFotos(new HashSet<>(list));
        return ResponseUtil.wrapOrNotFound(productoDTO);
    }

    /**
     * Gets all by top productos.
     *
     * @return the all by top productos
     */
    @GetMapping("/productos-top")
    public ResponseEntity<List<ProductoDTO>> getAllByTopProductos() {
        log.debug("REST request to get top by productos");
        List<ProductoDTO> list = topBusquedaService.getAllTopProductos();
        return ResponseEntity.ok().body(list);
    }

}
