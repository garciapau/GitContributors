package com.acme.git.contributors.infra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({
        "com.acme.git.contributors.infra.config",
        "com.acme.git.contributors.infra.rest.controller",
        "com.acme.git.contributors.infra.rest.config",
        "com.acme.git.contributors.infra.rest.handler"})
@EnableJpaRepositories(basePackages = "com.acme.git.contributors.infra.cache.repository")
@EntityScan(value = "com.acme.git.contributors.infra.cache.model")
public class GitContributorsApp {

    public static void main(String[] args) {
        SpringApplication.run(GitContributorsApp.class, args);
    }

}
