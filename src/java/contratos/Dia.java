/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contratos;

import contratos.Pagamento;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lucas
 */
public class Dia {
    
    public List<contratos.Pagamento2> pagamentos;
    public java.util.Date data;

    public Dia() {
        pagamentos = new ArrayList<>();
    }
}
