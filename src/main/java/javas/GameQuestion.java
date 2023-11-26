package javas;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameQuestion {
    private String question;
    private ArrayList<String> answers = new ArrayList<>();
    private String correctAnswer;

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}