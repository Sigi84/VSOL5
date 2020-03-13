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
import be.veterinarysolutions.vsol.tools.Cal;
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
import java.util.Collection;
import java.util.TreeSet;
import java.util.Vector;

public class Study extends Controller {
	@FXML private BorderPane viewerArea, canvasZone, controlZone;
	@FXML private Button btnSelect, btnBrightness, btnMeasure, btnImg, btnAdd, btnDog, btnCat;
	@FXML private VBox vboxMenus;
	@FXML private ScrollPane scrollPane;
	private Button btnAdd2 = new Button("Add");

	private PictureCanvas pictureCanvas = new PictureCanvas();
	private QuadrantsCanvas quadrantsCanvas = new QuadrantsCanvas(this);

	private boolean multitouch = false;
	private boolean mousePrimaryDown = false; // true when the primary mouse button is down
	private boolean mouseSecondaryDown = false; // secondary mouse button is down

	private double rotated = 0.0, zoomed = 0.0;
	private double touchx = 0.0, touchy = 0.0;
	private double mousex = 0.0, mousey = 0.0;
	private boolean mouseMoving = false, touchMoving = false, zooming = false, rotating = false;

	public enum Mode { SELECT, BRIGHTNESS, MEASURE };
	public enum Animal { CANINE, FELINE };

	private Mode mode = Mode.SELECT;
	private Animal animal = Animal.CANINE;

	private Vector<Menu> menus = new Vector<>();

	@Override
	public void init() {
		pictureCanvas.widthProperty().bind(canvasZone.widthProperty());
		pictureCanvas.heightProperty().bind(canvasZone.heightProperty());

		quadrantsCanvas.widthProperty().bind(canvasZone.widthProperty());
		quadrantsCanvas.heightProperty().bind(canvasZone.heightProperty());

		fillAnimal(Animal.CANINE);
		quadrantsCanvas.setInner(true);

		canvasZone.getChildren().add(pictureCanvas);
		canvasZone.getChildren().add(quadrantsCanvas);

//		btnAdd2.setText("Add");
//		btnAdd2.setOnMouseClicked(this::btnAddMouseClicked);
//		canvasZone.setCenter(btnAdd2);


		fillMode(Mode.SELECT);

		btnImg.setDisable(!ctrl.getImagen().isOpen());

		fillMenus();
	}





	// MENUS

	public void fillMenus() {
		pictureCanvas.clear();
		pictureCanvas.setMenu(null);

		vboxMenus.getChildren().clear();

		for (Menu menu : menus) {
			MenuComp comp = MenuComp.getInstance(ctrl, menu);
			vboxMenus.getChildren().add(comp.getNode());

			if (menu.isSelected())
				pictureCanvas.draw(menu);
		}

		fillQuadrants();
	}

	public void addMenu(Menu menu) {
		addMenu(menu, -1);
	}

	public void addMenu(Menu menu, int position) {
		if (position == -1)
			menus.add(menu);
		else {
			if (position > menus.size())
				position = menus.size();
			menus.add(position, menu);
		}
		quadrantsCanvas.selectAll(false);
	}

	public int getIndex(Menu menu) {
		int index = -1;

		int counter  = 0;
		for (Menu temp : menus) {
			if (temp.getId() == menu.getId()) {
				index = counter;
			}
			counter++;
		}

		return index;
	}

	public void reorderMenu(Menu menu, int pos) {

		Menu current = null;
		Vector<Menu> menusNew = new Vector<>();

//		int pos = 0;

		int counter  = 0;
		for (Menu temp : menus) {
			if (temp.getId() == menu.getId()) {
				current = temp;
//				pos = counter;
			} else {
				menusNew.add(temp);
			}
			counter++;
		}

//		pos += offset;
//		if (pos < 0)
//			pos = 0;
//		else if (pos > menusNew.size())
//			pos = menusNew.size();

		menusNew.add(pos, current);
		menus = menusNew;

		fillMenus();
	}

