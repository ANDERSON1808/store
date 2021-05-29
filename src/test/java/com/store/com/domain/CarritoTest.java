package com.store.com.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.store.com.web.rest.TestUtil;

public class CarritoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Carrito.class);
        Carrito carrito1 = new Carrito();
        carrito1.setId(1L);
        Carrito carrito2 = new Carrito();
        carrito2.setId(carrito1.getId());
        assertThat(carrito1).isEqualTo(carrito2);
        carrito2.setId(2L);
        assertThat(carrito1).isNotEqualTo(carrito2);
        carrito1.setId(null);
        assertThat(carrito1).isNotEqualTo(carrito2);
    }
}
