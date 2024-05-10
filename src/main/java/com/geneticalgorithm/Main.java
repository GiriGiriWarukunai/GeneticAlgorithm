package com.geneticalgorithm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.stage.Stage;


//public class HelloApplication extends Application {
//    @Override
//    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public static void main(String[] args) {
//        launch();
//    }
//}


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("UI.fxml"));


//        Button button = new Button("UwU");
//        Text text = new Text(10, 20, "test uwU");
//        text.setFont(new Font(30));

//        BorderPane pane = new BorderPane();
//        pane.setCenter(button);
//        pane.setTop(text);

        Scene scene = new Scene(fxmlLoader.load());

        primaryStage.setTitle("Genetic algorithm");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(final String[] args) {


//        int populationSize = 100;
//        int countChromosomes = 1;
//
//        double accuracy = 0.0000000000000001;
//        double crossoverRate = 0.9;
//        boolean useMutation = true;
//        double mutationRate = 0.3;
//        int maxIterations = 100;
//        int maxIterationsWithoutImprovement = 30000;
//
//        boolean findMin = true;
//        FitnessFunction fitnessFunction = new FitnessFunction();
//
//
//
//        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(
//                populationSize,
//                countChromosomes,
//                accuracy,
//                crossoverRate,
//                useMutation,
//                mutationRate,
//                maxIterations,
//                maxIterationsWithoutImprovement,
//                findMin,
//                fitnessFunction
//        );

       // geneticAlgorithm.startAlgorithm();

        launch();
    }
}
