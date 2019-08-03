package com.Dream.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        HttpSession session = request.getSession();
        String url = request.getRequestURI();
        if(!(url.contains("login") || url.contains("Login"))){
            // 不是登录请求，请求进入后台;
            Object user = session.getAttribute("user");
            if(user != null){
                return true;
            }else{
                Map<String, Object> map = new HashMap<>();
                map.put("status", "403");
                map.put("message","fail");
                map.put("info","用户未登录");
                //解决乱码问题
                response.setCharacterEncoding("utf-8");
                response.setHeader("Content-Type","text/html;charset=utf-8");
                String resultMessage = mapper.writeValueAsString(map);
                PrintWriter writer = response.getWriter();
                writer.print(resultMessage);
                writer.flush();
                response.flushBuffer();
                writer.close();
            }
        }else{
            return true;
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
