package be.veterinarysolutions.vsol.data;

import be.veterinarysolutions.vsol.main.Options;
import javafx.scene.image.Image;

public class Tooth implements Comparable<Tooth> {

    private String name;
    private double minX, maxX, minY, maxY;
    private boolean selected = false, ready = false;
    private Image imgGreen, imgOrange;

    public Tooth(String name, double minX, double maxX, double minY, double maxY) {
        this.name = name;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;

        imgGreen = new Image("file:" + Options.START_DIR + "canine/green/" + name + ".png");
        imgOrange = new Image("file:" + Options.START_DIR + "canine/orange/" + name + ".png");
    }

    public String getName() {
        return name;
    }

    public double getMinX() {
        return minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxY() {
        return maxY;
    }

    public boolean contains(double x, double y, double width, double height) {
        if (x < minX * width)
            return false;
        if (x > maxX * width)
            return false;
        if (y < minY * height)
            return false;
        if (y > maxY * height)
            return false;

        return true;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    @Override
    public String toString() {
        return name;
    }

    public Image getImgGreen() {
        return imgGreen;
    }

    public Image getImgOrange() {
        return imgOrange;
    }

    @Override
    public int compareTo(Tooth o) {
        return name.compareTo(o.name);
    }
}
