package be.veterinarysolutions.vsol.gui;

import be.veterinarysolutions.vsol.main.Gui;

public abstract class Controller {
    protected Gui gui;

    public void setGui(Gui gui) {
        this.gui = gui;
    }

}
