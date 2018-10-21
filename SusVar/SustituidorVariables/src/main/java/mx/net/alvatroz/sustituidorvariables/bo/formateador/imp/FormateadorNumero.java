/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.bo.formateador.imp;

import mx.net.alvatroz.sustituidorvariablescomundto.TipoFormateador;
import mx.net.alvatroz.sustituidorvariables.bo.formateador.IFormateador;
import org.springframework.stereotype.Service;

/**
 *
 * @author alvaro
 */
@Service
public class FormateadorNumero implements IFormateador{

    @Override
    public String formatea(String cadena, String formato) {
        return cadena;
    }

    @Override
    public TipoFormateador getElemento() {
        return TipoFormateador.NUMERO;
    }
    
}
