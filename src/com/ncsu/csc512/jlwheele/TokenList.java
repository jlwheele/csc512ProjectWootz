package com.ncsu.csc512.jlwheele;

public class TokenList {

    private Token[] tList;
    private int nextIndex;
    private int length;

    public TokenList() {
        tList = new Token[1];
        nextIndex = 0;
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
        if (nextIndex < length)
            return true;
        return false;
    }

    public Token getNextToken() {
        Token t = tList[nextIndex];
        nextIndex++;
        return t;
    }

    public int length() {
        return length;
    }
}
