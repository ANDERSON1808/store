package com.store.com.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.store.com.domain.Archivo;
import com.store.com.domain.*; // for static metamodels
import com.store.com.repository.ArchivoRepository;
import com.store.com.service.dto.ArchivoCriteria;
import com.store.com.service.dto.ArchivoDTO;
import com.store.com.service.mapper.ArchivoMapper;

/**
 * Service for executing complex queries for {@link Archivo} entities in the database.
 * The main input is a {@link ArchivoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ArchivoDTO} or a {@link Page} of {@link ArchivoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ArchivoQueryService extends QueryService<Archivo> {

    private final Logger log = LoggerFactory.getLogger(ArchivoQueryService.class);

    private final ArchivoRepository archivoRepository;

    private final ArchivoMapper archivoMapper;

    public ArchivoQueryService(ArchivoRepository archivoRepository, ArchivoMapper archivoMapper) {
        this.archivoRepository = archivoRepository;
        this.archivoMapper = archivoMapper;
    }

    /**
     * Return a {@link List} of {@link ArchivoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ArchivoDTO> findByCriteria(ArchivoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Archivo> specification = createSpecification(criteria);
        return archivoMapper.toDto(archivoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ArchivoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ArchivoDTO> findByCriteria(ArchivoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Archivo> specification = createSpecification(criteria);
        return archivoRepository.findAll(specification, page)
            .map(archivoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ArchivoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Archivo> specification = createSpecification(criteria);
        return archivoRepository.count(specification);
    }

    /**
     * Function to convert {@link ArchivoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Archivo> createSpecification(ArchivoCriteria criteria) {
        Specification<Archivo> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Archivo_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Archivo_.nombre));
            }
            if (criteria.getPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPath(), Archivo_.path));
            }
            if (criteria.getUbicacion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUbicacion(), Archivo_.ubicacion));
            }
            if (criteria.getProductoId() != null) {
                specification = specification.and(buildSpecification(criteria.getProductoId(),
                    root -> root.join(Archivo_.producto, JoinType.LEFT).get(Producto_.id)));
            }
        }
        return specification;
    }
}
