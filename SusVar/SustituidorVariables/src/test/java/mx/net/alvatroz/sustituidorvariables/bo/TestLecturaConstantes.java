/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.bo;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author alvaro
 */
public class TestLecturaConstantes {

   private final static Logger LOG = LoggerFactory.getLogger(TestLecturaConstantes.class);

   @Test
   public void testLeeConstantes() {

      try {
	 final String[] ARREGLO = new String[]{"DECLARE",
	    "",
	    " salary_increase CONSTANT number(3):= 3;",
	    "csgAlgo CONSTANT NUMBER(4):= 5; -- hola",
	    "",
	    "BEGIN",
	    "",
	    " salary_increase := 100;",
	    "",
	    " dbms_output.put_line (salary_increase);",
	    "",
	    "END;"
	 };

	 Assert.assertNull(getConstante(ARREGLO[0]));
	 Assert.assertNull(getConstante(ARREGLO[1]));
	 final ElementoTraductorBo constante = getConstante(ARREGLO[2]);
	 Assert.assertNotNull(constante);
	 Assert.assertEquals("salary_increase", constante.getConstante());
	 Assert.assertEquals("3", constante.getValor());
	 
	 /*Assert.assertNull( getConstante(ARREGLO[3]));
      Assert.assertNull( getConstante(ARREGLO[4]));
      Assert.assertNull( getConstante(ARREGLO[5]));
      Assert.assertNull( getConstante(ARREGLO[6]));
      Assert.assertNull( getConstante(ARREGLO[7]));
      Assert.assertNull( getConstante(ARREGLO[8]));
      Assert.assertNull( getConstante(ARREGLO[9]));
      Assert.assertNull( getConstante(ARREGLO[10]));
      Assert.assertNull( getConstante(ARREGLO[11]));*/
      } catch (Exception e) {
	 Assert.fail(e.getMessage());
      }
   }

   public ElementoTraductorBo getConstante(String linea) {

      if (linea == null || linea.isEmpty()) {
	 // la linea esta vacia
	 return null;
      }

      final String BUSCADO = " constant";
      Integer tamanioMaxBuscado = linea.length() - BUSCADO.length() - 2;
      LOG.debug("El tamaño {}", tamanioMaxBuscado);

      if (tamanioMaxBuscado <= 0) {
	 // no podría tener la palabra constant por que la linea es muy pequeña
	 return null;
      }

      int i = 0;
      Integer indiceAnterior;
      Integer indiceTexto;
      int indiceBase = -1;
      boolean encontrado = false;

      do {
	 indiceAnterior = i;
	 indiceTexto = 0;

	 //LOG.debug(" buscados i: {} indiceTexto: {} letraLinea: {} letraTexto: {}"
	 //   , i, indiceTexto, Character.toLowerCase( linea.charAt(i)), BUSCADO.charAt(indiceTexto));
	 while (i < tamanioMaxBuscado
	    && indiceTexto < BUSCADO.length()
	    && Character.toLowerCase(linea.charAt(i)) == BUSCADO.charAt(indiceTexto)) {
	    if (indiceTexto == 0) {
	       indiceBase = i;
	    }

	    i++;
	    indiceTexto++;

	 }
	 //LOG.debug("indice text {}", indiceTexto);

	 if (indiceTexto == BUSCADO.length()) {
	    encontrado = true;
	 }

	 if (indiceAnterior == i) {
	    i++;
	 }
      } while (!encontrado && i < tamanioMaxBuscado);

      if (encontrado) {
	 LOG.debug("Se encontro la palabra reservada CONSTANT en el indice {}", indiceBase);
	 String constante = buscaConstante( indiceBase, linea);
	 String valor = buscaValor( indiceBase+BUSCADO.length(), linea);
	 
	 ElementoTraductorBo elem = new ElementoTraductorBo(null);
	 elem.setConstante(constante);
	 elem.setValor(valor);
	 return elem;
	 

      }

      return null;
   }

   private String buscaConstante(int indiceBase, String linea) {
      StringBuilder resultado = new StringBuilder();
      
      indiceBase--;
      while( linea.charAt(indiceBase) == ' ')
      {
	 indiceBase--;
      }
      
      while( indiceBase > 0 && linea.charAt(indiceBase) != ' ')
      {
	 resultado.append( linea.charAt(indiceBase));
	 indiceBase--;
      }
      
      return resultado.reverse().toString();      
      
   }

   private String buscaValor(int indice, String linea) {
      
      String resultado = null;
      while( linea.charAt(indice)!= '=')
      {
	 indice++;
      }
      indice++;
      LOG.debug("El indice esta en {}", indice);
      while( linea.charAt(indice)== ' ')
      {
	 indice++;      
      }
      LOG.debug("El indice esta ahora en {}", indice);
      if( linea.charAt(indice) == '\'')
      {
	 LOG.debug("Se encontro una cadena");
	 indice++;
	 resultado = linea.substring(indice, linea.indexOf( indice, '\''));
      }
      if( Character.isDigit(linea.charAt(indice)) || linea.charAt(indice)== '.' )
      {
	 LOG.debug("Se encontro un numero");
	 resultado = "";
	 
	 while( linea.charAt(indice) != ' ' && linea.charAt(indice) != ';')
	 {
	    resultado+= linea.charAt(indice);
	    indice++;
	 }
      }
      return resultado;
   }

}
