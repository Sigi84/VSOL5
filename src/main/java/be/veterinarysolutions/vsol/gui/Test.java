package be.veterinarysolutions.vsol.gui;

import be.veterinarysolutions.vsol.data.Picture;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class Test extends Controller implements Initializable {

    @FXML private StackPane stackPane;
    ResizableCanvas canvas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Picture pic = new Picture("C:/Sandbox/small.jpg");

        canvas = new ResizableCanvas(pic);
        canvas.widthProperty().bind(stackPane.widthProperty());
        canvas.heightProperty().bind(stackPane.heightProperty());
        stackPane.getChildren().add(canvas);
    }

    @FXML protected void stackPaneMouseMoved(MouseEvent e) {


    }

    @FXML protected void stackPaneMouseClicked(MouseEvent e) {

        System.out.println(canvas.getRect().contains(e.getX(), e.getY()));
        System.out.println(canvas.getCircle().contains(e.getX(), e.getY()));

    }
}
