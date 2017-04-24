package com.kk.taurus.playerbase.setting;

import java.io.Serializable;

/**
 * Created by cyw on 2016/3/1.
 */
public class Rate implements Serializable{

    private int definitionType;

    private String rate_key;
    private String rate_value;

    public Rate() {
    }

    public Rate(String rate_key, String rate_value) {
        this.rate_key = rate_key;
        this.rate_value = rate_value;
    }

    public Rate(int definitionType, String rate_key, String rate_value) {
        this.definitionType = definitionType;
        this.rate_key = rate_key;
        this.rate_value = rate_value;
    }

    public int getDefinitionType() {
        return definitionType;
    }

    public void setDefinitionType(int definitionType) {
        this.definitionType = definitionType;
    }

    public String getRate_key() {
        return rate_key;
    }

    public void setRate_key(String rate_key) {
        this.rate_key = rate_key;
    }

    public String getRate_value() {
        return rate_value;
    }

    public void setRate_value(String rate_value) {
        this.rate_value = rate_value;
    }

    public String getDefinition(){
        return rate_value;
    }

}
