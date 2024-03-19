package com.hgzp.advertising.emnus.business;

public enum InvoiceApplyType {
    PRE_INVOICE(1, "预开发票"),
    PENDING_INVOICE(2, "挂开发票");

    private final int key;
    private final String description;

    InvoiceApplyType(int key, String description) {
        this.key = key;
        this.description = description;
    }

    public int getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }
}