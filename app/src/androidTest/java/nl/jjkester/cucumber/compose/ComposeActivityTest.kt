package nl.jjkester.cucumber.compose

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Regular Android Instrumented test, using Jetpack Compose to start an activity and assert the
 * text that is displayed.
 */
@RunWith(AndroidJUnit4::class)
class ComposeActivityTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun shouldDisplayHelloAndroid() {
        composeRule.onNodeWithText("Hello Android!").assertIsDisplayed()
    }
}
