package be.veterinarysolutions.vsol.gui;

import be.veterinarysolutions.vsol.data.Picture;
import be.veterinarysolutions.vsol.interfaces.Pollable;
import be.veterinarysolutions.vsol.main.Options;
import be.veterinarysolutions.vsol.tools.WatchDir;
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
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

public class Viewer extends Controller implements Pollable {
	private Logger logger = LogManager.getLogger();

	@FXML private Canvas canvas;
	@FXML private BorderPane bg, right;
	@FXML private VBox menu;
	@FXML private HBox hbox;
	@FXML private Button btnSliders, btnBrightness, btnVertebra;
	@FXML private Label lblInfo1, lblInfo2;
	@FXML private StackPane stackPane;

	private boolean multitouch = false;
	private boolean mousePrimaryDown = false; // true when the primary mouse button is down
	private boolean mouseSecondaryDown = false; // secondary mouse button is down

	private Vector<Picture> pics = new Vector<>();

	private double rotated = 0.0, zoomed = 0.0;
	private double touchx = 0.0, touchy = 0.0;
	private double mousex = 0.0, mousey = 0.0;

	public static enum DrawMode { NONE, BRIGHTNESS, VERTEBRA };
	private DrawMode drawMode = DrawMode.NONE;

	// PRIVATE

	public void addPic(Picture pic) {
		selectAll(false);
		pic.setSelected(true);
		pics.add(pic);
		drawImages();
	}

	private void drawImages() {
		if (pics.isEmpty()) {
			clearCanvas();
			return;
		}
		fillSliders();

		GraphicsContext gg = canvas.getGraphicsContext2D();
		clearCanvas();
		resizeCanvas();

		for (Picture pic : pics) {
			Image img = pic.getImg();
			if (img == null) return;

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

			pic.setRect(x, y, iw, ih);

			gg.save();

			// effect
			ColorAdjust ca = new ColorAdjust();
			ca.setBrightness(pic.getBrightness());
			ca.setContrast(pic.getContrast());

			gg.setEffect(ca);

			// translation
			gg.translate(pic.getTransx(), pic.getTransy());

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

			if (pic.isSelected()) {
				gg.setGlobalAlpha(1.0);
			} else {
				gg.setGlobalAlpha(0.3);
			}

			gg.drawImage(img, x, y, iw, ih);

			if (pic.isSelected()) {
				gg.setLineWidth(5.0);
				gg.setStroke(Color.RED);
				gg.strokeRect(x, y, iw, ih);
			}

			gg.restore();
		}
	}

	private void drawImage() {

	}

	private void drawDrawing() {

	}

