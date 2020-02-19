@Clean @InitializeAdministrator
Feature: Delete role by name in happy and negative flows

  Background:
    Given authenticate as root user with password password123
    When response status code should be 200
    Then save token

  Scenario Outline: Add new role and delete it
    Given create new role with <name> name
    When send created role
    Then response status code should be <createdStatusCode>
    And remove role with <name> name
    Then response status code should be <acceptedStatusCode>
    And validate deleted role entity
    Examples:
      | name      | createdStatusCode | acceptedStatusCode |
      | moderator | 201               | 202                |

  Scenario Outline: Delete non-existing role
    Given remove role with <wrongRoleName> name
    Then response status code should be <badRequestStatusCode>
    And response body error <location> should have <message>
    Examples:
      | wrongRoleName | badRequestStatusCode | location | message         |
      | wrongOne      | 400                  | details  | Role not found. |