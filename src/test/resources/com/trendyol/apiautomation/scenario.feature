@SearchFilms
Feature: Search Films

  Search films

  Scenario Outline: Search Films
    Given it should valid api key "<apiKey>"
    When it should find films by artist "<artist>" and film name "<filmName>"
      And it should find film by imdb id
    Then it should valid response


    Examples:
      | apiKey  | artist       | filmName                              |
      | eb807a9 | Harry Potter | Harry Potter and the Sorcerer's Stone |
