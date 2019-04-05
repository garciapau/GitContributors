package com.acme.git.contributors.infra.rest.controller;

import com.acme.git.contributors.application.GitContributorsApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GitContributorsRestController implements GitContributorsController {

    @Override
    @GetMapping(value = "/contributors")
    public List<Object> getContributors() {
        GitContributorsApplication gitContributorsApp = new GitContributorsApplication();
        return gitContributorsApp.getContributors();
    }
}
