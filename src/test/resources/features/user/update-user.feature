@Clean @InsertUserRole @InitializeAdministrator
Feature: Update user in happy and negative flows

  Background:
    Given authenticate as root user with password password123
    When response status code should be 200
    Then save token

  Scenario Outline: Update user happy flow
    Given create new user with <username> username and <password> password
    When send created user
    And save user in context
    Then response status code should be <createdStatusCode>

    When prepare user for update
    And update user's <updatedUsername> username
    And update user's <updatedPassword> password
    And update user's <role> role
    And send updated user
    And save user in context
    Then response status code should be <acceptedStatusCode>
    And validate updated user entity
    Examples:
      | username | updatedUsername | password | updatedPassword | role  | createdStatusCode | acceptedStatusCode |
      | test     | updated         | check    | doubleCheck     | admin | 201               | 202                |

  Scenario Outline: Update user negative flow - role not found
    Given create new user with <username> username and <password> password
    When send created user
    And save user in context
    Then response status code should be <createdStatusCode>

    When prepare user for update
    And update user's <updatedRole> role
    And send updated user
    Then response status code should be <badRequestStatusCode>
    And response body error <location> should have <message>
    Examples:
      | username | password | updatedRole | createdStatusCode | badRequestStatusCode | location | message         |
      | test     | check    | moderator   | 201               | 400                  | details  | Role not found. |

  Scenario Outline: Update user negative flow - username is already used
    Given create new user with <username> username and <password> password
    When send created user
    And save user in context
    Then response status code should be <createdStatusCode>

    When prepare user for update
    And update user's <updatedUsername> username
    And send updated user
    Then response status code should be <badRequestStatusCode>
    And response body error <location> should have <message>
    Examples:
      | username | password | updatedUsername | createdStatusCode | badRequestStatusCode | location | message                   |
      | test     | check    | test            | 201               | 400                  | details  | Username is already used. |

  Scenario Outline: Update user negative flow - username fields validation
    Given create new user with <username> username and <password> password
    When send created user
    And save user in context
    Then response status code should be 201
    When prepare user for update
    And update user's <updatedUsername> username
    And send updated user
    Then response status code should be 400
    And response body error source should have <sourceMessage>
    And response body error details should have <detailsMessage>
    Examples:
      | username | updatedUsername       | password | sourceMessage | detailsMessage                 |
      | test     | test1234567890qwertyu | check    | username      | Size must be between 3 and 20. |
      | test     | ab                    | check    | username      | Size must be between 3 and 20. |