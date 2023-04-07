import DAO.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import pojo.User;

import java.io.IOException;

public class TestClass {
    @Test
    public void testDB() throws IOException {
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("MyBatisCfg.xml"));
        var session = factory.openSession();
        var userDAO = session.getMapper(UserMapper.class);

    }

}
