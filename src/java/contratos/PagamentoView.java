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
public class PagamentoView {
    private int ID;
    private java.util.Date vencimento;
    private String semana;
    private String nome;
    private String descricao;
    private Double valor;
    private boolean vencido;
    private int numero;
    private int total;
    private contratos.Fornecedor fornecedor;

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
     * @return the vencimento
     */
    public java.util.Date getVencimento() {
        return vencimento;
    }

    /**
     * @param vencimento the vencimento to set
     */
    public void setVencimento(java.util.Date vencimento) {
        this.vencimento = vencimento;
    }

    /**
     * @return the semana
     */
    public String getSemana() {
        return semana;
    }

    /**
     * @param semana the semana to set
     */
    public void setSemana(String semana) {
        this.semana = semana;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the valor
     */
    public Double getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(Double valor) {
        this.valor = valor;
    }

    /**
     * @return the vencido
     */
    public boolean isVencido() {
        return vencido;
    }

    /**
     * @param vencido the vencido to set
     */
    public void setVencido(boolean vencido) {
        this.vencido = vencido;
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
     * @return the numero
     */
    public int getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }

    /**
     * @return the total
     */
    public int getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * @return the fornecedor
     */
    public contratos.Fornecedor getFornecedor() {
        return fornecedor;
    }

    /**
     * @param fornecedor the fornecedor to set
     */
    public void setFornecedor(contratos.Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }
}
