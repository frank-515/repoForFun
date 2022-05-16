
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Kontact {
        public static void main(String[] args) throws Exception {
            new CLInterface();
        }
}

class Date {
    public Integer yy = 1900, mm = 1, dd = 1;
    public Date(String s) throws Exception {
        try {
            String[] v = s.split("/");
            yy = Integer.parseInt(v[0]);
            mm = Integer.parseInt(v[1]);
            dd = Integer.parseInt(v[2]);
            if (yy.toString().length() != 4 || mm.toString().length() >= 2 || dd.toString().length() >= 2 || mm.toString().length() == 0 || dd.toString().length() == 0) {
                throw new Exception("Invalid number of date");
            }
        }
        finally {}
    }
    public String toString() {
        return yy + "/" + mm + "/" + dd;
    }
}

class Person {
    public String name;
    public String gender;
    public Date birthDay;
}

class Staff extends Person implements Comparable<Person> {
    public int compareTo(Person other) {
        return super.name.compareTo(other.name);
    }
    final static String[] keys = {"name", "gender", "birthDay", "phoneNumber", "address", "postcode", "mailAddress", "QQnumber", "classification"};
    public String phoneNumber, address, postcode, mailAddress, QQnumber, classification;
    public String toString() {
        String s = "";
        s += keys[0] + "=" + super.name + ", ";
        s += keys[1] + "=" + super.gender + ", ";
        s += keys[2] + "=" + super.birthDay + ", ";
        s += keys[3] + "=" + this.phoneNumber + ", ";
        s += keys[4] + "=" + this.address + ", ";
        s += keys[5] + "=" + this.postcode + ", ";
        s += keys[6] + "=" + this.mailAddress + ", ";
        s += keys[7] + "=" + this.QQnumber + ", ";
        s += keys[8] + "=" + this.classification + ", ";
        return s;
    }
    public String toDataString() {
        String s = "";
        s +=  super.name + " ";
        s +=  super.gender + " ";
        s +=  super.birthDay + " ";
        s +=  this.phoneNumber + " ";
        s +=  this.address + " ";
        s +=  this.postcode + " ";
        s +=  this.mailAddress + " ";
        s +=  this.QQnumber + " ";
        s +=  this.classification + " ";
        return s;
    }

    public void setProperties(int n, String s) throws Exception{
        switch (n) {
            case 0:
                super.name = s;
                break;
            case 1:
                super.gender = s;
                break;
            case 2:
                super.birthDay = new Date(s);
                break;
            case 3:
                this.phoneNumber = s;
                break;
            case 4:
                this.address = s;
                break;
            case 5:
                this.postcode = s;
                break;
            case 6:
                this.mailAddress = s;
                break;
            case 7:
                this.QQnumber = s;
                break;
            case 8:
                this.classification = s;
                break;

        }
    }
}

class CLInterface {
    private Scanner sc;
    private Core core;
    final static String[] keys = {"name", "gender", "birthDay", "phoneNumber", "address", "postcode", "mailAddress", "QQnumber", "classification"};
    private static void clearConsole() {
        System.out.print("\033[H\033[2J");
    }
    private static void puts(String str) {
        System.out.println(str);
    }
    private static void help() {
        puts("Usage: --help or -h for man page\n -a or --add to add a new entry to the list with hint\n -r or --remove to remove a entry\n -l or --list to list all entries by name\n -f or --find to find an entry by hint\n -m or --modify to modify an entry\n --save [filename]\n --load [filename]\n --version to print version information\n");
    }
    private static void start() {
        clearConsole();
        puts("Command Line: -arlfm, --save, --load, --version, -h for more information");
    }
    private void add() {
        clearConsole();
        Staff staff;
        try {
            staff = getStaff();
        } catch (Exception e) {
            System.out.println("Wrong format.");
            return;
        }
        
        core.push_back(staff);
    }
    private void remove() {
        clearConsole();
        puts("Remove by\n[1]Name\n[2]Phone\n");
        var op = sc.next();
        var index = -1;
        switch (op) {
            case "1":
                puts("Input the name to remove:");
                index = core.findByName(sc.next());
                if (index < 0) {
                    puts("Nothing found!");
                    return;
                }
                core.remove(index);
                break;
            case "2":
                puts("Input the phone to remove:");
                index = core.findByPhone(sc.next());
                if (index < 0) {
                    puts("Nothing found!");
                    return;
                }
                core.remove(index);
                break;
            default:
                puts("Unknown option!");
                return;
        }
    }

    private void find() {
        core.sortByName();
        clearConsole();
        puts("Find by\n[1]Name\n[2]Phone\n[3]Address\n[4]Classification");
        var op = sc.next();
        var index = new ArrayList<Integer>();
        switch (op) {
            case "1":
                puts("Input a name:");
                index.add(core.findByName(sc.next()));
                break;
            case "2":
                puts("Input a phone:");
                index.add(core.findByPhone(sc.next()));
                break;
            case "3":
                puts("Input a address key word:");
                index = core.findByAddress(sc.next());
                break;
            case "4":
                puts("Input a classification key word:");
                index = core.findByClassification(sc.next());
                break;
            default:
                puts("Unknown option");
                return;
        }
        if (index.size() == 0 || index.get(0) == -1) {
            puts("Nothing found");
            return;
        }
        for (var i : index) {
            var property = core.get(i).toString().split(", ");
            puts("=====Index of " + i + "=====");
            for (int j = 0; j < property.length; j++) {
                puts(property[j]);
            }
            puts("");
        }
    }

