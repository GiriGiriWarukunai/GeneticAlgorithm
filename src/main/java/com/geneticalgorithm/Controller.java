package com.geneticalgorithm;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller  {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField accuracyTextField;

    @FXML
    private TextField crossoverRateTextField;

    @FXML
    private ChoiceBox<String> findMinSelector;



    @FXML
    private NumberAxis fitness;

    @FXML
    private CategoryAxis iterations;

    @FXML
    private TextField maxIterationsTextField;

    @FXML
    private TextField maxIterationsWithoutImprovementTextField;

    @FXML
    private TextField mutationRateTextField;

    @FXML
    private TextField populationSizeTextField;

    @FXML
    private LineChart<String, Double> resultChart;

    @FXML
    private Label resultLabel;

    @FXML
    private Button solutionButton;

    @FXML
    void initialize() {


        ObservableList<String> options = FXCollections.observableArrayList("Min","Max");
        findMinSelector.setItems(options); // this statement adds all values in choiceBox
        findMinSelector.setValue("Min"); // this statement shows default value

        resultChart.getStylesheets().add(Main.class.getResource("lineChart.css").toExternalForm());

        solutionButton.setOnAction(event -> {

            resultChart.getData().clear();

            resultLabel.setText("Результат\n\n");

            int populationSize = Integer.parseInt(populationSizeTextField.getText());
            int countChromosomes = 1;

            double accuracy = Double.parseDouble(accuracyTextField.getText());
            double crossoverRate = Double.parseDouble(crossoverRateTextField.getText());
            boolean useMutation = true;
            double mutationRate = Double.parseDouble(mutationRateTextField.getText());
            int maxIterations = Integer.parseInt(maxIterationsTextField.getText());
            int maxIterationsWithoutImprovement = Integer.parseInt(maxIterationsWithoutImprovementTextField.getText());

            boolean findMin = findMinSelector.getValue() == "Min";

            FitnessFunction fitnessFunction = new FitnessFunction();


            GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(
                    populationSize,
                    countChromosomes,
                    accuracy,
                    crossoverRate,
                    useMutation,
                    mutationRate,
                    maxIterations,
                    maxIterationsWithoutImprovement,
                    findMin,
                    fitnessFunction
                    //, resultChart
            );

            Pair<String, XYChart.Series<String, Double>> result = geneticAlgorithm.startAlgorithm();

            resultLabel.setText("Результат\n\n" + result.getFirst());
            resultChart.getData().add(result.getSecond());


        });



    }

}




