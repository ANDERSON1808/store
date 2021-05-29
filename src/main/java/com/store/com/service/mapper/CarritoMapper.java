package com.store.com.service.mapper;


import com.store.com.domain.*;
import com.store.com.service.dto.CarritoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Carrito} and its DTO {@link CarritoDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProductoMapper.class})
public interface CarritoMapper extends EntityMapper<CarritoDTO, Carrito> {

    @Mapping(source = "producto.id", target = "productoId")
    CarritoDTO toDto(Carrito carrito);

    @Mapping(source = "productoId", target = "producto")
    Carrito toEntity(CarritoDTO carritoDTO);

    default Carrito fromId(Long id) {
        if (id == null) {
            return null;
        }
        Carrito carrito = new Carrito();
        carrito.setId(id);
        return carrito;
    }
}
