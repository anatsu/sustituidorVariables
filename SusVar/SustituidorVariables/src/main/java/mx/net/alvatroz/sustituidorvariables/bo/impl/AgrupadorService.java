/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.bo.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import mx.net.alvatroz.sustituidorvariables.bo.IAgrupadorService;
import mx.net.alvatroz.sustituidorvariables.bo.LectorVariablesServices;
import mx.net.alvatroz.sustituidorvariables.bo.exception.AgrupadorSinNombreException;
import mx.net.alvatroz.sustituidorvariables.bo.exception.AgrupadorYaExisteException;
import mx.net.alvatroz.sustituidorvariablescomundto.AgrupadorDto;
import mx.net.alvatroz.sustituidorvariablescomundto.AgrupadorSinElementosDto;
import mx.net.alvatroz.sustituidorvariablescomundto.ElementoTraductorDto;
import mx.net.alvatroz.sustituidorvariablescomundto.TipoFormateador;
import mx.net.alvatroz.sustituidorvariables.dao.IAgrupadorDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author alvaro
 */
@Service
public class AgrupadorService implements IAgrupadorService{

   private final static Logger LOG = LoggerFactory.getLogger(AgrupadorService.class);
   @Autowired
   private IAgrupadorDao agrupadorDao;
   
   @Value("${versionActual}")
   private Integer versionActual;
   
   @Autowired
   private LectorVariablesServices lectorService;
   
   @Override
   @Transactional
   public void guardaAgrupador(AgrupadorDto agrupadorCompleto) {
      
      AgrupadorSinElementosDto agrupadorSE = new AgrupadorSinElementosDto();
      agrupadorSE.setFcnombreAgrupador( agrupadorCompleto.getNombreAgrupador());
      agrupadorSE.setFiid( agrupadorCompleto.getFiid());
      
      Long idAgrupador;
      
      if( agrupadorCompleto.getFiid() < 0 )
      {
	 agrupadorCompleto.setFiid( System.currentTimeMillis());
	 agrupadorSE.setFiid( agrupadorCompleto.getFiid());
	 
	 
	 agrupadorDao.guardaNuevoAgruapador( agrupadorSE);
	 
	 
	 idAgrupador = agrupadorSE.getFiid();
	 
      }
      else{
	 agrupadorDao.actualizaNombreAgrupador(agrupadorSE);
	 agrupadorDao.elminaElementosXIdAgrupador( agrupadorSE.getFiid());
	 idAgrupador = agrupadorCompleto.getFiid();
	 
      }
      
      Set<String> constantes = new HashSet<>();
	 
      
      List<ElementoTraductorDto> resultado = new ArrayList<>(agrupadorCompleto.getElementosTraductor().size());
	 
      // elimina los dobles
      agrupadorCompleto.getElementosTraductor()
			.stream()
			.forEach( e -> {
			   if( !constantes.contains(e.getConstante()))
			   {
			      constantes.add(e.getConstante());
			      resultado.add(e);
			   }
			});
      
      agrupadorDao.guardaElementosDeAgrupador( resultado, idAgrupador);
      
      
   }

   @Override
   public void iniciaAplicacion() {
      boolean estaInicializadaLaPersistencia = agrupadorDao.estaInicializadaLaPersistencia();
      LOG.info("Esta inicializada la persistencia : {}", estaInicializadaLaPersistencia);
      if( !estaInicializadaLaPersistencia ){
	 LOG.info("Inicializando la persistencia");
	 agrupadorDao.inicializaPersistencia();
      }
      
      for( int numeroDeVersion = agrupadorDao.getVersionPersistencia() ; numeroDeVersion < versionActual ; numeroDeVersion++)
      {
	 LOG.info("Actualizando a la versión {}", numeroDeVersion+1);
	 agrupadorDao.ejecutaActualizacion(numeroDeVersion+1);
	 agrupadorDao.actualizaAVersion(numeroDeVersion+1);
	 LOG.info("Actualización realizada");
      }
      
   }

