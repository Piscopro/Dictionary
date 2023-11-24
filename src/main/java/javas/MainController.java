package javas;

import javas.WordBoxSearch;
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

public class MainController {
    @FXML private SplitPane worddisplaypane;
    @FXML private Label worddisplayword;
    @FXML private Text worddisplaymeanings;
    @FXML private AnchorPane searchpane;
    @FXML private TextField searchpaneinput;
    @FXML private VBox wordboxsearchholder;
    @FXML private ScrollPane savedwordspane;
    @FXML private ScrollPane recentwordspane;
    @FXML private ScrollPane displayallpane;
    @FXML
    private Label tenapp;
    private Node currentpane;
    @FXML
    private void initialize() {
        searchpaneinput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                changeSearchResults(newValue);
            }
        });
        currentpane = searchpane;
        currentpane.setVisible(true);
        openWordDisplayPane("kiss");
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
        currentpane.setVisible(true);
    }
    @FXML
    private void openDisplayAllPane(){
        currentpane.setVisible(false);
        currentpane = displayallpane;
        currentpane.setVisible(true);
    }
    public void openWordDisplayPane(String word){
        currentpane.setVisible(false);
        currentpane = worddisplaypane;
        worddisplayword.setText(word);
        DictionaryManagement dicman = new DictionaryManagement();
        DictionaryManagement.insertFromFile();
        worddisplaymeanings.setText(dicman.dictionaryLookup(word));
        currentpane.setVisible(true);
    }
    private void changeSearchResults(String newValue){
        wordboxsearchholder.getChildren().clear();
        wordboxsearchholder.getChildren().addAll(new WordBoxSearch(this, newValue));
    }
}