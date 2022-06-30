/*
Project: Cheerville Simulator
Class: Being.java
Author: Eric Miao
Date: November 22, 2019
 */

abstract class Being implements Comparable<Being> {

  private int health;
  private int x, y;

  Being(int health, int x, int y) {
    this.health = health;
    this.x = x;
    this.y = y;
  }

  public int getHealth() {
    return this.health;
  }



  public int getPositionX() {
    return this.x;
  }

  public int getPositionY() {
    return this.y;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public void setHealth(int i) {
    this.health = i;
  }

  public void addHealth(int i) {
    this.health += i;
  }

  public boolean isDead() {
    if (this.health <= 0)
      return true;
    else
      return false;
  }

  public int compareTo(Being obj) {
    return (int)(this.getHealth()-obj.getHealth());//zombie.compareTo(human) to determine whether to kill or infect
  }
}