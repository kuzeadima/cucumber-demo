@Clean @InsertUserRole @InitializeAdministrator
Feature: Delete user by username in happy and negative flows

  Background:
    Given authenticate as root user with password password123
    When response status code should be 200
    Then save token

  Scenario Outline: Add new user and delete him
    Given create new user with <username> username and <password> password
    When send created user
    And save user in context
    Then response status code should be <createdStatusCode>
    And validate new user entity
    And remove user with <username> username
    Then response status code should be <acceptedStatusCode>
    And validate deleted user entity
    Examples:
      | username | password | createdStatusCode | acceptedStatusCode |
      | test     | check    | 201               | 202                |

  Scenario Outline: Delete non-existing user
    Given remove user with <wrongUserName> username
    Then response status code should be <badRequestStatusCode>
    And response body error <location> should have <message>
    Examples:
      | wrongUserName | badRequestStatusCode | location | message         |
      | wrongOne      | 400                  | details  | User not found. |