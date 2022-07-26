package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import object.OBJ_Key;


public class UI {

	GamePanel gp;
	Font arial_30;
	BufferedImage keyImage;
	public boolean messageOn = false;
	public String message = "";
	public int messageCounter = 0;
	
	public UI(GamePanel gp) {
		this.gp = gp;
		
		arial_30 = new Font("Arial", Font.PLAIN, 30);
		OBJ_Key key = new OBJ_Key(gp);
		keyImage = key.image;
	}
	
	public void showMessage(String text) {
		message = text;
		messageOn = true;
	}
	
	public void draw(Graphics2D g2) {
		g2.setFont(arial_30);
		g2.setColor(Color.white);
		g2.drawImage(keyImage, gp.tileSize/2, gp.tileSize/2-15, gp.tileSize, gp.tileSize, null);
		g2.drawString("x " + gp.player.hasKey, 74, 50);
		
		if(messageOn == true) {
			g2.setFont(g2.getFont().deriveFont(30F));
			int textLength = (int)g2.getFontMetrics().getStringBounds(message, g2).getWidth();
			g2.drawString(message, gp.screenWidth/2- textLength/2, gp.screenHeight - 50);
			
			messageCounter++;
			if(messageCounter > 120) {
				messageCounter=0;
				messageOn = false;
			}
		}
	}
	

	
}
