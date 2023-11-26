package javas;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.Collections;

import static javas.DictionaryManagement.*;

class DictionaryCommandLine {
    private static final String QUESTIONS_FILE = "src/main/resources/text/questions.txt";
    private DictionaryManagement dictionaryManagement = new DictionaryManagement();

    private Dictionary dictionary = new Dictionary();

    public DictionaryCommandLine() {
    }

    public void insertFromCommandline() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập số lượng từ vựng: ");
        int numberOfWords = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numberOfWords; i++) {
            System.out.print("Nhập từ tiếng Anh: ");
            String wordTarget = scanner.nextLine();

            System.out.print("Nhập phát âm: ");
            String pronunciation = scanner.nextLine();

            // Tạo một đối tượng Word mới
            Word newWord = new Word(wordTarget, pronunciation);

            System.out.print("Nhập số lượng nghĩa của từ: ");
            int numberOfMeanings = scanner.nextInt();
            scanner.nextLine();

            for (int j = 0; j < numberOfMeanings; j++) {
                System.out.print("Nhập loại từ (phần cú pháp) cho nghĩa " + (j + 1) + ": ");
                String partOfSpeech = scanner.nextLine();

                System.out.println("Nhập mô tả nghĩa cho nghĩa " + (j + 1) + ": ");
                StringBuilder meaningInput = new StringBuilder();

                // Read multiple lines for the meaning
                while (true) {
                    String line = scanner.nextLine();
                    if (line.isEmpty()) {
                        break;
                    }
                    meaningInput.append(line).append("\n");
                }

                // Remove trailing newline character
                String meaningText = meaningInput.toString().trim();

                // Thêm mỗi nghĩa vào đối tượng Word
                newWord.addMeaning(new Meaning(partOfSpeech, meaningText));
            }

            // Thêm từ vào từ điển
            this.dictionaryManagement.addWord(newWord);
        }

        System.out.println("Success!");
    }


//    public void dictionaryBasic() {
//        this.insertFromCommandline();
//        this.showAllWords();
//    }

    private void readQuestionsFromFile(ArrayList<String[]> questions) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(QUESTIONS_FILE));
        String line;

        while ((line = br.readLine()) != null) {
            String[] parts = line.split(";");
            if (parts.length == 6) {
                questions.add(parts);
            }
        }

        br.close();
    }

