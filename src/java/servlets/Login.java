/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import database.Conexao;
import database.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import contratos.*;
import authentication.ValidadeUser;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Login extends HttpServlet {

    private class User {

        public String userName;
        public String password;
    }

    private class Response {

        public String Token;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, IllegalStateException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            JsonReceiver<User> j = new JsonReceiver<User>(User.class);
            j.Desserealizar(request.getParameter("data"));
            
            User user = j.getData();
            
            JsonResult<Response> json = new JsonResult<>();
            
            try {
                if (!new Usuario().ValidaUsuario(user.userName, user.password)) {
                    json.mensagem = "Usuário ou senha inválido.";
                    json.sucesso = false;
                } else {
                    contratos.Usuario u = new Usuario().get(user.userName, user.password);

                    if (u != null) {
                        ValidadeUser v = new ValidadeUser();
                        String token = v.GerarToken(u.getEmail(), u);

                        Response r = new Response();
                        r.Token = token;

                        json.sucesso = true;
                        json.resultado = r;
                    } else {
                        json.mensagem = "Usuário ou senha inválido.";
                        json.sucesso = false;
                    }
                }
            } catch (Exception e) {
                json.sucesso = false;
                json.mensagem = e.toString();
            }
            out.println(json.Serializar());
        }
    }
}
