package web;

import DAO.UserDB;
import com.alibaba.fastjson.JSON;
import pojo.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@WebServlet("/register")
public class ServletRegister extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(403);
        response.getWriter().println("<br> <h1> Get method is not support. </h1>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        InputStream is = request.getInputStream();
        String userJson = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        var user = JSON.parseObject(userJson, User.class);
        response.setStatus(200);
        if (UserDB.addUser(user)) {
            response.getWriter().println(true);
        } else {
            response.getWriter().println(false);
        }


    }
}
