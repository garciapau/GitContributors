package com.acme.git.contributors.infra.cache.repository;

import com.acme.git.contributors.application.domain.Contributor;
import com.acme.git.contributors.application.domain.ContributorsOfCity;
import com.acme.git.contributors.application.repository.ContributorsCache;

import java.util.List;

public class InMemoryContributorCache implements ContributorsCache {
    private final InMemoryContributorRepository contributorRepository;

    public InMemoryContributorCache(InMemoryContributorRepository contributorRepository) {
        this.contributorRepository = contributorRepository;
    }

    @Override
    public List<Contributor> getContributorsByCity(String city) {
        return contributorRepository
                .findById(city)
                .orElse(new ContributorsOfCity.Builder().build())
                .getContributors();
    }

    @Override
    public void saveContributorsOfCity(String city, List<Contributor> contributors) {
        ContributorsOfCity contributorsOfCity = new ContributorsOfCity.Builder()
                .setCity(city)
                .setContributors(contributors)
                .build();
        contributorRepository.save(contributorsOfCity);
    }
}
