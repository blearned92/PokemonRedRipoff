package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Currency;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JPanel;

import entity.Player;
import object.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
	
	// SCREEN SETTINGS
	final int originalTileSize = 16; //means 16x16 tile.
	final int scale = 3; 
	
	public final int tileSize = originalTileSize * scale; //48x48 tiles
	public final int maxScreenCol = 16; //amount of tiles horizontally
	public final int maxScreenRow = 12; //amount of tiles vertically
	public final int screenWidth = tileSize * maxScreenCol; //768 pixels
	public final int screenHeight = tileSize * maxScreenRow; //576 pixels
	
	//WORLD SETTINGS
	public final int maxWorldCol = 34;
	public final int maxWorldRow = 50;
//	public final int worldWitdth = tileSize * maxScreenCol;
//	public final int worldHeight = tileSize * maxScreenRow;

	
	
	
	//FPS
	int FPS = 60;
	
	TileManager tileM = new TileManager(this);
	KeyHandler keyH = new KeyHandler(); //instantiate key handler from customer made class
	Sound music = new Sound();
	Sound se = new Sound();
	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public UI ui = new UI(this);
	Thread gameThread; //this is to help implement time.

	
	// ENTITY AND OBJECT
	public Player player = new Player(this, keyH);
	public SuperObject obj[] = new SuperObject[10]; //can display up to 10 objects at a singular time.
	
	
	//constructor
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true); /*if true, all the drawing from this component will be done
			in an offscreen painting buffer*/
		this.addKeyListener(keyH); //allows the game panel to recognize the key input
		this.setFocusable(true); //gamepanel can be "focused" to receive key input		
	}
	
	public void setupGame() {
		aSetter.setObject();
		playMusic(0);
	}
	

	public void startGameThread() {
		gameThread = new Thread(this); //passing in the gamePanel class (THIS CLASS)
		gameThread.start(); //this will automatically call the run method below
	}

	@Override
//	public void run() {
//		
//		double drawInterval = 1000000000/FPS; //draw the screen 60 times per second, 0.016666 seconds
//		double nextDrawTime = System.nanoTime() + drawInterval;
//		
//		//this will be the core of the game.
//		while(gameThread != null) {
//						
//			//long currentTime = System.nanoTime(); //returns value of JVM time
//			
//			update(); //updates position data		
//			
//			repaint(); //refreshes the data visually
//			try {
//				double remainingTime = nextDrawTime - System.nanoTime(); //how much time remaining until next draw time
//				remainingTime = remainingTime/1000000; //converting to milliseconds
//				
//				if(remainingTime < 0) {
//					remainingTime = 0;
//				}
//				
//				Thread.sleep((long) remainingTime); //paused game loop for remainingTime
//				
//				nextDrawTime += drawInterval; //0.01666 seconds later
//				
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	
	public void run() {
		
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;
		
		while(gameThread != null) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime=currentTime;
			if(delta > 1) {
				update();
				repaint();
				delta--;
				drawCount++;
			}
			
			if(timer >= 1000000000) {
				System.out.println("FPS: " + drawCount);
				drawCount = 0;
				timer = 0;
			}
			
			
		}
	}

	public void update() {
		player.update();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g); //parent class inheritance 
		Graphics2D g2 = (Graphics2D)g; //instantiation of Graphics class to draw
		
		//DEBUG
		long drawStart = 0;
		if(keyH.checkDrawTime == true) {
			drawStart = System.nanoTime();
		}
		
		
		
		//TILE
		tileM.draw(g2);//this needs to be above the player draw method to be drawn under the player.
		
		//OBJECT
		for(int i=0; i<obj.length;i++) { //scan object array;
			if(obj[i]!=null) {
				obj[i].draw(g2, this);
			}
		}
		
		//PLAYER
		player.draw(g2);
		
		//UI
		ui.draw(g2);
		
		
		//DEBUG
		if(keyH.checkDrawTime == true) {
			long drawEnd = System.nanoTime();
			long passed = drawEnd - drawStart;
			g2.setColor(Color.white);
			g2.drawString("Draw Time: " + passed, 10, 400);
			System.out.println("Draw Time: " + passed);
		}
		
		
		g2.dispose(); //saves memory
	
	}
	
	public void playMusic(int i) {
		music.setFile(i);
		music.play();
		music.loop();
	}
	
	public void stopMusic() {
		music.stop();
	}
	
	public void playSE(int i) { // play sound effect
		se.setFile(i);
		se.play();
	}
}
