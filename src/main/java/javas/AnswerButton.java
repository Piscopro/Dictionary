package javas;

import javafx.scene.control.Button;

public class AnswerButton extends Button {
    private MainController controller;
    private boolean isRight;
    private GameTest test;
    public AnswerButton(MainController controller, boolean isRight, String text, GameTest test, Integer count) {
        this.setPrefHeight(90);
        this.setPrefWidth(190);
        switch (count) {
            case 0:
                this.setLayoutX(100);
                this.setLayoutY(300);
                break;
            case 1:
                this.setLayoutX(380);
                this.setLayoutY(300);
                break;
            case 2:
                this.setLayoutX(100);
                this.setLayoutY(400);
                break;
            case 3:
                this.setLayoutX(380);
                this.setLayoutY(400);
                break;
        }
        this.setText(text);
        this.controller = controller;
        this.isRight = isRight;
        this.test = test;
        this.setOnAction(e -> buttonClicked());
    }
    public void buttonClicked(){
        if(isRight){
            this.setStyle("-fx-background-color: green");
            test.answeredRight();
        } else {
            this.setStyle("-fx-background-color: red");
            test.answeredWrong();
        }
    }
}
