package be.veterinarysolutions.vsol.gui.tobedeleted;

import be.veterinarysolutions.vsol.data.Picture;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Test {

    @FXML private StackPane stackPane;
    ResizableCanvasOld canvas;

    public void initialize(URL location, ResourceBundle resources) {
        Picture pic = new Picture("C:/Sandbox/small.jpg");

        canvas = new ResizableCanvasOld(pic);
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