	public void selectMenu(Menu menu) {
		boolean selected = menu.isSelected();

		if (selected) {
			if (menu.isNext()) {
				menu.setSelected(false);
				menu.setNext(false);
				for (Tooth tooth : menu.getTeeth()) {
					quadrantsCanvas.getTooth(tooth).setSelected(false);
				}
			} else {
				setNextMenu(menu);
			}
		} else {
			if (menu.hasPic()) {
				deselectAllMenus();

				menu.setSelected(true);
				for (Tooth tooth : menu.getTeeth()) {
					quadrantsCanvas.getTooth(tooth).setSelected(true);
				}
			} else {
				setNextMenu(menu);
			}




		}

		fillMenus();
	}

	private void deselectAllMenus() {
		for (Menu temp : menus) {
			temp.setSelected(false);
		}
		for (Tooth tooth : quadrantsCanvas.getSelectedTeeth()) {
			tooth.setSelected(false);
		}
	}

	public void setNextMenu(int targetPos) {
		// mark the next menu in the queue (incl rollaround) to be the next picture receiver
		for (int i = 0; i < menus.size(); i++) {
			int j = (targetPos + i) % menus.size();
			Menu temp = menus.elementAt(j);
			if (!temp.hasPic()) {
				temp.setNext(true);
				break;
			}
		}
	}

	public void setNextMenu(Menu menu) {
		for (Menu temp : menus) {
			if (temp.getId() == menu.getId()) {
				menu.setNext(true);
			} else if (temp.isNext()) {
				temp.setNext(false);
			}
		}
	}

	public Menu getSelectedMenu() {
		Menu result = null;
		for (Menu menu : menus) {
			if (menu.isSelected()) {
				result = menu;
				break;
			}
		}
		return result;
	}

	public void deleteMenu(Menu menu) {
		if (menu == null) return;
		if (menu.hasPic() && !menu.isDeleted()) return;
		menu.selfDestruct();

		Vector<Menu> menusNew = new Vector<>();
		for (Menu temp : menus) {
			if (temp.getId() != menu.getId())
				menusNew.add(temp);
		}
		menus = menusNew;
	}

	public void cycleMenus(boolean up) {
		if (menus.size() == 0) return;

		int posNext = -1;
		int posSelected = -1;
		Menu firstUntaken = null;

		int counter = 0;
		for (Menu menu : menus) {
			if (firstUntaken == null && !menu.hasPic()) {
				firstUntaken = menu;
			}
			if (menu.isNext() && posNext == -1) {
				posNext = counter;
//				break;
			} else if (menu.isSelected() && posSelected == -1) {
				posSelected = counter;
			}
			counter++;
		}

		if (posNext == -1) { // there is no Next
			if (firstUntaken == null) { // there is no available Next (all pictures are taken)
				if (posSelected == -1) { // there is no Selected
					menus.firstElement().setSelected(true);
				} else {
					Menu current = menus.elementAt(posSelected);
					if (up) {
						posSelected = (posSelected + menus.size() - 1) % menus.size();
					} else {
						posSelected = (posSelected + 1) % menus.size();
					}
					Menu next = menus.elementAt(posSelected);

					current.setSelected(false);
					next.setSelected(true);
				}
			} else {
				firstUntaken.setNext(true);
			}
		} else {
			Menu current = menus.elementAt(posNext);
			if (up) {
				posNext = (posNext + menus.size() - 1) % menus.size();
			} else {
				posNext = (posNext + 1) % menus.size();
			}
			Menu next = menus.elementAt(posNext);

			current.setNext(false);
			next.setNext(true);
			if (current.isSelected()) {
				current.setSelected(false);
				next.setSelected(true);
			}
		}





		fillMenus();
	}




	// QUADRANTS CANVAS

