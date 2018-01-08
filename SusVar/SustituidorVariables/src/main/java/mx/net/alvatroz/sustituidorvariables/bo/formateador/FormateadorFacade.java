/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.bo.formateador;

import java.util.EnumMap;
import java.util.Map;
import mx.net.alvatroz.sustituidorvariables.bo.TipoFormateador;
import mx.net.alvatroz.sustituidorvariables.bo.exception.FormateadorInexistenteException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 *
 * @author alvaro
 */
@Service
public class FormateadorFacade implements ApplicationContextAware{
   
    private final Map<TipoFormateador, IFormateador> mapaTipos = new EnumMap<>(TipoFormateador.class);
    
    
    
    public String formatea( String cadena , TipoFormateador tipo)
    {
        if( tipo == null || cadena == null)
        {
            return "";
        }
        
        IFormateador formateador = mapaTipos.get(tipo);
        
        if( formateador == null )
        {
            throw new FormateadorInexistenteException(tipo);
        }
        else
        {
            String cadenaAtraducir = tipo.getEjecutarTrim() ? cadena.trim() : cadena;
            return formateador.formatea(cadenaAtraducir, tipo.getFormato());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        Map<String, IFormateador> mapa = ac.getBeansOfType(IFormateador.class);
        mapaTipos.clear();
        
        mapa.values().stream().forEach((formateador) -> {
            mapaTipos.put(formateador.getElemento(), formateador);
        });
    }
}
