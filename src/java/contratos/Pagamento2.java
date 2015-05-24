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
public class Pagamento2 {
    private int ID;
    private java.util.Date vencimento;
    private int contaBancariaID;
    private int statusPagamentoID;
    private Double valor;
    private Double descontos;
    private Double juros;
    private java.util.Date dataPagamento;
    private String comprovante;
    private String descricao;
    private String nome;
    private String contaNome;
    private int fornecedorID;
    private String statusNome;
    private String fornecedorNome;
    private Boolean debitoLimitado;
    private Boolean debitoIlimitado;
    private Boolean unico;
    private Boolean carne;
    private int quantidade;
    private int numero;

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
     * @return the statusPagamentoID
     */
    public int getStatusPagamentoID() {
        return statusPagamentoID;
    }

    /**
     * @param statusPagamentoID the statusPagamentoID to set
     */
    public void setStatusPagamentoID(int statusPagamentoID) {
        this.statusPagamentoID = statusPagamentoID;
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
     * @return the dataPagamento
     */
    public java.util.Date getDataPagamento() {
        return dataPagamento;
    }

    /**
     * @param dataPagamento the dataPagamento to set
     */
    public void setDataPagamento(java.util.Date dataPagamento) {
        this.dataPagamento = dataPagamento;
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
     * @return the contaNome
     */
    public String getContaNome() {
        return contaNome;
    }

    /**
     * @param contaNome the contaNome to set
     */
    public void setContaNome(String contaNome) {
        this.contaNome = contaNome;
    }

    /**
     * @return the fornecedorID
     */
    public int getFornecedorID() {
        return fornecedorID;
    }

    /**
     * @param fornecedorID the fornecedorID to set
     */
    public void setFornecedorID(int fornecedorID) {
        this.fornecedorID = fornecedorID;
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
     * @return the fornecedorNome
     */
    public String getFornecedorNome() {
        return fornecedorNome;
    }

    /**
     * @param fornecedorNome the fornecedorNome to set
     */
    public void setFornecedorNome(String fornecedorNome) {
        this.fornecedorNome = fornecedorNome;
    }

    /**
     * @return the debito
     */
    public Boolean getDebitoLimitado() {
        return debitoLimitado;
    }

    /**
     * @param debito the debito to set
     */
    public void setDebitoLimitado(Boolean debito) {
        this.debitoLimitado = debito;
    }

    /**
     * @return the quantidade
     */
    public int getQuantidade() {
        return quantidade;
    }

    /**
     * @param quantidade the quantidade to set
     */
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
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
     * @return the debitoIlimitado
     */
    public Boolean getDebitoIlimitado() {
        return debitoIlimitado;
    }

    /**
     * @param debitoIlimitado the debitoIlimitado to set
     */
    public void setDebitoIlimitado(Boolean debitoIlimitado) {
        this.debitoIlimitado = debitoIlimitado;
    }

    /**
     * @return the unico
     */
    public Boolean getUnico() {
        return unico;
    }

    /**
     * @param unico the unico to set
     */
    public void setUnico(Boolean unico) {
        this.unico = unico;
    }

    /**
     * @return the carne
     */
    public Boolean getCarne() {
        return carne;
    }

    /**
     * @param carne the carne to set
     */
    public void setCarne(Boolean carne) {
        this.carne = carne;
    }
    
    
    
}
