package com.acme.git.contributors.infra.remote;

import com.acme.git.contributors.application.domain.ContributorsOfCity;
import com.acme.git.contributors.infra.cache.repository.InMemoryContributorCache;
import com.acme.git.contributors.remote.GitServiceClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class GitHubServiceRestV3ClientTest {
    private static final String gitHubSearchUrl = "http://NotUsedSinceItIsMocked";
    private static final String EXISTING_CITY = "Barcelona";
    private static final String NON_EXISTING_CITY = "WrongCity";
    private static final int INITIAL_PAGE = 1;
    private static final int MAX_RESULTS = 50;
    private GitServiceClient gitServiceClient;
    private RestTemplate restTemplate;

    @Before
    public void setUp() {
        restTemplate = Mockito.mock(RestTemplate.class);
        InMemoryContributorCache inMemoryContributorCache = Mockito.mock(InMemoryContributorCache.class);
        gitServiceClient = new GitHubServiceRestV3Client(restTemplate, gitHubSearchUrl, inMemoryContributorCache);
    }

    @Test
    public void whenCallToGetContributorsByCity_shouldReturnTheTop50Users() throws IOException {
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(JsonNode.class)))
                .thenReturn(buildMockGitResponseWithUsers(50));

        ContributorsOfCity contributorsByCity = gitServiceClient.getContributorsByCity(EXISTING_CITY, INITIAL_PAGE, MAX_RESULTS);
        Assert.assertFalse(contributorsByCity.getContributors().isEmpty());
        Assert.assertEquals(50, contributorsByCity.getContributors().size());
    }

    @Test
    public void whenCallToGetContributorsByWrongCity_shouldReturnNoUsers() throws IOException {
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(JsonNode.class)))
            .thenReturn(buildMockGitResponseWithUsers(0));

        ContributorsOfCity contributorsByCity = gitServiceClient.getContributorsByCity(NON_EXISTING_CITY, INITIAL_PAGE, MAX_RESULTS);
        Assert.assertTrue(contributorsByCity.getContributors().isEmpty());
        Assert.assertEquals(0, contributorsByCity.getContributors().size());
    }

    private ResponseEntity<JsonNode> buildMockGitResponseWithUsers(int numberOfUsers) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = Test.class.getResourceAsStream(String.format("/jsonMockResponses/mockResponseWith%dUsers.json", numberOfUsers));
        JsonNode mockResponse = mapper.readTree(is);
        return new ResponseEntity<>(mockResponse, HttpStatus.OK);
    }
}