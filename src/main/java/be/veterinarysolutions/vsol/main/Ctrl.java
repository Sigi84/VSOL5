package be.veterinarysolutions.vsol.main;

import be.veterinarysolutions.vsol.dlls.Imagen;
import com.jpro.webapi.JProApplication;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Ctrl {

	public static final String version = "0.0.14";
	public static final String versionDate = "2020-03-03";
	public static final String versionTime = "21:00";

	private static final Logger logger = LogManager.getLogger();
	private Database db;
	private Gui gui;
	private Imagen imagen;
	private JProApplication jpro;
	
	public Ctrl(Stage primaryStage, JProApplication jpro) {

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
		gui.getViewer().deleteTempFiles();

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

	public JProApplication getJpro() {
		return jpro;
	}

	public Gui getGui() {
		return gui;
	}
}
