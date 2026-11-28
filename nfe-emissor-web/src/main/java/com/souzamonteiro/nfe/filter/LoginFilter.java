package com.souzamonteiro.nfe.filter;

import com.souzamonteiro.nfe.model.Usuario;
import com.souzamonteiro.nfe.service.LojaService;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter {
    private static final List<String> PAGINAS_PUBLICAS = Arrays.asList(
        "/login.xhtml", "/error.xhtml", "/loja/index.xhtml", "/loja/cadastro.xhtml", "/loja/entrar.xhtml"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest http = (HttpServletRequest) request;
        HttpServletResponse resultado = (HttpServletResponse) response;
        String caminho = http.getServletPath();
        if ("/faces".equals(caminho)) caminho = http.getPathInfo() == null ? "" : http.getPathInfo();
        if (PAGINAS_PUBLICAS.contains(caminho) || caminho.startsWith("/javax.faces.resource/")) {
            chain.doFilter(request, response);
            return;
        }
        HttpSession sessao = http.getSession(false);
        Usuario usuario = sessao == null ? null : (Usuario) sessao.getAttribute("usuarioLogado");
        boolean loja = caminho.startsWith("/loja/");
        if (loja && "/loja/carrinho.xhtml".equals(caminho)) {
            chain.doFilter(request, response);
        } else if (usuario == null) {
            if ("/pdf".equals(caminho)) {
                resultado.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                resultado.sendRedirect(http.getContextPath() + (loja ? "/loja/entrar.xhtml" : "/login.xhtml"));
            }
        } else if ((loja && LojaService.cliente(usuario)) || (!loja && LojaService.equipe(usuario))
                || ("/pdf".equals(caminho) && LojaService.cliente(usuario))) {
            chain.doFilter(request, response);
        } else if (LojaService.cliente(usuario) && ("".equals(caminho) || "/index.xhtml".equals(caminho))) {
            resultado.sendRedirect(http.getContextPath() + "/loja/pedidos.xhtml");
        } else {
            resultado.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
