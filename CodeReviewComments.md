SignInTest
---------------------

1. This test had a static Thread.sleep method which I replaced with an explicit wait. 
2. Created a helper class which contains common methods used across all tests. Cureently has only different types of waits.
3. Since login popup is within a frame, we need to first switch to the frame and then click the sign in button.


HotelBookingTest
-------------------------

1. Replaced all waits with explicit waits
2. Modified the code to select the location and dates so now its able to search for hotels
3. Completed the positive flow to verify it's able to reach the payment page successfully
4. One improvement could be to move all page objects to their own classes following the Page Object Design Pattern

FlightBookingTest
---------------------------

1. Since most of code is already implemented updated waits to explict waits
2. Made corrections so test case is now passing 


P.S.
-----------
There could be many design changes we could do here if we look from a framework perspective. 
Since I have exposure towards BDD framework and have experience in developing the same.
I'd like to show you the framework I've developed. 

https://github.com/ankitnandwani/CucumberPrototype

I've tried to include good design principles so it's very easy to scale and maintain.