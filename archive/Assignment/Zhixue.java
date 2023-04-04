
import java.io.*;
import java.util.*;

public class Zhixue {
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
            if (yy.toString().length() != 4 || mm.toString().length() > 2 || dd.toString().length() > 2 || mm.toString().length() == 0 || dd.toString().length() == 0) {
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
    public String ID;
}

class Student extends Person implements Comparable<Student> {
    public int compareTo(Student other) {
        return this.getSum() < other.getSum() ? -1 : 1;
    }
    final static String[] keys = {"name", "gender", "birthDay", "ID", "studentNumber", "grade1", "grade2", "grade3", "grade4", "classNumber"};
    public String studentNumber, classNumber;
    public Integer grade1, grade2, grade3, grade4;
    private Integer sum = -1;
    public String toString() {
        String s = "";
        s += keys[0] + "=" + super.name + ", ";
        s += keys[1] + "=" + super.gender + ", ";
        s += keys[2] + "=" + super.birthDay + ", ";
        s += keys[3] + "=" + super.ID + ", ";
        s += keys[4] + "=" + this.studentNumber + ", ";
        s += keys[5] + "=" + this.grade1 + ", ";
        s += keys[6] + "=" + this.grade2 + ", ";
        s += keys[7] + "=" + this.grade3 + ", ";
        s += keys[8] + "=" + this.grade4 + ", ";
        s += keys[9] + "=" + this.classNumber + ", ";
        return s;
    }
    public float getAvg() {
        this.sum = this.grade1 + this.grade2 + this.grade3 + this.grade4;
        return (float)this.sum / 4;
    }
    public int getSum() {
        this.sum = this.grade1 + this.grade2 + this.grade3 + this.grade4;
        return this.sum;
    }
    public String toDataString() {
        String s = "";
        s +=  super.name + " ";
        s +=  super.gender + " ";
        s +=  super.birthDay + " ";
        s +=  super.ID + " ";
        s +=  this.studentNumber + " ";
        s +=  this.grade1 + " ";
        s +=  this.grade2 + " ";
        s +=  this.grade3 + " ";
        s +=  this.grade4 + " ";
        s +=  this.classNumber + " ";
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
                super.ID = s;
                break;
            case 4:
                this.studentNumber = s;
                break;
            case 5:
                this.grade1 = Integer.parseInt(s);
                break;
            case 6:
                this.grade2 = Integer.parseInt(s);
                break;
            case 7:
                this.grade3 = Integer.parseInt(s);
                break;
            case 8:
                this.grade4 = Integer.parseInt(s);
                break;
            case 9:
                this.classNumber = s;
                break;

        }
    }
}

class CLInterface {
    private Scanner sc;
    private Core core;
    final static String[] keys = {"name", "gender", "birthDay", "ID", "studentNumber", "grade1", "grade2", "grade3", "grade4", "classNumber"};
    private static void clearConsole() {
        System.out.print("\033[H\033[2J");
    }
    private static void puts(String str) {
        System.out.println(str);
    }
    private static void help() {
        puts("Usage: --help or -h for man page\n -a or --add to add a new entry to the list with hint\n -r [studnetNumber] or --remove to remove a entry\n -l or --list to list all entries by name sequential\n -f or --find to find an entry by hint\n -m [studentNumber] or --modify to modify an entry\n --stat print statistics information. \n--save [filename]\n --load [filename]\n --version to print version information\n");
    }
    private static void start() {
        clearConsole();
        puts("Command Line: -arlfm, --save, --load, --version, --stat, -h for more information");
    }
    private void add() {
        clearConsole();
        Student student;
        try {
            student = getStudent();
        } catch (Exception e) {
            System.out.println("Wrong format.");
            return;
        }
        
        core.push_back(student);
    }
    // private void remove() {
    //     clearConsole();
    //     puts("Remove by\n[1]Name\n[2]StudentNumber\n");
    //     var op = sc.next();
    //     var index = -1;
    //     switch (op) {
    //         case "1":
    //             puts("Input the name to remove:");
    //             index = core.findByName(sc.next());
    //             if (index < 0) {
    //                 puts("Nothing found!");
    //                 return;
    //             }
    //             core.remove(index);
    //             break;
    //         case "2":
    //             puts("Input the StudentNumber to remove:");
    //             index = core.findByStudentNumber(sc.next());
    //             if (index < 0) {
    //                 puts("Nothing found!");
    //                 return;
    //             }
    //             core.remove(index);
    //             break;
    //         default:
    //             puts("Unknown option!");
    //             return;
    //     }
    // }
    private void remove(String target) {
        core.sortByName();
        var index = core.findByStudentNumber(target);
        if (index < 0) {
            puts("Nothing found!");
            return;
        } else {
            core.remove(index);
            puts(target + " successfully removed.");
        }
    }

