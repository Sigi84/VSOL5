package be.veterinarysolutions.vsol.gui.canvases;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class ResizableCanvas extends Canvas {

    public ResizableCanvas() {
        widthProperty().addListener(e -> draw());
        heightProperty().addListener(e -> draw());
    }

    public abstract void draw();

    public void clear() {
        GraphicsContext gg = getGraphicsContext2D();
        gg.clearRect(0, 0, getWidth(), getHeight());
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

}
