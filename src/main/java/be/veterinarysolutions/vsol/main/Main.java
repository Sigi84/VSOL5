package be.veterinarysolutions.vsol.main;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Main extends Application {

	private static final Logger logger = LogManager.getLogger();

	public static void main(String[] args) {
		logger.info("Starting VSOL5 version " + Ctrl.version);

		Args.load(args);

		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		new Ctrl(primaryStage);
	}

	@Override
	public void stop() {
		logger.info("Closing VSOL5.");
	}


}
