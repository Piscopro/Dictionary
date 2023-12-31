package javas;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

class DictionaryManagement {
    private static Dictionary dictionary;
    static Scanner scanner = new Scanner(System.in);

    public DictionaryManagement() {
        dictionary = new Dictionary();
    }

    public static void insertFromFile() {
        String importFilePath = "src/main/resources/text/dictionaries.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(importFilePath))) {
            String line;
            Word currentWord = null;
            Meaning currentMeaning = null;
            StringBuilder descriptionBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("@")) {
                    // Handle the end of the current Word
                    if (currentMeaning != null && descriptionBuilder.length() > 0) {
                        currentMeaning.setDescription(descriptionBuilder.toString().trim());
                        descriptionBuilder.setLength(0);
                    }
                    currentMeaning = null;

                    // Create a new Word
                    String[] parts = line.split(" /");
                    String word = parts[0].substring(1).trim();
                    String pronunciation = "/" + parts[1].trim();
                    currentWord = new Word(word, pronunciation);
                    dictionary.addWord(currentWord);
                } else if (currentWord != null && line.startsWith("*")) {
                    // End the current Meaning description
                    if (currentMeaning != null) {
                        currentMeaning.setDescription(descriptionBuilder.toString().trim());
                        descriptionBuilder.setLength(0);
                    }

                    // Create a new Meaning
                    String partOfSpeech = line.substring(1).trim();
                    currentMeaning = new Meaning(partOfSpeech, "");
                    currentWord.addMeaning(currentMeaning);
                } else if (currentMeaning != null) {
                    // Add data to the description
                    descriptionBuilder.append(line).append(" ");
                }
            }

            // Handle the final description
            if (currentMeaning != null && descriptionBuilder.length() > 0) {
                currentMeaning.setDescription(descriptionBuilder.toString().trim());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

public static void dictionaryExportToFile() {
    String exportFilePath = "src/main/resources/text/dictionaries.txt";

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(exportFilePath, false))) { // Set 'false' to overwrite
        Iterator<Word> iterator = dictionary.getWords().iterator();

        while (iterator.hasNext()) {
            Word entry = iterator.next();
            // Export word and pronunciation
            bw.write("@" + entry.getWordTarget() + " " + entry.getPronunciation() + "\n");

            // Export each Meaning
            for (Meaning meaning : entry.getMeanings()) {
                bw.write("*  " + meaning.getPartOfSpeech() + "\n");
                bw.write("  " + meaning.getDescription() + "\n");
            }

            // Add an empty line between words
            bw.newLine();
        }

        System.out.println("Data exported to " + exportFilePath);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    public static Dictionary getDictionary() {
        return dictionary;
    }

    public static Word dictionaryLookup(String wordToLookup) {
        ArrayList<Word> words = dictionary.getWords();
        if (!isDictionarySorted(words)) {
            sortDictionary(words);
        }

        int index = binarySearch(words, wordToLookup);

        if (index != -1) {
            Word entry = words.get(index);
            dictionary.addHistory(entry);
            return entry;
        } else {
            return null;
        }
    }

    private static boolean isDictionarySorted(ArrayList<Word> words) {
        for (int i = 1; i < words.size(); i++) {
            if (words.get(i - 1).getWordTarget().compareTo(words.get(i).getWordTarget()) > 0) {
                return false;
            }
        }
        return true;
    }

    private static void sortDictionary(ArrayList<Word> words) {
        Collections.sort(words, Comparator.comparing(Word::getWordTarget));
    }

    private static int binarySearch(ArrayList<Word> words, String target) {
        int left = 0;
        int right = words.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int compareResult = words.get(mid).getWordTarget().compareToIgnoreCase(target);

            if (compareResult == 0) {
                return mid; // Tìm thấy từ
            } else if (compareResult < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return -1; // Không tìm thấy từ
    }

    public static ArrayList<Word> showAllWords() {
        ArrayList<Word> words = DictionaryManagement.getDictionary().getWords();
        ArrayList<Word> result = new ArrayList<>();
        // Sort the words alphabetically
        //Collections.sort(words, (w1, w2) -> w1.getWordTarget().compareToIgnoreCase(w2.getWordTarget()));
        sortDictionary(words);

        for (Word word : words) {
            result.add(word);
        }
        return result;
    }

    public static String editWord() {
        System.out.print("Enter the word to update: ");
        String wordToUpdate = scanner.nextLine();

        Iterator<Word> iterator = dictionary.getWords().iterator();
        StringBuilder result = new StringBuilder();
        boolean isWordFound = false;

        while (iterator.hasNext()) {
            Word entry = iterator.next();

            if (entry.getWordTarget().equalsIgnoreCase(wordToUpdate)) {
                System.out.print("Enter the new pronunciation: ");
                entry.setPronunciation(scanner.nextLine()); // Update pronunciation

                for (int i = 0; i < entry.getMeanings().size(); i++) {
                    Meaning meaning = entry.getMeanings().get(i);
                    System.out.println("Current meaning: * " + meaning.getPartOfSpeech());
                    String[] descriptions = meaning.getDescription().split("(?=-)");
                    for (String description : descriptions) {
                        System.out.println("   " + description.trim());
                    }

                    System.out.println("Do you want to update this meaning? (yes/no)");
                    if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                        System.out.print("Enter the new part of speech: ");
                        String newPartOfSpeech = scanner.nextLine();
                        System.out.print("Enter the new description: ");
                        String newDescription = " -" + scanner.nextLine();

                        meaning.setPartOfSpeech(newPartOfSpeech); // Update part of speech
                        meaning.setDescription(newDescription); // Update description
                    }
                }

                isWordFound = true;
                break;
            }
        }

        if (!isWordFound) {
            return "Word not found in the dictionary.";
        } else {
            return "Word updated successfully.";
        }
    }


    public static void deleteWord() {
        System.out.print("Enter the word to remove: ");
        String wordToRemove = scanner.nextLine();

        Iterator<Word> iterator = dictionary.getWords().iterator();
        boolean isDeleted = false;

        while (iterator.hasNext()) {
            Word entry = iterator.next();

            if (entry.getWordTarget().equalsIgnoreCase(wordToRemove)) {
                iterator.remove();
                System.out.println("Word has been deleted from the dictionary.");
                isDeleted = true;
                break;
            }
        }

        if (!isDeleted) {
            System.out.println("Word not found in the dictionary.");
        }
    }

    //tối đa 5 từ
    public static ArrayList<Word> dictionarySearcher(String prefix) {
        ArrayList<Word> searchResults = new ArrayList<>();
        Iterator<Word> iterator = dictionary.getWords().iterator();

        int count = 0;  // Biến đếm số lượng từ đã thêm vào searchResults

        while (iterator.hasNext() && count < 5) {
            Word entry = iterator.next();
            if (entry.getWordTarget().toLowerCase().startsWith(prefix.toLowerCase())) {
                searchResults.add(entry);
                count++;  // Tăng biến đếm khi thêm từ vào kết quả
            }
        }

        return searchResults;
    }


    public void addWord(Word newWord) {
        dictionary.addWord(newWord);
    }

    public static ArrayList<String> BoxSearchPrefix(String prefix) {
        ArrayList<Word> searchResults = dictionarySearcher(prefix);
        ArrayList<String> formattedResults = new ArrayList<>();

        for (Word entry : searchResults) {
            formattedResults.add(entry.showWordTargetFirstMeaning());
        }
        return formattedResults;
    }

    public static void historyExportToFile() {
        String exportFilePath = "src/main/resources/text/history.txt";

        Set<String> exportedWords = new HashSet<>();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(exportFilePath, false))) { // false để tạo mới file
            for (Word word : dictionary.getSearchHistory()) {
                if (!exportedWords.contains(word.getWordTarget())) {
                    bw.write("@" + word.getWordTarget() + " " + word.getPronunciation() + "\n");

                    for (Meaning meaning : word.getMeanings()) {
                        bw.write("*  " + meaning.getPartOfSpeech() + "\n");
                        bw.write("  " + meaning.getDescription() + "\n");
                    }
                    bw.newLine();

                    exportedWords.add(word.getWordTarget());
                }
            }
            System.out.println("Data exported to " + exportFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

public static void historyFromFile() {
    String importFilePath = "src/main/resources/text/history.txt";

    try (BufferedReader reader = new BufferedReader(new FileReader(importFilePath))) {
        String line;
        Word currentWord = null;
        Meaning currentMeaning = null;
        StringBuilder descriptionBuilder = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            line = line.trim();

            if (line.startsWith("@")) {
                // Handle the end of the current Word
                if (currentMeaning != null && descriptionBuilder.length() > 0) {
                    currentMeaning.setDescription(descriptionBuilder.toString().trim());
                    descriptionBuilder.setLength(0);
                }
                currentMeaning = null;

                // Create a new Word
                String[] parts = line.split(" /");
                if (parts.length < 2) {
                    System.out.println("Invalid data format at line: " + line);
                    continue;
                }
                String word = parts[0].substring(1).trim();
                String pronunciation = "/" + parts[1].trim();
                currentWord = new Word(word, pronunciation);
                dictionary.addHistory(currentWord);
            } else if (currentWord != null && line.startsWith("*")) {
                // End the current Meaning description
                if (currentMeaning != null) {
                    currentMeaning.setDescription(descriptionBuilder.toString().trim());
                    descriptionBuilder.setLength(0);
                }

                // Create a new Meaning
                String partOfSpeech = line.substring(1).trim();
                currentMeaning = new Meaning(partOfSpeech, "");
                currentWord.addMeaning(currentMeaning);
            } else if (currentMeaning != null) {
                // Add data to the description
                descriptionBuilder.append(line).append(" ");
            }
        }

        // Handle the final description
        if (currentMeaning != null && descriptionBuilder.length() > 0) {
            currentMeaning.setDescription(descriptionBuilder.toString().trim());
        }

    } catch (IOException e) {
        e.printStackTrace();
    }
}

    public static ArrayList<Word> showFullHistory() {
        ArrayList<Word> history = new ArrayList<>(dictionary.getSearchHistory());
        ArrayList<Word> result = new ArrayList<>();

        Set<Word> uniqueWords = new HashSet<>(); // Sử dụng HashSet để lưu trữ các từ duy nhất

        // Duyệt qua danh sách lịch sử theo thứ tự ngược lại
        for (int i = history.size() - 1; i >= 0; i--) {
            Word word = history.get(i);

            // Kiểm tra xem từ đã xuất hiện chưa
            if (uniqueWords.add(word)) {
                // Nếu chưa, thêm từ vào danh sách kết quả
                result.add(word);
            }
        }

        return result;
    }


    public static ArrayList<Word> show5RecentHistory() {
        int maxEntries = 5;
        ArrayList<Word> history = new ArrayList<>(dictionary.getSearchHistory());
        int entriesToReturn = Math.min(maxEntries, history.size());
        ArrayList<Word> result = new ArrayList<>();

        int startIdx = history.size() - entriesToReturn;
        for (int i = history.size() - 1; i >= startIdx; i--) {
            Word word = history.get(i);
            result.add(word);
        }

        return result;
    }

    public static void exportFavouritesToFile() {
        String exportFilePath = "src/main/resources/text/favourites.txt";
        Set<String> exportedWords = new HashSet<>();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(exportFilePath, false))) { // false để tạo mới file
            for (Word word : dictionary.getFavouriteWords()) {
                if (!exportedWords.contains(word.getWordTarget())) {
                    bw.write("@" + word.getWordTarget() + " " + word.getPronunciation() + "\n");

                    for (Meaning meaning : word.getMeanings()) {
                        bw.write("*  " + meaning.getPartOfSpeech() + "\n");
                        bw.write("  " + meaning.getDescription() + "\n");
                    }
                    bw.newLine();

                    exportedWords.add(word.getWordTarget());
                }
            }
            System.out.println("Favorites exported to " + exportFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void importFavouritesFromFile() {
        String importFilePath = "src/main/resources/text/favourites.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(importFilePath))) {
            String line;
            Word currentWord = null;
            Meaning currentMeaning = null;
            StringBuilder descriptionBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("@")) {
                    if (currentMeaning != null && descriptionBuilder.length() > 0) {
                        currentMeaning.setDescription(descriptionBuilder.toString().trim());
                        descriptionBuilder.setLength(0);
                    }
                    currentMeaning = null;

                    if (currentWord != null) {
                        dictionary.addFavourite(currentWord);
                    }

                    String[] parts = line.split(" ", 2);
                    if (parts.length < 2) {
                        System.out.println("Invalid data format at line: " + line);
                        continue;
                    }
                    String word = parts[0].substring(1).trim();
                    String pronunciation = parts[1].trim();
                    currentWord = new Word(word, pronunciation);
                } else if (currentWord != null && line.startsWith("*")) {
                    if (currentMeaning != null) {
                        currentMeaning.setDescription(descriptionBuilder.toString().trim());
                        descriptionBuilder.setLength(0);
                    }

                    String partOfSpeech = line.substring(1).trim();
                    currentMeaning = new Meaning(partOfSpeech, "");
                    currentWord.addMeaning(currentMeaning);
                } else if (currentMeaning != null) {
                    descriptionBuilder.append(line).append(" ");
                }
            }

            if (currentMeaning != null && descriptionBuilder.length() > 0) {
                currentMeaning.setDescription(descriptionBuilder.toString().trim());
            }

            if (currentWord != null) {
                dictionary.addFavourite(currentWord);
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error importing favourites from file.");
        }
    }

    public static ArrayList<String> showFavourite() {
        ArrayList<Word> favourites = new ArrayList<>(Dictionary.getFavouriteWords());
        Set<String> displayedWords = new HashSet<>();
        ArrayList<String> formattedResults = new ArrayList<>();

        for (int i = favourites.size() - 1; i >= 0; i--) {
            if (!displayedWords.contains(favourites.get(i).getWordTarget())) {
                formattedResults.add(favourites.get(i).showWordTargetFirstMeaning());
                displayedWords.add(favourites.get(i).getWordTarget());
            }
        }

        return formattedResults;
    }

    public static Word findWord(String wordToFind) {
        ArrayList<Word> words = dictionary.getWords();
        if (!isDictionarySorted(words)) {
            sortDictionary(words);
        }

        int index = binarySearch(words, wordToFind);

        if (index != -1) {
            return words.get(index);
        } else {
            return null;
        }
    }

    public static String addFavourite(String wordToAdd) {
        Word wordObjToAdd = findWord(wordToAdd);

        if (wordObjToAdd != null) {
            dictionary.addFavourite(wordObjToAdd);
            exportFavouritesToFile();
            return wordToAdd + " added to favourites.";
        } else {
            return "Word not found.";
        }
    }

    public static void speak(String word) {
        try {
            String string = "http://translate.google.com/translate_tts?tl=en&q="
                    + word + "&client=tw-ob";

            // URL of the audio file
            URL url = new URL(string);
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();

            Player player = new Player(is);
            player.play();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JavaLayerException e) {
            throw new RuntimeException(e);
        }
    }

    public static String translateSentence(String sentence) throws IOException {
        sentence = URLEncoder.encode(sentence, StandardCharsets.UTF_8);

        String urlStr = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=vi&dt=t&q=" + sentence;
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        conn.disconnect();
        String jsonString = content.toString();
        String translatedText = jsonString.split("\"")[1];
        return translatedText;
    }

    public static void insertGameQuestion() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/text/questions.txt"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 2) {
                    GameQuestion question = new GameQuestion();
                    question.setQuestion(parts[0]);
                    for (int i = 1; i < parts.length - 1; i++) {
                        question.getAnswers().add(parts[i]);
                    }
                    String correctAnswerLetter = parts[parts.length - 1];
                    int correctAnswerIndex = 0;
                    switch (correctAnswerLetter) {
                        case "A":
                            correctAnswerIndex = 0;
                            break;
                        case "B":
                            correctAnswerIndex = 1;
                            break;
                        case "C":
                            correctAnswerIndex = 2;
                            break;
                        case "D":
                            correctAnswerIndex = 3;
                            break;
                    }
                    question.setCorrectAnswer(question.getAnswers().get(correctAnswerIndex));



                    dictionary.addQuestion(question);
                } else {
                    System.out.println("Invalid question format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your application's needs
        }
    }

    public static GameQuestion getRandomQuestion() {
        Random rand = new Random();
        int randomIndex = rand.nextInt(dictionary.getGameQuestions().size());
        return dictionary.getGameQuestions().get(randomIndex);
    }
}