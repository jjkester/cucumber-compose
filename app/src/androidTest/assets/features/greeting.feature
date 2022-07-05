Feature: Greeting

  # Tests the composable with different arguments
  Scenario Outline: Greet different names
    Given a greeting screen for name "<text>"
    Then "Hello <text>!" is displayed

    Examples:
      | text    |
      | Android |
      | World   |
      | Octocat |

  # Should fail, since we did not set any content
  Scenario: No content on the screen
    Then "Hello Android!" is displayed
