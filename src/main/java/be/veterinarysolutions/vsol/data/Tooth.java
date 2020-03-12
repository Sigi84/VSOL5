package be.veterinarysolutions.vsol.data;

import be.veterinarysolutions.vsol.main.Options;
import javafx.scene.image.Image;

import java.util.TreeSet;
import java.util.Vector;

public class Tooth implements Comparable<Tooth> {

    private String name;
    private double minX, maxX, minY, maxY;
    private boolean selected = false;
    private Image imgWhite, imgGray, imgGreen, imgRed, imgBlue;

    private TreeSet<Menu> menus = new TreeSet<>();

    public Tooth(String type, String name, double minX, double maxX, double minY, double maxY) {
        this.name = name;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;

        imgWhite = new Image("file:" + Options.START_DIR + type + "/white/" + name + ".png");
        imgGray = new Image("file:" + Options.START_DIR + type + "/gray/" + name + ".png");

        imgGreen = new Image("file:" + Options.START_DIR + type + "/green/dark/" + name + ".png");
        imgRed = new Image("file:" + Options.START_DIR + type + "/red/dark/" + name + ".png");
        imgBlue = new Image("file:" + Options.START_DIR + type + "/blue/dark/" + name + ".png");
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

    public Image getImgGreen() {
        return imgGreen;
    }

    public Image getImgRed() {
        return imgRed;
    }

    public Image getImgBlue() {
        return imgBlue;
    }

    public Image getImgGray() {
        return imgGray;
    }

    public TreeSet<Menu> getMenus() {
        return menus;
    }

    // TODO
    public Menu getTopMenu() {
        if (menus == null || menus.isEmpty())
            return null;

        return menus.first();

//        Menu result = null;
//
//        for (Menu menu : menus) {
//            if (result == null) {
//                result = menu;
//            } else {
//                if (menu.getStatus().ordinal() > result.getStatus().ordinal()) {
//                    result = menu;
//                }
//            }
//        }
    }

    @Override
    public int compareTo(Tooth o) {
        return name.compareTo(o.name);
    }
}
