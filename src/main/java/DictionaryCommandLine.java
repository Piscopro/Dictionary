import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.Collections;

class DictionaryCommandLine {
    private static final String QUESTIONS_FILE = "questions.txt";
    private DictionaryManagement dictionaryManagement = new DictionaryManagement();

    public DictionaryCommandLine() {
    }

    /*public void showAllWords() {
        ArrayList<Word> words = this.dictionaryManagement.getDictionary().getWords();
        System.out.println("No   | English             | Pronunciation       | VietNamese");

        for(int i = 0; i < words.size(); ++i) {
            Word word = (Word)words.get(i);
            System.out.printf("%-5d| %-20s| %-20s| %s%n", i + 1, word.getWordTarget(), word.getPronunciation(), word.getWordExplain());
        }

    }*/

    public void showAllWords() {
        ArrayList<Word> words = this.dictionaryManagement.getDictionary().getWords();

        // Sort the words alphabetically
        Collections.sort(words, (w1, w2) -> w1.getWordTarget().compareToIgnoreCase(w2.getWordTarget()));

        for (Word word : words) {
            // Print word and pronunciation
            System.out.println(word.getWordTarget() + " " + word.getPronunciation());

            // Loop through and print each meaning of the word
            for (Meaning meaning : word.getMeanings()) {
                System.out.println("*  " + meaning.getPartOfSpeech());

                // Split the description into lines based on the dash '-'
                String[] descriptionLines = meaning.getDescription().split("\\s*-\\s*");

                // Print each line of the description without the leading empty line
                boolean isFirstLine = true;
                for (String line : descriptionLines) {
                    if (!isFirstLine) {
                        System.out.println("   - " + line.trim());
                    } else {
                        isFirstLine = false;
                    }
                }
            }

            System.out.println();
        }
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
                    this.dictionaryManagement.deleteWord();
                    break;
                case 3:
                    this.dictionaryManagement.editWord();
                    break;
                case 4:
                    this.showAllWords();
                    break;
                case 5:
                    this.dictionaryManagement.dictionaryLookup();
                    break;
                case 6:
                    DictionaryManagement.BoxSearchPrefix();
                    break;
                case 7:
                    this.startGame();
                    break;
                case 8:
                    DictionaryManagement.insertFromFile();
                    break;
                case 9:
                    DictionaryManagement.dictionaryExportToFile();
                    break;
                case 10:
                    dictionaryManagement.showSearchHistory();
                    break;
                case 11:
                    DictionaryManagement.Favourite();
                   break;
                case 12:
                    DictionaryManagement.Speak();
                    break;
                default:
                    System.out.println("Action not supported.");
            }
        }
    }
}
