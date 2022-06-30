import java.util.Random;
import java.util.Scanner;

/*
Project: Cheerville Simulator
Class: Main.java
Author: Eric Miao
Date: November 22, 2019
 */

public class Main {
    private static int maxHealth;
    private static int minHealth;
    public static Random generate = new Random();
    private static int nutValueMax, nutValueMin;
    private static int grid;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        char[] movements = {'u', 'd', 'l', 'r'};
        System.out.println("What is the width of the map");
        grid = input.nextInt();//width of the map/grid
        System.out.println("What is the plant spawn rate in whole number percent?");
        int spawnRate = input.nextInt();//plant spawn rate
        Plants[] plant = new Plants[grid * grid];//7
        System.out.println("What is the plants' highest and lowest nutrition value?");
        nutValueMax = input.nextInt();//max spawn nutrition
        nutValueMin = input.nextInt();//min spawn nutrition
        System.out.println("Zombies' health is fixed at 100");
        System.out.println("What is the maximum health and minimum health of spawn for humans? (>100)");
        maxHealth = input.nextInt();//max health for human
        minHealth = input.nextInt();//min health for human
        System.out.println("How many humans spawn? (" + (grid / 5) + "<x<" + (grid * 3) + ")");
        int number1 = input.nextInt();//number of human initially spawn
        Human[] human = new Human[grid * 8];
        System.out.print("How many zombies spawn? (<");
        if (grid / 10 < 3) System.out.print(2);
        else System.out.print(grid / 10);
        System.out.println(")");
        int number2 = input.nextInt();//number of zombies initially spawn
        Zombies[] zombie = new Zombies[grid * 3];//3

        for (int i = 0; i < number1; i++) {
            human[i] = new Human(generate.nextInt(maxHealth - minHealth) + minHealth, generate.nextInt(grid - 1), generate.nextInt(grid - 1), generate.nextInt(2), generate.nextInt(65) + 10);
        }
        for (int i = 0; i < number2; i++) {
            zombie[i] = new Zombies(100, generate.nextInt(grid - 1), generate.nextInt(grid - 1));
        }

        int[][] map = new int[grid][grid];//create map

        MatrixDisplay display = new MatrixDisplay("Simulator", map, human, plant, zombie);
        boolean running = true;

