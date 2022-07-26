package entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

public class Player extends Entity{

	GamePanel gp;
	KeyHandler keyH;
	
	public final int screenX;
	public final int screenY; 
	public int hasKey = 0;
	boolean moving = false;
	int pixelCounter = 0;
	/*This is the player character's screen position and it will never change*/
	
	public Player(GamePanel gp, KeyHandler keyH) {
		this.gp = gp;
		this.keyH = keyH;
		
		screenX = gp.screenWidth/2 - (gp.tileSize/2);
		screenY = gp.screenHeight/2 - (gp.tileSize/2); 
		//this is the halfway point of the screen at all times.
		solidArea = new Rectangle(8, 16, 32, 32); //x, y, w, h
		//defines the parameters of the Player's solid spots for collision
		solidArea.x = 1;
		solidArea.y = 1;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 46;
		solidArea.height = 46;
		
		setDefaultValues();
		getPlayerImage();
	}
	
	public void setDefaultValues() {
		worldX = gp.tileSize * 17; //17, 48 is the final to set to
		worldY = gp.tileSize * 48; //this will be the starting point of the character
		speed = 3;
		direction = "up";
	}
	
	public void getPlayerImage() {	
		up0 = setup("red_u1");
		up1 = setup("red_u2");
		up2 = setup("red_u3");
		down0 = setup("red_d1");
		down1 = setup("red_d2");
		down2 = setup("red_d3");
		left1 = setup("red_l1");
		left2 = setup("red_l2");
		right1 = setup("red_r1");
		right2 = setup("red_r2");	
	}
	
	public BufferedImage setup(String imageName) {
		UtilityTool uTool = new UtilityTool();
		BufferedImage image = null;
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/player/" + imageName + ".png"));
			image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
		}catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	public void update() { //this method gets called 60 times per second\
		
		if(moving == false) {
			if(keyH.upPressed == true 
					|| keyH.downPressed == true
					|| keyH.leftPressed == true 
					|| keyH.rightPressed == true) {
				if(keyH.upPressed == true) {
					direction = "up";
				} else if(keyH.downPressed == true) {
					direction = "down";
				} else if(keyH.leftPressed == true) {
					direction = "left";
				} else if(keyH.rightPressed == true) {
					direction = "right";
				}
				
				moving = true;
				
				//CHECK TILE COLLISION
				collisionOn = false;
				gp.cChecker.checkTile(this);
				
				//CHECK OBJECT COLLISION
				int objectIndex = gp.cChecker.checkObject(this, true);
				pickUpObject(objectIndex);
			} else {
				spriteNum=1;
				
				
			}
		}	
		
		if(moving == true) {
			//IF COLLISION IS FALSE, PLAYER CAN MOVE
			if(collisionOn == false) {
				switch(direction) {
				case "up": worldY -= speed; break;
				case "down": worldY += speed; break;
				case "left": worldX -= speed; break;
				case "right": worldX += speed; break;
				}
			}
			
			spriteCounter++;
			if(spriteCounter > 12) { //controls animation speed
				if(spriteNum == 1) {
					spriteNum = 2;
				} else if(spriteNum == 2) {
					spriteNum = 3;
				}else if(spriteNum == 3) {
					spriteNum = 4;
				}else if(spriteNum == 4) {
					spriteNum = 1;
				}
				spriteCounter = 0;
			}
			
			pixelCounter += speed;
			
			if(pixelCounter==48) {
				moving = false;
				pixelCounter = 0;
			}
		}
	}
	
	public void pickUpObject(int i) { //controls picking up items
		if(i!=999) {
			String objectName = gp.obj[i].name;
			
			switch(objectName) {
			case "Key":
				if(keyH.actionPressed == true) {
					gp.playSE(2);
					gp.obj[i] = null;
					hasKey++;
					gp.ui.showMessage("You found a PokeFlute!");
				}
				break;
			case "Door":
				if(hasKey>0 && keyH.actionPressed == true) {
					gp.playSE(1);
					gp.ui.showMessage("You used the PokeFlute!");
					gp.obj[i] = null;
					hasKey--;
					gp.ui.showMessage("With a big yawn, SNORLAX returned to the mountains!");
				} else if(keyH.actionPressed == true){
					gp.ui.showMessage("A sleeping POKeMON \nblocks the way!");
				}
				System.out.println("Key:"+hasKey);
				break;
			case "Chest":
				if(keyH.actionPressed == true) {
					gp.playSE(2);
					gp.obj[i] = null;
					hasKey++;
					gp.ui.showMessage("You obtained the Master Ball!");
				}
				break;
			}
		}
	}
	
	public void draw(Graphics2D g2) {
		
//		g2.setColor(Color.MAGENTA);
//		g2.fillRect(x, y, gp.tileSize, gp.tileSize);
		BufferedImage image = null;
		switch(direction) {
		case "up":
			if(spriteNum==1 || spriteNum==3) {
				image=up0;
			}
			if(spriteNum==2) {
				image=up1;
			}
			if(spriteNum==4) {
				image=up2;
			}
			break;
		case "down":
			if(spriteNum==1 || spriteNum==3) {
				image=down0;
			}
			if(spriteNum==2) {
				image=down1;
			}
			if(spriteNum==4) {
				image=down2;
			}
			break;
		case "left":
			if(spriteNum==1 || spriteNum==3) {
				image=left1;
			}
			if(spriteNum==2 || spriteNum == 4) {
				image=left2;
			}
			break;
		case "right":
			if(spriteNum==1 || spriteNum == 3) {
				image=right1;
			}
			if(spriteNum==2 || spriteNum == 4) {
				image=right2;
			}
			break;
		}
		g2.drawImage(image, screenX, screenY, null);
		//draw image at x and y, for the sizes of tileSize height and width
	}
}
