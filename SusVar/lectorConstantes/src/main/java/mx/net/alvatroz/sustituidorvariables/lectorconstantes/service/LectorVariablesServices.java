/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.lectorconstantes.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import mx.net.alvatroz.sustituidorvariables.bo.ElementoTraductorBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author alvaro
 */
@Service
public class LectorVariablesServices {

   private final static Logger LOG = LoggerFactory.getLogger(LectorVariablesServices.class);

   /**
    * Lee un archivo en busca de sufijos y prefijos
    *
    * @param is
    *
    * @return
    */
   public List<ElementoTraductorBo> leeArchivo(InputStream is) {

      try (Stream<String> lineas = new BufferedReader(new InputStreamReader(is)).lines()) {

	 Set<String> conjunto = new HashSet<>();

	 return lineas
	    .map(new ProcesadorCadena())
	    .filter((ElementoTraductorBo p)
	       -> {
	       boolean resultado = p != null && !conjunto.contains(p.getConstante());
	       if (p != null) {
		  conjunto.add(p.getConstante());
	       }

	       return resultado;

	    })
	    .collect(Collectors.toList());

      }

   }
}
