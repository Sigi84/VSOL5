package be.veterinarysolutions.vsol.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Frame extends Controller {
    @FXML private Button btnBack, btnHome, btnEmail, btnRefresh;
    @FXML private Label lblVersion;
    @FXML private BorderPane pane;
    @FXML private AnchorPane bg;
    @FXML private VBox vbox;
    @FXML private HBox hbox;

    // EVENTS

    @FXML public void initialize() {

    }

    @FXML protected void btnBackAction(ActionEvent event) {
        System.out.println("back button pressed");

        gui.showHome();
    }

    @FXML protected void btnRefreshAction(ActionEvent event) {
        System.out.println("refresh button pressed");

        gui.showViewer();
    }

    // PUBLIC

    // GETTERS

    public BorderPane getPane() {
        return pane;
    }

    public VBox getVbox() {
        return vbox;
    }

    public HBox getHbox() {
        return hbox;
    }
}
