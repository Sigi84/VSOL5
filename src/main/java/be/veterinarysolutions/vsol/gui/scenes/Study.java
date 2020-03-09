package be.veterinarysolutions.vsol.gui.scenes;

import be.veterinarysolutions.vsol.data.Menu;
import be.veterinarysolutions.vsol.data.Picture;
import be.veterinarysolutions.vsol.data.Quadrant;
import be.veterinarysolutions.vsol.data.Tooth;
import be.veterinarysolutions.vsol.gui.Controller;
import be.veterinarysolutions.vsol.gui.MenuComp;
import be.veterinarysolutions.vsol.gui.canvases.PictureCanvas;
import be.veterinarysolutions.vsol.gui.canvases.QuadrantsCanvas;
import be.veterinarysolutions.vsol.main.Options;
import be.veterinarysolutions.vsol.tools.Bmp;
import be.veterinarysolutions.vsol.tools.Bytes;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.File;
import java.util.Vector;

public class Study extends Controller {
	@FXML private BorderPane viewerArea, canvasZone, controlZone;
	@FXML private Button btnSelect, btnBrightness, btnMeasure, btnImg, btnAdd;
	@FXML private VBox vboxMenus;
	@FXML private ScrollPane scrollMenus;

	private PictureCanvas pictureCanvas = new PictureCanvas();
	private QuadrantsCanvas quadrantsCanvas = new QuadrantsCanvas();

	private boolean multitouch = false;
	private boolean mousePrimaryDown = false; // true when the primary mouse button is down
	private boolean mouseSecondaryDown = false; // secondary mouse button is down

	private double rotated = 0.0, zoomed = 0.0;
	private double touchx = 0.0, touchy = 0.0;
	private double mousex = 0.0, mousey = 0.0;
	private boolean mouseMoving = false, touchMoving = false, zooming = false, rotating = false;

	public enum Mode { SELECT, BRIGHTNESS, MEASURE };
	private Mode mode = Mode.SELECT;

	private Vector<Menu> menus = new Vector<>();

	@Override
	public void init() {
		pictureCanvas.widthProperty().bind(canvasZone.widthProperty());
		pictureCanvas.heightProperty().bind(canvasZone.heightProperty());

		quadrantsCanvas.widthProperty().bind(canvasZone.widthProperty());
		quadrantsCanvas.heightProperty().bind(canvasZone.heightProperty());

		quadrantsCanvas.setInner(true);
		quadrantsCanvas.setScale(1.0);

		canvasZone.getChildren().add(pictureCanvas);
		canvasZone.getChildren().add(quadrantsCanvas);

		fillMode(Mode.SELECT);
		fillImagen();
		drawQuadrants();
	}




	private void addPic(Picture pic) {
		for (Picture temp : pictureCanvas.getPics()) {
			temp.setSelected(false);
		}
		pic.setSelected(true);
		pictureCanvas.getPics().add(pic);
		pictureCanvas.draw();
	}

	private void addMenu() {
		Menu menu = new Menu(quadrantsCanvas.getSelectedTeeth(), menus.size(), menus.size() == 0);
		menus.add(menu);
		quadrantsCanvas.selectAll(false);
		drawQuadrants();
		fillMenus();
//		Platform.runLater(() -> scrollMenus.setVvalue(1.0));
	}

	public void fillMenus() {
		vboxMenus.getChildren().clear();
		int counter = 0;
		for (Menu menu : menus) {
			MenuComp comp = MenuComp.getInstance(ctrl, menu);
			vboxMenus.getChildren().add(comp.getNode());
		}
	}

	public void deleteMenu(Menu menu) {
		Vector<Menu> menusNew = new Vector<>();
		int counter = 0;
		for (Menu temp : menus) {
			if (temp.getNr() != menu.getNr()) {
				menusNew.add(temp);
				temp.setNr(counter++);
			}
		}
		this.menus = menusNew;
		fillMenus();
	}

	public void readyMenu(Menu menu) {
		double scrollposition = scrollMenus.getVvalue();
		for (Menu temp : menus) {
			temp.setReady(temp.getNr() == menu.getNr());
		}

		fillMenus();
		Platform.runLater(() -> scrollMenus.setVvalue(scrollposition));

		for (Quadrant quadrant : quadrantsCanvas.getQuadrants()) {
			for (Tooth tooth : quadrant.getTeeth()) {
				tooth.setReady(false);
			}
		}

		for (Quadrant quadrant : quadrantsCanvas.getQuadrants()) {
			for (Tooth tooth : quadrant.getTeeth()) {
				for (Tooth temp : menu.getTeeth()) {
					if (tooth.getName().equals(temp.getName())) {
						tooth.setReady(true);
					}
				}
			}
		}

		drawQuadrants();
	}








