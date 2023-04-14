package org.galatea.starter.Cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * To run cucumber test.
 */
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features",
        plugin = {"pretty", "json:target/cucumber-report.json"},
        glue = {"org.galatea.starter.Cucumber"})

public class CucumberTest {
}