   @Override
   public AgrupadorDto cargaAgrupador(AgrupadorSinElementosDto agrupadorSinElementos){
      
      AgrupadorDto resultado = new AgrupadorDto();
      resultado.setFiid(agrupadorSinElementos.getFiid());
      resultado.setNombreAgrupador(agrupadorSinElementos.getFcnombreAgrupador());
      
      resultado.setElementosTraductor(agrupadorDao.getElementosDeAgrupador(agrupadorSinElementos.getFiid()));
      
      return resultado;
   }

   @Override
   @Transactional
   public void eliminaAgrupador(Long idAgrupador) {
      agrupadorDao.elminaElementosXIdAgrupador(idAgrupador);
      agrupadorDao.eliminaAgrupador(idAgrupador);
   }

   @Override
   public List<AgrupadorSinElementosDto> getListaAgrupadoresDadosDeAlta() {
      return agrupadorDao.getAgrupadores();
   }

   @Override
   public AgrupadorSinElementosDto generaNuevoAgrupador(String nombre) {
      AgrupadorSinElementosDto agrupadorSinElementosDto = new AgrupadorSinElementosDto();
      agrupadorSinElementosDto.setFcnombreAgrupador(nombre);
      agrupadorSinElementosDto.setFiid( -System.currentTimeMillis());
      return agrupadorSinElementosDto;
   }

   @Override
   public void agregaNuevoAgrupadorSinElementos(List<AgrupadorSinElementosDto> listaAgrupadores, String nombreNuevoAgrupador) {
      
      if( nombreNuevoAgrupador == null || nombreNuevoAgrupador.trim().isEmpty())
      {
	 throw new AgrupadorSinNombreException();
      }
      
      Optional<AgrupadorSinElementosDto> primerEncontrado = listaAgrupadores.stream().filter( agrup -> nombreNuevoAgrupador.equalsIgnoreCase(agrup.getFcnombreAgrupador())).findFirst();
      
      if( primerEncontrado.isPresent())
      {
	 throw new AgrupadorYaExisteException(nombreNuevoAgrupador);
      }
      LOG.debug("Agregando un nuevo agrupador");
      listaAgrupadores.add( generaNuevoAgrupador(nombreNuevoAgrupador));
      
      
      
   }

   @Override
   public void agregaNuevoElementoTraductor(AgrupadorDto agrupador) {
      ElementoTraductorDto elemento = new ElementoTraductorDto();
      elemento.setId(System.currentTimeMillis());
      elemento.setTipo(TipoFormateador.CADENA);
      elemento.setConstante("");
      elemento.setValor("");
      agrupador.getElementosTraductor().add( elemento );
   }

   @Override
   public void agregaAgrupadoresDesdeArchivo(AgrupadorDto agrupador, File archivo) throws FileNotFoundException, IOException{
      try ( FileInputStream fis = new FileInputStream(archivo))
      {
	       
	       List<ElementoTraductorDto> elementos = lectorService.leeArchivo( fis);
	       LOG.debug("Elementos encontrados {}", elementos.size());	       
	       
	       // no agrega elementos que ya existan
	       
	       final AtomicLong id = new AtomicLong(System.currentTimeMillis());
	       
	       List<ElementoTraductorDto> sinDobles = elementos.stream()
			.filter(  e ->
				 !agrupador.getElementosTraductor()
					  .stream()
					  .filter( g -> g.getValor()
					  .equalsIgnoreCase( e.getConstante()))
					  .findFirst()
					  .isPresent()
			      )
			.peek( e -> e.setId(id.getAndIncrement()))
			.collect( Collectors.toList());
	       
	       
	       LOG.debug("Sin dobles {}", sinDobles.size());
	       agrupador.getElementosTraductor().addAll(sinDobles);
	       
	    } catch (FileNotFoundException e) {
	       LOG.error("El archivo no se encuntra ", e);
	       throw e;
	    } catch (IOException e){
	       LOG.error("Error en la lectura del archivo ", e);
	       throw e;
	    }
   }
   
}
