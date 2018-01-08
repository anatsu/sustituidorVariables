/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.bo.exception;

import mx.net.alvatroz.sustituidorvariables.bo.TipoFormateador;

/**
 *
 * @author alvaro
 */
public class FormateadorInexistenteException extends RuntimeException{
    
    public FormateadorInexistenteException( TipoFormateador tipo)
    {
        super( "El tipo "+tipo +" no esta soportado");
    }
    
}
