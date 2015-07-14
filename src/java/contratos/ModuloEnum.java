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
public enum ModuloEnum {
    PAGAMENTOS(1),
    USUARIOS(2),
    FORNECEDORES(3),
    CLIENTES(4),
    CARGOS(5),
    FUNCIONARIO(6),
    CAMINHOES(7),
    CARGAS(8),
    CONTAS_BANCARIAS(9),
    BANCOS(10),
    VIAGENS(11),
    INTERNO(12),
    LOG(13),
    MODULO(14);

    private int moduloID;

    ModuloEnum(int moduloID) {
        this.moduloID = moduloID;
    }

    public int getModulo() {
        return this.moduloID;
    }
}