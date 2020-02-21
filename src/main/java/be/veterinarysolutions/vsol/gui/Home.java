package be.veterinarysolutions.vsol.gui;

import be.veterinarysolutions.vsol.main.Gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Home extends Controller {
	@FXML private Button btnHome;

	@FXML protected void btnOkAction(ActionEvent event) {
		System.out.println("ok");
	}


	@FXML public void initialize() {

	}
	
	// EVENTS

	@FXML protected void btnHomeAction(ActionEvent event) {

	}



	
	
}
