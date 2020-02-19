@Clean @InsertUserRole
Feature: Delete publication by topic in happy and negative flows

  Background:
    Given create new user with Larry username and password123 password
    When send created user
    Then response status code should be 201
    When authenticate as Larry user with password password123
    Then response status code should be 200
    And save token

  Scenario Outline: Delete publication by topic happy flow
    Given create new publication with <topicLength> topic length and <bodyLength> body length with publication time now
    When send created publication
    Then response status code should be <createdStatusCode>
    And validate new publication entity

    When remove publication with <topicType> topic
    Then response status code should be <acceptedStatusCode>
    And validate deleted publication entity
    Examples:
      | topicLength | bodyLength | topicType | createdStatusCode | acceptedStatusCode |
      | 19          | 3453       | GENERATED | 201               | 202                |

  Scenario Outline: Delete publication by topic negative flow
    Given remove publication with <topicType> topic
    Then response status code should be <badRequestStatusCode>
    And response body error <location> should have <message>
    Examples:
      | topicType | badRequestStatusCode | location | message                                  |
      | CONSTANT  | 400                  | details  | Publication not found by provided topic. |