package com.acme.git.contributors.infra.rest.controller;

import com.acme.git.contributors.application.usecase.ObtainContributorsByCity;
import com.acme.git.contributors.infra.rest.contract.GitContributorsController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GitContributorsRestController implements GitContributorsController {
    private ObtainContributorsByCity gitContributorsApp;

    public GitContributorsRestController(ObtainContributorsByCity gitContributorsApp) {
        this.gitContributorsApp = gitContributorsApp;
    }

    @Override
    @GetMapping(value = "/contributors")
    public List<Object> getContributors(@RequestParam String city) {
        return gitContributorsApp.getContributors(city);
    }
}
