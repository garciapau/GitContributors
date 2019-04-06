package com.acme.git.contributors.infra.config;

import com.acme.git.contributors.application.feature.ObtainContributorsByCity;
import com.acme.git.contributors.infra.remote.GitHubService;
import com.acme.git.contributors.remote.GitService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitContributorsConfig {
    @Bean
    GitService gitService() {
        return new GitHubService();
    }

    @Bean
    ObtainContributorsByCity gitContributorsApplication(GitService gitService) {
        return new ObtainContributorsByCity(gitService);
    }
}
