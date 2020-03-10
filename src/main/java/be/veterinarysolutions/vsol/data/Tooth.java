package be.veterinarysolutions.vsol.data;

import be.veterinarysolutions.vsol.main.Options;
import javafx.scene.image.Image;

public class Tooth implements Comparable<Tooth> {

    public enum Status { NONE, ADDED, TAKEN, FAILED, NEXT }

    private String name;
    private double minX, maxX, minY, maxY;
    private boolean selected = false;
    private Image imgWhite, imgGray, imgLightGreen, imgDarkGreen, imgLightRed, imgDarkRed, imgLightBlue, imgDarkBlue;
    private Status status = Status.NONE;

    public Tooth(String name, double minX, double maxX, double minY, double maxY) {
        this.name = name;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;

        imgWhite = new Image("file:" + Options.START_DIR + "canine/white/" + name + ".png");
        imgGray = new Image("file:" + Options.START_DIR + "canine/gray/" + name + ".png");

        imgLightGreen = new Image("file:" + Options.START_DIR + "canine/green/light/" + name + ".png");
        imgDarkGreen = new Image("file:" + Options.START_DIR + "canine/green/dark/" + name + ".png");

        imgLightRed = new Image("file:" + Options.START_DIR + "canine/red/light/" + name + ".png");
        imgDarkRed = new Image("file:" + Options.START_DIR + "canine/red/dark/" + name + ".png");

        imgLightBlue = new Image("file:" + Options.START_DIR + "canine/blue/light/" + name + ".png");
        imgDarkBlue = new Image("file:" + Options.START_DIR + "canine/blue/dark/" + name + ".png");

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

    @Override
    public String toString() {
        return name;
    }

    public Image getImgWhite() {
        return imgWhite;
    }

    public Image getImgLightGreen() {
        return imgLightGreen;
    }

    public Image getImgDarkGreen() {
        return imgDarkGreen;
    }

    public Image getImgLightRed() {
        return imgLightRed;
    }

    public Image getImgDarkRed() {
        return imgDarkRed;
    }

    public Image getImgLightBlue() {
        return imgLightBlue;
    }

    public Image getImgDarkBlue() {
        return imgDarkBlue;
    }

    public Image getImgGray() {
        return imgGray;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public int compareTo(Tooth o) {
        return name.compareTo(o.name);
    }
}
