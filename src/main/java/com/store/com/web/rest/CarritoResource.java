package com.store.com.web.rest;

import com.store.com.service.CarritoService;
import com.store.com.web.rest.errors.BadRequestAlertException;
import com.store.com.service.dto.CarritoDTO;
import com.store.com.service.dto.CarritoCriteria;
import com.store.com.service.CarritoQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.store.com.domain.Carrito}.
 */
@RestController
@RequestMapping("/api")
public class CarritoResource {

    private final Logger log = LoggerFactory.getLogger(CarritoResource.class);

    private static final String ENTITY_NAME = "storeCarrito";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarritoService carritoService;

    private final CarritoQueryService carritoQueryService;

    public CarritoResource(CarritoService carritoService, CarritoQueryService carritoQueryService) {
        this.carritoService = carritoService;
        this.carritoQueryService = carritoQueryService;
    }

    /**
     * Create carrito response entity.
     *
     * @param carritoDTOList the carrito dto list
     * @return the response entity
     * @throws URISyntaxException the uri syntax exception
     */
    @PostMapping("/carritos")
    public ResponseEntity<List<CarritoDTO>> createCarrito(@RequestBody List<CarritoDTO> carritoDTOList) throws URISyntaxException {
        log.debug("REST request to save Carrito : {}", carritoDTOList.listIterator().next().getProductoId());
        if (carritoDTOList.isEmpty()) {
            throw new BadRequestAlertException("Se requiere al menos un producto", ENTITY_NAME, "productosId null");
        }
        List<CarritoDTO> retur = carritoService.saveList(carritoDTOList);
        return ResponseEntity.ok().body(retur);
    }

    /**
     * {@code GET  /carritos} : get all the carritos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of carritos in body.
     */
    @GetMapping("/carritos")
    public ResponseEntity<List<CarritoDTO>> getAllCarritos(CarritoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Carritos by criteria: {}", criteria);
        Page<CarritoDTO> page = carritoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
