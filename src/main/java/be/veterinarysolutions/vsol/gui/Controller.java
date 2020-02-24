package be.veterinarysolutions.vsol.gui;

import be.veterinarysolutions.vsol.main.Ctrl;
import be.veterinarysolutions.vsol.main.Gui;

public abstract class Controller {
    protected Ctrl ctrl;
    protected Gui gui;

    public void init(Ctrl ctrl, Gui gui) {
        this.ctrl = ctrl;
        this.gui = gui;
    }

}
