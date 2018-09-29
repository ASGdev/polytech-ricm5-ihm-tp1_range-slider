package dynamic_queries;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;

public class UserInterface extends Application {

	// =============================================
	// ===== PARAMETERS OF THE APPLICATION
	// =============================================

	// Homes
	private Home tab_homes[];

	// =============================================
	// ===== ATTRIBUTS
	// =============================================

	// Size of the canvas
	// Used to calculate position
	private int canvas_h = 1000;
	private int canvas_w = 1000;

	private SplitPane root;
	private BorderPane right_pane;
	private StackPane left_pane;
	private Canvas canvas;
	private GraphicsContext gc;

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

		// Add the 2 components to the base
		root.getItems().add(left_pane);
		root.getItems().add(right_pane);

		// Let the show begin
		primaryStage.setTitle(" Another wonderful application by the Maitres Squaleurs :D");
		primaryStage.setScene(new Scene(root, 1920, 1080));
		primaryStage.show();
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
				// FIXME error handling
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
		gc.setLineWidth(2);

		for (Home home : tab_homes) {
			x = home.getCoord_x();
			y = home.getCoord_y();
			rooms = home.getNb_room();
			value = home.getValue();

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