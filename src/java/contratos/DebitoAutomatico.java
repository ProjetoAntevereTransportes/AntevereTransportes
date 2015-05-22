/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contratos;
import java.util.*;

/**
 *
 * @author lucas
 */
public class DebitoAutomatico {
    private int ID;
    private int contaBancariaID;
    private Date dataInicio;
    private Date dataFim;
    private int dia;
    private String nome;
    private String descricao;
    private int fornecedorID;
    private Double valor;
    private Boolean eValido;

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
     * @return the dataInicio
     */
    public Date getDataInicio() {
        return dataInicio;
    }

    /**
     * @param dataInicio the dataInicio to set
     */
    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    /**
     * @return the dataFim
     */
    public Date getDataFim() {
        return dataFim;
    }

    /**
     * @param dataFim the dataFim to set
     */
    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    /**
     * @return the dia
     */
    public int getDia() {
        return dia;
    }

    /**
     * @param dia the dia to set
     */
    public void setDia(int dia) {
        this.dia = dia;
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
     * @return the eValido
     */
    public Boolean geteValido() {
        return eValido;
    }

    /**
     * @param eValido the eValido to set
     */
    public void seteValido(Boolean eValido) {
        this.eValido = eValido;
    }
}
