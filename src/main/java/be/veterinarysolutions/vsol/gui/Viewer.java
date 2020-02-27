package be.veterinarysolutions.vsol.gui;

import be.veterinarysolutions.vsol.interfaces.Pollable;
import be.veterinarysolutions.vsol.main.Options;
import be.veterinarysolutions.vsol.tools.Nr;
import be.veterinarysolutions.vsol.tools.WatchDir;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
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
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchKey;
import java.text.DecimalFormat;
import java.util.Stack;
import java.util.Vector;

public class Viewer extends Controller implements Pollable {
	private Logger logger = LogManager.getLogger();

	@FXML private Canvas canvas;
	@FXML private BorderPane bg, right;
	@FXML private VBox menu;
	@FXML private HBox hbox;
	@FXML private Button btnSliders, btnBrightness, btnVertebra;
	@FXML private Label lblInfo1, lblInfo2;
	@FXML private Pane pane;

	private boolean multitouch = false;
	private boolean mousePrimaryDown = false; // true when the primary mouse button is down
	private boolean mouseSecundaryDown = false; // secondary mouse button is down

	private Image basicImg = null, basicImg2 = null;
	private Vector<Image> convertedImgs = new Vector<>(); // stack of converted images
	private int imgIndex = -1;
	private File basicFile = null;

	private double transx = 0.0, transy = 0.0;
	private double mirror = 1.0, flip = 1.0;
	private double brightness = 0.0, contrast = 0.0, rotation = 0.0, zoom = 1.0;
	private double rotated = 0.0, zoomed = 1.0;

	private double touchx = 0.0, touchy = 0.0;
	private double mousex = 0.0, mousey = 0.0;

	public static enum DrawMode { NONE, BRIGHTNESS, VERTEBRA };
	private DrawMode drawMode = DrawMode.BRIGHTNESS;

	// PRIVATE

	private void open(File file, File file2) {
		if (file == null) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(new File(Options.START_DIR));
			file = fileChooser.showOpenDialog(gui.getPrimaryStage());
		}

