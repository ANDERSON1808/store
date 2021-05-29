package com.store.com.service.mapper;


import com.store.com.domain.*;
import com.store.com.service.dto.TopBusquedaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TopBusqueda} and its DTO {@link TopBusquedaDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProductoMapper.class})
public interface TopBusquedaMapper extends EntityMapper<TopBusquedaDTO, TopBusqueda> {

    @Mapping(source = "producto.id", target = "productoId")
    TopBusquedaDTO toDto(TopBusqueda topBusqueda);

    @Mapping(source = "productoId", target = "producto")
    TopBusqueda toEntity(TopBusquedaDTO topBusquedaDTO);

    default TopBusqueda fromId(Long id) {
        if (id == null) {
            return null;
        }
        TopBusqueda topBusqueda = new TopBusqueda();
        topBusqueda.setId(id);
        return topBusqueda;
    }
}
