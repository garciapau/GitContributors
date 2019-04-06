package com.acme.git.contributors.infra.remote;

import com.acme.git.contributors.remote.GitService;

import java.util.List;

public class GitHubService implements GitService {
    @Override
    public List<Object> getContributorsByCity(String city) {
        throw new UnsupportedOperationException();
    }
}
