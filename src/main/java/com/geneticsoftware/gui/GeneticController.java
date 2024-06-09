package com.geneticsoftware.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.geneticsoftware.geneticalgorithm.FitnessFunction;
import com.geneticsoftware.Main;
import com.geneticsoftware.geneticalgorithm.GeneticAlgorithm;
import com.geneticsoftware.geneticalgorithm.crossover.Crossover;
import com.geneticsoftware.geneticalgorithm.crossover.CrossoverTwoPoint;
import com.geneticsoftware.geneticalgorithm.mutation.Mutation;
import com.geneticsoftware.geneticalgorithm.mutation.MutationEveryBitInvertion;
import com.geneticsoftware.geneticalgorithm.selection.Selection;
import com.geneticsoftware.geneticalgorithm.selection.SelectionTournament;
import com.geneticsoftware.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.script.ScriptException;

public class GeneticController {

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
    private ChoiceBox<String> chromosomeSelector;

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
    private TextField countChromosomesTextField;

    @FXML
    private LineChart<String, Double> resultChart;

    @FXML
    private Label resultLabel;

    @FXML
    private Button solutionButton;

    @FXML
    private Button cleanButton;

    @FXML
    private ChoiceBox<String> mutationSelector;

    @FXML
    private ChoiceBox<String> selectionSelector;

    @FXML
    private ChoiceBox<String> crossoverSelector;

    @FXML
    private Button backButton;

    private Scene scene;
    private Stage stage;
    private Parent root;


    //@FXML
    public void initialize(String function, Integer countArguments, String[] min, String[] max) {

        ObservableList<String> chromosome = FXCollections.observableArrayList(
                "Бинарное кодирование",
                "Вещественные числа"
        );
        chromosomeSelector.setItems(chromosome); // this statement adds all values in choiceBox
        chromosomeSelector.setValue("Бинарное кодирование"); // this statement shows default value

        chromosomeSelector.setOnAction(this::setChromosome);

        ObservableList<String> options = FXCollections.observableArrayList("Min", "Max");
        findMinSelector.setItems(options); // this statement adds all values in choiceBox
        findMinSelector.setValue("Min"); // this statement shows default value



        resultChart.getStylesheets().add(Main.class.getResource("lineChart.css").toExternalForm());

        //!!!!!
        resultChart.getData().clear();

        cleanButton.setOnAction(event -> {
            resultChart.getData().clear();
        });

        solutionButton.setOnAction(event -> {

            resultLabel.setText("Результат\n\n");

            int populationSize = Integer.parseInt(populationSizeTextField.getText());
            int countChromosomes = Integer.parseInt(countChromosomesTextField.getText());

//            double accuracy = Double.parseDouble(accuracyTextField.getText());
            double accuracy = 0.000000000000000000000000001;

            double crossoverRate = Double.parseDouble(crossoverRateTextField.getText());
            boolean useMutation = true;
            double mutationRate = Double.parseDouble(mutationRateTextField.getText());
            int maxIterations = Integer.parseInt(maxIterationsTextField.getText());
            int maxIterationsWithoutImprovement = Integer.parseInt(maxIterationsWithoutImprovementTextField.getText());

            boolean findMin = findMinSelector.getValue() == "Min";

            SelectionType selectionType = SelectionType.TOURNAMENT;
            switch (selectionSelector.getValue()) {
                case "Колесо рулетки" -> selectionType = SelectionType.ROULETTEWHEEL;
                case "Ранжированный отбор" -> selectionType = SelectionType.RANK;
                case "Турнирный отбор" -> selectionType = SelectionType.TOURNAMENT;
                case "Отбор усечением" -> selectionType = SelectionType.TRUNCATION;
            }

            CrossoverType crossoverType = CrossoverType.TWOPOINT;
            switch (crossoverSelector.getValue()) {
                case "Одноточечное скрещивание" -> crossoverType = CrossoverType.ONEPOINT;
                case "Двухточечное скрещивание" -> crossoverType = CrossoverType.TWOPOINT;
                case "k-точечное скрещивание" -> crossoverType = CrossoverType.KPOINT;
                case "Равномерное скрещивание" -> crossoverType = CrossoverType.UNIFORM;
                case "Промежуточная рекомбинация" -> crossoverType = CrossoverType.INTERMEDIATE;
                case "Линейная рекомбинация" -> crossoverType = CrossoverType.LINE;
                case "Имитация двоичного скрещивания" -> crossoverType = CrossoverType.SBX;
            }

            MutationType mutationType = MutationType.ONEBITINVERTION;
            switch (mutationSelector.getValue()) {
                case "Инвертирование одного бита" -> mutationType = MutationType.ONEBITINVERTION;
                case "Инвертирование нескольких битов" -> mutationType = MutationType.EVERYBITINVERTION;
                case "Обращение цепочки генов" -> mutationType = MutationType.PARTINVERTATION;
                case "Случайное изменение цепочки генов" -> mutationType = MutationType.PARTRANDOM;
                case "Случайное изменение гена" -> mutationType = MutationType.ONEREAL;
            }



            ChromosomeType chromosomeType = ChromosomeType.BINARY;
            switch (chromosomeSelector.getValue()) {
                case "Бинарное кодирование" -> chromosomeType = ChromosomeType.BINARY;
                case "Вещественные числа" -> chromosomeType = ChromosomeType.REAL;
            }



            boolean elitist = false; //!!!!!!!!!!!!!

            int arguments = countArguments;
//            double minArgument = Double.parseDouble(min);
//            double maxArgument = Double.parseDouble(max);

            double[] minArgument = new double[min.length];
            double[] maxArgument = new double[max.length];
            for (int i = 0; i < min.length; i++){
                minArgument[i] = Double.parseDouble(min[i]);
                maxArgument[i] = Double.parseDouble(max[i]);
            }

            FitnessFunction fitnessFunction = new FitnessFunction(function, arguments, minArgument, maxArgument, findMin);

            GeneticAlgorithm geneticAlgorithm = null;

            double alpha = 0;
            int k = 4;
            int size = 4;

            try {
                geneticAlgorithm = new GeneticAlgorithm(
                        populationSize,
                        countChromosomes,
                        accuracy,
                        crossoverRate,
                        mutationRate,
                        maxIterations,
                        maxIterationsWithoutImprovement,
                        findMin,
                        chromosomeType,
                        selectionType,
                        crossoverType,
                        mutationType,
                        elitist,
                        fitnessFunction,

                        size,
                        alpha,
                        k
                );
            } catch (ScriptException e) {
                throw new RuntimeException(e);
            }

            Pair<String, XYChart.Series<String, Double>> result = null;
            try {
                result = geneticAlgorithm.startAlgorithm();
            } catch (ScriptException e) {
                throw new RuntimeException(e);
            }

            resultChart.setLegendVisible(false);
            resultLabel.setText("Результат:\n" + result.getFirst());
            resultChart.getData().add(result.getSecond());

        });

        backButton.setOnAction(this::functionWindow);

    }

