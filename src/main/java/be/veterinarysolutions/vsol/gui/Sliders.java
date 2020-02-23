package be.veterinarysolutions.vsol.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class Sliders extends Controller {

    @FXML private AnchorPane pane;
    @FXML private Slider sliderHistogram, sliderWeight, sliderLatitude, sliderContrast, sliderEdge, sliderNoise;

    public double getWidth() {
        return pane.getPrefWidth();
    }

    // EVENT

    @FXML protected void sliderHistogramMousePressed(MouseEvent e) {

    }

    @FXML protected void sliderHistogramMouseReleased(MouseEvent e) {

    }
}
