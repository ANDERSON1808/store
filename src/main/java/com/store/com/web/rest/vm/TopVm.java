package com.store.com.web.rest.vm;

import lombok.Data;


public interface TopVm {
    Long getId();
    String getNombre();
    Long getPrecio();
    Long getPrecioDescuento();
    Float getPorcentaje();
}
