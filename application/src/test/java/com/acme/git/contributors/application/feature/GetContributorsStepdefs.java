package com.acme.git.contributors.application.feature;

import com.acme.git.contributors.application.domain.Contributor;
import com.acme.git.contributors.application.exception.APIRateLimitExceededException;
import com.acme.git.contributors.application.exception.IncorrectValuesException;
import com.acme.git.contributors.remote.GitServiceClient;
import cucumber.api.java8.En;
import org.junit.Assert;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class GetContributorsStepdefs implements En {
    private GitServiceClient gitServiceClient;
    private ObtainContributorsByCity obtainContributorsByCity;
    private List<Contributor> contributors;

    public GetContributorsStepdefs() {
        Before(() ->{
            gitServiceClient = Mockito.mock(GitServiceClient.class);
            try {
                Mockito.when(gitServiceClient.getContributorsByCity(any(String.class), eq(1), eq(50)))
                        .thenReturn(buildMockContributors(50));
                Mockito.when(gitServiceClient.getContributorsByCity(any(String.class), eq(1), eq(100)))
                        .thenReturn(buildMockContributors(100));
                Mockito.when(gitServiceClient.getContributorsByCity(any(String.class), eq(2), eq(50)))
                        .thenReturn(buildMockContributors(50));
            } catch (APIRateLimitExceededException e) {
                Assert.fail();
            }
        });

        Given("^a Github service API$", () -> {
            obtainContributorsByCity = new ObtainContributorsByCity(gitServiceClient);
        });
        When("^user requests top (\\d+) contributors of city '(.*)'$", (Integer top, String city) -> {
            try {
                contributors = obtainContributorsByCity.getContributors(city, top);
            } catch (APIRateLimitExceededException | IncorrectValuesException e) {
                Assert.fail();
            }
        });
        Then("^the (\\d+) contributors with more repositories are returned$", (Integer top) -> {
            Assert.assertFalse(contributors.isEmpty());
            Assert.assertEquals(contributors.size(), (int) top);
        });
    }

    private List<Contributor> buildMockContributors(Integer count) {
        return IntStream.range(0, count)
                .mapToObj(String::valueOf)
                .map(Contributor::new)
                .collect(Collectors.toList());
    }
}
