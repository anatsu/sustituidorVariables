/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.bo;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author alvaro
 */
public class ElementoTraductorBo implements Serializable, Comparable<ElementoTraductorBo>{
    /**
     * Identificador. No funciona en entornos concurrentes
     */
    private final Long id;
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

    public ElementoTraductorBo() {
        id = System.currentTimeMillis();
    }

   public ElementoTraductorBo(Long id) {
      this.id = id;
   }
    
    

    /**
     * Identificador del elemento traductor
     * @return 
     */
    public Long getId() {
        return id;
    }
    
    
    
    public String getConstante() {
        return constante;
    }

    public void setConstante(String constante) {
        if( constante != null )
        {
            this.constante = constante.trim();
        }
        
    }

    public TipoFormateador getTipo() {
        return tipo;
    }

    public void setTipo(TipoFormateador tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public int compareTo(ElementoTraductorBo o) {
        
        String esto = this.constante == null ? "" : this.constante.toUpperCase();
        String otro = o.constante == null ? "" : o.constante.toUpperCase();
        
        return esto.compareTo(otro);
        
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
        
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
        final ElementoTraductorBo other = (ElementoTraductorBo) obj;
        
        return Objects.equals(this.id, other.id);
        
    }


    

    
    
}
