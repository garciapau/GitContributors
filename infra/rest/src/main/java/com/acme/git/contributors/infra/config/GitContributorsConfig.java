package com.acme.git.contributors.infra.config;

import com.acme.git.contributors.application.feature.ObtainContributorsByCity;
import com.acme.git.contributors.remote.GitService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitContributorsConfig {
    @Bean
    GitService gitService() {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Bean
    ObtainContributorsByCity gitContributorsApplication(GitService gitService) {
        return new ObtainContributorsByCity(gitService);
    }
}
