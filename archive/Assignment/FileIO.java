import java.util.*;
import java.io.*;

public class FileIO {  
    static final int N = 5;
    public static void main(String[] args) {
        ArrayList<Student> students = new ArrayList<Student>(N);
        for (int i = 0; i < N; i++) {
            int a, b, c;
            a = (int)(Math.random() * 100);
            b = (int)(Math.random() * 100);
            c = (int)(Math.random() * 100);
            students.add(new Student(a, b, c));
        }

        try (IO io = new IO("./tmp.txt")) {
            io.save(students);
            ArrayList<Student> loaded
             = new ArrayList<Student>();
            loaded = io.load(loaded);
            for (int i = 0; i < loaded.size(); i++) {
                System.out.println(loaded.get(i));
            }
            
        } catch (Exception e) {
            System.out.println("IO error: " + e.getMessage());
        }
    }
    

    
    
}

class Student {
    public Integer chinese, math, foreingLanguage;
    public Student(int chinese, int math, int foreingLanguage) {
        this.chinese = chinese;
        this.math = math;
        this.foreingLanguage = foreingLanguage;
    }
    public String toString() {
        return "Student [chinese=" + chinese + ", math=" + math + ", foreingLanguage=" + foreingLanguage + "]";
    }
}

class IO implements AutoCloseable{
    Scanner sc;
    PrintWriter pw;
    public IO(String filename) throws IOException {
        var f = new File("./data");
        var os = new FileOutputStream(f);
        var is = new FileInputStream(f);
        pw = new PrintWriter(os);
        sc = new Scanner(is);
    }
    @Override
    public void close() throws Exception{
        pw.close();
        sc.close();
    }
    public void save(ArrayList<Student> students) {
        for (int i = 0; i < students.size(); i++) {
            String s = students.get(i).chinese.toString() + " " + students.get(i).math.toString() + " " + students.get(i).foreingLanguage.toString() + " ";
            pw.println(s);
        }
        pw.flush();
        pw.close();
    }

    public ArrayList<Student> load(ArrayList<Student> students) {
        students = new ArrayList<Student>();
        while (sc.hasNextInt() && sc.hasNextLine()) {
            students.add(new Student(sc.nextInt(), sc.nextInt(), sc.nextInt()));
        }
        return students;
    }
}