package com.store.com.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TopBusquedaMapperTest {

    private TopBusquedaMapper topBusquedaMapper;

    @BeforeEach
    public void setUp() {
        topBusquedaMapper = new TopBusquedaMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(topBusquedaMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(topBusquedaMapper.fromId(null)).isNull();
    }
}
