/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import mx.net.alvatroz.sustituidorvariablescomundto.AgrupadorDto;
import mx.net.alvatroz.sustituidorvariablescomundto.AgrupadorSinElementosDto;

/**
 * Objeto para almacenar el estado del formulario
 * @author alvaro
 */
@Data
public class FormFrameDto implements Serializable{
   /**
    * Lista con el total de agrupadores existentes en la BD
    */
   private List<AgrupadorSinElementosDto> agrupadorSinElementos;
   /**
    * Respresenta el agrupador que se esta viendo al momento en pantalla
    */
   private AgrupadorDto agrupadorActual;
}
