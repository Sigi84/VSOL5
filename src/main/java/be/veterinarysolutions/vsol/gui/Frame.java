package be.veterinarysolutions.vsol.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class Frame extends Controller {
    @FXML private Button btnBack, btnHome, btnEmail, btnRefresh;
    @FXML private Label lblVersion;
    @FXML private BorderPane pane;

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

    // GETTERS

    public BorderPane getPane() {
        return pane;
    }
}
