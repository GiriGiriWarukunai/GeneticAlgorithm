package com.geneticsoftware;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Function.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setX(550);
        primaryStage.setY(210);
        primaryStage.setTitle("Genetic algorithm");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(final String[] args) {
        launch();
    }
}
