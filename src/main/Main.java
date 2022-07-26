package main;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {

		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("2D Adventure");
		
		GamePanel gamePanel = new GamePanel();
		window.add(gamePanel); //adds the gamePanel to the window
		
		window.pack(); //so it can be seen
		
		window.setLocationRelativeTo(null); //window displayed cetner of screen
		window.setVisible(true); //so we can see the window
		
		gamePanel.setupGame();
		gamePanel.startGameThread(); 
	}

}
