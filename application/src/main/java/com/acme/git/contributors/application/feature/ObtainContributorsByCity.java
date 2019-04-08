package com.acme.git.contributors.application.feature;

import com.acme.git.contributors.application.domain.Contributor;
import com.acme.git.contributors.application.exception.APIRateLimitExceededException;
import com.acme.git.contributors.application.exception.IncorrectValuesException;
import com.acme.git.contributors.remote.GitServiceClient;

import java.util.List;

public class ObtainContributorsByCity {
    private GitServiceClient gitServiceClient;

    public ObtainContributorsByCity(GitServiceClient gitServiceClient) {
        this.gitServiceClient = gitServiceClient;
    }

    public List<Contributor> getContributors(String city, Integer top) throws APIRateLimitExceededException, IncorrectValuesException {
        if (top == 50 || top == 100) return gitServiceClient.getContributorsByCity(city, 1, top);
        else if (top == 150) {
            List<Contributor> contributorsByCity = gitServiceClient.getContributorsByCity(city, 1, 100);
            contributorsByCity.addAll(gitServiceClient.getContributorsByCity(city, 2, 50));
            return contributorsByCity;
        } else {
            throw new IncorrectValuesException("Top must be any value from 50, 100 or 150");
        }
    }
}
