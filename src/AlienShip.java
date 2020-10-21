import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AlienShip
{
    private int lives; // number of lives of the alien
    private boolean gotHit = false; // checks to see if the alien got hit
    private double size, speed, angle; // parameters of the alien
    private BufferedImage image; // image of the alien
    private BufferedImage preScaleSprite; // image before scaled
    private Image scaledSprite; // image after scaled
    private float imageX, imageY; // location of the image
    private Point imageCenter; // center of the image
    private double xSpeed,ySpeed; // speed of the alien
    private AffineTransform imageTransform; // transform the alien so it can rotate
    private int imageAngle; // angle at which the alien bounces
    private int ovalX,ovalY; // location of the alien

    public AlienShip(float x, float y, double angle, double speed, int lives)
    {
        this.lives = lives;
        imageX = x;
        imageY = y;
        this.angle = angle;
        this.size = 0.25;
        this.speed = speed;
        xSpeed = speed*Math.cos(angle);
        ySpeed = speed*Math.sin(angle);

        float scale = (float)0.25;
        //draws, rescales, and allows the alien to rotate
        try {
            //creates image
            preScaleSprite = ImageIO.read(new File("resources/alienspaceship.png"));
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

    public double getX(){
        return ovalX+getScale()/2;
    } // gets x location of alien

    public int getLives()
    {
        return lives;
    } // gets number of lives of alien
    public void setLives(int l){
        lives = l;
    } // sets number of lives of alien

    public double getY(){
        return ovalY + getScale()/2;
    } // gets y location of alien


    public int getScale(){
        return (int)(size*200);
    } // gets scale of alien
    public boolean hit(){
        return gotHit;
    } // checks if alien got hit
    public void setHit() { gotHit = false;} // sets hit if alien gets hit

    // checks if alien is hit by laser
    public boolean hitAlien(Ammo shot) {
        if(Math.sqrt(Math.pow(shot.getX()-(ovalX+getScale()/2),2)+Math.pow(shot.getY()-(ovalY+getScale()/2),2))<getScale()/2) {
            gotHit = true;
            return true;

        }
        return false;
    }


    //moves the alien
    public void move(){


        imageAngle %= 360f;

        imageTransform.setToRotation(Math.toRadians(imageAngle), imageCenter.x, imageCenter.y);





        imageX+=xSpeed;
        imageY+=ySpeed;

        if(imageX>1400) xSpeed = -xSpeed; //System.out.println("it happened");}
        if(imageX< 0)xSpeed = -xSpeed;
        if(imageY>700) ySpeed = -ySpeed;
        if(imageY< 0) ySpeed = -ySpeed;


    }
    //draws the alien, the health bar, and its collisions
    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.DARK_GRAY);
        move();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        AffineTransformOp affineOp = new AffineTransformOp(imageTransform, AffineTransformOp.TYPE_BILINEAR);
        //player.paint(g2d);
        g2d.drawImage(image, affineOp, Math.round(imageX), Math.round(imageY));
        ovalX = Math.round(imageX+image.getWidth()/2)-(int)(size*100);
        ovalY = Math.round(imageY+image.getHeight()/2)-(int)(size*100);
        g2d.setColor(Color.WHITE);
        g2d.drawOval(ovalX, ovalY, (int)(size*200),(int)(size*200));
        //health bar
        g2d.setColor(Color.RED);
        g2d.fillRect(ovalX-10,ovalY-25,60,7);
        //green
        g2d.setColor(Color.GREEN);
        g2d.fillRect(ovalX-10,ovalY-25,lives*20,7);
        //System.out.println(ovalX + " " + ovalY);
        //g2d.dispose();
    }
}
