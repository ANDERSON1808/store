package com.store.com.repository;

import com.store.com.domain.TopBusqueda;

import com.store.com.web.rest.vm.TopVm;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the TopBusqueda entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TopBusquedaRepository extends JpaRepository<TopBusqueda, Long>, JpaSpecificationExecutor<TopBusqueda> {
    /**
     * Gets all top productos.
     *
     * @return the all top productos
     */
    @Query(value = "select p.id, p.nombre, p.precio, p.precio_descuento, p.porcentaje\n" +
            "from producto p\n" +
            "         join top_busqueda tb on p.id = tb.producto_id\n" +
            "group by p.id, p.nombre, p.precio, p.precio_descuento, p.porcentaje\n" +
            "limit 10", nativeQuery = true)
    List<TopVm> getAllTopProductos();

}
