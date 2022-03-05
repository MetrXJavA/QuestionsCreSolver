package com.prometrx.questionscresolver.Fragments.Model;

public class SolvedLayoutLastModel {

    private String qId, qTitle, qCount;

    public SolvedLayoutLastModel(String qId, String qTitle, String qCount) {
        this.qId = qId;
        this.qTitle = qTitle;
        this.qCount = qCount;
    }

    public String getqCount() {
        return qCount;
    }

    public void setqCount(String qCount) {
        this.qCount = qCount;
    }

    public String getqId() {
        return qId;
    }

    public String getqTitle() {
        return qTitle;
    }

    public void setqTitle(String qTitle) {
        this.qTitle = qTitle;
    }

    public void setqId(String qId) {
        this.qId = qId;
    }

}
