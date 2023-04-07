import DAO.UserDB;
import org.junit.Test;
import pojo.User;

import java.io.IOException;

public class TestLogin {
    @Test
    public void testLogin() throws IOException {
        User user = new User("ff", "ff");
        user.setUsername("ff");
        System.out.println(user.getPassword());
        user.setPassword(user.getPassword());
        System.out.println(UserDB.login(user));
    }
}
