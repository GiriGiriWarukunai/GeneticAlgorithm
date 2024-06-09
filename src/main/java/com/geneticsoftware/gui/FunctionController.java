package com.geneticsoftware.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.geneticsoftware.geneticalgorithm.FitnessFunction;
import com.geneticsoftware.geneticalgorithm.GeneticAlgorithm;
import com.geneticsoftware.util.CrossoverType;
import com.geneticsoftware.util.MutationType;
import com.geneticsoftware.util.Pair;
import com.geneticsoftware.util.SelectionType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.script.ScriptException;

public class FunctionController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<String> functionPresetChoiceBox;

    @FXML
    private Spinner<Integer> argumentsSpinner;

    @FXML
    private ChoiceBox<String> argumentsChoiceBox;

    @FXML
    private TextField maxTextField;

    @FXML
    private TextField minTextField;

    @FXML
    private TextArea functionTextField;

    @FXML
    private Button functionButton;

    private String userFunction;
    private ArrayList<String> userMax;
    private ArrayList<String> userMin;

    private String[] finalMin, finalMax;
    private Integer userCountArguments;
    private boolean isNoneBefore;

    private Scene scene;
    private Stage stage;
    private Parent root;

    private SpinnerValueFactory<Integer> valueFactory;
    ObservableList<String> arguments;
    String lastArg;


    @FXML
    void initialize() {

        String userFunction = "";
        userMax = new ArrayList<String>();
        userMin = new ArrayList<String>();
        finalMax = new String[1];
        finalMin = new String[1];

        userMax.add("");
        userMin.add("");
        lastArg = "x1";

        arguments = FXCollections.observableArrayList("x1");
        argumentsChoiceBox.setItems(arguments); // this statement adds all values in choiceBox
        argumentsChoiceBox.setValue("x1"); // this statement shows default value
//        minTextField.setText("");
//        maxTextField.setText("");


        ObservableList<String> presets = FXCollections.observableArrayList(
                "<не выбрано>",
                "Функция Растригина",
                "Функция Била",
                "Функция Гольдшейна-Прайса",
                "Функция \"подставка для яиц\" (Eggholder function)"
        );
        functionPresetChoiceBox.setItems(presets); // this statement adds all values in choiceBox
        functionPresetChoiceBox.setValue("<не выбрано>"); // this statement shows default value

        valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE);
        valueFactory.setValue(1);
        argumentsSpinner.setValueFactory(valueFactory);

        argumentsSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                if(functionPresetChoiceBox.getValue() == "<не выбрано>" /*&& argumentsSpinner.getValue() != 0*/) {
                    int currentCount = argumentsSpinner.getValue();

                    if (currentCount > userMax.size()) {
                        while (userMax.size() < currentCount) {
                            userMax.add("");
                            userMin.add("");
                        }
                    }

                    if(isNoneBefore) {
                        if (minTextField.getText() != null) {
                            userMin.set(Integer.parseInt(argumentsChoiceBox.getValue().replace("x", "")) - 1, minTextField.getText());
                        } else {
                            userMin.set(Integer.parseInt(argumentsChoiceBox.getValue().replace("x", "")) - 1, "");
                        }
                        if (maxTextField.getText() != null) {
                            userMax.set(Integer.parseInt(argumentsChoiceBox.getValue().replace("x", "")) - 1, maxTextField.getText());
                        } else {
                            userMax.set(Integer.parseInt(argumentsChoiceBox.getValue().replace("x", "")) - 1, "");
                        }
                    }

                    if(isNoneBefore) {
                        if (currentCount > arguments.size()) {
                            String tmp = argumentsChoiceBox.getValue();
                            arguments.clear();
                            for (int i = 0; i < currentCount; i++) {
                                arguments.add("x" + (i + 1));
                            }
                            argumentsChoiceBox.setValue(tmp);
                            minTextField.setText(userMin.get(Integer.parseInt(tmp.replace("x", "")) - 1));
                            maxTextField.setText(userMax.get(Integer.parseInt(tmp.replace("x", "")) - 1));
                        } else {
                            if (currentCount > Integer.parseInt(argumentsChoiceBox.getValue().replace("x", ""))) {
                                String tmp = argumentsChoiceBox.getValue();
                                arguments.clear();
                                for (int i = 0; i < currentCount; i++) {
                                    arguments.add("x" + (i + 1));
                                }
                                argumentsChoiceBox.setValue(tmp);
                                minTextField.setText(userMin.get(Integer.parseInt(tmp.replace("x", "")) - 1));
                                maxTextField.setText(userMax.get(Integer.parseInt(tmp.replace("x", "")) - 1));
                            } else {
                                arguments.clear();
                                for (int i = 0; i < currentCount; i++) {
                                    arguments.add("x" + (i + 1));
                                }
                                argumentsChoiceBox.setValue("x" + currentCount);
                                minTextField.setText(userMin.get(currentCount - 1));
                                maxTextField.setText(userMax.get(currentCount - 1));
                            }
                        }
                        lastArg = argumentsChoiceBox.getValue();
                        argumentsChoiceBox.setItems(arguments);
                    }
                    else{
                        arguments.clear();
                        for (int i = 0; i < currentCount; i++) {
                            arguments.add("x" + (i + 1));
                        }
                        argumentsChoiceBox.setValue("x1");
                        minTextField.setText(userMin.get(0));
                        maxTextField.setText(userMax.get(0));
                        lastArg = argumentsChoiceBox.getValue();
                        argumentsChoiceBox.setItems(arguments);
                    }
                }
                else{

                    arguments.clear();
                    for (int i = 0; i < argumentsSpinner.getValue(); i++) {
                        arguments.add("x" + (i + 1));
                    }
                    argumentsChoiceBox.setValue("x1");
                    minTextField.setText(finalMin[0]);
                    maxTextField.setText(finalMax[0]);
                    argumentsChoiceBox.setItems(arguments);
                }
            }
        });

        argumentsChoiceBox.setOnAction(this::setArgument);

        isNoneBefore = true;
        functionPresetChoiceBox.setOnAction(this::setPreset);
        functionButton.setOnAction(this::geneticWindow);
    }

    public void setArgument(ActionEvent event){
        if(functionPresetChoiceBox.getValue() == "<не выбрано>" /*&& argumentsSpinner.getValue() != 0 */) {

            if(isNoneBefore) {
                if (minTextField.getText() != null) {
                    userMin.set(Integer.parseInt(lastArg.replace("x", "")) - 1, minTextField.getText());
                } else {
                    userMin.set(Integer.parseInt(lastArg.replace("x", "")) - 1, "");
                }
                if (maxTextField.getText() != null) {
                    userMax.set(Integer.parseInt(lastArg.replace("x", "")) - 1, maxTextField.getText());
                } else {
                    userMax.set(Integer.parseInt(lastArg.replace("x", "")) - 1, "");
                }
            }
            if (argumentsChoiceBox.getValue() != null) {
                minTextField.setText(userMin.get(Integer.parseInt(argumentsChoiceBox.getValue().replace("x", "")) - 1));
                maxTextField.setText(userMax.get(Integer.parseInt(argumentsChoiceBox.getValue().replace("x", "")) - 1));
                lastArg = argumentsChoiceBox.getValue();
            }
        }
        else {
            if (argumentsChoiceBox.getValue() != null) {
                minTextField.setText(finalMin[Integer.parseInt(argumentsChoiceBox.getValue().replace("x", "")) - 1]);
                maxTextField.setText(finalMax[Integer.parseInt(argumentsChoiceBox.getValue().replace("x", "")) - 1]);
            }
        }
    }

    public void geneticWindow(ActionEvent event) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/geneticsoftware/Genetic.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        GeneticController geneticController = loader.getController();

        if(functionPresetChoiceBox.getValue() == "<не выбрано>"){
            finalMax = new String[argumentsSpinner.getValue()];
            finalMin = new String[argumentsSpinner.getValue()];
            for(int i = 0; i < argumentsSpinner.getValue(); i++){
                finalMax[i] = userMax.get(i);
                finalMin[i] = userMin.get(i);
            }
        }

        geneticController.initialize(
                functionTextField.getText(),
                argumentsSpinner.getValue(),
                finalMin,
                finalMax);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setX(315);
        stage.setY(100);

        scene = new Scene(root);
        stage.setTitle("Genetic algorithm");
        stage.setScene(scene);
        stage.show();
    }

    public void setPreset(ActionEvent event){
        if(functionPresetChoiceBox.getValue() != "<не выбрано>" && isNoneBefore){
            userFunction = functionTextField.getText();
            userCountArguments = argumentsSpinner.getValue();
            lastArg = argumentsChoiceBox.getValue();
//            userMax = maxTextField.getText();
//            userMin = minTextField.getText();
        }

        switch (functionPresetChoiceBox.getValue()) {
            case ("<не выбрано>"):
                functionTextField.setText(userFunction);
                valueFactory.setValue(userCountArguments);

//                minTextField.setText(userMin.get(Integer.parseInt(lastArg.replace("x", "")) - 1));
//                maxTextField.setText(userMax.get(Integer.parseInt(lastArg.replace("x", "")) - 1));

                isNoneBefore = true;
                break;
            case ("Функция Растригина"):

                finalMax = new String[2];
                finalMax[0] = "5.12";
                finalMax[1] = "5.12";
                finalMin = new String[2];
                finalMin[0] = "-5.12";
                finalMin[1] = "-5.12";

                if(isNoneBefore) {
                    if (minTextField.getText() != null) {
                        userMin.set(Integer.parseInt(lastArg.replace("x", "")) - 1, minTextField.getText());
                    } else {
                        userMin.set(Integer.parseInt(lastArg.replace("x", "")) - 1, "");
                    }
                    if (maxTextField.getText() != null) {
                        userMax.set(Integer.parseInt(lastArg.replace("x", "")) - 1, maxTextField.getText());
                    } else {
                        userMax.set(Integer.parseInt(lastArg.replace("x", "")) - 1, "");
                    }
                }

                valueFactory.setValue(0);
                isNoneBefore = false;
                valueFactory.setValue(2);

                functionTextField.setText("10 * 2 + Math.pow(x1, 2) - 10 * Math.cos(2 * Math.PI * x1) + Math.pow(x2, 2) - 10 * Math.cos(2 * Math.PI * x2)");

                break;
            case ("Функция Била"):

                finalMax = new String[2];
                finalMax[0] = "4.5";
                finalMax[1] = "4.5";
                finalMin = new String[2];
                finalMin[0] = "-4.5";
                finalMin[1] = "-4.5";

                if(isNoneBefore) {
                    if (minTextField.getText() != null) {
                        userMin.set(Integer.parseInt(lastArg.replace("x", "")) - 1, minTextField.getText());
                    } else {
                        userMin.set(Integer.parseInt(lastArg.replace("x", "")) - 1, "");
                    }
                    if (maxTextField.getText() != null) {
                        userMax.set(Integer.parseInt(lastArg.replace("x", "")) - 1, maxTextField.getText());
                    } else {
                        userMax.set(Integer.parseInt(lastArg.replace("x", "")) - 1, "");
                    }
                }


                valueFactory.setValue(0);
                valueFactory.setValue(2);
                argumentsSpinner.setEditable(false);

                functionTextField.setText("Math.pow(1.5 - x1 + x1 * x2, 2) + Math.pow(2.25 - x1 + x1 * Math.pow(x2, 2), 2) + Math.pow(2.625 - x1 + x1 * Math.pow(x2, 3), 2)");

                isNoneBefore = false;
                break;
            case ("Функция Гольдшейна-Прайса"):

                finalMax = new String[2];
                finalMax[0] = "2";
                finalMax[1] = "2";
                finalMin = new String[2];
                finalMin[0] = "-2";
                finalMin[1] = "-2";

                if(isNoneBefore) {
                    if (minTextField.getText() != null) {
                        userMin.set(Integer.parseInt(lastArg.replace("x", "")) - 1, minTextField.getText());
                    } else {
                        userMin.set(Integer.parseInt(lastArg.replace("x", "")) - 1, "");
                    }
                    if (maxTextField.getText() != null) {
                        userMax.set(Integer.parseInt(lastArg.replace("x", "")) - 1, maxTextField.getText());
                    } else {
                        userMax.set(Integer.parseInt(lastArg.replace("x", "")) - 1, "");
                    }
                }


                valueFactory.setValue(0);
                valueFactory.setValue(2);
                argumentsSpinner.setEditable(false);

                functionTextField.setText("(1 + Math.pow(x1 + x2 + 1, 2) * (19 - 14 * x1 + 3 * Math.pow(x1, 2) - 14 * x2 + 6 * x1 * x2 + 3 * Math.pow(x2, 2))) * (30 + Math.pow(2 * x1 - 3 * x2, 2) * (18 - 32 * x1 + 12 * Math.pow(x1, 2) + 48 * x2 - 36 * x1 * x2 + 27 * Math.pow(x2, 2)))");

                isNoneBefore = false;
                break;
            case ("Функция \"подставка для яиц\" (Eggholder function)"):

                finalMax = new String[2];
                finalMax[0] = "512";
                finalMax[1] = "512";
                finalMin = new String[2];
                finalMin[0] = "-512";
                finalMin[1] = "-512";

                if(isNoneBefore) {
                    if (minTextField.getText() != null) {
                        userMin.set(Integer.parseInt(lastArg.replace("x", "")) - 1, minTextField.getText());
                    } else {
                        userMin.set(Integer.parseInt(lastArg.replace("x", "")) - 1, "");
                    }
                    if (maxTextField.getText() != null) {
                        userMax.set(Integer.parseInt(lastArg.replace("x", "")) - 1, maxTextField.getText());
                    } else {
                        userMax.set(Integer.parseInt(lastArg.replace("x", "")) - 1, "");
                    }
                }

                valueFactory.setValue(0);
                valueFactory.setValue(2);

                functionTextField.setText("-(x2 + 47) * Math.sin(Math.sqrt(Math.abs(x2 + x1 / 2 + 47))) - x1 * Math.sin(Math.sqrt(Math.abs(x1 - (x2 + 47))))");

                isNoneBefore = false;
                break;
        }
    }
}
