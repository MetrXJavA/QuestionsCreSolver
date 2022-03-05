package com.prometrx.questionscresolver.Fragments.Model;

public class CreateLayoutLastModel {

    private String qId, qTitle, aCount;

    public CreateLayoutLastModel(String qId, String qTitle, String aCount) {
        this.qId = qId;
        this.qTitle = qTitle;
        this.aCount = aCount;
    }

    public String getqId() {
        return qId;
    }

    public void setqId(String qId) {
        this.qId = qId;
    }

    public String getqTitle() {
        return qTitle;
    }

    public void setqTitle(String qTitle) {
        this.qTitle = qTitle;
    }

    public String getaCount() {
        return aCount;
    }

    public void setaCount(String aCount) {
        this.aCount = aCount;
    }
}