		if (file != null) {
			Image img = new Image("file:" + file.getAbsolutePath());
			Image img2 = new Image("file:" + file2.getAbsolutePath());

			if (imgIndex == -1) {
				reset();
				convertedImgs.clear();

				basicImg = img;
				basicImg2 = img2;
				basicFile = file;
				lblInfo1.setText(file.getName());
				lblInfo2.setText(file2.getName());
			} else {
				convertedImgs.add(img);
			}

			drawImage();
		}
	}

	private void openTest(MouseButton button) {
		if (button == MouseButton.PRIMARY) {
			open(null, null);
		} else {
			File newFile = new File("C:/Sandbox/examplepic.jpg");
			File newFile2 = new File("C:/Sandbox/dog.jpg");

			open(newFile, newFile2);
		}
	}

	private void drawImage() {
//		clearCanvas();
//		fillSliders();
//		Image img = basicImg;
//		if (imgIndex > -1) {
//			img = convertedImgs.elementAt(imgIndex);
//		}
//
//		if (img == null) return;
//
//		resizeCanvas();
//		clearCanvas();
//
//		double cw = canvas.getWidth(), ch = canvas.getHeight();
//		double x = 0.0, y = 0.0;
//		double iw = img.getWidth(), ih = img.getHeight();
//
//		if (iw > cw) { // rescale on width
//			double ratio = (cw / iw);
//			iw *= ratio;
//			ih *= ratio;
//
//			if (ih > ch) { // after that ALSO rescale on height
//				ratio = (ch / ih);
//				iw *= ratio;
//				ih *= ratio;
//			}
//		} else  if (ih > ch) { // rescale on height
//			double ratio = (ch / ih);
//			iw *= ratio;
//			ih *= ratio;
//		}
//
//		if (iw < cw) { // translate right
//			x += (cw / 2.0) - (iw / 2.0);
//		}
//		if (ih < ch) { // translate down
//			y += (ch / 2.0) - (ih / 2.0);
//		}
//
//		GraphicsContext gg = canvas.getGraphicsContext2D();
//		gg.save();
//
//		// effect
//		ColorAdjust ca = new ColorAdjust();
//		ca.setBrightness(brightness);
//		ca.setContrast(contrast);
//		gg.setEffect(ca);
//
//		// translation
//		gg.translate(transx, transy);
//
//		// mirror (x-axis) and flip (y-axis) if needed
//		gg.translate(cw/2, ch/2);
//		gg.scale(mirror, flip);
//		gg.translate(-cw/2, -ch/2);
//
//		// rotation
//		gg.translate(cw/2, ch/2);
//		gg.rotate(rotation * mirror * flip);
//		gg.translate(-cw/2, -ch/2);
//
//		// zoom
//		gg.translate(cw/2, ch/2);
//		gg.scale(zoom, zoom);
//		gg.translate(-cw/2, -ch/2);
//
//		gg.drawImage(img, x, y, iw, ih);
//
//		gg.restore();

		// first
		Canvas one = new Canvas(300, 300);
		GraphicsContext g1 = one.getGraphicsContext2D();
		g1.setFill(new Color(0, 0, 1.0, 0.5));
		g1.fillRect(0, 0, 300, 300);
		one.setLayoutX(50);
		one.setLayoutY(50);
		pane.getChildren().add(one);

		// second
		Canvas sub = new Canvas(500, 500);
		GraphicsContext g2 = sub.getGraphicsContext2D();
		g2.setFill(new Color(1.0, 0, 0, 0.5));
		g2.fillRect(0, 0, 500, 500);
		pane.getChildren().add(sub);

	}

	private void resizeCanvas() {
		boolean isSliders = btnSliders.getProperties().get("selected") == null ? false : (Boolean) btnSliders.getProperties().get("selected");
		double sliderWidth = isSliders ? gui.getSliders().getWidth() : 0.0;

		double width = gui.getFrame().getBgWidth() - gui.getFrame().getMenuWidth() - getMenuWidth() - sliderWidth;
		double height = gui.getFrame().getBgHeight() - getBottomWidth();

		canvas.setWidth(width);
		canvas.setHeight(height);
	}

	private void fillSliders() {
		gui.getSliders().getBtnUndo().setDisable(imgIndex == -1);
		gui.getSliders().getBtnRedo().setDisable(imgIndex >= convertedImgs.size() - 1);
	}

	private void rotateMagick(double angle) {
		logger.debug("rotate magick: " + angle);
		File file = basicFile;
		if (file == null) return;

		ConvertCmd cmd = new ConvertCmd(true);

		IMOperation operation = new IMOperation();
		operation.rotate(angle);

		imgIndex++;
		operation.addImage(file.getAbsolutePath(), Options.START_DIR + file.getName() + "_" + imgIndex + ".tmp");

		try {
			cmd.run(operation);
		} catch (IOException | InterruptedException | IM4JavaException e) {
			logger.error(e);
		}
	}

	private void rotate(double angle) {
		rotation += angle;
		rotation = rotation % 360.0;

		drawImage();
	}

	private void zoom(double factor) {
		zoom *= factor;

		drawImage();
	}

	private void translate(double x, double y) {
		transx += x;
		transy += y;

		drawImage();
	}

	public void clearCanvas() {
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
		transx = 0.0;
		transy = 0.0;

		gui.getSliders().getSliderHistogram().setValue(0.0);
	}

	private void refresh() {
		imgIndex = -1;

		if (convertedImgs.size() > 0) {
			convertedImgs.clear();
		} else {
			basicImg = null;
		}

		drawImage();
	}

	private TouchPoint getFirstTouchPoint(TouchEvent e) {
		for (TouchPoint p : e.getTouchPoints()) {
			if (p.getId() == 1) {
				return p;
			}
		}
		return null;
	}

	private void fill() {
		btnBrightness.getStyleClass().removeAll("selected");
		btnVertebra.getStyleClass().removeAll("selected");

		switch (drawMode) {
			case BRIGHTNESS:
				btnBrightness.getStyleClass().add("selected");
				break;
			case VERTEBRA:
				btnVertebra.getStyleClass().add("selected");
				break;
		}
	}

	// PUBLIC

	public void resize() {
		drawImage();
	}

	public void histogramOptimization(double value) {
		rotateMagick(value);
	}

	@Override
	public void fileModified(Path path) {
		File file = new File(path.toString());
		open(file, null);
	}

	public void undo() {
		if (imgIndex >= -1) {
			imgIndex--;
			drawImage();
		}
	}

	public void redo() {
		if (imgIndex < convertedImgs.size() - 1) {
			imgIndex++;
			drawImage();
		}
	}

	public void deleteTempFiles() {
		File dir = new File(Options.START_DIR);
		for (File file : dir.listFiles()) {
			if (file.getName().endsWith(".tmp")) {
				file.delete();
			}
		}
	}

	// EVENT

	@FXML public void initialize() {
		lblInfo1.setText("");
		lblInfo2.setText("");

		Path dir = Paths.get(Options.START_DIR);

		try {
			new WatchDir(dir, false, this).processEvents();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// BUTTONS

	@FXML protected void btnOpenMouseClicked(MouseEvent e) {
		openTest(e.getButton());
	}


	@FXML protected void btnRotateMousePressed(MouseEvent e) {
		if (e.isSynthesized()) return; // ignore Touch

		if (e.getButton() == MouseButton.PRIMARY) {
			rotate(+Options.ROTATION_STEP_BIG / 2.0);
		} else if (e.getButton() == MouseButton.SECONDARY) {
			rotate(+Options.ROTATION_STEP_SMALL / 2.0);
		}
	}

	@FXML protected void btnRotateMouseReleased(MouseEvent e) {
		if (e.isSynthesized()) return; // ignore Touch

		if (e.getButton() == MouseButton.PRIMARY) {
			rotate(+Options.ROTATION_STEP_BIG / 2.0);
		} else if (e.getButton() == MouseButton.SECONDARY) {
			rotate(+Options.ROTATION_STEP_SMALL / 2.0);
		}
	}

	@FXML protected void btnRotateTouchPressed(TouchEvent e) {
		rotate(+Options.ROTATION_STEP_BIG / 2.0);
	}

	@FXML protected void btnRotateTouchReleased(TouchEvent e) {
		rotate(+Options.ROTATION_STEP_BIG / 2.0);
	}

	@FXML protected void btnCounterMousePressed(MouseEvent e) {
		if (e.isSynthesized()) return; // ignore Touch

		if (e.getButton() == MouseButton.PRIMARY) {
			rotate(-Options.ROTATION_STEP_BIG / 2.0);
		} else if (e.getButton() == MouseButton.SECONDARY) {
			rotate(-Options.ROTATION_STEP_SMALL / 2.0);
		}
	}

	@FXML protected void btnCounterMouseReleased(MouseEvent e) {
		if (e.isSynthesized()) return; // ignore Touch

		if (e.getButton() == MouseButton.PRIMARY) {
			rotate(-Options.ROTATION_STEP_BIG / 2.0);
		} else if (e.getButton() == MouseButton.SECONDARY) {
			rotate(-Options.ROTATION_STEP_SMALL / 2.0);
		}
	}

	@FXML protected void btnCounterTouchPressed(TouchEvent e) {
		rotate(-Options.ROTATION_STEP_BIG / 2.0);
	}

	@FXML protected void btnCounterTouchReleased(TouchEvent e) {
		rotate(-Options.ROTATION_STEP_BIG / 2.0);
	}


	@FXML protected void btnZoomInMousePressed(MouseEvent e) {
		if (e.isSynthesized()) return; // ignore Touch

		if (e.getButton() == MouseButton.PRIMARY) {
			zoom(1.0 + (Options.ZOOM_STEP_BIG / 2.0));
		} else if (e.getButton() == MouseButton.SECONDARY) {
			zoom(1.0 + (Options.ZOOM_STEP_SMALL / 2.0));
		}
	}

	@FXML protected void btnZoomInMouseReleased(MouseEvent e) {
		if (e.isSynthesized()) return; // ignore Touch

		if (e.getButton() == MouseButton.PRIMARY) {
			zoom(1.0 + (Options.ZOOM_STEP_BIG / 2.0));
		} else if (e.getButton() == MouseButton.SECONDARY) {
			zoom(1.0 + (Options.ZOOM_STEP_SMALL / 2.0));
		}
	}

	@FXML protected void btnZoomInTouchPressed(TouchEvent e) {
		zoom(1.0 + (Options.ZOOM_STEP_BIG / 2.0));
	}

	@FXML protected void btnZoomInTouchReleased(TouchEvent e) {
		zoom(1.0 + (Options.ZOOM_STEP_BIG / 2.0));
	}


	@FXML protected void btnZoomOutMousePressed(MouseEvent e) {
		if (e.isSynthesized()) return; // ignore Touch

		if (e.getButton() == MouseButton.PRIMARY) {
			zoom(1.0 - (Options.ZOOM_STEP_BIG / 2.0));
		} else if (e.getButton() == MouseButton.SECONDARY) {
			zoom(1.0 - (Options.ZOOM_STEP_SMALL / 2.0));
		}
	}

	@FXML protected void btnZoomOutMouseReleased(MouseEvent e) {
		if (e.isSynthesized()) return; // ignore Touch

		if (e.getButton() == MouseButton.PRIMARY) {
			zoom(1.0 - (Options.ZOOM_STEP_BIG / 2.0));
		} else if (e.getButton() == MouseButton.SECONDARY) {
			zoom(1.0 - (Options.ZOOM_STEP_SMALL / 2.0));
		}
	}

	@FXML protected void btnZoomOutTouchPressed(TouchEvent e) {
		zoom(1.0 - (Options.ZOOM_STEP_BIG / 2.0));
	}

	@FXML protected void btnZoomOutTouchReleased(TouchEvent e) {
		zoom(1.0 - (Options.ZOOM_STEP_BIG / 2.0));
	}


	@FXML protected void btnFlipMouseClicked(MouseEvent e) {
		if (e.isSynthesized()) return;

		flip *= -1.0;
		drawImage();
	}

	@FXML protected void btnFlipTouchPressed(TouchEvent e) {
		flip *= -1.0;
		drawImage();
	}


	@FXML protected void btnMirrorMouseClicked(MouseEvent e) {
		if (e.isSynthesized()) return;

		mirror *= -1.0;
		drawImage();
	}

	@FXML protected void btnMirrorTouchPressed(TouchEvent e) {
		mirror *= -1.0;
		drawImage();
	}


	@FXML protected void btnSlidersMouseClicked(MouseEvent e) {
		boolean selected = btnSliders.getProperties().get("selected") == null ? false : (Boolean) btnSliders.getProperties().get("selected");
		if (selected) {
			btnSliders.setStyle("-fx-background-color: #aaaaaa");
			right.setCenter(null);
			btnSliders.getProperties().put("selected", false);
		} else {
			btnSliders.setStyle("-fx-background-color: #66aaff;");
			right.setCenter(gui.getNodeSliders());
			btnSliders.getProperties().put("selected", true);
		}
		drawImage();
	}

	@FXML protected void btnSlidersTouchPressed(TouchEvent e) { } //  sliders(); }


	@FXML protected void btnRefreshMouseClicked(MouseEvent e) { refresh(); }

	@FXML protected void btnRefreshTouchPressed(TouchEvent e) { } //  refresh(); }


	@FXML protected void btnBrightnessMouseClicked(MouseEvent e) {
		drawMode = (drawMode == DrawMode.BRIGHTNESS ? drawMode.NONE : DrawMode.BRIGHTNESS);
		fill();
	}

	@FXML protected void btnVertebraMouseClicked(MouseEvent e) {
		drawMode = (drawMode == DrawMode.VERTEBRA ? drawMode.NONE : DrawMode.VERTEBRA);
		fill();
	}


	// CANVAS

	@FXML protected void canvasMouseDragged(MouseEvent e) {
		if (e.isSynthesized()) return; // ignore Touch

		double currentx = e.getX();
		double currenty = e.getY();

		double offsetx = currentx - mousex;
		mousex = currentx;

		double offsety = currenty - mousey;
		mousey = currenty;

		if (mousePrimaryDown) {
			if (drawMode == DrawMode.BRIGHTNESS.BRIGHTNESS) {
				changeContrast(offsetx / 1000.0);
				changeBrightness(offsety / 1000.0);
			}
		} else if (mouseSecundaryDown) {
			translate(offsetx, offsety);
		}
	}

	@FXML protected void canvasMousePressed(MouseEvent e) {
		if (e.isSynthesized()) return; // ignore Touch

		mousex = e.getX();
		mousey = e.getY();

		if (e.getButton() == MouseButton.PRIMARY) {
			mousePrimaryDown = true;
		} else if (e.getButton() == MouseButton.SECONDARY) {
			mouseSecundaryDown = true;
		}
	}

	@FXML protected void canvasMouseReleased(MouseEvent e) {
		if (e.isSynthesized()) return; // ignore Touch

		mousex = 0.0;
		mousey = 0.0;

		mousePrimaryDown = false;
		mouseSecundaryDown = false;
	}

	@FXML protected void canvasTouchMoved(TouchEvent e) {
		TouchPoint p = getFirstTouchPoint(e); // get the first touched point
		if (p == null) return;

		double currentx = p.getX();
		double offsetx = currentx - touchx;
		touchx = currentx;

		double currenty = p.getY();
		double offsety = currenty - touchy;
		touchy = currenty;

		if (multitouch) {
			translate(offsetx, offsety);
		} else {
			if (drawMode == DrawMode.BRIGHTNESS) {
				changeContrast(offsetx / 1000.0);
				changeBrightness(offsety / 1000.0);
			}
		}
	}

	@FXML protected void canvasTouchPressed(TouchEvent e) {
		if (e.getTouchCount() == 1) { // only do this on press of the first point
			TouchPoint p = e.getTouchPoint();
			touchx = p.getX();
			touchy = p.getY();
		} else {
			multitouch = true;
		}
	}

	@FXML protected void canvasTouchReleased(TouchEvent e) {
		if (e.getTouchCount() == 1) { // only do this on release of the last touch point
			touchx = 0.0;
			touchy = 0.0;

			multitouch = false;
		}
	}

	@FXML protected void canvasRotate(RotateEvent e) {
		double totalAngle = e.getTotalAngle();
		totalAngle -= rotated;

		if (Math.abs(totalAngle) >= Options.ROTATION_STEP_SMALL) {
			double angle = totalAngle - (totalAngle % Options.ROTATION_STEP_SMALL);
			rotate(angle);
			rotated += angle;
		}
	}

	@FXML protected void canvasRotateFinished(RotateEvent e) {
		rotated = 0.0;
	}

	@FXML protected void canvasZoom(ZoomEvent e) {
		double totalZoom = e.getTotalZoomFactor();
		totalZoom /= zoomed;

		if (Math.abs(totalZoom - 1.0) > Options.ZOOM_STEP_SMALL) {
			zoom(totalZoom);
			drawImage();

			zoomed *= totalZoom;
		}
	}

	@FXML protected void canvasZoomFinished(ZoomEvent e) {
		zoomed = 1.0;
	}

	@FXML protected void canvasDragOver(DragEvent e) {
		if (e.getDragboard().hasFiles()) {
			e.acceptTransferModes(TransferMode.MOVE);
		}
		e.consume();
	}

	@FXML protected void canvasDragDropped(DragEvent e) {
		Dragboard db = e.getDragboard();
		if (db.hasFiles()) {
			String path = db.getFiles().get(0).getAbsolutePath();
			open(new File(path), null);
		}
		e.setDropCompleted(true);
		e.consume();
	}

	@FXML protected void canvasScroll(ScrollEvent e) {
		if (e.getTouchCount() != 0) return;

		double delta = e.getDeltaY();
		if (delta == -40.0) {
			zoom(1.0 - (Options.ZOOM_STEP_BIG));
		} else if (delta == +40.0) {
			zoom(1.0 + (Options.ZOOM_STEP_BIG));
		}
	}



	// GETTERS

	public double getRightWidth() {
		return right.widthProperty().doubleValue();
	}

	public double getMenuWidth() {
		return menu.widthProperty().doubleValue();
	}

	public double getBottomWidth() {
		return hbox.heightProperty().doubleValue();
	}

}
