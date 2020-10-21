import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BigAlien {

    //int tempCounter = 0;
    private int lives;//big alien lives
    private boolean gotHit = false;//checks to see if big alien gets hit
    private double size, speed, angle; //parameters of big alien
    private BufferedImage image;//image of big alien
    private BufferedImage preScaleSprite;//image before alien is scaled
    private Image scaledSprite;//image after alien is scaled
    private float imageX, imageY;//location of the alien
    private Point imageCenter;//center of the alien
    private double xSpeed,ySpeed;//speed of alien
    private AffineTransform imageTransform;//transforms alien so it can rotate
    private int imageAngle;//determines the angle of the image
    private int ovalX,ovalY;//collision locations

    public BigAlien(float x, float y, double angle, double speed, int lives)
    {
        this.lives = lives;
        imageX = x;
        imageY = y;
        this.angle = angle;
        this.size = 0.25;
        this.speed = speed;
        xSpeed = speed*Math.cos(angle);
        ySpeed = speed*Math.sin(angle);

        float scale = (float)0.5;
        /*
        * resizes the big alien and gives it the ability to rotate if necessary
        * */
        try {
            //creates image
            preScaleSprite = ImageIO.read(new File("resources/bigalien.png"));
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
    } // returns the X coordinate of the alien
    public float getSpawnX() { return (float)(200+(int)(Math.random()*(1500 - 100)));} // gets the spawned location of the big alien
    public float getSpawnY() { return (float)(200+(int)(Math.random()*(800 - 100)));} // gets the spawned location of the big alien

    public int getLives()
    {
        return lives;
    } // gets the number of lives
    public void setLives(int l){
        lives = l;
    } // changes the number of lives

    public double getY(){
        return ovalY + getScale()/2;
    } // returns the Y coordinate of the alien

    public int getScale(){
        return (int)(size*500);
    } // gets the scaled size of the big alien
    public boolean hit(){
        return gotHit;
    } // checks if alien got hit
    public void setHit() { gotHit = false;} // changes if alien got hit

    //checks if big alien got hit by a laser
    public boolean hitAlien(Ammo shot) {
        if(Math.sqrt(Math.pow(shot.getX()-(ovalX+getScale()/2),2)+Math.pow(shot.getY()-(ovalY+getScale()/2),2))<getScale()/2) {
            gotHit = true;
            return true;

        }
        return false;
    }


    //moves the player
    public void move(){


        imageAngle %= 360f;

        imageTransform.setToRotation(Math.toRadians(imageAngle), imageCenter.x, imageCenter.y);





        imageX+=xSpeed;
        imageY+=ySpeed;

        if(imageX>1380) xSpeed = -xSpeed; //System.out.println("it happened");}
        if(imageX< 0)xSpeed = -xSpeed;
        if(imageY>630) ySpeed = -ySpeed;
        if(imageY< 0) ySpeed = -ySpeed;


    }
    //draws the big alien and the collision around it
    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.DARK_GRAY);
        move();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        AffineTransformOp affineOp = new AffineTransformOp(imageTransform, AffineTransformOp.TYPE_BILINEAR);
        //player.paint(g2d);
        g2d.drawImage(image, affineOp, Math.round(imageX), Math.round(imageY));
        ovalX = Math.round(imageX+image.getWidth()/2)-(int)(size*250);
        ovalY = Math.round(imageY+image.getHeight()/2)-(int)(size*250);
        g2d.setColor(Color.WHITE);
        g2d.drawOval(ovalX, ovalY, (int)(size*500),(int)(size*500));
        //health bar
        g2d.setColor(Color.RED);
        g2d.fillRect(ovalX-10,ovalY-25,125,7);
        //green
        g2d.setColor(Color.GREEN);
        g2d.fillRect(ovalX-10,ovalY-25,lives*5,7);
        //System.out.println(ovalX + " " + ovalY);
        //g2d.dispose();
    }
}
