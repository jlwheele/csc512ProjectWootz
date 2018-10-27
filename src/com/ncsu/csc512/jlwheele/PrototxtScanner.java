package com.ncsu.csc512.jlwheele;

import java.util.Scanner;

public class PrototxtScanner {

    public PrototxtScanner(Scanner fScanner) {
        System.out.println("Prototxt Scanner started");

        int count = 0;

        while (fScanner.hasNextLine()) {
            count++;
            fScanner.nextLine();
        }

        System.out.println("Lines read: " + count);
    }
}
