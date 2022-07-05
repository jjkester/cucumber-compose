# Test case: Cucumber Android with Jetpack Compose

This project demonstrates the use of Cucumber for Android with Jetpack Compose, specifically testing
reusable composables without an activity.

## Demonstrated use case

Jetpack Compose enables us to easily create and share reusable UI components, from small components
to complete flows. Especially for more complex components it becomes increasingly relevant to write
comprehensive tests using feature files, in order for non-technical people to understand the test
cases and thereby the requirements and supported flows.

### Contents of this repository

The functional behavior of the code in this repository is a reusable greeting composable and an app
that displays it for demonstration purposes.

- [`MainActivity.kt`](app/src/main/java/nl/jjkester/cucumber/compose/MainActivity.kt)
    - Single activity that implements the reusable composable
    - The reusable composable itself
- [`ComposeActivityTest.kt`](app/src/androidTest/java/nl/jjkester/cucumber/compose/ComposeActivityTest.kt)
    - Tests the single activity of the app, by starting `MainActivity`
- [`ComposeTest.kt`](app/src/androidTest/java/nl/jjkester/cucumber/compose/ComposeTest.kt)
    - Tests the reusable composable by setting a composable view without an explicit activity
- [`greeting.feature`](app/src/androidTest/assets/features/greeting.feature)
    - Describes tests for the reusable composable to be run by Cucumber
- [`Cucumber.kt`](app/src/androidTest/java/nl/jjkester/cucumber/compose/test/Cucumber.kt)
    - Contains the configuration for Cucumber
- [`GreetingSteps.kt`](app/src/androidTest/java/nl/jjkester/cucumber/compose/test/GreetingSteps.kt)
    - Implements the steps that are referenced from `greeting.feature`

## Demonstrated issue

At the time of writing, a suspected issue in the way Cucumber Android uses JUnit rules prevents
these tests from being written.

### Observations when running the Cucumber tests

When running the Cucumber tests, an activity is briefly started and displayed on the test device (
for example an emulated device). The activity is quickly destroyed before the test case has even
started executing. The test case fails with an exception explaining that the activity has already
been destroyed.

#### Steps to reproduce

1. Ensure a connected test device is available, for example an emulator
2. Run the connected tests: `./gradlew :app:connectedDebugAndroidTest`

## Desired behavior

The activity should be running as long as the test is executed. After the tests completes (either
passes or fails), the activity can and should be destroyed.

## Analysis

### First analysis

The implementation of `io.cucumber.junit.RulesExecutor` looks correct in that it uses latches to
ensure the JUnit base `Statement` runs for as long as the tests run. However, during debugging it
was observed that the activity finishes at an earlier point.

Debug points used:

- `io.cucumber.junit.RulesExecutor`, lines 75 and 76 (latch operations within the `Statement`)
- `io.cucumber.junit.RulesExecutor`, line 57 (latch operation in `startRules`)
- `io.cucumber.junit.RulesExecutor`, line 116 (latch operation in `stopRules`)
- `cucumber.runner.TestCase`, line 35 in order to step through the `run` method
- `java.util.concurrent.CountDownLatch`, on the definition of the `await` method for method exits

With the above debug points in place, it can be observed that the thread running the `Statement`
waits for the latch to count down while the `TestCase.run` method is called, as expected. When
stepping through the `TestCase.run` method line by line, the activity on the test device finishes
after execution of line 38 (paused at line 39), which sends the `TestCaseStarted` event. Stepping
further through this method shows the test failing with the aforementioned exception. The breakpoint
on `CountDownLatch.await` shows that the `Statement`, as expected, only completes once the tests
have finished.

This observation is consistent through multiple test runs, I have not seen the behavior differ in
any way. Although it can not be concluded with certainty, this suggests that the place in the code
where the activity stops is not coincidental.

From the above observations, it seems the issue may be triggered by reporting the test case to have
started to JUnit. Further investigation on the root cause is necessary.

### Second analysis

See https://github.com/cucumber/cucumber-android/issues/102

The activity is stopped by the `TestCaseStarted` event because of the
Android `ActivityFinisherRunListener`. As it turns out, under normal JUnit conditions the event
fires (and all activities are stopped) before the rule is evaluated. The issue can be mitigated by
disabling the activity finisher by setting `waitForActivitiesToComplete=true`. A more permanent
solution could be to change the order in with Cucumber performs these steps.
