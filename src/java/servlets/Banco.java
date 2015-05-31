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
public class Banco extends HttpServlet {

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

    private String remover(String receiveJson) {
        JsonResult<Boolean> json = new JsonResult<>();
        JsonReceiver<Integer> re = new JsonReceiver<Integer>(Integer.class);
        re.Desserealizar(receiveJson);

        database.Banco f = new database.Banco();
        boolean resultado = f.Excluir(re.getData().intValue());
        if (resultado) {
            json.sucesso = true;
            json.resultado = true;
        } else {
            json.sucesso = true;
            json.resultado = false;
            json.mensagem = "Não foi possível excluir o banco.";
        }

        return json.Serializar();
    }

    
    private String Editar(String json) {
        contratos.JsonReceiver<contratos.Banco> f = new JsonReceiver<>(contratos.Banco.class);
        f.Desserealizar(json);

        contratos.Banco banco = f.getData();
        Boolean resultado = new database.Banco().editar(banco);

        JsonResult<Boolean> result = new JsonResult<>();
        if (resultado) {
            result.resultado = true;
            result.sucesso = true;
        } else {
            result.resultado = false;
            result.sucesso = false;
            result.mensagem = "Não foi possível editar o banco.";

        }

        return result.Serializar();
    }


    public String lervarios(String receiveJson) {
         JsonResult<List<contratos.Banco>> json = new JsonResult<List<contratos.Banco>>();

        database.Banco data = new database. Banco();
        List<contratos.Banco> bancos = data.listar();

        if (bancos == null) {
            json.mensagem = "Houve um erro ao carregar os bancos.";
            json.sucesso = false;
        } else {
            json.resultado = bancos;
            json.sucesso = true;
        }
        return json.Serializar();
    
    }
    
    private String Inserir(String json) {
        JsonReceiver<contratos.Banco> banco = new JsonReceiver<>(contratos.Banco.class);
        banco.Desserealizar(json);

        database.Banco f = new database.Banco();
        boolean resultado = f.Inserir(banco.getData());

        JsonResult<Boolean> result = new JsonResult<>();
        if (resultado) {
            result.resultado = true;
            result.sucesso = true;
        } else {
            result.resultado = false;
            result.sucesso = false;
            result.mensagem = "Não foi possível salvar o banco.";
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
    
     @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}

