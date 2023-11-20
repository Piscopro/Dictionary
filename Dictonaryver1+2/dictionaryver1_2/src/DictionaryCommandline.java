
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class DictionaryCommandLine {
    private DictionaryManagement dictionaryManagement;

    public DictionaryCommandLine() {
        dictionaryManagement = new DictionaryManagement();
    }

    public void showAllWords() {
        ArrayList<Word> words = dictionaryManagement.getDictionary().getWords();
        System.out.println("No | English | Vietnamese");
        for (int i = 0; i < words.size(); i++) {
            Word word = words.get(i);
            System.out.printf("%-3d| %-8s| %s%n", i + 1, word.getWordTarget(), word.getWordExplain());
        }
    }

    public void insertFromCommandline() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nhập số lượng từ vựng: ");
        int numberOfWords = scanner.nextInt();
        scanner.nextLine(); // Đọc ký tự Enter sau số lượng từ vựng

        for (int i = 0; i < numberOfWords; i++) {
            System.out.print("Nhập từ tiếng Anh: ");
            String word_target = scanner.nextLine();

            System.out.print("Nhập giải thích bằng tiếng Việt: ");
            String word_explain = scanner.nextLine();

            dictionaryManagement.addWord(word_target, word_explain);
        }
        System.out.println("Sucess!");
    }

    public void dictionaryBasic() {
        insertFromCommandline();
        showAllWords();
    }

    private static final String QUESTIONS_FILE = "questions.txt";

    public void startGame() {
        try {
            // Đọc danh sách các câu hỏi từ tệp "questions.txt"
            ArrayList<String> questions = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(QUESTIONS_FILE));
            String line;
            while ((line = br.readLine()) != null) {
                questions.add(line);
            }
            br.close();

            // Chọn một câu hỏi ngẫu nhiên từ danh sách
            Random rand = new Random();
            int randomIndex = rand.nextInt(questions.size());
            String randomQuestion = questions.get(randomIndex);

            // Hiển thị câu hỏi và lựa chọn
            System.out.println(randomQuestion);
            String answer = randomQuestion.split("Answer: ")[1];

            // Yêu cầu người chơi chọn câu trả lời
            Scanner scanner = new Scanner(System.in);
            System.out.print("Your choice [A/B/C/D]: ");
            String playerAnswer = scanner.nextLine().toUpperCase();

            // Kiểm tra câu trả lời của người chơi
            if (playerAnswer.equals(answer)) {
                System.out.println("Correct!");
            } else {
                System.out.println("Incorrect. The correct answer is: " + answer);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading questions from file.");
        }
    }

    public void dictionaryAdvanced() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to My Application!");
            System.out.println("[0] Exit");
            System.out.println("[1] Add");
            System.out.println("[2] Remove");
            System.out.println("[3] Update");
            System.out.println("[4] Display");
            System.out.println("[5] Lookup");
            System.out.println("[6] Search");
            System.out.println("[7] Game");
            System.out.println("[8] Import from file");
            System.out.println("[9] Export to file");

            System.out.print("Your action: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 0:
                    System.out.println("Goodbye!");
                    return;
                case 1:
                    /*
                    System.out.print("Nhập số lượng từ vựng: ");
                    int numberOfWords = scanner.nextInt();
                    scanner.nextLine(); // Đọc ký tự Enter sau số lượng từ vựng

                    for (int i = 0; i < numberOfWords; i++) {
                        System.out.print("Nhập từ tiếng Anh: ");
                        String word_target = scanner.nextLine();

                        System.out.print("Nhập giải thích bằng tiếng Việt: ");
                        String word_explain = scanner.nextLine();

                        Word word = new Word(word_target, word_explain);

                        dictionaryManagement.addWord(word_target, word_explain);
                    }
                    System.out.println("Word added to the dictionary.");
                    break;
                    */
                    insertFromCommandline();
                    break;

                case 2:
                    System.out.print("Enter the word to remove: ");
                    String wordToRemove = scanner.nextLine();
                    dictionaryManagement.deleteWord(wordToRemove);
                    break;
                case 3:
                    System.out.print("Enter the word to update: ");
                    String wordToUpdate = scanner.nextLine();
                    System.out.print("Enter the new meaning in Vietnamese: ");
                    String newExplain = scanner.nextLine();
                    dictionaryManagement.editWord(wordToUpdate, newExplain);
                    break;
                case 4:
                    showAllWords();
                    break;
                case 5:
                    // Read data from dictionaries.txt before looking up
                    //dictionaryManagement.insertFromFile("dictionaries.txt");

                    System.out.print("Enter a word to look up: ");
                    String wordToLookup = scanner.nextLine();
                    dictionaryManagement.dictionaryLookup(wordToLookup);
                    break;

                case 6:
                    //dictionaryManagement.exportFromFile("dictionaries.txt");
                    System.out.print("Enter a prefix to search for: ");
                    String prefix = scanner.nextLine();
                    ArrayList<Word> searchResults = dictionaryManagement.dictionarySearcher(prefix);
                    if (searchResults.size() > 0) {
                        System.out.println("Words that start with \"" + prefix + "\":");
                        for (Word entry : searchResults) {
                            System.out.println(entry.getWordTarget() + " Definition: " + entry.getWordExplain());
                        }
                    } else {
                        System.out.println("No words found.");
                    }
                    break;
                case 7:
                    startGame();
                    //tự làm nhé k biết làm đâu
                    break;
                case 8:
                    String importFilePath = "dictionaries.txt";
                    dictionaryManagement.insertFromFile(importFilePath);
                    break;

                case 9:
                    String exportFilePath = "dictionaries.txt";
                    dictionaryManagement.dictionaryExportToFile(exportFilePath);
                    break;

                default:
                    System.out.println("Action not supported.");
                    break;
            }
        }
    }
}

