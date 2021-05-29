package com.store.com.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.store.com.domain.Archivo} entity. This class is used
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /archivos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ArchivoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter path;

    private StringFilter ubicacion;

    private LongFilter productoId;

    public ArchivoCriteria() {
    }

    public ArchivoCriteria(ArchivoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.path = other.path == null ? null : other.path.copy();
        this.ubicacion = other.ubicacion == null ? null : other.ubicacion.copy();
        this.productoId = other.productoId == null ? null : other.productoId.copy();
    }

    @Override
    public ArchivoCriteria copy() {
        return new ArchivoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNombre() {
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getPath() {
        return path;
    }

    public void setPath(StringFilter path) {
        this.path = path;
    }

    public StringFilter getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(StringFilter ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LongFilter getProductoId() {
        return productoId;
    }

    public void setProductoId(LongFilter productoId) {
        this.productoId = productoId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ArchivoCriteria that = (ArchivoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(path, that.path) &&
            Objects.equals(ubicacion, that.ubicacion) &&
            Objects.equals(productoId, that.productoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nombre,
        path,
        ubicacion,
        productoId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArchivoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nombre != null ? "nombre=" + nombre + ", " : "") +
                (path != null ? "path=" + path + ", " : "") +
                (ubicacion != null ? "ubicacion=" + ubicacion + ", " : "") +
                (productoId != null ? "productoId=" + productoId + ", " : "") +
            "}";
    }

}
