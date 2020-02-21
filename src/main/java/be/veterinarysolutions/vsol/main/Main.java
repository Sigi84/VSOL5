package be.veterinarysolutions.vsol.main;

import javafx.application.Application;
import javafx.stage.Stage;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import java.io.IOException;
import java.util.LinkedList;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		new Ctrl(primaryStage);
	}


}
