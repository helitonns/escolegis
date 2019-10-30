package br.leg.alrr.cursos.filter;

import br.leg.alrr.cursos.business.TipoUsuario;
import br.leg.alrr.cursos.model.Autorizacao;
import br.leg.alrr.cursos.model.UsuarioComUnidade;
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

@WebFilter("/pages/*")
public class NavegacaoFilter implements Filter {

    public NavegacaoFilter() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession httpSession = ((HttpServletRequest) request).getSession();
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (httpServletRequest.getRequestURI().indexOf("index.xhtml") <= -1) {

            UsuarioComUnidade u = (UsuarioComUnidade) httpSession.getAttribute("usuario");
            if (u == null) {
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/index.xhtml");
            } else {
                Autorizacao autorizacao = (Autorizacao) httpSession.getAttribute("autorizacao");
                
                if (autorizacao.getPrivilegio().getDescricao().equals("RELATORIO") && (httpServletRequest.getRequestURI().contains("superadmin") || httpServletRequest.getRequestURI().contains("admin") || httpServletRequest.getRequestURI().contains("user"))) {
                    httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/pages/relatorio/relatorio-geral.xhtml");
                } else if (autorizacao.getPrivilegio().getDescricao().equals("OPERADOR") && (httpServletRequest.getRequestURI().contains("admin") || httpServletRequest.getRequestURI().contains("superadmin"))) {
                    httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/pages/user/verificar-cpf.xhtml");
                } else if (autorizacao.getPrivilegio().getDescricao().equals("ADMIN") && httpServletRequest.getRequestURI().contains("superadmin")) {
                    httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/pages/user/verificar-cpf.xhtml");
                } else {
                    chain.doFilter(request, response);
                }

            }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
    }

}
