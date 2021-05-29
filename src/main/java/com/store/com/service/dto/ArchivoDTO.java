package com.store.com.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.store.com.domain.Archivo} entity.
 */
public class ArchivoDTO implements Serializable {
    
    private Long id;

    @NotNull
    @Size(max = 100)
    private String nombre;

    private String path;

    private String ubicacion;


    private Long productoId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArchivoDTO)) {
            return false;
        }

        return id != null && id.equals(((ArchivoDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArchivoDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", path='" + getPath() + "'" +
            ", ubicacion='" + getUbicacion() + "'" +
            ", productoId=" + getProductoId() +
            "}";
    }
}
