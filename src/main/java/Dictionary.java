import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

class Dictionary {
    private ArrayList<Word> words = new ArrayList<>();
    private Queue<Word> searchHistory = new LinkedList<>();
    private Queue<Word> favouriteWords = new LinkedList<>();

    public Dictionary() {
    }

    public void addWord(Word word) {
        this.words.add(word);
    }

    public void addHistory(Word word) {
        final int maxNumOfWord = 50;
        if (searchHistory.size() > maxNumOfWord - 1) {
            searchHistory.poll();
            searchHistory.add(word);
        } else {
            searchHistory.add(word);
        }
    }

    public void addFavourite(Word word) {
        final int favouriteSize = 50;
        if (favouriteWords.size() > favouriteSize - 1) {
            favouriteWords.poll();
            favouriteWords.add(word);
        } else {
            favouriteWords.add(word);
        }
    }

    public void deleteFavourite(Word word) {
        favouriteWords.remove(word);
    }

    public ArrayList<Word> getWords() {
        return this.words;
    }

    public Queue<Word> getSearchHistory() {
        return searchHistory;
    }

    public Queue<Word> getFavouriteWords() {
        return favouriteWords;
    }
}
