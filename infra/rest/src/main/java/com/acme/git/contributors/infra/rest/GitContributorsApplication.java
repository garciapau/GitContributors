package com.acme.git.contributors.infra.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.acme.git.contributors.infra.rest.controller"})
public class GitContributorsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitContributorsApplication.class, args);
    }

}
