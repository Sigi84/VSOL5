package be.veterinarysolutions.vsol.gui.canvases;

import be.veterinarysolutions.vsol.data.Picture;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Vector;

public class PictureCanvas extends ResizableCanvas {

    private Vector<Picture> pics = new Vector<>();

    public void draw() {
        clear();
//        drawCross();

        for (Picture pic : pics) {
            drawPic(pic);
        }
    }

    private void drawPic(Picture pic) {
        GraphicsContext gg = getGraphicsContext2D();

        Image img = pic.getImg();
        if (img == null) return;

        double cw = getWidth(), ch = getHeight();
        double x = 0.0, y = 0.0;
        double iw = img.getWidth(), ih = img.getHeight();

        if (iw > cw) { // rescale on width
            double ratio = (cw / iw);
            iw *= ratio;
            ih *= ratio;

            if (ih > ch) { // after that ALSO rescale on height
                ratio = (ch / ih);
                iw *= ratio;
                ih *= ratio;
            }
        } else  if (ih > ch) { // only rescale on height
            double ratio = (ch / ih);
            iw *= ratio;
            ih *= ratio;
        }

        if (iw < cw) { // translate right
            x += (cw / 2.0) - (iw / 2.0);
        }
        if (ih < ch) { // translate down
            y += (ch / 2.0) - (ih / 2.0);
        }

        double sq = (iw + ih) / 2;
        Rectangle rect = new Rectangle(x + (iw / 2.0 - sq / 2.0), y + (ih / 2.0 - sq / 2.0), sq, sq);

        gg.save();

        // effect
        ColorAdjust ca = new ColorAdjust();
        ca.setBrightness(pic.getBrightness());
        ca.setContrast(pic.getContrast());

        gg.setEffect(ca);

        // translation
        gg.translate(pic.getTransx(), pic.getTransy());
        rect.setX(rect.getX() + pic.getTransx());
        rect.setY(rect.getY() + pic.getTransy());

        // mirror (x-axis) and flip (y-axis) if needed
        gg.translate(cw/2, ch/2);
        gg.scale(pic.getMirror(), pic.getFlip());
        gg.translate(-cw/2, -ch/2);

        // rotation
        gg.translate(cw/2, ch/2);
        gg.rotate(pic.getRotation() * pic.getMirror() * pic.getFlip());
        gg.translate(-cw/2, -ch/2);

        // zoom
        gg.translate(cw/2, ch/2);
        gg.scale(pic.getZoom(), pic.getZoom());
        gg.translate(-cw/2, -ch/2);

        rect.setX(rect.getX() + ( (sq - pic.getZoom() * sq) / 2.0));
        rect.setY(rect.getY() + ( (sq - pic.getZoom() * sq) / 2.0));
        rect.setWidth(sq * pic.getZoom());
        rect.setHeight(sq * pic.getZoom());


        if (pic.isSelected()) {
            gg.setGlobalAlpha(1.0);
        } else {
            gg.setGlobalAlpha(0.3);
        }

        gg.drawImage(img, x, y, iw, ih);

        if (pic.isSelected()) {
            ca = new ColorAdjust();
            gg.setEffect(ca);

            gg.setLineWidth(5.0);
            gg.setStroke(Color.RED);
            gg.strokeRect(x, y, iw, ih);
        }

        pic.setRect(rect);

        gg.restore();
    }

    private void drawCross() {
        GraphicsContext gg = getGraphicsContext2D();

        double width = getWidth();
        double height = getHeight();

        gg.clearRect(0, 0, width, height);

        gg.setStroke(Color.RED);
        gg.setLineWidth(1.0);

        gg.strokeLine(0, 0, width, height);
        gg.strokeLine(width, 0, 0, height);
    }

    public Vector<Picture> getPics() {
        return pics;
    }

    public void deleteSelection() {
        Vector<Picture> temp = new Vector<>();
        for (Picture pic : pics) {
            if (!pic.isSelected())
                temp.add(pic);
        }
        pics = temp;
    }
}
