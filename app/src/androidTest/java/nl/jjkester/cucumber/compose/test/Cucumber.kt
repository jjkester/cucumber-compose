package nl.jjkester.cucumber.compose.test

import io.cucumber.junit.CucumberOptions

@Suppress("unused")
@CucumberOptions(
    strict = true,
    features = ["features"]
)
internal object Cucumber
