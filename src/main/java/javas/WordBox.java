package javas;

import javas.MainController;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class WordBox extends AnchorPane {
    private MainController controller;
    private String word;
    public WordBox(MainController controller, String word){
        this.controller = controller;
        this.word = word;
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                controller.openWordDisplayPane(word);
            }
        });
    }
}