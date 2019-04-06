package com.acme.git.contributors.remote;

import java.util.List;

public interface GitService {
    List<Object> getContributorsByCity(String city);
}
