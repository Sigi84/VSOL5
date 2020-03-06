package be.veterinarysolutions.vsol.gui.canvases;

import be.veterinarysolutions.vsol.data.Quadrant;
import be.veterinarysolutions.vsol.data.Tooth;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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

//        for (Quadrant quadrant : quadrants) {
//            drawQuadrant(quadrant);
//        }

        testDraw(quadrants.elementAt(0).getImg());
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
            if (tooth.isSelected()) {
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

    public boolean isInner() {
        return inner;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public void setInner(boolean inner) {
        this.inner = inner;
    }

    public void select(double x, double y) {

        System.out.println((x / testWidth) + "   " + (y / testHeight));

//        for (Quadrant quadrant : quadrants) {
//            Rectangle rect = quadrant.getRect();
//            if (rect.contains(x, y)) {
//                for (Tooth tooth : quadrant.getTeeth()) {
//                    if (tooth.contains(x - rect.getX(), y - rect.getY(), rect.getWidth(), rect.getHeight())) {
//                        System.out.println(tooth.getName());
//                        tooth.setSelected(!tooth.isSelected());
//                    }
//                }
//
//            }
//        }
    }
}
