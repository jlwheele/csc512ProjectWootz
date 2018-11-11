package com.ncsu.csc512.jlwheele;

public class TokenList {

    private Token[] tList;
    private int currIndex;
    private int length;

    public TokenList() {
        tList = new Token[1];
        currIndex = 0;
        length = 0;
    }

    public void addToken(Token t) {
        Token[] tListTmp = new Token[length + 1];

        for (int i = 0; i < length; i++) {
            tListTmp[i] = tList[i];
        }

        tListTmp[length] = t;
        tList = tListTmp;
        length++;
    }

    public boolean hasNextToken() {
        if (currIndex < length)
            return true;
        return false;
    }

    public Token getCurrToken() {
        Token t = tList[currIndex];
        return t;
    }

    public void nextToken() {
        currIndex++;
    }

    public int length() {
        return length;
    }
}
