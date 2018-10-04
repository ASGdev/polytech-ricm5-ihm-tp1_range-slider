package main_test;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import RangeSlider.*;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });


        Slider simpleslider = new Slider();
        simpleslider.setMin(0);
        simpleslider.setMax(10);
        simpleslider.setValue(5.);
        simpleslider.setShowTickLabels(true);
        simpleslider.setShowTickMarks(true);
        simpleslider.setMajorTickUnit(5);
        simpleslider.setMinorTickCount(5);
        simpleslider.setBlockIncrement(1);
        simpleslider.setSnapToTicks(true);

        RangeSlider rangeslider = new RangeSlider(0, 10, 5, 7, 0);
        rangeslider.setShowTickLabels(true);
        rangeslider.setShowTickLabels(true);
        rangeslider.setShowTickMarks(true);
        rangeslider.setMajorTickUnit(5);
        rangeslider.setMinorTickCount(5);
        rangeslider.setBlockIncrement(1);
        rangeslider.setSnapToTicks(false);

        VBox root = new VBox();
        root.getChildren().add(btn);
        root.getChildren().add(simpleslider);
        root.getChildren().add(rangeslider);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
