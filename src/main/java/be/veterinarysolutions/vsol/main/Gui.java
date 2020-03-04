package be.veterinarysolutions.vsol.main;

import be.veterinarysolutions.vsol.gui.*;
import be.veterinarysolutions.vsol.gui.scenes.Home;
import be.veterinarysolutions.vsol.gui.scenes.Settings;
import be.veterinarysolutions.vsol.gui.scenes.Study;
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

	private Parent frameRoot;
//	private Node homeScene, settingsScene, studyScene;

	private Frame frame;
	private Home home;
	private Settings settings;
	private Study study;

	private Node nodeViewer, nodeSliders;

	private Viewer viewer;
	private Sliders sliders;
	private Test test;

	public Gui(Ctrl ctrl, Stage primaryStage) throws IOException {
		this.ctrl = ctrl;
		this.primaryStage = primaryStage;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Frame.fxml"));
		frameRoot = loader.load();
		frame = loader.getController();
		frame.init(ctrl, this, frameRoot);


		loader = new FXMLLoader(getClass().getResource("/fxml/HomeScene.fxml"));
		Node node = loader.load();
		home = loader.getController();
		home.init(ctrl, this, node);

		loader = new FXMLLoader(getClass().getResource("/fxml/SettingsScene.fxml"));
		node = loader.load();
		settings = loader.getController();
		settings.init(ctrl, this, node);

		loader = new FXMLLoader(getClass().getResource("/fxml/StudyScene.fxml"));
		node = loader.load();
		study = loader.getController();
		study.init(ctrl, this, node);





		Scene scene = new Scene(frameRoot, 1200, 800);
		scene.setOnKeyPressed(new KeyHandler(this));
		primaryStage.setScene(scene);

		showHome();

		if (Args.x != null)
			primaryStage.setX(Args.x);
		if (Args.y != null)
			primaryStage.setY(Args.y);

		primaryStage.setWidth(Args.width);
		primaryStage.setHeight(Args.height);

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
	}

	private void load(Controller controller, String fxml) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxml + ".fxml"));
			Node node = loader.load();
			controller = loader.getController();
			controller.init(ctrl, this, node);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// GETTERS

	public Ctrl getCtrl() {
		return ctrl;
	}

	public Frame getFrame() {
		return frame;
	}

	public Viewer getViewer() {
		return viewer;
	}

	public Home getHome() {
		return home;
	}

	public Sliders getSliders() {
		return sliders;
	}

	public Test getTest() {
		return test;
	}

	public Node getNodeViewer() {
		return nodeViewer;
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public Node getNodeSliders() {
		return nodeSliders;
	}
}
