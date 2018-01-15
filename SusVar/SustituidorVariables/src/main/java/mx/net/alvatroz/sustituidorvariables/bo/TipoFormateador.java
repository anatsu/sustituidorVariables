/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.bo;

import java.io.Serializable;

/**
 *
 * @author alvaro
 */
public enum TipoFormateador implements Serializable
{
    
 

    CADENA(1, "VARCHAR2", "", false),
    NUMERO(2, "NUMBER", "", true),
    FECHA(3, "DATE", "dd-MM-YYYY", true),
    FECHAYHORA(4, "TIMESTAMP", "dd-MM-YYYY HH24:MI:SS", true);

    private final Integer id;    
    private final String descripcion;
    private final String formato;
    private final Boolean ejecutarTrim;

    private TipoFormateador(Integer id, String des, String form, Boolean ejecutarTrim)
    {
        this.id = id;
        descripcion = des;
        formato = form;
        this.ejecutarTrim = ejecutarTrim;
    }

    public String getFormato() {
        return formato;
    }

    public Boolean getEjecutarTrim() {
        return ejecutarTrim;
    }

   public Integer getId() {
      return id;
   }
    
    

    @Override
    public String toString() {
        return descripcion;
    }

    
    
}
