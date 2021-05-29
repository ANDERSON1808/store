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
 * Criteria class for the {@link com.store.com.domain.Carrito} entity. This class is used
 * in {@link com.store.com.web.rest.CarritoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /carritos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CarritoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter cantidad;

    private LongFilter valor;

    private LongFilter productoId;

    public CarritoCriteria() {
    }

    public CarritoCriteria(CarritoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.cantidad = other.cantidad == null ? null : other.cantidad.copy();
        this.valor = other.valor == null ? null : other.valor.copy();
        this.productoId = other.productoId == null ? null : other.productoId.copy();
    }

    @Override
    public CarritoCriteria copy() {
        return new CarritoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getCantidad() {
        return cantidad;
    }

    public void setCantidad(LongFilter cantidad) {
        this.cantidad = cantidad;
    }

    public LongFilter getValor() {
        return valor;
    }

    public void setValor(LongFilter valor) {
        this.valor = valor;
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
        final CarritoCriteria that = (CarritoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(cantidad, that.cantidad) &&
            Objects.equals(valor, that.valor) &&
            Objects.equals(productoId, that.productoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        cantidad,
        valor,
        productoId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarritoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (cantidad != null ? "cantidad=" + cantidad + ", " : "") +
                (valor != null ? "valor=" + valor + ", " : "") +
                (productoId != null ? "productoId=" + productoId + ", " : "") +
            "}";
    }

}
