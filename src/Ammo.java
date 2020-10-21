//(c) A+ Computer Science
//www.apluscompsci.com
//Name -

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;

public class Ammo
{
	private int speed; // speed of laser
	private int x, y; // location of laser
	private double angle; // angle at which laser moves
	private boolean collided; // checks if laser collided with something
	private boolean type; // determines type of laser - player of alien

	//creates laser object
	public Ammo(int x, int y, int s, double angle, boolean type)
	{
		this.x = x;
		this.y = y;
		speed = s;
		this.angle = angle;
		collided = false;
		this.type = type;
		//add code
	}

	//changes speed of laser
	public void setSpeed(int s)
	{
	   speed = s;//add code
	}
	//returns speed of laser
	public int getSpeed()
	{
	   return speed;
	}

	//draws laser
	public void draw( Graphics window )
	{
		window.setColor(Color.GREEN);
		if(type)
		window.setColor(Color.red);
		window.fillRect(x,y,5,5);//add code to draw the ammo

	}

	public int getX()
	{
		return x;
	} // returns x coordinate of laser
	public int getY()
	{
		return y;
	} // returns y coordinate of laser
	public void move() // moves the laser
	{
		double toRad = Math.toRadians(angle);
		x+=speed*Math.cos(toRad);
		y+=speed*Math.sin(toRad);


			
	}
	public boolean getCollided()
	{
		return collided;
	} // checks if laser hit something
	public void setCollided()
	{
		collided = !collided;
	} // changes status of laser if it hits something
	//does nothing
	public String toString()
	{
		return "";
	}
}
