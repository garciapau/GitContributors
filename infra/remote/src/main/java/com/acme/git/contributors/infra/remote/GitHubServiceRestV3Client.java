package com.acme.git.contributors.infra.remote;

import com.acme.git.contributors.application.domain.Contributor;
import com.acme.git.contributors.application.domain.ContributorsOfCity;
import com.acme.git.contributors.application.domain.ContributorsOfCitySource;
import com.acme.git.contributors.infra.cache.repository.InMemoryContributorCache;
import com.acme.git.contributors.remote.GitServiceClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class GitHubServiceRestV3Client implements GitServiceClient {
    private static final String SORTED_BY_REPOSITORIES = "repositories";
    private static final String SORTED_DESC = "desc";
    private static final String TYPE_USER = "user";
    private static final String ACCEPTED_HEADER_GITHUB_V3_JSON = "application/vnd.github.v3+json";
    private static final String USER_AGENT_CLIENT = "GitContributors";
    private RestTemplate restTemplate;

    @Value("${github.url}")
    private String githubUrl;

    private InMemoryContributorCache inMemoryContributorCache;

    public GitHubServiceRestV3Client(RestTemplate restTemplate, String serviceUrl, InMemoryContributorCache inMemoryContributorCache) {
        this.restTemplate = restTemplate;
        this.githubUrl = serviceUrl;
        this.inMemoryContributorCache = inMemoryContributorCache;
    }

    @Override
    public ContributorsOfCity getContributorsByCity(String city, Integer initialPage, Integer maxResults) {
        List<Contributor> contributors;
        try {
            contributors = parseResponseIntoContributorsList(callGithubService(city, initialPage, maxResults));
        } catch (RestClientException e) {
            return fallback_GetContributorsOfCity_ToCache(city, e);
        }
        inMemoryContributorCache.saveContributorsOfCity(city, contributors);
        return buildContributorsOfCity(city, contributors, ContributorsOfCitySource.REMOTE_SERVICE);
    }

    private ContributorsOfCity fallback_GetContributorsOfCity_ToCache(String city, RestClientException e) {
        // Fallback - Look for it in the cache. When not found in cache, just propagate the error
        return inMemoryContributorCache.getContributorsByCity(city).orElseThrow(() -> e);
    }

    private ContributorsOfCity buildContributorsOfCity(String city, List<Contributor> contributors, ContributorsOfCitySource remoteService) {
        return new ContributorsOfCity.Builder()
                .setCity(city)
                .setSource(remoteService)
                .setContributors(contributors).build();
    }

    private List<Contributor> parseResponseIntoContributorsList(ResponseEntity<JsonNode> response) {
        List<Contributor> contributorList;
        JsonNode contributors = response.getBody().path("items");
        Iterable<JsonNode> iterable = contributors::elements;
        Stream<JsonNode> targetStream = StreamSupport.stream(iterable.spliterator(), false);
        contributorList = targetStream.map(jsonNode -> new Contributor(jsonNode.path("login").textValue())).collect(Collectors.toList());
        return contributorList;
    }

    private ResponseEntity<JsonNode> callGithubService(String city, Integer initialPage, Integer maxResults)
            throws RestClientException {
        HttpHeaders headers = buildHttpHeaders();
        UriComponentsBuilder uriWithQueryParamsBuilder = buildUriWithParams(city, initialPage, maxResults);
        return restTemplate.exchange(
                uriWithQueryParamsBuilder.toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                JsonNode.class);
    }

    private UriComponentsBuilder buildUriWithParams(String city, Integer initialPage, Integer maxResults) {
        return UriComponentsBuilder.fromHttpUrl(githubUrl)
                .queryParam("q", String.format("type:%s+location:%s", TYPE_USER, city))
                .queryParam("page", initialPage)
                .queryParam("per_page", maxResults)
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
