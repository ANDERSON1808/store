package com.store.com.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.store.com.domain.TopBusqueda} entity.
 */
public class TopBusquedaDTO implements Serializable {
    
    private Long id;


    private Long productoId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(o instanceof TopBusquedaDTO)) {
            return false;
        }

        return id != null && id.equals(((TopBusquedaDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TopBusquedaDTO{" +
            "id=" + getId() +
            ", productoId=" + getProductoId() +
            "}";
    }
}
