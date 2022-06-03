package com.example.sys.db;


import com.example.sys.pojo.Staff;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffDb {
    private Connection conn;
    private Statement stmt;
    public final static String URL = "jdbc:mysql:///mainDB";
    public final static String password = "frank515";
    public final static String username = "root";
    public StaffDb() throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(URL, username, password);
        stmt = conn.createStatement();


    }
    private List<Staff> sqlToObj(ResultSet rs) throws Exception {
        List<Staff> ret = new ArrayList<Staff>();
        while (rs.next()) {
            Staff curr = new Staff();
            curr.setIdx(rs.getInt("idx"));
            curr.setID(rs.getString("ID"));
            curr.setQq(rs.getString("qq"));
            curr.setAddress(rs.getString("address"));
            curr.setClassify(rs.getString("classify"));
            curr.setPostcode(rs.getString("postcode"));
            curr.setName(rs.getString("name"));
            curr.setBirth(rs.getDate("birth"));
            curr.setGender(rs.getString("gender"));
            curr.setPhoneNumber(rs.getString("phoneNumber"));
            ret.add(curr);
        }
        return ret;
    }
    public List<Staff> selectAll() throws Exception {
        ResultSet rs = stmt.executeQuery("SELECT * FROM mainTable;");
        return sqlToObj(rs);
    }
    public void add(Staff s) throws Exception{
        String sql;
        sql = String.format("insert into mainTable (ID, address, postcode, qq, classify, name, gender, phonenumber, birth) value (\"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\");", s.getID(),  s.getAddress(), s.getPostcode(), s.getQq(), s.getClassify(), s.getName(), s.getGender(),s.getPhoneNumber(), s.getBirth());
        try {
            stmt.execute(sql);
            stmt.execute("commit;");
        } catch (Exception e) {
            stmt.execute("rollback;");
            throw e;
        }
    }
    public List<Staff> selectPhonenumber(String phoneNumber) throws Exception{
        String sql;
        sql = String.format("select * from mainTable where phoneNumber = \"%s\"", phoneNumber);
        System.out.println(sql);
        return sqlToObj(stmt.executeQuery(sql));
    }
    public List<Staff> selectName(String name) throws Exception{
        String sql;
        sql = String.format("select * from mainTable where name = \"%s\"", name);
        var s = stmt.executeQuery(sql);
        return sqlToObj(s);
    }
    public List<Staff> selectClassify(String classify) throws Exception{
        String sql;
        sql = String.format("select * from mainTable where classify = \"%s\"", classify);
        return sqlToObj(stmt.executeQuery(sql));
    }
    public List<Staff> selectAddress(String address) throws Exception {
        String sql;
        sql = "select * from mainTable where address like \"" + "%"+ address + "%" + "\"";
        return sqlToObj(stmt.executeQuery(sql));
    }
    public void deleteName(String name) throws Exception{
        String sql;
        sql = String.format("delete from mainTable where name = \"%s\"", name);
        try {
            stmt.execute(sql);
            stmt.execute("commit;");
        } catch (Exception e) {
            stmt.execute("rollback;");
            throw e;
        }
    }
    public void deletePhonenumber(String phoneNumber) throws Exception{
        String sql;
        sql = String.format("delete from mainTable where name = \"%s\"", phoneNumber);
        try {
            stmt.execute(sql);
            stmt.execute("commit;");
        } catch (Exception e) {
            stmt.execute("rollback;");
            throw e;
        }
    }
    public void update(int idx, Staff s) throws Exception{
        String sql;
        sql = String.format("update mainTable set ID=\"%s\", phoneNumber=\"%s\", address=\"%s\", postcode=\"%s\", qq=\"%s\", classify=\"%s\", name=\"%s\", gender=\"%s\", birth=%s where idx = %s", s.getID(), s.getPhoneNumber(), s.getAddress(), s.getPostcode(), s.getQq(), s.getClassify(), s.getName(), s.getGender(), s.getBirth().toString(), idx);
        stmt.execute(sql);
    }


}
