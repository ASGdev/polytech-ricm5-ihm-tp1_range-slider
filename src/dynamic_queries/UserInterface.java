package dynamic_queries;

import RangeSlider.RangeSlider;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import parameters.Application_variables;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;

public class UserInterface extends Application {

	// =============================================
	// ===== ATTRIBUTS
	// =============================================
	// Homes
	private Home tab_homes[];

	// The value we filter on
	private int min_rooms;
	private int max_rooms;
	private int min_price;
	private int max_price;

	// Size of the canvas
	// Used to calculate position
	private int canvas_h = 1000;
	private int canvas_w = 1000;

	// Basic container
	private SplitPane root;

	// Left and right container
	private BorderPane right_pane;
	private StackPane left_pane;

	// The canvas on the left
	private Canvas canvas;
	private GraphicsContext gc;

	// An other container for the right
	private VBox right_vbox;

	// Containter of the title button
	private BorderPane title_pane;
	private Button title;

	// Container of the two buttons reset and quit
	private BorderPane reset_and_quit;
	private Button reset;
	private Button quit;
	private DropShadow shadow;

	// The RangeSlider
	private RangeSlider price_slider;
	private RangeSlider room_slider;

	// Container
	private BorderPane price_bp;
	private BorderPane room_bp;

	// Title
	private Button price_b;
	private Button room_b;
	// =============================================
	// ===== METHODES
	// =============================================

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		// Initiate the homes
		initiateHouses();

		// Base value for out filters
		min_rooms = Application_variables.min_rooms;
		max_rooms = Application_variables.max_rooms;
		min_price = Application_variables.min_price;
		max_price = Application_variables.max_price;

		// Contains all the components of the GUI
		root = new SplitPane();

		// Where we add the buttons
		right_pane = new BorderPane();
		right_pane.setStyle("-fx-background-color: #383c4a;");

		// Where we keep the canvas
		left_pane = new StackPane();
		left_pane.setStyle("-fx-background-color: #c0c5ce;");

		// Where we draw the houses
		canvas = new Canvas(canvas_w, canvas_h);
		gc = canvas.getGraphicsContext2D();
		drawHomes(gc);

		// Add the canvas to the left_pane
		left_pane.getChildren().add(canvas);

		// The right discending button list
		right_vbox = new VBox();
		right_pane.setCenter(right_vbox);

		// Initiate and add the title
		title_pane = new BorderPane();
		title = new Button("HOUSE PICKER");
		title.setStyle("-fx-background-color: #c0c5ce; -fx-font-size:35");
		title.maxWidth(Double.MAX_VALUE);
		title.setDisable(true);
		title_pane.setCenter(title);
		BorderPane.setMargin(title, new Insets(20));

		// Reset and quit
		reset_and_quit = new BorderPane();
		shadow = new DropShadow();

