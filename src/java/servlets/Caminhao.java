/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import contratos.JsonReceiver;
import contratos.JsonReceiver2;
import contratos.JsonResult;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author felipe
 */
public class Caminhao extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
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
            String resposta = "";

            switch (json.getData().getOperacao()) {
                case INSERIR: {
                    resposta = Inserir(receiveJson);
                    break;
                }
                case REMOVER: {
                    resposta = remover(receiveJson);
                    break;
                }
                case EDITAR: {
                    resposta = Editar(receiveJson);
                    break;
                }
                case LER: {

                    break;
                }
                case LERVARIOS: {
                    resposta = lervarios();
                    break;
                }
            }

            out.print(resposta);

        }
    }

    public String lervarios() {
        JsonResult<List<contratos.Caminhao>> json = new JsonResult<List<contratos.Caminhao>>();

        database.Caminhao data = new database.Caminhao();
        List<contratos.Caminhao> caminhaos = data.listar();

        if (caminhaos == null) {
            json.mensagem = "Houve um erro ao carregar os caminhões.";
            json.sucesso = false;
        } else {
            json.resultado = caminhaos;
            json.sucesso = true;
        }
        return json.Serializar();
    }

    private String Editar(String json) {
        contratos.JsonReceiver<contratos.Caminhao> c = new JsonReceiver<>(contratos.Caminhao.class);
        c.Desserealizar(json);

        contratos.Caminhao caminhao = c.getData();
        Boolean resultado = new database.Caminhao().editar(caminhao);

        JsonResult<Boolean> result = new JsonResult<>();
        if (resultado) {
            result.resultado = true;
            result.sucesso = true;
        } else {
            result.resultado = false;
            result.sucesso = false;
            result.mensagem = "Não foi possível editar o caminhao.";

        }

        return result.Serializar();
    }

    private String Inserir(String json) {
        JsonReceiver<contratos.Caminhao> recebido = new JsonReceiver<>(contratos.Caminhao.class);
        recebido.Desserealizar(json);

        database.Caminhao c = new database.Caminhao();
        boolean resultado = c.Inserir(recebido.getData());

        JsonResult<Boolean> result = new JsonResult<>();
        if (resultado) {
            result.resultado = true;
            result.sucesso = true;
        } else {
            result.resultado = false;
            result.sucesso = false;
            result.mensagem = "Não foi possível salvar o caminhao.";
        }

        return result.Serializar();
    }

    private String remover(String receiveJson) {
        JsonResult<Boolean> json = new JsonResult<>();
        JsonReceiver<Integer> re = new JsonReceiver<Integer>(Integer.class);
        re.Desserealizar(receiveJson);

        database.Caminhao c = new database.Caminhao();
        boolean resultado = c.excluir(re.getData().intValue());
        if (resultado) {
            json.sucesso = true;
            json.resultado = true;
        } else {
            json.sucesso = true;
            json.resultado = false;
            json.mensagem = "Não foi possível excluir o caminhao.";
        }

        return json.Serializar();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
