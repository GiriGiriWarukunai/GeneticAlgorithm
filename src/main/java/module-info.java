module com.geneticalgorithm {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.geneticalgorithm to javafx.fxml;
    exports com.geneticalgorithm;
}