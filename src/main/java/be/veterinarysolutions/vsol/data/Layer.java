package be.veterinarysolutions.vsol.data;

import be.veterinarysolutions.vsol.main.Options;
import javafx.scene.image.Image;

public abstract class Layer {
    protected double brightness = 0.0, contrast = 0.0;
    protected double transx = 0.0, transy = 0.0;
    protected double rotation = 0.0, zoom = 1.0;
    protected double flip = 1.0, mirror = 1.0;
    protected boolean selected = false;

    protected void reset() {
        brightness = 0.0;
        contrast = 0.0;
        transx = 0.0;
        transy = 0.0;
        rotation = 0.0;
        zoom = 1.0;
        flip = 1.0;
        mirror = 1.0;
    }

    // GETTERS

    public double getBrightness() {
        return brightness;
    }

    public double getContrast() {
        return contrast;
    }

    public double getTransx() {
        return transx;
    }

    public double getTransy() {
        return transy;
    }

    public double getRotation() {
        return rotation;
    }

    public double getZoom() {
        return zoom;
    }

    public double getFlip() {
        return flip;
    }

    public double getMirror() {
        return mirror;
    }

    public boolean isSelected() {
        return selected;
    }

    // SETTERS

    public void setBrightness(double brightness) {
        if (brightness < -1.0) brightness = -1.0;
        if (brightness > +1.0) brightness = +1.0;
        this.brightness = brightness;
    }

    public void setContrast(double contrast) {
        if (contrast < -1.0) contrast = -1.0;
        if (contrast > +1.0) contrast = +1.0;
        this.contrast = contrast;
    }

    public void setTransx(double transx) {
        this.transx = transx;
    }

    public void setTransy(double transy) {
        this.transy = transy;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation % 360.0;
    }

    public void setZoom(double zoom) {
        if (zoom <= Options.ZOOM_STEP_SMALL)
            zoom = Options.ZOOM_STEP_SMALL;
        this.zoom = zoom;
    }

    public void setFlip(double flip) {
        this.flip = flip;
    }

    public void setMirror(double mirror) {
        this.mirror = mirror;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
