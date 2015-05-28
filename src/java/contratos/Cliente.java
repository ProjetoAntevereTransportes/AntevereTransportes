/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contratos;

/**
 *
 * @author Felipe_Botelho
 */
public class Cliente {

    private int id;
    private String nome;
    private String email;
    private String telefone;
    private String cnpj;
    private String observacao;
    private int statusID;
    private String statusNome;
    private contratos.StatusCliente status;
    private int enderecoID;
    private contratos.Endereco endereco;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the telefone
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     * @param telefone the telefone to set
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * @return the cnpj
     */
    public String getCnpj() {
        return cnpj;
    }

    /**
     * @param cnpj the cnpj to set
     */
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    /**
     * @return the observacao
     */
    public String getObservacao() {
        return observacao;
    }

    /**
     * @param observacao the observacao to set
     */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    /**
     * @return the status
     */
    public contratos.StatusCliente getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(contratos.StatusCliente status) {
        this.status = status;
    }

    /**
     * @return the statusID
     */
    public int getStatusID() {
        return statusID;
    }

    /**
     * @param statusID the statusID to set
     */
    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    /**
     * @return the statusNome
     */
    public String getStatusNome() {
        return statusNome;
    }

    /**
     * @param statusNome the statusNome to set
     */
    public void setStatusNome(String statusNome) {
        this.statusNome = statusNome;
    }

    /**
     * @return the endereco
     */
    public contratos.Endereco getEndereco() {
        return endereco;
    }

    /**
     * @param endereco the endereco to set
     */
    public void setEndereco(contratos.Endereco endereco) {
        this.endereco = endereco;
    }

    /**
     * @return the enderecoID
     */
    public int getEnderecoID() {
        return enderecoID;
    }

    /**
     * @param enderecoID the enderecoID to set
     */
    public void setEnderecoID(int enderecoID) {
        this.enderecoID = enderecoID;
    }

}
