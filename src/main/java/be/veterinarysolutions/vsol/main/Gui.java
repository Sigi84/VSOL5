package be.veterinarysolutions.vsol.main;

import be.veterinarysolutions.vsol.gui.*;
import be.veterinarysolutions.vsol.gui.scenes.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Gui {
	private Logger logger = LogManager.getLogger();
	private Ctrl ctrl;
	private Stage primaryStage;

	private Frame frame;

	private Home home;
	private Settings settings;
	private Study study;

	private Controller activeScene;

	public Gui(Ctrl ctrl, Stage primaryStage) throws IOException {
		this.ctrl = ctrl;
		this.primaryStage = primaryStage;

		// ROOT

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Frame.fxml"));
		Parent frameRoot = loader.load();
		frame = loader.getController();
		frame.set(ctrl, this, frameRoot);

		// SCENES

		loader = new FXMLLoader(getClass().getResource("/fxml/HomeScene.fxml"));
		Node node = loader.load();
		home = loader.getController();
		home.set(ctrl, this, node);

		loader = new FXMLLoader(getClass().getResource("/fxml/SettingsScene.fxml"));
		node = loader.load();
		settings = loader.getController();
		settings.set(ctrl, this, node);

		loader = new FXMLLoader(getClass().getResource("/fxml/StudyScene.fxml"));
		node = loader.load();
		study = loader.getController();
		study.set(ctrl, this, node);

		Scene scene = new Scene(frameRoot);
		scene.setOnKeyPressed(new KeyPressedHandler(this));
//		frame.getBorderPane().widthProperty().addListener((observableValue, oldValue, newValue) -> activeScene.resizeWidth(newValue.doubleValue()));
//		frame.getBorderPane().heightProperty().addListener((observableValue, oldValue, newValue) -> activeScene.resizeHeight(newValue.doubleValue()));
		primaryStage.setScene(scene);

		showHome();

		if (Args.x != null)
			primaryStage.setX(Args.x);
		if (Args.y != null)
			primaryStage.setY(Args.y);

		primaryStage.setWidth(Args.width);
		primaryStage.setHeight(Args.height);

		if (Args.maximize)
			primaryStage.setMaximized(true);

		primaryStage.setTitle("VSOL Dental"); // set window title

		Image logo = new Image(getClass().getResourceAsStream("/logo.png"));
		primaryStage.getIcons().add(logo); // set application icon

		primaryStage.show();
	}

	public void showHome() {
		show(home);
	}

	public void showSettings() {
		show(settings);
	}

	public void showStudy() {
		show(study);
	}

	private void show(Controller controller) {
		frame.getBorderPane().setCenter(controller.getNode());
		controller.getBorderPane().prefWidthProperty().bind(frame.getBorderPane().widthProperty());
		controller.getBorderPane().prefHeightProperty().bind(frame.getBorderPane().heightProperty());

		activeScene = controller;
//		activeScene.resizeWidth(frame.getBorderPane().widthProperty().doubleValue());
//		activeScene.resizeHeight(frame.getBorderPane().heightProperty().doubleValue());
	}

	// GETTERS

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public Frame getFrame() {
		return frame;
	}

	public Home getHome() {
		return home;
	}

	public Settings getSettings() {
		return settings;
	}

	public Study getStudy() {
		return study;
	}

}
