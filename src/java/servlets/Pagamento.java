package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import database.Conexao;
import contratos.*;
import java.sql.*;
import java.util.*;
import authentication.ValidadeUser;

public class Pagamento extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            contratos.JsonReceiver2 j = new JsonReceiver2();

            JsonReceiver<JsonReceiver2> json = new JsonReceiver<JsonReceiver2>(JsonReceiver2.class);
            String dataRequest = request.getParameter("data");
            String receiveJson = "";
            if (!dataRequest.equals("") && dataRequest != null) {
                json.Desserealizar(request.getParameter("data"));
                receiveJson = json.getData().getJson();
            }
            String resposta = "";

            switch (json.getData().getOperacao()) {
                case INSERIR: {
                    break;
                }
                case REMOVER: {
                    break;
                }
                case EDITAR: {
                    break;
                }
                case LER: {

                    break;
                }
                case LERVARIOS: {
                    resposta = lervarios(receiveJson);
                    break;
                }
                case SALVARVARIOS: {
                    resposta = salvarVarios(receiveJson);
                    break;
                }
            }

            out.print(resposta);
        }
    }

    public String salvarVarios(String receiveJson) {
        database.Pagamento pg = new database.Pagamento();

        JsonReceiver<contratos.Pagamento> pagamentos = new JsonReceiver<>(contratos.Pagamento.class);
        pagamentos.Desserealizar(receiveJson);
        
        JsonResult<Boolean> json = new JsonResult<Boolean>();
        
        try {
            json.resultado = pg.salvarVarios(pagamentos.getData());
            json.sucesso = true;
        } catch (Exception ex) {
            json.sucesso = false;
            json.mensagem = "Não foi possível carregar os pagamentos. " + ex.toString();
        }

        return json.Serializar();
    }

    public String lervarios(String receiveJson) {
        database.Pagamento pg = new database.Pagamento();

        JsonResult<List<contratos.Semana>> json = new JsonResult<>();

        try {
            json.resultado = pg.GetMonthPayments();
            json.sucesso = true;
        } catch (Exception ex) {
            json.sucesso = false;
            json.mensagem = "Não foi possível carregar os pagamentos. " + ex.toString();
        }

        return json.Serializar();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, IllegalStateException {
        processRequest(request, response);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, IllegalStateException {
        processRequest(request, response);
    }
}
