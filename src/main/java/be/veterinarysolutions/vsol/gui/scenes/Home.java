package be.veterinarysolutions.vsol.gui.scenes;

import be.veterinarysolutions.vsol.gui.Controller;
import be.veterinarysolutions.vsol.main.Ctrl;
import com.jpro.webapi.WebAPI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Home extends Controller implements Initializable {
	@FXML private Button btnNew, btnImport, btnSettings, btnExit;
	@FXML private Label lblVersion;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		btnExit.setVisible(!WebAPI.isBrowser()); // the exit button is not relevant in a browser window
		lblVersion.setText("© 2020 - Veterinary Solutions - version " + Ctrl.version);
	}

	@FXML protected void btnNewMouseClicked(MouseEvent e) {
		gui.showStudy();
	}

	@FXML protected void btnImportMouseClicked(MouseEvent e) {
		gui.showStudy();
	}

	@FXML protected void btnSettingsMouseClicked(MouseEvent e) {
		gui.showSettings();
	}

	@FXML protected void btnExitMouseClicked(MouseEvent e) {
		gui.getPrimaryStage().close();
	}

}