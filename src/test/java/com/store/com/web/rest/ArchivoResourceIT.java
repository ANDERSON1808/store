package com.store.com.web.rest;

import com.store.com.StoreApp;
import com.store.com.domain.Archivo;
import com.store.com.domain.Producto;
import com.store.com.repository.ArchivoRepository;
import com.store.com.service.ArchivoService;
import com.store.com.service.dto.ArchivoDTO;
import com.store.com.service.mapper.ArchivoMapper;
import com.store.com.service.dto.ArchivoCriteria;
import com.store.com.service.ArchivoQueryService;

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
public class ArchivoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_UBICACION = "AAAAAAAAAA";
    private static final String UPDATED_UBICACION = "BBBBBBBBBB";

    @Autowired
    private ArchivoRepository archivoRepository;

    @Autowired
    private ArchivoMapper archivoMapper;

    @Autowired
    private ArchivoService archivoService;

    @Autowired
    private ArchivoQueryService archivoQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArchivoMockMvc;

    private Archivo archivo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Archivo createEntity(EntityManager em) {
        Archivo archivo = new Archivo()
            .nombre(DEFAULT_NOMBRE)
            .path(DEFAULT_PATH)
            .ubicacion(DEFAULT_UBICACION);
        return archivo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Archivo createUpdatedEntity(EntityManager em) {
        Archivo archivo = new Archivo()
            .nombre(UPDATED_NOMBRE)
            .path(UPDATED_PATH)
            .ubicacion(UPDATED_UBICACION);
        return archivo;
    }

    @BeforeEach
    public void initTest() {
        archivo = createEntity(em);
    }

    @Test
    @Transactional
    public void createArchivo() throws Exception {
        int databaseSizeBeforeCreate = archivoRepository.findAll().size();
        // Create the Archivo
        ArchivoDTO archivoDTO = archivoMapper.toDto(archivo);
        restArchivoMockMvc.perform(post("/api/archivos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(archivoDTO)))
            .andExpect(status().isCreated());

        // Validate the Archivo in the database
        List<Archivo> archivoList = archivoRepository.findAll();
        assertThat(archivoList).hasSize(databaseSizeBeforeCreate + 1);
        Archivo testArchivo = archivoList.get(archivoList.size() - 1);
        assertThat(testArchivo.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testArchivo.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testArchivo.getUbicacion()).isEqualTo(DEFAULT_UBICACION);
    }

    @Test
    @Transactional
    public void createArchivoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = archivoRepository.findAll().size();

        // Create the Archivo with an existing ID
        archivo.setId(1L);
        ArchivoDTO archivoDTO = archivoMapper.toDto(archivo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArchivoMockMvc.perform(post("/api/archivos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(archivoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Archivo in the database
        List<Archivo> archivoList = archivoRepository.findAll();
        assertThat(archivoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = archivoRepository.findAll().size();
        // set the field null
        archivo.setNombre(null);

        // Create the Archivo, which fails.
        ArchivoDTO archivoDTO = archivoMapper.toDto(archivo);


        restArchivoMockMvc.perform(post("/api/archivos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(archivoDTO)))
            .andExpect(status().isBadRequest());

        List<Archivo> archivoList = archivoRepository.findAll();
        assertThat(archivoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllArchivos() throws Exception {
        // Initialize the database
        archivoRepository.saveAndFlush(archivo);

        // Get all the archivoList
        restArchivoMockMvc.perform(get("/api/archivos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(archivo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].ubicacion").value(hasItem(DEFAULT_UBICACION)));
    }

    @Test
    @Transactional
    public void getArchivo() throws Exception {
        // Initialize the database
        archivoRepository.saveAndFlush(archivo);

        // Get the archivo
        restArchivoMockMvc.perform(get("/api/archivos/{id}", archivo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(archivo.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH))
            .andExpect(jsonPath("$.ubicacion").value(DEFAULT_UBICACION));
    }


    @Test
    @Transactional
    public void getArchivosByIdFiltering() throws Exception {
        // Initialize the database
        archivoRepository.saveAndFlush(archivo);

        Long id = archivo.getId();

        defaultArchivoShouldBeFound("id.equals=" + id);
        defaultArchivoShouldNotBeFound("id.notEquals=" + id);

        defaultArchivoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultArchivoShouldNotBeFound("id.greaterThan=" + id);

        defaultArchivoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultArchivoShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllArchivosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        archivoRepository.saveAndFlush(archivo);

        // Get all the archivoList where nombre equals to DEFAULT_NOMBRE
        defaultArchivoShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the archivoList where nombre equals to UPDATED_NOMBRE
        defaultArchivoShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllArchivosByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        archivoRepository.saveAndFlush(archivo);

        // Get all the archivoList where nombre not equals to DEFAULT_NOMBRE
        defaultArchivoShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the archivoList where nombre not equals to UPDATED_NOMBRE
        defaultArchivoShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllArchivosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        archivoRepository.saveAndFlush(archivo);

        // Get all the archivoList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultArchivoShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the archivoList where nombre equals to UPDATED_NOMBRE
        defaultArchivoShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllArchivosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        archivoRepository.saveAndFlush(archivo);

        // Get all the archivoList where nombre is not null
        defaultArchivoShouldBeFound("nombre.specified=true");

        // Get all the archivoList where nombre is null
        defaultArchivoShouldNotBeFound("nombre.specified=false");
    }
                @Test
    @Transactional
    public void getAllArchivosByNombreContainsSomething() throws Exception {
        // Initialize the database
        archivoRepository.saveAndFlush(archivo);

        // Get all the archivoList where nombre contains DEFAULT_NOMBRE
        defaultArchivoShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the archivoList where nombre contains UPDATED_NOMBRE
        defaultArchivoShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllArchivosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        archivoRepository.saveAndFlush(archivo);

        // Get all the archivoList where nombre does not contain DEFAULT_NOMBRE
        defaultArchivoShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the archivoList where nombre does not contain UPDATED_NOMBRE
        defaultArchivoShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }


    @Test
    @Transactional
    public void getAllArchivosByPathIsEqualToSomething() throws Exception {
        // Initialize the database
        archivoRepository.saveAndFlush(archivo);

        // Get all the archivoList where path equals to DEFAULT_PATH
        defaultArchivoShouldBeFound("path.equals=" + DEFAULT_PATH);

        // Get all the archivoList where path equals to UPDATED_PATH
        defaultArchivoShouldNotBeFound("path.equals=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    public void getAllArchivosByPathIsNotEqualToSomething() throws Exception {
        // Initialize the database
        archivoRepository.saveAndFlush(archivo);

        // Get all the archivoList where path not equals to DEFAULT_PATH
        defaultArchivoShouldNotBeFound("path.notEquals=" + DEFAULT_PATH);

        // Get all the archivoList where path not equals to UPDATED_PATH
        defaultArchivoShouldBeFound("path.notEquals=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    public void getAllArchivosByPathIsInShouldWork() throws Exception {
        // Initialize the database
        archivoRepository.saveAndFlush(archivo);

        // Get all the archivoList where path in DEFAULT_PATH or UPDATED_PATH
        defaultArchivoShouldBeFound("path.in=" + DEFAULT_PATH + "," + UPDATED_PATH);

        // Get all the archivoList where path equals to UPDATED_PATH
        defaultArchivoShouldNotBeFound("path.in=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    public void getAllArchivosByPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        archivoRepository.saveAndFlush(archivo);

        // Get all the archivoList where path is not null
        defaultArchivoShouldBeFound("path.specified=true");

        // Get all the archivoList where path is null
        defaultArchivoShouldNotBeFound("path.specified=false");
    }
                @Test
    @Transactional
    public void getAllArchivosByPathContainsSomething() throws Exception {
        // Initialize the database
        archivoRepository.saveAndFlush(archivo);

        // Get all the archivoList where path contains DEFAULT_PATH
        defaultArchivoShouldBeFound("path.contains=" + DEFAULT_PATH);

        // Get all the archivoList where path contains UPDATED_PATH
        defaultArchivoShouldNotBeFound("path.contains=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    public void getAllArchivosByPathNotContainsSomething() throws Exception {
        // Initialize the database
        archivoRepository.saveAndFlush(archivo);

        // Get all the archivoList where path does not contain DEFAULT_PATH
        defaultArchivoShouldNotBeFound("path.doesNotContain=" + DEFAULT_PATH);

        // Get all the archivoList where path does not contain UPDATED_PATH
        defaultArchivoShouldBeFound("path.doesNotContain=" + UPDATED_PATH);
    }


    @Test
    @Transactional
    public void getAllArchivosByUbicacionIsEqualToSomething() throws Exception {
        // Initialize the database
        archivoRepository.saveAndFlush(archivo);

        // Get all the archivoList where ubicacion equals to DEFAULT_UBICACION
        defaultArchivoShouldBeFound("ubicacion.equals=" + DEFAULT_UBICACION);

        // Get all the archivoList where ubicacion equals to UPDATED_UBICACION
        defaultArchivoShouldNotBeFound("ubicacion.equals=" + UPDATED_UBICACION);
    }

    @Test
    @Transactional
    public void getAllArchivosByUbicacionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        archivoRepository.saveAndFlush(archivo);

        // Get all the archivoList where ubicacion not equals to DEFAULT_UBICACION
        defaultArchivoShouldNotBeFound("ubicacion.notEquals=" + DEFAULT_UBICACION);

        // Get all the archivoList where ubicacion not equals to UPDATED_UBICACION
        defaultArchivoShouldBeFound("ubicacion.notEquals=" + UPDATED_UBICACION);
    }

    @Test
    @Transactional
    public void getAllArchivosByUbicacionIsInShouldWork() throws Exception {
        // Initialize the database
        archivoRepository.saveAndFlush(archivo);

        // Get all the archivoList where ubicacion in DEFAULT_UBICACION or UPDATED_UBICACION
        defaultArchivoShouldBeFound("ubicacion.in=" + DEFAULT_UBICACION + "," + UPDATED_UBICACION);

        // Get all the archivoList where ubicacion equals to UPDATED_UBICACION
        defaultArchivoShouldNotBeFound("ubicacion.in=" + UPDATED_UBICACION);
    }

    @Test
    @Transactional
    public void getAllArchivosByUbicacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        archivoRepository.saveAndFlush(archivo);

        // Get all the archivoList where ubicacion is not null
        defaultArchivoShouldBeFound("ubicacion.specified=true");

        // Get all the archivoList where ubicacion is null
        defaultArchivoShouldNotBeFound("ubicacion.specified=false");
    }
                @Test
    @Transactional
    public void getAllArchivosByUbicacionContainsSomething() throws Exception {
        // Initialize the database
        archivoRepository.saveAndFlush(archivo);

        // Get all the archivoList where ubicacion contains DEFAULT_UBICACION
        defaultArchivoShouldBeFound("ubicacion.contains=" + DEFAULT_UBICACION);

        // Get all the archivoList where ubicacion contains UPDATED_UBICACION
        defaultArchivoShouldNotBeFound("ubicacion.contains=" + UPDATED_UBICACION);
    }

    @Test
    @Transactional
    public void getAllArchivosByUbicacionNotContainsSomething() throws Exception {
        // Initialize the database
        archivoRepository.saveAndFlush(archivo);

        // Get all the archivoList where ubicacion does not contain DEFAULT_UBICACION
        defaultArchivoShouldNotBeFound("ubicacion.doesNotContain=" + DEFAULT_UBICACION);

        // Get all the archivoList where ubicacion does not contain UPDATED_UBICACION
        defaultArchivoShouldBeFound("ubicacion.doesNotContain=" + UPDATED_UBICACION);
    }


    @Test
    @Transactional
    public void getAllArchivosByProductoIsEqualToSomething() throws Exception {
        // Initialize the database
        archivoRepository.saveAndFlush(archivo);
        Producto producto = ProductoResourceIT.createEntity(em);
        em.persist(producto);
        em.flush();
        archivo.setProducto(producto);
        archivoRepository.saveAndFlush(archivo);
        Long productoId = producto.getId();

        // Get all the archivoList where producto equals to productoId
        defaultArchivoShouldBeFound("productoId.equals=" + productoId);

        // Get all the archivoList where producto equals to productoId + 1
        defaultArchivoShouldNotBeFound("productoId.equals=" + (productoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultArchivoShouldBeFound(String filter) throws Exception {
        restArchivoMockMvc.perform(get("/api/archivos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(archivo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].ubicacion").value(hasItem(DEFAULT_UBICACION)));

        // Check, that the count call also returns 1
        restArchivoMockMvc.perform(get("/api/archivos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultArchivoShouldNotBeFound(String filter) throws Exception {
        restArchivoMockMvc.perform(get("/api/archivos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restArchivoMockMvc.perform(get("/api/archivos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingArchivo() throws Exception {
        // Get the archivo
        restArchivoMockMvc.perform(get("/api/archivos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArchivo() throws Exception {
        // Initialize the database
        archivoRepository.saveAndFlush(archivo);

        int databaseSizeBeforeUpdate = archivoRepository.findAll().size();

        // Update the archivo
        Archivo updatedArchivo = archivoRepository.findById(archivo.getId()).get();
        // Disconnect from session so that the updates on updatedArchivo are not directly saved in db
        em.detach(updatedArchivo);
        updatedArchivo
            .nombre(UPDATED_NOMBRE)
            .path(UPDATED_PATH)
            .ubicacion(UPDATED_UBICACION);
        ArchivoDTO archivoDTO = archivoMapper.toDto(updatedArchivo);

        restArchivoMockMvc.perform(put("/api/archivos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(archivoDTO)))
            .andExpect(status().isOk());

        // Validate the Archivo in the database
        List<Archivo> archivoList = archivoRepository.findAll();
        assertThat(archivoList).hasSize(databaseSizeBeforeUpdate);
        Archivo testArchivo = archivoList.get(archivoList.size() - 1);
        assertThat(testArchivo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testArchivo.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testArchivo.getUbicacion()).isEqualTo(UPDATED_UBICACION);
    }

    @Test
    @Transactional
    public void updateNonExistingArchivo() throws Exception {
        int databaseSizeBeforeUpdate = archivoRepository.findAll().size();

        // Create the Archivo
        ArchivoDTO archivoDTO = archivoMapper.toDto(archivo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArchivoMockMvc.perform(put("/api/archivos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(archivoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Archivo in the database
        List<Archivo> archivoList = archivoRepository.findAll();
        assertThat(archivoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteArchivo() throws Exception {
        // Initialize the database
        archivoRepository.saveAndFlush(archivo);

        int databaseSizeBeforeDelete = archivoRepository.findAll().size();

        // Delete the archivo
        restArchivoMockMvc.perform(delete("/api/archivos/{id}", archivo.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Archivo> archivoList = archivoRepository.findAll();
        assertThat(archivoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
