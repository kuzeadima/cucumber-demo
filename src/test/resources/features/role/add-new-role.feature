@Clean @InitializeAdministrator
Feature: Add new role in happy and negative flows

  Background:
    Given authenticate as root user with password password123
    When response status code should be 200
    Then save token

  Scenario Outline: Add new role happy flow
    Given create new role with <name> name
    When send created role
    Then response status code should be <createdStatusCode>
    And validate new role entity
    Examples:
      | name      | createdStatusCode |
      | moderator | 201               |

  Scenario Outline: Add new role negative flow - double save
    Given create new role with <name> name
    When send created role
    Then response status code should be <createdStatusCode>
    And send created role once again
    Then response status code should be <badRequestStatusCode>
    And response body error <location> should have <message>
    Examples:
      | name      | createdStatusCode | badRequestStatusCode | location | message              |
      | moderator | 201               | 400                  | details  | Role already exists. |

  Scenario Outline: Add new role negative flow - field validation
    Given create new role with <name> name
    When send created role
    Then response status code should be 400
    And response body error source should have <sourceMessage>
    And response body error details should have <detailsMessage>
    Examples:
      | name                  | sourceMessage | detailsMessage                 |
      | test1234567890qwertyu | name          | Size must be between 2 and 10. |
      | a                     | name          | Size must be between 2 and 10. |