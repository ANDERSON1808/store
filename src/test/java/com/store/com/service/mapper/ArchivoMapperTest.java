package com.store.com.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ArchivoMapperTest {

    private ArchivoMapper archivoMapper;

    @BeforeEach
    public void setUp() {
        archivoMapper = new ArchivoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(archivoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(archivoMapper.fromId(null)).isNull();
    }
}
