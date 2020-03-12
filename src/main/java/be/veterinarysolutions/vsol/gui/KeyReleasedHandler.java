package be.veterinarysolutions.vsol.gui;

import be.veterinarysolutions.vsol.gui.scenes.Study;
import be.veterinarysolutions.vsol.main.Gui;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyReleasedHandler implements EventHandler<KeyEvent> {

    private Gui gui;

    public KeyReleasedHandler(Gui gui) {
        this.gui = gui;
    }

    @Override
    public void handle(KeyEvent e) {
        System.out.println(e.getCharacter() + " " + e.getCode());
        Study study = gui.getStudy();

        if (e.getCode() == KeyCode.A) {
            study.selectAll();
        } else if (e.getCode() == KeyCode.DELETE) {
            study.deleteMenu(study.getSelectedMenu());
            study.fillMenus();
        } else if (e.getCode() == KeyCode.UP) {
            study.cycleMenus(true);
        } else if (e.getCode() == KeyCode.DOWN) {
            study.cycleMenus(false);
        } else if (e.getCode() == KeyCode.LEFT) {
            study.cycleMenus(true);
        } else if (e.getCode() == KeyCode.RIGHT) {
            study.cycleMenus(false);
        } else if (e.getCode() == KeyCode.ENTER) {
            study.btnImgMouseClicked(null);
        } else if (e.getCode() == KeyCode.CONTROL) {
            study.btnImgMouseClicked(null);
        }
    }
}
