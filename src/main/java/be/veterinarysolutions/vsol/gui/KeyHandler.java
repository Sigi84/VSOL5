package be.veterinarysolutions.vsol.gui;

import be.veterinarysolutions.vsol.main.Gui;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyHandler implements EventHandler<KeyEvent> {

    private Gui gui;

    public KeyHandler(Gui gui) {
        this.gui = gui;
    }

    @Override
    public void handle(KeyEvent e) {
        System.out.println("Key Press: " + e.getCharacter());
        Viewer viewer = gui.getViewer();

        if (e.getCode() == KeyCode.A) {
            viewer.selectAll();
        } else if (e.getCode() == KeyCode.N) {
            viewer.selectNone();
        } else if (e.getCode() == KeyCode.RIGHT) {
            viewer.selectNext();
        } else if (e.getCode() == KeyCode.LEFT) {
            viewer.selectPrevious();
        } else if (e.getCode() == KeyCode.DELETE) {
            viewer.delete();
        }
    }
}
