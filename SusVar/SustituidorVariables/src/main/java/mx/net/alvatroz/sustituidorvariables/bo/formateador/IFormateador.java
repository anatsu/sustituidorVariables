/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.bo.formateador;

import mx.net.alvatroz.sustituidorvariables.bo.TipoFormateador;

/**
 *
 * @author alvaro
 */
public interface IFormateador {
    
    String formatea( String cadena, String formato);
        
    TipoFormateador getElemento();
    
}
