package com.store.com.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.store.com.service.dto.ArchivoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.store.com.domain.Producto;
import com.store.com.domain.*; // for static metamodels
import com.store.com.repository.ProductoRepository;
import com.store.com.service.dto.ProductoCriteria;
import com.store.com.service.dto.ProductoDTO;
import com.store.com.service.mapper.ProductoMapper;

/**
 * Service for executing complex queries for {@link Producto} entities in the database.
 * The main input is a {@link ProductoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductoDTO} or a {@link Page} of {@link ProductoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductoQueryService extends QueryService<Producto> {

    private final Logger log = LoggerFactory.getLogger(ProductoQueryService.class);

    private final ProductoRepository productoRepository;

    private final ProductoMapper productoMapper;

    private final ArchivoService archivoService;

    public ProductoQueryService(ProductoRepository productoRepository, ProductoMapper productoMapper, ArchivoService archivoService) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
        this.archivoService = archivoService;
    }

    /**
     * Return a {@link List} of {@link ProductoDTO} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductoDTO> findByCriteria(ProductoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Producto> specification = createSpecification(criteria);
        return productoMapper.toDto(productoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductoDTO} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductoDTO> findByCriteria(ProductoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Producto> specification = createSpecification(criteria);
        return productoRepository.findAll(specification, page)
                .map(productoMapper::toDto);
    }



    /**
     * Return the number of matching entities in the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Producto> specification = createSpecification(criteria);
        return productoRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductoCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Producto> createSpecification(ProductoCriteria criteria) {
        Specification<Producto> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Producto_.nombre));
            }
        }
        return specification;
    }
}
