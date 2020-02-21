package be.veterinarysolutions.vsol.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;

public class Viewer extends Controller {

	@FXML private AnchorPane ancRoot;
	@FXML private Canvas canvas;
	@FXML private VBox vboxRight;
	@FXML private Button btnRotate;

	private Image img;

	@FXML public void initialize() {
//		canvas.setLayoutX(0);
//		canvas.setLayoutY(0);

//		ancRoot.setPrefWidth(gui.get);

		Rectangle rectangle = new Rectangle(10, 10, 100, 200);

		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.rect(10, 20 ,100, 200);

		gc.fillRect(10, 20, 100, 200);

	}

	@FXML protected void btnOpenAction(ActionEvent e) {
		// implement this behind a resize listener
//		canvas.setWidth(ancRoot.getBoundsInParent().getWidth() - vboxRight.getBoundsInParent().getWidth());
//		canvas.setHeight(ancRoot.getBoundsInParent().getHeight());

		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

//		FileChooser fileChooser = new FileChooser();
//		fileChooser.setInitialDirectory(new File("C:/Sandbox/"));
//		File file = fileChooser.showOpenDialog(gui.getPrimaryStage());
		File file = new File("C:/Sandbox/examplepic.jpg");
		if (file != null) {
			img = new Image("file:" + file.getAbsolutePath());
			gc.drawImage(img, 0, 0);
		}
	}

	@FXML protected void btnRotateAction(ActionEvent e) {




//		GraphicsContext gc = canvas.getGraphicsContext2D();
//		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
//
//
//		int xp = 0, yp = 0, degrees = 45;
//		if (img != null) {
//			System.out.println("rotate");
//			gc.translate(xp, yp);
//			gc.rotate(degrees);
//
//			// Note how the image is drawn relative to 0.  Since the image needs to be
//			// rotated around the image center, the center is simply half of the image
//			// dimension.
//			gc.drawImage( img, -img.getWidth()/2.0, -img.getHeight()/2.0);
//
//			// Reverse the translation and rotation once drawn.
//			gc.rotate(-degrees);
//			gc.translate(-xp, -yp);
//		}

	}
	
}
