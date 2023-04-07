package web;

import DAO.UserDB;
import pojo.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/ServletVerifyUser")
public class ServletVerifyUser extends HttpServlet {
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
        verify(request, response, username, password, user);
    }

    static void verify(HttpServletRequest request, HttpServletResponse response, String username, String password, User user) throws IOException {
        if (username == null || password == null || !UserDB.login(user)) {
            response.setStatus(302);
            response.setHeader("Location", "/playground/noLogin.html");
        } else {
            response.setStatus(302);
            response.setHeader("Location", request.getParameter("uri"));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var cookies = request.getCookies();
        String username = null, password = null;
        for (var cookie : cookies) {
            switch (cookie.getName()) {
                case "username" -> username = cookie.getValue();
                case "password" -> password = cookie.getValue();
            }
        }
        if (username == null || password == null) {
            response.setStatus(-1);
            return;
        }
        User user = new User(username, password);
        if (UserDB.login(user)) {
            response.setStatus(200);
        } else {
            response.setStatus(-1);
        }
        return;


    }
}
