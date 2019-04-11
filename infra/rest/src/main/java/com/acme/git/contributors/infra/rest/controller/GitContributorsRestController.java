package com.acme.git.contributors.infra.rest.controller;

import com.acme.git.contributors.application.domain.ContributorsOfCity;
import com.acme.git.contributors.application.exception.IncorrectValuesException;
import com.acme.git.contributors.application.feature.ObtainContributorsByCity;
import com.acme.git.contributors.infra.rest.contract.GitContributorsController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GitContributorsRestController implements GitContributorsController {
    private ObtainContributorsByCity obtainContributorsByCity;

    public GitContributorsRestController(ObtainContributorsByCity obtainContributorsByCity) {
        this.obtainContributorsByCity = obtainContributorsByCity;
    }

    @Override
    @GetMapping(value = "/contributors")
    public ResponseEntity<ContributorsOfCity> getContributors(@RequestParam String city, @RequestParam Integer top)
            throws IncorrectValuesException {
        ContributorsOfCity contributorsOfCity = obtainContributorsByCity.getContributors(city, top);
        return ResponseEntity.ok(contributorsOfCity);
    }
}
