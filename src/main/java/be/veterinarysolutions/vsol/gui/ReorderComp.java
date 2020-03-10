package be.veterinarysolutions.vsol.gui;

import be.veterinarysolutions.vsol.data.Menu;
import be.veterinarysolutions.vsol.main.Ctrl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class ReorderComp extends Controller {

    @FXML private BorderPane borderPane;


    @Override
    public void init() {

    }

    public static ReorderComp getInstance(Ctrl ctrl) {
        ReorderComp comp = null;
        try {
            FXMLLoader loader = new FXMLLoader(ctrl.getGui().getClass().getResource("/fxml/ReorderComponent.fxml"));
            Node node = loader.load();
            comp = loader.getController();
            comp.set(ctrl, ctrl.getGui(), node);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return comp;
    }


}
