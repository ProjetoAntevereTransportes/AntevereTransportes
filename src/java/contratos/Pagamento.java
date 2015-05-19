/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contratos;

import java.util.List;

/**
 *
 * @author lucas
 */
public class Pagamento {

    private int ID;
    private contratos.Fornecedor fornecedor;
    private int fornecedorID;
    private String nome;
    private List<contratos.ParcelaPagamento> parcelas;

    public int getNumero(int parcelaID) {        
        int i = 1;        
        for (contratos.ParcelaPagamento p : this.parcelas) {
            if (p.getID() == parcelaID) {
                return i;
            }else{
                i++;
            }
        }
        return i;
    }
    
    public int getTotalParcelas(){
        return this.parcelas.size();
    }
    
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
     * @return the parcelas
     */
    public List<contratos.ParcelaPagamento> getParcelas() {
        return parcelas;
    }

    /**
     * @param parcelas the parcelas to set
     */
    public void setParcelas(List<contratos.ParcelaPagamento> parcelas) {
        this.parcelas = parcelas;
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
}
