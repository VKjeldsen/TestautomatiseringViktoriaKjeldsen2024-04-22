Feature: Registration

  Scenario Outline: Creating a new supporter account for Basketball England
    Given I am using "<browser>" as a browser
    And the user is of legal age
    And I enter a first and "<last>" name
    And an email is entered and confirmed
    And a password is entered and "<retyped>"
    And I "<action>" agree to the Terms and Conditions
    And agree to the code of ethics and conduct
    When I click the Confirm and Join button
    Then a "<text>" will show and the registration process will end

    Examples:
      | browser | last     | retyped   | action | text                                                                      |
      | chrome  | Svensson | same      | do     | THANK YOU FOR CREATING AN ACCOUNT WITH BASKETBALL ENGLAND                 |
      | edge    |          | same      | do     | Last Name is required                                                     |
      | chrome  | Svensson | different | do     | Password did not match                                                    |
      | edge    | Svensson | same      | do not | You must confirm that you have read and accepted our Terms and Conditions |