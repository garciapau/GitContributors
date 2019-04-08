Feature: Retrieve git contributors

  Scenario: Retrieve 50 GitHub contributors by city
    Given a Github service API
    When user requests top 50 contributors of city 'Barcelona'
    Then the 50 contributors with more repositories are returned

  Scenario: Retrieve 150 GitHub contributors by city
    Given a Github service API
    When user requests top 150 contributors of city 'Barcelona'
    Then the 150 contributors with more repositories are returned
