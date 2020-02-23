package be.veterinarysolutions.vsol.main;

import be.veterinarysolutions.vsol.gui.Frame;
import be.veterinarysolutions.vsol.gui.Home;
import be.veterinarysolutions.vsol.gui.Sliders;
import be.veterinarysolutions.vsol.gui.Viewer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Gui {
	private Logger logger = LogManager.getLogger();

	private Ctrl ctrl;

	private Stage primaryStage;
//	private FXMLLoader loader;
	private Parent rootFrame;
	private Node nodeHome, nodeViewer, nodeSliders;
	private Image logo;

	private Frame frame;
	private Home home;
	private Viewer viewer;
	private Sliders sliders;

	public Gui(Ctrl ctrl, Stage primaryStage) throws IOException {
		this.ctrl = ctrl;
		this.primaryStage = primaryStage;
		logo = new Image(getClass().getResourceAsStream("/logo.png"));

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Frame.fxml"));
		rootFrame = loader.load();
		frame = loader.getController();
		frame.setGui(this);

		loader = new FXMLLoader(getClass().getResource("/fxml/Home.fxml"));
		nodeHome = loader.load();
		home = loader.getController();
		home.setGui(this);

		loader = new FXMLLoader(getClass().getResource("/fxml/Viewer.fxml"));
		nodeViewer = loader.load();
		viewer = loader.getController();
		viewer.setGui(this);

		loader = new FXMLLoader(getClass().getResource("/fxml/Sliders.fxml"));
		nodeSliders = loader.load();
		sliders = loader.getController();
		sliders.setGui(this);

//		frame.viewer();

		primaryStage.setTitle("VSOL Dental"); // set window title
		primaryStage.getIcons().add(logo); // set application icon

		primaryStage.setScene(new Scene(rootFrame, 1200, 800));

		if (Args.x != null)
			primaryStage.setX(Args.x);
		if (Args.y != null)
			primaryStage.setY(Args.y);

		primaryStage.show();
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

	public Node getNodeHome() {
		return nodeHome;
	}

	public Node getNodeViewer() {
		return nodeViewer;
	}

	public Window getPrimaryStage() {
		return primaryStage;
	}

	public Node getNodeSliders() {
		return nodeSliders;
	}
}
