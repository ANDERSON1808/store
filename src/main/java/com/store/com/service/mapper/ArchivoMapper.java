package com.store.com.service.mapper;


import com.store.com.domain.*;
import com.store.com.service.dto.ArchivoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Archivo} and its DTO {@link ArchivoDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProductoMapper.class})
public interface ArchivoMapper extends EntityMapper<ArchivoDTO, Archivo> {

    @Mapping(source = "producto.id", target = "productoId")
    ArchivoDTO toDto(Archivo archivo);

    @Mapping(source = "productoId", target = "producto")
    Archivo toEntity(ArchivoDTO archivoDTO);

    default Archivo fromId(Long id) {
        if (id == null) {
            return null;
        }
        Archivo archivo = new Archivo();
        archivo.setId(id);
        return archivo;
    }
}
