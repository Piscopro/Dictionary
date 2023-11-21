
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

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
            while((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    String word_target = parts[0];
                    String word_explain = parts[1];
                    Word newWord = new Word(word_target, word_explain);
                    dictionary.addWord(newWord);
                }
            }

            br.close();
        } catch (IOException var7) {
            var7.printStackTrace();
        }

    }

    public void dictionaryLookup(String word) {
        Iterator var2 = dictionary.getWords().iterator();

        Word entry;
        do {
            if (!var2.hasNext()) {
                System.out.println("Word not found in the dictionary.");
                return;
            }

            entry = (Word)var2.next();
        } while(!entry.getWordTarget().equalsIgnoreCase(word));

        System.out.println("Definition: " + entry.getWordExplain());
    }

    public void addWord(String word_target, String word_explain) {
        Word newWord = new Word(word_target, word_explain);
        dictionary.addWord(newWord);
    }

    public void editWord(String word_target, String newExplain) {
        Iterator var3 = dictionary.getWords().iterator();

        Word entry;
        do {
            if (!var3.hasNext()) {
                System.out.println("Word not found in the dictionary.");
                return;
            }

            entry = (Word)var3.next();
        } while(!entry.getWordTarget().equalsIgnoreCase(word_target));

        entry.word_explain = newExplain;
        System.out.println("Definition updated.");
    }

    public void deleteWord(String word_target) {
        Iterator var2 = dictionary.getWords().iterator();

        Word entry;
        do {
            if (!var2.hasNext()) {
                System.out.println("Word not found in the dictionary.");
                return;
            }

            entry = (Word)var2.next();
        } while(!entry.getWordTarget().equalsIgnoreCase(word_target));

        dictionary.getWords().remove(entry);
        System.out.println("Word deleted from the dictionary.");
    }

    public ArrayList<Word> dictionarySearcher(String prefix) {
        ArrayList<Word> searchResults = new ArrayList();
        Iterator var3 = dictionary.getWords().iterator();

        while(var3.hasNext()) {
            Word entry = (Word)var3.next();
            if (entry.getWordTarget().toLowerCase().startsWith(prefix.toLowerCase())) {
                searchResults.add(entry);
            }
        }

        return searchResults;
    }

    public static void dictionaryExportToFile(String fileName) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
            Iterator var2 = dictionary.getWords().iterator();

            while(var2.hasNext()) {
                Word entry = (Word)var2.next();
                String var10001 = entry.getWordTarget();
                bw.write(var10001 + "\t" + entry.getWordExplain());
                bw.newLine();
            }

            bw.close();
            System.out.println("Data exported to " + fileName);
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

    private void exportToFile(String fileName) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
            Iterator var3 = dictionary.getWords().iterator();

            while(var3.hasNext()) {
                Word entry = (Word)var3.next();
                String var10001 = entry.getWordTarget();
                bw.write(var10001 + "\t" + entry.getWordExplain());
                bw.newLine();
            }

            bw.close();
            System.out.println("Data exported to " + fileName);
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }
}
