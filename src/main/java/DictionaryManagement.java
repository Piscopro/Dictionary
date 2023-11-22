import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

class DictionaryManagement {
    private static Dictionary dictionary;

    public DictionaryManagement() {
        dictionary = new Dictionary();
    }

    public static void insertFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            Word currentWord = null;
            Meaning currentMeaning = null;
            StringBuilder descriptionBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("@")) {
                    // Xử lý kết thúc của Word hiện tại
                    if (currentMeaning != null && descriptionBuilder.length() > 0) {
                        currentMeaning.setDescription(descriptionBuilder.toString().trim());
                        descriptionBuilder.setLength(0);
                    }
                    currentMeaning = null;

                    // Tạo Word mới
                    String[] parts = line.split(" /");
                    if (parts.length < 2) {
                        System.out.println("Dữ liệu không đúng định dạng tại dòng: " + line);
                        continue;
                    }
                    String word = parts[0].substring(1).trim();
                    String pronunciation = "/" + parts[1].trim();
                    currentWord = new Word(word, pronunciation);
                    dictionary.addWord(currentWord);
                } else if (currentWord != null && line.startsWith("*")) {
                    // Kết thúc mô tả Meaning hiện tại
                    if (currentMeaning != null) {
                        currentMeaning.setDescription(descriptionBuilder.toString().trim());
                        descriptionBuilder.setLength(0);
                    }

                    // Tạo Meaning mới
                    String partOfSpeech = line.substring(1).trim();
                    currentMeaning = new Meaning(partOfSpeech, "");
                    currentWord.addMeaning(currentMeaning);
                } else if (currentMeaning != null) {
                    // Thêm dữ liệu vào mô tả
                    descriptionBuilder.append(line).append(" ");
                }
            }

            // Xử lý mô tả cuối cùng
            if (currentMeaning != null && descriptionBuilder.length() > 0) {
                currentMeaning.setDescription(descriptionBuilder.toString().trim());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void dictionaryExportToFile(String fileName) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
            Iterator<Word> iterator = dictionary.getWords().iterator();

            while (iterator.hasNext()) {
                Word entry = iterator.next();
                // Xuất từ và phiên âm
                bw.write("@" + entry.getWordTarget() + " /" + entry.getPronunciation() + "/\n");

                // Xuất mỗi Meaning
                for (Meaning meaning : entry.getMeanings()) {
                    bw.write("*  " + meaning.getPartOfSpeech() + "\n");
                    bw.write("  " + meaning.getDescription() + "\n");
                }

                // Thêm một dòng trống giữa các từ
                bw.newLine();
            }

            bw.close();
            System.out.println("Data exported to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void dictionaryLookup(String word) {
        Iterator<Word> iterator = dictionary.getWords().iterator();

        while (iterator.hasNext()) {
            Word entry = iterator.next();

            if (entry.getWordTarget().equalsIgnoreCase(word)) {
                System.out.println("Word: " + entry.getWordTarget());
                System.out.println("Pronunciation: " + entry.getPronunciation());

                for (Meaning meaning : entry.getMeanings()) {
                    System.out.println("Part of Speech: " + meaning.getPartOfSpeech());
                    System.out.println("Description: " + meaning.getDescription());
                }

                return; // Kết thúc phương thức sau khi đã tìm thấy và in từ
            }
        }

        // Nếu không tìm thấy từ
        System.out.println("Word not found in the dictionary.");
    }

    public void addWord(String word_target, String pronunciation, String partOfSpeech, String description) {
        Word newWord = new Word(word_target, pronunciation);
        dictionary.addWord(newWord);
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

    public void editWord(String word_target, String newPronunciation, String newPartOfSpeech, String newDescription) {
        Iterator<Word> iterator = dictionary.getWords().iterator();

        while (iterator.hasNext()) {
            Word entry = iterator.next();

            if (entry.getWordTarget().equalsIgnoreCase(word_target)) {
                entry.setPronunciation(newPronunciation); // Cập nhật phiên âm

                boolean foundMeaning = false;
                for (Meaning meaning : entry.getMeanings()) {
                    if (meaning.getPartOfSpeech().equalsIgnoreCase(newPartOfSpeech)) {
                        meaning.setDescription(newDescription);
                        foundMeaning = true;
                        break;
                    }
                }

                if (!foundMeaning) {
                    // Nếu không tìm thấy Meaning phù hợp, thêm Meaning mới
                    entry.addMeaning(new Meaning(newPartOfSpeech, newDescription));
                }

                System.out.println("Word, pronunciation, and meanings updated.");
                return;
            }
        }

        // Nếu không tìm thấy từ
        System.out.println("Word not found in the dictionary.");
    }

    public void deleteWord(String wordTarget) {
        Iterator<Word> iterator = dictionary.getWords().iterator();

        while (iterator.hasNext()) {
            Word entry = iterator.next();

            if (entry.getWordTarget().equalsIgnoreCase(wordTarget)) {
                iterator.remove();
                System.out.println("Word deleted from the dictionary.");
                return;
            }
        }

        System.out.println("Word not found in the dictionary.");
    }

    public ArrayList<Word> dictionarySearcher(String prefix) {
        ArrayList<Word> searchResults = new ArrayList<>();
        Iterator<Word> iterator = dictionary.getWords().iterator();

        while (iterator.hasNext()) {
            Word entry = iterator.next();
            if (entry.getWordTarget().toLowerCase().startsWith(prefix.toLowerCase())) {
                searchResults.add(entry);
            }
        }

        return searchResults;
    }

    public void addWord(Word newWord) {
        dictionary.addWord(newWord);
    }

}
