package com.acme.git.contributors.infra.remote;

import com.acme.git.contributors.application.domain.Contributor;
import com.acme.git.contributors.remote.GitServiceClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class GitHubServiceRestV3Client implements GitServiceClient {
    private static final String SORTED_BY_REPOSITORIES = "repositories";
    private static final String SORTED_DESC = "desc";
    private static final int INITIAL_PAGE = 1;
    private static final int MAX_USERS_PER_PAGE = 50;
    private static final String TYPE_USER = "user";
    private static final String ACCEPTED_HEADER_GITHUB_V3_JSON = "application/vnd.github.v3+json";
    private static final String USER_AGENT_CLIENT = "GitContributors";
    private RestTemplate restTemplate;

    @Value("${github.url}")
    private String githubUrl;

    public GitHubServiceRestV3Client(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Contributor> getContributorsByCity(String city) {
        List<Contributor> contributorList = new ArrayList<>();
        ResponseEntity<JsonNode> response = callGithubService(city);
        if (response.getStatusCode().equals(HttpStatus.OK))
        {
            return parseResponseIntoContributorsList(response);
        } else {
//            TODO throw new Exception(response.getStatusCode().getReasonPhrase());
        }
        return contributorList;
    }

    private List<Contributor> parseResponseIntoContributorsList(ResponseEntity<JsonNode> response) {
        List<Contributor> contributorList;
        JsonNode contributors = response.getBody().path("items");
        Iterable<JsonNode> iterable = contributors::elements;
        Stream<JsonNode> targetStream = StreamSupport.stream(iterable.spliterator(), false);
        contributorList = targetStream.map(jsonNode -> new Contributor(jsonNode.path("login").textValue())).collect(Collectors.toList());
        return contributorList;
    }

    private ResponseEntity<JsonNode> callGithubService(String city) {
        HttpHeaders headers = buildHttpHeaders();
        UriComponentsBuilder uriWithQueryParamsBuilder = buildUriWithParams(city);
        return restTemplate.exchange(
                uriWithQueryParamsBuilder.toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                JsonNode.class);
    }

    private UriComponentsBuilder buildUriWithParams(String city) {
        return UriComponentsBuilder.fromHttpUrl(githubUrl)
                    .queryParam("q", String.format("type:%s+location:%s", TYPE_USER, city))
                    .queryParam("page", INITIAL_PAGE)
                    .queryParam("per_page", MAX_USERS_PER_PAGE)
                    .queryParam("sort", SORTED_BY_REPOSITORIES)
                    .queryParam("order", SORTED_DESC);
    }

    private HttpHeaders buildHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", ACCEPTED_HEADER_GITHUB_V3_JSON);
        headers.set("User-Agent", USER_AGENT_CLIENT);
        return headers;
    }
}
