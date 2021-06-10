package com.acrobat.ztb.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 登录拦截请求，如果未登录
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static final List<String> whiteList = new ArrayList<>();

    static {
        whiteList.add("/user/sign");
        whiteList.add("/user/sign/req");
        whiteList.add("/user/login");
        whiteList.add("/user/login/req");
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
//        String uri = request.getRequestURI();       // uri带了context path前缀

//        // 白名单放行
//        String path = request.getServletPath();
//        if (whiteList.contains(path)) return true;

        // 检查用户是否登录
        Object userId = request.getSession(true).getAttribute("userId");
        if (userId == null)  {
            // 用户未登录，重定向到登录页
            response.sendRedirect(request.getContextPath() + "/user/login");
            return false;  
        }
        return true;
    }  
}