	private void changeBrightness(double offset) {
		for (Picture pic : pictureCanvas.getPics()) {
			if (pic.isSelected()) {
				pic.setBrightness(pic.getBrightness() + offset);
			}
		}

		pictureCanvas.draw();
	}

	private void changeContrast(double offset) {
		for (Picture pic : pictureCanvas.getPics()) {
			if (pic.isSelected()) {
				pic.setContrast(pic.getContrast() + offset);
			}
		}

		pictureCanvas.draw();
	}

	private void translate(double x, double y) {
		for (Picture pic : pictureCanvas.getPics()) {
			if (pic.isSelected()) {
				pic.setTransx(pic.getTransx() + x);
				pic.setTransy(pic.getTransy() + y);
			}
		}

		pictureCanvas.draw();
	}

	private void addRotation(double angle) {
		for (Picture pic : pictureCanvas.getPics()) {
			if (pic.isSelected()) {
				pic.setRotation(pic.getRotation() + angle);
			}
		}

		pictureCanvas.draw();
	}

	private void addZoom(double zoom) {
		for (Picture pic : pictureCanvas.getPics()) {
			if (pic.isSelected()) {
				pic.setZoom(pic.getZoom() + zoom);
			}
		}

		pictureCanvas.draw();
	}

	private void flip() {
		for (Picture pic : pictureCanvas.getPics()) {
			if (pic.isSelected()) {
				pic.setFlip(-pic.getFlip());
			}
		}

		pictureCanvas.draw();
	}

	private void mirror() {
		for (Picture pic : pictureCanvas.getPics()) {
			if (pic.isSelected()) {
				pic.setMirror(-pic.getMirror());
			}
		}

		pictureCanvas.draw();
	}

	private void reset() {
		pictureCanvas.getPics().clear();
		pictureCanvas.draw();
	}

	// TODO: move this function over to PictureCanvas (= more logical and consistent with Q-Canvas.select())
	private void select(double x, double y) {
		boolean found = false;
		for (Picture pic : pictureCanvas.getPics()) {
			if (pic.getRect().contains(x, y)) {
				pic.setSelected(!pic.isSelected());
				found = true;
			}
		}

		if (!found) {
			for (Picture pic : pictureCanvas.getPics()) {
				pic.setSelected(false);
			}
		}

		pictureCanvas.draw();
	}

	public void selectAll() {
		for (Picture pic : pictureCanvas.getPics()) {
			pic.setSelected(true);
		}

		pictureCanvas.draw();
	}

	public void deleteSelection() {
		pictureCanvas.deleteSelection();
		pictureCanvas.draw();
	}

	private void drawQuadrants() {
		quadrantsCanvas.draw();
		boolean selection = false;

		outerloop:
		for (Quadrant quadrant : quadrantsCanvas.getQuadrants()) {
			for (Tooth tooth : quadrant.getTeeth()) {
				if (tooth.isSelected()) {
					selection = true;
					break outerloop;
				}
			}
		}

		btnAdd.setDisable(!selection);
	}





	@FXML protected void btnBackMouseClicked(MouseEvent e) {
		gui.showHome();
	}

	@FXML protected void btnControlsMouseClicked(MouseEvent e) {
		if (viewerArea.getRight() == null) {
			viewerArea.setRight(controlZone);
		} else {
			viewerArea.setRight(null);
		}
	}

	@FXML protected void btnOpenMouseClicked(MouseEvent e) {
		if (e.getButton() == MouseButton.PRIMARY) {
			Picture pic = Picture.chooseFile(gui);
			if (pic == null) return;
			addPic(pic);
		} else {
			Picture pic = new Picture(Options.START_DIR + "1.jpg");
			addPic(pic);
		}
	}


	@FXML protected void btnResetMouseClicked(MouseEvent e) {
		if (e.isSynthesized()) return;
		reset();
	}

