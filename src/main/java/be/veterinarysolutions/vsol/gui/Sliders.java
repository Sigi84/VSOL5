package be.veterinarysolutions.vsol.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class Sliders extends Controller {

    @FXML private AnchorPane pane;
    @FXML private Slider sliderHistogram, sliderWeight, sliderLatitude, sliderContrast, sliderEdge, sliderNoise;
    @FXML private Button btnUndo, btnRedo;

    public double getWidth() {
        return pane.getPrefWidth();
    }

    // PRIVATE

    private void disableSliders(boolean histogram, boolean weight, boolean latitude, boolean contrast, boolean edge, boolean noise) {
        sliderHistogram.setDisable(histogram);
        sliderWeight.setDisable(weight);
        sliderLatitude.setDisable(latitude);
        sliderContrast.setDisable(contrast);
        sliderEdge.setDisable(edge);
        sliderNoise.setDisable(noise);
    }

    private void enableSliders() {
        sliderHistogram.setDisable(false);
        sliderWeight.setDisable(false);
        sliderLatitude.setDisable(false);
        sliderContrast.setDisable(false);
        sliderEdge.setDisable(false);
        sliderNoise.setDisable(false);
    }

    // EVENT

    @FXML protected void sliderHistogramMousePressed(MouseEvent e) {
        disableSliders(false, true, true, true, true, true);
    }

    @FXML protected void sliderHistogramMouseReleased(MouseEvent e) {
        enableSliders();
        gui.getViewer().histogramOptimization(sliderHistogram.getValue());
    }

    @FXML protected void btnUndoMouseClicked(MouseEvent e) {
        gui.getViewer().undo();
    }

    @FXML protected void btnRedoMouseClicked(MouseEvent e) {
        gui.getViewer().redo();
    }

    // GETTERS


    public Button getBtnUndo() {
        return btnUndo;
    }

    public Button getBtnRedo() {
        return btnRedo;
    }

    public Slider getSliderHistogram() {
        return sliderHistogram;
    }

    public Slider getSliderWeight() {
        return sliderWeight;
    }

    public Slider getSliderLatitude() {
        return sliderLatitude;
    }

    public Slider getSliderContrast() {
        return sliderContrast;
    }

    public Slider getSliderEdge() {
        return sliderEdge;
    }

    public Slider getSliderNoise() {
        return sliderNoise;
    }
}
