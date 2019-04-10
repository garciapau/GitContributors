package com.acme.git.contributors.application.domain;


public class Contributor {
    private String name;

    public Contributor(String name) {
        this.name = name;
    }

    public Contributor() {
    }

    public String getName() {
        return name;
    }
}
