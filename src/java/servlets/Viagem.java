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
 * @author lucas
 */
public class Viagem extends HttpServlet {

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
                    resposta = lerVarios(receiveJson);
                    break;
                }
                case SALVARVARIOS: {
                    break;
                }
                case SALVARDEBITO: {
                    break;
                }
                case PAGARUNICO: {
                    break;
                }
                case PAGAMENTOMENSALFORNECEDOR: {
                    break;
                }
                case PAGARDEBITO: {
                    break;
                }
            }

            out.print(resposta);

        }
    }

    public String lerVarios(String receiveJson) {
        contratos.JsonResult<List<contratos.Viagem>> json = new JsonResult<List<contratos.Viagem>>();

        try {
            database.Viagem v = new database.Viagem();
            json.resultado = v.listar();
            json.sucesso = true;
        } catch (Exception ex) {
            json.sucesso = false;
            json.mensagem = "Não foi possível carregar as viagens. Erro: " + ex.toString();
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
