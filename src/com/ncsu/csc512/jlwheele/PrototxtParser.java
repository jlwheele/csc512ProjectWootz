package com.ncsu.csc512.jlwheele;

public class PrototxtParser {

    private TokenList tl;
    private PrototxtGrammar pGrammar;
    private String[] nodeList;
    private String currNode;
    private PrototxtData pData;

    public PrototxtParser(TokenList tl) {
        System.out.println("Prototxt Parser init");
        this.tl = tl;
        pGrammar = new PrototxtGrammar(this);
        nodeList = new String[0];
        currNode = "";
        pData = null;
    }

    public boolean parsePrototxt() {
        System.out.println("Prototxt Parser started");
        boolean passed = pGrammar.prototxt();
        if (passed) {
            parseNodes();
        }
        return passed;
    }

    private void parseNodes() {
        pData = new PrototxtData();
        for (int n = 0; n < nodeList.length; n++) {
            String node = nodeList[n];
            System.out.println("node: " + node);
            if (node.startsWith("name")) {
                pData.setName(node.split(":")[1].replaceAll(",", ""));
            } else if (node.startsWith("input")) {
                pData.setInput(node.split(":")[1].replaceAll(",", ""));
            } else if (node.startsWith("input_shape")) {
                node = node.substring(node.indexOf("{") + 1, node.indexOf("}"));
                String[] dims = node.split(",");
                for (int d = 0; d < dims.length; d++) {
                    if ((dims[d] != null) && (dims[d].length() > 0)) {
                        String i = dims[d].split(":")[1];
                        pData.addInputShapeDim(Integer.valueOf(i));
                    }
                }
            } else if (node.startsWith("layer")) {
                PrototxtLayer layer = new PrototxtLayer();
                //todo: parse layer defs and set
                pData.addLayer(layer);
            }
        }
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
        int length = nodeList.length;
        String[] tmp = new String[length + 1];

        for (int i = 0; i < length; i++) {
            tmp[i] = nodeList[i];
        }

        tmp[length] = currNode;
        nodeList = tmp;
        currNode = "";
    }

    public PrototxtData getPrototxtData() {
        return pData;
    }
}
