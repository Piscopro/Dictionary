package javas;
import java.util.Set;
import java.util.*;

class Dictionary {
    private ArrayList<Word> words = new ArrayList<>();
    private Queue<Word> searchHistory = new LinkedList<>();
    public static List<Word> favouriteWords = new ArrayList<>();
    private ArrayList<GameQuestion> gameQuestions = new ArrayList<>();
    private static final int FAVOURITE_SIZE = 50;
    private static final int HISTORY_SIZE = 50;

    public void addFavourite(Word word) {
        if (favouriteWords.size() >= FAVOURITE_SIZE) {
            favouriteWords.getFirst().setSaved(false);
            favouriteWords.remove(0);  // Remove the oldest favourite if the list is full

        }
        favouriteWords.add(word);
        word.setSaved(true);
    }

    public void deleteFavourite(Word word) {
        favouriteWords.remove(word);
        word.setSaved(false);
    }

    public Dictionary() {
    }

    public void addWord(Word word) {
        // Kiểm tra xem từ đã tồn tại trong danh sách hay chưa
        for (Word existingWord : this.words) {
            if (existingWord.getWordTarget().equalsIgnoreCase(word.getWordTarget())) {
                return; // Trả về ngay lập tức để không thêm từ trùng lặp
            }
        }
        // Thêm từ mới nếu nó chưa tồn tại
        this.words.add(word);
    }


    //    public void addHistory(Word word) {
//        if (searchHistory.size() > HISTORY_SIZE- 1) {
//            searchHistory.poll();
//            searchHistory.add(word);
//        } else {
//            searchHistory.add(word);
//        }
//    }
//public void addHistory(Word word) {
//    // Kiểm tra xem từ đã có trong searchHistory chưa
//    if (!searchHistory.contains(word)) {
//        if (searchHistory.size() > HISTORY_SIZE - 1) {
//            searchHistory.poll();
//            searchHistory.add(word);
//        } else {
//            searchHistory.add(word);
//        }
//    }
//}

    public void addHistory(Word word) {
        // Kiểm tra xem từ đã có trong searchHistory chưa
        if (!searchHistory.contains(word)) {
            // Kiểm tra xem danh sách đã đầy chưa
            if (searchHistory.size() > HISTORY_SIZE - 1) {
                // Kiểm tra xem từ đã có trong searchHistory chưa
                if (searchHistory.contains(word)) {
                    // Nếu từ đã có, xóa nó khỏi danh sách
                    searchHistory.remove(word);
                } else {
                    // Nếu danh sách đã đầy, loại bỏ phần tử đầu tiên
                    searchHistory.poll();
                }
            }
            // Thêm từ mới vào danh sách
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

    public ArrayList<GameQuestion> getGameQuestions() {
        return gameQuestions;
    }

    public void addQuestion(GameQuestion question) {
        gameQuestions.add(question);
    }
}
