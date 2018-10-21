/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.dao.h2;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import mx.net.alvatroz.sustituidorvariablescomundto.AgrupadorSinElementosDto;
import mx.net.alvatroz.sustituidorvariablescomundto.ElementoTraductorDto;
import mx.net.alvatroz.sustituidorvariablescomundto.TipoFormateador;
import mx.net.alvatroz.sustituidorvariables.dao.h2.dto.TaElementoTraductorDto;
import mx.net.alvatroz.sustituidorvariables.dao.IAgrupadorDao;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Implementación usando H2 de la persistencia
 * @author alvaro
 */
@Repository
public class AgrupadorH2Dao implements IAgrupadorDao{

   private final static Logger LOG = LoggerFactory.getLogger(AgrupadorH2Dao.class);
   
   @Value("${totalFormateadores}")
   private String queryTotalFormateadores;
   
   @Value("${TAAGRUPADOR.insert}")
   private String taAgrupadorInsert;
   
   @Value("${TAAGRUPADOR.update}")
   private String taAgrupadorUpdate;
   
   @Value("${TAAGRUPADOR.selectAll}")
   private String taAgrupadorSelectTodo;
   
   @Value("${TAAGRUPADOR.delete}")
   private String taAgrupadorDelete;
   
   @Value("${TAELEMENTOTRADUCTOR.delete}")
   private String taElementoTraductorDelete;
   
   @Value("${TAELEMENTOTRADUCTOR.update}")
   private String taElementoTraductorUpdate;
   
   @Value("${TAELEMENTOTRADUCTOR.insert}")
   private String taElementoTraductorInsert;
   
   @Value("${TAELEMENTOTRADUCTOR.selectPorIdAgrupador}")
   private String taElementoTraductorSelectPorIdAgrupador;
   
   @Value("${TAVERSIONES.selectVersionActual}")
   private String taVersionesSelectVersionActual;
   
   @Value("${TAVERSIONES.update}")
   private String taVersionesUpdate;
   
   
   
   @Autowired
   private ResourceLoader resourceLoader;
   
   @Autowired
   private JdbcTemplate  template;

   
   /**
    * Ejecuta la instrucción que inicia la base de datos la base de datos
    * @return 
    */
   @Override
   public boolean estaInicializadaLaPersistencia()
   {
      try{
	LOG.info("Revisando si la base de datos esta inicializada con el query {}", queryTotalFormateadores);
	int total = template.queryForObject(queryTotalFormateadores, Integer.class); 
	return total > 0;
      }catch( BadSqlGrammarException e)
      {
	 return false;
      }
      
   }
   
   
   
   @Override
   public List<AgrupadorSinElementosDto> getAgrupadores() {
      return template.query(taAgrupadorSelectTodo, new BeanPropertyRowMapper<>(AgrupadorSinElementosDto.class));
   }

   @Override
   public List<ElementoTraductorDto> getElementosDeAgrupador(Long idAgrupador) {
      
      
      
      List<TaElementoTraductorDto> lista = template.query(taElementoTraductorSelectPorIdAgrupador
	 , new Long[]{idAgrupador}
	 , new BeanPropertyRowMapper<>(TaElementoTraductorDto.class));
      
      Map<Integer, TipoFormateador> mapaEnum = new HashMap<>();
      mapaEnum.put(TipoFormateador.CADENA.getId(), TipoFormateador.CADENA);
      mapaEnum.put(TipoFormateador.NUMERO.getId(), TipoFormateador.NUMERO);
      mapaEnum.put(TipoFormateador.FECHA.getId(), TipoFormateador.FECHA);
      mapaEnum.put(TipoFormateador.FECHAYHORA.getId(), TipoFormateador.FECHAYHORA);
      
      class TraduceTraductores implements Function<TaElementoTraductorDto, ElementoTraductorDto>{

	 @Override
	 public ElementoTraductorDto apply(TaElementoTraductorDto t) {
	    ElementoTraductorDto dto = new ElementoTraductorDto();
	    dto.setConstante(t.getFcconstante());
	    dto.setId(t.getFiid());
	    dto.setValor(t.getFcvalor());
	    dto.setTipo( mapaEnum.get(t.getFitipoagrupador()));
	    return dto;
	 }
	 
	 
      }
      
      
      return lista.stream().map( new TraduceTraductores()).collect(Collectors.toList());
      
   }

   @Override
   public void guardaNuevoAgruapador(AgrupadorSinElementosDto agrupadorSoloNombre) {
            
      template.update(taAgrupadorInsert, agrupadorSoloNombre.getFiid(), agrupadorSoloNombre.getFcnombreAgrupador());
            
   }

   @Override
   public void actualizaNombreAgrupador(AgrupadorSinElementosDto agrupadorSoloNombre) {
      template.update(taAgrupadorUpdate
	 , agrupadorSoloNombre.getFcnombreAgrupador()
	 , agrupadorSoloNombre.getFiid());
   }

   @Override
   public void guardaElementosDeAgrupador( List<ElementoTraductorDto> elementos, Long idAgrupador) {
      elementos.forEach( e-> {
           template.update(taElementoTraductorInsert
	      , e.getId()
	      , idAgrupador
	      , e.getConstante()
	      , e.getTipo().getId()
	      , e.getValor()
	   );
      });
   }

   @Override
   public void inicializaPersistencia() {
      Resource recurso = resourceLoader.getResource("classpath:inicializa.sql");
      LOG.debug("El archivo de inicialización inicializa.sql existe : {}", recurso.exists());
      try{
	 
	String ejecucion = IOUtils.toString(recurso.getURL(), Charset.defaultCharset()); 
	template.execute( ejecucion);
      }catch( IOException e)
      {
	 LOG.error("Fallo la inicialización ",e);
	 throw new DataAccessResourceFailureException("persistencia.error.inicializaPersistencia", e);	 
      }
      
   }

   @Override
   public void eliminaAgrupador(Long idAgrupador) {
      template.update(taAgrupadorDelete, idAgrupador);
   }

   @Override
   public void elminaElementosXIdAgrupador(Long idAgrupador) {
      template.update(taElementoTraductorDelete, idAgrupador);
   }

   @Override
   public Integer getVersionPersistencia() {
      try
      {
	 return template.queryForObject(taVersionesSelectVersionActual, Integer.class);
      }catch( BadSqlGrammarException e )
      {
	 
	 return 0;
      }
   }

   @Override
   public void actualizaAVersion(Integer numeroVersion) {
      template.update(taVersionesUpdate, numeroVersion);
   }

   @Override
   public void ejecutaActualizacion(Integer numeroVersion) {
      
      final String FORMATO = "v%02d.sql";
      
      String nombreArchivo = String.format(FORMATO, numeroVersion);
      
      Resource recurso = resourceLoader.getResource("classpath:"+nombreArchivo);
      LOG.debug("Revisando si existe el archivo {} : {}", nombreArchivo, recurso.exists());
      try{
	 
	String ejecucion = IOUtils.toString(recurso.getURL(), Charset.defaultCharset()); 
	template.execute( ejecucion);
      }catch( IOException e)
      {
	 LOG.error("Fallo la inicialización ",e);
	 throw new DataAccessResourceFailureException("persistencia.error.actualizacionVersion", e);	 
      }
   }
   
}
