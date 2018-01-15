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

   @Override
   public String toString() {
      return "AgrupadorBo{" + "nombreAgrupador=" + nombreAgrupador + ", elementosTraductor=" + elementosTraductor + '}';
   }

}
