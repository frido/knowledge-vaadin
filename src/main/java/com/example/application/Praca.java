package com.example.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Praca {

    private static int c = 0;
    private static int cw = 0;
    private static int ce = 0;
    private static int cn = 0;
    private static int cb = 0;

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("C:\\Users\\Peter\\Desktop\\Error_warning.csv");
        List<String> lines = Files.readAllLines(path);
        // System.out.println(lines.size());
        List<Message> msgs = new ArrayList<>();
        lines.forEach(line -> processLine(msgs, line));
        // System.out.println(msgs.size());
        msgs.stream().sorted().forEach(msg -> System.out.println(msg));
        // System.out.println(c);
        // System.out.println(cw);
        // System.out.println(ce);
        // System.out.println(cn);
        // System.out.println(cb);
    }

    public static void processLine(List<Message> msgs, String line) {
        // System.out.println(line);
        String[] cells = line.split("@", -1);
        // for (String string : cells) {
        //     System.out.println("--- " + string);
        // }
        String type = cells[0];
        String place = cells[1];
        String action = cells[2];
        String warning = cells[3];
        String error = cells[4];
        
        c++;

        if (!warning.isBlank()) {
            msgs.add(new Message(type, place, action, "warning", warning));
            // System.out.println("war" + line);
            cw++;
        }

        if (!error.isBlank()) {
            msgs.add(new Message(type, place, action, "error", error));
            // System.out.println("err" + line);
            ce++;
        }

        if (!warning.isBlank() && !error.isBlank()) {
            cb++;
        }

        if (warning.isBlank() && error.isBlank()) {
            // System.out.println("xxx" + line);
            cn++;
        }
    }
}
