package com.ncsu.csc512.jlwheele;

public class PrototxtParser {

    private TokenList tl;
    private PrototxtGrammar pGrammar;
    private String[] pNodeList;
    private String currNode;

    public PrototxtParser(TokenList tl) {
        System.out.println("Prototxt Parser init");
        this.tl = tl;
        pGrammar = new PrototxtGrammar(this);
        pNodeList = new String[1];
        currNode = "";
    }

    public boolean parsePrototxt() {
        System.out.println("Prototxt Parser started");
        return pGrammar.prototxt();
    }

    public boolean hasNextToken() {
        return tl.hasNextToken();
    }

    public Token getCurrToken() {
        return tl.getCurrToken();
    }

    public void nextToken() {
        currNode += tl.getCurrToken().getValue();
        int type = tl.getCurrToken().getType();
        if ((type == Token.STRING_TYPE) || (type == Token.NUM_TYPE) || (type == Token.BOOL_TYPE)) {
            currNode += ",";
        }
        tl.nextToken();
    }

    public void addNode() {
//        System.out.println("Prototxt Parser adding node: " + currNode);
        int length = pNodeList.length;
        String[] tmp = new String[length + 1];

        for (int i = 0; i < length; i++) {
            tmp[i] = pNodeList[i];
        }

        tmp[length] = currNode;
        pNodeList = tmp;
        currNode = "";
    }

    public TokenList getTokenList() {
        return tl;
    }
}
