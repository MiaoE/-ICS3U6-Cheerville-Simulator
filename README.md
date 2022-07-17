# Cheerville-Simulator

- At the beginning, a random number of human beings and plants, and small number of zombies spawn on a grid of the world. Humans are either male or female, spawned at random age, and their health is a random number between a range given by the user. Plants are given a random nutrition values between a range, and plants spawn at a certain rate also given by the user. Zombies are at a fixed health.
- The movement for humans and zombies are random each tick.
- Every tick human's age increases by 1, zombie's health decreases by 1.
- If human lands on plants, human gain health with respect to the nutritional value of the plant.
- If zombie lands on plants, the plant gets trampled.
- Only a certain aged male and female can mate if they land on the same square of the grid, the new human is aged 0.
- If zombie and human lands on the same square: if zombie's health is greater than the human, the human gets killed; otherwise the human gets infected.

There is also population bar graph implemented with the program.

This project utilizes OOP concepts such as inheritance, encapsulation, polymorphism, and abstraction.
