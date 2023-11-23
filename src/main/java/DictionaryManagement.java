import java.io.*;
import java.util.*;

class DictionaryManagement {
    private static Dictionary dictionary;
    static Scanner scanner = new Scanner(System.in);

    public DictionaryManagement() {
        dictionary = new Dictionary();
    }

    public static void insertFromFile() {
        String importFilePath = "src/main/dictionaries.txt";

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
        String exportFilePath = "src/main/dictionaries.txt";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(exportFilePath))) {
            Iterator<Word> iterator = dictionary.getWords().iterator();

            while (iterator.hasNext()) {
                Word entry = iterator.next();
                // Export word and pronunciation
                bw.write("@" + entry.getWordTarget() + " /" + entry.getPronunciation() + "/\n");

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

    public Dictionary getDictionary() {
        return dictionary;
    }


    public void dictionaryLookup() {
        System.out.print("Enter a word to look up: ");
        String wordToLookup = scanner.nextLine();

        // Sắp xếp danh sách từ điển nếu chưa được sắp xếp
        ArrayList<Word> words = dictionary.getWords();
        if (!isDictionarySorted(words)) {
            sortDictionary(words);
        }

        int index = binarySearch(words, wordToLookup);

        if (index != -1) {
            Word entry = words.get(index);
            System.out.println("Word: " + entry.getWordTarget() + " " + entry.getPronunciation());

            for (Meaning meaning : entry.getMeanings()) {
                System.out.println("*  " + meaning.getPartOfSpeech());

                // Split the description into lines based on the dash '-'
                String[] descriptionLines = meaning.getDescription().split("\\s*-\\s*");

                // Print each line of the description without the leading empty line
                boolean isFirstLine = true;
                for (String line : descriptionLines) {
                    if (!isFirstLine) {
                        System.out.println("   - " + line.trim());
                    } else {
                        //System.out.println("   - " + line.trim());
                        isFirstLine = false;
                    }
                }
            }
            dictionary.addHistory(entry);
        } else {
            System.out.println("Word not found in the dictionary.");
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


    public void addWord(String word_target, String pronunciation, String partOfSpeech, String description) {
        Word newWord = new Word(word_target, pronunciation);
        dictionary.addWord(newWord);
    }

    public void editWord() {
        System.out.print("Enter the word to update: ");
        String wordToUpdate = scanner.nextLine();

        Iterator<Word> iterator = dictionary.getWords().iterator();
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
            System.out.println("Word not found in the dictionary.");
        } else {
            System.out.println("Word updated successfully.");
        }
    }

    public void deleteWord() {
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

    /*public static String showWordFirstMeaning(Word word) {
        StringBuilder wordMeaning = new StringBuilder();
        wordMeaning.append(word.getFirstMeaning());
        return wordMeaning.toString();
    }*/
    public static String showWordFirstMeaning(Word word) {
        return word.getWordTarget() + " "
                + word.getPronunciation() + "\n" + word.getFirstMeaning();
    }

    public static void BoxSearchPrefix() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a prefix to search for: ");
        String prefix = scanner.nextLine();
        ArrayList<Word> searchResults = dictionarySearcher(prefix);

        if (!searchResults.isEmpty()) {
            //System.out.println("Words that start with \"" + prefix + "\":");

            for (Word entry : searchResults) {
                System.out.println(showWordFirstMeaning(entry));
                System.out.println();
                }
            }

        else {
            System.out.println("No words found.");
        }
    }

    public static void showSearchHistory() {
        System.out.println("1: Full history, 2: Search history");
        Scanner sc = new Scanner(System.in);
        int op = sc.nextInt();

        ArrayList<Word> history = new ArrayList<>(dictionary.getSearchHistory());

        if (op == 1) {
            for (int i = history.size() - 1; i >= 0; i--) {
                System.out.println(showWordFirstMeaning(history.get(i)));
            }
        } else if (op == 2) {
            int startIdx = history.size() - Math.min(5, history.size());
            for (int i = history.size() - 1; i >= startIdx; i--) {
                System.out.println(showWordFirstMeaning(history.get(i)));
            }
        }
    }

    public static void exportFavouritesToFile() {
        String exportFilePath = "src/main/favourites.txt";
        Set<String> exportedWords = new HashSet<>();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(exportFilePath))) {
            for (Word word : dictionary.getFavouriteWords()) {
                // Check if the word has already been exported
                if (!exportedWords.contains(word.getWordTarget())) {
                    bw.write("@" + word.getWordTarget() + " " + word.getPronunciation() + "\n");

                    for (Meaning meaning : word.getMeanings()) {
                        bw.write("*  " + meaning.getPartOfSpeech() + "\n");
                        bw.write("  " + meaning.getDescription() + "\n");
                    }
                    bw.newLine();

                    // Add the word to the set of exported words
                    exportedWords.add(word.getWordTarget());
                }
            }
            System.out.println("Favorites exported to " + exportFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void importFavouritesFromFile() {
        String importFilePath = "src/main/favourites.txt";

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




    public static void Favourite() {
        importFavouritesFromFile();

        while (true) {
            System.out.println("[0] Exit:");
            System.out.println("[1] Show full favourite list:");
            System.out.println("[2] Add favourite:");
            System.out.println("[3] Delete favourite:");
            System.out.println("[4] Save!");
            System.out.print("Choose an option: ");
            int op1 = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            if (op1 == 0) {
                System.out.println("Exiting Favourites...");
                break;
            }

            switch (op1) {
                case 1:
                    showFavourite();
                    break;
                case 2:
                    addFavourite();
                    break;
                case 3:
                    deleteFavourite();
                    break;
                case 4:
                    exportFavouritesToFile();
                    break;
                default:
                    System.out.println("Invalid option. Please choose 0, 1, 2, 3, or 4.");
                    break;
            }
        }
    }

    /*public static void showFavourite() {
        ArrayList<Word> favourites = new ArrayList<>(Dictionary.getFavouriteWords());
        for (int i = favourites.size() - 1; i >= 0; i--) {
            System.out.println(showWordFirstMeaning(favourites.get(i)));
            System.out.println();
        }
    }*/
    public static void showFavourite() {
        ArrayList<Word> favourites = new ArrayList<>(Dictionary.getFavouriteWords());
        Set<String> displayedWords = new HashSet<>();

        for (Word favourite : favourites) {
            if (!displayedWords.contains(favourite.getWordTarget())) {
                System.out.println(showWordFirstMeaning(favourite));
                System.out.println();
                displayedWords.add(favourite.getWordTarget());
            }
        }
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


    public static void addFavourite() {
        System.out.println("Enter the word you want to add: ");
        String wordToAdd = scanner.nextLine();
        Word wordObjToAdd = findWord(wordToAdd);

        if (wordObjToAdd != null) {
            dictionary.addFavourite(wordObjToAdd);
            System.out.println(wordToAdd + " added to favourites.");
        } else {
            System.out.println("Word not found.");
        }
    }


    public static void deleteFavourite() {
        String wordToDelete = scanner.nextLine();
        Word wordObjToDelete = findWord(wordToDelete);
        if (wordObjToDelete != null) {
            dictionary.deleteFavourite(wordObjToDelete);
            System.out.println(wordToDelete + " removed from favourites.");
        } else {
            System.out.println("Word not found in favourites.");
        }
    }

}