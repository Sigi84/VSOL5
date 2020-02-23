package be.veterinarysolutions.vsol.gui;

import be.veterinarysolutions.vsol.main.Options;
import be.veterinarysolutions.vsol.tools.Nr;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Stack;

public class Viewer extends Controller {
	private Logger logger = LogManager.getLogger();

	@FXML private Canvas canvas;
	@FXML private BorderPane right;
	@FXML private VBox menu;
	@FXML private Button btnSliders;

	private boolean multitouch = false;
	private boolean primaryMouseButtonDown = false; // true when the primary mouse button is down
	private Stack<Image> imgs = new Stack<>(); // stack of converted images
	private Stack<File> files = new Stack<>(); // stack of associated file names
	private int stackIndex = -1;
	private double brightness = 0.0, contrast = 0.0, rotation = 0.0, zoom = 1.0;
	private double rotated = 0.0, zoomed = 1.0;
	private double touchx = 0.0, touchy = 0.0;
	private double mousex = 0.0, mousey = 0.0;

	// PRIVATE

	private void init() {

	}

	private void open(File file) {
		if (file == null) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(new File("C:/Sandbox/"));
			file = fileChooser.showOpenDialog(gui.getPrimaryStage());

			if (file != null) {
				files.clear();
				imgs.clear();
				stackIndex = -1;
				reset();

				files.add(file);
			}
		}

