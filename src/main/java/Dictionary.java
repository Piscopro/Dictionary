import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class Dictionary {
    private ArrayList<Word> words = new ArrayList<>();
    private Queue<Word> searchHistory = new LinkedList<>();
    public static List<Word> favouriteWords = new ArrayList<>();
    private static final int FAVOURITE_SIZE = 50;
    private static final int HISTORY_SIZE = 50;

    public void addFavourite(Word word) {
        if (favouriteWords.size() >= FAVOURITE_SIZE) {
            favouriteWords.remove(0);  // Remove the oldest favourite if the list is full
        }
        favouriteWords.add(word);
    }

    public void deleteFavourite(Word word) {
        favouriteWords.remove(word);
    }

    public Dictionary() {
    }

    public void addWord(Word word) {
        this.words.add(word);
    }

    public void addHistory(Word word) {
        if (searchHistory.size() > HISTORY_SIZE- 1) {
            searchHistory.poll();
            searchHistory.add(word);
        } else {
            searchHistory.add(word);
        }
    }

    public static List<Word> getFavouriteWords() {
        return favouriteWords;
    }

    /*public void addFavourite(Word word) {
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
    }*/

    public ArrayList<Word> getWords() {
        return this.words;
    }

    public Queue<Word> getSearchHistory() {
        return searchHistory;
    }

    /*public Queue<Word> getFavouriteWords() {
        return favouriteWords;
    }*/
}
