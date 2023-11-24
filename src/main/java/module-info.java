module javas {
    requires javafx.controls;
    requires javafx.fxml;


    opens javas to javafx.fxml;
    exports javas;
}