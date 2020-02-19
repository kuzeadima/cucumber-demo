@Clean @InsertUserRole @InitializeAdministrator
Feature: Get user by username happy and negative flows

  Background:
    Given authenticate as root user with password password123
    When response status code should be 200
    Then save token

  Scenario Outline: Add new user and find him
    Given create new user with <username> username and <password> password
    When send created user
    And save user in context
    Then response status code should be <createdStatusCode>
    And validate new user entity
    When search for user with <username> username
    Then response status code should be <okStatusCode>
    And validate received user entity
    Examples:
      | username | password | createdStatusCode | okStatusCode |
      | test     | check    | 201               | 200          |

  Scenario Outline: Find non-existing user
    Given search for user with <wrongUserName> username
    Then response status code should be <badRequestStatusCode>
    And response body error <location> should have <message>
    Examples:
      | wrongUserName | badRequestStatusCode | location | message         |
      | wrongOne      | 400                  | details  | User not found. |