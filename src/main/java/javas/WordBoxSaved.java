package javas;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.control.Separator;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class WordBoxSaved extends WordBox {
    private Text wordboxword = new Text();
    private Text wordboxmeanings = new Text();
    private Separator separator = new Separator();
    private SaveButton button;
    public WordBoxSaved(MainController controller, Word word){
        super(controller, word);
        button = new SaveButton(controller, word);
        this.setPrefHeight(60);
        this.setPrefWidth(630);
        wordboxword.setLayoutX(15);
        wordboxword.setLayoutY(27);
        wordboxword.setText(word.getWordTarget());
        wordboxword.setWrappingWidth(630);
        wordboxword.setStrokeWidth(0);
        wordboxword.setStrokeType(StrokeType.OUTSIDE);
        wordboxword.setFont(new Font("System Bold", 15));
        wordboxmeanings.setLayoutX(15);
        wordboxmeanings.setLayoutY(44);
        wordboxmeanings.setText(word.getFirstMeaning());
        wordboxmeanings.setWrappingWidth(630);
        wordboxmeanings.setStrokeWidth(0);
        wordboxmeanings.setStrokeType(StrokeType.OUTSIDE);
        separator.setLayoutX(0);
        separator.setLayoutY(59);
        separator.setPrefHeight(3);
        separator.setPrefWidth(650);
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (word.getSaved()) {
                    DictionaryManagement.getDictionary().deleteFavourite(word);
                } else DictionaryManagement.getDictionary().addFavourite(word);
                button.renderText();
                DictionaryManagement.exportFavouritesToFile();
                VBox parentPane = (VBox) WordBoxSaved.this.getParent();
                parentPane.getChildren().remove(WordBoxSaved.this);
            }
        });
        this.getChildren().addAll(wordboxword, wordboxmeanings, separator, button);
    }
}
