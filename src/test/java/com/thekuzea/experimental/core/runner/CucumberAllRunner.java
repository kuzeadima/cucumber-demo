package com.thekuzea.experimental.core.runner;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {
                "pretty",
                "html:target/cucumber"
        },
        glue = {
                "com.thekuzea.experimental.config",
                "com.thekuzea.experimental.core.hook",
                "com.thekuzea.experimental.core.stepdef"
        },
        features = "src/test/resources/features",
        junit = "--step-notifications",
        tags = "not @Ignore"
)
public class CucumberAllRunner {

}
