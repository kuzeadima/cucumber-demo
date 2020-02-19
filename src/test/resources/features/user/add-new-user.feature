@Clean @InsertUserRole
Feature: Add new user in happy and negative flows

  Scenario Outline: Add new user happy flow
    Given create new user with <username> username and <password> password
    When send created user
    And save user in context
    Then response status code should be <createdStatusCode>
    And validate new user entity
    Examples:
      | username | password | createdStatusCode |
      | test     | check    | 201               |

  Scenario Outline: Add new user negative flow - double save
    Given create new user with <username> username and <password> password
    When send created user
    Then response status code should be <createdStatusCode>
    And send created user once again
    Then response status code should be <badRequestStatusCode>
    And response body error <location> should have <message>
    Examples:
      | username | password | createdStatusCode | badRequestStatusCode | location | message              |
      | test     | check    | 201               | 400                  | details  | User already exists. |

  Scenario Outline: Add new user negative flow - fields validation
    Given create new user with <username> username and <password> password
    When send created user
    Then response status code should be 400
    And response body error source should have <sourceMessage>
    And response body error details should have <detailsMessage>
    Examples:
      | username              | password | sourceMessage | detailsMessage                 |
      | test1234567890qwertyu | check    | username      | Size must be between 3 and 20. |
      | ab                    | check    | username      | Size must be between 3 and 20. |
      |                       | check    | username      | Must not be blank.             |
      | abcd                  |          | password      | Must not be blank.             |