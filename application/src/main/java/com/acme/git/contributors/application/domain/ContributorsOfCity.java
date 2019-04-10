package com.acme.git.contributors.application.domain;

import java.util.ArrayList;
import java.util.List;

public class ContributorsOfCity {
    private String city;
    private List<Contributor> contributors;

    private ContributorsOfCity(String city, List<Contributor> contributors) {
        this.city = city;
        this.contributors = contributors;
    }

    public List<Contributor> getContributors() {
        return contributors;
    }

    public String getCity() {
        return city;
    }

    public static class Builder {
        private String city;
        private List<Contributor> contributors;

        public Builder() {
            this.city = "";
            this.contributors = new ArrayList<>();
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setContributors(List<Contributor> contributors) {
            this.contributors = contributors;
            return this;
        }

        public ContributorsOfCity build() {
            return new ContributorsOfCity(city, contributors);
        }
    }
}
