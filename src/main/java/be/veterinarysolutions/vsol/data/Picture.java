package be.veterinarysolutions.vsol.data;

import be.veterinarysolutions.vsol.main.Gui;
import be.veterinarysolutions.vsol.main.Options;
import javafx.scene.image.Image;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Vector;

public class Picture extends Layer {

    private Image img;
    private File file;
    private Rectangle rect;
    private Line line0 = null, line1 = null, line2 = null;
    private int lineMode = 0;

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

    public void startLine(double x, double y) {
        switch (lineMode) {
            case 0:
                if (line0 == null) {
                    line0 = new Line(x, y, x, y);
                } else {
                    line0.setStartX(x);
                    line0.setStartY(y);
                }
                break;
            case 1:
                if (line1 == null) {
                    line1 = new Line(x, y, x, y);
                } else {
                    line1.setStartX(x);
                    line1.setStartY(y);
                }
                break;
            case 2:
                if (line2 == null) {
                    line2 = new Line(x, y, x, y);
                } else {
                    line2.setStartX(x);
                    line2.setStartY(y);
                }
                break;
        }
    }

    public void endLine(double x, double y) {
        switch (lineMode) {
            case 0:
                line0.setEndX(x);
                line0.setEndY(y);
                break;
            case 1:
                // y = ax + b
                // b = y - ax
                double a0 = (line0.getEndY() - line0.getStartY()) / (line0.getEndX() - line0.getStartX());
                double b0 = line0.getStartY() - (a0 * line0.getStartX());

                double a1 = -1 / a0; // this ensures perpendicularity
                double b1 = line1.getStartY() - (a1 * line1.getStartX());

                double distance = ((line0.getEndY() - line0.getStartY()) * line1.getStartX()) - ((line0.getEndX() - line0.getStartX()) * line1.getStartY()) + (line0.getEndX() * line0.getStartY()) - (line0.getEndY() * line0.getStartX());
                distance = Math.abs(distance);
                distance /= Math.sqrt( Math.pow((line0.getEndY() - line0.getStartY()), 2) + Math.pow((line0.getEndX() - line0.getStartX()), 2) );

                double xx = x;
                double yy = a1 * xx + b1;

                line1.setEndX(xx);
                line1.setEndY(yy);

                break;
            case 2:
                line2.setEndX(x);
                line2.setEndY(y);

                double length0 = Math.sqrt( Math.pow((line0.getEndX() - line0.getStartX()), 2) + Math.pow(line0.getEndY() - line0.getStartY(), 2) );
                double length1 = Math.sqrt( Math.pow((line1.getEndX() - line1.getStartX()), 2) + Math.pow(line1.getEndY() - line1.getStartY(), 2) );
                double length2 = Math.sqrt( Math.pow((line2.getEndX() - line2.getStartX()), 2) + Math.pow(line2.getEndY() - line2.getStartY(), 2) );

                double vhs = (4 * (length0 + length1)) / length2;

                System.out.println("VHS = " + vhs);

                break;
        }

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

    public void cycleLineMode() {
        lineMode = (lineMode + 1) % 3;
    }

    public Line getLine0() {
        return line0;
    }

    public Line getLine1() {
        return line1;
    }

    public Line getLine2() {
        return line2;
    }

    // SETTERS

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }
}
