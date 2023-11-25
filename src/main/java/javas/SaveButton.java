package javas;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class SaveButton extends Button {
    private MainController controller;
    private Word word;
    public SaveButton(MainController controller, Word word) {
        this.controller = controller;
        this.word = word;
        renderText();
        this.setLayoutX(565);
        this.setLayoutY(26.0);
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (word.getSaved()) {
                    DictionaryManagement.getDictionary().deleteFavourite(word);
                } else DictionaryManagement.getDictionary().addFavourite(word);
                renderText();
                DictionaryManagement.exportFavouritesToFile();
            }
        });
    }

    public void renderText() {
        if (word.getSaved()) {
            this.setText("Bỏ lưu");
        }
        else this.setText("Lưu");
    }
}
