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
public class Rota {
    private String partida;
    private String destino;
    private String metros;
    private String segundos;

    /**
     * @return the partida
     */
    public String getPartida() {
        return partida;
    }

    /**
     * @param partida the partida to set
     */
    public void setPartida(String partida) {
        this.partida = partida;
    }

    /**
     * @return the destino
     */
    public String getDestino() {
        return destino;
    }

    /**
     * @param destino the destino to set
     */
    public void setDestino(String destino) {
        this.destino = destino;
    }

    /**
     * @return the metros
     */
    public String getMetros() {
        return metros;
    }

    /**
     * @param metros the metros to set
     */
    public void setMetros(String metros) {
        this.metros = metros;
    }

    /**
     * @return the minutos
     */
    public String getSegundos() {
        return segundos;
    }

    /**
     * @param minutos the minutos to set
     */
    public void setSegundos(String segundos) {
        this.segundos = segundos;
    }
}
