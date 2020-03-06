package be.veterinarysolutions.vsol.data;

import be.veterinarysolutions.vsol.main.Options;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

import java.util.Vector;

public class Quadrant {
    private String name;
    private Image img;
    private boolean left, top;
    private Vector<Tooth> teeth = new Vector<>();
    private Rectangle rect;

    public Quadrant(String name, boolean left, boolean top) {
        this.name = name;
        this.left = left;
        this.top = top;

        img = new Image("file:" + Options.START_DIR + "canine/" + name + ".png");
        teeth = Canine.getTeeth(left, top);
    }


    public Image getImg() {
        return img;
    }

    public Vector<Tooth> getTeeth() {
        return teeth;
    }

    public String getName() {
        return name;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isTop() {
        return top;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    @Override
    public String toString() {
        return name;
    }
}
