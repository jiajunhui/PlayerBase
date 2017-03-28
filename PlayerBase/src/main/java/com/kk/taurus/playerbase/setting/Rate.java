package com.kk.taurus.playerbase.setting;

import java.io.Serializable;

/**
 * Created by cyw on 2016/3/1.
 */
public class Rate implements Serializable{

    public static final int DEFINITION_TYPE_LETV = 1;
    public static final int DEFINITION_TYPE_BF = 2;

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
        switch (definitionType){
            case DEFINITION_TYPE_BF:
                return rate_value;

            case DEFINITION_TYPE_LETV:
                return rate_value;
        }
        return "";
    }

}
