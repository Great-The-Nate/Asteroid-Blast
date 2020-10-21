/**
 * @(#)Player.java
 *
 *
 * @author 
 * @version 1.00 2019/4/4
 */


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Player {

    private boolean isAlive; // checks if player is alive
    private boolean shield; // is shield active?
    private int lives; // number of lives

    private static final float LINEAR_ACCELERATION = 0.08f; // how fast player accelerates
    private static final float LINEAR_DECELERATION = 0.005f; // how fast player decelerates
    private static final float ANGULAR_ACCELERATION = 0.05f; // how fast player turns
    private static final float ANGULAR_DECELERATION = 0.03f; // how fast player slows down its turn
    private static final float MAX_LINEAR_VELOCITY = 2.0f; // maximum speed
    private static final float MAX_ANGULAR_VELOCITY = 2.0f; // maximum turn speed

    private BufferedImage image; // image of the player
    private BufferedImage heart; // image of the lives
    private int imageAngle; // angle for player to rotate
    private Point imageCenter; // center of image
    private float imageX, imageY; // coordinates of image
    private float linearVelocity, angularVelocity, dLinearVelocity, dAngularVelocity; // variables for its movement
    private AffineTransform imageTransform; // allows player to rotate

    private HashMap<Integer, Boolean> keyMap; // stores the keys pressed by user
    
    public Player(int lives){

        shield = true;
        this.lives = lives;
        isAlive = true;
        imageAngle = 0;
        linearVelocity = 0;
        angularVelocity = 0;
        keyMap = new HashMap<>();


        float scale = 0.1f;
        //draws and scales the player
        try {
            //creates image
            BufferedImage preScaleHeart = ImageIO.read(new File("resources/heart.png"));
            heart = new BufferedImage(20,20,BufferedImage.TYPE_INT_ARGB);
            Image scaledHeart = preScaleHeart.getScaledInstance(20,20,Image.SCALE_SMOOTH);
            Graphics f = heart.getGraphics();
            f.drawImage(scaledHeart,0,0,20,20,null);

            BufferedImage preScaleSprite = ImageIO.read(new File("resources/spaceship.png"));
            final int width = Math.round(preScaleSprite.getWidth() * scale);
            final int height = Math.round(preScaleSprite.getHeight() * scale);
            //scales the image to certain size
            Image scaledSprite = preScaleSprite.getScaledInstance(width, height, Image.SCALE_SMOOTH);

            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.getGraphics();
            //draws out the image
            g.drawImage(scaledSprite, 0, 0, width, height, null);



        } catch (IOException e) {
            e.printStackTrace();
        }

        //transforms the image to get it to rotate
        image.getScaledInstance(Math.round(image.getWidth() * scale), Math.round(image.getHeight() * scale), Image.SCALE_SMOOTH);
        imageX = (1500 - image.getWidth()) / 2;
        imageY = (800 - image.getHeight()) / 2;
        imageCenter = new Point(image.getWidth() / 2, image.getHeight() / 2);

        imageTransform = AffineTransform.getRotateInstance(Math.toRadians(imageAngle), imageCenter.x, imageCenter.y);

    }
    //checks if keys are pressed
    public void pressed(int keyCode){
        keyMap.put(keyCode, true);
    }
    public void released(int keyCode){
        keyMap.put(keyCode, false);
    }

    public boolean hasShield()
    {
        return shield;
    } // does the player have its shield
    public void changeShield() { shield = false; } // turns shield off
    public void addShield() { shield = true; } // turns shield on
    public int getLives()
    {
        return lives;
    } // returns number of lives
    public double getAngle()
    {
        return imageAngle;
    } // returns angle player rotates
    public int getX()
    {
        return (int)imageX+image.getWidth()/2;
    } // returns x coordinate of player
    public int getY() { return (int)imageY+image.getHeight()/2;} // returns y coordinate of player

    public boolean checkAlive()
    {
        return isAlive;
    } // checks if player is alive
    public void changeAlive()
    {
        isAlive = false;
    } // changes player to dead
    //did the big alien hit the player
    public boolean hitBigAlien(BigAlien b)
    {
        if(shield == false && Math.sqrt(Math.pow( getX() - b.getX() , 2) + Math.pow( getY() - b.getY() , 2) ) < (20 + b.getScale()/2)) {
            //System.out.println("collided!!");
            return true;

        }
        return false;
    }
    // did the rocks hit the player
    public boolean hitShip(Rock r)
    {
        if(shield == false && Math.sqrt(Math.pow( getX() - r.getX() , 2) + Math.pow( getY() - r.getY() , 2) ) < (20 + r.getScale()/2)) {
            return true;

        }
        return false;
    }
    //if alien bullet hits player
    public boolean hitPlayer(Ammo shot) {
        if(shield == false && Math.sqrt(Math.pow( getX() - shot.getX() , 2) + Math.pow( getY() - shot.getY() , 2) ) < (20)) {
            //System.out.println("you hit!" + tempCounter);
            // tempCounter++;
            return true;

        }
        return false;
    }
    //did alien hit player
    public boolean hitAlien(AlienShip r)
    {
        if(shield == false && Math.sqrt(Math.pow( getX() - r.getX() , 2) + Math.pow( getY() - r.getY() , 2) ) < (20 + r.getScale()/2)) {
            //System.out.println("collided!!");
            return true;

        }
        return false;
    }
    //moves the player
    public void move()
    {
        if (keyMap.getOrDefault(KeyEvent.VK_LEFT, false)) {
            dAngularVelocity = angularVelocity - ANGULAR_ACCELERATION;
            //System.out.println("moving to the left");
        } else if (keyMap.getOrDefault(KeyEvent.VK_RIGHT, false)) {
            dAngularVelocity = angularVelocity + ANGULAR_ACCELERATION;
        } else {
            if (dAngularVelocity > 0)
                dAngularVelocity = angularVelocity - ANGULAR_DECELERATION;
            else if (dAngularVelocity < 0)
                dAngularVelocity = angularVelocity + ANGULAR_DECELERATION;
            if (Math.round(dAngularVelocity) == 0)
                dAngularVelocity = 0;
        }
        if (keyMap.getOrDefault(KeyEvent.VK_UP, false)) {
            dLinearVelocity = linearVelocity + LINEAR_ACCELERATION;
        } else if (keyMap.getOrDefault(KeyEvent.VK_DOWN, false)) {
            dLinearVelocity = linearVelocity - LINEAR_ACCELERATION;
        } else {
            if (dLinearVelocity > 0)
                dLinearVelocity = linearVelocity - LINEAR_DECELERATION;
            else if (dLinearVelocity < 0)
                dLinearVelocity = linearVelocity + LINEAR_DECELERATION;
            if (Math.round(dLinearVelocity) == 0)
                dLinearVelocity = 0;
        }
        linearVelocity = Math.max(-MAX_LINEAR_VELOCITY, Math.min(dLinearVelocity, MAX_LINEAR_VELOCITY));
        angularVelocity = Math.max(-MAX_ANGULAR_VELOCITY, Math.min(dAngularVelocity, MAX_ANGULAR_VELOCITY));

        imageX += linearVelocity * Math.cos(Math.toRadians(imageAngle));
        imageY += linearVelocity * Math.sin(Math.toRadians(imageAngle));
        imageAngle += angularVelocity;
        //System.out.println(angularVelocity);
        imageAngle %= 360f;
        imageTransform.setToRotation(Math.toRadians(imageAngle), imageCenter.x, imageCenter.y);

        if(imageX>1500)imageX = 0;
        if(imageX<-0)imageX = 1500;
        if(imageY>800)imageY = 0;
        if(imageY<-0)imageY = 800;

    }
    //draws player and health
    public void draw(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        AffineTransformOp affineOp = new AffineTransformOp(imageTransform, AffineTransformOp.TYPE_BILINEAR);
        //player.paint(g2d);
        g2d.drawImage(image, affineOp, Math.round(imageX), Math.round(imageY));
        //draw collision
        g2d.setColor(Color.WHITE);
        g2d.drawOval(getX()-17, getY()-17, 40,40);
        if (shield == true)
            g2d.setColor(Color.GREEN);
        g2d.drawOval(getX()-17, getY()-17, 40,40);
        for(int i = 1; i<=lives; i++)
        {
            g2d.drawImage(heart, i*20, 30, null);
        }

    }
}