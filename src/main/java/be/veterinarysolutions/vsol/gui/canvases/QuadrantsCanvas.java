package be.veterinarysolutions.vsol.gui.canvases;

import be.veterinarysolutions.vsol.data.Quadrant;
import be.veterinarysolutions.vsol.data.Tooth;
import be.veterinarysolutions.vsol.tools.Nr;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.TreeSet;
import java.util.Vector;

public class QuadrantsCanvas extends ResizableCanvas {

    private double scale = 1.0;
    private boolean inner = true;

    private Vector<Quadrant> quadrants = new Vector<>();

    public QuadrantsCanvas() {
        quadrants.add(new Quadrant("Q1", true, true));
        quadrants.add(new Quadrant("Q2", false, true));
        quadrants.add(new Quadrant("Q3", false, false));
        quadrants.add(new Quadrant("Q4", true, false));
    }

    public void draw() {
        clear();
        drawCross();

        for (Quadrant quadrant : quadrants) {
            drawQuadrant(quadrant);
        }

//        testDraw(quadrants.elementAt(3).getImg());
    }

    private void drawQuadrant(Quadrant quadrant) {
        Image img = quadrant.getImg();

        GraphicsContext gg = getGraphicsContext2D();

        double cw = getWidth(), ch = getHeight();
        double x = 0.0, y = 0.0;
        double iw = img.getWidth(), ih = img.getHeight();


        double ratio = ((ch / 2.0) * scale) / ih; // scale 1.0 = half of the image height (because it's a quadrant)
        iw *= ratio;
        ih *= ratio;

        if (inner) {
            x += cw / 2.0 - iw;
            y += ch / 2.0 - ih;

            if (!quadrant.isLeft())
                x += iw;
            if (!quadrant.isTop())
                y += ih;
        } else {
            if (!quadrant.isLeft())
                x += cw - iw;
            if (!quadrant.isTop())
                y += ch - ih;
        }

        for (Tooth tooth : quadrant.getTeeth()) {
            if (tooth.isReady()) {
                gg.drawImage(tooth.getImgOrange(), x, y, iw, ih);
            } else if (tooth.isSelected()) {
                gg.drawImage(tooth.getImgGreen(), x, y, iw, ih);
            }
        }
        gg.drawImage(img, x, y, iw, ih);
        quadrant.setRect(new Rectangle(x, y, iw, ih));
    }

    private double testWidth, testHeight;
    private void testDraw(Image img) {
        GraphicsContext gg = getGraphicsContext2D();

        double cw = getWidth(), ch = getHeight();
        double x = 0.0, y = 0.0;
        double iw = img.getWidth(), ih = img.getHeight();


        double ratio = ch / ih; // scale 1.0 = half of the image height (because it's a quadrant)
        iw *= ratio;
        ih *= ratio;

        testWidth = iw;
        testHeight = ih;

        gg.drawImage(img, x, y, iw, ih);
    }

    private void drawCross() {
        GraphicsContext gg = getGraphicsContext2D();

        double width = getWidth();
        double height = getHeight();

        gg.clearRect(0, 0, width, height);

        gg.setStroke(Color.BLUE);
        gg.setLineWidth(1.0);

        gg.strokeLine(width / 2.0, 0, width / 2.0, height);
        gg.strokeLine(0, height / 2.0, width, height / 2.0);
    }

    public void select(double x, double y) {
        for (Quadrant quadrant : quadrants) {
            Rectangle rect = quadrant.getRect();
            if (rect.contains(x, y)) {
                for (Tooth tooth : quadrant.getTeeth()) {
                    if (tooth.contains(x - rect.getX(), y - rect.getY(), rect.getWidth(), rect.getHeight())) {
//                        System.out.println(tooth.getName());
                        tooth.setSelected(!tooth.isSelected());
                    }
                }

            }
        }
    }

    public void testSelect(boolean primary, double x, double y) {
        if (primary)
            System.out.println(Nr.fullPrecision(x / testWidth));
        else
            System.out.println(Nr.fullPrecision(y / testHeight));
    }

    public void selectAll(boolean selected) {
        for (Quadrant quadrant : quadrants) {
            for (Tooth tooth : quadrant.getTeeth()) {
                tooth.setSelected(selected);
            }
        }
    }

    // GETTERS AND SETTERS

    public boolean isInner() {
        return inner;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public void setInner(boolean inner) {
        this.inner = inner;
    }

    public Vector<Quadrant> getQuadrants() {
        return quadrants;
    }

    public TreeSet<Tooth> getSelectedTeeth() {
        TreeSet<Tooth> result = new TreeSet<>();

        for (Quadrant quadrant : quadrants) {
            for (Tooth tooth : quadrant.getTeeth()) {
                if (tooth.isSelected()) {
                    result.add(tooth);
                }
            }
        }

        return result;
    }


}
