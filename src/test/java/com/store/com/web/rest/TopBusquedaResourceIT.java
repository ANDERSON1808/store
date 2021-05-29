package com.store.com.web.rest;

import com.store.com.StoreApp;
import com.store.com.domain.TopBusqueda;
import com.store.com.domain.Producto;
import com.store.com.repository.TopBusquedaRepository;
import com.store.com.service.TopBusquedaService;
import com.store.com.service.dto.TopBusquedaDTO;
import com.store.com.service.mapper.TopBusquedaMapper;

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
 */
@SpringBootTest(classes = StoreApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TopBusquedaResourceIT {

    @Autowired
    private TopBusquedaRepository topBusquedaRepository;

    @Autowired
    private TopBusquedaMapper topBusquedaMapper;

    @Autowired
    private TopBusquedaService topBusquedaService;


    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTopBusquedaMockMvc;

    private TopBusqueda topBusqueda;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TopBusqueda createEntity(EntityManager em) {
        TopBusqueda topBusqueda = new TopBusqueda();
        return topBusqueda;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TopBusqueda createUpdatedEntity(EntityManager em) {
        TopBusqueda topBusqueda = new TopBusqueda();
        return topBusqueda;
    }

    @BeforeEach
    public void initTest() {
        topBusqueda = createEntity(em);
    }

    @Test
    @Transactional
    public void createTopBusqueda() throws Exception {
        int databaseSizeBeforeCreate = topBusquedaRepository.findAll().size();
        // Create the TopBusqueda
        TopBusquedaDTO topBusquedaDTO = topBusquedaMapper.toDto(topBusqueda);
        restTopBusquedaMockMvc.perform(post("/api/top-busquedas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(topBusquedaDTO)))
            .andExpect(status().isCreated());

        // Validate the TopBusqueda in the database
        List<TopBusqueda> topBusquedaList = topBusquedaRepository.findAll();
        assertThat(topBusquedaList).hasSize(databaseSizeBeforeCreate + 1);
        TopBusqueda testTopBusqueda = topBusquedaList.get(topBusquedaList.size() - 1);
    }

    @Test
    @Transactional
    public void createTopBusquedaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = topBusquedaRepository.findAll().size();

        // Create the TopBusqueda with an existing ID
        topBusqueda.setId(1L);
        TopBusquedaDTO topBusquedaDTO = topBusquedaMapper.toDto(topBusqueda);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTopBusquedaMockMvc.perform(post("/api/top-busquedas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(topBusquedaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TopBusqueda in the database
        List<TopBusqueda> topBusquedaList = topBusquedaRepository.findAll();
        assertThat(topBusquedaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTopBusquedas() throws Exception {
        // Initialize the database
        topBusquedaRepository.saveAndFlush(topBusqueda);

        // Get all the topBusquedaList
        restTopBusquedaMockMvc.perform(get("/api/top-busquedas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(topBusqueda.getId().intValue())));
    }

    @Test
    @Transactional
    public void getTopBusqueda() throws Exception {
        // Initialize the database
        topBusquedaRepository.saveAndFlush(topBusqueda);

        // Get the topBusqueda
        restTopBusquedaMockMvc.perform(get("/api/top-busquedas/{id}", topBusqueda.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(topBusqueda.getId().intValue()));
    }


    @Test
    @Transactional
    public void getTopBusquedasByIdFiltering() throws Exception {
        // Initialize the database
        topBusquedaRepository.saveAndFlush(topBusqueda);

        Long id = topBusqueda.getId();

        defaultTopBusquedaShouldBeFound("id.equals=" + id);
        defaultTopBusquedaShouldNotBeFound("id.notEquals=" + id);

        defaultTopBusquedaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTopBusquedaShouldNotBeFound("id.greaterThan=" + id);

        defaultTopBusquedaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTopBusquedaShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTopBusquedasByProductoIsEqualToSomething() throws Exception {
        // Initialize the database
        topBusquedaRepository.saveAndFlush(topBusqueda);
        Producto producto = ProductoResourceIT.createEntity(em);
        em.persist(producto);
        em.flush();
        topBusqueda.setProducto(producto);
        topBusquedaRepository.saveAndFlush(topBusqueda);
        Long productoId = producto.getId();

        // Get all the topBusquedaList where producto equals to productoId
        defaultTopBusquedaShouldBeFound("productoId.equals=" + productoId);

        // Get all the topBusquedaList where producto equals to productoId + 1
        defaultTopBusquedaShouldNotBeFound("productoId.equals=" + (productoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTopBusquedaShouldBeFound(String filter) throws Exception {
        restTopBusquedaMockMvc.perform(get("/api/top-busquedas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(topBusqueda.getId().intValue())));

        // Check, that the count call also returns 1
        restTopBusquedaMockMvc.perform(get("/api/top-busquedas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTopBusquedaShouldNotBeFound(String filter) throws Exception {
        restTopBusquedaMockMvc.perform(get("/api/top-busquedas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTopBusquedaMockMvc.perform(get("/api/top-busquedas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingTopBusqueda() throws Exception {
        // Get the topBusqueda
        restTopBusquedaMockMvc.perform(get("/api/top-busquedas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTopBusqueda() throws Exception {
        // Initialize the database
        topBusquedaRepository.saveAndFlush(topBusqueda);

        int databaseSizeBeforeUpdate = topBusquedaRepository.findAll().size();

        // Update the topBusqueda
        TopBusqueda updatedTopBusqueda = topBusquedaRepository.findById(topBusqueda.getId()).get();
        // Disconnect from session so that the updates on updatedTopBusqueda are not directly saved in db
        em.detach(updatedTopBusqueda);
        TopBusquedaDTO topBusquedaDTO = topBusquedaMapper.toDto(updatedTopBusqueda);

        restTopBusquedaMockMvc.perform(put("/api/top-busquedas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(topBusquedaDTO)))
            .andExpect(status().isOk());

        // Validate the TopBusqueda in the database
        List<TopBusqueda> topBusquedaList = topBusquedaRepository.findAll();
        assertThat(topBusquedaList).hasSize(databaseSizeBeforeUpdate);
        TopBusqueda testTopBusqueda = topBusquedaList.get(topBusquedaList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingTopBusqueda() throws Exception {
        int databaseSizeBeforeUpdate = topBusquedaRepository.findAll().size();

        // Create the TopBusqueda
        TopBusquedaDTO topBusquedaDTO = topBusquedaMapper.toDto(topBusqueda);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTopBusquedaMockMvc.perform(put("/api/top-busquedas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(topBusquedaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TopBusqueda in the database
        List<TopBusqueda> topBusquedaList = topBusquedaRepository.findAll();
        assertThat(topBusquedaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTopBusqueda() throws Exception {
        // Initialize the database
        topBusquedaRepository.saveAndFlush(topBusqueda);

        int databaseSizeBeforeDelete = topBusquedaRepository.findAll().size();

        // Delete the topBusqueda
        restTopBusquedaMockMvc.perform(delete("/api/top-busquedas/{id}", topBusqueda.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TopBusqueda> topBusquedaList = topBusquedaRepository.findAll();
        assertThat(topBusquedaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
