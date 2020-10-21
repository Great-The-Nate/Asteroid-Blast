import java.awt.Graphics;
import java.awt.*;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.IOException;
   import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class test extends JPanel {
	
	private boolean[] keys = new boolean[3];
//	private double angle = 45;
double rotationRequired; //= Math.toRadians (angle);
double locationX;
double locationY;
AffineTransform tx;
AffineTransformOp op;
Graphics g1;

public void rotatemedaddyuwu(Graphics g)
{

	
}


  public void paint(Graphics g) {
  	g1 = g;
  	Graphics2D g2d = (Graphics2D)g;
  	BufferedImage originalImage = null;
			try {

			originalImage = ImageIO.read(new File(
					"C:\\Users\\706664\\Desktop\\EOY Project\\urapieceofship.png"));
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}

 
 	// The required drawing location
int drawLocationX = 300;
int drawLocationY = 300;

// Rotation information
	
	if(keys[0] == true)
{
	rotationRequired = Math.toRadians(-45);
 locationX = 256 / 2;
 locationY = 256 / 2;
 tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

}
if(keys[1] == true)
{
	rotationRequired = Math.toRadians(45);
 locationX = 256 / 2;
 locationY = 256 / 2;
 tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
 op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

}
// Drawing the rotated image at the required drawing locations
if (originalImage == null)
	System.err.println("u fuked up man originaimage was null");
if (op == null)
	System.err.println("u fuked up man op was null");
if(keys[1]==true || keys[0]==true)
g2d.drawImage(op.filter(originalImage, null), drawLocationX, drawLocationY, null);

repaint();
  }
  
  

  public static void main(String[] args) {
    JFrame frame = new JFrame();
    frame.getContentPane().add(new test());

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(200,200);
    frame.setVisible(true);
    
    
    
  }
  
  	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_UP)
		{
			keys[2] = true;
			rotatemedaddyuwu(g1);
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
		{
			keys[0] = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			keys[1] = true;
		}
		repaint();
	}

	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_UP)
		{
			keys[2] = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
		{
			keys[0] = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			keys[1] = false;
		}
		repaint();
	}

	public void keyTyped(KeyEvent e) {}

   	public void run()
   	{
  	 	try
   	{
   		while(true)
   		{
   		   Thread.currentThread().sleep(5);
            repaint();
         }
    }
    	catch(Exception e){}
  	}
  
}
           
         