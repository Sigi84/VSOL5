package be.veterinarysolutions.vsol.data;

import be.veterinarysolutions.vsol.main.Options;
import javafx.scene.image.Image;

import java.util.TreeSet;
import java.util.Vector;

public class Tooth implements Comparable<Tooth> {

    private String name;
    private double minX, maxX, minY, maxY;
    private boolean selected = false;
    private Image imgWhite, imgGray, imgLightGreen, imgDarkGreen, imgLightRed, imgDarkRed, imgLightBlue, imgDarkBlue;

    private TreeSet<Menu> menus = new TreeSet<>();

    public Tooth(String type, String name, double minX, double maxX, double minY, double maxY) {
        this.name = name;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;

        imgWhite = new Image("file:" + Options.START_DIR + type + "/white/" + name + ".png");
        imgGray = new Image("file:" + Options.START_DIR + type + "/gray/" + name + ".png");

        imgLightGreen = new Image("file:" + Options.START_DIR + type + "/green/light/" + name + ".png");
        imgDarkGreen = new Image("file:" + Options.START_DIR + type + "/green/dark/" + name + ".png");

        imgLightRed = new Image("file:" + Options.START_DIR + type + "/red/light/" + name + ".png");
        imgDarkRed = new Image("file:" + Options.START_DIR + type + "/red/dark/" + name + ".png");

        imgLightBlue = new Image("file:" + Options.START_DIR + type + "/blue/light/" + name + ".png");
        imgDarkBlue = new Image("file:" + Options.START_DIR + type + "/blue/dark/" + name + ".png");
    }

    public String getName() {
        return name;
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

    public TreeSet<Menu> getMenus() {
        return menus;
    }

    public Menu getTopMenu() {
        Menu result = null;

        for (Menu menu : menus) {
            if (result == null) {
                result = menu;
            } else {
                if (menu.getStatus().ordinal() > result.getStatus().ordinal()) {
                    result = menu;
                }
            }
        }

        return result;
    }

    @Override
    public int compareTo(Tooth o) {
        return name.compareTo(o.name);
    }
}
