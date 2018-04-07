/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.lectorconstantes.service;

import java.io.InputStream;
import java.util.List;
import mx.net.alvatroz.sustituidorvariables.bo.ElementoTraductorBo;
import org.springframework.stereotype.Service;

/**
 *
 * @author alvaro
 */
@Service
public class LectorVariablesServices {
   
   /**
    * Lee un archivo en busca de sufijos y prefijos
    * @param is
    * @param prefijo
    * @param sufijo
    * @return 
    */
   public ElementoTraductorBo leeArchivo( InputStream is, List<String> prefijo, List<String> sufijo )
   {
      return null;
   }
}
