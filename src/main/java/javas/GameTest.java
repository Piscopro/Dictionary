package javas;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

public class GameTest {
    private MainController controller;
    private Integer questionNumber;
    private Integer questionIndex;
    private Integer rightAnswers;
    public GameTest(MainController controller, Integer questionsNumber) {
        this.controller = controller;
        this.questionNumber = questionsNumber;
        questionIndex = 0;
        rightAnswers = 0;
        displayStatistic();
        renderNewQuestion();
    }

    public void renderNewQuestion() {
        GameQuestion newQuestion = DictionaryManagement.getRandomQuestion();
        controller.questiondisplay.setText(newQuestion.getQuestion());
        controller.testpane.getChildren().removeIf(node -> node instanceof AnswerButton);
        Integer count = 0;
        System.out.println(newQuestion.getCorrectAnswer());
        for (String answer : newQuestion.getAnswers()) {
            System.out.println(answer);
            if(answer.equals(newQuestion.getCorrectAnswer())){
                controller.testpane.getChildren().add(new AnswerButton(controller, true, answer, this, count));
            }
            else{
                controller.testpane.getChildren().add(new AnswerButton(controller, false, answer, this, count));
            }
            count++;
        }
    }
    public void answeredRight(){
        questionIndex++;
        rightAnswers++;
        displayStatistic();
        displayNextPane("Đúng rồi!");
    }
    public void answeredWrong(){
        questionIndex++;
        displayStatistic();
        displayNextPane("Sai mất rồi!");
    }
    public void displayStatistic(){
        controller.questionindexdisplay.setText("Số câu đã trả lời: " + questionIndex + "/" + questionNumber);
        controller.rightanswersdisplay.setText("Số câu đúng: " + rightAnswers);

    }
    public void displayNextPane(String text){
        Region overlayPane = new Region();
        overlayPane.setStyle("-fx-background-color: rgba(192, 192, 192, 0.5);"); // rgba defines color with alpha (transparency)
        overlayPane.setPrefHeight(565);
        overlayPane.setPrefWidth(665);
        overlayPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(questionIndex < questionNumber) {
                    renderNewQuestion();
                    controller.testpane.getChildren().remove(overlayPane);
                }
                else {
                    controller.testpane.getChildren().remove(overlayPane);
                    controller.openGamePane();
                }
            }
        });
        Label overlayText = new Label(text);
        controller.testpane.getChildren().add(overlayPane);
    }
}
