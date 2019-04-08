package com.acme.git.contributors.remote;

import com.acme.git.contributors.application.domain.Contributor;

import java.util.List;

public interface GitServiceClient {
    List<Contributor> getContributorsByCity(String city);
}