package com.raissapayments.conector.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class DataSourceInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String idDsEmpresa = request.getHeader("idDsEmpresa");

        if (idDsEmpresa != null) {
            DataSourceContextHolder.setDataSourceKey(idDsEmpresa);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        DataSourceContextHolder.clearDataSourceKey();
    }
}