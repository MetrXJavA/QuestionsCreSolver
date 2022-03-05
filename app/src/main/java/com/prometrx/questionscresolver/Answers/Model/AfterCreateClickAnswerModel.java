package com.prometrx.questionscresolver.Answers.Model;

public class AfterCreateClickAnswerModel {

    private String uId, username, qCount, qId;

    public AfterCreateClickAnswerModel(String uId, String username, String qCount, String qId) {
        this.uId = uId;
        this.username = username;
        this.qCount = qCount;
        this.qId = qId;
    }

    public String getqId() {
        return qId;
    }

    public void setqId(String qId) {
        this.qId = qId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getqCount() {
        return qCount;
    }

    public void setqCount(String qCount) {
        this.qCount = qCount;
    }
}
