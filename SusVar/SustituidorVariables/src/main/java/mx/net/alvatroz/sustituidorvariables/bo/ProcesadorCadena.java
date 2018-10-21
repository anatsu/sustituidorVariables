/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.bo;

import java.util.function.Function;
import mx.net.alvatroz.sustituidorvariablescomundto.ElementoTraductorDto;
import mx.net.alvatroz.sustituidorvariablescomundto.TipoFormateador;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *Convierte una cadena de texto en un ElementoTraductorBo.
 * Si la cadena no tiene un elemento traductor retorna nulo
 * @author alvaro
 */
public class ProcesadorCadena implements Function<String, ElementoTraductorDto> {

   private final static Logger LOG = LoggerFactory.getLogger(ProcesadorCadena.class);
   private static final String CONSTANTE = "CONSTANT";
   
   @Override
   public ElementoTraductorDto apply(String letras) {

      //LOG.debug("La linea es: {}", letras);
      int indice = 0;
      ElementoTraductorDto elementoTraductorBo = null;

      StringBuilder token1 = new StringBuilder();
      StringBuilder token2 = new StringBuilder();
      StringBuilder valorConstante = new StringBuilder();
      
      // se quitan los espacios en blanco o garabatos hasta encontrar otro tipo de caracter
      indice = quitaEspacios(indice, letras);

      LOG.debug("Procesando letras {} quitando espacios hasta el indice {}", letras, indice);
      
      
      // si aun queda texto y no es comentario
      if (indice < letras.length() && letras.charAt(indice) != '-') {

	 LOG.debug("Procesando lo que quedo de los espacios {}", indice);
	 
	 indice = leeToken(indice, letras, token1);
	 
	 LOG.debug("El primer token es: {}", token1);

	 indice = quitaEspacios(indice, letras);
	 
	 LOG.debug("Luego de quitar espacios se llego al indice {} quedando {}", indice, letras.substring(indice));
	 // se busca el token de la constante
	 indice = leeToken(indice, letras, token2);
	 LOG.debug(" El segundo token es {}", token2);
	 if( token2.toString().equalsIgnoreCase(CONSTANTE)){
	    
	    LOG.debug("se encontro constante ");
	    indice = quitaHastaOperadorAsignacion(indice, letras);
	    indice = quitaEspaciosRespetaNegYApostrofes(indice, letras);
	    
	    TipoFormateador tipoEsperado = null;
	    
	    
	    // si aun hay letras
	    if( indice < letras.length() )
	    {
	       tipoEsperado = recuperaValorConstante(letras, indice, valorConstante);
	    }
	    // si es negativo
	    
	    
	    LOG.debug("Valor de la constante {}", valorConstante);
	    if( valorConstante.length() > 0)
	    {
	       String valCadena = valorConstante.toString();
	       elementoTraductorBo = new ElementoTraductorDto();
	       elementoTraductorBo.setConstante( token1.toString());
	       elementoTraductorBo.setValor( valCadena);
	       elementoTraductorBo.setTipo( tipoEsperado );
	    }
	    
	 }
	 

      }

      return elementoTraductorBo;
   }

   private TipoFormateador recuperaValorConstante(String letras, int indice, StringBuilder valorConstante) {
   
      LOG.debug("El caracter en el apuntador es: {}", letras.charAt(indice));
      TipoFormateador tipoEsperado = TipoFormateador.NUMERO;
      if ( letras.charAt(indice) == '-') {
	 LOG.debug("Se encontro un negativo");
	 valorConstante.append("-");
	 leeToken(indice + 1, letras, valorConstante);
      } else {
	 if ( letras.charAt(indice) == '\'') {
	    LOG.debug("Se encontro una cadena");
	    // lee una cadena el true es para indicar que considere los espacios
	    leeCadena(indice + 1, letras, valorConstante);
	    tipoEsperado = TipoFormateador.CADENA;
	 } else {
	    // si trae un TO_DATE o to_date para identificar si el formato es completo hay que realizar otra cosa
	    if( letras.charAt(indice) == 't' || letras.charAt(indice) == 'T')
	    {
	       LOG.debug("Se infiere un TO_DATE");
	       // lee una cadena el true es para indicar que considere los espacios
	       leeToken(indice, letras, valorConstante, true);
	       tipoEsperado = TipoFormateador.FECHA;
	    }else{
	       LOG.debug("Se encontro un número");
	       // se da por hecho que es número
	       leeToken(indice, letras, valorConstante);
	    }
	    
	 }
      }
      return tipoEsperado;
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
    * Lee tokens indicando que si se encuentra un espacio pare
    * @param indice  indice desde cual se lee
    * @param letras  cadena donde se hacen las lecturas
    * @param token  token donde se realizará el resultado de la lectura
    * @return el indice donde se ha parado la lectura
    */
   private int leeToken( int indice, String letras, StringBuilder token)
   {
      return leeToken(indice, letras, token, false);
   }
   
   /**
    * Lee una cadena
    * @param indice
    * @param letras
    * @param token
    * @return 
    */
   private int leeCadena( int indice, String letras, StringBuilder token)
   {
      while (indice < letras.length()
	 &&  letras.charAt(indice) != '\'')	 
      {
	 //LOG.debug(" indice {} letra {}", indice, letras.charAt(indice));
	 token.append(letras.charAt(indice));
	 indice++;
      }
      return indice;
   }
   
   /**
    * Lee de la cadena y almacena el token que lee en la variable token
    * @param indice indice donde se inicia la lectura
    * @param letras letras que tiene la cadena
    * @param token token donde se acumula el token de la lectura
    * @return Un entero que indica el punto en el que se quedo la lectura
    */
   private int leeToken(int indice, String letras, StringBuilder token, boolean procesarEspacio) {
      
      
      
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

   private int quitaEspaciosRespetaNegYApostrofes( int indice, String letras){
      indice++;
      // se eliminan el resto de los espacios
      while (indice < letras.length()
	 &&  ' ' == letras.charAt(indice)
	 ) {
	 
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
