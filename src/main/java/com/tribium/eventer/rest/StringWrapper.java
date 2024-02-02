package com.tribium.eventer.rest;

public class StringWrapper {
    public String string;

    public StringWrapper(String v) {
        this.string = v;
    }

    @Override
    public String toString() {
        return string;
    }
}
