/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lucas
 */
public class Modulo {
    Connection con;
    
    public List<contratos.Modulo> GetList(){
        try{            
            contratos.ModuloEnum[] values = contratos.ModuloEnum.values();
            
            List<contratos.Modulo> modulos = new ArrayList<>();
            for(contratos.ModuloEnum m : values){
                contratos.Modulo d = new contratos.Modulo();
                d.setId(m.getModulo());
                d.setNome(m.toString());
                modulos.add(d);
            }
             return modulos;            
        }catch(Exception e){
            library.Log.writeError("Não foi possível carregar os módulos Modulo.GetList()", e.toString(),
                    contratos.ModuloEnum.MODULO);
            return null;
        }
    }
}
