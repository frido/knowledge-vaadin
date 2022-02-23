package com.example.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
public class Praca {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("C:\\Users\\Peter\\Desktop\\Error_warning.csv");
        List<String> lines = Files.readAllLines(path);
        List<Message> msgs = new ArrayList<>();
        lines.forEach(line -> processLine(msgs, line));
        msgs.stream().sorted().forEach(msg -> System.out.println(msg));
    }

    public static void processLine(List<Message> msgs, String line) {
        String[] cells = line.split("@", -1);
        String type = cells[0];
        String place = cells[1];
        String action = cells[2];
        String warning = cells[3];
        String error = cells[4];
        
        if (!warning.isBlank()) {
            msgs.add(new Message(type, place, action, "warning", warning));
        }

        if (!error.isBlank()) {
            msgs.add(new Message(type, place, action, "error", error));
        }
    }
}
