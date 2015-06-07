/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contratos;

import com.google.gson.Gson;

/**
 *
 * @author lucas
 */
public class JsonReceiver2 {
    private String json;
    private Operacao operacao;

    /**
     * @return the json
     */
    public String getJson() {
        return json;
    }

    /**
     * @param json the json to set
     */
    public void setJson(String json) {
        this.json = json;
    }

    /**
     * @return the operacao
     */
    public Operacao getOperacao() {
        return operacao;
    }

    /**
     * @param operacao the operacao to set
     */
    public void setOperacao(Operacao operacao) {
        this.operacao = operacao;
    }


    public enum Operacao {
        INSERIR, REMOVER, EDITAR, LER, LERVARIOS, SALVARVARIOS, SALVARDEBITO, PAGARUNICO, PAGAMENTOMENSALFORNECEDOR, PAGARDEBITO;
    }
}
