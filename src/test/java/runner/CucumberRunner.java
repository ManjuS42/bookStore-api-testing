package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", glue = "steps", tags = "@Sanity or @Regression",
    plugin = {"pretty", "html:target/cucumber-report.html", "json:target/cucumber.json"})

public class CucumberRunner {
}
