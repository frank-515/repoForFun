package com.example.sys.pojo;

import com.alibaba.fastjson.JSON;

import com.example.sys.pojo.Staff;

import java.io.Serializable;
import java.sql.Date;
import java.util.Scanner;
import lombok.Data;
@Data
public class Staff extends Person implements Serializable {

    private String ID, phoneNumber, address, postcode, qq, classify;
    private Integer idx = -1;

    public Staff() {};
    public Staff(String s) {

        Scanner sc = new Scanner(s);
        sc.next();
        setIdx(Integer.parseInt(sc.next()));
        setName(sc.next());
        setPhoneNumber(sc.next());
        String[] ymd = sc.next().split("-");
        Date d = new Date(Integer.parseInt(ymd[0]) - 1900, Integer.parseInt(ymd[1]) - 1, Integer.parseInt(ymd[2]));
        setBirth(d);
        setID(sc.next());
        setQq(sc.next());
        setAddress(sc.next());
        setClassify(sc.next());
        setPostcode(sc.next());
        setGender(sc.next());
    }
    public String toData() {
        String ret = "";
        ret += this.idx + " ";
        ret += this.name + " ";
        ret += this.phoneNumber + " ";
        ret += this.birth + " ";
        ret += this.ID + " ";
        ret += this.qq + " ";
        ret += this.address + " ";
        ret += this.classify + " ";
        ret += this.postcode + " ";
        ret += this.gender + " ";
        return ret;

    }
    public Staff(String name, String gender, Date birth, String ID, String phoneNumber, String address, String postcode, String qq, String classify) {
        super(name, gender, birth);
        this.ID = ID;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.postcode = postcode;
        this.qq = qq;
        this.classify = classify;
    }
    public String toJson() {
        return JSON.toJSONString(this);
    }
    @Override
    public String toString() {
        return "Staff{" +
                "ID='" + ID + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", postcode='" + postcode + '\'' +
                ", qq='" + qq + '\'' +
                ", classify='" + classify + '\'' +
                ", idx=" + idx +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", birth=" + birth +
                '}';
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return this.postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getClassify() {
        return this.classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }
}

class Person {
    public String name, gender;
    public Date birth;

    public Person(String name, String gender, Date birth) {
        this.name = name;
        this.gender = gender;
        this.birth = birth;
    }
    public Person() {};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }
}
