package com.store.com.repository;

import com.store.com.domain.Archivo;

import com.store.com.domain.Producto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Archivo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArchivoRepository extends JpaRepository<Archivo, Long>, JpaSpecificationExecutor<Archivo> {
    Optional<List<Archivo>>getAllByProducto(Producto producto);
}
