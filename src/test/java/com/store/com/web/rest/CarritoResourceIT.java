package com.store.com.web.rest;

import com.store.com.StoreApp;
import com.store.com.domain.Carrito;
import com.store.com.domain.Producto;
import com.store.com.repository.CarritoRepository;
import com.store.com.service.CarritoService;
import com.store.com.service.dto.CarritoDTO;
import com.store.com.service.mapper.CarritoMapper;
import com.store.com.service.dto.CarritoCriteria;
import com.store.com.service.CarritoQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CarritoResource} REST controller.
 */
@SpringBootTest(classes = StoreApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CarritoResourceIT {

    private static final Long DEFAULT_CANTIDAD = 1L;
    private static final Long UPDATED_CANTIDAD = 2L;
    private static final Long SMALLER_CANTIDAD = 1L - 1L;

    private static final Long DEFAULT_VALOR = 1L;
    private static final Long UPDATED_VALOR = 2L;
    private static final Long SMALLER_VALOR = 1L - 1L;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private CarritoMapper carritoMapper;

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private CarritoQueryService carritoQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarritoMockMvc;

    private Carrito carrito;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Carrito createEntity(EntityManager em) {
        Carrito carrito = new Carrito()
            .cantidad(DEFAULT_CANTIDAD)
            .valor(DEFAULT_VALOR);
        return carrito;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Carrito createUpdatedEntity(EntityManager em) {
        Carrito carrito = new Carrito()
            .cantidad(UPDATED_CANTIDAD)
            .valor(UPDATED_VALOR);
        return carrito;
    }

    @BeforeEach
    public void initTest() {
        carrito = createEntity(em);
    }

    @Test
    @Transactional
    public void createCarrito() throws Exception {
        int databaseSizeBeforeCreate = carritoRepository.findAll().size();
        // Create the Carrito
        CarritoDTO carritoDTO = carritoMapper.toDto(carrito);
        restCarritoMockMvc.perform(post("/api/carritos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carritoDTO)))
            .andExpect(status().isCreated());

        // Validate the Carrito in the database
        List<Carrito> carritoList = carritoRepository.findAll();
        assertThat(carritoList).hasSize(databaseSizeBeforeCreate + 1);
        Carrito testCarrito = carritoList.get(carritoList.size() - 1);
        assertThat(testCarrito.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testCarrito.getValor()).isEqualTo(DEFAULT_VALOR);
    }

    @Test
    @Transactional
    public void createCarritoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = carritoRepository.findAll().size();

        // Create the Carrito with an existing ID
        carrito.setId(1L);
        CarritoDTO carritoDTO = carritoMapper.toDto(carrito);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarritoMockMvc.perform(post("/api/carritos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carritoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Carrito in the database
        List<Carrito> carritoList = carritoRepository.findAll();
        assertThat(carritoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCarritos() throws Exception {
        // Initialize the database
        carritoRepository.saveAndFlush(carrito);

        // Get all the carritoList
        restCarritoMockMvc.perform(get("/api/carritos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carrito.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD.intValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.intValue())));
    }
    
    @Test
    @Transactional
    public void getCarrito() throws Exception {
        // Initialize the database
        carritoRepository.saveAndFlush(carrito);

        // Get the carrito
        restCarritoMockMvc.perform(get("/api/carritos/{id}", carrito.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(carrito.getId().intValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD.intValue()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.intValue()));
    }


    @Test
    @Transactional
    public void getCarritosByIdFiltering() throws Exception {
        // Initialize the database
        carritoRepository.saveAndFlush(carrito);

        Long id = carrito.getId();

        defaultCarritoShouldBeFound("id.equals=" + id);
        defaultCarritoShouldNotBeFound("id.notEquals=" + id);

        defaultCarritoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCarritoShouldNotBeFound("id.greaterThan=" + id);

        defaultCarritoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCarritoShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCarritosByCantidadIsEqualToSomething() throws Exception {
        // Initialize the database
        carritoRepository.saveAndFlush(carrito);

        // Get all the carritoList where cantidad equals to DEFAULT_CANTIDAD
        defaultCarritoShouldBeFound("cantidad.equals=" + DEFAULT_CANTIDAD);

        // Get all the carritoList where cantidad equals to UPDATED_CANTIDAD
        defaultCarritoShouldNotBeFound("cantidad.equals=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    public void getAllCarritosByCantidadIsNotEqualToSomething() throws Exception {
        // Initialize the database
        carritoRepository.saveAndFlush(carrito);

        // Get all the carritoList where cantidad not equals to DEFAULT_CANTIDAD
        defaultCarritoShouldNotBeFound("cantidad.notEquals=" + DEFAULT_CANTIDAD);

        // Get all the carritoList where cantidad not equals to UPDATED_CANTIDAD
        defaultCarritoShouldBeFound("cantidad.notEquals=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    public void getAllCarritosByCantidadIsInShouldWork() throws Exception {
        // Initialize the database
        carritoRepository.saveAndFlush(carrito);

        // Get all the carritoList where cantidad in DEFAULT_CANTIDAD or UPDATED_CANTIDAD
        defaultCarritoShouldBeFound("cantidad.in=" + DEFAULT_CANTIDAD + "," + UPDATED_CANTIDAD);

        // Get all the carritoList where cantidad equals to UPDATED_CANTIDAD
        defaultCarritoShouldNotBeFound("cantidad.in=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    public void getAllCarritosByCantidadIsNullOrNotNull() throws Exception {
        // Initialize the database
        carritoRepository.saveAndFlush(carrito);

        // Get all the carritoList where cantidad is not null
        defaultCarritoShouldBeFound("cantidad.specified=true");

        // Get all the carritoList where cantidad is null
        defaultCarritoShouldNotBeFound("cantidad.specified=false");
    }

    @Test
    @Transactional
    public void getAllCarritosByCantidadIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        carritoRepository.saveAndFlush(carrito);

        // Get all the carritoList where cantidad is greater than or equal to DEFAULT_CANTIDAD
        defaultCarritoShouldBeFound("cantidad.greaterThanOrEqual=" + DEFAULT_CANTIDAD);

        // Get all the carritoList where cantidad is greater than or equal to UPDATED_CANTIDAD
        defaultCarritoShouldNotBeFound("cantidad.greaterThanOrEqual=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    public void getAllCarritosByCantidadIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        carritoRepository.saveAndFlush(carrito);

        // Get all the carritoList where cantidad is less than or equal to DEFAULT_CANTIDAD
        defaultCarritoShouldBeFound("cantidad.lessThanOrEqual=" + DEFAULT_CANTIDAD);

        // Get all the carritoList where cantidad is less than or equal to SMALLER_CANTIDAD
        defaultCarritoShouldNotBeFound("cantidad.lessThanOrEqual=" + SMALLER_CANTIDAD);
    }

    @Test
    @Transactional
    public void getAllCarritosByCantidadIsLessThanSomething() throws Exception {
        // Initialize the database
        carritoRepository.saveAndFlush(carrito);

        // Get all the carritoList where cantidad is less than DEFAULT_CANTIDAD
        defaultCarritoShouldNotBeFound("cantidad.lessThan=" + DEFAULT_CANTIDAD);

        // Get all the carritoList where cantidad is less than UPDATED_CANTIDAD
        defaultCarritoShouldBeFound("cantidad.lessThan=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    public void getAllCarritosByCantidadIsGreaterThanSomething() throws Exception {
        // Initialize the database
        carritoRepository.saveAndFlush(carrito);

        // Get all the carritoList where cantidad is greater than DEFAULT_CANTIDAD
        defaultCarritoShouldNotBeFound("cantidad.greaterThan=" + DEFAULT_CANTIDAD);

        // Get all the carritoList where cantidad is greater than SMALLER_CANTIDAD
        defaultCarritoShouldBeFound("cantidad.greaterThan=" + SMALLER_CANTIDAD);
    }


    @Test
    @Transactional
    public void getAllCarritosByValorIsEqualToSomething() throws Exception {
        // Initialize the database
        carritoRepository.saveAndFlush(carrito);

        // Get all the carritoList where valor equals to DEFAULT_VALOR
        defaultCarritoShouldBeFound("valor.equals=" + DEFAULT_VALOR);

        // Get all the carritoList where valor equals to UPDATED_VALOR
        defaultCarritoShouldNotBeFound("valor.equals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    public void getAllCarritosByValorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        carritoRepository.saveAndFlush(carrito);

        // Get all the carritoList where valor not equals to DEFAULT_VALOR
        defaultCarritoShouldNotBeFound("valor.notEquals=" + DEFAULT_VALOR);

        // Get all the carritoList where valor not equals to UPDATED_VALOR
        defaultCarritoShouldBeFound("valor.notEquals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    public void getAllCarritosByValorIsInShouldWork() throws Exception {
        // Initialize the database
        carritoRepository.saveAndFlush(carrito);

        // Get all the carritoList where valor in DEFAULT_VALOR or UPDATED_VALOR
        defaultCarritoShouldBeFound("valor.in=" + DEFAULT_VALOR + "," + UPDATED_VALOR);

        // Get all the carritoList where valor equals to UPDATED_VALOR
        defaultCarritoShouldNotBeFound("valor.in=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    public void getAllCarritosByValorIsNullOrNotNull() throws Exception {
        // Initialize the database
        carritoRepository.saveAndFlush(carrito);

        // Get all the carritoList where valor is not null
        defaultCarritoShouldBeFound("valor.specified=true");

        // Get all the carritoList where valor is null
        defaultCarritoShouldNotBeFound("valor.specified=false");
    }

    @Test
    @Transactional
    public void getAllCarritosByValorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        carritoRepository.saveAndFlush(carrito);

        // Get all the carritoList where valor is greater than or equal to DEFAULT_VALOR
        defaultCarritoShouldBeFound("valor.greaterThanOrEqual=" + DEFAULT_VALOR);

        // Get all the carritoList where valor is greater than or equal to UPDATED_VALOR
        defaultCarritoShouldNotBeFound("valor.greaterThanOrEqual=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    public void getAllCarritosByValorIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        carritoRepository.saveAndFlush(carrito);

        // Get all the carritoList where valor is less than or equal to DEFAULT_VALOR
        defaultCarritoShouldBeFound("valor.lessThanOrEqual=" + DEFAULT_VALOR);

        // Get all the carritoList where valor is less than or equal to SMALLER_VALOR
        defaultCarritoShouldNotBeFound("valor.lessThanOrEqual=" + SMALLER_VALOR);
    }

    @Test
    @Transactional
    public void getAllCarritosByValorIsLessThanSomething() throws Exception {
        // Initialize the database
        carritoRepository.saveAndFlush(carrito);

        // Get all the carritoList where valor is less than DEFAULT_VALOR
        defaultCarritoShouldNotBeFound("valor.lessThan=" + DEFAULT_VALOR);

        // Get all the carritoList where valor is less than UPDATED_VALOR
        defaultCarritoShouldBeFound("valor.lessThan=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    public void getAllCarritosByValorIsGreaterThanSomething() throws Exception {
        // Initialize the database
        carritoRepository.saveAndFlush(carrito);

        // Get all the carritoList where valor is greater than DEFAULT_VALOR
        defaultCarritoShouldNotBeFound("valor.greaterThan=" + DEFAULT_VALOR);

        // Get all the carritoList where valor is greater than SMALLER_VALOR
        defaultCarritoShouldBeFound("valor.greaterThan=" + SMALLER_VALOR);
    }


    @Test
    @Transactional
    public void getAllCarritosByProductoIsEqualToSomething() throws Exception {
        // Initialize the database
        carritoRepository.saveAndFlush(carrito);
        Producto producto = ProductoResourceIT.createEntity(em);
        em.persist(producto);
        em.flush();
        carrito.setProducto(producto);
        carritoRepository.saveAndFlush(carrito);
        Long productoId = producto.getId();

        // Get all the carritoList where producto equals to productoId
        defaultCarritoShouldBeFound("productoId.equals=" + productoId);

        // Get all the carritoList where producto equals to productoId + 1
        defaultCarritoShouldNotBeFound("productoId.equals=" + (productoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCarritoShouldBeFound(String filter) throws Exception {
        restCarritoMockMvc.perform(get("/api/carritos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carrito.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD.intValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.intValue())));

        // Check, that the count call also returns 1
        restCarritoMockMvc.perform(get("/api/carritos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCarritoShouldNotBeFound(String filter) throws Exception {
        restCarritoMockMvc.perform(get("/api/carritos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCarritoMockMvc.perform(get("/api/carritos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingCarrito() throws Exception {
        // Get the carrito
        restCarritoMockMvc.perform(get("/api/carritos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCarrito() throws Exception {
        // Initialize the database
        carritoRepository.saveAndFlush(carrito);

        int databaseSizeBeforeUpdate = carritoRepository.findAll().size();

        // Update the carrito
        Carrito updatedCarrito = carritoRepository.findById(carrito.getId()).get();
        // Disconnect from session so that the updates on updatedCarrito are not directly saved in db
        em.detach(updatedCarrito);
        updatedCarrito
            .cantidad(UPDATED_CANTIDAD)
            .valor(UPDATED_VALOR);
        CarritoDTO carritoDTO = carritoMapper.toDto(updatedCarrito);

        restCarritoMockMvc.perform(put("/api/carritos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carritoDTO)))
            .andExpect(status().isOk());

        // Validate the Carrito in the database
        List<Carrito> carritoList = carritoRepository.findAll();
        assertThat(carritoList).hasSize(databaseSizeBeforeUpdate);
        Carrito testCarrito = carritoList.get(carritoList.size() - 1);
        assertThat(testCarrito.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testCarrito.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    @Transactional
    public void updateNonExistingCarrito() throws Exception {
        int databaseSizeBeforeUpdate = carritoRepository.findAll().size();

        // Create the Carrito
        CarritoDTO carritoDTO = carritoMapper.toDto(carrito);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarritoMockMvc.perform(put("/api/carritos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carritoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Carrito in the database
        List<Carrito> carritoList = carritoRepository.findAll();
        assertThat(carritoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCarrito() throws Exception {
        // Initialize the database
        carritoRepository.saveAndFlush(carrito);

        int databaseSizeBeforeDelete = carritoRepository.findAll().size();

        // Delete the carrito
        restCarritoMockMvc.perform(delete("/api/carritos/{id}", carrito.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Carrito> carritoList = carritoRepository.findAll();
        assertThat(carritoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
