package com.acme.git.contributors.infra.remote;

import com.acme.git.contributors.application.domain.Contributor;
import com.acme.git.contributors.application.exception.APIRateLimitExceededException;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class GitHubServiceRestV3ClientTest {
    private static final String gitHubSearchUrl = "https://api.github.com/search/users?q=type:user+location:Barcelona&page=1&per_page=50&sort=repositories&order=desc";
    private GitServiceClient gitServiceClient;
    private RestTemplate restTemplate;

    @Before
    public void setUp() {
        restTemplate = Mockito.mock(RestTemplate.class);
        gitServiceClient = new GitHubServiceRestV3Client(restTemplate);
        // Using reflection test util to avoid SpringRunner for a unit test
        ReflectionTestUtils.setField(gitServiceClient, "githubUrl", gitHubSearchUrl);
    }

    @Test
    public void whenCallToGetContributorsByCity_shouldReturnTheTop50Users() throws IOException, APIRateLimitExceededException {
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(JsonNode.class)))
                .thenReturn(buildMockGitResponseWithUsers(50));

        List<Contributor> contributors = gitServiceClient.getContributorsByCity("Barcelona", 1, 50);
        Assert.assertFalse(contributors.isEmpty());
        Assert.assertEquals(50, contributors.size());
    }

    @Test
    public void whenCallToGetContributorsByWrongCity_shouldReturnNoUsers() throws IOException, APIRateLimitExceededException {
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(JsonNode.class)))
            .thenReturn(buildMockGitResponseWithUsers(0));

        List<Contributor> contributors = gitServiceClient.getContributorsByCity("WrongCity", 1, 50);
        Assert.assertTrue(contributors.isEmpty());
        Assert.assertEquals(0, contributors.size());
    }

    @Test(expected = APIRateLimitExceededException.class)
    public void whenCallToGetContributorsElevenTimes_shouldThrowApiRateLimitExceeded() throws IOException, APIRateLimitExceededException {
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(JsonNode.class)))
            .thenReturn(buildMockGitResponseRateLimitExceeded());

        List<Contributor> contributors = gitServiceClient.getContributorsByCity("AnyCity", 1, 50);
        Assert.fail();
    }

    private ResponseEntity<JsonNode> buildMockGitResponseRateLimitExceeded() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = Test.class.getResourceAsStream("/jsonMockResponses/mockResponseRateLimitExceeded.json");
        JsonNode mockResponse = mapper.readTree(is);
        return new ResponseEntity<>(mockResponse, HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<JsonNode> buildMockGitResponseWithUsers(int numberOfUsers) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = Test.class.getResourceAsStream(String.format("/jsonMockResponses/mockResponseWith%dUsers.json", numberOfUsers));
        JsonNode mockResponse = mapper.readTree(is);
        return new ResponseEntity<>(mockResponse, HttpStatus.OK);
    }
}