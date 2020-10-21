
import javax.swing.*;
import java.awt.*;
import java.awt.Component;
import javax.sound.sampled.*;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.awt.event.MouseMotionListener;


class HomePage extends JPanel implements MouseListener, Runnable, MouseMotionListener {

     static int mode = 0;
     static boolean startGame = false;
     Image image, name, lightbutton, darkbutton;
     int mouseX,mouseY;
     boolean cover = false;
    private ArrayList<Rock> rocks;

  //  private static boolean clicked = false;

    public HomePage() {

        setBackground(Color.black);
        try
        {
            //creates main page images
             image = ImageIO.read(new File("resources/mainscreenBack.jpg"));
            name = ImageIO.read(new File("resources/name.png"));
            lightbutton = ImageIO.read(new File("resources/lightbutton.png"));
            darkbutton = ImageIO.read(new File("resources/darkbutton.png"));
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File ("resources/sound.wav"));
            Clip clip = AudioSystem.getClip();
            clip.start(); //can also use "clip.open(ais);" either work

        }
        catch(Exception e)
        {
            //feel free to do something here
        }



        rocks= new ArrayList<Rock>();

        int tempRockX;
        int tempRockY;
        double tempRockAngle;

        //draws all the rocks in main page
        for(int i = 0; i<4; i++){
            tempRockX = (int)(Math.random()*1500);
            tempRockY = (int)(Math.random()*800);

            //System.out.println("x" + tempRockX);
            //System.out.println("y" + tempRockY);
            tempRockAngle = Math.random()*360;
            tempRockAngle = Math.toRadians(tempRockAngle);

            rocks.add(new Rock(tempRockX, tempRockY, tempRockAngle, 1, 1.5));
        }

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        addMouseMotionListener(this);
        setFocusable(true);
        requestFocus();

        new Thread(this).start();
        setVisible(true);
    }

    public void mousePressed(MouseEvent e) {

        //if mouse presses play button, the game starts
        if (e.getX() > 570 && e.getX() < 920 && e.getY() > 340 && e.getY() < 490)
           // clicked = true;
        {
            mode = 1;
            startGame = true;
        }
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {

    }
    public void mouseMoved(MouseEvent e)
    {
        //tracks location of user's mouse
        mouseX = e.getX();
        mouseY = e.getY();
    }
    public void mouseDragged(MouseEvent e){}

    public void mouseExited(MouseEvent e) {

    }

    public void paint(Graphics g) {

      //draws all the rocks and moves them
        g.drawImage(image,getX(),getY(),getWidth(),getHeight(),null);
        for (int i = 0; i < rocks.size(); i++)
            rocks.get(i).draw(g);

        for(int i = 0; i<rocks.size(); i++)
        {
            rocks.get(i).move();
        }
        //draws the name of the game
        g.drawImage(name,270,50,1000,250,null);

        Font tr = new Font("Arial Rounded MT", Font.PLAIN, 50);
        g.setFont(tr);
        g.setColor(Color.WHITE);
        //System.out.println(mouseX + " " + mouseY);

        //g.drawString("TEST",10,25);
        g.setColor(Color.black);

        //draws the buttons
        if(mouseX>570&&mouseX<920&&mouseY>340&&mouseY<490) {

            g.setColor(Color.WHITE);
            g.drawImage(lightbutton,567,345,350,150,null);
        }
        else {
            g.drawImage(darkbutton, 570, 340, 350, 150, null);
            g.setColor(Color.GRAY);
        }


        g.drawString("Play", 695,430);
    }

    @Override
    public void run() {
        try {
            while (mode == 0) {
                repaint();
                Thread.sleep(1000/60);

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class runGame extends JFrame implements Runnable {
    private static Component homePage, game;

    static {
        game = new AsteroidGame();
        homePage = new HomePage();
    }

    public static void main(String args[]) {
        runGame rungame = new runGame();
        rungame.run();

    }

    runGame() {

        setPreferredSize(new Dimension(1500, 800));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(homePage);
      homePage.setVisible(true);
      homePage.setFocusable(true);
      homePage.requestFocus();
      pack();
      setVisible(true);
    }

    /*
    * this switches between the main page and the game page
    * */
    @Override
    public void run() {
        try {
            while (true) {

                    if(HomePage.startGame)
                    {
                        add(game);
                        HomePage.startGame=false;
                    }

                //System.out.println(HomePage.mode);
                switch (HomePage.mode) {
                    case 0: {
                        game.setVisible(false);
                        game.setFocusable(false);
                        homePage.setVisible(true);
                        homePage.setFocusable(true);
                        homePage.requestFocus();
                        break;
                    }
                    case 1: {
                        homePage.setVisible(false);
                        homePage.setFocusable(false);
                        //add(game);
                        game.setVisible(true);
                        game.setFocusable(true);
                        game.requestFocus();
                        break;
                    }

                }
             //   homePage.setVisible();
                repaint();
                Thread.sleep(1000 / 60);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}