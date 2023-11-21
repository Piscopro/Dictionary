
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashSet;

class DictionaryManagement {
    private static Dictionary dictionary;

    public DictionaryManagement() {
        dictionary = new Dictionary();
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    /*public static void insertFromFile(String fileName) {
        HashSet<Word> wordSet = new HashSet<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    String word_target = parts[0];
                    String word_explain = parts[1];
                    Word newWord = new Word(word_target, word_explain);

                    // Thêm từ mới vào HashSet, nếu từ đó chưa tồn tại trong HashSet
                    wordSet.add(newWord);
                }
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Cập nhật từ điển với các từ trong HashSet
        for (Word word : wordSet) {
            dictionary.addWord(word);
        }
    }*/

    public static void insertFromFile(String fileName) {
        HashSet<Word> wordSet = new HashSet<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 3) {  // Thay đổi từ 2 thành 3
                    String word_target = parts[0];
                    String word_explain = parts[2];
                    String pronunciation = parts[1];  // Thêm pronunciation từ file vào

                    Word newWord = new Word(word_target, pronunciation, word_explain);
                    //newWord.setPronunciation(pronunciation);

                    wordSet.add(newWord);
                }
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Word word : wordSet) {
            dictionary.addWord(word);
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

        System.out.println("Pronunciation: " + " " + entry.getPronunciation() + "          Definition: " + " " + entry.getWordExplain());
    }

    public void addWord(String word_target, String pronunciation, String word_explain) {
        Word newWord = new Word(word_target, pronunciation, word_explain);
        dictionary.addWord(newWord);
    }

    /*public void editWord(String word_target, String newExplain) {
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
    }*/
    public void editWord(String word_target, String newPronunciation ,String newExplain) {
        Iterator var3 = dictionary.getWords().iterator();

        Word entry;
        do {
            if (!var3.hasNext()) {
                System.out.println("Word not found in the dictionary.");
                return;
            }

            entry = (Word)var3.next();
        } while(!entry.getWordTarget().equalsIgnoreCase(word_target));

        entry.setWord_explain(newExplain);
        entry.setPronunciation(newPronunciation);  // Thêm dòng này để cập nhật pronunciation
        System.out.println("Definition and pronunciation updated.");
    }


    /*public void deleteWord(String word_target) {
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
    }*/

    public void deleteWord(String word_target) {
        Iterator<Word> iterator = dictionary.getWords().iterator();

        while (iterator.hasNext()) {
            Word entry = iterator.next();

            if (entry.getWordTarget().equalsIgnoreCase(word_target)) {
                iterator.remove();
                System.out.println("Word deleted from the dictionary.");
                return;
            }
        }

        System.out.println("Word not found in the dictionary.");
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



    /*public static void dictionaryExportToFile(String fileName) {
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

    }*/

    public static void dictionaryExportToFile(String fileName) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
            Iterator var2 = dictionary.getWords().iterator();

            while(var2.hasNext()) {
                Word entry = (Word)var2.next();
                String var10001 = entry.getWordTarget();
                bw.write(var10001 + "\t" + entry.getPronunciation() + "\t" + entry.getWordExplain());  // Thêm pronunciation vào file
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
                bw.write(var10001 + "\t" + entry.getPronunciation() + "\t" + entry.getWordExplain());
                bw.newLine();
            }

            bw.close();
            System.out.println("Data exported to " + fileName);
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }
}
