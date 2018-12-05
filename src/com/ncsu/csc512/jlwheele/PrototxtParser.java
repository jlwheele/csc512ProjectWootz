package com.ncsu.csc512.jlwheele;

import java.util.Arrays;

public class PrototxtParser {

    private TokenList tl;
    private PrototxtGrammar pGrammar;
    private String[] nodeList;
    private String currNode;
    private PrototxtData pData;
    private boolean withinParam = false;
    private boolean withinParamParam = false;

    public PrototxtParser(TokenList tl) {
//        System.out.println("Prototxt Parser init");
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
        System.out.println("...Done");
        return passed;
    }

    private void parseNodes() {
        pData = new PrototxtData();
        for (int n = 0; n < nodeList.length; n++) {
            String node = nodeList[n];
//            System.out.println("node: " + node);
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
                String[] layerDefinitions = node.substring(node.indexOf('{') + 1, node.lastIndexOf('}')).split(",");
                //System.out.println("layer: " + Arrays.toString(layerDefinitions));

                for (String definition : layerDefinitions) {
//                    System.out.println("definition: " + definition);

                    if (definition.contains("param")) {
                        String[] paramDefinitions = definition.substring(definition.indexOf('{') + 1, definition.lastIndexOf('}')).split(";");
                        layer.defineParam(paramDefinitions);
                    } else {
                        String[] def = definition.split(":");
                        String var = def[0];

                        if (var.equals("name")) {
                            layer.setName(def[1]);
                        } else if (var.equals("type")) {
                            layer.setType(def[1]);
                        } else if (var.equals("top")) {
                            layer.setTop(def[1]);
                        } else if (var.equals("bottom")) {
                            layer.setBottom(def[1]);
                        }
                    }
                }

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
        if (val.contains("param") && tl.getCurrToken().getValue().equals("{")) {
            withinParam = true;
        } else if (withinParam && val.equals("}")) {
            if (withinParamParam)
                withinParamParam = false;
            else
                withinParam = false;
        } else if (withinParam && (val.equals("weight_filler") || val.equals("shape")) && tl.getCurrToken().getValue().equals("{")) {
            withinParamParam = true;
        }

        if (delimComma && ((type == Token.STRING_TYPE) || (type == Token.NUM_TYPE) || (type == Token.BOOL_TYPE) || (type == Token.NON_TYPE) || (val.equals("}")))) {
            if (withinParam) {
                if (withinParamParam)
                    currNode += "&";
                else
                    currNode += ";";
            } else
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
