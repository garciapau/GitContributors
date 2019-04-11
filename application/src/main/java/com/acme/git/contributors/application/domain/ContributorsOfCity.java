package com.acme.git.contributors.application.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ContributorsOfCity {
    private String city;
    private ContributorsOfCitySource source;
    private String timestamp;
    private List<Contributor> contributors;

    private ContributorsOfCity(String city, ContributorsOfCitySource source, List<Contributor> contributors) {
        this.city = city;
        this.source = source;
        this.contributors = contributors;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public List<Contributor> getContributors() {
        return contributors;
    }

    public String getCity() {
        return city;
    }

    public ContributorsOfCitySource getSource() {
        return source;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public ContributorsOfCity append(List<Contributor> contributors) {
        this.contributors.addAll(contributors);
        return this;
    }

    public static class Builder {
        private String city;
        private ContributorsOfCitySource source;
        private List<Contributor> contributors;

        public Builder() {
            this.city = "";
            this.contributors = new ArrayList<>();
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setSource(ContributorsOfCitySource source) {
            this.source = source;
            return this;
        }

        public Builder setContributors(List<Contributor> contributors) {
            this.contributors = contributors;
            return this;
        }

        public ContributorsOfCity build() {
            return new ContributorsOfCity(city, source, contributors);
        }
    }
}
