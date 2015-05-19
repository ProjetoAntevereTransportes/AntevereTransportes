/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contratos;

/**
 *
 * @author lucas
 */
public class Fornecedor {
    private int ID;
    private int enderecoID;
    private contratos.Endereco endereco;
    private String email;
    private String telefone;
    private String nome;

    /**
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(int ID) {
        this.ID = ID;
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

}
