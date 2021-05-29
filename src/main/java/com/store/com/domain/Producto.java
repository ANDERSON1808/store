package com.store.com.domain;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Producto.
 */
@Data
@Entity
@Table(name = "producto")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "precio", nullable = false)
    private Long precio;

    @Column(name = "precio_descuento")
    private Long precioDescuento;

    @Column(name = "porcentaje")
    private Float porcentaje;

    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(mappedBy = "producto")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Archivo> archivos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Producto getId(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return nombre;
    }

    public Producto nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getPrecio() {
        return precio;
    }

    public Producto precio(Long precio) {
        this.precio = precio;
        return this;
    }

    public void setPrecio(Long precio) {
        this.precio = precio;
    }

    public Long getPrecioDescuento() {
        return precioDescuento;
    }

    public Producto precioDescuento(Long precioDescuento) {
        this.precioDescuento = precioDescuento;
        return this;
    }

    public void setPrecioDescuento(Long precioDescuento) {
        this.precioDescuento = precioDescuento;
    }

    public Float getPorcentaje() {
        return porcentaje;
    }

    public Producto porcentaje(Float porcentaje) {
        this.porcentaje = porcentaje;
        return this;
    }

    public void setPorcentaje(Float porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Producto descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Archivo> getArchivos() {
        return archivos;
    }

    public Producto archivos(Set<Archivo> archivos) {
        this.archivos = archivos;
        return this;
    }

    public Producto addArchivo(Archivo archivo) {
        this.archivos.add(archivo);
        archivo.setProducto(this);
        return this;
    }

    public Producto removeArchivo(Archivo archivo) {
        this.archivos.remove(archivo);
        archivo.setProducto(null);
        return this;
    }

    public void setArchivos(Set<Archivo> archivos) {
        this.archivos = archivos;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Producto)) {
            return false;
        }
        return id != null && id.equals(((Producto) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Producto{" +
                "id=" + getId() +
                ", nombre='" + getNombre() + "'" +
                ", precio=" + getPrecio() +
                ", precioDescuento=" + getPrecioDescuento() +
                ", porcentaje=" + getPorcentaje() +
                ", descripcion='" + getDescripcion() + "'" +
                "}";
    }
}
