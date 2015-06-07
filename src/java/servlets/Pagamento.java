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
import static java.net.Proxy.Type.HTTP;

public class Pagamento extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {

            contratos.JsonReceiver2 j = new JsonReceiver2();

            JsonReceiver<JsonReceiver2> json = new JsonReceiver<JsonReceiver2>(JsonReceiver2.class);
            String dataRequest = request.getParameter("data");
            String receiveJson = "";
            if (!dataRequest.equals("") && dataRequest != null) {
                json.Desserealizar(request.getParameter("data"));
                receiveJson = json.getData().getJson();
            }

            String value = new String(receiveJson.getBytes("UTF-8"), "UTF-8");

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
                case SALVARDEBITO: {
                    resposta = salvarDebito(receiveJson);
                    break;
                }
                case PAGARUNICO: {
                    resposta = pagarUnico(receiveJson);
                    break;
                }
                case PAGAMENTOMENSALFORNECEDOR: {
                    resposta = pagamentoFornecedorMensal(receiveJson);
                    break;
                }
                case PAGARDEBITO: {
                    resposta = pagarDebito(receiveJson);
                    break;
                }
            }

            out.print(resposta);
        }
    }

    public String pagarDebito(String receiveJson) {
        database.Pagamento pg = new database.Pagamento();

        JsonResult<Boolean> json = new JsonResult<Boolean>();

        try {
            JsonReceiver<contratos.Pagamento2> pagamento = new JsonReceiver<>(contratos.Pagamento2.class);
            pagamento.Desserealizar(receiveJson);

            json.resultado = pg.pagarDebito(pagamento.getData());
            json.sucesso = true;
        } catch (Exception ex) {
            json.sucesso = false;
            json.mensagem = "Não foi possível obter as informações dos fornecedores. " + ex.toString();
        }

        return json.Serializar();
    }

    public String pagamentoFornecedorMensal(String receiveJson) {
        database.Pagamento pg = new database.Pagamento();

        JsonReceiver<Integer> pagamento = new JsonReceiver<>(Integer.class);
        pagamento.Desserealizar(receiveJson);

        JsonResult<List<contratos.PagamentoFornecedor>> json = new JsonResult<List<contratos.PagamentoFornecedor>>();

        try {
            json.resultado = pg.pagamentoFornecedorMensal(pagamento.getData());
            json.sucesso = true;
        } catch (Exception ex) {
            json.sucesso = false;
            json.mensagem = "Não foi possível obter as informações dos fornecedores. " + ex.toString();
        }

        return json.Serializar();
    }

    public String pagarUnico(String receiveJson) {
        database.Pagamento pg = new database.Pagamento();

        JsonReceiver<contratos.Pagamento2> pagamento = new JsonReceiver<>(contratos.Pagamento2.class);
        pagamento.Desserealizar(receiveJson);

        JsonResult<Boolean> json = new JsonResult<Boolean>();

        try {
            json.resultado = pg.pagarUnico(pagamento.getData());
            json.sucesso = true;
        } catch (Exception ex) {
            json.sucesso = false;
            json.mensagem = "Não foi possível realizar o pagamento. " + ex.toString();
        }

        return json.Serializar();
    }

    public String salvarDebito(String receiveJson) {
        database.Pagamento pg = new database.Pagamento();
        JsonResult<Boolean> json = new JsonResult<Boolean>();

        try {
            JsonReceiver<contratos.DebitoAutomatico> pagamentos = new JsonReceiver<>(contratos.DebitoAutomatico.class);
            pagamentos.Desserealizar(receiveJson);

            json.resultado = pg.salvarDebito(pagamentos.getData());
            json.sucesso = true;
        } catch (Exception ex) {
            json.sucesso = false;
            json.mensagem = "Não foi possível carregar os pagamentos. " + ex.toString();
        }

        return json.Serializar();
    }

    public String salvarVarios(String receiveJson) {
        JsonResult<Boolean> json = new JsonResult<Boolean>();

        try {
            database.Pagamento pg = new database.Pagamento();

            JsonReceiver<contratos.Pagamento> pagamentos = new JsonReceiver<>(contratos.Pagamento.class);
            pagamentos.Desserealizar(receiveJson);

            json.resultado = pg.salvarVarios(pagamentos.getData());
            json.sucesso = true;
        } catch (Exception ex) {
            json.sucesso = false;
            json.mensagem = "Não foi possível carregar os pagamentos. " + ex.toString();
        }

        return json.Serializar();
    }

    public String lervarios(String receiveJson) {

        JsonReceiver<java.util.Date> r = new JsonReceiver<java.util.Date>(java.util.Date.class);
        r.Desserealizar(receiveJson);

        database.Pagamento pg = new database.Pagamento();

        JsonResult<List<contratos.Semana>> json = new JsonResult<>();

        try {
            json.resultado = pg.GetMonthPayments(r.getData());
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