		reset = new Button("RESET");
		reset.setStyle("-fx-background-color: #c0c5ce; -fx-font-size:30");
		reset.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Reset");
				reset();
			}
		});

		// Adding the shadow when the mouse cursor is on
		reset.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				reset.setEffect(shadow);
			}
		});
		// Removing the shadow when the mouse cursor is off
		reset.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				reset.setEffect(null);
			}
		});
		quit = new Button("EXIT");
		quit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Platform.exit()");
				Platform.exit();
			}
		});
		quit.setStyle("-fx-background-color: #c0c5ce; -fx-font-size:30");
		// Adding the shadow when the mouse cursor is on
		quit.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				quit.setEffect(shadow);
			}
		});
		// Removing the shadow when the mouse cursor is off
		quit.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				quit.setEffect(null);
			}
		});

		reset_and_quit.setLeft(reset);
		BorderPane.setMargin(reset, new Insets(20));
		reset_and_quit.setRight(quit);
		BorderPane.setMargin(quit, new Insets(20));

		// RANGESLIDER part

		// Initiate and add the title
		room_bp = new BorderPane();
		room_b = new Button("ROOMS");
		room_b.setStyle("-fx-background-color: #c0c5ce; -fx-font-size:30");
		room_b.setDisable(true);
		//BorderPane.setMargin(room_b, new Insets(20));

		// Initiate and add the title
		price_bp = new BorderPane();
		price_b = new Button("PRICES");
		price_b.setStyle("-fx-background-color: #c0c5ce; -fx-font-size:30");
		price_b.setDisable(true);
		//BorderPane.setMargin(price_b, new Insets(20));

		// room_slider = new RangeSlider();
		// price_slider = new RangeSlider();
		room_slider = new RangeSlider(min_rooms, max_rooms, min_rooms, max_rooms, 10);
		room_slider.setShowTickLabels(true);
		room_slider.setShowTickMarks(true);
		room_slider.setBlockIncrement(1);
		room_slider.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				min_rooms = (int) room_slider.getInfValue();
				max_rooms = (int) room_slider.getSupValue();
				drawHomes(gc);
			}
		});

		price_slider = new RangeSlider(min_price, max_price, min_price, max_price, 10);
		price_slider.setShowTickLabels(true);
		price_slider.setShowTickMarks(true);
		// price_slider.setBlockIncrement(1);
		price_slider.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				min_price = (int) price_slider.getInfValue();
				max_price = (int) price_slider.getSupValue();
				drawHomes(gc);
			}
		});
		
		room_bp.setLeft(room_b);
		BorderPane.setMargin(room_b, new Insets(20));
		room_bp.setCenter(room_slider);
		BorderPane.setMargin(room_slider, new Insets(20));
		
		price_bp.setLeft(price_b);
		BorderPane.setMargin(price_b, new Insets(20));
		price_bp.setCenter(price_slider);
		BorderPane.setMargin(price_slider, new Insets(20));

		// Add the several component to the right container
		right_vbox.getChildren().add(title_pane);
		right_vbox.getChildren().add(reset_and_quit);
		right_vbox.getChildren().add(room_bp);
		right_vbox.getChildren().add(price_bp);

		// Add the 2 components to the base
		root.getItems().add(left_pane);
		root.getItems().add(right_pane);

		// Let the show begin
		primaryStage.setTitle(" Another wonderful application by the Maitres Squaleurs :D");
		primaryStage.setScene(new Scene(root, 1920, 1080));
		primaryStage.show();
	}

	// TODO -> now, the behavior isn't what we want it to be
	private void reset() {
		// min_rooms +=1;

		min_rooms = Application_variables.min_rooms;
		max_rooms = Application_variables.max_rooms;
		min_price = Application_variables.min_price;
		max_price = Application_variables.max_price;
		drawHomes(gc);
	}

	// Handle the initalization of the nb_homes houses
	private void initiateHouses() {
		tab_homes = new Home[100];
		for (int i = 0; i < Application_variables.nb_homes; i++) {
			// Randoms values
			double x = Application_variables.min_x
					+ (Math.random() * (Application_variables.max_x - Application_variables.min_x));
			double y = Application_variables.min_y
					+ (Math.random() * (Application_variables.max_y - Application_variables.min_y));
			double price = Application_variables.min_price
					+ (Math.random() * (Application_variables.max_price - Application_variables.min_price));
			double rooms = Application_variables.min_rooms
					+ (Math.random() * (Application_variables.max_rooms - Application_variables.min_rooms));
			try {
				tab_homes[i] = new Home(x, y, rooms, price);
			} catch (HomeException e) {
				i--;
				e.printStackTrace();
			}
		}
	}

	// Handle the drawing of all the houses on the GC of the canvas
	private void drawHomes(GraphicsContext gc) {
		int x;
		int y;
		int rooms;
		int value;
		Paint p;
		gc.clearRect(0, 0, canvas_w, canvas_h);
		gc.setLineWidth(2);

		for (Home home : tab_homes) {
			x = home.getCoord_x();
			y = home.getCoord_y();
			rooms = home.getNb_room();
			value = home.getValue();

			if (rooms < min_rooms || rooms > max_rooms || value < min_price || value > max_price)
				continue;
			p = translatePrice(value);
			gc.setFill(p);
			gc.setStroke(p);
			gc.fillOval(x, y, Application_variables.graph_size_home, Application_variables.graph_size_home);
		}
	}

	// Get the value and return an appropriate color
	// (from green to red)
	private Paint translatePrice(int p) {
		if (p < Application_variables.max_price * 0.20)
			return Color.BLUE;
		else if (p < Application_variables.max_price * 0.40)
			return Color.GREEN;
		else if (p < Application_variables.max_price * 0.60)
			return Color.YELLOW;
		else if (p < Application_variables.max_price * 0.80)
			return Color.ORANGE;
		else if (p < Application_variables.max_price)
			return Color.RED;
		else
			return Color.BLACK;
	}
}