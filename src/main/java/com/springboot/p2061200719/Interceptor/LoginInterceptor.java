package com.springboot.p2061200719.Interceptor;

import com.springboot.p2061200719.Utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

public class LoginInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取url
        logger.info("请求url："+request.getRequestURL());

        //获取请求参数
        String queryString = request.getQueryString();
        logger.info("请求参数：{}",queryString);

        //获取请求体
        byte[] bodyBytes = StreamUtils.copyToByteArray(request.getInputStream());
        String body = new String(bodyBytes,request.getCharacterEncoding());
        logger.info("请求体：{}",body);

        //获取请求头的token
        String token = request.getHeader("token");
        logger.info("token："+token);
        //验证token
        try {
            Map<String,Object> map = JwtUtil.parseToken(token);
            logger.info("token正在生效...");
            //放行
            return true;
        }catch (Exception e){
            //http响应状态码401
            response.setStatus(401);
            logger.info("token失效！！！");
            //不放行
            return false;
        }
    }
}