package com.acme.git.contributors.application.feature;

import com.acme.git.contributors.application.domain.Contributor;
import com.acme.git.contributors.remote.GitServiceClient;

import java.util.List;

public class ObtainContributorsByCity {
    private GitServiceClient gitServiceClient;

    public ObtainContributorsByCity(GitServiceClient gitServiceClient) {
        this.gitServiceClient = gitServiceClient;
    }

    public List<Contributor> getContributors(String city) {
        return gitServiceClient.getContributorsByCity(city);
    }
}
