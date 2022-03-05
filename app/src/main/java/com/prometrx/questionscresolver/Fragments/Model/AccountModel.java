package com.prometrx.questionscresolver.Fragments.Model;

public class AccountModel {
    private String qId, qTitle, pending;

    public AccountModel(String qId, String qTitle, String pending) {
        this.qId = qId;
        this.qTitle = qTitle;
        this.pending = pending;
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

    public String getPending() {
        return pending;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }
}
