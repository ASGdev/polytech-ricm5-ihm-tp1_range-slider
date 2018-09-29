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
	// ===== ATTRIBUTS
	// =============================================
	private int canvas_h = 500;
	private int canvas_w = 500;

	private SplitPane root;
	private Canvas canvas;
	private BorderPane right_pane;

	// =============================================
	// ===== METHODES
	// =============================================
	public static void main(String[] args) {
		launch(args);
	}

    @Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle(" Another wonderful application by the Maitres Squaleurs :D");
		// Contains all the components of the gui
		root =  new SplitPane();
		
		// Where we draw the houses
		canvas = new Canvas(1280, 720);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawShapes(gc);
        
        // Where we add the buttons
        right_pane = new BorderPane();
		right_pane.setStyle("-fx-background-color: #383c4a;");
        
        // Add the 2 layers
        root.getItems().add(canvas);
        root.getItems().add(right_pane);
        
        // Let the show begin
        primaryStage.setScene(new Scene(root, 1920, 1080));
		primaryStage.show();
	}
    
    private void drawShapes(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);
        gc.strokeLine(40, 10, 10, 40);
        gc.fillOval(10, 60, 30, 30);
        gc.strokeOval(60, 60, 30, 30);
        gc.fillRoundRect(110, 60, 30, 30, 10, 10);
        gc.strokeRoundRect(160, 60, 30, 30, 10, 10);
        gc.fillPolygon(new double[]{10, 40, 10, 40},
                       new double[]{210, 210, 240, 240}, 4);
        gc.strokePolygon(new double[]{60, 90, 60, 90},
                         new double[]{210, 210, 240, 240}, 4);
        gc.strokePolyline(new double[]{110, 140, 110, 140},
                          new double[]{210, 210, 240, 240}, 4);
    }
}

/*
 * Button btn = new Button(); btn.setText("Say 'Hello World'");
 * btn.setOnAction(new EventHandler<ActionEvent>() {
 * 
 * @Override public void handle(ActionEvent event) {
 * System.out.println("Hello World!"); } });
 */