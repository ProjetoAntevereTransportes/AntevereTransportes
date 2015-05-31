/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contratos;

import java.util.Date;

/**
 *
 * @author lucas
 */
public class ParcelaPagamento {
    private int ID;
    private Date vencimento;
    private Double valor;
    private Double descontos;
    private Double juros;
    private int statusPagamento;
    private String comprovante;
    private int comprovanteID;
    private int boletoID;
    private int contaBancariaID;
    private String descricao;
    private int debitoAutomaticoID;
    private String statusNome;
    private String contaBancariaNome;
    private String boleto;

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
    public Date getVencimento() {
        return vencimento;
    }

    /**
     * @param vencimento the vencimento to set
     */
    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
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
     * @return the descontos
     */
    public Double getDescontos() {
        return descontos;
    }

    /**
     * @param descontos the descontos to set
     */
    public void setDescontos(Double descontos) {
        this.descontos = descontos;
    }

    /**
     * @return the juros
     */
    public Double getJuros() {
        return juros;
    }

    /**
     * @param juros the juros to set
     */
    public void setJuros(Double juros) {
        this.juros = juros;
    }

    /**
     * @return the statusPagamento
     */
    public int getStatusPagamento() {
        return statusPagamento;
    }

    /**
     * @param statusPagamento the statusPagamento to set
     */
    public void setStatusPagamento(int statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

    /**
     * @return the comprovante
     */
    public String getComprovante() {
        return comprovante;
    }

    /**
     * @param comprovante the comprovante to set
     */
    public void setComprovante(String comprovante) {
        this.comprovante = comprovante;
    }

    /**
     * @return the contaBancariaID
     */
    public int getContaBancariaID() {
        return contaBancariaID;
    }

    /**
     * @param contaBancariaID the contaBancariaID to set
     */
    public void setContaBancariaID(int contaBancariaID) {
        this.contaBancariaID = contaBancariaID;
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
     * @return the debitoAutomaticoID
     */
    public int getDebitoAutomaticoID() {
        return debitoAutomaticoID;
    }

    /**
     * @param debitoAutomaticoID the debitoAutomaticoID to set
     */
    public void setDebitoAutomaticoID(int debitoAutomaticoID) {
        this.debitoAutomaticoID = debitoAutomaticoID;
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
     * @return the contaBancariaNome
     */
    public String getContaBancariaNome() {
        return contaBancariaNome;
    }

    /**
     * @param contaBancariaNome the contaBancariaNome to set
     */
    public void setContaBancariaNome(String contaBancariaNome) {
        this.contaBancariaNome = contaBancariaNome;
    }

    /**
     * @return the comprovanteID
     */
    public int getComprovanteID() {
        return comprovanteID;
    }

    /**
     * @param comprovanteID the comprovanteID to set
     */
    public void setComprovanteID(int comprovanteID) {
        this.comprovanteID = comprovanteID;
    }

    /**
     * @return the boletoID
     */
    public int getBoletoID() {
        return boletoID;
    }

    /**
     * @param boletoID the boletoID to set
     */
    public void setBoletoID(int boletoID) {
        this.boletoID = boletoID;
    }
}
