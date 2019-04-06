package com.acme.git.contributors.infra.rest.config;

import com.acme.git.contributors.application.usecase.ObtainContributorsByCity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitContributorsRestConfig {
    @Bean
    ObtainContributorsByCity gitContributorsApplication() {
        return new ObtainContributorsByCity();
    }
}
