package com.kiotfpt.request;

import java.util.List;

public class CreateOrderRequest {
    private int accountId;
    private List<SectionRequest> sections;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(int accountId, List<SectionRequest> sections) {
        this.accountId = accountId;
        this.sections = sections;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public List<SectionRequest> getSections() {
        return sections;
    }

    public void setSections(List<SectionRequest> sections) {
        this.sections = sections;
    }
}

