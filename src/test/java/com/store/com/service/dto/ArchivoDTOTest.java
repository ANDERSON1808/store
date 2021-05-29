package com.store.com.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.store.com.web.rest.TestUtil;

public class ArchivoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArchivoDTO.class);
        ArchivoDTO archivoDTO1 = new ArchivoDTO();
        archivoDTO1.setId(1L);
        ArchivoDTO archivoDTO2 = new ArchivoDTO();
        assertThat(archivoDTO1).isNotEqualTo(archivoDTO2);
        archivoDTO2.setId(archivoDTO1.getId());
        assertThat(archivoDTO1).isEqualTo(archivoDTO2);
        archivoDTO2.setId(2L);
        assertThat(archivoDTO1).isNotEqualTo(archivoDTO2);
        archivoDTO1.setId(null);
        assertThat(archivoDTO1).isNotEqualTo(archivoDTO2);
    }
}
