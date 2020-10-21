//(c) A+ Computer Science
//www.apluscompsci.com
//Name -

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;

public class Bullets
{
	private List<Ammo> ammo;

	//creates arraylist of ammo
	public Bullets()
	{
		ammo = new ArrayList<Ammo>();
	}
	//adds a new ammo
	public void add(Ammo al)
	{
		ammo.add(al);
	}

	//draws all the ammo
	public void drawEmAll( Graphics window )
	{
		for(int i= 0; i<ammo.size(); i++){
			ammo.get(i).draw(window);
			}
	}
	//moves all the ammo
	public void moveEmAll()
	{
		for(int i = 0; i<ammo.size(); i++){
			ammo.get(i).move();
			}
	}
	//cleans up all the ammo
	public void cleanEmUp()
	{
		for(int i =0; i<ammo.size(); i++){
			if(ammo.get(i).getY()<0 || ammo.get(i).getY()>800 || ammo.get(i).getX()<0 || ammo.get(i).getX()>1500||ammo.get(i).getCollided())
			{
				ammo.remove(i);
				i--;
			}
		}
		
	}
	//returns list of the ammo
	public List<Ammo> getList()
	{
		return ammo;
	}
	//does nothing
	public String toString()
	{
		return "";
	}
}
