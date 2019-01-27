package ru.geekbrains.pocket.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.geekbrains.pocket.backend.domain.db.User;
import ru.geekbrains.pocket.backend.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        System.out.println("\n\nIn customAuthenticationSuccessHandler\n\n");

        String emailUser = authentication.getName();

        System.out.println("userName=" + emailUser);

        User user = userService.getUserByEmail(emailUser);

        // now place in the session
        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        // forward to home page

        response.sendRedirect(request.getContextPath() + "/");
    }
}