import java.util.ArrayList;

class Dictionary {
    private ArrayList<Word> words = new ArrayList();

    public Dictionary() {
    }

    public void addWord(Word word) {
        this.words.add(word);
    }

    public ArrayList<Word> getWords() {
        return this.words;
    }
}
