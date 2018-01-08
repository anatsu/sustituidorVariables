/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.bo.exception;

/**
 *
 * @author alvaro
 */
public class LectorFinalizacionException extends RuntimeException{

    public LectorFinalizacionException(String mensaje, Exception error) {
        super(mensaje, error);
    }
    
}
