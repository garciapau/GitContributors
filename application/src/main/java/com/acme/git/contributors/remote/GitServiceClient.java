package com.acme.git.contributors.remote;

import com.acme.git.contributors.application.domain.ContributorsOfCity;

public interface GitServiceClient {
    ContributorsOfCity getContributorsByCity(String city, Integer initialPage, Integer maxResults);
}
