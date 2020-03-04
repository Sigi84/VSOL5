package be.veterinarysolutions.vsol.gui;

import be.veterinarysolutions.vsol.main.Ctrl;
import be.veterinarysolutions.vsol.main.Gui;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public abstract class Controller {
    protected Ctrl ctrl;
    protected Gui gui;
    protected Node node;

    @FXML
    protected BorderPane borderPane;

    public void init(Ctrl ctrl, Gui gui, Node node) {
        this.ctrl = ctrl;
        this.gui = gui;
        this.node = node;
    }

    public Node getNode() { return node; }

    public BorderPane getBorderPane() {
        return borderPane;
    }

}
