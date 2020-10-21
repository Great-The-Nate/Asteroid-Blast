/**
 * @(#)Rock.java
 *
 *
 * @author 
 * @version 1.00 2019/4/4
 */


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class Rock {

    private boolean gotHit = false; // checks if rock got hit
	private double size, speed, angle; // creates parameters for rock
	private BufferedImage image; // image of rock
	private BufferedImage preScaleSprite; // prescaled iamge
	private Image scaledSprite;//postscaled image
	private float imageX, imageY; // location of image
	private Point imageCenter; // location of image center
    private AffineTransform imageTransform; // allows rocks to rotate
    private int imageAngle; // gets angle at which rock moves
    private int ovalX,ovalY; // location of the rock

    //creates rock object
    public Rock(float x, float y, double angle, double size, double speed)
    {
        imageX = x;
        imageY = y;
        this.angle = angle;
        this.size = size;
        this.speed = speed;

        float scale = (float)size;
        //draws and rescales rock
        try {
            //creates image
             preScaleSprite = ImageIO.read(new File("resources/asteroid.png"));
            final int width = Math.round(preScaleSprite.getWidth() * scale);
            final int height = Math.round(preScaleSprite.getHeight() * scale);
            //scales the image to certain size
            scaledSprite = preScaleSprite.getScaledInstance(width, height, Image.SCALE_SMOOTH);

            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.getGraphics();
            //draws out the image
            g.drawImage(scaledSprite, 0, 0, width, height, null);



        } catch (IOException e) {
            e.printStackTrace();
        }

        //transforms the image to get it to rotate
        image.getScaledInstance(Math.round(image.getWidth() * scale), Math.round(image.getHeight() * scale), Image.SCALE_SMOOTH);
        imageX = (x - image.getWidth()) / 2;
        imageY = (y - image.getHeight()) / 2;
        imageCenter = new Point(image.getWidth() / 2, image.getHeight() / 2);

        imageTransform = AffineTransform.getRotateInstance(Math.toRadians(imageAngle), imageCenter.x, imageCenter.y);


    }

    public double getSpeed() { return speed; } // gets speed of rock
    public double getX(){
        return ovalX+getScale()/2;
    } // gets x coordinate of rock

    public double getY(){
        return ovalY + getScale()/2;
    } // gets y coordinate of rock
    public double getOvalX() { return ovalX; } // gets x coordinate of image
    public double getOvalY() { return ovalY; } // gets y coordinate of image

    public int getScale(){
        return (int)(size*120);
    } // gets the scale of the rock
    public double getSize() {return size;} // returns original size
    public boolean hit(){
        return gotHit;
    } // checks if rock got hit
    public double getAngle()
    {
        return angle;
    } // gets angle at which rock moves

    //checks if player hit the rock
    public boolean hitRock(Ammo shot) {
        if(Math.sqrt(Math.pow(shot.getX()-(ovalX+getScale()/2),2)+Math.pow(shot.getY()-(ovalY+getScale()/2),2))<getScale()/2) {
            //System.out.println("you hit!" + tempCounter);
           // tempCounter++;
            gotHit = true;
            return true;

        }
        return false;
    }


    //moves the rock
    public void move(){

        double xSpeed = speed*Math.cos(angle);
        double ySpeed = speed*Math.sin(angle);

        imageAngle+=1;
        imageAngle %= 360f;

        imageTransform.setToRotation(Math.toRadians(imageAngle), imageCenter.x, imageCenter.y);





        imageX+=xSpeed;
        imageY+=ySpeed;

        if(imageX>1500)imageX = 0;
        if(imageX< -100)imageX = 1500;
        if(imageY>800)imageY = 0;
        if(imageY< -100)imageY = 800;


    }
    //draws the rock and collision around it
    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.DARK_GRAY);
        move();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        AffineTransformOp affineOp = new AffineTransformOp(imageTransform, AffineTransformOp.TYPE_BILINEAR);
        //player.paint(g2d);
        g2d.drawImage(image, affineOp, Math.round(imageX), Math.round(imageY));
        ovalX = Math.round(imageX+image.getWidth()/2)-(int)(size*50);
        ovalY = Math.round(imageY+image.getHeight()/2)-(int)(size*50);
        g2d.drawOval(ovalX, ovalY, (int)(size*120),(int)(size*120));

        //g2d.dispose();
    }
}