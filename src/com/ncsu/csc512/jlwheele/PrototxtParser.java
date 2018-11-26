package com.ncsu.csc512.jlwheele;

public class PrototxtParser {

    private TokenList tl;
    private PrototxtGrammar pGrammar;
    private String[] nodeList;
    private String currNode;
    private PrototxtData pData;
    private boolean withinParam = false;

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
        String val = tl.getCurrToken().getValue();
        int type = tl.getCurrToken().getType();
        currNode += val;
        tl.nextToken();
        boolean delimComma = false;
        if ((currNode.startsWith("input_shape{") || currNode.startsWith("layer{")) && !tl.getCurrToken().getValue().equals("}")) {
            delimComma = true;
        }
        //    <param> --> param LEFT_BRACKET <param_defs> RIGHT_BRACKET <convolution_param> (Convolution)
        //        | batch_norm_param LEFT_BRACKET <batch_norm_defs> RIGHT_BRACKET (BatchNorm)
        //        | scale_param LEFT_BRACKET <scale_defs> RIGHT_BRACKET (Scale)
        //        | pooling_param LEFT_BRACKET <pooling_defs> RIGHT_BRACKET (Pooling)
        //        | dropout_param LEFT_BRACKET <dropout_defs> RIGHT_BRACKET (Dropout)
        //        | reshape_param LEFT_BRACKET <reshape_defs> RIGHT_BRACKET (Reshape)
        if (val.contains("param") && tl.getCurrToken().getValue().equals("{")) {
            withinParam = true;
        } else if (withinParam && val.equals("}")) {
            withinParam = false;
        }

        if (delimComma && ((type == Token.STRING_TYPE) || (type == Token.NUM_TYPE) || (type == Token.BOOL_TYPE) || (val.equals("}")))) {
            if (withinParam)
                currNode += ";";
            else
                currNode += ",";
        }
    }

    public void addNode() {
//        System.out.println("Prototxt Parser adding node: " + currNode);
        int length = nodeList.length;
        String[] tmp = new String[length + 1];

        for (int i = 0; i < length; i++) {
            tmp[i] = nodeList[i];
        }

        if (currNode.charAt(currNode.length() - 1) == ',') {
            currNode = currNode.substring(0, currNode.length() - 1);
        }
        tmp[length] = currNode;
        nodeList = tmp;
        currNode = "";
    }

    public PrototxtData getPrototxtData() {
        return pData;
    }
}
