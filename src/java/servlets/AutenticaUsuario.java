/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import authentication.ValidadeUser;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author lucas
 */
public class AutenticaUsuario implements Filter {
    public class Token {
        public String userName;
        public String token;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        //chain.doFilter(request, response);
        try {
            ValidadeUser v = new ValidadeUser();

            contratos.Usuario usuario = v.ValidarTokenPeloRequest(httpRequest);
            
            //boolean eValido = v.ValidateToken(token.token, key);
            if (usuario == null) {
                httpResponse.sendError(401, "Usuário não autorizado.");
                return;
            } else {
                chain.doFilter(request, response);
                return;
            }
        } catch (Exception ex) {
            httpResponse.sendError(401, "Usuário não autorizado. Erro na autenticação: " + ex.toString());
            return;
        }
    }

    @Override
    public void destroy() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
