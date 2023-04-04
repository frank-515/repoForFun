import java.io.Serializable;

public class Person implements Serializable{
	private static final long serialVersionUID = 1L;
    protected String name;
    protected String sex;
    protected long idcard;
    protected String date_birth;
    

Person(String name,String sex,long idcard,String date_birth){
	this.name = name;
	this.sex = sex;
	this.idcard = idcard;
	this.date_birth = date_birth;
}
public String getname(){
	return name;
}
public void setname(String name){
	this.name = name;
}
public String getsex(){
	return sex;
}
public void setsex(String sex){
	this.sex = sex;
}
public long getidcard(){
	return idcard;
}
public void setidcard(long idcard){
	this.idcard = idcard;
}
public String getdate_birth(){
	return date_birth;
}
public void setdate_birth(String date_birth){
	this.date_birth = date_birth;
}
}
