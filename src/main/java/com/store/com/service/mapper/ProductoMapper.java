package com.store.com.service.mapper;


import com.store.com.domain.*;
import com.store.com.service.dto.ProductoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Producto} and its DTO {@link ProductoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProductoMapper extends EntityMapper<ProductoDTO, Producto> {


    @Mapping(target = "archivos", ignore = true)
    @Mapping(target = "removeArchivo", ignore = true)
    Producto toEntity(ProductoDTO productoDTO);

    default Producto fromId(Long id) {
        if (id == null) {
            return null;
        }
        Producto producto = new Producto();
        producto.setId(id);
        return producto;
    }
}
