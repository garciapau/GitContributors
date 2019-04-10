package com.acme.git.contributors.infra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({
        "com.acme.git.contributors.infra.config",
        "com.acme.git.contributors.infra.rest.controller",
        "com.acme.git.contributors.infra.rest.config",
        "com.acme.git.contributors.infra.rest.handler"})
public class GitContributorsApp {

    public static void main(String[] args) {
        SpringApplication.run(GitContributorsApp.class, args);
    }

}
