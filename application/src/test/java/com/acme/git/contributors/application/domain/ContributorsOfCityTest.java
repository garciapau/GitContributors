package com.acme.git.contributors.application.domain;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ContributorsOfCityTest {

    @Test
    public void append_shouldAddNewContributorsToTheEndOfExistingOnes() {
        List<Contributor> contributorList = new ArrayList<>();
        contributorList.add(new Contributor("user1"));
        ContributorsOfCity contributorsOfCity = new ContributorsOfCity.Builder()
                .setCity("Barcelona")
                .setContributors(contributorList)
                .setSource(ContributorsOfCitySource.REMOTE_SERVICE)
                .build();

        List<Contributor> contributorList2 = new ArrayList<>();
        contributorList2.add(new Contributor("user2"));

        List<Contributor> contributorsMerged = contributorsOfCity.append(contributorList2).getContributors();
        Assert.assertEquals(contributorsMerged.size(), 2);
        Assert.assertEquals(contributorsMerged.get(0).getName(), "user1");
        Assert.assertEquals(contributorsMerged.get(1).getName(), "user2");
    }
}