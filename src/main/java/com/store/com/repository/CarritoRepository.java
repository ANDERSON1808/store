package com.store.com.repository;

import com.store.com.domain.Carrito;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Carrito entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long>, JpaSpecificationExecutor<Carrito> {
}
