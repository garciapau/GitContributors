package com.acme.git.contributors.infra.cache.repository;

import com.acme.git.contributors.application.domain.ContributorsOfCity;
import com.acme.git.contributors.application.repository.ContributorRepository;

import java.util.HashMap;
import java.util.Optional;

public class InMemoryContributorRepository implements ContributorRepository {
    private HashMap<String, ContributorsOfCity> repository;

    public InMemoryContributorRepository() {
        this.repository = new HashMap<>();
    }
    public InMemoryContributorRepository(HashMap<String, ContributorsOfCity> repository) {
        this.repository = repository;
    }

    @Override
    public Optional<ContributorsOfCity> findById(String city) {
        return Optional.ofNullable(repository.get(city));
    }

    @Override
    public void save(ContributorsOfCity contributorsOfCity) {
        repository.put(contributorsOfCity.getCity(), contributorsOfCity);
    }
}
