package com.acme.git.contributors.infra;

import com.acme.git.contributors.application.domain.Contributor;
import com.acme.git.contributors.application.domain.ContributorsOfCity;
import com.acme.git.contributors.application.domain.ContributorsOfCitySource;
import com.acme.git.contributors.application.feature.ObtainContributorsByCity;
import com.acme.git.contributors.infra.cache.repository.InMemoryContributorCache;
import com.acme.git.contributors.infra.cache.repository.InMemoryContributorRepository;
import com.acme.git.contributors.infra.remote.GitHubServiceRestV3Client;
import com.acme.git.contributors.infra.rest.controller.GitContributorsRestController;
import com.acme.git.contributors.infra.rest.handler.RestExceptionHandler;
import com.acme.git.contributors.remote.GitServiceClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = GitContributorsApp.class)
public class GitContributorsAppTests {
    private static final String MESSAGE_UNREACHABLE_CLIENT_AND_NOT_FOUND_IN_CACHE = "Unable to perform the request to the remote service and unfortunately the requested data is not cached";
    private static final String TEST_EXISTENT_CITY = "Barcelona";
    private static final String TEST_CITY_IN_CACHE = "Barcelona";
    private static final String TEST_CITY_NOT_IN_CACHE = "Tarragona";
    private static final String TEST_USER = "user1";
    private MockMvc mockMvc;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${github.url}")
    String serviceUrl;

    private InMemoryContributorRepository inMemoryContributorRepository;

    @Before
    public void setUp() {
    }

    @Test
    public void whenRemoteServiceIsUp_shouldReturnListOfContributors() throws Exception {
        inMemoryContributorRepository = new InMemoryContributorRepository();
        initializeInfrastructure("https://api.github.com/search/users");

        mockMvc.perform(get(String.format("/contributors?city=%s&top=%s", TEST_EXISTENT_CITY, 100))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.source", is(ContributorsOfCitySource.REMOTE_SERVICE.name())))
                .andExpect(jsonPath("$.contributors[0].name", not("")))
                .andExpect(jsonPath("$.timestamp", not("")));
    }

    @Test
    public void whenRemoteServiceIsUnreachable_shouldCallFallbackMechanismAndFoundIt() throws Exception {
        HashMap<String, ContributorsOfCity> mockedRepository = buildMockRepoWithContributors();
        inMemoryContributorRepository = new InMemoryContributorRepository(mockedRepository);
        initializeInfrastructure("https://api.github.com/AAAAsearch/users");

        mockMvc.perform(get(String.format("/contributors?city=%s&top=%s", TEST_CITY_IN_CACHE, 100))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.source", is(ContributorsOfCitySource.CACHE.name())))
                .andExpect(jsonPath("$.contributors[0].name", is(TEST_USER)))
                .andExpect(jsonPath("$.timestamp", is( mockedRepository.get(TEST_CITY_IN_CACHE).getTimestamp())));
    }

    @Test
    public void whenRemoteServiceIsUnreachable_shouldCallFallbackMechanismAndFail() throws Exception {
        HashMap<String, ContributorsOfCity> mockedRepository = buildMockRepoWithContributors();
        inMemoryContributorRepository = new InMemoryContributorRepository(mockedRepository);
        initializeInfrastructure("https://api.github.com/AAAAsearch/users");

        mockMvc.perform(get(String.format("/contributors?city=%s&top=%s", TEST_CITY_NOT_IN_CACHE, 100))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message", is(MESSAGE_UNREACHABLE_CLIENT_AND_NOT_FOUND_IN_CACHE)));
    }


    private void initializeInfrastructure(String gitHubUrl) {
        InMemoryContributorCache inMemoryContributorCache = new InMemoryContributorCache(inMemoryContributorRepository);
        GitServiceClient gitServiceClient = new GitHubServiceRestV3Client(restTemplate, gitHubUrl, inMemoryContributorCache);
        ObtainContributorsByCity obtainContributorsByCity = new ObtainContributorsByCity(gitServiceClient);
        GitContributorsRestController gitContributorsRestController = new GitContributorsRestController(obtainContributorsByCity);
        mockMvc = MockMvcBuilders.standaloneSetup(gitContributorsRestController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }

    private HashMap<String, ContributorsOfCity> buildMockRepoWithContributors() {
        List<Contributor> cachedContributors = new ArrayList<>();
        cachedContributors.add(new Contributor(GitContributorsAppTests.TEST_USER));
        ContributorsOfCity bcnContributors = new ContributorsOfCity.Builder()
                .setCity(GitContributorsAppTests.TEST_CITY_IN_CACHE)
                .setSource(ContributorsOfCitySource.CACHE)
                .setContributors(cachedContributors).build();
        HashMap<String, ContributorsOfCity> mockedRepository = new HashMap<>();
        mockedRepository.put(bcnContributors.getCity(), bcnContributors);
        return mockedRepository;
    }
}
