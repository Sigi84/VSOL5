package be.veterinarysolutions.vsol.main;

import be.veterinarysolutions.vsol.gui.Controller;
import be.veterinarysolutions.vsol.gui.Frame;
import be.veterinarysolutions.vsol.gui.Home;
import be.veterinarysolutions.vsol.gui.Viewer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Gui {

	public Stage primaryStage;
//	private FXMLLoader loader;
	private Parent rootFrame;
	private Node nodeHome, nodeViewer;
	private Image logo;

	private Frame frame;
	private Home home;
	private Viewer viewer;

	public Gui(Stage primaryStage) throws IOException {
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

		showViewer();

		primaryStage.setTitle("VSOL Dental"); // set window title
		primaryStage.getIcons().add(logo); // set application icon

		primaryStage.setScene(new Scene(rootFrame, 1200, 800));
		primaryStage.setX(10);
		primaryStage.setY(10);

		setResizeListeners();

		primaryStage.show();
	}

	// PUBLIC

	public void showHome() {
		show(nodeHome);
	}

	public void showViewer() {
		show(nodeViewer);
	}

	// PRIVATE

	private void show(Node node) {
		frame.getPane().setCenter(node);
	}

	private void setResizeListeners() {
		// width resize
		primaryStage.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				viewer.resize();
			}
		});

		// no height resize necessary for now
		primaryStage.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				viewer.resize();
			}
		});
	}

	// GETTERS

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public Frame getFrame() {
		return frame;
	}
}
