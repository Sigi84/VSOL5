package be.veterinarysolutions.vsol.main;

import javafx.stage.Stage;

import java.io.IOException;

public class Ctrl {
	
	private Database db;
	private Gui gui;
	
	public Ctrl(Stage primaryStage) {

//		db = new Database();

		try {
			gui = new Gui(primaryStage);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		init();
	}
	
	// PRIVATE
	
	private void init() {
		
	}
	
	// GETTERS
	
	public Database getDb() { return db; }
	
}
