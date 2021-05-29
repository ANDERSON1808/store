package com.store.com.service;

import com.store.com.domain.Carrito;
import com.store.com.repository.CarritoRepository;
import com.store.com.service.dto.CarritoDTO;
import com.store.com.service.mapper.CarritoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Carrito}.
 */
@Service
@Transactional
public class CarritoService {

    private final Logger log = LoggerFactory.getLogger(CarritoService.class);

    private final CarritoRepository carritoRepository;

    private final CarritoMapper carritoMapper;

    public CarritoService(CarritoRepository carritoRepository, CarritoMapper carritoMapper) {
        this.carritoRepository = carritoRepository;
        this.carritoMapper = carritoMapper;
    }

    /**
     * Save a carrito.
     *
     * @param carritoDTO the entity to save.
     * @return the persisted entity.
     */
    public CarritoDTO save(CarritoDTO carritoDTO) {
        log.debug("Request to save Carrito : {}", carritoDTO);
        Carrito carrito = carritoMapper.toEntity(carritoDTO);
        carrito = carritoRepository.save(carrito);
        return carritoMapper.toDto(carrito);
    }

    /**
     * Get all the carritos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CarritoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Carritos");
        return carritoRepository.findAll(pageable)
                .map(carritoMapper::toDto);
    }

    public List<CarritoDTO> saveList(List<CarritoDTO> list) {
        return carritoRepository.saveAll(carritoMapper.toEntity(list))
                .stream().map(carritoMapper::toDto)
                .collect(Collectors.toList());
    }

}
