package com.prometrx.questionscresolver.Fragments.Model;

public class ExploreModel {

    private String qCount, qTitle, qId;

    public ExploreModel(String qCount, String qTitle, String qId) {
        this.qCount = qCount;
        this.qTitle = qTitle;
        this.qId = qId;
    }

    public String getqCount() {
        return qCount;
    }

    public void setqCount(String qCount) {
        this.qCount = qCount;
    }

    public String getqTitle() {
        return qTitle;
    }

    public void setqTitle(String qTitle) {
        this.qTitle = qTitle;
    }

    public String getqId() {
        return qId;
    }

    public void setqId(String qId) {
        this.qId = qId;
    }
}
