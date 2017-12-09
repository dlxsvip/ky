package com.ky.logic.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yl on 2017/8/15.
 */
public class IcsAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Value(value = "${login.successHandler.url}")
    private String LOCAL_SERVER_URL;

    public IcsAuthenticationSuccessHandler() {
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        request.getRequestDispatcher(LOCAL_SERVER_URL).forward(request, response);
    }
}
