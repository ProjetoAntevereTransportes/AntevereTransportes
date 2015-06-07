/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contratos;

/**
 *
 * @author lucas ID da Viagem - int auto incremento; ID do Caminhão - int; ID do
 * Funcionário - int; ID do Endereço de Carga - int; ID do Endereço de Descarga
 * - int; Valor da Viagem - decimal(10,2); ID do Tipo de Carga - int; ID do
 * Cliente - int; Valor Adiantamento - decimal (10,2); ID Status da Viagem -
 * int; Motivo de Cancelamento - varchar(60).
 */
public class Viagem {

    private int ID;
    private int caminhaoID;
    private int funcionarioID;
    private int enderecoCargaID;
    private int enderecoDescargaID;
    private Double valor;
    private int tipoCargaID;
    private int clienteID;
    private Double adiantamento;
    private int statusViagemID;
    private String motivo;
    private String nomeFunctionario;
    private String nomeCarga;
    private String nomeCliente;
    private String nomeStatusViagem;
    private String nomeCaminhao;
    private String placarCaminhao;
    private contratos.Endereco enderecoCarga;
    private contratos.Endereco enderecoDescarga;

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
     * @return the caminhaoID
     */
    public int getCaminhaoID() {
        return caminhaoID;
    }

    /**
     * @param caminhaoID the caminhaoID to set
     */
    public void setCaminhaoID(int caminhaoID) {
        this.caminhaoID = caminhaoID;
    }

    /**
     * @return the funcionarioID
     */
    public int getFuncionarioID() {
        return funcionarioID;
    }

    /**
     * @param funcionarioID the funcionarioID to set
     */
    public void setFuncionarioID(int funcionarioID) {
        this.funcionarioID = funcionarioID;
    }

    /**
     * @return the enderecoCargaID
     */
    public int getEnderecoCargaID() {
        return enderecoCargaID;
    }

    /**
     * @param enderecoCargaID the enderecoCargaID to set
     */
    public void setEnderecoCargaID(int enderecoCargaID) {
        this.enderecoCargaID = enderecoCargaID;
    }

    /**
     * @return the enderecoDescargaID
     */
    public int getEnderecoDescargaID() {
        return enderecoDescargaID;
    }

    /**
     * @param enderecoDescargaID the enderecoDescargaID to set
     */
    public void setEnderecoDescargaID(int enderecoDescargaID) {
        this.enderecoDescargaID = enderecoDescargaID;
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
     * @return the tipoCargaID
     */
    public int getTipoCargaID() {
        return tipoCargaID;
    }

    /**
     * @param tipoCargaID the tipoCargaID to set
     */
    public void setTipoCargaID(int tipoCargaID) {
        this.tipoCargaID = tipoCargaID;
    }

    /**
     * @return the clienteID
     */
    public int getClienteID() {
        return clienteID;
    }

    /**
     * @param clienteID the clienteID to set
     */
    public void setClienteID(int clienteID) {
        this.clienteID = clienteID;
    }

    /**
     * @return the adiantamento
     */
    public Double getAdiantamento() {
        return adiantamento;
    }

    /**
     * @param adiantamento the adiantamento to set
     */
    public void setAdiantamento(Double adiantamento) {
        this.adiantamento = adiantamento;
    }

    /**
     * @return the statusViagemID
     */
    public int getStatusViagemID() {
        return statusViagemID;
    }

    /**
     * @param statusViagemID the statusViagemID to set
     */
    public void setStatusViagemID(int statusViagemID) {
        this.statusViagemID = statusViagemID;
    }

    /**
     * @return the motivo
     */
    public String getMotivo() {
        return motivo;
    }

    /**
     * @param motivo the motivo to set
     */
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    /**
     * @return the nomeFunctionario
     */
    public String getNomeFunctionario() {
        return nomeFunctionario;
    }

    /**
     * @param nomeFunctionario the nomeFunctionario to set
     */
    public void setNomeFunctionario(String nomeFunctionario) {
        this.nomeFunctionario = nomeFunctionario;
    }

    /**
     * @return the nomeCarga
     */
    public String getNomeCarga() {
        return nomeCarga;
    }

    /**
     * @param nomeCarga the nomeCarga to set
     */
    public void setNomeCarga(String nomeCarga) {
        this.nomeCarga = nomeCarga;
    }

    /**
     * @return the nomeCliente
     */
    public String getNomeCliente() {
        return nomeCliente;
    }

    /**
     * @param nomeCliente the nomeCliente to set
     */
    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    /**
     * @return the nomeStatusViagem
     */
    public String getNomeStatusViagem() {
        return nomeStatusViagem;
    }

    /**
     * @param nomeStatusViagem the nomeStatusViagem to set
     */
    public void setNomeStatusViagem(String nomeStatusViagem) {
        this.nomeStatusViagem = nomeStatusViagem;
    }

    /**
     * @return the nomeCaminhao
     */
    public String getNomeCaminhao() {
        return nomeCaminhao;
    }

    /**
     * @param nomeCaminhao the nomeCaminhao to set
     */
    public void setNomeCaminhao(String nomeCaminhao) {
        this.nomeCaminhao = nomeCaminhao;
    }

    /**
     * @return the placarCaminhao
     */
    public String getPlacarCaminhao() {
        return placarCaminhao;
    }

    /**
     * @param placarCaminhao the placarCaminhao to set
     */
    public void setPlacarCaminhao(String placarCaminhao) {
        this.placarCaminhao = placarCaminhao;
    }

    /**
     * @return the enderecoCarga
     */
    public contratos.Endereco getEnderecoCarga() {
        return enderecoCarga;
    }

    /**
     * @param enderecoCarga the enderecoCarga to set
     */
    public void setEnderecoCarga(contratos.Endereco enderecoCarga) {
        this.enderecoCarga = enderecoCarga;
    }

    /**
     * @return the enderecoDescarga
     */
    public contratos.Endereco getEnderecoDescarga() {
        return enderecoDescarga;
    }

    /**
     * @param enderecoDescarga the enderecoDescarga to set
     */
    public void setEnderecoDescarga(contratos.Endereco enderecoDescarga) {
        this.enderecoDescarga = enderecoDescarga;
    }

}
