package com.acme.git.contributors.infra.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GitContributorsRestController implements GitContributorsController {
    @Override
    @GetMapping(value = "/contributors")
    public void getContributors() {

    }
}
