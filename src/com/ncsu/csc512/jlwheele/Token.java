package com.ncsu.csc512.jlwheele;

public class Token {

    private String value;
    private int type;

    public final static int SYMBOL_TYPE = 0;
    public final static int STRING_TYPE = 1;
    public final static int VAR_TYPE = 2;
    public final static int NUM_TYPE = 3;
    public final static int VAL_TYPE = 4;
    public final static int BOOL_TYPE = 5;

    public Token (String value, int type) {
        setValue(value);
        setType(type);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
