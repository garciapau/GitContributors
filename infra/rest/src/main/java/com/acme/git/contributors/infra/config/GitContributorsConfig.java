package com.acme.git.contributors.infra.config;

import com.acme.git.contributors.application.feature.ObtainContributorsByCity;
import com.acme.git.contributors.infra.remote.GitHubServiceRestV3Client;
import com.acme.git.contributors.remote.GitServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GitContributorsConfig {
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    GitServiceClient gitService(RestTemplate restTemplate) {
        return new GitHubServiceRestV3Client(restTemplate);
    }

    @Bean
    ObtainContributorsByCity gitContributorsApplication(GitServiceClient gitServiceClient) {
        return new ObtainContributorsByCity(gitServiceClient);
    }
}
