package com.ncsu.csc512.jlwheele;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Wootz Compiler");

        //java com.ncsu.csc512.jlwheele.Main <file name>
        //C:\Users\James\IdeaProjects\Wootz\tests\v1.prototxt

        if (args.length == 0) {
            System.out.println("Usage: java com.ncsu.csc512.jlwheele.Main <file name>");
            return;
        } else if (args.length > 1) {
            System.out.println("More than one file name provided!");
            System.out.println("Using first provided file");
        }

        System.out.println("Prototxt file input: " + args[0]);

        String fName = args[0];
        String[] tmp = fName.split("\\.");

        if ((tmp.length == 2) && !tmp[1].equalsIgnoreCase("prototxt")) {
            System.out.println("Please provide a *.prototxt file only");
            return;
        }

        File f = new File(fName);
        Scanner fScanner;

        try {
            fScanner = new Scanner(f);
        } catch (FileNotFoundException e) {
            System.out.println("Please provide a file that exists!");
            return;
        }

        PrototxtScanner pScanner = new PrototxtScanner(fScanner);
    }
}
