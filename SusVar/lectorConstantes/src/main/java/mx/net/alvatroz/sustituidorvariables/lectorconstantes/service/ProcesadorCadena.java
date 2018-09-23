/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.lectorconstantes.service;

import java.util.function.Function;
import mx.net.alvatroz.sustituidorvariables.bo.ElementoTraductorBo;
import mx.net.alvatroz.sustituidorvariables.bo.TipoFormateador;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author alvaro
 */
public class ProcesadorCadena implements Function<String, ElementoTraductorBo> {

   private final static Logger LOG = LoggerFactory.getLogger(ProcesadorCadena.class);
   private static final String CONSTANTE = "CONSTANT";
   
   @Override
   public ElementoTraductorBo apply(String letras) {

      //LOG.debug("La linea es: {}", letras);
      int indice = 0;
      ElementoTraductorBo elementoTraductorBo = null;

      StringBuilder token1 = new StringBuilder();
      StringBuilder token2 = new StringBuilder();
      StringBuilder valorConstante = new StringBuilder();
      
      // se quitan los espacios en blanco o garabatos
      indice = quitaEspacios(indice, letras);

      
      
      // si aun queda texto y no es comentario
      if (indice < letras.length() && letras.charAt(indice) != '-') {

	 //LOG.debug("Procesando lo que quedo de los espacios {}", indice);
	 
	 indice = leeToken(indice, letras, token1);
	 
	 //LOG.debug("El primer token es: {}", token1);

	 indice = quitaEspacios(indice, letras);
	 
	 // se busca el token de la constante
	 indice = leeToken(indice, letras, token2);
	 //LOG.debug(" El segundo token es {}", token2);
	 if( token2.toString().equalsIgnoreCase(CONSTANTE)){
	    
	    //LOG.debug("se encontro constante ");
	    indice = quitaHastaOperadorAsignacion(indice, letras);
	    indice = quitaEspacios(indice, letras);
	    leeToken(indice, letras, valorConstante);
	    
	    //LOG.debug("Valor de la constante {}", valorConstante);
	    if( valorConstante.length() > 0)
	    {
	       elementoTraductorBo = new ElementoTraductorBo();
	       elementoTraductorBo.setConstante( token1.toString());
	       elementoTraductorBo.setValor(valorConstante.toString());
	       elementoTraductorBo.setTipo(TipoFormateador.NUMERO);
	    }
	    
	 }
	 

      }

      return elementoTraductorBo;
   }
   
   
   private int quitaHastaOperadorAsignacion( int indice , String letras)
   {
      while ( indice < letras.length() && letras.charAt(indice) != ':')	 
      {
	 indice++;
      }
      
      // salta el caracter igual
      indice++;
      
      return indice;
      
   }

   /**
    * Lee de la cadena y almacena el token que lee en la variable token
    * @param indice indice donde se inicia la lectura
    * @param letras letras que tiene la cadena
    * @param token token donde se acumula el token de la lectura
    * @return Un entero que indica el punto en el que se quedo la lectura
    */
   private int leeToken(int indice, String letras, StringBuilder token) {
      // se almacena el token inicial
      boolean procesarEspacio = false;
      if( indice < letras.length() && letras.charAt(indice) == '\''){
	//LOG.debug( " Letra inicial {} ", letras.charAt(indice)); 
	procesarEspacio = true;
	indice++;
      }
      
      
      while (indice < letras.length()
	 && ( Character.isLetterOrDigit(letras.charAt(indice))
	 || letras.charAt(indice) == '_'
	 || letras.charAt(indice) == '.'
	 || ( procesarEspacio && letras.charAt(indice)== ' ')
	 )
	 ) {
	 //LOG.debug(" indice {} letra {}", indice, letras.charAt(indice));
	 token.append(letras.charAt(indice));
	 indice++;
      }
      return indice;
   }

   private int quitaEspacios(int indice, String letras) {
      // se eliminan el resto de los espacios
      while (indice < letras.length()
	 && !Character.isLetterOrDigit(letras.charAt(indice))
	 && letras.charAt(indice) != '-'
	 && letras.charAt(indice) != '\'') {
	 
	 indice++;
      }
      return indice;
   }

}
