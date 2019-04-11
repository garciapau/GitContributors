package com.acme.git.contributors.application.feature;

import com.acme.git.contributors.application.domain.Contributor;
import com.acme.git.contributors.application.domain.ContributorsOfCity;
import com.acme.git.contributors.application.exception.IncorrectValuesException;
import com.acme.git.contributors.remote.GitServiceClient;
import cucumber.api.java8.En;
import org.junit.Assert;
import org.mockito.Mockito;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;

public class GetContributorsStepdefs implements En {
    private GitServiceClient gitServiceClient;
    private ObtainContributorsByCity obtainContributorsByCity;
    private ContributorsOfCity contributors;

    public GetContributorsStepdefs() {
        Before(() ->{
            gitServiceClient = Mockito.mock(GitServiceClient.class);
            Mockito.when(gitServiceClient.getContributorsByCity(any(String.class), any(Integer.class), any(Integer.class)))
                    .then(invocation -> buildMockContributors(invocation.getArgument(2, Integer.class)));
        });

        Given("^a Github service API$", () -> {
            obtainContributorsByCity = new ObtainContributorsByCity(gitServiceClient);
        });
        When("^user requests top (\\d+) contributors of city '(.*)'$", (Integer top, String city) -> {
            try {
                contributors = obtainContributorsByCity.getContributors(city, top);
            } catch (IncorrectValuesException e) {
                Assert.fail();
            }
        });
        Then("^the (\\d+) contributors with more repositories are returned$", (Integer top) -> {
            Assert.assertFalse(contributors.getContributors().isEmpty());
            Assert.assertEquals(contributors.getContributors().size(), (int) top);
        });
    }

    private ContributorsOfCity buildMockContributors(Integer count) {
        List<Contributor> contributorList = IntStream.range(0, count)
                .mapToObj(String::valueOf)
                .map(Contributor::new)
                .collect(Collectors.toList());
        return new ContributorsOfCity.Builder()
                .setContributors(contributorList).build();
    }
}
