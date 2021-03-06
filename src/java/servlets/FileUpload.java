/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import contratos.JsonResult;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author lucas
 */
public class FileUpload extends HttpServlet {

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
        JsonResult<String> result = new JsonResult<>();
        try (PrintWriter out = response.getWriter()) {

            if (ServletFileUpload.isMultipartContent(request)) {
                try {
                    Map<String, List<FileItem>> multiparts = new ServletFileUpload(
                            new DiskFileItemFactory()).parseParameterMap(request);

                    FileItem item = multiparts.get("file").get(0);

                    if (!item.isFormField()) {
                        database.FileUpload f = new database.FileUpload();
                        int id = f.uploadFile(item);
                        
                        if(id == 0){
                            throw new Exception();
                        }
                        result.sucesso = true;
                        result.resultado = String.valueOf(id);
                    } else {
                        result.sucesso = false;
                        result.mensagem = "Resquest não é válido.";
                    }

                } catch (Exception ex) {
                    result.sucesso = false;
                    result.mensagem = "Não foi possível salvar o arquivo. Erro: " + ex;
                }
            } else {
                result.sucesso = false;
                result.mensagem = "O método não é MultipartContent.";
            }

            out.println(result.Serializar());
        }
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
