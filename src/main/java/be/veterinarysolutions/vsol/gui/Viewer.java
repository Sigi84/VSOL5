package be.veterinarysolutions.vsol.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.Operation;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Viewer extends Controller {

	@FXML private BorderPane pane;
	@FXML private Canvas canvas;
	@FXML private VBox vbox;
	@FXML private Button btnRotate;

	private boolean multitouch = false;
	private Image img;
	private double brightness = 0.0, contrast = 0.0;
	private double touchx = 0.0, touchy = 0.0;

	// PRIVATE

	private void open() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("C:/Sandbox/"));

//		File file = fileChooser.showOpenDialog(gui.getPrimaryStage());
		File file = new File("C:/Sandbox/examplepic.jpg");

		if (file != null) {
			setBrightness(0.0);
			setContrast(0.0);

			img = new Image("file:" + file.getAbsolutePath());
			setImage();
		}
	}

	private void touchMoved(TouchPoint p) {
		if (!multitouch) {
			double currentx = p.getX();
			double offsetx = currentx - touchx;
			touchx = currentx;
			changeContrast(offsetx / 1000.0);

			double currenty = p.getY();
			double offsety = currenty - touchy;
			touchy = currenty;
			changeBrightness(offsety / 1000.0);
		}
	}

	private void touchPressed(@NotNull TouchPoint p, int touchCount) {
		touchx = p.getX();
		touchy = p.getY();
		if (touchCount > 1) {
			multitouch = true;
		}
	}

	private void touchReleased(int touchCount) {
		touchx = 0.0;
		touchy = 0.0;

		if (touchCount == 1) {
			multitouch = false;
		}
	}

	private void rotate(double angle) {

	}

	private void zoom(double level) {
		canvas.setScaleX(2.0);
	}

	private void setImage() {
		if (img == null)  return;

		resizeImg();
		GraphicsContext gg = canvas.getGraphicsContext2D();

		double x = 0.0, y = 0.0;
		double cw = canvas.getWidth(), ch = canvas.getHeight();
		double iw = img.getWidth(), ih = img.getHeight();
		double ratio = 1.0;

		if (iw > cw) { // rescale on width
			ratio *= (cw / iw);
		}
		if (ih > ch) { // rescale on height
			ratio *= (ch / ih);
		}

		double w = iw * ratio;
		double h = ih * ratio;

		if (w < cw) { // translate right
			x += (cw / 2.0) - (w / 2.0);
		}
		if (h < ch) { // translate down
			y += (ch / 2.0) - (h / 2.0);
		}

		gg.clearRect(0.0, 0.0, cw, ch);
		gg.drawImage(img, x, y, w, h);
	}

	private void resizeImg() {
		double width = pane.getLayoutBounds().getWidth() - gui.getFrame().getVbox().getLayoutBounds().getWidth() - vbox.getLayoutBounds().getWidth();
		double height = pane.getLayoutBounds().getHeight() - gui.getFrame().getHbox().getLayoutBounds().getHeight();

		canvas.setWidth(width);
		canvas.setHeight(height);
	}

	private void changeBrightness(double offset) {
		if (brightness < -1.0) return;
		if (brightness > +1.0) return;

		GraphicsContext gg = canvas.getGraphicsContext2D();
		ColorAdjust ca = new ColorAdjust();

		ca.setContrast(contrast);
		ca.setBrightness(brightness += offset);

		gg.setEffect(ca);
		setImage();

		if (brightness < -1.0) brightness = -1.0;
		else if (brightness > +1.0) brightness = +1.0;
	}

	private void changeContrast(double offset) {
		if (contrast < -1.0) return;
		if (contrast > +1.0) return;

		GraphicsContext gg = canvas.getGraphicsContext2D();
		ColorAdjust ca = new ColorAdjust();

		ca.setContrast(contrast += offset);
		ca.setBrightness(brightness);

		gg.setEffect(ca);
		setImage();

		if (contrast < -1.0) contrast = -1.0;
		else if (contrast > +1.0) contrast = +1.0;
	}

	private void setBrightness(double value) {
		GraphicsContext gg = canvas.getGraphicsContext2D();
		ColorAdjust ca = new ColorAdjust();

		ca.setBrightness(value);
		gg.setEffect(ca);
		setImage();

		brightness = value;
	}

	private void setContrast(double value) {
		GraphicsContext gg = canvas.getGraphicsContext2D();
		ColorAdjust ca = new ColorAdjust();

		ca.setContrast(value);
		gg.setEffect(ca);
		setImage();

		contrast = value;
	}

	// PUBLIC

	public void resize() {
		setImage();
	}

	// EVENT

	@FXML public void initialize() { }

	@FXML protected void btnOpenMouseClicked(MouseEvent e) { open(); }

	@FXML protected void btnOpenTouchPressed(TouchEvent e) { open(); }

	@FXML protected void btnRotateAction(ActionEvent e) { }

	@FXML protected void btnHorseAction(ActionEvent e) { }

	@FXML protected void canvasTouchMoved(TouchEvent e) { touchMoved(e.getTouchPoint());}

	@FXML protected void canvasTouchPressed(TouchEvent e) { touchPressed(e.getTouchPoint(), e.getTouchCount()); }

	@FXML protected void canvasTouchReleased(TouchEvent e) { touchReleased(e.getTouchCount()); }

	@FXML protected void canvasRotate(RotateEvent e) { rotate(e.getTotalAngle()); }

	@FXML protected void canvasZoom(ZoomEvent e) { zoom(e.getTotalZoomFactor()); }

}
