package com.sluka.taras.security;

import com.sluka.taras.service.UserService;
import com.sluka.taras.common.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Spring Security success handler, specialized for Ajax requests.
 */
@Component
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final Logger logger = LoggerFactory.getLogger(RestAuthenticationSuccessHandler.class);
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        response.setStatus(HttpServletResponse.SC_OK);
        writer.flush();
        writer.close();
    }
}
