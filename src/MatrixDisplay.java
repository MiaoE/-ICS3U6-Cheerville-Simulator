import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.event.MouseListener;//unused
import java.awt.event.MouseEvent;//unused

/*Project: Cheerville Simulator
Class: MatrixDisplay.java
Author: Eric Miao
Date: November 22, 2019
*/

public class MatrixDisplay extends JFrame {
  private int maxX, maxY, GridToScreenRatio;
  private int[][] matrix;
  private Human[] human;
  private Plants[] plant;
  private int turn = 0;
  private  Zombies[] zombie;
  private BufferedImage boyImg, zombieImg, plantImg, girlImg;
  private int highestHuman = 0, highestZombie = 0;



  MatrixDisplay(String title, int[][] map, Human[] human, Plants[] plant, Zombies[] zombie) {
    super(title);

    this.matrix = map;
    this.human = human;
    this.plant = plant;
    this.zombie = zombie;
    this.maxX = Toolkit.getDefaultToolkit().getScreenSize().width;
    this.maxY = Toolkit.getDefaultToolkit().getScreenSize().height;
    GridToScreenRatio = (maxY) / (matrix.length + 1);

    try {
      //change these destinations depending on where you store your picture files
      boyImg = ImageIO.read(new File("boy.jpg"));
      girlImg = ImageIO.read(new File("girl.jpg"));
      zombieImg = ImageIO.read(new File("zombie.jpg"));
      plantImg = ImageIO.read(new File("plant.jpg"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(Toolkit.getDefaultToolkit().getScreenSize());

    this.add(new MatrixPanel());

    this.setVisible(true);
  }

  public void refresh() {
    this.repaint();
  }

  class MatrixPanel extends JPanel {
    MatrixPanel() {
      //addMouseListener(new MouseInteractions());//unused
    }

    public void paintComponent(Graphics g) {
      super.repaint();
      setDoubleBuffered(true);
      g.setColor(Color.BLACK);

      for (int i = 0; i < matrix[0].length; i++) {
        for (int j = 0; j < matrix.length; j++) {
          g.setColor(Color.cyan);
          g.fillRect(j * GridToScreenRatio, i * GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);
          /*g.setColor(Color.black);
          g.drawRect(j * GridToScreenRatio, i * GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);*/
        }
      }

      int thuman = 0, tzombie = 0, tplant = 0;
      for (int i = 0; i < human.length; i++) {
        g.setColor(Color.blue);
        if (human[i] != null) {
          thuman++;
          int x = human[i].getPositionX();
          int y = human[i].getPositionY();
          if(human[i].gender() == 0)
            g.drawImage(boyImg, x * GridToScreenRatio, y * GridToScreenRatio, GridToScreenRatio, GridToScreenRatio, null);
          else
            g.drawImage(girlImg, x * GridToScreenRatio, y * GridToScreenRatio, GridToScreenRatio, GridToScreenRatio, null);
//            g.fillOval(x * GridToScreenRatio, y * GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);
        }
      }
      if (thuman > highestHuman) highestHuman = thuman;

      for (int i = 0; i < zombie.length; i++) {
        g.setColor(Color.black);
        if (zombie[i] != null) {
          tzombie++;
          int x = zombie[i].getPositionX();
          int y = zombie[i].getPositionY();
          g.drawImage(zombieImg, x * GridToScreenRatio, y * GridToScreenRatio, GridToScreenRatio, GridToScreenRatio, null);
//          g.fillRect(x * GridToScreenRatio, y * GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);
        }
      }
      if (tzombie > highestZombie) highestZombie = tzombie;
      for (int i = 0; i < plant.length; i++) {
        g.setColor(Color.green);
        if (plant[i] != null) {
          tplant++;
          int x = plant[i].getPositionX();
          int y = plant[i].getPositionY();
          g.drawImage(plantImg, x * GridToScreenRatio, y * GridToScreenRatio, GridToScreenRatio, GridToScreenRatio, null);
//            g.fillRect(x * GridToScreenRatio, y * GridToScreenRatio, GridToScreenRatio, GridToScreenRatio);
        }
      }
//        System.out.println(turn);
      String human = String.valueOf(thuman);
      String zombie = String.valueOf(tzombie);
      String plant = String.valueOf(tplant);
      String str = "Turn: " + turn;
      g.setColor(Color.black);
      g.setFont(new Font("Times New Roman", Font.BOLD, 20));
      g.drawString("Human", maxX-220, maxY - GridToScreenRatio * 3);
      g.drawString("Zombie", maxX-140, maxY - GridToScreenRatio * 3);
      g.drawString("Plant", maxX-300, maxY - GridToScreenRatio * 3);
      g.drawString(str, maxX-150, 20);
      int height = 3;
      //if(maxY/(human.length()*8) < 1) height = 1; else height = maxY/(human.length()*8);
      g.setColor(Color.blue);
      g.fillRect(maxX-210, maxY - GridToScreenRatio * 3 - 25 - height * thuman, 25, height * thuman);
      g.setColor(Color.red);
      g.fillRect(maxX-130, maxY - GridToScreenRatio * 3 - 25 - height * tzombie, 25, height * tzombie);
      g.setColor(Color.green);
      g.fillRect(maxX-290, maxY - GridToScreenRatio * 3 - 25 - height * tplant, 25, height * tplant);
      g.setColor(Color.black);
      g.drawString(human, maxX-210, maxY - GridToScreenRatio * 3 - 30 - height * thuman);
      g.drawString(zombie, maxX-130, maxY - GridToScreenRatio * 3 - 30 - height * tzombie);
      g.drawString(plant, maxX-290, maxY - GridToScreenRatio * 3 - 30 - height * tplant);

      if (thuman == 0) {
        g.setColor(Color.cyan);
        g.fillRect(0, 0, maxX, maxY);
        String highHuman = String.valueOf(highestHuman);
        String highZombie = String.valueOf(highestZombie);
        g.setFont(new Font("Times New Roman", Font.BOLD, 25));
        g.setColor(Color.black);
        String numturn = "Number of Turns Survived: "+(turn-1);
        g.drawString("Human", 20, maxY - 120);
        g.drawString("Zombie", maxX/2, maxY - 120);
        g.drawString("Highest Number of Population", maxX/4-100, maxY - 100);
        g.drawString(numturn, maxX/4, 120);
        g.setColor(Color.blue);
        g.fillRect(40, maxY - 145 - height * highestHuman, 50, height * highestHuman);
        g.drawString(highHuman, 40, maxY - 150 - height * highestHuman);
        g.setColor(Color.red);
        g.fillRect(maxX/2+20, maxY - 145 - height * highestZombie, 50, height * highestZombie);
        g.drawString(highZombie, maxX/2+20, maxY - 150 - height * highestZombie);
      }
    }
  }
/*
  class MouseInteractions implements MouseListener {
    public void mousePressed(MouseEvent e) {
      System.out.println("Mouse pressed; # of clicks: " + e.getClickCount());
      System.out.println("x: " + (e.getPoint().x / GridToScreenRatio) + ",y: " + (e.getPoint().y / GridToScreenRatio));
    }

    public void mouseReleased(MouseEvent e) {
      System.out.println("Mouse released; # of clicks: " + e.getClickCount());
    }

    public void mouseEntered(MouseEvent e) {
      System.out.println("Mouse entered");
    }

    public void mouseExited(MouseEvent e) {
      System.out.println("Mouse exited");
    }

    public void mouseClicked(MouseEvent e) {
      System.out.println("Mouse clicked (# of clicks: "+ e.getClickCount() + ")");
    }
  }*/

  public void setTurn(int i) {
    this.turn += i;
  }
}