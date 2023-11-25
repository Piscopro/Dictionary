module javas {
    requires javafx.controls;
    requires javafx.fxml;
    requires freetts;
    requires java.desktop;
    requires jlayer;

    opens javas to javafx.fxml;
    exports javas;
}