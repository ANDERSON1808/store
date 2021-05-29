package com.store.com.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CarritoMapperTest {

    private CarritoMapper carritoMapper;

    @BeforeEach
    public void setUp() {
        carritoMapper = new CarritoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(carritoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(carritoMapper.fromId(null)).isNull();
    }
}
