package be.veterinarysolutions.vsol.gui;

import be.veterinarysolutions.vsol.main.Ctrl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.im4java.core.*;

import java.io.IOException;

public class Frame extends Controller {
    private Logger logger = LogManager.getLogger();

    @FXML private Label lblVersion;
    @FXML private BorderPane bg;
    @FXML private VBox menu;

    // PRIVATE

    private void init() {
        lblVersion.setText(Ctrl.version);

        bg.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                resize();
            }
        });

        bg.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                resize();
            }
        });
    }

    private void resize() {
        gui.getViewer().resize();
    }

    private void exit() {
        gui.getCtrl().exit();
    }

    private void  back() {
        bg.setCenter(null);
    }

    private void home() {
//        bg.setCenter(gui.getNodeHome());
    }

    public void viewer() {
        bg.setCenter(gui.getNodeViewer());
    }

    // EVENTS

    @FXML public void initialize() { init(); }

    @FXML public void exitApplication(ActionEvent e) { exit(); }

    @FXML protected void btnBackMouseClicked(MouseEvent e) { back(); }

    @FXML protected void btnBackTouchPressed(TouchEvent e) { back(); }

    @FXML protected void btnHomeMouseClicked(MouseEvent e) { home(); }

    @FXML protected void btnHomeTouchPressed(TouchEvent e) { home(); }

    @FXML protected void btnViewerMouseClicked(MouseEvent e) { viewer(); }

    @FXML protected void btnViewerTouchPressed(TouchEvent e) { viewer(); }


    // PUBLIC

    // GETTERS

    public double getBgWidth() {
        return bg.widthProperty().doubleValue();
    }

    public double getBgHeight() {
        return bg.heightProperty().doubleValue();
    }

    public double getMenuWidth() {
        return menu.widthProperty().doubleValue();
    }
}
