package com.acme.git.contributors.application.feature;

import com.acme.git.contributors.application.domain.ContributorsOfCity;
import com.acme.git.contributors.application.exception.IncorrectValuesException;
import com.acme.git.contributors.remote.GitServiceClient;

public class ObtainContributorsByCity {
    private GitServiceClient gitServiceClient;

    public ObtainContributorsByCity(GitServiceClient gitServiceClient) {
        this.gitServiceClient = gitServiceClient;
    }

    public ContributorsOfCity getContributors(String city, Integer top) throws IncorrectValuesException {
        if (top == 50 || top == 100) return gitServiceClient.getContributorsByCity(city, 1, top);
        else if (top == 150) {
            ContributorsOfCity contributorsByCity = gitServiceClient.getContributorsByCity(city, 1, 100);
            contributorsByCity.append(gitServiceClient.getContributorsByCity(city, 2, 50).getContributors());
            return contributorsByCity;
        } else {
            throw new IncorrectValuesException("Top must be any value from 50, 100 or 150");
        }
    }
}
