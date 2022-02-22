package com.example.application;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message implements Comparable{
    private String type;
    private String place;
    private String action;
    private String level;
    private String line;

    private String ctrlname;
    private String formname;
    private String msg;

    private int sorter;

    public Message(String type, String place, String action, String level, String line) {
        this.type = type;
        this.place = place;
        this.action = action;
        this.level = level;
        this.line = line;
        parseLine();
        setSorter();
    }

    
    public Message(String type, String place, String action, String level, String line, String ctrlname, String formname, String msg) {
        this.type = type;
        this.place = place;
        this.action = action;
        this.level = level;
        this.line = line;
        this.ctrlname = ctrlname;
        this.formname = formname;
        this.msg = msg;
    }

    private void setSorter() {
        if (type.equals("p")) {
            sorter = 1;
        } else if (type.equals("v")) {
            sorter = 2;
        } else if (type.equals("d")) {
            sorter = 3;
        } else if (type.equals("s")) {
            sorter = 4;
        } else {
            sorter = 5;
        }
    }

    private String correctType() {
        if (type.equals("p")) {
            return "page";
        } else if (type.equals("v")) {
            return "view";
        } else if (type.equals("d")) {
            return "dialog";
        } else if (type.equals("s")) {
            return "service";
        } else {
            return "validation";
        }
    }

    private void parseLine() {
        // (.+)=(.+)\.label.+, ?"(.+)"
        Pattern pattern = Pattern.compile("(.+) ?= ?(.+)\\.label.+, ?\"(.+)\"");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            // System.out.println("-1- " + matcher.group(1));
            // System.out.println("-2- " + matcher.group(2));
            // System.out.println("-3- " + matcher.group(3));
            ctrlname = matcher.group(1).trim();
            formname = matcher.group(2).trim();
            msg = matcher.group(3).trim();
        } else {
            msg = line;
        }
    }

    @Override
    public String toString() {
        return String.format("%s;%s;%s;%s;%s;%s;%s", correctType(), place, action, level, ctrlname, formname,
                msg);
    }


    public String getType() {
        return this.type;
    }

    public String getPlace() {
        return this.place;
    }

    public String getAction() {
        return this.action;
    }

    public String getLevel() {
        return this.level;
    }

    public String getLine() {
        return this.line;
    }

    public String getCtrlname() {
        return this.ctrlname;
    }

    public String getFormname() {
        return this.formname;
    }

    public String getMsg() {
        return this.msg;
    }

    public int getSorter() {
        return this.sorter;
    }

    @Override
    public int compareTo(Object o) {
        Message that = (Message) o;
        int com = Integer.compare(this.sorter, that.getSorter());
        if (com == 0) {
            com = compareString(place, that.getPlace());
            if (com == 0) {
                com = compareString(action, that.getAction());
                if (com == 0) {
                    com = compareString(level, that.getLevel());
                    if (com == 0) {
                        com = compareString(ctrlname, that.getCtrlname());
                    }
                }
            }
        }
        return com;
    }

    private int compareString(String thisOne, String that) {
        return String.valueOf(thisOne).compareToIgnoreCase(String.valueOf(that));
    }


}
