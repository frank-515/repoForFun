package com.example.sys;//package com.example.sys;
//
import com.alibaba.fastjson.JSON;
//import com.example.sys.pojo.Staff;
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.layout.GridPane;
//import javafx.stage.Stage;
//
//import java.nio.Buffer;
//
//public class HelloApplication extends Application {
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        Button addBtn = new Button("Add");
//        addBtn.setOnAction(event -> {
//            getStaff gs = new getStaff();
//            try {
//                gs.start(new Stage());
//            } catch (Exception e) {
//                System.out.println(e.getStackTrace());
//                //123throw new RuntimeException(e);
//            }
//        });
//        GridPane gp = new GridPane();
//        gp.add(addBtn, 0, 0);
//        primaryStage.setScene(new Scene(gp, 300,300));
//        primaryStage.show();
//    }
//    public static void main(String[] argv) {
//        launch(argv);
//    }
//}

import com.example.sys.db.StaffDb;
import com.example.sys.pojo.Staff;
import javafx.scene.Scene;
import lombok.Data;

import java.io.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class HelloApplication {
    public static void main(String[] args) throws Exception {
        new commandLine();
    }
}

class commandLine {
    final static String[] keys = {"name", "gender", "birthDay", "phoneNumber", "address", "postcode", "ID", "QQnumber", "classification"};
    StaffDb db;
    Scanner sc = new Scanner(System.in);
    public commandLine() throws Exception {
        db = new StaffDb();
        System.out.print(">");
        while (sc.hasNext()) {

            String cmd = sc.next();

            switch (cmd) {
                case "q":
                case "quit":
                    System.exit(0);
                    break;
                case "a":
                case "add":
                    add();
                    break;
                case "delete":
                case "d":
                    delete();
                    break;
                case "help":
                case "h":
                    help();
                    break;
                case "list":
                case "l":
                    listAll();
                    break;
                case "find":
                case "f":
                case "query":
                    query();
                    break;
                case "update":
                case "modify":
                case "m":
                    update();
                    break;
                case "dump":
                case "save":
                    save();
                    break;
                case "load":
                    load(sc.next());
                    break;
                default:
                    puts(cmd + "can't be  identified.");
            }
            System.out.print(">");
        }
    }
    private static void clearConsole() {
        System.out.print("\033[H\033[2J");
    }
    private static void puts(String str) {
        System.out.println(str);
    }
    private static void help() {
        puts("Usage: --help or -h for man page\n -a or --add to add a new entry to the list with hint\n -d or --delete to remove a entry\n -l or --list to list all entries by name\n -f or --find to find an entry by hint\n -m or --modify to modify an entry\n --save [filename]\n --load [filename]\n --version to print version information\n");
    }
    private static void start() {
        clearConsole();
        puts("Command Line: -arlfm, --save, --load, --version, -h for more information");
    }
    private Staff getStaff() throws Exception{
        clearConsole();
        ArrayList<String> buffer = new ArrayList<>();
        for (String s : keys) {
            puts(s + ":");
            buffer.add(sc.next());
        }
        Staff s = new Staff();
        s.setName(buffer.get(0));
        s.setGender(buffer.get(1));
        String[] ymd = buffer.get(2).split("-");
        if (ymd.length != 3) throw new Exception("Bad format.");
        Date d = new Date(0);
        d.setYear(Integer.parseInt(ymd[0]) - 1900);
        d.setMonth(Integer.parseInt(ymd[1]) - 1);
        d.setDate(Integer.parseInt(ymd[2]));
        s.setBirth(d);
        s.setPhoneNumber(buffer.get(3));
        s.setAddress(buffer.get(4));
        s.setPostcode(buffer.get(5));
        s.setID(buffer.get(6));
        s.setQq(buffer.get(7));
        s.setClassify(buffer.get(8));
        return s;

    }
    private void update() throws Exception{
        puts("Search by [1]name\n [2]phone number");
        String opt = sc.next();
        List<Staff> ls;
        Integer index = -1;
        switch (opt) {
            case "1":
                ls = db.selectName(sc.next());
                break;
            case "2":
                ls = db.selectPhonenumber(sc.next());
                break;
            default:
                puts("Bad option!");
                return;
        }
        if (ls.isEmpty()) {
            puts("Nothing found!");
            return;
        }
        index = ls.get(0).getIdx();
        Staff s = getStaff();
        s.setIdx(index);
        db.update(index, s);
    }
    private void save() throws Exception{
        IO.dump(sc.next());


    }
    private void load(String filename) throws Exception{
        List<Staff> ls = IO.load(filename);
        Integer count = 0;
        for (int i = 0; i < ls.size(); i++) {
            List<Staff> tmp = db.selectName(ls.get(i).getName());
            if (tmp.isEmpty()) {
                db.add(ls.get(i));
                count++;
            }
        }
        puts(count + " loaded.");
    }
    private void add() {
        try {
            db.add(getStaff());
        } catch (Exception e) {
            System.err.println("Unable add new entry," + e.getMessage());
            return;
        }
    }
    private void listAll() {
        try {
            List<Staff> ls = db.selectAll();
            for (var i : ls) {
                System.out.println(i.toString());
            }
        } catch (Exception e) {}
    }
    private void query() {
        puts("By [1]Name, [2]Classify, [3]Address, [4]Phone number");
        List<Staff> list = new ArrayList<>();
        String target;
        try {
            switch (sc.next()) {
                case "1":
                    target = sc.next();
                    list = db.selectName(target);
                    break;
                case "2":
                    target = sc.next();
                    list = db.selectClassify(target);
                    break;
                case "3":
                    target = sc.next();
                    list = db.selectAddress(target);
                    break;
                case "4":
                    target = sc.next();
                    list = db.selectPhonenumber(target);
                    break;
                default:
                    puts("Bad option!");
                    return;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }
        if (list.size() == 0) {
            puts("Nothing found!");
            return;
        } else {
            for (int i = 0; i < list.size(); i++) {
                puts(list.get(i).toString());
            }
        }
    }

    private void modify() throws Exception {
        puts("By [1]Name \n [2]Phone number");
        String target;
        int index;
        switch (sc.next()) {
            case "1":
                target = sc.next();
                try {
                    index = db.selectName(target).get(0).getIdx();
                } catch (Exception e) {
                    puts("Nothing found!");
                    return;
                }
                db.update(index, getStaff());
                break;
            case "2":
                target = sc.next();
                try {
                    index = db.selectPhonenumber(target).get(0).getIdx();
                } catch (Exception e) {
                    return;
                }
                db.update(index, getStaff());
                break;
            default:
                puts("Bad option.");
                return;
        }


    }

    private void delete() throws Exception {
        puts("By [1]Name \n[2]Phone number");
        List<Staff> ls;
        String target;
        switch (sc.next()) {
            case "1":
                puts("Input name");
                target = sc.next();
                ls = db.selectName(target);
                if (ls.size() == 0) {
                    puts("Nothing found!");
                    return;
                } else {
                    puts(String.format("%d record found in the database.", ls.size()));
                    for (int i = 0; i < ls.size(); i++) {
                        System.out.println(ls.get(i));
                    }
                }
                db.deleteName(target);
                System.out.println("Done");
                break;
            case "2":
                puts("Input phone number:");
                target = sc.next();
                ls = db.selectName(target);
                if (ls.size() == 0) {
                    puts("Nothing found!");
                    return;
                } else {
                    puts(String.format("%d record found in the database.", ls.size()));
                    for (int i = 0; i < ls.size(); i++) {
                        System.out.println(ls.get(i));
                    }
                    db.deletePhonenumber(target);
                    System.out.println("Done");
                }
                break;
        }
    }
}
class IO {
    public static List<Staff> load(String source) {
        Scanner sc;
        try {
            sc = new Scanner(new FileInputStream(source));
        } catch (Exception e) {
            System.err.println("Unable to open the file!");
            return null;
        }
        ArrayList<Staff> ls = new ArrayList<>();
        while (sc.hasNextLine()) {
            String s = sc.nextLine();
            System.out.println(s);
            ls.add(JSON.parseObject(s, Staff.class));
            //String s = sc.nextLine();
            //System.out.println(s);
            //ls.add(new Staff(s));
        }
        sc.close();
        return ls;
    }
    public static void export(String source) throws Exception{
        File f = new File(source);
        if (!f.exists()) f.createNewFile();
        PrintWriter pw = new PrintWriter(f);
        var pt = new StaffDb().selectAll();
        for (int i = 0; i < pt.size(); i++) {
            //pw.println(JSON.toJSONString(pt.get(i)));
            pw.println(pt.get(i).toString());
        }
        pw.flush();
        pw.close();
    }
    public static void dump(String source) throws Exception{
        File f = new File(source);
        if (!f.exists()) f.createNewFile();
        PrintWriter pw = new PrintWriter(f);
        var pt = new StaffDb().selectAll();
        for (int i = 0; i < pt.size(); i++) {
            pw.println(JSON.toJSONString(pt.get(i)));
            //pw.println(pt.get(i).toData());
        }
        pw.flush();
        pw.close();
    }
}