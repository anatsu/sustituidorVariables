/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.bo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;

import mx.net.alvatroz.sustituidorvariables.bo.exception.AgrupadorYaExisteException;
import mx.net.alvatroz.sustituidorvariables.bo.exception.LectorFinalizacionException;
import mx.net.alvatroz.sustituidorvariables.bo.exception.LectorInicializacionException;
import mx.net.alvatroz.sustituidorvariables.bo.formateador.FormateadorFacade;
import mx.net.alvatroz.sustituidorvariables.dao.AdministradorAgrupadoresDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author alvaro
 */
@Service
public class AdministradorAgrupadoresBo {
    protected Set<AgrupadorBo> agrupadores;
    private final static Logger LOG = LoggerFactory.getLogger(AdministradorAgrupadoresBo.class);
    
    @Autowired
    private AdministradorAgrupadoresDao dao;
    
    @Autowired
    private FormateadorFacade formateador;

    public AdministradorAgrupadoresBo() {
        agrupadores = new LinkedHashSet<>();
    }

    @PostConstruct
    public void init() 
    {
        try
        {
            agrupadores = dao.lee();
        }catch(FileNotFoundException e)
        {
            LOG.error("Aun no existe el archivo de agrupadores por lo que se generará uno. "
                    + "Se genero una excepcioń que será ignorada",e);
            agrupadores = new  LinkedHashSet<>();
        }catch( IOException | ClassNotFoundException e)
        {
            LOG.error("Ocurrio un error al cargar los agrupadores {}", e);
            throw new LectorInicializacionException("No fue posible cargar el lector", e);
        }
    }
    
    public void persisteCambios()
    {
        try
        {
            dao.escribe(agrupadores);
        }catch( Exception e )
        {
            LOG.error("Ocurrio un error al guardar los agrupadores", e);
            throw new LectorFinalizacionException("No fue posible almacenar los datos", e);
        }
    }
    
    /**
     * Intenta agregar un agrupador
     * @param nombreAgrupador Nombre del agrupador que se va a agregar
     * @throws AgrupadorYaExisteException Si el agrupador ya existe
     */
    public void agregaAgrupador( String nombreAgrupador)
    {
        AgrupadorBo agrupador = new AgrupadorBo(nombreAgrupador, null);
        if( agrupadores.contains(agrupador))
        {
            throw new AgrupadorYaExisteException(nombreAgrupador);
        }
        agrupadores.add(agrupador);
        
    }
    
    public List<String> getNombresAgrupadores()
    {
        List<String> listaNombres = new ArrayList<>(agrupadores.size());
        agrupadores.stream().forEach((agrupador) -> {
            listaNombres.add(agrupador.getNombreAgrupador());
        });
        return listaNombres;
    }
    
    public AgrupadorBo getAgrupador( String nombre )
    {
        return agrupadores.stream().filter((AgrupadorBo t) -> t.getNombreAgrupador().equals(nombre)).findFirst().get();
    }
    
    public String traduce(AgrupadorBo agrupador, String texto)
    {
        return agrupador.traduce(texto, formateador);
    }
    
    public void carga()
    {
        
    }
}
