package com.acme.git.contributors.application.repository;

import com.acme.git.contributors.application.domain.Contributor;

import java.util.List;

public interface ContributorsCache {
    List<Contributor> getContributorsByCity(String city);

    void saveContributorsOfCity(String city, List<Contributor> contributors);
}
