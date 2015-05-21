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
public class Usuario extends HttpServlet {

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
                    resposta = Inserir(receiveJson);
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
                    //resposta = salvarVarios(receiveJson);
                    break;
                }
            }

            out.print(resposta);
        }
    }

//    public String salvarVarios(String receiveJson) {
//        database.Pagamento pg = new database.Pagamento();
//
//        JsonReceiver<contratos.Pagamento> pagamentos = new JsonReceiver<>(contratos.Pagamento.class);
//        pagamentos.Desserealizar(receiveJson);
//        
//        JsonResult<Boolean> json = new JsonResult<Boolean>();
//        
//        try {
//            json.resultado = pg.salvarVarios(pagamentos.getData());
//            json.sucesso = true;
//        } catch (Exception ex) {
//            json.sucesso = false;
//            json.mensagem = "Não foi possível carregar os pagamentos. " + ex.toString();
//        }
//
//        return json.Serializar();
//    }

    public String lervarios(String receiveJson) {
         JsonResult<List<contratos.Usuario>> json = new JsonResult<List<contratos.Usuario>>();

        database.Usuario data = new database.Usuario();
        List<contratos.Usuario> usuarios = data.listar();

        if (usuarios == null) {
            json.mensagem = "Houve um erro ao carregar as perguntas.";
            json.sucesso = false;
        } else {
            json.resultado = usuarios;
            json.sucesso = true;
        }
        return json.Serializar();
    
    }
    
    private String Inserir(String json) {
        JsonReceiver<contratos.Usuario> usu = new JsonReceiver<>(contratos.Usuario.class);
        usu.Desserealizar(json);

        database.Usuario f = new database.Usuario();
        boolean resultado = f.Inserir(usu.getData());

        JsonResult<Boolean> result = new JsonResult<>();
        if (resultado) {
            result.resultado = true;
            result.sucesso = true;
        } else {
            result.resultado = false;
            result.sucesso = false;
            result.mensagem = "Não foi possível salvar o usuario.";
        }

        return result.Serializar();
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
