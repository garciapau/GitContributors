package com.acme.git.contributors.application.feature;

import com.acme.git.contributors.remote.GitService;

import java.util.List;

public class ObtainContributorsByCity {
    private GitService gitService;

    public ObtainContributorsByCity(GitService gitService) {
        this.gitService = gitService;
    }

    public List<Object> getContributors(String city) {
        return gitService.getContributorsByCity(city);
        //return new ArrayList<>();
    }
}
