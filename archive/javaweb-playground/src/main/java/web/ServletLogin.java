package web;

import DAO.UserDB;
import DAO.UserMapper;
import com.alibaba.fastjson.JSON;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import pojo.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@WebServlet("/login")
public class ServletLogin extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var cookies = request.getCookies();
        String username = null, password = null;
        for (var cookie : cookies) {
            switch (cookie.getName()) {
                case "username" -> username = cookie.getValue();
                case "password" -> password = cookie.getValue();
            }
        }
        User user = new User(username, password);
        user.setPassword(user.getPassword());
        ServletVerifyUser.verify(request, response, username, password, user);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        InputStream is = request.getInputStream();
        String userJson = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        var user = JSON.parseObject(userJson, User.class);
        user.setPassword(user.getPassword());
        var loginStat = UserDB.login(user);
        response.setStatus(200);
        response.getWriter().println(loginStat);
    }
}
