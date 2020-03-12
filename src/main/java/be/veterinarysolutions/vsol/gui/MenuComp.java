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

    @FXML private BorderPane borderPane, thumbnail;
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

            comp.btn1.setDisable(menu.hasPic() && !menu.isDeleted());
            comp.btn2.setDisable(!menu.hasPic());

            if (menu.isNext()) {
                comp.borderPane.getStyleClass().add("green");
                comp.borderPane.getStyleClass().add(menu.isSelected() ? "whiteborder" : "greenborder");
            } else if (menu.isDeleted()) {
                comp.borderPane.getStyleClass().add("red");
                comp.borderPane.getStyleClass().add(menu.isSelected() ? "whiteborder" : "redborder");
            } else if (menu.hasPic()) {
//                comp.borderPane.getStyleClass().add("blue");
//                comp.borderPane.getStyleClass().add(menu.isSelected() ? "whiteborder" : "blueborder");
                comp.borderPane.getStyleClass().add("black");
                comp.borderPane.getStyleClass().add(menu.isSelected() ? "whiteborder" : "blackborder");
            } else {
//                comp.borderPane.getStyleClass().add("blue"); (redundant)
                comp.borderPane.getStyleClass().add(menu.isSelected() ? "whiteborder" : "blackborder");
            }

            if (menu.hasPic()) {
                ImageView imageView = new ImageView(menu.getPic().getImg());
                imageView.setPreserveRatio(true);
                imageView.setFitHeight(60);
                comp.thumbnail.setCenter(imageView);
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
        gui.getStudy().deleteMenu(menu);
        gui.getStudy().fillMenus();
    }

    @FXML protected void btn2MouseClicked(MouseEvent e) {
        menu.setDeleted(!menu.isDeleted());
        gui.getStudy().fillMenus();
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
