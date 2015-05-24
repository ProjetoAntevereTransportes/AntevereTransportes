/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contratos;

/**
 *
 * @author felipe
 */
import com.google.gson.*;



public class JsonResult<T> {
    public boolean sucesso;
    public String mensagem;
    public T resultado;
    
    public String Serializar() {
        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
        return g.toJson(this);
    }
    
}
