package com.store.com.service.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Set;

/**
 * A DTO for the {@link com.store.com.domain.Producto} entity.
 */
@Data
public class ProductoDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String nombre;

    @NotNull
    private Long precio;

    private Long precioDescuento;

    private Float porcentaje;

    private String descripcion;

    private Set<ArchivoDTO>fotos;

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

    public Long getPrecio() {
        return precio;
    }

    public void setPrecio(Long precio) {
        this.precio = precio;
    }

    public Long getPrecioDescuento() {
        return precioDescuento;
    }

    public void setPrecioDescuento(Long precioDescuento) {
        this.precioDescuento = precioDescuento;
    }

    public Float getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Float porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductoDTO)) {
            return false;
        }

        return id != null && id.equals(((ProductoDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductoDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", precio=" + getPrecio() +
            ", precioDescuento=" + getPrecioDescuento() +
            ", porcentaje=" + getPorcentaje() +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
