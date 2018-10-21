/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariablescomundto;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * Elemento que se ha de traducir
 * @author alvaro
 */
@Setter
@Getter
public class ElementoTraductorDto {
   /**
     * Identificador del elemento traductor
     */
    private Long id;
    /**
     * Cadena que representa el valor constante: csgAlgo
     */
    private String constante;
    /**
     * Tipo del traductor
     */
    private TipoFormateador tipo;
    /**
     * Valor por el que se sustituira la constante
     */
    private String valor;

   
    
    
    
}
