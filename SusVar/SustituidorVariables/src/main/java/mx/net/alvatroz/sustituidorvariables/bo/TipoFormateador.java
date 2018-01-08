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
    
 

    CADENA("VARCHAR2", "", false),
    NUMERO("NUMBER", "", true),
    FECHA("DATE", "dd-MM-YYYY", true),
    FECHAYHORA("DATE", "dd-MM-YYYY HH24:MI:SS", true);
    
    private final String descripcion;
    private final String formato;
    private final Boolean ejecutarTrim;

    private TipoFormateador(String des, String form, Boolean ejecutarTrim)
    {
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
    
    

    @Override
    public String toString() {
        return descripcion;
    }

    
    
}
