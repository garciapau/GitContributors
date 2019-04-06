package com.acme.git.contributors.application;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {
                "pretty",
                "html:../../build/reports/component/html-report/",
                "json:../../build/reports/component/report.json"
        },
        features = "src/test/resources/features"
)public class GetContributorsTest {
}
