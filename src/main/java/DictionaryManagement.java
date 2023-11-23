import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
        // Sắp xếp danh sách từ điển nếu chưa được sắp xếp
        ArrayList<Word> words = dictionary.getWords();
        if (!isDictionarySorted(words)) {
            sortDictionary(words);
        }

        int index = binarySearch(words, word);

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

    private boolean isDictionarySorted(ArrayList<Word> words) {
        for (int i = 1; i < words.size(); i++) {
            if (words.get(i - 1).getWordTarget().compareTo(words.get(i).getWordTarget()) > 0) {
                return false;
            }
        }
        return true;
    }

    private void sortDictionary(ArrayList<Word> words) {
        Collections.sort(words, Comparator.comparing(Word::getWordTarget));
    }

    private int binarySearch(ArrayList<Word> words, String target) {
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

    /*public ArrayList<Word> dictionarySearcher(String prefix) {
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
     */
    //tối đa 5 từ
    public ArrayList<Word> dictionarySearcher(String prefix) {
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

    public String showWordFirstMeaning(Word word) {
        return word.getWordTarget() + "\n" + word.getFirstMeaning();
    }

    public void showSearchHistory(String op) {
        ArrayList<Word> history = new ArrayList<>(dictionary.getSearchHistory());

        if (op.equals("full")) {
            for (int i = history.size() - 1; i >= 0; i--) {
                System.out.println(showWordFirstMeaning(history.get(i)));
            }
        } else if (op.equals("search")) {
            if (history.size() > 5) {
                for (int i = history.size() - 1; i >= history.size() - 5; i--) {
                    System.out.println(showWordFirstMeaning(history.get(i)));
                }
            } else {
                for (int i = history.size() - 1; i >= 0; i--) {
                    System.out.println(showWordFirstMeaning(history.get(i)));
                }
            }
        }
    }

    public void showFavourite() {
        ArrayList<Word> favourites = new ArrayList<>(dictionary.getFavouriteWords());
        for (int i = favourites.size() - 1; i >= 0; i--) {
            System.out.println(showWordFirstMeaning(favourites.get(i)));
        }
    }

    public void translateSentence() throws IOException {
        Scanner sc = new Scanner(System.in);

        System.out.print("Choose language: 1. Eng-Vie, 2. Vie-Eng ");
        int choice = sc.nextInt();
        String sl, tl;
        if (choice == 1) {
            System.out.println("Eng-Vie:");
            sl = "en";
            tl = "vi";
        } else {
            System.out.println("Vie-Eng:");
            sl = "vi";
            tl = "en";
        }

        sc.nextLine(); // Consume newline left-over
        System.out.print("Enter the sentence to be translated: ");
        String query = sc.nextLine();

        query = URLEncoder.encode(query, StandardCharsets.UTF_8);

        String urlStr = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=" + sl + "&tl=" + tl + "&dt=t&q=" + query;
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
        System.out.println("Translated sentenced: " + translatedText);
    }

    public void addFavourite(Word word) {
        dictionary.addFavourite(word);
    }

    public void deleteFavourite(Word word) {
        dictionary.deleteFavourite(word);
    }
}