/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contratos;

import contratos.Dia;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lucas
 */
public class Semana {

    public int numero;
    public List<Dia> dias;

    public Semana() {
        dias = new ArrayList<>();
    }
}
