Feature: Show restaurants by cuisine

  Scenario: Show all restaurants on the map
    #Show all restaurants on the map by default
    Given The map is loaded
#    When All cuisines menu is selected
    Then All restaurants are shown on the map