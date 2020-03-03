package be.veterinarysolutions.vsol.gui;

import be.veterinarysolutions.vsol.data.Picture;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class ResizableCanvas extends Canvas {

    private Picture pic;
    private Circle circle;
    private Rectangle rect;

    public ResizableCanvas(Picture pic) {
        this.pic = pic;
        widthProperty().addListener(e -> draw());
        heightProperty().addListener(e -> draw());
    }

    private void draw() {
        double width = getWidth();
        double height = getHeight();

        GraphicsContext gg = getGraphicsContext2D();
        gg.clearRect(0, 0, height, height);

        Image img = pic.getImg();

        double square = img.getWidth() > img.getHeight() ? img.getWidth() : img.getHeight();
        double radius = square / 2;

        rect = new Rectangle(0, 0, square, square);
        circle = new Circle(0, 0, radius);

        gg.save();



        gg.translate(width / 2.0 - img.getWidth() / 2.0, height / 2.0 - img.getHeight() / 2.0);
        rect.setX(rect.getX() + (width / 2.0 - square / 2.0));
        rect.setY(rect.getY() + (height / 2.0 - square / 2.0));
        circle.setCenterX(circle.getCenterX() + width / 2.0);
        circle.setCenterY(circle.getCenterY() + height / 2.0);



        gg.translate(rect.getWidth() / 2.0, rect.getHeight() / 2.0);
        gg.rotate(0.0);
        gg.translate(-rect.getWidth() / 2.0, -rect.getHeight() / 2.0);

        gg.translate(img.getWidth() / 2.0, img.getHeight() / 2.0);
        gg.scale(2.0, 2.0);
        gg.translate(-img.getWidth() / 2.0, -img.getHeight() / 2.0);

        rect.setX(rect.getX() - (square / 2.0));
        rect.setY(rect.getY() - (square / 2.0));
        rect.setWidth(square * 2.0);
        rect.setHeight(square * 2.0);

        circle.setRadius(radius * 2.0);

        gg.setStroke(Color.RED);
        gg.drawImage(img, 0, 0, img.getWidth(), img.getHeight());

        gg.restore();

        gg.setStroke(Color.BLUE);
        System.out.println(rect.getX() + " " + rect.getY() + "   " + rect.getWidth() + " " + rect.getHeight());
        gg.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        gg.setStroke(Color.ORANGE);
        gg.strokeOval(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }

    public Circle getCircle() {
        return circle;
    }

    public Rectangle getRect() {
        return rect;
    }
}
