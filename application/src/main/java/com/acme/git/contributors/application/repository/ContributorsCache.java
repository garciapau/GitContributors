package com.acme.git.contributors.application.repository;

import com.acme.git.contributors.application.domain.Contributor;
import com.acme.git.contributors.application.domain.ContributorsOfCity;

import java.util.List;
import java.util.Optional;

public interface ContributorsCache {
    Optional<ContributorsOfCity> getContributorsByCity(String city);

    void saveContributorsOfCity(String city, List<Contributor> contributors);
}