    public void modify() {
        core.sortByName();
        clearConsole();
        puts("Modify by\n[1]Name\n[2]Phone");
        var op = sc.next();
        var index = -1;
        switch (op) {
            case "1":
                System.out.print("Target name:");
                index = core.findByName(sc.next());
                break;
            case "2":
                System.out.print("Target number:");
                index = core.findByPhone(sc.next());
                break;
            default:
                puts("Unknown option");
                return;
        }
        var staff = new Staff();
        if (index < 0) {
            puts("Nothing found!");
            return;
        }
        puts("Input the new info to the staff");
        try {
            staff = getStaff();
        } catch (Exception e) {
            System.err.println("Wrong format.");
            return;
        }
        
        core.modify(index, staff);
    }

    public void save(String filename) {
        IO.save(filename, core.getList());
    }

    public void load(String filename) {
        var tmp = IO.load(filename);
        if (tmp.isEmpty()) {
            puts("No file to load");
            return;
        } else {
            core.setList(tmp);
        }
    }

    public void list() {
        clearConsole();
        core.sortByName();
        if (core.length() == 0) puts("There no record in the database.");
        for (var i = 0; i < core.length(); i++) {
            var property = core.get(i).toString().split(", ");
            puts("=====Index of " + i + "=====");
            for (int j = 0; j < property.length; j++) {
                puts(property[j]);
            }
            puts("");
        }
    }

    public void version() {
        puts("dev-0.01");
    }

    public CLInterface() throws Exception {
        boolean isSaved = true;
        sc = new Scanner(System.in);
        core = new Core();  
        start();
        System.out.print("Kontact>");
        while (sc.hasNextLine()) {
            Thread.sleep(100);
            System.out.print("Kontact>");
            switch (sc.next()) {
                case "q":
                case "quit":
                    if (!isSaved) {
                        puts("Quit without save? [y/n]");
                        if (sc.next().equals("y")) System.exit(0);
                    } else {
                        System.exit(0);
                    }
                    break;
                case "-a":
                case "--add":
                    isSaved = false;
                    add();
                break;
                case "-r":
                case "--remove":
                    isSaved = false;
                    remove();
                    break;
                case "-f":
                case "--find":
                    find();
                    break;
                case "-m":
                case "--modify":
                    isSaved = false;
                    modify();
                    break;
                case "--save":
                    save(sc.next());
                    isSaved = true;
                    break;
                case "--load":
                    load(sc.next());
                    break;
                case "-l":
                case "--list":
                    list();
                    break;
                case "--version":
                    version();
                    break;
                case "-h":
                case "--help":
                    help();
                    break;
                default:
                    puts("Command not found, -h for help");
            }
        }
    }
    public Staff getStaff() throws Exception{
        Staff staff = new Staff();
        sc.nextLine();
        for (int i = 0; i < keys.length; i++) {
            System.out.print(keys[i] + ":");
            staff.setProperties(i, sc.nextLine());
        }
        return staff;
    }
}

class IO {
    static final int keyNum = 9;
    static public void save(String name, Staff[] list) {
        try (OutputStream os = new FileOutputStream(name)) {
            PrintWriter pw = new PrintWriter(os);
            for (int i = 0; i < Staff.keys.length; i++) {
                pw.print(Staff.keys[i] + " ");
            }
            pw.println("");
            for (int i = 0; i < list.length ; i++) {
                pw.println(list[i].toDataString());
            }
            pw.close();
        } catch (Exception e) {
            System.err.println("Unable to establish FileOutputStream with file " + name + ", " + e.getMessage());
            return;
        }
    }
    static public ArrayList<Staff> load(String name) {
        var ret = new ArrayList<Staff>();
        try (InputStream is = new FileInputStream(name)) {
            Scanner sc = new Scanner(is);
            sc.nextLine();
            while (sc.hasNext()) {
                var tmp = new Staff();
                for (int i = 0; i < keyNum; i++) {
                    tmp.setProperties(i, sc.next());
                }
                ret.add(tmp);
            }
            sc.close();
        } catch (Exception e) {
            System.err.println("Unable to open file " + name + ", " + e.getMessage());
            return ret;
        }
        return ret;
    }
}

class Core {
    private ArrayList<Staff> list = new ArrayList<Staff>();

    public int length() {
        return list.size();
    }

    public void setList(ArrayList<Staff> list) {
        this.list = list;
    }

    public Staff[] getList() {
        Staff[] list = new Staff[this.length()];
        for (int i = 0; i < this.length(); i++) {
            list[i] = this.get(i);
        }
        return list;

    }

    public Staff get(int index) {
        return list.get(index);
    }

    public void push_back(Staff staff) {
        list.add(staff);
    }

    public int findByName(String name) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).name.equals(name)) return i;
        }
        return -1;
    }

    public int findByPhone(String phone) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).phoneNumber.equals(phone)) return i;
        }
        return -1;
    }

    public ArrayList<Integer> findByClassification(String classification) {
        ArrayList<Integer> ret = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).classification.equals(classification)) ret.add(i);
        }
        return ret;
    }

    public ArrayList<Integer> findByAddress(String address) {
        var ret = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).address.toLowerCase().contains(address.toLowerCase()) || address.toLowerCase().contains(list.get(i).address.toLowerCase())) { //无视大小写包含
                ret.add(i);
            }
        }
        return ret;
    }
    public void remove(int index) {
        list.remove(index);
    }

    public void modify(int index, Staff s) {
        list.set(index, s);
    }
    //没有用有序数据结构就请每次都sort一下吧
    public void sortByName() {
        Collections.sort(list); 
    }
}
