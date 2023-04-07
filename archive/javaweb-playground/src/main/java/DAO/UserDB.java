package DAO;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import pojo.User;

import java.io.IOException;

public class UserDB {
    //Connect to DB


    public static boolean login(User user) throws IOException {
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("MyBatisCfg.xml"));
        var session = factory.openSession();
        var userDAO = session.getMapper(UserMapper.class);
        var userInDB = userDAO.getUserByUsername(user.getUsername());
        if (userInDB == null) {
            return false;
        }

        session.close();
        return userInDB.getPassword().equals(user.getPassword());
    }

    public static boolean addUser(User user) throws IOException {
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("MyBatisCfg.xml"));
        var session = factory.openSession();
        var userDAO = session.getMapper(UserMapper.class);
        var userInDB = userDAO.getUserByUsername(user.getUsername());
        if (userInDB != null) return false;
        userDAO.insertUser(user);
        session.commit();
        session.close();
        return true;
    }
}