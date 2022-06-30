/*
Project: Cheerville Simulator
Class: Zombies.java
Author: Eric Miao
Date: November 22, 2019
 */

public class Zombies extends Being implements Movement {

  Zombies(int health, int x, int y) {
    super(health, x, y);
  }

  public void move(char i) {
    if (i == 'u') {
      super.setY(super.getPositionY() - 1);
    } else if (i == 'd') {
      super.setY(super.getPositionY() + 1);
    } else if (i == 'l') {
      super.setX(super.getPositionX() - 1);
    } else {
      super.setX(super.getPositionX() + 1);
    }
    super.setHealth(super.getHealth() - 1);
  }
}