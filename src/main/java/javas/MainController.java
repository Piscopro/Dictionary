package javas;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MainController {
    @FXML private SplitPane worddisplaypane;
    @FXML private AnchorPane worddisplayupperpane;
    @FXML private Label worddisplayword;
    @FXML private Text worddisplayspelling;
    @FXML private Text worddisplayspeaker;
    @FXML private Text worddisplaymeanings;
    @FXML private AnchorPane searchpane;
    @FXML private TextField searchpaneinput;
    @FXML private VBox wordboxsearchholder;
    @FXML private ScrollPane savedwordspane;
    @FXML private VBox wordboxsavedholder;
    @FXML private ScrollPane recentwordspane;
    @FXML private VBox wordboxrecentholder;
    @FXML private AnchorPane graphtranslatepane;
    @FXML private TextArea translateinput;
    @FXML private Text wordtranslatespeaker;
    @FXML private Text translateinputwordcount;
    @FXML private Text translatedtext;
    @FXML private AnchorPane gamepane;
    @FXML private Text gametext;
    @FXML private TextField questionnumberinput;
    @FXML public AnchorPane testpane;
    @FXML public Text questionindexdisplay;
    @FXML public Text rightanswersdisplay;
    @FXML public Text questiondisplay;

    private DictionaryManagement dictionaryManagement = new DictionaryManagement();
    @FXML
    private Label tenapp;
    private Node currentpane;
    @FXML
    private void initialize() {
        DictionaryManagement.insertGameQuestion();
        DictionaryManagement.historyFromFile();
        DictionaryManagement.insertFromFile();
        DictionaryManagement.importFavouritesFromFile();
        searchpaneinput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                changeSearchResults(newValue);
            }
        });
        currentpane = searchpane;
        openSearchPane();
    }
    @FXML
    private void openSearchPane(){
        currentpane.setVisible(false);
        currentpane = searchpane;
        wordboxsearchholder.getChildren().clear();
        changeSearchResults("");
        currentpane.setVisible(true);
    }
    @FXML
    private void openSavedWordsPane(){
        currentpane.setVisible(false);
        currentpane = savedwordspane;
        wordboxsavedholder.getChildren().clear();
        for (Word word : Dictionary.getFavouriteWords()) {
            wordboxsavedholder.getChildren().add(new WordBoxSaved(this, word));
        }
        currentpane.setVisible(true);

    }
    @FXML
    private void openRecentWordsPane(){
        currentpane.setVisible(false);
        currentpane = recentwordspane;
        wordboxrecentholder.getChildren().clear();
        for (Word word: DictionaryManagement.showFullHistory()) {
            wordboxrecentholder.getChildren().add(new WordBoxRecent(this, word));
        }
        currentpane.setVisible(true);
    }
    public void openWordDisplayPane(Word word){
        currentpane.setVisible(false);
        currentpane = worddisplaypane;
        worddisplayupperpane.getChildren().add(new SaveButton(this, word));
        worddisplayword.setText(word.getWordTarget());
        worddisplayspelling.setText(word.getPronunciation());
        worddisplayspeaker.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                DictionaryManagement.speak(word.getWordTarget());
            }
        });
        worddisplaymeanings.setText(word.getMeaning());
        currentpane.setVisible(true);
    }
    private void changeSearchResults(String newValue){
        wordboxsearchholder.getChildren().clear();
        ArrayList<Word> results = null;
        if (Objects.equals(newValue, "")) {
            results = DictionaryManagement.show5RecentHistory();
        }
        else{
            results = DictionaryManagement.dictionarySearcher(newValue);
        }
        WordBoxSearch fwbs = null;
        for (Word word : results) {
            if (fwbs == null) {
                fwbs = new WordBoxSearch(this, word);
                wordboxsearchholder.getChildren().add(fwbs);
            } else {
                wordboxsearchholder.getChildren().add(new WordBoxSearch(this, word));
            }
        }
        WordBoxSearch finalFwbs = fwbs;
        currentpane.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                openWordDisplayPane(finalFwbs.getWord());
                DictionaryManagement.getDictionary().addHistory(finalFwbs.getWord());
                DictionaryManagement.historyExportToFile();
            }
        });
    }

    @FXML private void openDisplayGraphTranslatePane() {
        currentpane.setVisible(false);
        currentpane = graphtranslatepane;
        translateinput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.length() > 100) {
                    translateinput.setText(oldValue);
                    newValue = oldValue;
                }
                translateinputwordcount.setText("(" + newValue.length() + "/100)");
            }
        });
        wordtranslatespeaker.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                DictionaryManagement.speak(translateinput.getText());
            }
        });
        currentpane.setVisible(true);
    }
    @FXML private void translateText() throws IOException {
        translatedtext.setText(DictionaryManagement.translateSentence(translateinput.getText()));
    }
    @FXML public void openGamePane() {
        currentpane.setVisible(false);
        currentpane = gamepane;
        currentpane.setVisible(true);
    }

    @FXML private void startGame() {
        String input = questionnumberinput.getText();
        try {
            int number = Integer.parseInt(input);
            currentpane.setVisible(false);
            currentpane = testpane;
            currentpane.setVisible(true);
            GameTest test = new GameTest(this, number);
        } catch (NumberFormatException e) {
            gametext.setText("Vui lòng nhập vào một số nguyên!");
        }
    }
}