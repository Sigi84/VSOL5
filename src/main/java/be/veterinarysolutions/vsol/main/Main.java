package be.veterinarysolutions.vsol.main;

import com.jpro.webapi.JProApplication;
import com.jpro.webapi.WebAPI;
import com.jpro.webapi.WebCallback;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Main extends JProApplication {

	private static final Logger logger = LogManager.getLogger();
	private Ctrl ctrl;

	public static void main(String[] args) {
		logger.info("Starting VSOL5 version " + Ctrl.version);

		Args.load(args);
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("VSOL");
		ctrl = new Ctrl(primaryStage, this);
	}

	@Override
	public void stop() {
		logger.info("Closing VSOL5.");
		ctrl.exit();
	}

}
