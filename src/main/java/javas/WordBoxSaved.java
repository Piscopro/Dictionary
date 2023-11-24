package javas;

import javas.MainController;
import javas.WordBox;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.text.Text;

public class WordBoxSaved extends WordBox {
    private Text wordboxword = new Text();
    private Text wordboxmeanings = new Text();
    private Separator separator = new Separator();
    private Button unsavebtn = new Button();
    public WordBoxSaved(MainController controller, String word){
        super(controller,word);
    }
}