	@FXML protected void btnResetTouchPressed(TouchEvent e) {
		reset();
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


	@FXML protected void btnSelectMouseClicked(MouseEvent e) {
		if (e.isSynthesized()) return;
		fillMode(Mode.SELECT);
	}

	@FXML protected void btnSelectTouchPressed(TouchEvent e) {
		fillMode(Mode.SELECT);
	}


	@FXML protected void btnBrightnessMouseClicked(MouseEvent e) {
		if (e.isSynthesized()) return;
		fillMode(Mode.BRIGHTNESS);
	}

	@FXML protected void btnBrightnessTouchPressed(TouchEvent e) {
		fillMode(Mode.BRIGHTNESS);
	}


	@FXML protected void btnMeasureMouseClicked(MouseEvent e) {
		if (e.isSynthesized()) return;
		fillMode(Mode.MEASURE);
	}

	@FXML protected void btnMeasureTouchPressed(TouchEvent e) {
		fillMode(Mode.MEASURE);
	}


	@FXML protected void btnImgMouseClicked(MouseEvent e) {
		byte[] pixels = ctrl.getImagen().getMockCapture();

		double min = Bytes.MAX_16;
		double max = 0.0;

		int[] values = Bytes.get16BitBuffer(pixels, Bytes.LITTLE_ENDIAN);
		for (int value : values) {
			if (value < min) min = value;
			if (value > max) max = value;
		}

//		System.out.println("MIN = " + min);
//		System.out.println("MAX = " + max);

		int[] rgb = new int[values.length];
		for (int i = 0; i < rgb.length; i++) {
			double fraction = (values[i] - min) / (max - min);
			int current = (int) ( fraction * 255 );

			Color color = new Color(current, current, current);
			rgb[i] = color.getRGB();
		}

		WritableImage img = new WritableImage(1300, 1706);
		PixelWriter writer = img.getPixelWriter();
		writer.setPixels(0, 0, 1300, 1706, PixelFormat.getIntArgbInstance(), rgb, 0, 1300);

		Picture pic = new Picture(img);

		addPic(pic);
	}


	@FXML protected void btnAddMouseClicked(MouseEvent e) {
		addMenu();
	}


	@FXML protected void btnSaveMouseClicked(MouseEvent e) {
		Bmp.saveAsPng(pictureCanvas, Options.START_DIR + "snapshot");
	}


	@FXML protected void btnQuadrantMouseClicked(MouseEvent e) {
		if (quadrantsCanvas.isInner()) {
			quadrantsCanvas.setInner(false);
			quadrantsCanvas.setScale(0.75);
		} else {
			quadrantsCanvas.setInner(true);
			quadrantsCanvas.setScale(1.0);
		}

		drawQuadrants();
	}


	@FXML protected void canvasZoneMouseDragged(MouseEvent e) {
		if (e.isSynthesized()) return; // ignore Touch

		mouseMoving = true;

		double currentx = e.getX();
		double currenty = e.getY();

		double offsetx = currentx - mousex;
		mousex = currentx;

		double offsety = currenty - mousey;
		mousey = currenty;

		switch (mode) {
			case SELECT:
				translate(offsetx, offsety);
				break;
			case BRIGHTNESS:
				if (mousePrimaryDown) {
					if (mode == Mode.BRIGHTNESS) {
						changeContrast(offsetx / 1000.0);
						changeBrightness(offsety / 1000.0);
					}
				} else if (mouseSecondaryDown) {
					translate(offsetx, offsety);
				}
				break;
			case MEASURE:
				break;
		}


	}

	@FXML protected void canvasZoneMousePressed(MouseEvent e) {
		if (e.isSynthesized()) return; // ignore Touch

		mousex = e.getX();
		mousey = e.getY();

		if (e.getButton() == MouseButton.PRIMARY) {
			mousePrimaryDown = true;
		} else if (e.getButton() == MouseButton.SECONDARY) {
			mouseSecondaryDown = true;
		}
	}

	@FXML protected void canvasZoneMouseReleased(MouseEvent e) {
		if (e.isSynthesized()) return; // ignore Touch

		if (e.getButton() == MouseButton.PRIMARY && !mouseMoving && (mode == Mode.SELECT || mode == Mode.BRIGHTNESS)) {
			select(e.getX(), e.getY());
		}

		mousex = 0.0;
		mousey = 0.0;
		mouseMoving = false;

		mousePrimaryDown = false;
		mouseSecondaryDown = false;
	}

	@FXML protected void canvasZoneMouseClicked(MouseEvent e) {
		quadrantsCanvas.select(e.getX(), e.getY());
		drawQuadrants();

//		if (e.getButton() == MouseButton.MIDDLE) {
//			System.out.println();
//		} else {
//			quadrantsCanvas.testSelect(e.getButton() == MouseButton.PRIMARY, e.getX(), e.getY());
//		}
	}

	@FXML protected void canvasZoneTouchMoved(TouchEvent e) {
		TouchPoint p = getFirstTouchPoint(e); // get the first touched point
		if (p == null) return;

		double currentx = p.getX();
		double offsetx = currentx - touchx;
		touchx = currentx;

		double currenty = p.getY();
		double offsety = currenty - touchy;
		touchy = currenty;

		if (offsetx > 1.0 || offsety > 1.0) // ignore very small touch movings to avoid selecting by accident
			touchMoving = true;

		if (multitouch) {
			translate(offsetx, offsety);
		} else {
			if (mode == Mode.BRIGHTNESS) {
				changeContrast(offsetx / 1000.0);
				changeBrightness(offsety / 1000.0);
			}
		}
	}

	@FXML protected void canvasZoneTouchPressed(TouchEvent e) {
		if (e.getTouchCount() == 1) { // only do this on press of the first point
			TouchPoint p = e.getTouchPoint();
			touchx = p.getX();
			touchy = p.getY();
		} else {
			multitouch = true;
		}
	}

	@FXML protected void canvasZoneTouchReleased(TouchEvent e) {
		if (e.getTouchCount() == 1) { // only do this on release of the last touch point
			if (!touchMoving && !rotating && !zooming) {
				select(touchx, touchy);
			}

			touchMoving = false;
			zooming = false;
			rotating = false;

			touchx = 0.0;
			touchy = 0.0;

			multitouch = false;
		}
	}

	@FXML protected void canvasZoneRotate(RotateEvent e) {
		rotating = true;
		double angle = e.getTotalAngle() - rotated;
		double step = Options.ROTATION_STEP_SMALL;
		double totalAngle = angle - (angle % step) ;
		rotated += totalAngle;
		addRotation(totalAngle);
	}

	@FXML protected void canvasZoneRotationFinished(RotateEvent e) {
		rotated = 0.0;
	}

	@FXML protected void canvasZoneZoom(ZoomEvent e) {
		zooming = true;
		double zoom = e.getTotalZoomFactor() - 1.0 - zoomed;
		double step = Options.ZOOM_STEP_SMALL;
		double totalZoom = zoom - (zoom % step);
		zoomed += totalZoom;
		addZoom(totalZoom);
	}

	@FXML protected void canvasZoneZoomFinished(ZoomEvent e) {
		zoomed = 0.0;
	}

	@FXML protected void canvasZoneDragOver(DragEvent e) {
		if (e.getDragboard().hasFiles()) {
			e.acceptTransferModes(TransferMode.MOVE);
		}
		e.consume();
	}

	@FXML protected void canvasZoneDragDropped(DragEvent e) {
		Dragboard db = e.getDragboard();
		for (File file : db.getFiles()) {
			addPic(new Picture(file));
		}

		e.setDropCompleted(true);
		e.consume();
	}

	@FXML protected void canvasZoneScroll(ScrollEvent e) {
		if (e.getTouchCount() != 0) return;

		double delta = e.getDeltaY();
		if (delta == -40.0 || delta == -100.0) {
			addZoom(-Options.ZOOM_STEP_SMALL);
		} else if (delta == +40.0 || delta == +100.0) {
			addZoom(+Options.ZOOM_STEP_SMALL);
		}
	}



	
	private TouchPoint getFirstTouchPoint(TouchEvent e) {
		for (TouchPoint p : e.getTouchPoints()) {
			if (p.getId() == 1) {
				return p;
			}
		}
		return null;
	}

	private void fillMode(Mode mode) {
		this.mode = mode;
		final String SELECTED = "selected";
		btnSelect.getStyleClass().removeAll(SELECTED);
		btnBrightness.getStyleClass().removeAll(SELECTED);
		btnMeasure.getStyleClass().removeAll(SELECTED);

		switch (mode) {
			case SELECT:
				btnSelect.getStyleClass().add(SELECTED);
				break;
			case BRIGHTNESS:
				btnBrightness.getStyleClass().add(SELECTED);
				break;
			case MEASURE:
				btnMeasure.getStyleClass().add(SELECTED);
				break;
		}


		//		btnBrightness.getStyleClass().removeAll("selected");
//		btnVertebra.getStyleClass().removeAll("selected");
//
//		switch (drawMode) {
//			case BRIGHTNESS:
//				btnBrightness.getStyleClass().add("selected");
//				break;
//			case VERTEBRA:
//				btnVertebra.getStyleClass().add("selected");
//				break;
//		}
	}

	private void fillImagen() {
		final String ACTIVE = "active";

		btnImg.getStyleClass().removeAll(ACTIVE);

		if (ctrl.getImagen().isOpen()) {
			btnImg.getStyleClass().add(ACTIVE);
		}
	}

}
