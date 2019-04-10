package com.acme.git.contributors.application.repository;

import com.acme.git.contributors.application.domain.ContributorsOfCity;

import java.util.Optional;

public interface ContributorRepository {
    Optional<ContributorsOfCity> findById(String city);

    void save(ContributorsOfCity contributorsOfCity);
}
