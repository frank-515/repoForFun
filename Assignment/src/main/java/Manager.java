import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Manager {
    public static Scanner scanner = new Scanner(System.in);
    public static ArrayList<Student> students = new ArrayList<>();

	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException {
		File f = new File("学生信息.txt");
		ArrayList<Student> students;
		if (!f.exists()) {
			f.createNewFile();
			students = new ArrayList<>();
		} else {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
			students = (ArrayList<Student>) ois.readObject();
		}
		
		
		
		while(true){
			System.out.println("------------欢迎使用学生成绩管理系统--------------------");
			System.out.println("---------------【1】录入学生信息-----------------------");
			System.out.println("---------------【2】学号查找学生成绩-------------------");
			System.out.println("---------------【3】姓名、班级查找学生成绩--------------");
			System.out.println("---------------【4】修改学生信息------------------------");
			System.out.println("---------------【5】删除学生信息------------------------");
			System.out.println("---------------【6】排序学生学号------------------------");
			System.out.println("---------------【7】显示所有信息------------------------");
			System.out.println("---------------【8】按照班级、科目统计平均分-------------");
			System.out.println("---------------【0】退出管理系统-------------------------");

			Student2 student2 = new Student2();

			System.out.println("请输入你的选择【0~8】");
			String choice = scanner.next();

			switch(choice){
			case "1":
				//录入学生信息
				student2.addScore(students);
				break;
			case "2":
				//查找学生成绩
				student2.lookupStudentbynum(students);
				break;
			case "3":
				//查找学生成绩
				student2.lookupStudentbyname(students);
				break;
			case "4":
				//修改学生成绩
				student2.modifyStudent(students);
				break;
			case "5":
				//删除学生信息
				student2.deleteStudent(students);
				break;
			case "6":
				//排序学生学号
				student2.sortStudent(students);
				break;
			case "7":
				//显示所有成绩
				student2.showAllStudent(students);
				break;
			case "8":
				//按照班级统计人数
				student2.countStudent(students);
				break;
			case "0":
				//退出管理系统
				System.exit(0);
				break;
			default:
				break;
			}
		}

	}
}

