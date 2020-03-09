package be.veterinarysolutions.vsol.gui;

import be.veterinarysolutions.vsol.data.Menu;
import be.veterinarysolutions.vsol.main.Ctrl;
import be.veterinarysolutions.vsol.main.Gui;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MenuComp extends Controller {

    @FXML private BorderPane borderPane;
    @FXML private Label lblName;

    private Menu menu;

    @Override
    public void init() {

    }

    public static MenuComp getInstance(Ctrl ctrl, Menu menu) {
        MenuComp comp = null;
        try {
            FXMLLoader loader = new FXMLLoader(ctrl.getGui().getClass().getResource("/fxml/MenuComponent.fxml"));
            Node node = loader.load();
            comp = loader.getController();
            comp.set(ctrl, ctrl.getGui(), node);
            comp.menu = menu;
            comp.lblName.setText(menu.toString());
            if (menu.isReady()) {
                comp.borderPane.getStyleClass().add("selectedborder");
            } else {
                comp.borderPane.getStyleClass().add("unselectedborder");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return comp;
    }


    @FXML protected void lblNameMouseClicked(MouseEvent e) {
        gui.getStudy().readyMenu(menu);
    }

    @FXML protected void btnDeleteMouseClicked(MouseEvent e) {
        gui.getStudy().deleteMenu(menu);
    }
}
