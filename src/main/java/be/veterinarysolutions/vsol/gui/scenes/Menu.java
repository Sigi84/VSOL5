package be.veterinarysolutions.vsol.gui.scenes;

import be.veterinarysolutions.vsol.gui.Controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Menu extends Controller implements Initializable {
	@FXML private Button btnBack;
	@FXML private BorderPane borderPane;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

	}

	@FXML protected void btnBackMouseClicked(MouseEvent e) {
		gui.showHome();
	}

	@Override
	public BorderPane getBorderPane() {
		return borderPane;
	}

}
