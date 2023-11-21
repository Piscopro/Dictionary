import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class DictionaryManagement {
    private static Dictionary dictionary;

    public DictionaryManagement() {
        dictionary = new Dictionary();
    }


    public Dictionary getDictionary() {
        return dictionary;
    }

    public static void insertFromFile(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    String word_target = parts[0];
                    String word_explain = parts[1];
                    Word newWord = new Word(word_target, word_explain);
                    dictionary.addWord(newWord);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dictionaryLookup(String word) {
        for (Word entry : dictionary.getWords()) {
            if (entry.getWordTarget().equalsIgnoreCase(word)) {
                System.out.println("Definition: " + entry.getWordExplain());
                return;
            }
        }
        System.out.println("Word not found in the dictionary.");
    }

    public void addWord(String word_target, String word_explain) {
        Word newWord = new Word(word_target, word_explain);
        dictionary.addWord(newWord);
    }

    public void editWord(String word_target, String newExplain) {
        for (Word entry : dictionary.getWords()) {
            if (entry.getWordTarget().equalsIgnoreCase(word_target)) {
                entry.word_explain = newExplain;
                System.out.println("Definition updated.");
                //exportToFile("dictionaries.txt");
                return;
            }
        }
        System.out.println("Word not found in the dictionary.");
    }

    public void deleteWord(String word_target) {
        for (Word entry : dictionary.getWords()) {
            if (entry.getWordTarget().equalsIgnoreCase(word_target)) {
                dictionary.getWords().remove(entry);
                System.out.println("Word deleted from the dictionary.");
                //exportToFile("dictionaries.txt");
                return;
            }
        }
        System.out.println("Word not found in the dictionary.");
    }

    public ArrayList<Word> dictionarySearcher(String prefix) {
        ArrayList<Word> searchResults = new ArrayList<>();
        for (Word entry : dictionary.getWords()) {
            if (entry.getWordTarget().toLowerCase().startsWith(prefix.toLowerCase())) {
                searchResults.add(entry);
            }
        }
        return searchResults;
    }

    public static void dictionaryExportToFile(String fileName) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
            for (Word entry : dictionary.getWords()) {
                bw.write(entry.getWordTarget() + "\t" + entry.getWordExplain());
                bw.newLine();
            }
            bw.close();
            System.out.println("Data exported to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportToFile(String fileName) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
            for (Word entry : dictionary.getWords()) {
                bw.write(entry.getWordTarget() + "\t" + entry.getWordExplain());
                bw.newLine();
            }
            bw.close();
            System.out.println("Data exported to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