	private void resizeCanvas() {
		boolean isSliders = btnSliders.getProperties().get("selected") == null ? false : (Boolean) btnSliders.getProperties().get("selected");
		double sliderWidth = isSliders ? gui.getSliders().getWidth() : 0.0;

		double width = gui.getFrame().getBgWidth() - gui.getFrame().getMenuWidth() - getMenuWidth() - sliderWidth;
		double height = gui.getFrame().getBgHeight() - getBottomWidth();

		canvas.setWidth(width);
		canvas.setHeight(height);
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

	private void fillSliders() {
//		gui.getSliders().getBtnUndo().setDisable(imgIndex == -1);
//		gui.getSliders().getBtnRedo().setDisable(imgIndex >= convertedImgs.size() - 1);
	}

	private void rotateMagick(double angle) {
//		logger.debug("rotate magick: " + angle);
//		File file = basicFile;
//		if (file == null) return;
//
//		ConvertCmd cmd = new ConvertCmd(true);
//
//		IMOperation operation = new IMOperation();
//		operation.rotate(angle);
//
//		imgIndex++;
//		operation.addImage(file.getAbsolutePath(), Options.START_DIR + file.getName() + "_" + imgIndex + ".tmp");
//
//		try {
//			cmd.run(operation);
//		} catch (IOException | InterruptedException | IM4JavaException e) {
//			logger.error(e);
//		}
	}

	private void addRotation(double angle) {
		for (Picture pic : pics) {
			if (pic.isSelected()) {
				pic.setRotation(pic.getRotation() + angle);
			}
		}

		drawImages();
	}

	private void addZoom(double zoom) {
		for (Picture pic : pics) {
			if (pic.isSelected()) {
				pic.setZoom(pic.getZoom() + zoom);
			}
		}

		drawImages();
	}

	private void translate(double x, double y) {
		for (Picture pic : pics) {
			if (pic.isSelected()) {
				pic.setTransx(pic.getTransx() + x);
				pic.setTransy(pic.getTransy() + y);
			}
		}

		drawImages();
	}

	private void flip() {
		for (Picture pic : pics) {
			if (pic.isSelected()) {
				pic.setFlip(-pic.getFlip());
			}
		}

		drawImages();
	}

	private void mirror() {
		for (Picture pic : pics) {
			if (pic.isSelected()) {
				pic.setMirror(-pic.getMirror());
			}
		}

		drawImages();
	}

	private void changeBrightness(double offset) {
		for (Picture pic : pics) {
			if (pic.isSelected()) {
				pic.setBrightness(pic.getBrightness() + offset);
			}
		}

		drawImages();
	}

	private void changeContrast(double offset) {
		for (Picture pic : pics) {
			if (pic.isSelected()) {
				pic.setContrast(pic.getContrast() + offset);
			}
		}

		drawImages();
	}

	private void refresh() {
		pics.clear();
		clearCanvas();
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

	private void selectAll(boolean selected) {
		for (Picture pic : pics) {
			pic.setSelected(selected);
		}
	}

	// PUBLIC

	public void resize() {
//		drawImages();
	}

	public void histogramOptimization(double value) {
		rotateMagick(value);
	}

	public void selectNext() {
		int counter = 0, index = 0;
		for (Picture pic : pics) {
			if (pic.isSelected()) {
				index = counter;
			}

			pic.setSelected(false);
			counter++;
		}
		index = (index + 1) % pics.size();

		counter = 0;
		for (Picture pic : pics) {
			if (counter == index) {
				pic.setSelected(true);
				break;
			}
			counter++;
		}

		drawImages();
	}

	public void selectPrevious() {
		int counter = 0, index = 0;
		for (Picture pic : pics) {
			if (pic.isSelected()) {
				index = counter;
			}

			pic.setSelected(false);
			counter++;
		}
		index = (index - 1 + pics.size()) % pics.size();

		counter = 0;
		for (Picture pic : pics) {
			if (counter == index) {
				pic.setSelected(true);
				break;
			}
			counter++;
		}

		drawImages();
	}

	public void selectAll() {
		System.out.println("select all");
		for (Picture pic : pics) {
			pic.setSelected(true);
		}

		drawImages();
	}

	public void selectNone() {
		for (Picture pic : pics) {
			pic.setSelected(false);
		}
		drawImages();
	}

	public void delete() {
		Vector<Picture> picsNew = new Vector<>();
		for (Picture pic : pics) {
			if (!pic.isSelected()) {
				picsNew.add(pic);
			}
		}
		pics = picsNew;
		drawImages();
	}

	@Override
	public void fileModified(Path path) {
		File file = new File(path.toString());
		if (file == null) return;

		Picture pic = new Picture(file);
		if (pic == null) return;

		addPic(pic);
	}

	public void undo() {

	}

	public void redo() {

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
		canvas = new Canvas();
		stackPane.getChildren().add(canvas);
		lblInfo1.setText("");
		lblInfo2.setText("");

		Path dir = Paths.get(Options.START_DIR);

//		try {
//			new WatchDir(dir, false, this).processEvents();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	// BUTTONS

	@FXML protected void btnOpenMouseClicked(MouseEvent e) {
		if (e.getButton() == MouseButton.PRIMARY) {
			Picture pic = Picture.chooseFile(gui);
//			Picture pic = Picture.jpro();
			if (pic == null) return;
			addPic(pic);
		} else {
			Picture pic = new Picture(Options.START_DIR + "1.jpg");
			addPic(pic);
		}
	}


	@FXML protected void btnRotateMousePressed(MouseEvent e) {
		if (e.isSynthesized()) return; // ignore Touch

		if (e.getButton() == MouseButton.PRIMARY) {
			addRotation(+Options.ROTATION_STEP_BIG / 2.0);
		} else if (e.getButton() == MouseButton.SECONDARY) {
			addRotation(+Options.ROTATION_STEP_SMALL / 2.0);
		}
	}

	@FXML protected void btnRotateMouseReleased(MouseEvent e) {
		if (e.isSynthesized()) return; // ignore Touch

		if (e.getButton() == MouseButton.PRIMARY) {
			addRotation(+Options.ROTATION_STEP_BIG / 2.0);
		} else if (e.getButton() == MouseButton.SECONDARY) {
			addRotation(+Options.ROTATION_STEP_SMALL / 2.0);
		}
	}

	@FXML protected void btnRotateTouchPressed(TouchEvent e) {
		addRotation(+Options.ROTATION_STEP_BIG / 2.0);
	}

	@FXML protected void btnRotateTouchReleased(TouchEvent e) {
		addRotation(+Options.ROTATION_STEP_BIG / 2.0);
	}

	@FXML protected void btnCounterMousePressed(MouseEvent e) {
		if (e.isSynthesized()) return; // ignore Touch

		if (e.getButton() == MouseButton.PRIMARY) {
			addRotation(-Options.ROTATION_STEP_BIG / 2.0);
		} else if (e.getButton() == MouseButton.SECONDARY) {
			addRotation(-Options.ROTATION_STEP_SMALL / 2.0);
		}
	}

	@FXML protected void btnCounterMouseReleased(MouseEvent e) {
		if (e.isSynthesized()) return; // ignore Touch

		if (e.getButton() == MouseButton.PRIMARY) {
			addRotation(-Options.ROTATION_STEP_BIG / 2.0);
		} else if (e.getButton() == MouseButton.SECONDARY) {
			addRotation(-Options.ROTATION_STEP_SMALL / 2.0);
		}
	}

	@FXML protected void btnCounterTouchPressed(TouchEvent e) {
		addRotation(-Options.ROTATION_STEP_BIG / 2.0);
	}

	@FXML protected void btnCounterTouchReleased(TouchEvent e) {
		addRotation(-Options.ROTATION_STEP_BIG / 2.0);
	}


	@FXML protected void btnZoomInMousePressed(MouseEvent e) {
		if (e.isSynthesized()) return; // ignore Touch

		if (e.getButton() == MouseButton.PRIMARY) {
			addZoom(Options.ZOOM_STEP_BIG / 2.0);
		} else if (e.getButton() == MouseButton.SECONDARY) {
			addZoom(Options.ZOOM_STEP_SMALL / 2.0);
		}
	}

	@FXML protected void btnZoomInMouseReleased(MouseEvent e) {
		if (e.isSynthesized()) return; // ignore Touch

		if (e.getButton() == MouseButton.PRIMARY) {
			addZoom(Options.ZOOM_STEP_BIG / 2.0);
		} else if (e.getButton() == MouseButton.SECONDARY) {
			addZoom(Options.ZOOM_STEP_SMALL / 2.0);
		}
	}

	@FXML protected void btnZoomInTouchPressed(TouchEvent e) {
		addZoom(Options.ZOOM_STEP_BIG / 2.0);
	}

	@FXML protected void btnZoomInTouchReleased(TouchEvent e) {
		addZoom(Options.ZOOM_STEP_BIG / 2.0);
	}


	@FXML protected void btnZoomOutMousePressed(MouseEvent e) {
		if (e.isSynthesized()) return; // ignore Touch

		if (e.getButton() == MouseButton.PRIMARY) {
			addZoom(-Options.ZOOM_STEP_BIG / 2.0);
		} else if (e.getButton() == MouseButton.SECONDARY) {
			addZoom(-Options.ZOOM_STEP_SMALL / 2.0);
		}
	}

	@FXML protected void btnZoomOutMouseReleased(MouseEvent e) {
		if (e.isSynthesized()) return; // ignore Touch

		if (e.getButton() == MouseButton.PRIMARY) {
			addZoom(-Options.ZOOM_STEP_BIG / 2.0);
		} else if (e.getButton() == MouseButton.SECONDARY) {
			addZoom(-Options.ZOOM_STEP_SMALL / 2.0);
		}
	}

	@FXML protected void btnZoomOutTouchPressed(TouchEvent e) {
		addZoom(-Options.ZOOM_STEP_BIG / 2.0);
	}

	@FXML protected void btnZoomOutTouchReleased(TouchEvent e) {
		addZoom(-Options.ZOOM_STEP_BIG / 2.0);
	}


	@FXML protected void btnFlipMouseClicked(MouseEvent e) {
		if (e.isSynthesized()) return;

		flip();
	}

	@FXML protected void btnFlipTouchPressed(TouchEvent e) {
		flip();
	}


	@FXML protected void btnMirrorMouseClicked(MouseEvent e) {
		if (e.isSynthesized()) return;

		mirror();
	}

	@FXML protected void btnMirrorTouchPressed(TouchEvent e) {
		mirror();
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
		drawImages();
	}

	@FXML protected void btnSlidersTouchPressed(TouchEvent e) { } //  sliders(); }


	@FXML protected void btnRefreshMouseClicked(MouseEvent e) { refresh(); }

	@FXML protected void btnRefreshTouchPressed(TouchEvent e) { } //  refresh(); }


	@FXML protected void btnBrightnessMouseClicked(MouseEvent e) {
		drawMode = (drawMode == DrawMode.BRIGHTNESS ? DrawMode.NONE : DrawMode.BRIGHTNESS);
		fill();
	}

	@FXML protected void btnVertebraMouseClicked(MouseEvent e) {
		drawMode = (drawMode == DrawMode.VERTEBRA ? DrawMode.NONE : DrawMode.VERTEBRA);
		fill();
	}


	// PANE

	@FXML protected void stackPaneMouseDragged(MouseEvent e) {
		if (e.isSynthesized()) return; // ignore Touch

		double currentx = e.getX();
		double currenty = e.getY();

		double offsetx = currentx - mousex;
		mousex = currentx;

		double offsety = currenty - mousey;
		mousey = currenty;

		if (mousePrimaryDown) {
			if (drawMode == DrawMode.BRIGHTNESS) {
				changeContrast(offsetx / 1000.0);
				changeBrightness(offsety / 1000.0);
			}
		} else if (mouseSecondaryDown) {
			translate(offsetx, offsety);
		}
	}

	@FXML protected void stackPaneMousePressed(MouseEvent e) {
		if (e.isSynthesized()) return; // ignore Touch

		mousex = e.getX();
		mousey = e.getY();

		if (e.getButton() == MouseButton.PRIMARY) {
			mousePrimaryDown = true;
		} else if (e.getButton() == MouseButton.SECONDARY) {
			mouseSecondaryDown = true;
		}
	}

	@FXML protected void stackPaneMouseClicked(MouseEvent e) {

	}

	@FXML protected void stackPaneMouseReleased(MouseEvent e) {
		if (e.isSynthesized()) return; // ignore Touch

		mousex = 0.0;
		mousey = 0.0;

		mousePrimaryDown = false;
		mouseSecondaryDown = false;
	}

	@FXML protected void stackpaneTouchMoved(TouchEvent e) {
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

	@FXML protected void stackpaneTouchPressed(TouchEvent e) {
		if (e.getTouchCount() == 1) { // only do this on press of the first point
			TouchPoint p = e.getTouchPoint();
			touchx = p.getX();
			touchy = p.getY();
		} else {
			multitouch = true;
		}
	}

	@FXML protected void stackpaneTouchReleased(TouchEvent e) {
		if (e.getTouchCount() == 1) { // only do this on release of the last touch point
			touchx = 0.0;
			touchy = 0.0;

			multitouch = false;
		}
	}

	@FXML protected void stackpaneRotate(RotateEvent e) {
		double angle = e.getTotalAngle() - rotated;
		double step = Options.ROTATION_STEP_SMALL;
		double totalAngle = angle - (angle % step) ;
		rotated += totalAngle;
		addRotation(totalAngle);
	}

	@FXML protected void stackpaneRotateFinished(RotateEvent e) {
		rotated = 0.0;
	}

	@FXML protected void stackpaneZoom(ZoomEvent e) {
		double zoom = e.getTotalZoomFactor() - 1.0 - zoomed;
		double step = Options.ZOOM_STEP_SMALL;
		double totalZoom = zoom - (zoom % step);
		zoomed += totalZoom;
		addZoom(totalZoom);
	}

	@FXML protected void stackpaneZoomFinished(ZoomEvent e) {
		zoomed = 0.0;
	}

	@FXML protected void stackpaneDragOver(DragEvent e) {
		if (e.getDragboard().hasFiles()) {
			e.acceptTransferModes(TransferMode.MOVE);
		}
		e.consume();
	}

	@FXML protected void stackpaneDragDropped(DragEvent e) {
		Dragboard db = e.getDragboard();
		for (File file : db.getFiles()) {
			addPic(new Picture(file));
		}

		e.setDropCompleted(true);
		e.consume();
	}

	@FXML protected void stackpaneScroll(ScrollEvent e) {
		if (e.getTouchCount() != 0) return;

		double delta = e.getDeltaY();
		if (delta == -40.0 || delta == -100.0) {
			addZoom(-Options.ZOOM_STEP_BIG);
		} else if (delta == +40.0 || delta == +100.0) {
			addZoom(+Options.ZOOM_STEP_BIG);
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
