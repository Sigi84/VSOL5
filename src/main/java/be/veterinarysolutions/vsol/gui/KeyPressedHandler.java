package be.veterinarysolutions.vsol.gui;

import be.veterinarysolutions.vsol.gui.scenes.Study;
import be.veterinarysolutions.vsol.main.Gui;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyPressedHandler implements EventHandler<KeyEvent> {

    private Gui gui;

    public KeyPressedHandler(Gui gui) {
        this.gui = gui;
    }

    @Override
    public void handle(KeyEvent e) {
        Study study = gui.getStudy();

        if (e.getCode() == KeyCode.A) {
            study.selectAll();
        } else if (e.getCode() == KeyCode.DELETE) {
            study.deleteSelection();
        }
    }
}