	private void fillQuadrants() {
		quadrantsCanvas.draw();
		btnAdd.setDisable(getSelectedMenu() != null || quadrantsCanvas.getSelectedTeeth().size() == 0);

		btnAdd2.setVisible(quadrantsCanvas.isInner());
		btnAdd2.setDisable(btnAdd.isDisable());
	}





	// PICTURE CANVAS

	public void addPic(Picture pic) {
		for (Picture temp : pictureCanvas.getPics()) {
			temp.setSelected(false);
		}
		pic.setSelected(true);
		pictureCanvas.getPics().add(pic);
		pictureCanvas.draw();
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

	private TouchPoint getFirstTouchPoint(TouchEvent e) {
		for (TouchPoint p : e.getTouchPoints()) {
			if (p.getId() == 1) {
				return p;
			}
		}
		return null;
	}






	// EVENTS

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
			Picture pic = new Picture(Options.START_DIR + "dog.jpg");
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


	@FXML
	public void btnImgMouseClicked(MouseEvent e) {
		Menu targetMenu = null;
		int targetPos = -1;

		int counter = 0;
		for (Menu menu : menus) {
			menu.setSelected(false);
			if (menu.isNext()) {
				targetMenu = menu;
				targetPos = counter;
			}
			counter++;
		}

		if (targetMenu != null) {
			quadrantsCanvas.setInner(false);

			byte[] pixels = ctrl.getImagen().getMockCapture();

			double min = Bytes.MAX_16;
			double max = 0.0;

			int[] values = Bytes.get16BitBuffer(pixels, Bytes.LITTLE_ENDIAN);
			for (int value : values) {
				if (value < min) min = value;
				if (value > max) max = value;
			}

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

			if (targetMenu.hasPic()) {
				if (Options.AUTO_DELETE_ON_RETAKE)
					targetMenu.setDeleted(true);

				Menu copy = new Menu(targetMenu.getTeeth());
				copy.setPic(pic);
				copy.setSelected(true);

				int position = getIndex(targetMenu) + 1;
				addMenu(copy, position);
			} else {
				targetMenu.setPic(pic);
				targetMenu.setSelected(true);
			}

			targetMenu.setNext(false);
			setNextMenu(targetPos);
		}

		fillMenus();
	}


	@FXML protected void btnAddMouseClicked(MouseEvent e) {
		TreeSet<Tooth> selection = quadrantsCanvas.getSelectedTeeth();
		if (selection.size() == 0) return;

		Menu menu = new Menu(selection);

		boolean hasNext = false;
		for (Menu temp : menus) {
			if (temp.isNext()) {
				hasNext = true;
				break;
			}
		}

		if (!hasNext) {
			menu.setNext(true);
		}

		addMenu(menu);
		fillMenus();
	}

	@FXML protected void btnSaveMouseClicked(MouseEvent e) {
		Bmp.saveAsPng(pictureCanvas, Options.START_DIR + "snapshot");
	}





	@FXML protected void btnQuadrantMouseClicked(MouseEvent e) {
		if (quadrantsCanvas.isInner()) {
			quadrantsCanvas.setInner(false);
		} else {
			deselectAllMenus();
			fillMenus();
			quadrantsCanvas.setInner(true);
		}

		fillQuadrants();
	}


	@FXML protected void btnDogMouseClicked(MouseEvent e) {
		fillAnimal(Animal.CANINE);
		quadrantsCanvas.init(Animal.CANINE);
		fillQuadrants();
	}

	@FXML protected void btnCatMouseClicked(MouseEvent e) {
		fillAnimal(Animal.FELINE);
		quadrantsCanvas.init(Animal.FELINE);
		fillQuadrants();
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
				if (mousePrimaryDown) {
					Picture pic = pictureCanvas.getSelectedPic();
					if (pic != null) {
						pic.endLine(e.getX(), e.getY());
						pictureCanvas.draw();
					}
					break;
				}
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

		if (e.getButton() == MouseButton.PRIMARY && mode == Mode.MEASURE) {
			Picture pic = pictureCanvas.getSelectedPic();
			if (pic != null) {
				pic.startLine(e.getX(), e.getY());
				pictureCanvas.draw();
			}
		}
	}

	@FXML protected void canvasZoneMouseReleased(MouseEvent e) {
		if (e.isSynthesized()) return; // ignore Touch

		if (e.getButton() == MouseButton.PRIMARY && !mouseMoving && (mode == Mode.SELECT || mode == Mode.BRIGHTNESS)) {
			select(e.getX(), e.getY());
		}

		if (e.getButton() == MouseButton.SECONDARY && mode == Mode.MEASURE) {
			Picture pic = pictureCanvas.getSelectedPic();
			if (pic != null) {
				pic.cycleLineMode();
			}
		}

		mousex = 0.0;
		mousey = 0.0;
		mouseMoving = false;

		mousePrimaryDown = false;
		mouseSecondaryDown = false;

//		if (mode == Mode.MEASURE) {
//			Picture pic = pictureCanvas.getSelectedPic();
//			if (pic != null) {
//				pic.getLine1().setStartX(e.getX());
//				pic.getLine1().setStartY(e.getY());
//			}
//		}
	}

	@FXML protected void canvasZoneMouseClicked(MouseEvent e) {
		quadrantsCanvas.select(e.getX(), e.getY());
		fillQuadrants();

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
			} else if (mode == Mode.MEASURE) {
				Picture pic = pictureCanvas.getSelectedPic();
				if (pic != null) {
					pic.endLine(e.getTouchPoint().getX(), e.getTouchPoint().getY());
					pictureCanvas.draw();
				}
			}
		}
	}

	@FXML protected void canvasZoneTouchPressed(TouchEvent e) {
		if (e.getTouchCount() == 1) { // only do this on press of the first point
			TouchPoint p = e.getTouchPoint();
			touchx = p.getX();
			touchy = p.getY();

			if (mode == Mode.MEASURE) {
				Picture pic = pictureCanvas.getSelectedPic();
				if (pic != null) {
					pic.startLine(e.getTouchPoint().getX(), e.getTouchPoint().getY());
					pictureCanvas.draw();
				}
			}
		} else {
			multitouch = true;
		}
	}

	@FXML protected void canvasZoneTouchReleased(TouchEvent e) {
		if (e.getTouchCount() == 1) { // only do this on release of the last touch point
			if (!touchMoving && !rotating && !zooming  && (mode == Mode.SELECT || mode == Mode.BRIGHTNESS)) {
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


	@FXML protected void vboxMenusScroll(ScrollEvent e) {
//		System.out.println("vbox " + e.getTotalDeltaY());
//		e.consume();
	}


	@FXML protected void scrollPaneScroll(ScrollEvent e) {
		scrollPane.setVvalue(scrollPane.getVvalue() * 2.0);

		e.consume();
	}





	public void fillMode(Mode mode) {
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
	}

	private void fillAnimal(Animal animal) {
		this.animal = animal;
		final String SELECTED = "selected";
		btnDog.getStyleClass().removeAll(SELECTED);
		btnCat.getStyleClass().removeAll(SELECTED);

		switch (animal) {
			case CANINE:
				btnDog.getStyleClass().add(SELECTED);
				break;
			case FELINE:
				btnCat.getStyleClass().add(SELECTED);
				break;
		}
	}

	public void clearMenus() {
		for (Menu menu : menus) {
			menu.selfDestruct();
		}
		menus.clear();
		quadrantsCanvas.clear();
	}

	public void addRandomMenus() {
		long id = Cal.getId();
		int counter = 0;
		for (Tooth tooth : quadrantsCanvas.getTeeth()) {
			if (Math.random() < 0.25) {
				Menu menu = new Menu(id + counter++, tooth);
				addMenu(menu);
			}
		}
		fillMenus();
	}

	public QuadrantsCanvas getQuadrantsCanvas() {
		return quadrantsCanvas;
	}
}