    private void find() {
        core.sortByName();
        clearConsole();
        puts("Find by\n[1]Name and classNumber\n[2]Class Number\n[3]Student number\n[4]Student name");
        var op = sc.next();
        var index = new ArrayList<Integer>();
        switch (op) {
            case "1":
                puts("Input a name and class number separate by spaces:");
                var name = sc.next();
                var classNumber = sc.next();
                ArrayList<Integer> classBucket = core.findByclassNumber(classNumber);
                ArrayList<Integer> nameBucket = core.findByName(name);
                for (var i : nameBucket) { //暴力吧
                    for (var j : classBucket) {
                        if (i == j) index.add(i);
                    }
                }
                break;
            case "2":
                puts("Input a classNumber:");
                index = core.findByclassNumber(sc.next());
                break;
            case "3":
                puts("Input a student number:");
                index.add(core.findByStudentNumber(sc.next()));
                break;
            case "4":
                puts("Input a student name:");
                index = core.findByName(sc.next());
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
    // public void find(String target) {
    //     core.sortByName();
    //     clearConsole();
    //     var index = core.findByStudentNumber(target);
    //     if (index < 0) puts("Nothing found!");
    //     else puts(core.get(index).toString());
    // }

    // public void modify(String ta) {
    //     core.sortByName();
    //     clearConsole();
    //     puts("Modify by\n[1]Name\n[2]Phone");
    //     var op = sc.next();
    //     var index = -1;
    //     switch (op) {
    //         case "1":
    //             System.out.print("Target name:");
    //             index = core.findByName(sc.next()).get(0); 
    //             break;
    //         case "2":
    //             System.out.print("Target number:");
    //             index = core.findByStudentNumber(sc.next());
    //             break;
    //         default:
    //             puts("Unknown option");
    //             return;
    //     }
    //     var student = new Student();
    //     if (index < 0) {
    //         puts("Nothing found!");
    //         return;
    //     }
    //     puts("Input the new info to the student");
    //     try {
    //         student = getStudent();
    //     } catch (Exception e) {
    //         System.err.println("Wrong format.");
    //         return;
    //     }
        
    //     core.modify(index, student);
    // }

    public void modify(String target) {
        int index = core.findByStudentNumber(target);
        if (index < 0) {
            puts("Couldn't find student " + target);
        } else {
            puts("Input the new info to the student.");
            try {
                Student s = getStudent();
                core.modify(index, s);
            } catch (Exception e) {
                System.err.println("Wrong format.");
                return;
            }
        }

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
    public void stat() {

        class pair{
            public Integer first, second; //人数，总分
            public pair(int x, int y) {
                this.first = x;
                this.second = y;
            }
        }
        Map<String, pair> tmp = new HashMap<String, pair>();
        int sum1 = 0, sum2 = 0, sum3 = 0, sum4 = 0;
        for (int i = 0; i < core.length(); i++) {
            if (tmp.containsKey(core.get(i).classNumber)) {
                tmp.put(core.get(i).classNumber, new pair(tmp.get(core.get(i).classNumber).first + 1, tmp.get(core.get(i).classNumber).second + core.get(i).getSum()));
            } else {
                tmp.put(core.get(i).classNumber, new pair(1, core.get(i).getSum()));
            }
            sum1 += core.get(i).grade1;
            sum2 += core.get(i).grade2;
            sum3 += core.get(i).grade3;
            sum4 += core.get(i).grade4;
        }
        String put = "";
        put += "There " + core.length() + " student(s) on record\n";
        put += "Course1 Avg. " + String.format("%.1f" ,(float)sum1 / core.length()) + "\n";
        put += "Course2 Avg. " + String.format("%.1f" ,(float)sum2 / core.length()) + "\n";
        put += "Course3 Avg. " + String.format("%.1f" ,(float)sum3 / core.length()) + "\n";
        put += "Course4 Avg. " + String.format("%.1f" ,(float)sum4 / core.length()) + "\n";
        for (var i : tmp.keySet()) {
            put += "Class " + i + ": Avg. " + String.format("%.1f", tmp.get(i).second.floatValue() / tmp.get(i).first.floatValue()) + "\n";
        }
        puts(put);
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
        System.out.print("$ ");
        while (sc.hasNextLine()) {
            Thread.sleep(100);
            System.out.print("$ ");
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
                    remove(sc.next());
                    break;
                case "--stat":
                    stat();
                    break;
                case "-f":
                case "--find":
                    find();
                    break;
                case "-m":
                case "--modify":
                    isSaved = false;
                    modify(sc.next());
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
    public Student getStudent() throws Exception{
        Student student = new Student();
        sc.nextLine();
        for (int i = 0; i < keys.length; i++) {
            System.out.print(keys[i] + ":");
            student.setProperties(i, sc.nextLine());
        }
        return student;
    }
}

class IO {
    static final int keyNum = 10;
    static public void save(String name, Student[] list) {
        try (OutputStream os = new FileOutputStream(name)) {
            PrintWriter pw = new PrintWriter(os);
            for (int i = 0; i < Student.keys.length; i++) {
                pw.print(Student.keys[i] + " ");
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
    static public ArrayList<Student> load(String name) {
        var ret = new ArrayList<Student>();
        try (InputStream is = new FileInputStream(name)) {
            Scanner sc = new Scanner(is);
            sc.nextLine();
            while (sc.hasNext()) {
                var tmp = new Student();
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
    private ArrayList<Student> list = new ArrayList<Student>();

    public int length() {
        return list.size();
    }

    public void setList(ArrayList<Student> list) {
        this.list = list;
    }

    public Student[] getList() {
        Student[] list = new Student[this.length()];
        for (int i = 0; i < this.length(); i++) {
            list[i] = this.get(i);
        }
        return list;

    }

    public Student get(int index) {
        return list.get(index);
    }

    public void push_back(Student student) {
        list.add(student);
    }

    public ArrayList<Integer> findByName(String name) {
        ArrayList<Integer> ret = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).name.equals(name)) ret.add(i);
        }
        return ret;
    }

    public int findByStudentNumber(String phone) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).studentNumber.equals(phone)) return i;
        }
        return -1;
    }

    public ArrayList<Integer> findByclassNumber(String classNumber) {
        ArrayList<Integer> ret = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).classNumber.equals(classNumber)) ret.add(i);
        }
        return ret;
    }

    // public ArrayList<Integer> findBygrade1(String grade1) {
    //     var ret = new ArrayList<Integer>();
    //     for (int i = 0; i < list.size(); i++) {
    //         if (list.get(i).grade1.toLowerCase().contains(grade1.toLowerCase()) || grade1.toLowerCase().contains(list.get(i).grade1.toLowerCase())) { //无视大小写包含
    //             ret.add(i);
    //         }
    //     }
    //     return ret;
    // }
    public void remove(int index) {
        list.remove(index);
    }

    public void modify(int index, Student s) {
        list.set(index, s);
    }
    //没有用有序数据结构就请每次都sort一下吧
    public void sortByName() {
        Collections.sort(list); 
    }
}