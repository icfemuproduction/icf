package com.example.application.iricf;

public class Property {

    String property,value;

    public Property(String property, String value) {
        this.property = property;
        this.value = value;
    }

    public Property() {
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
