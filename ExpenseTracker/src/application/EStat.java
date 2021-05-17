package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.*;
import javafx.scene.Group;
 
public class EStat extends Application {
	
	
 
    @Override public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Distribution of Expenses");
        stage.setWidth(500);
        stage.setHeight(500);
 //"Home Expenses", "Entertainment/Eating out", "Gifts/Donations"
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                new PieChart.Data("Home Expenses", EController.a),
                new PieChart.Data("Entertainment/Eating out", EController.b),
                new PieChart.Data("Gifts/Donations", EController.c));
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Distribution");

        ((Group) scene.getRoot()).getChildren().add(chart);
        stage.setScene(scene);
        stage.show();
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}