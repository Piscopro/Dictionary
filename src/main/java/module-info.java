module javas {
    requires javafx.controls;
    requires javafx.fxml;
    requires freetts;


    opens javas to javafx.fxml;
    exports javas;
}