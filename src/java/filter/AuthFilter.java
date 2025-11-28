// AuthFilter.java - na pasta src/filter/
package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialização do filter (opcional)
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        
        boolean loggedIn = (session != null && session.getAttribute("medico") != null);
        boolean loginRequest = requestURI.equals(contextPath + "/login");
        boolean loginPost = requestURI.equals(contextPath + "/login") && "POST".equalsIgnoreCase(httpRequest.getMethod());
        boolean resourceRequest = requestURI.startsWith(contextPath + "/css/") || 
                                 requestURI.startsWith(contextPath + "/js/") ||
                                 requestURI.startsWith(contextPath + "/images/");
        
        // URLs públicas que não requerem autenticação
        boolean publicResource = loginRequest || loginPost || resourceRequest;
        
        if (loggedIn || publicResource) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(contextPath + "/login");
        }
    }

    @Override
    public void destroy() {
        // Cleanup do filter (opcional)
    }
}