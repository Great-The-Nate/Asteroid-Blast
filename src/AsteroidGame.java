import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Canvas;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.sound.sampled.*;
import java.awt.Font;

public class AsteroidGame extends Canvas implements KeyListener, Runnable
{
	private int GAME_WIDTH = 1500;//width of game
	private int GAME_HEIGHT = 800;//height of game
	private int level = 1;//what level are you on

	Rock temp = null;//used when splitting rocks
	private boolean hitRock = false;//has a rock gotten hit by a bullet
	private boolean gameStarted = true;//is the game playing
	private boolean startedGame = true;
	private long lastSpawnTime = 0;//when was the last time player died
	private long lastAlienShotTime = 0;//when was the last time aliens shot
	private long lastSpawnAlienTime = 0;//when was the last time big alien spawned small alien


	private long transitionTime = System.currentTimeMillis();//determines how long the transition lasts
	private boolean transition = true;//is the game transitioning between levels
	private boolean respawning = false;//is the player respawning

	private boolean alienShot = false;//has the alien shot
	private long lastShotTime = 0;//when was the last time player shot
	private BufferedImage back;//background image
	private Player ship;//player object
	private ArrayList<Rock> rocks;//rocks for level 1
	private ArrayList<AlienShip> aliens;//aliens for level 2
	private Bullets playerBullets;//player bullets
	private ArrayList<Bullets> alienBullets;//alien bullets for level 2

	private BigAlien bigAlien;//big alien object for level 3
	private ArrayList<AlienShip> spawnedAliens;//spawned aliens in level 3
	private ArrayList<Bullets> spawnedBullets;//spawned bullets in level 3

	private int numRocks;//number of rocks in level 1
	/*
	* images when game is over
	* */
	Image image;
	Image youlost;
	Image gameover;
	
