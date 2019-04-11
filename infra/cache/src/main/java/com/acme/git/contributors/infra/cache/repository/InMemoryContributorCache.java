package com.acme.git.contributors.infra.cache.repository;

import com.acme.git.contributors.application.domain.Contributor;
import com.acme.git.contributors.application.domain.ContributorsOfCity;
import com.acme.git.contributors.application.domain.ContributorsOfCitySource;
import com.acme.git.contributors.application.repository.ContributorsCache;

import java.util.List;
import java.util.Optional;

public class InMemoryContributorCache implements ContributorsCache {
    private final InMemoryContributorRepository contributorRepository;

    public InMemoryContributorCache(InMemoryContributorRepository contributorRepository) {
        this.contributorRepository = contributorRepository;
    }

    @Override
    public Optional<ContributorsOfCity> getContributorsByCity(String city) {
        return contributorRepository.findById(city);
    }

    @Override
    public void saveContributorsOfCity(String city, List<Contributor> contributors) {
        ContributorsOfCity contributorsOfCity = new ContributorsOfCity.Builder()
                .setCity(city)
                .setSource(ContributorsOfCitySource.CACHE)
                .setContributors(contributors)
                .build();
        contributorRepository.save(contributorsOfCity);
    }
}
