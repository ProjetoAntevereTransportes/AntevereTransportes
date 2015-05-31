/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contratos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author felipe
 */
public class JsonReceiver<T> {

    public JsonReceiver(Class<T> type) {
        this.type = type;
    }
    private T data;
    private Class<T> type;

    public void Desserealizar(String json) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
        data = gson.fromJson(json, type);
    }

    /**
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(T data) {
        this.data = data;
    }
}