	public AsteroidGame()
	{
		setBackground(Color.black);
		try
		{
			//creates the images when player dies
			image = ImageIO.read(new File("resources/gamescreenBack.jpg"));
			youlost = ImageIO.read(new File("resources/youlost.png"));
			gameover = ImageIO.read(new File("resources/gameover.png"));
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File ("resources/sound.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
		}
		catch(Exception e)
		{
			//feel free to do something here
		}

		numRocks = 4;//four rocks in level 1
		ship= new Player(5);//player has 5 lives
		rocks= new ArrayList<Rock>();//rocks are instantiated
		aliens = new ArrayList<AlienShip>();//aliens are instantiated
		playerBullets = new Bullets();//player bullets are instantiated
		alienBullets = new ArrayList<Bullets>();//alien bullets are instantiated
		spawnedBullets = new ArrayList<Bullets>();//spawned bullets are instantiated
		bigAlien = new BigAlien(750,200,Math.toRadians(Math.random()*360), 3, 25);//determines a random location to spawn big alien
		spawnedAliens = new ArrayList<AlienShip>();//spawned aliens arraylist instantiated
		for(int i = 0; i<4; i++)
			alienBullets.add(new Bullets());//adds new bullets for the aliens in level 2
		/*
		* temporary variables to randomize location of rocks
		* */
		int tempRockX;
		int tempRockY;
		double tempRockAngle;

		//draws all the rocks and aliens
		for(int i = 0; i<numRocks; i++){
			tempRockX = (int)(Math.random()*(GAME_WIDTH+1300));
			tempRockY = (int)(Math.random()*(GAME_HEIGHT+500));

			tempRockAngle = Math.random()*360;
			tempRockAngle = Math.toRadians(tempRockAngle);

			rocks.add(new Rock(tempRockX, tempRockY, tempRockAngle, 1, 0.5));
			//draws all the aliens
			tempRockX = 200+(int)(Math.random()*(GAME_WIDTH - 100));
			tempRockY = 200 + (int)(Math.random()*(GAME_HEIGHT -100));
			tempRockAngle = Math.random()*360;
			aliens.add(new AlienShip(tempRockX,tempRockY,tempRockAngle,2, 3));
		}

		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
				ship.released(e.getKeyCode());
				//System.out.println("released");
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_SPACE&&(System.currentTimeMillis() - lastShotTime >= 200)) {
					playerBullets.add(new Ammo(ship.getX(), ship.getY(), 5, ship.getAngle(),false));
					lastShotTime = System.currentTimeMillis();

				}
				else
					ship.pressed(e.getKeyCode());

			}
		});

		this.addKeyListener(this);
		new Thread(this).start();
		setVisible(true);
	}
	
	public void paint( Graphics window )
	{
		//set up the double buffering to make the game animation nice and smooth
		Graphics2D twoDGraph = (Graphics2D)window;

		//draws background
		if(back==null)
		   back = (BufferedImage)(createImage(getWidth(),getHeight()));

		//create a graphics reference to the back ground image
		//we will draw all changes on the background image
		Graphics g = back.createGraphics();

		//draws background
		g.drawImage(image,getX(),getY(),getWidth(),getHeight(),null);

		//if the player has not died yet
		if(ship.getLives()>0) {


			//draws ship and lives
			ship.draw(g);
			//creates player bullets objects
			playerBullets.drawEmAll(g);
			playerBullets.moveEmAll();
			playerBullets.cleanEmUp();

			//Level 1
			if(level == 1)
			{
				//starts transition for first 3 seconds
				if(startedGame){
					startedGame = false;
					transitionTime  = System.currentTimeMillis();
				}
				if(transition){
					Font f = new Font("Bauhaus 93", Font.BOLD, 80);
					g.setFont(f);
					g.setColor(Color.GREEN);
					g.drawString("LEVEL 1",600,100);
				}
				if(transition && System.currentTimeMillis()-transitionTime>3000)
				{
					ship.changeShield();
					transition = false;
				}
				//draws rocks
				for (int i = 0; i < rocks.size(); i++)
					rocks.get(i).draw(g);

				//move rocks
				for(int i = 0; i<rocks.size(); i++)
				{
					rocks.get(i).move();
				}

				//deletes ammo that hits rocks and sets rock to hit in method hitRock
				for (Rock r : rocks) {
					for (Ammo ammo : playerBullets.getList()) {
						if (r.hitRock(ammo))
							ammo.setCollided();
					}
				}
				//gets rid of hit rocks;
				for (int i = 0; i < rocks.size(); i++) {
					if (rocks.get(i).hit()) {
						temp = rocks.get(i);
						rocks.remove(rocks.get(i));

						hitRock = true;
						numRocks--;
					}
				}


				//splits rocks
				if(hitRock == true)
				{
					hitRock = false;
					if(temp.getScale()>= 60) {
						numRocks += 2;
						rocks.add(new Rock((float) (temp.getOvalX() * 2 + temp.getScale()), (float) (temp.getOvalY() * 2 + temp.getScale()), 180 + temp.getAngle(), temp.getSize() / 1.5, temp.getSpeed()*1.4));
						rocks.add(new Rock((float) (temp.getOvalX() * 2 + temp.getScale()), (float) (temp.getOvalY() * 2 + temp.getScale()), 90 + temp.getAngle(), temp.getSize() / 1.5, temp.getSpeed()*1.4));

					}
				}
				//if the rocks are all destroyed, level is now 2
			if(rocks.size() == 0)
			{
				transitionTime = System.currentTimeMillis();
				ship = new Player(ship.getLives());
				transition = true;
				level = 2;
			}


			}

			//Level 2
			if(level == 2)
			{
				if(transition){
					Font f = new Font("Bauhaus 93", Font.BOLD, 80);
					g.setFont(f);
					g.setColor(Color.GREEN);
					g.drawString("LEVEL 2",600,100);
				}
				if(transition && System.currentTimeMillis()-transitionTime > 3000)
				{
					transition = false;
					ship.changeShield();
				}

				//draws aliens
				for(int i = 0; i < aliens.size(); i++)
					aliens.get(i).draw(g);
				//draws alien lasers
				for(int i = 0; i<alienBullets.size(); i++)
				{
					alienBullets.get(i).drawEmAll(g);
					alienBullets.get(i).moveEmAll();
					alienBullets.get(i).cleanEmUp();
				}
				//aliens shoot lasers every 2 seconds
				if(alienShot == false && System.currentTimeMillis()-lastAlienShotTime > 3000)
				{
					alienShot = true;
					lastAlienShotTime = System.currentTimeMillis();
					for(int i = 0; i<alienBullets.size(); i++)
					{
						double angleBW = Math.atan2(ship.getY()-aliens.get(i).getY(),ship.getX()-aliens.get(i).getX());
						alienBullets.get(i).add(new Ammo((int)aliens.get(i).getX(), (int)aliens.get(i).getY(), 5, Math.toDegrees(angleBW), true));

					}

				}
				else if(alienShot)
					alienShot=false;





				//deletes ammo when hits player, sets alien to collided in method hitAlien
				for(int i = 0; i<alienBullets.size(); i++)
				{
					for(Ammo ammo : alienBullets.get(i).getList()){
							if(ship.hitPlayer(ammo))
								ammo.setCollided();
					}
				}


				//deletes ammo when hits alien, sets alien to collided in method hitAlien
				for(Ammo ammo : playerBullets.getList()){
					for(int i = 0; i<aliens.size(); i++)
						if(aliens.get(i).hitAlien(ammo))
							ammo.setCollided();
				}

				//removes hit aliens
				for(int i = 0; i<aliens.size(); i++){
					if(aliens.get(i).hit()){
						aliens.get(i).setLives(aliens.get(i).getLives()-1);
						aliens.get(i).setHit();
						//System.out.println(aliens.get(i).getLives());
					}
					if(aliens.get(i).getLives() == 0) {
						aliens.remove(i);
						alienBullets.remove(i);
					}
				}
				//if there are no more aliens, level is now 3
				if(aliens.size() == 0)
				{
					transitionTime = System.currentTimeMillis();
					ship = new Player(ship.getLives());
					//System.out.println(ship.hasShield());
					transition = true;
					level = 3;
				}

			}
			//Level 3
			if(level == 3)
			{
				if(transition){
					Font f = new Font("Bauhaus 93", Font.BOLD, 80);
					g.setFont(f);
					g.setColor(Color.GREEN);
					g.drawString("LEVEL 3",600,100);
				}
				if(transition && System.currentTimeMillis()-transitionTime > 3000)
				{
					transition = false;
					ship.changeShield();
				}
				//draws big alien if it is not dead
				if(bigAlien.getLives() > 0) {
					bigAlien.draw(g);
				}
				//checks to see if big alien is hit by player bullets
				for(Ammo ammo : playerBullets.getList()){
					if(bigAlien.hitAlien(ammo))
						ammo.setCollided();
				}
				//if big alien is hit, it loses lives
				if(bigAlien.hit()){
					bigAlien.setLives(bigAlien.getLives()-1);
					bigAlien.setHit();
				}
				//big alien spawns small aliens every 10 seconds
				if(bigAlien.getLives()>0 && System.currentTimeMillis()-lastSpawnAlienTime > 10000)
				{
					lastSpawnAlienTime = System.currentTimeMillis();
					spawnedAliens.add(new AlienShip((float)bigAlien.getSpawnX(), (float)bigAlien.getSpawnY(),Math.random()*360, 2, 3));
					spawnedBullets.add(new Bullets());

				}
				//draws spawned aliens
				for(int i = 0; i < spawnedAliens.size(); i++)
					spawnedAliens.get(i).draw(g);
				//draws spawned bullets
				for(int i = 0; i<spawnedBullets.size(); i++) {
					spawnedBullets.get(i).drawEmAll(g);
					spawnedBullets.get(i).moveEmAll();
					spawnedBullets.get(i).cleanEmUp();
				}
				//spawned aliens shoot every 3 seconds
				if(alienShot == false && System.currentTimeMillis()-lastAlienShotTime > 3000)
				{
					alienShot = true;
					lastAlienShotTime = System.currentTimeMillis();
					for(int i = 0; i<spawnedBullets.size(); i++)
					{
						double angleBW = Math.atan2(ship.getY()-spawnedAliens.get(i).getY(),ship.getX()-spawnedAliens.get(i).getX());
						spawnedBullets.get(i).add(new Ammo((int)spawnedAliens.get(i).getX(), (int)spawnedAliens.get(i).getY(), 5, Math.toDegrees(angleBW), true));

					}

				}
				else if(alienShot)
					alienShot=false;
				//checks to see if spawned aliens are hit by player bullets
				for(Ammo ammo : playerBullets.getList()){
					for(int i = 0; i<spawnedAliens.size(); i++)
						if(spawnedAliens.get(i).hitAlien(ammo))
							ammo.setCollided();
				}

				//removes hit aliens
				for(int i = 0; i<spawnedAliens.size(); i++){
					if(spawnedAliens.get(i).hit()){
						spawnedAliens.get(i).setLives(spawnedAliens.get(i).getLives()-1);
						spawnedAliens.get(i).setHit();
						//System.out.println(aliens.get(i).getLives());
					}
					if(spawnedAliens.get(i).getLives() == 0) {
						spawnedAliens.remove(i);
						spawnedBullets.remove(i);
					}
				}

			}

			/*
			* determines all of player's kill options
			* */
			if(gameStarted == true) {
				//checks level 1
				if(level == 1)
				for (int i = 0; i < rocks.size(); i++) {
					if (ship.hitShip(rocks.get(i)))
						ship.changeAlive();
				}
				//checks level 2
				if(level == 2) {
					for (int i = 0; i < aliens.size(); i++)
					{
						if (ship.hitAlien(aliens.get(i)))
							ship.changeAlive();
					}
					for(int i = 0; i<alienBullets.size(); i++)
					{
						for(Ammo ammo : alienBullets.get(i).getList()){
							if(ship.hitPlayer(ammo))
								ship.changeAlive();
						}
					}
				}
				if(level == 3)
				{
					if(ship.hitBigAlien(bigAlien))
						ship.changeAlive();
					for (int i = 0; i < spawnedAliens.size(); i++)
					{
						if (ship.hitAlien(spawnedAliens.get(i)))
							ship.changeAlive();
					}
					for(int i = 0; i<spawnedBullets.size(); i++)
					{
						for(Ammo ammo : spawnedBullets.get(i).getList()){
							if(ship.hitPlayer(ammo))
								ship.changeAlive();
						}
					}
				}
				//if dead, respawns
				if (ship.checkAlive() == false) {
					respawning = true;
					ship = new Player(ship.getLives() - 1);
					lastSpawnTime = System.currentTimeMillis();
				}
				//checks how long shield lasts
				if (respawning && (System.currentTimeMillis() - lastSpawnTime) >= 2000) {
					ship.changeShield(); respawning = false;
				}
			}
			//changes shield




		}
		//you lost screen
		else {
			g.drawImage(youlost,1000,300,200,200,null);
			g.drawImage(gameover,450,150,550,420,null);
		}
		twoDGraph.drawImage(back, null, 0, 0);
	}
	
	public void keyPressed(KeyEvent e)
	{

		repaint();
	}

	public void keyReleased(KeyEvent e)
	{

		repaint();
	}

	public void keyTyped(KeyEvent e) {}

   	public void run()
   	{
  	 	try
   	{
   		while(true)
   		{
			ship.move();

			try
			{
				Thread.currentThread().sleep(5);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
				repaint();
         }
    }
    	catch(Exception e){
  	 		e.printStackTrace();
		}
  	}
	
	public void update(Graphics window)
   {
	   paint(window);
   }

}