package be.veterinarysolutions.vsol.gui.scenes;

import be.veterinarysolutions.vsol.data.Picture;
import be.veterinarysolutions.vsol.dlls.Imagen;
import be.veterinarysolutions.vsol.gui.Controller;
import be.veterinarysolutions.vsol.main.Ctrl;
import be.veterinarysolutions.vsol.main.Options;
import com.jpro.webapi.WebAPI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class Frame extends Controller {
    private Logger logger = LogManager.getLogger();

    @FXML private Label lblVersion;
    @FXML private BorderPane bg;
    @FXML private VBox menu;
    @FXML private Button btnCamera;

    // PRIVATE

    private void resize() {
//        gui.getViewer().resize();
    }

    private void fillImagen() {
        if (ctrl.getImagen().isOpen())
            btnCamera.setStyle("-fx-background-color: green");
        else
            btnCamera.setStyle("-fx-background-color: red");
    }

    public void exit() {
        ctrl.exit();
    }

    private void  back() {
        bg.setCenter(null);
    }

    private void home() {
//        bg.setCenter(gui.getNodeHome());
//        ctrl.getImagen().mockCapture();
    }

    public void viewer() {
//        bg.setCenter(gui.getNodeViewer());
    }

    // EVENTS

    @Override
    public void init() {
//        lblVersion.setText(Ctrl.version);
//
//        bg.widthProperty().addListener((observable, oldValue, newValue) -> resize());
//        bg.heightProperty().addListener((observable, oldValue, newValue) -> resize());
//
//        btnCamera.widthProperty().addListener((observable, oldValue, newValue) -> fillImagen());
    }

    @FXML protected void btnBackMouseClicked(MouseEvent e) { back(); }

    @FXML protected void btnHomeMouseClicked(MouseEvent e) { home(); }

    @FXML protected void btnViewerMouseClicked(MouseEvent e) { viewer(); }

    @FXML protected void btnCameraMouseClicked(MouseEvent e) {
        Imagen imagen = ctrl.getImagen();
        if (Options.IMAGEN_MOCK_CAPTURE) {
            String filename = ctrl.getImagen().mockCapture();
            if (filename != null) {
                Picture pic = new Picture(filename);
//                gui.getViewer().addPic(pic);
            }
        } else {
            ctrl.getImagen().capture();
        }
    }

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
