/*
Project: Cheerville Simulator
Class: Human.java
Author: Eric Miao
Date: November 22, 2019
 */

public class Human extends Being implements Movement {
  private int gender;
  private double age;

  Human(int health, int x, int y, int gender, double age) {
    super(health, x, y);
    this.gender = gender;
    this.age = age;
  }

  public void move(char i) {
    if (i == 'u') {
      super.setY(super.getPositionY() - 1);
    } else if (i == 'd') {
      super.setY(super.getPositionY() + 1);
    } else if (i == 'l') {
      super.setX(super.getPositionX() - 1);
    } else if (i == 'r') {
      super.setX(super.getPositionX() + 1);
    }
    super.setHealth(super.getHealth() - 1);
  }

  public int gender() {
    return this.gender;
  }

  public double getAge() {
    return this.age;
  }

  public void addAge(double add) {
    this.age += add;
  }
}