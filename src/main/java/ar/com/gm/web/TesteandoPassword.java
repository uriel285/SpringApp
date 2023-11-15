package ar.com.gm.web;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class TesteandoPassword extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String formData = "";
        if ("POST".equalsIgnoreCase(req.getMethod())) {
            // Supongamos que 'username' y 'password' son los nombres de los campos de tu formulario
            formData = "username: " + req.getParameter("username") + ", password: " + req.getParameter("password");
        }
        System.out.println("Request data: " + formData);
        chain.doFilter(request, response);
    }
}