        //Main Program
        while (running) {
            checkAlive(human, zombie, plant);

            //determines when to stop running
            boolean anybodyAlive = false;
            for (int i = 0; i < human.length; i++) {
                if (human[i] != null) {
                    anybodyAlive = true;
                }
            }
            if (!anybodyAlive) {
                running = false;
            }

            //movement for each human
            for (int i = 0; i < human.length; i++) {//for each human
                if (human[i] != null) {
                    int x = human[i].getPositionX(), y = human[i].getPositionY();//get their position
                    int num = nextMove(x, y);//get direction of which way they're gonna travel
                    if (num != 4)
                        human[i].move(movements[num]);

                    human[i].addAge(0.1);//each turn age increases by 1
                    if (human[i].isDead()) {//boolean method that checks if the human health <= 0
                        human[i] = null;
                    }
                }
            }

            //movement for each zombies
            for (int i = 0; i < zombie.length; i++) {//for each human
                if (zombie[i] != null) {
                    int x = zombie[i].getPositionX(), y = zombie[i].getPositionY();//get their position
                    int num = nextMove(x, y);//get direction of which way they're gonna travel
                    if (num != 4)
                        zombie[i].move(movements[num]);

                    if (zombie[i].isDead()) {
                        zombie[i] = null;
                    }
                }
            }

            //Spawn Plants using spawn rate
            if (generate.nextInt(100) + 1 <= spawnRate) {
                addPlant(plant);
            }

            checkAlive(human, zombie, plant);
            checkOverlap(human, plant, zombie);//overlap method defines the kill and eats

            //Display update
            display.refresh();
            display.setTurn(1);//add turn number to MatrixDisplay class

            //Pause
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }
        }
        //Game ends
        System.out.println("QUIT");
        display.refresh();
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
        }
        System.exit(0);
    }

    private static void checkOverlap(Human[] human, Plants[] plant, Zombies[] zombie) {//perform overlap functions
        //if two humans, opposite gender, within producible age range overlaps
        for (int i = 0; i < human.length; i++) {
            for (int j = i + 1; j < human.length; j++) {
                if (human[i] != null && human[j] != null) {
                    if ((human[i].gender() == 0 && human[j].gender() == 1) || (human[j].gender() == 0 && human[i].gender() == 1)) {
                        if (human[i].getAge() >= 16 && human[j].getAge() >= 16 && human[i].getAge() <= 45 && human[j].getAge() <= 45) {
                            int x1 = human[i].getPositionX();
                            int x2 = human[j].getPositionX();
                            int y1 = human[i].getPositionY();
                            int y2 = human[j].getPositionY();
                            int xDiff = x1 - x2;
                            int yDiff = y1 - y2;
                            String gender1, gender2;
                            if (human[i].gender() == 0) gender1 = "Male";
                            else gender1 = "Female";
                            if (human[j].gender() == 0) gender2 = "Male";
                            else gender2 = "Female";
                            if (xDiff == 0 && yDiff == 0) {
                                //System.out.println("Make Human");
            /*Random number = new Random();
            if(number.nextInt(10) < 4)*/
                                System.out.println(gender1 + " Birth at age " + human[i].getAge() + " and " + gender2 + " " + human[j].getAge());
                                addHuman(human);
                            }
                        }
                    }
                }
            }
        }//end human-human overlap

        //if human and plant overlaps
        for (int i = 0; i < human.length; i++) {
            for (int j = 0; j < plant.length; j++) {
                if (human[i] != null && plant[j] != null) {
                    if (human[i].getPositionX() == plant[j].getPositionX() && human[i].getPositionY() == plant[j].getPositionY()) {
                        human[i].addHealth(plant[j].getHealth());
                        plant[j].setHealth(0);
                        plant[j] = null;
                    }
                }
            }
        }//end human-plant overlap

        //if zombie and plant overlaps
        for (int i = 0; i < zombie.length; i++) {
            for (int j = 0; j < plant.length; j++) {
                if (zombie[i] != null && plant[j] != null) {
                    if (zombie[i].getPositionX() == plant[j].getPositionX() && zombie[i].getPositionY() == plant[j].getPositionY()) {
                        plant[j].setHealth(0);
                        plant[j] = null;
                    }
                }
            }
        }//end zombie-plant overlap

        //if human and zombie overlaps, determine whether to kill or infect
        for (int i = 0; i < human.length; i++) {
            for (int j = 0; j < zombie.length; j++) {
                if (human[i] != null && zombie[j] != null) {
                    if (human[i].getPositionX() == zombie[j].getPositionX() && human[i].getPositionY() == zombie[j].getPositionY()) {
                        if (zombie[j].compareTo(human[i]) > 0) {
                            zombie[j].addHealth(human[i].getHealth());
                            human[i].setHealth(0);
                            System.out.println("Killed");
                            human[i] = null;
                        } else {
                            addZombie(zombie, human[i].getHealth(), zombie[j].getPositionX(), zombie[j].getPositionY());
                            System.out.println("Infected " + zombie[j].getHealth() + "<" + human[i].getHealth());
                            human[i].setHealth(0);
                            human[i] = null;
                        }
                    }
                }
            }
        }//end human-zombie overlap
    }

    private static void checkAlive(Human[] human, Zombies[] zombie, Plants[] plant) {
        for (int i = 0; i < human.length; i++) {
            if (human[i] != null) {
                if (human[i].getAge() >= 85.0) {
                    human[i].setHealth(0);
                    human[i] = null;
                    System.out.println("Old to death");
                }
            }
        }
        deleteHuman(human);
        deleteZombie(zombie);
        deletePlant(plant);
    }

    private static void addHuman(Human[] human) {
        int count = 0;

        for (int i = 0; i < human.length; i++) {
            if (human[i] != null) {
                count++;
                continue;
            } else {
                break;
            }
        }
        if (count < human.length)
            human[count] = new Human(generate.nextInt(maxHealth - minHealth) + minHealth, generate.nextInt(grid - 1), generate.nextInt(grid - 1), generate.nextInt(2), 0);
    }

    private static void deleteHuman(Human[] human) {
        for (int i = 0; i < human.length; i++) {
            if (human[i] != null) {
                if (human[i].isDead()) {
                    System.out.println("Dead of health");
                    human[i] = null;
                }
            }
        }
    }

    private static void addZombie(Zombies[] zombie, int health, int x, int y) {
        int count = 0;

        for (int i = 0; i < zombie.length; i++) {
            if (zombie[i] != null) {
                count++;
                continue;
            } else {
                break;
            }
        }
        if (count < zombie.length)
            zombie[count] = new Zombies(health, x, y);
    }

    private static void deleteZombie(Zombies[] zombie) {
        for (int i = 0; i < zombie.length; i++) {
            if (zombie[i] != null) {
                if (zombie[i].isDead()) {
                    zombie[i] = null;
                }
            }
        }
    }

    private static void addPlant(Plants[] plant) {
        int count = 0;

        for (int i = 0; i < plant.length; i++) {
            if (plant[i] != null) {
                count++;
                continue;
            } else {
                break;
            }
        }
        if (count < plant.length)
            plant[count] = new Plants(generate.nextInt(nutValueMax - nutValueMin) + nutValueMin, generate.nextInt(grid - 1), generate.nextInt(grid - 1));
    }

    private static void deletePlant(Plants[] plant) {
        for (int i = 0; i < plant.length; i++) {
            if (plant[i] != null) {
                if (plant[i].getHealth() <= 0) {
                    plant[i] = null;
                }
            }
        }
    }

    private static int nextMove(int x, int y) {
        boolean allowed = false;
        int num = generate.nextInt(5);//get direction of which way they're gonna travel

        while (!allowed) {//loop to see if movement is possible
            if (num == 0 && y != 0) {//go up
                allowed = true;
            } else if (num == 1 && y != grid - 1) {//go down
                allowed = true;
            } else if (num == 2 && x != 0) {//go left
                allowed = true;
            } else if (num == 3 && x != grid - 1) {//go right
                allowed = true;
            } else if (num == 4) {
                allowed = true;
            } else {
                num = generate.nextInt(5);
            }
        }//when movement is achievable
        return num;
    }
}