@Clean @InsertUserRole
Feature: Add new publication in happy and negative flows

  Background:
    Given create new user with Larry username and password123 password
    When send created user
    Then response status code should be 201
    When authenticate as Larry user with password password123
    Then response status code should be 200
    And save token

  Scenario Outline: Add new publication happy flow
    Given create new publication with <topicLength> topic length and <bodyLength> body length with publication time now
    When send created publication
    Then response status code should be <createdStatusCode>
    And validate new publication entity
    Examples:
      | topicLength | bodyLength | createdStatusCode |
      | 20          | 50         | 201               |
      | 40          | 2000       | 201               |

  Scenario Outline: Add new publication negative flow - double save
    Given create new publication with <topicLength> topic length and <bodyLength> body length with publication time now
    When send created publication
    Then response status code should be <createdStatusCode>
    And send created publication once again
    Then response status code should be <badRequestStatusCode>
    And response body error <location> should have <message>
    Examples:
      | topicLength | bodyLength | createdStatusCode | badRequestStatusCode | location | message                     |
      | 20          | 50         | 201               | 400                  | details  | Publication already exists. |

  Scenario Outline: Add new publication negative flow - fields validation
    Given create new publication with <topicLength> topic length and <bodyLength> body length with publication time <time>
    When send created publication
    Then response status code should be 400
    And response body error source should have <sourceMessage>
    And response body error details should have <detailsMessage>
    Examples:
      | topicLength | bodyLength | time                          | sourceMessage   | detailsMessage                          |
      | 5           | 50         | now                           | topic           | Size must be between 10 and 80.         |
      | 90          | 50         | now                           | topic           | Size must be between 10 and 80.         |
      | 40          | 10         | now                           | body            | Size must be between 30 and 2147483647. |
      | 40          | 120        | 2020-AA-26T11:48:54.758+02:00 | publicationTime | Wrong date provided.                    |