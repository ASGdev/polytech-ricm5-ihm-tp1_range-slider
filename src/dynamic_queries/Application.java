package dynamic_queries;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class Application {

	// ============================================= 
	// =====  PARAMETERS OF THE APPLICATION
	// =============================================

	// Coordinates
	static public float min_x = -100;
	static public float min_y = -100;
	static public float max_x = 100;
	static public float max_y = 100;

	// Price
	static public float max_price = 1000000;
	static public float min_price = 1;

	// Homes
	static public int nb_homes = 100;
	
	// ============================================= 
	// =====  MAIN
	// =============================================
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Contains all the components of the GUI
		container = new BorderPane();

		// First layer
		splitP = new SplitPane();
		container.setCenter(splitP);

		// Will contain canvas FIXME
		left_pane = new BorderPane();
		left_pane.setStyle("-fx-background-color: #c0c5ce;");

		// Will contain several range slider FIXME
		right_pane = new BorderPane();
		right_pane.setStyle("-fx-background-color: #383c4a;");

		// The visual reporter of the home market
		canvas = new Canvas();
	    canvas.widthProperty().bind(left_pane.widthProperty());
	    canvas.heightProperty().bind(left_pane.heightProperty());

		// Change some settings of the canvas
	    GraphicsContext gc = canvas.getGraphicsContext2D();
	    double w = canvas.getWidth();
        double h = canvas.getHeight();

        gc.clearRect(0, 0, w, h);
		gc.setFill(Color.RED);
		gc.strokeRoundRect(10, 10, 50, 50, 10, 10);

		left_pane.getChildren().add(canvas);
		splitP.getItems().addAll(left_pane, right_pane);

		// Force the position of the separator
		//left_pane.maxWidthProperty().bind(splitP.widthProperty().multiply(0.7));
		//left_pane.minWidthProperty().bind(splitP.widthProperty().multiply(0.7));

		
		// Let the show begin
		main_scene = new Scene(container, 1280, 720);
		primaryStage.setScene(main_scene);

	}

}
