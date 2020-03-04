package be.veterinarysolutions.vsol.gui.scenes;

import be.veterinarysolutions.vsol.gui.Controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Study extends Controller implements Initializable {
	@FXML private Button btnBack;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

	}

	@FXML protected void btnBackMouseClicked(MouseEvent e) {
		gui.showHome();
	}


}
