package com.Dream.util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;

//@WebServlet(urlPatterns = "/validateCode")
public class VerifyCodeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获得当前的会话，如果没有，则新建
        HttpSession httpSession = req.getSession();
        int width = 180;
        int height = 40;
        String imgType = "jpeg";
        OutputStream outputStream = resp.getOutputStream();
        String code = ValidateCode.generateCode(width, height, imgType, outputStream);
        httpSession.setAttribute("validateCode", code);
        System.out.println(code);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        doGet(req, resp);
    }
}
