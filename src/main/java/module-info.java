module com.geneticalgorithm {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.scripting;


    opens com.geneticsoftware to javafx.fxml;
    exports com.geneticsoftware;
    exports com.geneticsoftware.gui;
    opens com.geneticsoftware.gui to javafx.fxml;
    exports com.geneticsoftware.util;
    opens com.geneticsoftware.util to javafx.fxml;
    exports com.geneticsoftware.geneticalgorithm;
    opens com.geneticsoftware.geneticalgorithm to javafx.fxml;

}