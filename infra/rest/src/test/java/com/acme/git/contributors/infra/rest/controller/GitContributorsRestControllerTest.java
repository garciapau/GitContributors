package com.acme.git.contributors.infra.rest.controller;

import com.acme.git.contributors.application.exception.APIRateLimitExceededException;
import com.acme.git.contributors.application.exception.IncorrectValuesException;
import com.acme.git.contributors.application.feature.ObtainContributorsByCity;
import com.acme.git.contributors.infra.rest.handler.RestExceptionHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;

import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GitContributorsRestControllerTest {
    private static final String MESSAGE_API_RATE_LIMIT_EXCEEDED = "API rate limit exceeded. (But here's the good news: Authenticated requests get a higher rate limit. Check out the documentation for more details.)";
    private static final String MESSAGE_PREFIX_EXPEXTED_API_RATE_LIMIT_EXCEEDED = "API rate limit exceeded";
    public static final String MESSAGE_TOP_INVALID = "Top must be any value from 50, 100 or 150";
    private MockMvc mockMvc;

    @Mock
    ObtainContributorsByCity obtainContributorsByCity;

    @InjectMocks
    GitContributorsRestController gitContributorsRestController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(gitContributorsRestController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }

    @Test
    public void whenCallToGetContributors_shouldReturnHttpStatusOk() throws Exception, APIRateLimitExceededException, IncorrectValuesException {
        when(obtainContributorsByCity.getContributors(any(String.class), any(Integer.class)))
                .thenReturn(new ArrayList<>());
        mockMvc.perform(get(String.format("/contributors?city=%s&top=%s", "Barcelona", 100))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenCallToGetContributorsWithoutCity_shouldReturnHttpStatusBadRequest() throws Exception {
        mockMvc.perform(get("/contributors")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenCallToGetContributorsExceedingTheRemoteServiceLimit_shouldReturnHttpStatusServiceUnavailable() throws Exception, APIRateLimitExceededException, IncorrectValuesException {
        when(obtainContributorsByCity.getContributors(any(String.class), any(Integer.class)))
                .thenThrow(new RestClientException(MESSAGE_API_RATE_LIMIT_EXCEEDED));
        mockMvc.perform(get(String.format("/contributors?city=%s&top=%s", "Barcelona", 100))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message", startsWith(MESSAGE_PREFIX_EXPEXTED_API_RATE_LIMIT_EXCEEDED)));
    }

    @Test
    public void whenCallToGetContributorsWithTopNotIn50_100_150_shouldReturnHttpStatusBadRequestAndExplanatoryMessage() throws Exception, APIRateLimitExceededException, IncorrectValuesException {
        when(obtainContributorsByCity.getContributors(any(String.class), any(Integer.class)))
                .thenThrow(new IncorrectValuesException(MESSAGE_TOP_INVALID));
        mockMvc.perform(get(String.format("/contributors?city=%s&top=%s", "Barcelona", 51))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
