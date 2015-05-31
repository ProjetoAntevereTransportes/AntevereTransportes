/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contratos;

/**
 *
 * @author Bruno
 */
public class ContaBancaria {
    
    private int id;
    private String nome;
    private String numero;
    private String agencia;
    private int pessoa_id;
    private String pessoa_nome;
    private int banco_id;
    private String banco_nome;
    //

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getPessoa_id() {
        return pessoa_id;
    }

    public void setPessoa_id(int pessoa_id) {
        this.pessoa_id = pessoa_id;
    }

    public String getPessoa_nome() {
        return pessoa_nome;
    }

    public void setPessoa_nome(String pessoa_nome) {
        this.pessoa_nome = pessoa_nome;
    }
    
    public int getBanco_id() {
        return banco_id;
    }

    public void setBanco_id(int banco_id) {
        this.banco_id = banco_id;
    }

    /**
     * @return the agencia
     */
    public String getAgencia() {
        return agencia;
    }

    /**
     * @param agencia the agencia to set
     */
    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    /**
     * @return the banco_nome
     */
    public String getBanco_nome() {
        return banco_nome;
    }

    /**
     * @param banco_nome the banco_nome to set
     */
    public void setBanco_nome(String banco_nome) {
        this.banco_nome = banco_nome;
    }
        
}
