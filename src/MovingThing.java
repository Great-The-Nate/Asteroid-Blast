//(c) A+ Computer Science
//www.apluscompsci.com
//Name -

import java.awt.Color;
import java.awt.Graphics;

public abstract class MovingThing implements Moveable
{
	private int xPos;
	private int yPos;
	private int width;
	private int height;
	private int speed;

	public MovingThing()
	{
		xPos = 10;
		yPos = 10;
		width = 10;
		height = 10;
	}

	public MovingThing(int x, int y)
	{
		xPos = x;
		yPos = y;
		width = 10;
		height = 10;
	}

	public MovingThing(int x, int y, int w, int h, int s)
	{
		xPos = x;
		yPos = y;
		width = w;
		height = h;//add code here
		speed = s;
	}

	public void setPos( int x, int y)
	{
		xPos = x;
		yPos = y;//add code here
	}

	public void setX(int x)
	{
		xPos = x;//add code here
	}

	public void setY(int y)
	{
		yPos = y;//add code here
	}

	public int getX()
	{
		return xPos;   //finish this method
	}

	public int getY()
	{
		return yPos;  //finish this method
	}

	public void setWidth(int w)
	{
		width = w;//add code here
	}

	public void setHeight(int h)
	{
		height = h;//add code here
	}

	public int getWidth()
	{
		return width;  //finish this method
	}

	public int getHeight()
	{
		return height;  //finish this method
	}

	public abstract void move(int cx, int cy);
	public abstract void draw(Graphics window);

	public String toString()
	{
		return getX() + " " + getY() + " " + getWidth() + " " + getHeight();
	}
	
    public void setSpeed( int s )
    {
    	speed = s;
    }
	 public int getSpeed()
	 {
	 	return speed;
	 }
}