    public void functionWindow(ActionEvent event) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/geneticsoftware/Function.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FunctionController functionController = loader.getController();
        functionController.initialize();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setX(550);
        stage.setY(210);
        scene = new Scene(root);
        stage.setTitle("Genetic algorithm");
        stage.setScene(scene);
        stage.show();
    }

    public void setChromosome(ActionEvent event) {
        if(chromosomeSelector.getValue() == "Бинарное кодирование") {
            ObservableList<String> selections = FXCollections.observableArrayList(
                    "Колесо рулетки",
                    "Ранжированный отбор",
                    "Турнирный отбор",
                    "Отбор усечением"
            );
            selectionSelector.setItems(selections); // this statement adds all values in choiceBox
            selectionSelector.setValue("Турнирный отбор"); // this statement shows default value

            ObservableList<String> crossovers = FXCollections.observableArrayList(
                    "Одноточечное скрещивание",
                    "Двухточечное скрещивание",
                    "k-точечное скрещивание",
                    "Равномерное скрещивание"
            );
            crossoverSelector.setItems(crossovers); // this statement adds all values in choiceBox
            crossoverSelector.setValue("Двухточечное скрещивание"); // this statement shows default value

            ObservableList<String> mutations = FXCollections.observableArrayList(
                    "Инвертирование одного бита",
                    "Инвертирование нескольких битов",
                    "Обращение цепочки генов",
                    "Случайное изменение цепочки генов"
            );
            mutationSelector.setItems(mutations); // this statement adds all values in choiceBox
            mutationSelector.setValue("Инвертирование одного бита"); // this statement shows default value

        }
        else {

            ObservableList<String> selections = FXCollections.observableArrayList(
                    "Колесо рулетки",
                    "Ранжированный отбор",
                    "Турнирный отбор",
                    "Отбор усечением"
            );
            selectionSelector.setItems(selections); // this statement adds all values in choiceBox
            selectionSelector.setValue("Турнирный отбор"); // this statement shows default value

            ObservableList<String> crossovers = FXCollections.observableArrayList(
                    "Промежуточная рекомбинация",
                    "Линейная рекомбинация",
                    "Имитация двоичного скрещивания"
            );
            crossoverSelector.setItems(crossovers); // this statement adds all values in choiceBox
            crossoverSelector.setValue("Промежуточная рекомбинация"); // this statement shows default value

            ObservableList<String> mutations = FXCollections.observableArrayList(

                    "Случайное изменение гена"
            );
            mutationSelector.setItems(mutations); // this statement adds all values in choiceBox
            mutationSelector.setValue("Случайное изменение гена"); // this statement shows default value

        }
    }
}




