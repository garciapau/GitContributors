package com.acme.git.contributors.infra.remote;

import com.acme.git.contributors.application.domain.Contributor;
import com.acme.git.contributors.remote.GitServiceClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class GitHubServiceRestV3Client implements GitServiceClient {
    private RestTemplate restTemplate;

    @Value("${github.url}")
    private String githubUrl;

    public GitHubServiceRestV3Client(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Contributor> getContributorsByCity(String city) {
        List<Contributor> contributorList = new ArrayList<>();
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(githubUrl, JsonNode.class);
        if (response.getStatusCode().equals(HttpStatus.OK))
        {
            JsonNode contributors = response.getBody().path("items");
            Iterable<JsonNode> iterable = contributors::elements;
            Stream<JsonNode> targetStream = StreamSupport.stream(iterable.spliterator(), false);
            contributorList = targetStream.map(jsonNode -> new Contributor(jsonNode.path("login").textValue())).collect(Collectors.toList());
            System.out.println("num items" + contributorList.size());
        } else {
//            TODO throw new Exception(response.getStatusCode().getReasonPhrase());
        }
        return contributorList;
    }
}
