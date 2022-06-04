import java.io.Serializable;

public class Student extends Person{
private static final long serialVersionUID = 1L;
private long studynum;
private String grade;
private float math;
private float english;
private float sport;
private float java;
private float total;
private double average;
private java.util.Date date;


Student(String name, String sex, long idcard, String date_birth){
	super(name, sex, idcard, date_birth);
}
Student(String name, String sex, long idcard, String date_birth,long studynum,
		String grade,float math,float english,float sport,float java,float total,double average) {
		super(name, sex, idcard, date_birth);
		this.studynum = studynum;
		this.grade = grade;
		this.math = math;
		this.english = english;
		this.sport = sport;
		this.java = java;
		this.average = average;
		date = new java.util.Date();
	}

public long getstudynum(){
	return studynum;
}
public void setstudynum(long studynum){
	this.studynum = studynum;
}
public String getgrade(){
	return grade;
}
public void setgrade(String grade){
	this.grade = grade;
}
public float getmath(){
	return math;
}
public void setmath(float math){
	this.math = math;
}
public float getenglish(){
	return english;
}
public void setenglish(float english){
	this.english = english;
}
public float getsport(){
	return sport;
}
public void setsport(float sport){
	this.sport = sport;
}
public float getjava(){
	return java;
}
public void setjava(float java){
	this.java = java;
}
public float gettotal() {
	return total;
}
public void settotal(float total) {
	this.total = total;
}
public double getaverage(){
	return average;
}
public void setaverage(double average){
	this.average = average;
}
public java.util.Date getDate() {
	return date;
}
public void setDate(java.util.Date  date){
	this.date = date;
}
@Override
public String toString() {
	return "Student [ name =" + name + ", sex =" + sex + ", idcard =" + idcard + " date_birth =" + date_birth +  ", studynum =" + studynum + ", grade=" + grade + ", math=" + math 
			+ ", english=" + english + ", sport=" + sport + ", java=" + java + ", total=" + total + ", average=" + average + ", date=" + date + "]";
}

}
