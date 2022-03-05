package com.prometrx.questionscresolver.AfterSearch.Model;

public class AfterSearchResultModel {

    private String qId, qCount, qTitle;

    public AfterSearchResultModel(String qId, String qCount, String qTitle) {
        this.qId = qId;
        this.qCount = qCount;
        this.qTitle = qTitle;
    }

    public String getqId() {
        return qId;
    }

    public void setqId(String qId) {
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
}
