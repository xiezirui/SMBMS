package com.xiezirui.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPortType;
import com.xiezirui.pojo.Role;
import com.xiezirui.pojo.User;
import com.xiezirui.service.role.roleServiceImpl;
import com.xiezirui.service.user.userService;
import com.xiezirui.service.user.userServiceImpl;
import com.xiezirui.util.Constants;
import com.xiezirui.util.PageSupport;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class userServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        System.out.println(method);
        if (method.equals("savepwd") && method != null){
            this.updataPassword(req,resp);
        }else if (method.equals("pwdmodify") && method != null){
            this.pwdmodify(req,resp);
        }else if (method.equals("query") && method != null){
            this.query(req,resp);
        }else if (method.equals("add") && method != null){
            this.add(req,resp);
        }else if (method.equals("view") && method != null){
            this.getUserById(req,resp,"userview.jsp");
        }else if (method.equals("modify") && method != null){
            this.getUserById(req,resp,"usermodify.jsp");
        }else if (method.equals("deluser") && method != null){
            this.delUser(req,resp);
        }else if (method.equals("modifyexe") && method != null){
            this.modify(req,resp);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    public void updataPassword(HttpServletRequest req, HttpServletResponse resp){
        //???session??????id
        User user = (User) req.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = req.getParameter("newpassword");
        Boolean flag = false;
        if (user != null && !StringUtils.isNullOrEmpty(newpassword)){
            userServiceImpl userService = new userServiceImpl();
            flag = userService.updataPassword(user.getId(), newpassword);
            System.out.println(newpassword);
            if (flag){
                req.setAttribute(Constants.MESSAGE,"???????????????????????????????????????????????????");
                //???????????????????????????session
                System.out.println("===============");
                req.getSession().removeAttribute(Constants.USER_SESSION);
                System.out.println("===============");
            }else {
                req.setAttribute(Constants.MESSAGE,"??????????????????");
                System.out.println("false");
            }
        }else {
            req.setAttribute(Constants.MESSAGE,"??????????????????");
            System.out.println("false");
        }
        //req.getRequestDispatcher("pwdmodify.jsp").forward(req,resp);
        try {
            resp.sendRedirect("pwdmodify.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pwdmodify(HttpServletRequest req, HttpServletResponse resp){
        Object user =  req.getSession().getAttribute(Constants.USER_SESSION);
        String old_password = req.getParameter("oldpassword");
        HashMap<String, String> resultMap = new HashMap<String, String>();

        if (user == null){
            resultMap.put("result","sessionError");
        }else if (StringUtils.isNullOrEmpty(old_password)){//?????????????????????
            resultMap.put("result","error");
        }else {
            String session_oldPassword = ((User) user).getUserPassword();
            if (session_oldPassword.equals(old_password)){
                resultMap.put("result","true");
            }else {
                resultMap.put("result","false");


            }
        }
        //??????json
        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void query(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String queryname = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");

        int queryUserRole = 0;

        userServiceImpl userService = new userServiceImpl();
        roleServiceImpl roleService = new roleServiceImpl();

        int pageSive = 5;
        int currentPageNo = 1;

        //??????????????????????????????
        if (queryname == null){
            queryname = "";
        }
        //?????????????????????????????????
        System.out.println(temp);
        if (temp != null && !temp.equals("")){
            //?????????-->??????
            queryUserRole = Integer.parseInt(temp);
        }
        //????????????????????????
        if (pageIndex != null){
            try {
                currentPageNo = Integer.parseInt(pageIndex);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    resp.sendRedirect("error.jsp");
                } catch (IOException ex) { ex.printStackTrace(); }
            }

        }

        //??????????????????
        int totalCount = userService.userCount(queryname, queryUserRole);

        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSive);
        pageSupport.setTotalCount(totalCount);

        //??????????????????
        int totalPageCount = pageSupport.getTotalPageCount();

        if (currentPageNo < 1){//??????????????????1?????????????????????
            currentPageNo = 1;
        }else if (currentPageNo > totalPageCount){//???????????????????????????????????????????????????
            currentPageNo = totalPageCount;
        }

        //??????????????????
        List<User> userList = userService.getUserList(queryname, queryUserRole, currentPageNo, pageSive);
        List<Role> roleList = roleService.getRoleList();

        req.setAttribute("userList",userList);
        req.setAttribute("roleList",roleList);
        req.setAttribute("totalCount",totalCount);
        req.setAttribute("currentPageNo",currentPageNo);
        req.setAttribute("totalPageCount",totalPageCount);
        req.setAttribute("queryUserName", queryname);
        req.setAttribute("queryUserRole", queryUserRole);

        req.getRequestDispatcher("userlist.jsp").forward(req,resp);


    }

    public void add(HttpServletRequest req, HttpServletResponse resp){
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        String ruserPassword = req.getParameter("ruserPassword");

        if (ruserPassword.equals(userPassword)){
            User user = new User();

            user.setUserCode(userCode);
            user.setUserName(userName);
            user.setUserPassword(userPassword);
            user.setGender(Integer.parseInt(gender));

            try {
                user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            user.setPhone(phone);
            user.setAddress(address);
            user.setUserRole(Integer.parseInt(userRole));

            user.setCreationDate(new Date());
            user.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());

            userServiceImpl userService = new userServiceImpl();
            boolean add = userService.add(user);

            if(add){
                try {
                    resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    req.getRequestDispatcher("useradd.jsp").forward(req,resp);
                } catch (ServletException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    //??????????????????????????????Id????????????????????????????????????
    public void delUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String id = req.getParameter("uid");
        Integer delId = 0;
        try{
            delId = Integer.parseInt(id);
        }catch (Exception e) {
            delId = 0;
        }
        //?????????????????????????????????
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(delId <= 0){
            resultMap.put("delResult", "notexist");
        }else{
            userServiceImpl userService = new userServiceImpl();
            if(userService.deleteUserById(delId)){
                resultMap.put("delResult", "true");

            }else{
                resultMap.put("delResult", "false");
            }

        }

        //???resultMap?????????json????????????
        resp.setContentType("application/json");
        PrintWriter outPrintWriter = resp.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();
        outPrintWriter.close();
    }

    //??????id??????????????????
    public void getUserById(HttpServletRequest req, HttpServletResponse resp, String url) throws ServletException, IOException{

        String id = req.getParameter("uid");
        if(!StringUtils.isNullOrEmpty(id)){
            //????????????????????????user??????
            userServiceImpl userService = new userServiceImpl();
            User user = userService.getUserById(id);
            req.setAttribute("user", user);
            req.getRequestDispatcher(url).forward(req, resp);
        }
    }

    //??????????????????
    public void modify(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        //???????????????????????????????????????
        String id = req.getParameter("uid");;
        String userName = req.getParameter("userName");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        //????????????user????????????????????????
        User user = new User();
        user.setId(Long.valueOf(id));
        user.setUserName(userName);
        System.out.println("--" + gender);
        user.setGender(Integer.parseInt(gender));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.valueOf(userRole));
        user.setModifyBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());

        //??????service???
        userServiceImpl userService = new userServiceImpl();
        Boolean flag = null;
        try {
            flag = userService.modify(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //??????????????????????????????????????????????????????
        if(flag){
            resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
        }else{
            req.getRequestDispatcher("usermodify.jsp").forward(req, resp);
        }

    }

}
