package com.acme.git.contributors.infra.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({
        "com.acme.git.contributors.infra.rest.controller",
        "com.acme.git.contributors.infra.rest.config"})
public class GitContributorsRestApp {

    public static void main(String[] args) {
        SpringApplication.run(GitContributorsRestApp.class, args);
    }

}