		if (file != null) {
			Image img = new Image("file:" + file.getAbsolutePath());

			imgs.add(img);
			stackIndex++;

			drawImage();
		}
	}

	private void openTest(MouseButton button) {
		if (button == MouseButton.PRIMARY) {
			open(null);
		} else {
			File newFile = new File("C:/Sandbox/examplepic.jpg");
			reset();
			files.add(newFile);
			open(newFile);
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

	private void touchPressed(TouchPoint p, int touchCount) {
		if (p == null) return;

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

	private void mouseDragged(double currentx, double currenty) {
		if (primaryMouseButtonDown) {
			double offsetx = currentx - mousex;
			mousex = currentx;
			changeContrast(offsetx / 1000.0);

			double offsety = currenty - mousey;
			mousey = currenty;
			changeBrightness(offsety / 1000.0);
		}
	}

	private void mousePressed(double x, double y, boolean primary) {
		mousex = x;
		mousey = y;

		if (primary) {
			primaryMouseButtonDown = true;
		}
	}

	private void mouseReleased() {
		mousex = 0.0;
		mousey = 0.0;

		primaryMouseButtonDown = false;
	}

	private void drawImage() {
		clearCanvas();

		if (stackIndex == -1 || stackIndex > imgs.size()) return;
		Image img = imgs.get(stackIndex);
		if (img == null) return;

		resizeCanvas();
		clearCanvas();

		GraphicsContext gg = canvas.getGraphicsContext2D();

		double cw = canvas.getWidth(), ch = canvas.getHeight();
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
		} else  if (ih > ch) { // rescale on height
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

		// effect
		ColorAdjust ca = new ColorAdjust();
		ca.setBrightness(brightness);
		ca.setContrast(contrast);
		gg.setEffect(ca);

		// rotation
		gg.translate(cw/2, ch/2);
		gg.rotate(rotation);
		rotation = 0.0;
		gg.translate(-cw/2, -ch/2);

		// zoom
		gg.translate(cw/2, ch/2);
		gg.scale(zoom, zoom);
		zoom = 1.0;
		gg.translate(-cw/2, -ch/2);

		gg.drawImage(img, x, y, iw, ih);
	}

	private void resizeCanvas() {
		boolean isSliders = btnSliders.getProperties().get("selected") == null ? false : (Boolean) btnSliders.getProperties().get("selected");
		double sliderWidth = isSliders ? gui.getSliders().getWidth() : 0.0;

		double width = gui.getFrame().getBgWidth() - gui.getFrame().getMenuWidth() - getMenuWidth() - sliderWidth;
		double height = gui.getFrame().getBgHeight();

		canvas.setWidth(width);
		canvas.setHeight(height);
	}

	private void rotateMagick(double angle) {
		if (stackIndex == -1) return;

		File file = files.get(stackIndex);
		if (file == null) return;

		ConvertCmd cmd = new ConvertCmd(true);

		IMOperation operation = new IMOperation();
		operation.rotate(angle);

		String newFilename = getNewFilename();

		operation.addImage(file.getAbsolutePath(), newFilename); // eg C:/image.jpg_2

		try {
			cmd.run(operation);
			File newFile = new File(newFilename);
			files.add(newFile);
			open(newFile);

		} catch (IOException e) {
			logger.error(e);
		} catch (InterruptedException e) {
			logger.error(e);
		} catch (IM4JavaException e) {
			logger.error(e);
		}
	}

	private void canvasRotated(double totalAngle) {
		totalAngle -= rotated;

		if (Math.abs(totalAngle) >= Options.ROTATION_STEP) {
			double angle = totalAngle - (totalAngle % Options.ROTATION_STEP);
			rotate(angle);
			rotated += angle;
		}
	}

	private void rotate(double angle) {
		rotation += angle;
		rotation = rotation % 360.0;

		drawImage();
	}

	private void canvasZoomed(double totalZoom) {
		totalZoom /= zoomed;

		if (Math.abs(totalZoom - 1.0) > Options.ZOOM_STEP) {
			zoom(totalZoom);
			drawImage();

			zoomed *= totalZoom;
		}
	}

	private void zoom(double factor) {
		zoom *= factor;

		drawImage();
	}

	private String getNewFilename() {
		return files.get(0).getAbsolutePath() + "_" + (stackIndex + 1);
	}

	private void clearCanvas() {
		GraphicsContext gg = canvas.getGraphicsContext2D();

		ColorAdjust ca = new ColorAdjust();
		ca.setBrightness(0.0);
		ca.setContrast(0.0);
		gg.setEffect(ca);

		gg.setFill(Color.BLACK);
		gg.fillRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight());
	}

	private void changeBrightness(double offset) {
		brightness += offset;
		if (brightness < -1.0) brightness = -1.0;
		else if (brightness > +1.0) brightness = +1.0;

//		logBrightness();
		drawImage();
	}

	private void changeContrast(double offset) {
		contrast += offset;
		if (contrast < -1.0) contrast = -1.0;
		else if (contrast > +1.0) contrast = +1.0;

//		logContrast();
		drawImage();
	}

	private void reset() {
		brightness = 0.0;
		contrast = 0.0;
		rotation = 0.0;
		zoom = 1.0;

//		logBrightness();
		drawImage();
	}

	private void logBrightness() {
		logger.info("Brightness: " + Nr.perc(brightness * 100.0, true));
	}

	private void logContrast() {
		logger.info("Contrast: " + Nr.perc(contrast * 100.0, true));
	}

	private void refresh() {
		if (imgs.size() > 1) { // revert back to the original
			while (imgs.size() > 1) {
				imgs.pop();
				files.pop();
			}
			stackIndex = 0;
			drawImage();
		} else if (imgs.size() == 1) { // clear
			imgs.clear();
			files.clear();
			stackIndex = -1;
			drawImage();
		}
	}

	private void sliders() {
		boolean selected = btnSliders.getProperties().get("selected") == null ? false : (Boolean) btnSliders.getProperties().get("selected");
		if (selected) {
			btnSliders.setStyle("-fx-background-color: #AAAAAA");
			right.setCenter(null);
			btnSliders.getProperties().put("selected", false);
		} else {
			btnSliders.setStyle("-fx-background-color: red;");
			right.setCenter(gui.getNodeSliders());
			btnSliders.getProperties().put("selected", true);
		}
		drawImage();
	}

	private void undo() {

	}

	private void redo() {

	}

	// PUBLIC

	public void resize() {
		drawImage();
	}

	// EVENT

	@FXML public void initialize() { init(); }

	@FXML protected void btnOpenMouseClicked(MouseEvent e) { openTest(e.getButton()); }

	@FXML protected void btnOpenTouchPressed(TouchEvent e) { } // open(null); }

	@FXML protected void btnRotateMouseClicked(MouseEvent e) { rotate(+Options.ROTATION_STEP); }

	@FXML protected void btnRotateTouchPressed(TouchEvent e) { } //  rotate(+10.0); }

	@FXML protected void btnCounterMouseClicked(MouseEvent e) { rotate(-Options.ROTATION_STEP); }

	@FXML protected void btnCounterTouchPressed(TouchEvent e) { } //  rotate(-10.0); }

	@FXML protected void btnZoomInMouseClicked(MouseEvent e) { zoom(1.0 + Options.ZOOM_STEP); }

	@FXML protected void btnZoomInTouchPressed(TouchEvent e) { } //  zoom(+0.1); }

	@FXML protected void btnZoomOutMouseClicked(MouseEvent e) { zoom(1.0 - Options.ZOOM_STEP); }

	@FXML protected void btnZoomOutTouchPressed(TouchEvent e) { } //  zoom(-0.1); }

	@FXML protected void btnSlidersMouseClicked(MouseEvent e) { sliders(); }

	@FXML protected void btnSlidersTouchPressed(TouchEvent e) { } //  sliders(); }

	@FXML protected void btnRefreshMouseClicked(MouseEvent e) { refresh(); }

	@FXML protected void btnRefreshTouchPressed(TouchEvent e) { } //  refresh(); }

	@FXML protected void btnUndoMouseClicked(MouseEvent e) { undo(); }

	@FXML protected void btnUndoTouchPressed(TouchEvent e) { } //  undo(); }

	@FXML protected void btnRedoMouseClicked(MouseEvent e) { redo(); }

	@FXML protected void btnRedoTouchPressed(TouchEvent e) { } //  redo(); }

	@FXML protected void canvasMouseDragged(MouseEvent e) { mouseDragged(e.getX(), e.getY());}

	@FXML protected void canvasMousePressed(MouseEvent e) { mousePressed(e.getX(), e.getY(), e.getButton() == MouseButton.PRIMARY && !e.isSynthesized()); }

	@FXML protected void canvasMouseReleased(MouseEvent e) { mouseReleased(); }

	@FXML protected void canvasTouchMoved(TouchEvent e) { touchMoved(e.getTouchPoint());}

	@FXML protected void canvasTouchPressed(TouchEvent e) {
		touchPressed(e.getTouchPoint(), e.getTouchCount());
	}

	@FXML protected void canvasTouchReleased(TouchEvent e) { touchReleased(e.getTouchCount()); }

	@FXML protected void canvasRotate(RotateEvent e) { canvasRotated(e.getTotalAngle()); }

	@FXML protected void canvasRotateFinished(RotateEvent e) { rotated = 0.0; }

	@FXML protected void canvasZoom(ZoomEvent e) {
		canvasZoomed(e.getTotalZoomFactor());
	}

	@FXML protected void canvasZoomFinished(ZoomEvent e) { zoomed = 1.0; }

	// GETTERS

	public double getRightWidth() {
		return right.widthProperty().doubleValue();
	}

	public double getMenuWidth() {
		return menu.widthProperty().doubleValue();
	}
}
