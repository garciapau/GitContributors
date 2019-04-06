Feature: Retrieve git contributors

  Scenario: Retrieve GitHub contributors by city
    Given a Github service API
    When user requests contributors of city 'Barcelona'
    Then a list of contributors is returned
