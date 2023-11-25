package javas;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Objects;

public class MainController {
    @FXML private SplitPane worddisplaypane;
    @FXML private Label worddisplayword;
    @FXML private Text worddisplaymeanings;
    @FXML private AnchorPane searchpane;
    @FXML private TextField searchpaneinput;
    @FXML private VBox wordboxsearchholder;
    @FXML private ScrollPane savedwordspane;
    @FXML private ScrollPane recentwordspane;
    @FXML private VBox wordboxrecentholder;
    @FXML private ScrollPane displayallpane;
    @FXML private VBox wordboxallholder;
    private DictionaryManagement dictionaryManagement = new DictionaryManagement();
    @FXML
    private Label tenapp;
    private Node currentpane;
    @FXML
    private void initialize() {
        DictionaryManagement.insertFromFile();
        searchpaneinput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                changeSearchResults(newValue);
            }
        });
        currentpane = searchpane;
        currentpane.setVisible(true);
    }
    @FXML
    private void openSearchPane(){
        currentpane.setVisible(false);
        currentpane = searchpane;
        currentpane.setVisible(true);
    }
    @FXML
    private void openSavedWordsPane(){
        currentpane.setVisible(false);
        currentpane = savedwordspane;
        currentpane.setVisible(true);

    }
    @FXML
    private void openRecentWordsPane(){
        currentpane.setVisible(false);
        currentpane = recentwordspane;
        wordboxrecentholder.getChildren().clear();
        for (Word word: dictionaryManagement.showFullHistory()) {
            wordboxrecentholder.getChildren().add(new WordBoxRecent(this, word));
        }
        currentpane.setVisible(true);
    }
    @FXML
    private void openDisplayAllPane(){
        currentpane.setVisible(false);
        currentpane = displayallpane;
        for (Word word: DictionaryManagement.getDictionary().getWords()) {
            wordboxallholder.getChildren().add(new WordBoxAll(this, word));
        }
        currentpane.setVisible(true);
    }
    public void openWordDisplayPane(Word word){
        currentpane.setVisible(false);
        currentpane = worddisplaypane;
        worddisplayword.setText(word.getWordTarget());
        worddisplaymeanings.setText(word.getMeaning());
        currentpane.setVisible(true);
    }
    private void changeSearchResults(String newValue){
        wordboxsearchholder.getChildren().clear();
        if (Objects.equals(newValue, "")) {
            for (Word word : dictionaryManagement.show5RecentHistory()) {
                wordboxsearchholder.getChildren().add(new WordBoxSearch(this, word));
            }
            return;
        }
        for (Word word: DictionaryManagement.dictionarySearcher(newValue)) {
            wordboxsearchholder.getChildren().add(new WordBoxSearch(this, word));
        }
    }
}