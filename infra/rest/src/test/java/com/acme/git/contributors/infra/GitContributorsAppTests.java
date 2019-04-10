package com.acme.git.contributors.infra;

import com.acme.git.contributors.application.domain.Contributor;
import com.acme.git.contributors.application.domain.ContributorsOfCity;
import com.acme.git.contributors.application.exception.IncorrectValuesException;
import com.acme.git.contributors.application.feature.ObtainContributorsByCity;
import com.acme.git.contributors.infra.cache.repository.InMemoryContributorCache;
import com.acme.git.contributors.infra.cache.repository.InMemoryContributorRepository;
import com.acme.git.contributors.infra.remote.GitHubServiceRestV3Client;
import com.acme.git.contributors.infra.rest.controller.GitContributorsRestController;
import com.acme.git.contributors.remote.GitServiceClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = GitContributorsApp.class)
public class GitContributorsAppTests {
    private MockMvc mockMvc;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${github.url}")
    String serviceUrl;

    private InMemoryContributorRepository inMemoryContributorRepository;
    private GitContributorsRestController gitContributorsRestController;

    @Before
    public void setUp() {
    }

    @Test
    public void whenRemoteServiceIsUp_shouldReturnListOfContributors() throws IncorrectValuesException {
        inMemoryContributorRepository = new InMemoryContributorRepository();
        initializeInfrastructure("https://api.github.com/search/users");

        ResponseEntity<List<Contributor>> contributors = gitContributorsRestController.getContributors("Barcelona", 50);
        Assert.assertEquals(contributors.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(contributors.getBody().size(), 50);
    }

    @Test
    public void whenRemoteServiceIsDown_shouldCallFallbackMechanism() throws IncorrectValuesException, Exception {
        HashMap<String, ContributorsOfCity> mockedRepository = buildMockRepoWithContributors("Barcelona", 1, "pau");
        inMemoryContributorRepository = new InMemoryContributorRepository(mockedRepository);
        initializeInfrastructure("https://api.github.com/AAAAsearch/users");

        ResponseEntity<List<Contributor>> contributors = gitContributorsRestController.getContributors("Barcelona", 50);
        Assert.assertEquals(contributors.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(contributors.getBody().size(), 1);
        Assert.assertEquals(contributors.getBody().get(0).getName(), "pau");
    }


    private void initializeInfrastructure(String gitHubUrl) {
        InMemoryContributorCache inMemoryContributorCache = new InMemoryContributorCache(inMemoryContributorRepository);
        GitServiceClient gitServiceClient = new GitHubServiceRestV3Client(restTemplate, gitHubUrl, inMemoryContributorCache);
        ObtainContributorsByCity obtainContributorsByCity = new ObtainContributorsByCity(gitServiceClient);
        gitContributorsRestController = new GitContributorsRestController(obtainContributorsByCity);
    }

    private HashMap<String, ContributorsOfCity> buildMockRepoWithContributors(String city, int numContributors, String name) {
        List<Contributor> cachedContributors = new ArrayList<>();
        cachedContributors.add(new Contributor(name));
        ContributorsOfCity bcnContributors = new ContributorsOfCity.Builder().setCity(city).setContributors(cachedContributors).build();
        HashMap<String, ContributorsOfCity> mockedRepository = new HashMap<>();
        mockedRepository.put(bcnContributors.getCity(), bcnContributors);
        return mockedRepository;
    }
}
