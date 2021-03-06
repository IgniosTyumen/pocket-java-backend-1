package ru.geekbrains.pocket.backend.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

//https://www.baeldung.com/securing-a-restful-web-service-with-spring-security

@Component
public class MySavedRequestAwareAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws ServletException, IOException {
        final SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest == null) {

            String uri = request.getRequestURI();
            if (uri.equals("/login") || uri.equals("/authenticateTheUser") || uri.startsWith("/authenticateTheUser"))
                super.onAuthenticationSuccess(request, response, authentication);
            else
                clearAuthenticationAttributes(request);
            //response.sendRedirect(request.getContextPath() + "/login");


            return;
        }
        final String targetUrlParameter = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl() || (targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
            requestCache.removeRequest(request, response);
            String uri = request.getRequestURI();
            if (uri.equals("/login") || uri.equals("/authenticateTheUser") || uri.startsWith("/authenticateTheUser"))
                super.onAuthenticationSuccess(request, response, authentication);
            else
                clearAuthenticationAttributes(request);
            return;
        }

        clearAuthenticationAttributes(request);

        // Use the DefaultSavedRequest URL
        // final String targetUrl = savedRequest.getRedirectUrl();
        // logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
        // getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    public void setRequestCache(final RequestCache requestCache) {
        this.requestCache = requestCache;
    }
}
