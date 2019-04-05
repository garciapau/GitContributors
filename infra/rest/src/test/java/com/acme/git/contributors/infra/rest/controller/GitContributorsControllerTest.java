package com.acme.git.contributors.infra.rest.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GitContributorsControllerTest {
    private MockMvc mockMvc;
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new GitContributorsRestController())
                .build();
    }

    @Test
    public void whenCallToGetContributors_shouldReturnHttpStatusOk() throws Exception {
        mockMvc.perform(get("/contributors")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
