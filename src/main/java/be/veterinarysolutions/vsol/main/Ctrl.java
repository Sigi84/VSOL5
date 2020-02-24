package be.veterinarysolutions.vsol.main;

import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Ctrl {

	public static final String version = "0.0.5";
	public static final String versionDate = "2020-02-23";
	public static final String versionTime = "21:28";

	private static final Logger logger = LogManager.getLogger();
	private Database db;
	private Gui gui;
	
	public Ctrl(Stage primaryStage) {

		logger.info("Entered Ctrl.");

//		db = new Database();

		try {
			gui = new Gui(this, primaryStage);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		init();
	}

	// PUBLIC

	public void exit() {
		logger.info("VSOL5 exited succesfully.");
	}
	
	// PRIVATE
	
	private void init() {
		
	}
	
	// GETTERS
	
	public Database getDb() { return db; }
	
}
