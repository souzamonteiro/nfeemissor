package com.souzamonteiro.nfe.filter;

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

@WebFilter("*.xhtml")
public class LoginFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
                         FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        
        System.out.println("LoginFilter: Acessando URL: " + url);
        
        // URLs permitidas sem autenticação
        if (url.contains("/login.xhtml") || 
            url.contains("javax.faces.resource") ||
            url.contains("/error.xhtml") ||
            url.endsWith(contextPath + "/")) {
            
            System.out.println("LoginFilter: URL permitida sem autenticação");
            chain.doFilter(request, response);
            return;
        }
        
        // Verificar se usuário está na sessão
        Object usuarioLogado = req.getSession().getAttribute("usuarioLogado");
        
        if (usuarioLogado != null) {
            System.out.println("LoginFilter: Usuário autenticado");
            chain.doFilter(request, response);
        } else {
            System.out.println("LoginFilter: Usuário NÃO autenticado. Redirecionando para login.");
            res.sendRedirect(contextPath + "/login.xhtml");
        }
    }
    
    @Override
    public void init(FilterConfig config) throws ServletException {
        System.out.println("LoginFilter inicializado");
    }
    
    @Override
    public void destroy() {
        System.out.println("LoginFilter destruído");
    }
}