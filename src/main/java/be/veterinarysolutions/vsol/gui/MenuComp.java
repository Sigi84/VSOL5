package be.veterinarysolutions.vsol.gui;

import be.veterinarysolutions.vsol.data.Menu;
import be.veterinarysolutions.vsol.data.Tooth;
import be.veterinarysolutions.vsol.main.Ctrl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MenuComp extends Controller {

    @FXML private BorderPane borderPane;
    @FXML private Label lblName;
    @FXML private Button btn1, btn2;
    @FXML private ImageView ico2;

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

            switch (menu.getStatus()) {
                case ADDED:
                    comp.btn2.setDisable(true);
                    if (menu.isSelected()) {
                        comp.borderPane.getStyleClass().add("bgwhite");
                    }
                    break;
                case NEXT:
                    comp.btn1.setDisable(true);
                    comp.btn2.setDisable(true);
                    comp.borderPane.getStyleClass().add(menu.isSelected() ? "lightblue" : "darkblue");
                    break;
                case TAKEN:
                    comp.btn1.setDisable(true);
                    comp.borderPane.getStyleClass().add(menu.isSelected() ? "lightgreen" : "darkgreen");
                    break;
                case FAILED:
                    comp.borderPane.getStyleClass().add(menu.isSelected() ? "lightred" : "darkred");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return comp;
    }


    @FXML protected void lblNameMouseClicked(MouseEvent e) {
        gui.getStudy().selectMenu(menu);
    }

    @FXML protected void btn1MouseClicked(MouseEvent e) {
        switch (menu.getStatus()) {
            case ADDED:
                gui.getStudy().setNextMenu(menu);
                gui.getStudy().fillMenus();
                break;
            case FAILED:
                Menu copy = new Menu(menu.getTeeth());
//                copy.setStatus(Tooth.Status.ADDED);
                int position = gui.getStudy().getIndex(menu) + 1;
                gui.getStudy().addMenu(copy, position);
                gui.getStudy().setNextMenu(copy);
                gui.getStudy().fillMenus();
                break;
        }
    }

    @FXML protected void btn2MouseClicked(MouseEvent e) {
        switch (menu.getStatus()) {
            case TAKEN:
                menu.setStatus(Menu.Status.FAILED);
                gui.getStudy().fillMenus();
                break;
            case FAILED:
                menu.setStatus(Menu.Status.TAKEN);
                gui.getStudy().fillMenus();
                break;
        }
    }

    @FXML protected void grabberMousePressed(MouseEvent e) {
        node.setOpacity(0.5);
        oldIndex = gui.getStudy().getIndex(menu);
    }

    private int oldIndex = -1;
    private int reorderIndex = -1;

    @FXML protected void grabberMouseDragged(MouseEvent e) {
        VBox parent = (VBox) node.getParent();
        if (reorderIndex >= 0) {
            parent.getChildren().remove(reorderIndex);
        }

        ReorderComp comp = ReorderComp.getInstance(ctrl);

        double y = e.getY();
        int drag = 0;

        if (y < 0.0) {
            drag = (int) ((y + 12 + 36) / 72) - 1;
            reorderIndex = oldIndex + drag;

        } else {
            drag = (int) ((y - 12) / 72);
            reorderIndex = oldIndex + 1 + drag;
        }

        if (reorderIndex < 0) {
            reorderIndex = 0;
        } else if (reorderIndex > parent.getChildren().size()) {
            reorderIndex = parent.getChildren().size();
        }

        parent.getChildren().add(reorderIndex, comp.getNode());
    }

    @FXML protected void grabberMouseReleased(MouseEvent e) {
        node.setOpacity(1.0);

        if (reorderIndex > oldIndex) {
            reorderIndex--;
        }

        if (reorderIndex > -1) {
            gui.getStudy().reorderMenu(menu, reorderIndex);
            reorderIndex = -1;
        }


//        node.setOpacity(1.0);
//
//        int offset = newIndex - oldIndex;
//        if (offset != 0) {
//
//        }
//
//
//        double y = e.getY();
//        int drag = 0;
//
//        if (y < -22.0) {
//            drag = (int) ((y / 84) - 1);
//        } else if (y > 84) { // 62 (current) + 22 (spacing) + 62 (next)
//            drag = (int) (y / 84);
//        }
//
//        if (drag != 0) {
//            gui.getStudy().reorderMenu(menu, drag);
//        }
    }
}
