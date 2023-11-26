package javas;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Separator;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class WordBoxSearch extends WordBox {
    private Text wordboxword = new Text();
    private Text wordboxmeanings = new Text();
    private Separator separator = new Separator();
    public WordBoxSearch(MainController controller, Word word) {
        super(controller, word);
        this.setPrefHeight(60);
        this.setPrefWidth(400);
        wordboxword.setLayoutX(15);
        wordboxword.setLayoutY(27);
        wordboxword.setText(word.getWordTarget());
        wordboxword.setWrappingWidth(160);
        wordboxword.setStrokeWidth(0);
        wordboxword.setStrokeType(StrokeType.OUTSIDE);
        wordboxword.setFont(new Font("System Bold", 15));
        wordboxmeanings.setLayoutX(15);
        wordboxmeanings.setLayoutY(44);
        wordboxmeanings.setText(word.getFirstMeaning());
        wordboxmeanings.setWrappingWidth(370);
        wordboxmeanings.setStrokeWidth(0);
        wordboxmeanings.setStrokeType(StrokeType.OUTSIDE);
        separator.setLayoutX(0);
        separator.setLayoutY(59);
        separator.setPrefHeight(3);
        separator.setPrefWidth(400);
        this.getChildren().addAll(wordboxword, wordboxmeanings, separator);
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                controller.openWordDisplayPane(word);
                DictionaryManagement.getDictionary().addHistory(word);
                DictionaryManagement.historyExportToFile();
                DictionaryManagement.historyFromFile();;
            }
        });
    }
}
