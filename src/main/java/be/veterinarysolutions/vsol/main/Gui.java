package be.veterinarysolutions.vsol.main;

import be.veterinarysolutions.vsol.gui.*;
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
	private Parent rootFrame, rootTest;
	private Node nodeHome, nodeViewer, nodeSliders;
	private Image logo;

	private Frame frame;
	private Home home;
	private Viewer viewer;
	private Sliders sliders;
	private Test test;

	public Gui(Ctrl ctrl, Stage primaryStage) throws IOException {


//		primaryStage.setTitle("Hello World!");
//		Button btn = new Button();
//		btn.setText("Say 'Hello World'");
//		btn.setOnAction(new EventHandler<ActionEvent>() {
//
//			@Override
//			public void handle(ActionEvent event) {
//				System.out.println("Hello World!");
//			}
//		});
//
//		StackPane root = new StackPane();
//		root.getChildren().add(btn);
//		primaryStage.setScene(new Scene(root, 300, 250));
//		primaryStage.show();


		this.ctrl = ctrl;
		this.primaryStage = primaryStage;
		logo = new Image(getClass().getResourceAsStream("/logo.png"));

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Frame.fxml"));
		rootFrame = loader.load();
		frame = loader.getController();
		frame.init(ctrl, this);

		loader = new FXMLLoader(getClass().getResource("/fxml/Home.fxml"));
		nodeHome = loader.load();
		home = loader.getController();
		home.init(ctrl, this);

		loader = new FXMLLoader(getClass().getResource("/fxml/Viewer.fxml"));
		nodeViewer = loader.load();
		viewer = loader.getController();
		viewer.init(ctrl, this);

		loader = new FXMLLoader(getClass().getResource("/fxml/Sliders.fxml"));
		nodeSliders = loader.load();
		sliders = loader.getController();
		sliders.init(ctrl, this);

		loader = new FXMLLoader(getClass().getResource("/fxml/Test.fxml"));
		rootTest = loader.load();
		test = loader.getController();
		test.init(ctrl, this);

		frame.viewer();

		primaryStage.setTitle("VSOL Dental"); // set window title
		primaryStage.getIcons().add(logo); // set application icon

		Scene scene = new Scene(rootFrame, 1200, 800);
//		Scene scene = new Scene(rootTest, 1200, 800);
		scene.setOnKeyPressed(new KeyHandler(this));

		primaryStage.setScene(scene);

		if (Args.x != null)
			primaryStage.setX(Args.x);
		if (Args.y != null)
			primaryStage.setY(Args.y);
		if (Args.width != null)
			primaryStage.setWidth(Args.width);
		if (Args.height != null)
			primaryStage.setHeight(Args.height);



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

	public Test getTest() {
		return test;
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
