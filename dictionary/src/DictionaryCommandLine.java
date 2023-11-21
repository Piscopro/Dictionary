

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

class DictionaryCommandLine {
    private DictionaryManagement dictionaryManagement = new DictionaryManagement();
    private static final String QUESTIONS_FILE = "questions.txt";

    public DictionaryCommandLine() {
    }

    public void showAllWords() {
        ArrayList<Word> words = this.dictionaryManagement.getDictionary().getWords();
        System.out.println("No | English | Vietnamese");

        for(int i = 0; i < words.size(); ++i) {
            Word word = (Word)words.get(i);
            System.out.printf("%-5d| %-20s| %s%n", i + 1, word.getWordTarget(), word.getWordExplain());
        }

    }

    public void insertFromCommandline() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập số lượng từ vựng: ");
        int numberOfWords = scanner.nextInt();
        scanner.nextLine();

        for(int i = 0; i < numberOfWords; ++i) {
            System.out.print("Nhập từ tiếng Anh: ");
            String word_target = scanner.nextLine();
            System.out.print("Nhập giải thích bằng tiếng Việt: ");
            String word_explain = scanner.nextLine();
            this.dictionaryManagement.addWord(word_target, word_explain);
        }

        System.out.println("Sucess!");
    }

    public void dictionaryBasic() {
        this.insertFromCommandline();
        this.showAllWords();
    }

    private void readQuestionsFromFile(ArrayList<String[]> questions) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("src/main/questions.txt"));
        String line;

        while ((line = br.readLine()) != null) {
            String[] parts = line.split(";");
            if (parts.length == 6) {
                questions.add(parts);
            }
        }

        br.close();
    }

    public void startGame() {
        ArrayList<String[]> questions = new ArrayList<>();
        try {
            readQuestionsFromFile(questions);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading questions from file.");
            return;
        }
        System.out.println("Enter the number of question you want to play: ");
        Scanner sc = new Scanner(System.in);
        int questionNumber = sc.nextInt();

        int index = 0;
        int userScore = 0;
        while (index < questionNumber) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(questions.size());
            String[] randomQuestion = questions.get(randomIndex);
            System.out.println("Question " + (index + 1) + ": " + randomQuestion[0]);
            for (int j = 0; j < 4; j++) {
                System.out.println("[" + ((char) (65 + j)) + "] " + randomQuestion[j + 1]);
            }
            System.out.println("Your choice [A/B/C/D]: ");
            String answer = sc.next();
            if (answer.equalsIgnoreCase(randomQuestion[5])) {
                userScore += 1;
                System.out.println("Correct! Your points: " + userScore + "/" + questionNumber);
            } else {
                System.out.println("Incorrect! The correct answer is: " + randomQuestion[5]);
                System.out.println("Your points: " + userScore + "/" + questionNumber);
            }
            questions.remove(randomIndex);
            index++;
        }

    }

    public void dictionaryAdvanced() {
        Scanner scanner = new Scanner(System.in);

        label32:
        while(true) {
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
            scanner.nextLine();
            DictionaryManagement var10000;
            switch (choice) {
                case 0:
                    System.out.println("Goodbye!");
                    return;
                case 1:
                    this.insertFromCommandline();
                    break;
                case 2:
                    System.out.print("Enter the word to remove: ");
                    String wordToRemove = scanner.nextLine();
                    this.dictionaryManagement.deleteWord(wordToRemove);
                    break;
                case 3:
                    System.out.print("Enter the word to update: ");
                    String wordToUpdate = scanner.nextLine();
                    System.out.print("Enter the new meaning in Vietnamese: ");
                    String newExplain = scanner.nextLine();
                    this.dictionaryManagement.editWord(wordToUpdate, newExplain);
                    break;
                case 4:
                    this.showAllWords();
                    break;
                case 5:
                    System.out.print("Enter a word to look up: ");
                    String wordToLookup = scanner.nextLine();
                    this.dictionaryManagement.dictionaryLookup(wordToLookup);
                    break;
                case 6:
                    System.out.print("Enter a prefix to search for: ");
                    String prefix = scanner.nextLine();
                    ArrayList<Word> searchResults = this.dictionaryManagement.dictionarySearcher(prefix);
                    if (searchResults.size() > 0) {
                        System.out.println("Words that start with \"" + prefix + "\":");
                        Iterator var13 = searchResults.iterator();

                        while(true) {
                            if (!var13.hasNext()) {
                                continue label32;
                            }

                            Word entry = (Word)var13.next();
                            PrintStream var12 = System.out;
                            String var10001 = entry.getWordTarget();
                            var12.println(var10001 + " Definition: " + entry.getWordExplain());
                        }
                    }

                    System.out.println("No words found.");
                    break;
                case 7:
                    this.startGame();
                    break;
                case 8:
                    String importFilePath = "dictionaries.txt";
                    var10000 = this.dictionaryManagement;
                    DictionaryManagement.insertFromFile(importFilePath);
                    break;
                case 9:
                    String exportFilePath = "dictionaries.txt";
                    var10000 = this.dictionaryManagement;
                    DictionaryManagement.dictionaryExportToFile(exportFilePath);
                    break;
                default:
                    System.out.println("Action not supported.");
            }
        }
    }
}
