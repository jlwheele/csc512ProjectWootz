package com.ncsu.csc512.jlwheele;

import java.util.Scanner;

public class PrototxtScanner {

        private TokenList tList;

    public PrototxtScanner(Scanner fScanner) {
        System.out.println("Prototxt Scanner started");

//        int count = 0;
//
//        while (fScanner.hasNextLine()) {
//            count++;
//            fScanner.nextLine();
//        }
//
//        System.out.println("Lines read: " + count);

        tList = new TokenList();

        String s = "";
//        String val = "";
        Token t;
        while (fScanner.hasNext()) {
            String c = fScanner.next();
            System.out.println("Scanner: " + c);
            if (c.startsWith("\"")) {
                t = new Token(c.replaceAll("\"",""), Token.STRING_TYPE);
                tList.addToken(t);
            } else if (c.endsWith(":")) {
                t = new Token(c.replaceAll(":", ""), Token.VAR_TYPE);
                tList.addToken(t);
                t = new Token(":", Token.SYMBOL_TYPE);
                tList.addToken(t);
            } else if (c.equals("{") || c.equals("}")) {
                t = new Token(c, Token.SYMBOL_TYPE);
                tList.addToken(t);
            } else {
                try {
                    Double.parseDouble(c);
                    t = new Token(c, Token.NUM_TYPE);
                } catch (NumberFormatException e) {
                    System.out.println(c + " not a number!");
                    if (c.equals("true") || c.equals("false"))
                        t = new Token(c, Token.BOOL_TYPE);
                    else
                        t = new Token(c, Token.VAL_TYPE);
                }
                tList.addToken(t);
            }

            s += c;
        }

        System.out.println(s);
    }

    public TokenList getTokenList() {
        return tList;
    }
}
