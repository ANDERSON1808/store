package com.store.com.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.store.com.web.rest.TestUtil;

public class TopBusquedaDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TopBusquedaDTO.class);
        TopBusquedaDTO topBusquedaDTO1 = new TopBusquedaDTO();
        topBusquedaDTO1.setId(1L);
        TopBusquedaDTO topBusquedaDTO2 = new TopBusquedaDTO();
        assertThat(topBusquedaDTO1).isNotEqualTo(topBusquedaDTO2);
        topBusquedaDTO2.setId(topBusquedaDTO1.getId());
        assertThat(topBusquedaDTO1).isEqualTo(topBusquedaDTO2);
        topBusquedaDTO2.setId(2L);
        assertThat(topBusquedaDTO1).isNotEqualTo(topBusquedaDTO2);
        topBusquedaDTO1.setId(null);
        assertThat(topBusquedaDTO1).isNotEqualTo(topBusquedaDTO2);
    }
}
