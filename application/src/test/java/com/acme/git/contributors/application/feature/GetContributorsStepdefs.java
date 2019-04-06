package com.acme.git.contributors.application.feature;

import com.acme.git.contributors.remote.GitService;
import cucumber.api.java8.En;
import org.junit.Assert;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class GetContributorsStepdefs implements En {
    private GitService gitService;
    private ObtainContributorsByCity obtainContributorsByCity;
    private List<Object> contributors;

    public GetContributorsStepdefs() {
        Before(() ->{
            gitService = Mockito.mock(GitService.class);
            Mockito.when(gitService.getContributorsByCity(any(String.class))).thenReturn(new ArrayList<>());
        });

        Given("^a Github service API$", () -> {
            obtainContributorsByCity = new ObtainContributorsByCity(gitService);
        });
        When("^user requests contributors of city '(.*)'$", (String city) -> {
            contributors = obtainContributorsByCity.getContributors(city);
        });
        Then("^a list of contributors is returned$", () -> {
            Assert.assertNotNull(contributors);
        });
    }
}
