package DAO;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import pojo.User;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {

    @Insert("insert into loginDB (username, password) values (#{username}, #{password})")
    void insertUser(User user);

    @Select("select * from loginDB where username = #{username}")
    User getUserByUsername(String username);

}
