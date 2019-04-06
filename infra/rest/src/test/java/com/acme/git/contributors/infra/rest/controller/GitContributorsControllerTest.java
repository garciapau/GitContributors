package com.acme.git.contributors.infra.rest.controller;

import com.acme.git.contributors.application.usecase.ObtainContributorsByCity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GitContributorsControllerTest {
    private MockMvc mockMvc;

    @Mock
    ObtainContributorsByCity obtainContributorsByCity;

    @InjectMocks
    GitContributorsRestController gitContributorsRestController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(gitContributorsRestController).build();
    }

    @Test
    public void whenCallToGetContributors_shouldReturnHttpStatusOk() throws Exception {
        when(obtainContributorsByCity.getContributors(any(String.class)))
                .thenReturn(new ArrayList<>());
        mockMvc.perform(get(String.format("/contributors?city=%s", "Barcelona"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenCallToGetContributorsWithoutCity_shouldReturnHttpStatusBadRequest() throws Exception {
        mockMvc.perform(get("/contributors")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
