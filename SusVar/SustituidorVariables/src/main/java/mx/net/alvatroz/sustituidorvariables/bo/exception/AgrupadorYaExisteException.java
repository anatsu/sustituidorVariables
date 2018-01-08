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
public class AgrupadorYaExisteException extends RuntimeException{

    public AgrupadorYaExisteException(String nombreAgrupador) 
    {
        super("El agrupador "+nombreAgrupador+" ya existe");
    }
 
    
}
