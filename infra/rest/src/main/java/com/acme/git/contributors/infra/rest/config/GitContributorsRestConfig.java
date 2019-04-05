package com.acme.git.contributors.infra.rest.config;

import com.acme.git.contributors.application.GitContributorsApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitContributorsRestConfig {
    @Bean
    GitContributorsApplication gitContributorsApplication() {
        return new GitContributorsApplication();
    }
}
