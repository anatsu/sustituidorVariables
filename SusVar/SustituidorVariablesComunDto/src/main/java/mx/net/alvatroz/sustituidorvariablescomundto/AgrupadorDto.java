/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariablescomundto;

import java.util.List;
import lombok.Data;

/**
 *
 * @author alvaro
 */
@Data
public class AgrupadorDto 
{
   private Long fiid;
   private String nombreAgrupador;
   private List<ElementoTraductorDto> elementosTraductor;
   
}
