Assignment 4 Due 11:59PM August 07

Let's create a module for a game. In the game, the player can take multiple forms or avatars. Each form or avatar has different capabilities. There are some strict rules as to how the player can transform or change from one form to another.

The following is a list of forms and capabilities of each. The list below also shows an example of the transformation sequence. You can change from a particular form to either the form above it or to the form below it, but not to any other form directly. Consider the list to be cyclic, that is you can go back and forth from Bike to Rocket.


Bike   drive through narrow lanes
Car    drive fast
Plane  fly fast
Rocket fly really fast

There are several aspects of the game that we won't focus here. The only thing we're going to focus on is exercising the activity in each form and the transformation to a different form.

Write your code in a way that at any given time we can ask the activity (like drive, fly, etc.) for a form to be performed. Also, at any given time, we can ask the form to be changed to one of the two permissible forms. 

Write your application so that the above sequence may be altered without any code change (for example, if we want to allow going from a Bike to Plane, we should be able to do that without having to change any code). Also, write the code in a way that other forms or avatars can be easily added later without any change to any existing code.

Total[100]: 100
Program works [20]:
All tests pass [20]:
Code coverage [20]:

Test quality [10]:
Just noticed, as I was looking for specific tests, that you've excluded core classes from coverage. Don't exclude classes that can be tested from coverage so easily. Raise an issue before doing that so it does not go unnoticed so easily.

Design quality [20]:
Code quality [10]:
