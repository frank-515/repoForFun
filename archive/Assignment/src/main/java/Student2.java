import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Student2 {
	private static final String[][] String = null;
	public static Scanner scanner = new Scanner(System.in);
   // public static ArrayList<Student> students = new ArrayList<>();


	public void addScore(ArrayList<Student> students){
		 ObjectOutputStream oos = null;
         try{
         oos = new ObjectOutputStream(new FileOutputStream("学生信息.txt"));
		 System.out.println("----------输出学生统计数据----------");
		 System.out.println("请依次输入如下数据：");
		 long studynum;
		 while(true){
			 System.out.println("学号");
			 studynum = scanner.nextLong();
			 if(isValid(students,studynum)){
				 System.out.println("学号重复，请重新输入");
			 }else{
				 break;
			 }
		 }

			System.out.println("姓名");
		    String name = scanner.next();
		    System.out.println("性别");
		    String sex = scanner.next();
		    System.out.println("身份证号码");
		    long idcard = scanner.nextLong();
		    System.out.println("出生年月");
		    String date_birth = scanner.next();
		    System.out.println("班级");
		    String grade = scanner.next();
		    System.out.println("数学");
		    float math = scanner.nextFloat();
		    System.out.println("英语");
		    float english = scanner.nextFloat();
		    System.out.println("体育");
		    float sport = scanner.nextFloat();
		    System.out.println("java");
		    float java = scanner.nextFloat();
		    float total =  math + english + sport +java;
		    float average = total / 4;


		    Student student =  new Student(name,sex,idcard,date_birth,
		    studynum,grade,math,english,sport,java,total,average);
            student.setname(name);
            student.setsex(sex);
		    student.setidcard(idcard);
		    student.setdate_birth(date_birth);
		    student.setstudynum(studynum);
		    student.setgrade(grade);
		    student.setmath(math);
		    student.setenglish(english);
		    student.setsport(sport);
		    student.setjava(java);
		    student.settotal(total);
		    student.setaverage(average);

		    students.add(student);//添加到集合
		    System.out.println("添加成功！");
		    oos.writeObject(students);
	 }catch(Exception e){
		 e.printStackTrace();
	 }finally{
		 try{
			 if(oos != null){
				 oos.close();
			 }
		 }catch(Exception e){
			 e.printStackTrace();
		 }
	 }


}


public void showAllStudent(ArrayList<Student> students) throws FileNotFoundException, IOException, ClassNotFoundException{
	for(Student s: students){
		System.out.println(s);
	}
	}


public void lookupStudentbynum (ArrayList<Student> students){
	System.out.println("----------查找学生成绩----------");
	if(students.size() == 0){
		System.out.println("当前无数据，请添加后重试！");
	}else {
		System.out.println("请输入要查找学生的学号:");
		long studynum1 = scanner.nextLong();
		int flag = -1;
		for(Student s:students){
			if((s.getstudynum()) == studynum1){
				flag = 1;
				System.out.println(s);
				break;
			}
		}
		if(flag == -1){
			 System.out.println("未找到对应学号的学生，请确认后重新输入！");
		}
	}
}

public void lookupStudentbyname (ArrayList<Student> students){
	System.out.println("----------查找学生成绩----------");
	if(students.size() == 0){
		System.out.println("当前无数据，请添加后重试！");
	}else {
		System.out.println("请输入要查找学生的姓名、班级:");
		String name1 = scanner.next();
		String grade1 = scanner.next();
		int flag = -1;
		for(Student s:students){
			if((s.getname()) == name1 || (s.getgrade()==grade1)){
				flag = 1;
				System.out.println(s);
				break;
			}
		}
		if(flag == -1){
			 System.out.println("未找到对应学号的学生，请确认后重新输入！");
		}
	}
}


public void modifyStudent (ArrayList<Student> students){
	System.out.println("----------修改学生信息----------");
	if(students.size() == 0){
		System.out.println("当前无数据，请添加后重试！");
	}else {
		System.out.println("请输入要修改学生的学号:");
		long studynum1 = scanner.nextLong();
		int flag = -1;
		for(Student s:students){
			if((s.getstudynum()) == studynum1){
				flag = 1;
			}

			System.out.println("------------欢迎使用学生修改系统---------------");
			System.out.println("---------------【1】修改姓名-------------------");
			System.out.println("---------------【2】修改性别-------------------");
			System.out.println("---------------【3】修改身份证号码--------------");
			System.out.println("---------------【4】修改出生年月----------------");
			System.out.println("---------------【5】修改学号--------------------");
			System.out.println("---------------【6】修改班级--------------------");
			System.out.println("---------------【7】修改数学成绩----------------");
			System.out.println("---------------【8】修改英语成绩----------------");
			System.out.println("---------------【9】修改体育成绩----------------");
			System.out.println("---------------【10】修改java成绩----------------");
			System.out.println("---------------【0】退出修改系统-----------------");
			System.out.println("请输入你的选择【0~10】");
			String choice = scanner.next();

			switch(choice){
			case "1":
				s.setname(scanner.next());
				break;
			case "2":
				s.setsex(scanner.next());
				break;
			case "3":
				s.setidcard(scanner.nextLong());
				break;
			case "4":
				s.setdate_birth(scanner.next());
				break;
			case "5":
				s.setstudynum(scanner.nextLong());
				break;
			case "6":
				s.setgrade(scanner.next());
				break;
			case "7":
				s.setmath(scanner.nextFloat());
				break;
			case "8":
				s.setenglish(scanner.nextFloat());
				break;
			case "9":
				s.setsport(scanner.nextFloat());
				break;
			case "10":
				s.setenglish(scanner.nextFloat());
				break;
			case "0":
				//退出系统
				System.exit(0);
				break;
			default:
				break;
			}
			break;
		}
		if(flag == -1){
			 System.out.println("未找到对应学号的学生，请确认后重新输入！");
		}
	}
	 System.out.println("修改成功！");
}


public void deleteStudent (ArrayList<Student> students){
	System.out.println("----------删除学生信息----------");
	if(students.size() == 0){
		System.out.println("当前无数据，请添加后重试！");
	}else {
		System.out.println("请输入要删除学生的学号:");
		long studynum1 = scanner.nextLong();
		int flag = -1;
		for(Student s:students){
			if((s.getstudynum()) == studynum1){
				flag = 1;
			}

			System.out.println("------------欢迎使用学生删除系统----------------");
			System.out.println("---------------【1】修改数学成绩----------------");
			System.out.println("---------------【2】删除英语成绩----------------");
			System.out.println("---------------【3】删除体育成绩----------------");
			System.out.println("---------------【4】删除java成绩----------------");
			System.out.println("---------------【0】退出删除系统-----------------");
			System.out.println("注意：当成绩为0时，删除成功！！！");
			System.out.println("请输入你的选择【0~4】");
			String choice = scanner.next();

			switch(choice){
			case "1":
				s.setmath(0);
				break;
			case "2":
				s.setenglish(0);
				break;
			case "3":
				s.setsport(0);
				break;
			case "4":
				s.setjava(0);
				break;
			case "0":
				//退出管理系统
				System.exit(0);
				break;
			default:
				break;
			}
			break;
		}
		if(flag == -1){
			 System.out.println("未找到对应学号的学生，请确认后重新输入！");
		}
	}
	 System.out.println("删除成功！");
}


public void sortStudent(ArrayList<Student> students){
	System.out.println("----------删除学生信息----------");
	if (students.isEmpty()) {
        System.out.println("当前无数据，请添加后重试！");
    }else{
    	for (int i = 0; i < students.size() - 1; i++){
    		if(students.get(i).getstudynum() > students.get(i+1).getstudynum()){
    			Student temp = students.get(i);
    			students.set(i, students.get(i + 1));
                students.set(i + 1, temp);
    		}
    	}
    	for (Student s:students){
    		System.out.println(s);
    	}
    }
	 System.out.println("排序成功！");
}


public void countStudent (ArrayList<Student> students){
	System.out.println("------------按照班级、科目统计平均分------------");
	if (students.isEmpty()) {
        System.out.println("当前无数据，请添加后重试！");
	}else{
		Set<String> set = new HashSet<String>();
		for(Student s:students){
		   set.add(s.getgrade());
		}
		int n = 0;
		int i = 0;
		n = set.size();
		String []string = new String[n];
		int[] nums = new int[n];
		for(String st: set){
			string[i] = st;
			i++;
		}
		float mathscore = 0;
		float englishscore = 0;
		float sportscore = 0;
		float javascore = 0;
		for(int j=0;j<string.length;j++){
			for(Student stu:students){
				if(string[j].equals(stu.getgrade())){
					mathscore = stu.getmath() + mathscore;
					englishscore = stu.getenglish() + englishscore;
					sportscore = stu.getsport() + sportscore;
					javascore = stu.getjava() + javascore;
					nums[j]++;
				}
			}
			System.out.println("班级:"+string[j]+"\t有"+nums[j]+"人");
			System.out.println("-----------------------------------");
			System.out.println("班级:"+string[j]+"\t数学平均分为:"+ mathscore / nums[j]);
			System.out.println("班级:"+string[j]+"\t英语平均分为:"+ englishscore / nums[j]);
			System.out.println("班级:"+string[j]+"\t体育平均分为:"+ sportscore / nums[j]);
			System.out.println("班级:"+string[j]+"\tjava平均分为:"+ javascore / nums[j] +"\n");
			mathscore = 0;
			englishscore = 0;
			sportscore = 0;
			javascore = 0;
		}

	}
}


public boolean isValid(ArrayList<Student> students, long studynum) {
    for (Student student : students) {
        if (student.getidcard() == studynum) {
            return true;
        }
    }
    return false;
}

}