//    public void startGame() {
//        ArrayList<String[]> questions = new ArrayList<>();
//        try {
//            readQuestionsFromFile(questions);
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("Error reading questions from file.");
//            return;
//        }
//        System.out.println("Enter the number of question you want to play: ");
//        Scanner sc = new Scanner(System.in);
//        int questionNumber = sc.nextInt();
//
//        int index = 0;
//        int userScore = 0;
//        while (index < questionNumber) {
//            Random rand = new Random();
//            int randomIndex = rand.nextInt(questions.size());
//            String[] randomQuestion = questions.get(randomIndex);
//            System.out.println("Question " + (index + 1) + ": " + randomQuestion[0]);
//            for (int j = 0; j < 4; j++) {
//                System.out.println("[" + ((char) (65 + j)) + "] " + randomQuestion[j + 1]);
//            }
//            System.out.println("Your choice [A/B/C/D]: ");
//            String answer = sc.next();
//            if (answer.equalsIgnoreCase(randomQuestion[5])) {
//                userScore += 1;
//                System.out.println("Correct! Your points: " + userScore + "/" + questionNumber);
//            } else {
//                System.out.println("Incorrect! The correct answer is: " + randomQuestion[5]);
//                System.out.println("Your points: " + userScore + "/" + questionNumber);
//            }
//            questions.remove(randomIndex);
//            index++;
//        }
//
//    }
    public void startGame() {
        dictionaryManagement.insertGameQuestion();
        int questionNumber = 3;
        int i = 0;
        while (i < questionNumber) {
            GameQuestion randomQuestion = dictionaryManagement.getRandomQuestion();
            System.out.println(randomQuestion.getQuestion());
//            for (int j = 0; j < randomQuestion.getAnswers().size(); j++) {
//                System.out.println("[" + ((char) (65 + j)) + "] " + randomQuestion.getAnswers().get(j));
//            }
//            System.out.println("Your choice [A/B/C/D]: ");
//            String answer = scanner.next();
//            if (answer.equalsIgnoreCase(randomQuestion.getCorrectAnswer())) {
//                System.out.println("Correct!");
//            } else {
//                System.out.println("Incorrect! The correct answer is: " + randomQuestion.getCorrectAnswer());
//            }
//            dictionary.removeQuestion(randomQuestion);
            System.out.println(randomQuestion.getCorrectAnswer());
            i++;
        }
    }

    public void dictionaryAdvanced() throws IOException {
        Scanner scanner = new Scanner(System.in);
        DictionaryManagement.insertFromFile();
        label32:
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
            System.out.println("[10] History");
            System.out.println("[11] Favourites / My Words");
            System.out.println("[12] Speak");
            System.out.println("[13] Translate sentence");
            System.out.print("Your action: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            DictionaryManagement var10000;
            switch (choice) {
                case 0:
                    System.out.println("Goodbye!");
                    return;
                case 1:
                    insertFromCommandline();
                    break;
                case 2:
                    DictionaryManagement.deleteWord();
                    break;
                case 3:
                    DictionaryManagement.editWord();
                    break;
                case 4:
                    for (Word word : DictionaryManagement.showAllWords()) {
                        System.out.println(word.getWordTarget()
                                + " " + word.getPronunciation()
                                + "\n" + word.getMeaning());
                    }
                    break;
                case 5:
//                    System.out.println("nhập vào:");
//                    String s = scanner.nextLine();
////                    DictionaryManagement.dictionaryLookup(s);
//                    System.out.println(DictionaryManagement.dictionaryLookup(s).getWordTarget()
//                            + "\n" + DictionaryManagement.dictionaryLookup(s).getMeaning());
                    ArrayList<Word> aaa = DictionaryManagement.showAllWords();
                    System.out.println(aaa.size());
                    break;
                case 6:
//                    DictionaryManagement.BoxSearchPrefix();
                    break;
                case 7:
                    startGame();
                    break;
                case 8:
                    DictionaryManagement.insertFromFile();
                    break;
                case 9:
                    DictionaryManagement.dictionaryExportToFile();
                    break;
                case 10:
                    do {
                        System.out.println("Menu:");
                        System.out.println("1. Export history to file");
                        System.out.println("2. Import history from file");
                        System.out.println("3. Show full history");
                        System.out.println("4. Show 5 recent history");
                        //System.out.println("5. Add word to history");
                        System.out.println("0. Exit");
                        System.out.print("Enter your choice: ");

                        int Choice = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character

                        switch (Choice) {
                            case 1:
                                historyExportToFile();
                                break;
                            case 2:
                                historyFromFile();
                                break;
                            case 3:
                                ArrayList<Word> fullHistory = DictionaryManagement.showFullHistory();
                                for (Word word : fullHistory) {
                                    System.out.println(word.getWordTarget() + "\n" + word.getMeaning());
                                }
                                break;
                            case 4:
                                ArrayList<Word> recentHistory = DictionaryManagement.show5RecentHistory();
                                for (Word word : recentHistory) {
                                    System.out.println(word.getWordTarget() + "\n" + word.getMeaning());
                                }
                                break;
                            case 5:
                                //addWordToHistory();
                                break;
                            case 0:
                                System.out.println("Exiting the program. Goodbye!");
                                break;
                            default:
                                System.out.println("Invalid choice. Please enter a valid option.");
                        }
                    }
                    while (choice != 0);
                    break;
                case 11:
//                    DictionaryManagement.favourite();
                    break;
                case 12:
                    System.out.print("Enter the word");
                    String word = scanner.nextLine();
                    DictionaryManagement.speak(word);
                    break;
                case 13:
//                    DictionaryManagement.translateSentence();
                    break;
                default:
                    System.out.println("Action not supported.");
            }
        }
    }
}