package nl.jjkester.cucumber.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Regular Android Instrumented test, using Jetpack Compose to display a composable to test it in
 * isolation. This is essential for testing library code or testing reusable parts of the UI.
 */
@RunWith(AndroidJUnit4::class)
class ComposeTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldDisplayDefaultName() {
        composeRule.setContentWithinTheme {
            GreetingScreen()
        }
        composeRule.onNodeWithText("Hello Android!").assertIsDisplayed()
    }

    @Test
    fun shouldDisplayCustomizedName() {
        composeRule.setContentWithinTheme {
            GreetingScreen("World")
        }
        composeRule.onNodeWithText("Hello World!").assertIsDisplayed()
    }
}

fun ComposeContentTestRule.setContentWithinTheme(content: @Composable () -> Unit) {
    setContent {
        MaterialTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                content()
            }
        }
    }
}
