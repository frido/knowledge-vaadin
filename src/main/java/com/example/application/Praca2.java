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

public class Praca2 {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("C:\\Users\\Peter\\Desktop\\error2.csv");
        List<String> lines = Files.readAllLines(path);
        lines.stream().map(line -> parseLine(line)).sorted()
                .forEach(msg -> System.out.println(msg));
    }

    private static Message parseLine(String line) {
        // (.+)=(.+)\.label.+, ?"(.+)"
        Pattern pattern = Pattern.compile("(.+) ?= ?(.+)\\.label.+, ?\"(.+)\"");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            var ctrlname = matcher.group(1).trim();
            var formname = matcher.group(2).trim();
            var msg = matcher.group(3).trim();
            return new Message("validation", "", "", "warning", null, ctrlname, formname, msg);
        } else {
            System.out.println(line);
            throw new NullPointerException();
        }
    }
}
