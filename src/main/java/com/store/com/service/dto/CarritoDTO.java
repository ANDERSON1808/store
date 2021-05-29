package com.store.com.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.store.com.domain.Carrito} entity.
 */
public class CarritoDTO implements Serializable {
    
    private Long id;

    private Long cantidad;

    private Long valor;


    private Long productoId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public Long getValor() {
        return valor;
    }

    public void setValor(Long valor) {
        this.valor = valor;
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
        if (!(o instanceof CarritoDTO)) {
            return false;
        }

        return id != null && id.equals(((CarritoDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarritoDTO{" +
            "id=" + getId() +
            ", cantidad=" + getCantidad() +
            ", valor=" + getValor() +
            ", productoId=" + getProductoId() +
            "}";
    }
}
