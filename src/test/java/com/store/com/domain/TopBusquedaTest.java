package com.store.com.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.store.com.web.rest.TestUtil;

public class TopBusquedaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TopBusqueda.class);
        TopBusqueda topBusqueda1 = new TopBusqueda();
        topBusqueda1.setId(1L);
        TopBusqueda topBusqueda2 = new TopBusqueda();
        topBusqueda2.setId(topBusqueda1.getId());
        assertThat(topBusqueda1).isEqualTo(topBusqueda2);
        topBusqueda2.setId(2L);
        assertThat(topBusqueda1).isNotEqualTo(topBusqueda2);
        topBusqueda1.setId(null);
        assertThat(topBusqueda1).isNotEqualTo(topBusqueda2);
    }
}
