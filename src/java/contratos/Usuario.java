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
public class Usuario {
    private int id;
    private String nome;
    private String senha;
    private String email;
    private int perguntaID;
    private String resposta;
    private int tipoUsuarioID;
    private String tipoUsuarioNome;
    private contratos.TipoUsuario tipoUsuario;
    private int statusID;
    private String statusNome;

    private contratos.StatusUsuario status;
    private String chave;

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
     * @return the senha
     */
    public String getSenha() {
        return senha;
    }

    /**
     * @param senha the senha to set
     */
    public void setSenha(String senha) {
        this.senha = senha;
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
     * @return the perguntaID
     */
    public int getPerguntaID() {
        return perguntaID;
    }

    /**
     * @param perguntaID the perguntaID to set
     */
    public void setPerguntaID(int perguntaID) {
        this.perguntaID = perguntaID;
    }

    /**
     * @return the resposta
     */
    public String getResposta() {
        return resposta;
    }

    /**
     * @param resposta the resposta to set
     */
    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    /**
     * @return the tipoUsuarioID
     */
    public int getTipoUsuarioID() {
        return tipoUsuarioID;
    }

    /**
     * @param tipoUsuarioID the tipoUsuarioID to set
     */
    public void setTipoUsuarioID(int tipoUsuarioID) {
        this.tipoUsuarioID = tipoUsuarioID;
    }

    /**
     * @return the tipoUsuarioNome
     */
    public String getTipoUsuarioNome() {
        return tipoUsuarioNome;
    }

    /**
     * @param tipoUsuarioNome the tipoUsuarioNome to set
     */
    public void setTipoUsuarioNome(String tipoUsuarioNome) {
        this.tipoUsuarioNome = tipoUsuarioNome;
    }

    /**
     * @return the tipoUsuario
     */
    public contratos.TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    /**
     * @param tipoUsuario the tipoUsuario to set
     */
    public void setTipoUsuario(contratos.TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
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
     * @return the status
     */
    public contratos.StatusUsuario getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(contratos.StatusUsuario status) {
        this.status = status;
    }

    /**
     * @return the chave
     */
    public String getChave() {
        return chave;
    }

    /**
     * @param chave the chave to set
     */
    public void setChave(String chave) {
        this.chave = chave;
    }
}
