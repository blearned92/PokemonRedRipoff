package tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {

	GamePanel gp;
	public Tile[] tile;
	public int mapTileNum[][];
	
	public TileManager(GamePanel gp) { //when constructor is called, everything below happens
		this.gp = gp;
		
		tile = new Tile[20]; //ten types of tiles
		mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
		
		getTileImage();
		loadMap("/maps/veridianForest.txt");
	}
	
	public void getTileImage() {
//			setup(0, "grass_1", false);
//			setup(1, "wall", true);
//			setup(2, "water", true);
//			setup(3, "earth", false);
//			setup(4, "tree", true);
//			setup(5, "grass_0", false);		
//			setup(6, "tree_1", true);
//			setup(7, "tree_2", true);
//			setup(8, "tree_3", true);
//			setup(9, "tree_4", true);
			
	

			setup(0, "blackspace", true);
			setup(1, "grass_0", false);
			setup(2, "grass_1", false);
			setup(3, "grass_2", false);
			setup(4, "grass_3", false);
			setup(5, "grass_4", false);		
			setup(6, "grass_5", false);
			setup(7, "tree_1", true);
			setup(8, "tree_2", true);
			setup(9, "tree_3", true);
			setup(10, "tree_4", true);
			setup(11, "stump", true);
			
	}
	
	public void setup(int index, String imageName, boolean collision) {
		UtilityTool uTool = new UtilityTool();
		try {
			tile[index] = new Tile();
			tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png"));
			tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
			tile[index].collision = collision;

		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadMap(String filePath) {
		try {
			InputStream is = getClass().getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			//BufferedReader br will read the content of the map01.txt file. 
			int col = 0;
			int row = 0;
			while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
				String line = br.readLine(); 
				/*reads a single line and stores it into the variable "line" as one large strong*/
				while(col < gp.maxWorldCol) {
					String numbers[] = line.split(" "); 
					//splits every entry and puts it into an array as single strings
					int num = Integer.parseInt(numbers[col]);
					// turns each string into a usable number
					mapTileNum[col][row] = num;
					col++;
				}
				if(col == gp.maxWorldCol) {
					col = 0;
					row++;
				}
			}
			br.close();
		}catch(Exception e){
		}
	}
	
	public void draw(Graphics2D g2) {
		
		int worldCol = 0;
		int worldRow = 0;
		
		while(worldCol<gp.maxWorldCol && worldRow < gp.maxWorldRow) {
			
			int tileNum = mapTileNum[worldCol][worldRow];
			//just analyze this closely and it will make sense
			
			int worldX = worldCol * gp.tileSize; //position on the map
			int worldY = worldRow * gp.tileSize;
			int screenX = worldX - gp.player.worldX + gp.player.screenX; //where on the screen x is drawn
			int screenY = worldY - gp.player.worldY + gp.player.screenY;

			if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
					worldX - gp.tileSize< gp.player.worldX + gp.player.screenX &&
					worldY + gp.tileSize> gp.player.worldY - gp.player.screenY &&
					worldY - gp.tileSize< gp.player.worldY + gp.player.screenY) {
				
				g2.drawImage(tile[tileNum].image, screenX, screenY, null);
			} 
			/*With the loop above, before drawing tiles, it will check to see if the tiles fall within
			 * a range of the player character in all 4 directions, and THEN draw them, to not waste 
			 * resources processing the whole map!*/
			
			worldCol++;
			
			if(worldCol == gp.maxWorldCol) {
				worldCol = 0;
				worldRow++;
			}
		}
		
		
		
		
	}
}
