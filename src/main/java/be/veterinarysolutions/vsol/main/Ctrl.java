package be.veterinarysolutions.vsol.main;

import be.veterinarysolutions.vsol.dlls.Imagen;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Ctrl {

	public static final String version = "0.0.7";
	public static final String versionDate = "2020-02-25";
	public static final String versionTime = "17:06";

	private static final Logger logger = LogManager.getLogger();
	private Database db;
	private Gui gui;
	private Imagen imagen;
	
	public Ctrl(Stage primaryStage) {

		logger.info("Entered Ctrl.");

//		db = new Database();
		imagen = new Imagen();

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
		imagen.close();

		logger.info("VSOL5 exited succesfully.");

		System.exit(0);
	}
	
	// PRIVATE
	
	private void init() {
		
	}
	
	// GETTERS
	
	public Database getDb() {
		return db;
	}

	public Imagen getImagen() {
		return imagen;
	}
}
