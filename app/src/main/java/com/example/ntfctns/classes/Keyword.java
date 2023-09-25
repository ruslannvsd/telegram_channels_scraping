package com.example.ntfctns.classes;

public class Keyword {
    public String key;
    public int amount;

    public Keyword(String key, int amount) {
        this.key = key;
        this.amount = amount;
    }

    public String getKey() {
        return key;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
