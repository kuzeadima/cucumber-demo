@Clean @InsertUserRole @InitializeAdministrator
Feature: Authenticate as user in happy and negative flows

  Scenario Outline: Authenticate with root user
    Given authenticate as <username> user with password <password>
    Then response status code should be <okStatusCode>
    Examples:
      | username | password    | okStatusCode |
      | root     | password123 | 200          |

  Scenario Outline: Authenticate negative flow - wrong username or password
    Given authenticate as <username> user with password <password>
    Then response status code should be 400
    And response body error <location> should have <message>
    Examples:
      | username | password | location | message                                              |
      | root     | password | details  | Authentication failed. Invalid credentials provided. |
      | unknown  | password | details  | Authentication failed. Invalid credentials provided. |

  Scenario Outline: Authenticate negative flow - fields validation
    Given authenticate as <username> user with password <password>
    Then response status code should be 400
    And response body error source should have <sourceMessage>
    And response body error details should have <detailsMessage>
    Examples:
      | username              | password | sourceMessage | detailsMessage                 |
      | test1234567890qwertyu | check    | username      | Size must be between 3 and 20. |
      | ab                    | check    | username      | Size must be between 3 and 20. |
      |                       | check    | username      | Must not be blank.             |
      | abcd                  |          | password      | Must not be blank.             |