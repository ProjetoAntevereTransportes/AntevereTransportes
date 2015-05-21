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
 * @author Felipe_Botelho
 */
public class TipoUsuario extends HttpServlet {

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
                    //resposta = Inserir(receiveJson);
                    break;
                }
                case REMOVER: {
                    // resposta = remover(receiveJson);
                    break;
                }
                case EDITAR: {
                    // resposta = Editar(receiveJson);
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
        JsonResult<List<contratos.TipoUsuario>> json = new JsonResult<List<contratos.TipoUsuario>>();

        database.TipoUsuario data = new database.TipoUsuario();
        List<contratos.TipoUsuario> tipos = data.listar();

        if (tipos == null) {
            json.mensagem = "Houve um erro ao carregar as perguntas.";
            json.sucesso = false;
        } else {
            json.resultado = tipos;
            json.sucesso = true;
        }
        return json.Serializar();
    }

//    private String Editar(String json) {
//        contratos.JsonReceiver<contratos.Pergunta> f = new JsonReceiver<>(contratos.Pergunta.class);
//        f.Desserealizar(json);
//
//        contratos.Pergunta pergunta = f.getData();
//        Boolean resultado = new database.Pergunta().editar(pergunta);
//
//        JsonResult<Boolean> result = new JsonResult<>();
//        if (resultado) {
//            result.resultado = true;
//            result.sucesso = true;
//        } else {
//            result.resultado = false;
//            result.sucesso = false;
//            result.mensagem = "Não foi possível editar o fornecedor.";
//
//        }
//
//        return result.Serializar();
////    }
//
//    private String Inserir(String json) {
//        JsonReceiver<contratos.Fornecedor> recebido = new JsonReceiver<>(contratos.Fornecedor.class);
//        recebido.Desserealizar(json);
//
//        database.Fornecedor f = new database.Fornecedor();
//        boolean resultado = f.Inserir(recebido.getData());
//
//        JsonResult<Boolean> result = new JsonResult<>();
//        if (resultado) {
//            result.resultado = true;
//            result.sucesso = true;
//        } else {
//            result.resultado = false;
//            result.sucesso = false;
//            result.mensagem = "Não foi possível salvar o fornecedor.";
//        }
//
//        return result.Serializar();
//    }
//
//    private String remover(String receiveJson) {
//        JsonResult<Boolean> json = new JsonResult<>();
//        JsonReceiver<Integer> re = new JsonReceiver<Integer>(Integer.class);
//        re.Desserealizar(receiveJson);
//
//        database.Fornecedor f = new database.Fornecedor();
//        boolean resultado = f.excluir(re.getData().intValue());
//        if (resultado) {
//            json.sucesso = true;
//            json.resultado = true;
//        } else {
//            json.sucesso = true;
//            json.resultado = false;
//            json.mensagem = "Não foi possível excluir o fornecedor.";
//        }
//
//        return json.Serializar();
//    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
