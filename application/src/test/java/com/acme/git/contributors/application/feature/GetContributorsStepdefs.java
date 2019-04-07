package com.acme.git.contributors.application.feature;

import com.acme.git.contributors.application.domain.Contributor;
import com.acme.git.contributors.remote.GitServiceClient;
import cucumber.api.java8.En;
import org.junit.Assert;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class GetContributorsStepdefs implements En {
    private GitServiceClient gitServiceClient;
    private ObtainContributorsByCity obtainContributorsByCity;
    private List<Contributor> contributors;

    public GetContributorsStepdefs() {
        Before(() ->{
            gitServiceClient = Mockito.mock(GitServiceClient.class);
            List<Contributor> mockedContributors = new ArrayList<>();
            mockedContributors.add(new Contributor("garciapau"));
            Mockito.when(gitServiceClient.getContributorsByCity(any(String.class))).thenReturn(mockedContributors);
        });

        Given("^a Github service API$", () -> {
            obtainContributorsByCity = new ObtainContributorsByCity(gitServiceClient);
        });
        When("^user requests contributors of city '(.*)'$", (String city) -> {
            contributors = obtainContributorsByCity.getContributors(city);
        });
        Then("^a list of contributors is returned$", () -> {
            Assert.assertFalse(contributors.isEmpty());
        });
    }
}
