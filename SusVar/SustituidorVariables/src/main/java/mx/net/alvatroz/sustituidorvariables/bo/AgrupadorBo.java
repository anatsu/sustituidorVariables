/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.bo;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import mx.net.alvatroz.sustituidorvariables.bo.exception.AgrupadorSinNombreException;
import mx.net.alvatroz.sustituidorvariables.bo.formateador.FormateadorFacade;
import org.h2.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Un agrupador solo almacena los elementos que se usarán para traducir
 *
 * @author alvaro
 */
public class AgrupadorBo implements Serializable {

   private final static Logger LOG = LoggerFactory.getLogger(AgrupadorBo.class);
   private Integer fiid;
   private String nombreAgrupador;
   private Set<ElementoTraductorBo> elementosTraductor;
   

   public AgrupadorBo(String nombre, Integer fiid) {
      if (nombre == null || nombre.trim().isEmpty()) {
	 throw new AgrupadorSinNombreException();
      }
      this.nombreAgrupador = nombre.trim();
      elementosTraductor = new LinkedHashSet<>();
      this.fiid = fiid;
   }

   public void setFiid(Integer fiid) {
      this.fiid = fiid;
   }

   
   

   public Integer getFiid() {
      return fiid;
   }

   /**
    * Aplica traduccion a un texto
    *
    * @param texto texto a traducir
    * @param formateador formateador a usar para traducir
    * @return Texto formateado
    */
   protected String traduce(String texto, FormateadorFacade formateador) {
      if (texto == null) {
	 return null;
      }

      StringBuilder sb = new StringBuilder(texto);
      Traductor traductor = new Traductor();

      LOG.debug("Elementos de traductor {}", elementosTraductor);
      for (ElementoTraductorBo elemento : elementosTraductor) {
	 if (elemento.getConstante() != null && elemento.getTipo() != null) {
	    LOG.debug("Traduciendo {}", elemento.getConstante());

	    sb = traductor.getTraduccion(sb, elemento.getConstante(), formateador.formatea(elemento.getValor(), elemento.getTipo()));

	 }
      }
      return sb.toString();
   }

   public ElementoTraductorBo agregaElemento() {
      ElementoTraductorBo dto = new ElementoTraductorBo();
      elementosTraductor.add(dto);
      return dto;
   }
   public ElementoTraductorBo agregaElemento( Long id)
   {
      ElementoTraductorBo dto = new ElementoTraductorBo(id);
      elementosTraductor.add(dto);
      return dto;
   }

   /**
    * Elimina un elemento traductor (elimina en base al nombre de su constante
    *
    * @param elemento elemento a eliminar
    * @return
    */
   public boolean eliminaElemento(ElementoTraductorBo elemento) {
      return elementosTraductor.remove(elemento);
   }

   public String getNombreAgrupador() {
      return nombreAgrupador;
   }

   public Set<ElementoTraductorBo> getElementos() {
      return elementosTraductor;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.nombreAgrupador);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
	 return true;
      }
      if (obj == null) {
	 return false;
      }
      if (getClass() != obj.getClass()) {
	 return false;
      }
      final AgrupadorBo other = (AgrupadorBo) obj;
      if (!Objects.equals(this.nombreAgrupador, other.nombreAgrupador)) {
	 return false;
      }
      return true;
   }
   
   
   
   private ElementoTraductorBo recuperaElementoTraductor(String linea) {

      if (linea == null || linea.isEmpty()) {
	 // la linea esta vacia
	 return null;
      }

      final String BUSCADO = " constant";
      Integer tamanioMaxBuscado = linea.length() - BUSCADO.length() - 2;
      //LOG.debug("El tamaño {}", tamanioMaxBuscado);

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
	 String constante = buscaConstante(indiceBase, linea);
	 LOG.debug("Constante {}", constante);
	 String valor = buscaValor(indiceBase + BUSCADO.length(), linea);
	 LOG.debug("Valor {}", valor);

	 ElementoTraductorBo elem = new ElementoTraductorBo();
	 elem.setConstante(constante);
	 elem.setValor(valor);
	 
	 if( StringUtils.isNumber(valor))
	 {
	    elem.setTipo(TipoFormateador.NUMERO);
	 }
	 else{
	    elem.setTipo(TipoFormateador.CADENA);
	 }
	 
	 return elem;

      }

      return null;
   }

   private String buscaConstante(int indiceBase, String linea) {
      StringBuilder resultado = new StringBuilder();

      indiceBase--;
      while (linea.charAt(indiceBase) == ' ') {
	 indiceBase--;
      }

      while (indiceBase >= 0 && linea.charAt(indiceBase) != ' ') {
	 resultado.append(linea.charAt(indiceBase));
	 indiceBase--;
      }

      return resultado.reverse().toString();

   }

   private String buscaValor(int indice, String linea) {

      String resultado = null;
      while (linea.charAt(indice) != '=') {
	 indice++;
      }
      indice++;
      ///LOG.debug("El indice esta en {}", indice);
      while (linea.charAt(indice) == ' ') {
	 indice++;
      }
      //LOG.debug("El indice esta ahora en {}", indice);
      if (linea.charAt(indice) == '\'') {
	 LOG.debug("Se encontro una cadena");
	 indice++;
	 int indiceFinal = linea.indexOf('\'', indice);
	 LOG.debug("indice final {}", indiceFinal);
	 resultado = linea.substring(indice, indiceFinal );
	 LOG.debug("El valor es {}", resultado);
      } else {

	 if (Character.isDigit(linea.charAt(indice)) || linea.charAt(indice) == '.') {
	    //LOG.debug("Se encontro un numero");
	    resultado = "";

	    while (linea.charAt(indice) != ' ' && linea.charAt(indice) != ';') {
	       resultado += linea.charAt(indice);
	       indice++;
	    }
	 }
      }
      return resultado;
   }
   
   

   @Override
   public String toString() {
      return "AgrupadorBo{" + "nombreAgrupador=" + nombreAgrupador + ", elementosTraductor=" + elementosTraductor + '}';
   }

}
