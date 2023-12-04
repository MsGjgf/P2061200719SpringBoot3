package com.springboot.p2061200719.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @summary 自定义 DispatcherServlet 来分派 NewHttpServletRequestWrapper
 */
public class NewDispatcherServlet extends DispatcherServlet {

    /**
     * 包装成我们自定义的request
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.doDispatch(new NewHttpServletRequestWrapper(request), response);
    }
}
