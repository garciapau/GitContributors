package com.acme.git.contributors.infra;

import com.acme.git.contributors.application.domain.Contributor;
import com.acme.git.contributors.application.exception.IncorrectValuesException;
import com.acme.git.contributors.application.feature.ObtainContributorsByCity;
import com.acme.git.contributors.infra.remote.GitHubServiceRestV3Client;
import com.acme.git.contributors.infra.rest.controller.GitContributorsRestController;
import com.acme.git.contributors.remote.GitServiceClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = GitContributorsApp.class)
public class GitContributorsAppTests {

    @Autowired
    private RestTemplate restTemplate;

    private GitServiceClient gitServiceClient;
    private ObtainContributorsByCity obtainContributorsByCity;
    private GitContributorsRestController gitContributorsRestController;

    @Value("${github.url}")
    String serviceUrl;

    @Test
    public void whenRemoteServiceIsUp_shouldReturnListOfContributors() throws IncorrectValuesException {
        gitServiceClient = new GitHubServiceRestV3Client(restTemplate, "https://api.github.com/search/users");
        obtainContributorsByCity = new ObtainContributorsByCity(gitServiceClient);
        gitContributorsRestController = new GitContributorsRestController(obtainContributorsByCity);

        ResponseEntity<List<Contributor>> contributors = gitContributorsRestController.getContributors("Barcelona", 50);
        Assert.assertEquals(contributors.getBody().size(), 50);
    }

    @Test
    public void whenRemoteServiceIsDown_shouldCallFallbackMechanism() throws IncorrectValuesException {
        //        Alternative: ReflectionTestUtils.setField(gitServiceClient, "githubUrl", "https://WRONGapi.github.com/search/users");
        gitServiceClient = new GitHubServiceRestV3Client(restTemplate, "https://WRONGapi.github.com/search/users");
        obtainContributorsByCity = new ObtainContributorsByCity(gitServiceClient);
        gitContributorsRestController = new GitContributorsRestController(obtainContributorsByCity);

        ResponseEntity<List<Contributor>> contributors = gitContributorsRestController.getContributors("Barcelona", 50);
        Assert.assertEquals(contributors.getBody().size(), 1);
    }
}
