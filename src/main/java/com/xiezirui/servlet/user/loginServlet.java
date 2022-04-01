package com.xiezirui.servlet.user;

import com.xiezirui.pojo.User;
import com.xiezirui.service.user.userServiceImpl;
import com.xiezirui.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class loginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");

        userServiceImpl userServiceImpl = new userServiceImpl();
        User loginUser = userServiceImpl.login(userCode, userPassword);

        if (loginUser != null){//查询到了这个人
            //将用户的信息放到session中
            req.getSession().setAttribute(Constants.USER_SESSION, loginUser);
            //跳转到frame.jsp
            resp.sendRedirect("jsp/frame.jsp");
        }else {//没有查询到这个人
            //跳转到登录页面，提示登录失败
            req.setAttribute("error","用户名或密码不正确");
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
