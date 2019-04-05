package com.acme.git.contributors.infra.rest.controller;

import org.junit.Test;

public class GitContributorsControllerTest {
    @Test
    public void getContributors()
    {
        GitContributorsController gitContributorsController = new GitContributorsRestController();
        gitContributorsController.getContributors();
    }
}
