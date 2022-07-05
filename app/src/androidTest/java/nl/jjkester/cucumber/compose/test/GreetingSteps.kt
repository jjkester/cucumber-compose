package nl.jjkester.cucumber.compose.test

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.junit.WithJunitRule
import nl.jjkester.cucumber.compose.GreetingScreen
import nl.jjkester.cucumber.compose.setContentWithinTheme
import org.junit.Rule

/**
 * Cucumber steps for assertions on the greeting composable. This is primarily useful for testing
 * reusable UI components, for example in a library (no activity is present, and combinations of
 * configurations/arguments need to be tested).
 */
@WithJunitRule
class GreetingSteps {

    /**
     * Compose rule that should allow us to, within each scenario, set composable content and run
     * assertions against that content.
     */
    @get:Rule
    val composeRule = createComposeRule()

    /**
     * Precondition that sets up the desired content.
     */
    @Given("a greeting screen for name {string}")
    fun greetingIsCalledWithText(text: String) {
        composeRule.setContentWithinTheme {
            GreetingScreen(name = text)
        }
    }

    /**
     * Assertion that checks the contents of the screen.
     */
    @Then("{string} is displayed")
    fun textIsDisplayed(text: String) {
        composeRule.onNodeWithText(text).assertIsDisplayed()
    }
}
