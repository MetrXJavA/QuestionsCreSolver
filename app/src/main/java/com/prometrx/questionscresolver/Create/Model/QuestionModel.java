package com.prometrx.questionscresolver.Create.Model;

public class QuestionModel {

    private String question, choiceOne, choiceTwo, choiceThree, choiceFour, imageUrl;
    private Boolean torc;
    private int questionAnswer, questionTime;

    public QuestionModel(String question, Boolean torc) {
        this.question = question;
        this.torc = torc;
    }

    public QuestionModel(String question, String choiceOne, String choiceTwo, Boolean torc, int questionAnswer, String imageUrl, int questionTime) {
        this.question = question;
        this.choiceOne = choiceOne;
        this.choiceTwo = choiceTwo;
        this.imageUrl = imageUrl;
        this.torc = torc;
        this.questionAnswer = questionAnswer;
        this.questionTime = questionTime;
    }

    public QuestionModel(String question, String choiceOne, String choiceTwo, String choiceThree, Boolean torc, int questionAnswer, String imageUrl, int questionTime) {
        this.question = question;
        this.choiceOne = choiceOne;
        this.choiceTwo = choiceTwo;
        this.choiceThree = choiceThree;
        this.imageUrl = imageUrl;
        this.torc = torc;
        this.questionAnswer = questionAnswer;
        this.questionTime = questionTime;
    }

    public QuestionModel(String question, String choiceOne, String choiceTwo, String choiceThree, String choiceFour, Boolean torc, int questionAnswer, String imageUrl, int questionTime) {
        this.question = question;
        this.choiceOne = choiceOne;
        this.choiceTwo = choiceTwo;
        this.choiceThree = choiceThree;
        this.choiceFour = choiceFour;
        this.imageUrl = imageUrl;
        this.torc = torc;
        this.questionAnswer = questionAnswer;
        this.questionTime = questionTime;
    }

    public int getQuestionTime() {
        return questionTime;
    }

    public void setQuestionTime(int questionTime) {
        this.questionTime = questionTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(int questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getChoiceOne() {
        return choiceOne;
    }

    public void setChoiceOne(String choiceOne) {
        this.choiceOne = choiceOne;
    }

    public String getChoiceTwo() {
        return choiceTwo;
    }

    public void setChoiceTwo(String choiceTwo) {
        this.choiceTwo = choiceTwo;
    }

    public String getChoiceThree() {
        return choiceThree;
    }

    public void setChoiceThree(String choiceThree) {
        this.choiceThree = choiceThree;
    }

    public String getChoiceFour() {
        return choiceFour;
    }

    public void setChoiceFour(String choiceFour) {
        this.choiceFour = choiceFour;
    }

    public Boolean getTorc() {
        return torc;
    }

    public void setTorc(Boolean torc) {
        this.torc = torc;
    }
}
