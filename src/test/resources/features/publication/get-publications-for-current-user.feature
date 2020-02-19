@Clean @InsertUserRole
Feature: Get publications for current user in happy flow

  Background:
    Given create new user with Larry username and password123 password
    When send created user
    And save user in context
    Then response status code should be 201
    And validate new user entity
    When authenticate as Larry user with password password123
    Then response status code should be 200
    And save token

  Scenario Outline: Get publication for current user happy flow
    Given create new publication with <topic1> topic length and <body1> body length with publication time now
    When send created publication
    Then response status code should be <createdStatusCode>
    And validate new publication entity

    When create new publication with <topic2> topic length and <body2> body length with publication time now
    And send created publication
    Then response status code should be <createdStatusCode>
    And validate new publication entity

    When create new publication with <topic3> topic length and <body3> body length with publication time now
    And send created publication
    Then response status code should be <createdStatusCode>
    And validate new publication entity

    When get all the publications for current logged in user
    Then response status code should be <okStatusCode>
    And validate received publication entities
    Examples:
      | topic1 | body1 | topic2 | body2 | topic3 | body3 | createdStatusCode | okStatusCode |
      | 19     | 3453  | 36     | 5756  | 51     | 324   | 201               | 200          |