package be.veterinarysolutions.vsol.data;

import be.veterinarysolutions.vsol.main.Gui;
import be.veterinarysolutions.vsol.main.Options;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Vector;

public class Picture extends Layer {

    private Image img;
    private File file;
    private Rectangle rect;

    // CONSTRUCTOR

    public Picture(File file) {
        this.file = file;
        img = new Image("file:" + file.getAbsolutePath());
        reset();
        rect = new Rectangle(0, 0, img.getWidth(), img.getHeight());
    }

    public Picture(String filename) {
        this(new File(filename));
    }

    public Picture(Image img) {
        this.img = img;
        reset();
        rect = new Rectangle(0, 0, img.getWidth(), img.getHeight());
    }

    // STATIC

    public static Picture chooseFile(Gui gui) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(Options.START_DIR));
        File file = fileChooser.showOpenDialog(gui.getPrimaryStage());
        if (file == null) {
            return null;
        } else {
            return new Picture(file);
        }
    }

    // GETTERS

    public Image getImg() {
        return img;
    }

    public Rectangle getRect() {
        return rect;
    }

    // SETTERS

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }
}
