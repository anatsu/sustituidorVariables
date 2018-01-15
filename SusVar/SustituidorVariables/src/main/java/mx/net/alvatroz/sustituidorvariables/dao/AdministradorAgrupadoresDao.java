/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.dao;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import mx.net.alvatroz.sustituidorvariables.bo.AgrupadorBo;
import mx.net.alvatroz.sustituidorvariables.bo.ElementoTraductorBo;
import mx.net.alvatroz.sustituidorvariables.bo.TipoFormateador;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author alvaro
 */
@Repository
public class AdministradorAgrupadoresDao {

   private final static Logger LOG = LoggerFactory.getLogger(AdministradorAgrupadoresDao.class);

   
   @Autowired
   private ResourceLoader resourceLoader;
   
   @Autowired
   private JdbcTemplate template;

   @Transactional
   public void escribe(Collection<AgrupadorBo> coleccion) throws IOException {
      coleccion.stream().map((agrupador) -> {
	 if (agrupador.getFiid() == null) {
	    LOG.debug("No se habia registrado el agrupador {}", agrupador.getNombreAgrupador());
	    final String INSERT_AGRUPADOR = "INSERT INTO TAAGRUPADOR ( FCNOMBREAGRUPADOR ) VALUES ( ? )";
	    PreparedStatementCreator psc = new PreparedStatementCreator() {
	       @Override
	       public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
		  PreparedStatement ps = con.prepareStatement(INSERT_AGRUPADOR,
		     new String[]{"FIID"});

		  ps.setString(1, agrupador.getNombreAgrupador());

		  return ps;
	       }
	    };
	    
	    KeyHolder keyHolder = new GeneratedKeyHolder();
	    template.update( psc, keyHolder);
	    LOG.debug("SE ha insertado con: {}", keyHolder.getKey());
	    agrupador.setFiid(keyHolder.getKey().intValue());

	 } else {
	    LOG.debug("Se actualiza un nuevo agrupador");
	    template.update("UPDATE TAAGRUPADOR SET FCNOMBREAGRUPADOR = ? WHERE FIID = ?", agrupador.getNombreAgrupador(), agrupador.getFiid());
	 }
	 return agrupador;
      }).forEachOrdered((agrupador) -> {
	 LOG.debug("Registrando detalles del agrupador los elementos son: {}", agrupador.getElementos());
	 agrupador
	    .getElementos()
	    .stream()
	    .filter( 
	       elemento -> 
		  elemento.getConstante() != null 
		     && !elemento.getConstante().trim().isEmpty()
		     && elemento.getValor() != null
	             && !elemento.getValor().trim().isEmpty()
	             && elemento.getTipo() != null
	    ).forEach((elemento) -> {
	    
	    LOG.debug("Actualizando el elemento {}", elemento);
	    int filasActualizadas = template.update("update TAELEMENTOTRADUCTOR  \n"
		  + "   set FCCONSTANTE = ?\n"
		  + "     , FITIPOAGRUPADOR = ?\n"
		  + "     , FCVALOR = ?\n"
		  + " where FIID = ?", elemento.getConstante(), elemento.getTipo().getId(), elemento.getValor(), elemento.getId());
	    
	    if ( filasActualizadas == 0) {
	    
	       final String INSERT_AGRUPADOR = "INSERT INTO TAELEMENTOTRADUCTOR (FIID, FIIDTRADUCTOR,FCCONSTANTE,FITIPOAGRUPADOR,FCVALOR) "
		  + "VALUES ( ?,?,?,?,?);";

	       int filasInsertadas = template.update(INSERT_AGRUPADOR
		  , elemento.getId(), agrupador.getFiid(), elemento.getConstante(), elemento.getTipo().getId(), elemento.getValor());
	       LOG.debug("Filas insertadas {}", filasInsertadas);
	       

	    } 
	 });
      });
   }

   public void inicializaBD()
   {
      Resource recurso = resourceLoader.getResource("classpath:inicializa.sql");
      LOG.debug("El archivo existe : {}", recurso.exists());
      try{
	String ejecucion = IOUtils.toString(recurso.getURL(), Charset.defaultCharset()); 
	template.execute( ejecucion);
      }catch( IOException e)
      {
	 LOG.error("Fallo la inicialización ",e);
      }
      
      
   }
   
   public boolean estaInicializadaLaBd()
   {
      try{
	int total = template.queryForObject(" SELECT COUNT(*) FROM TATIPOFORMATEADOR", Integer.class); 
	return total > 0;
      }catch( DataAccessException e)
      {
	 return false;
      }
      
      
   }
   
   /**
    * Lee todos los datos de la base de datos.
    * @return El agrupador nuevo que se ha generado
    * @throws IOException
    * @throws ClassNotFoundException 
    */
   public Set<AgrupadorBo> lee() throws IOException, ClassNotFoundException {

      Map<Integer, AgrupadorBo> agrupadores = new HashMap<>();
      String SELECT = "  SELECT nom.FIID\n"
	 + "       , nom.FCNOMBREAGRUPADOR\n"
	 + "       , elem.FIID IDELEMENTO\n"
	 + "       , elem.FCCONSTANTE\n"
	 + "       , elem.FITIPOAGRUPADOR\n"
	 + "       , elem.FCVALOR\n"
	 + "    FROM TAELEMENTOTRADUCTOR elem\n"
	 + "    JOIN TAAGRUPADOR nom\n"
	 + "      ON nom.FIID = elem.FIIDTRADUCTOR\n"
	 + "ORDER BY nom.FCNOMBREAGRUPADOR\n"
	 + "       , elem.FCCONSTANTE";
      template.query(SELECT, new ColumnMapRowMapper()).forEach((mapa) -> {
	 
	 Integer idAgrupador = ((Long) mapa.get("FIID")).intValue();
	 AgrupadorBo agrupador = agrupadores.get( idAgrupador);

	 if (agrupador == null) {
	    agrupador = new AgrupadorBo((String) mapa.get("FCNOMBREAGRUPADOR"), idAgrupador);
	    agrupadores.put(agrupador.getFiid(), agrupador);

	 }

	 ElementoTraductorBo elemento = agrupador.agregaElemento( (Long)mapa.get("IDELEMENTO"));
	 elemento.setConstante((String) mapa.get("FCCONSTANTE"));
	 elemento.setTipo(getTipo( new Integer( (Byte)mapa.get("FITIPOAGRUPADOR"))));
	 elemento.setValor((String) mapa.get("FCVALOR"));
      });

      return new HashSet<>(agrupadores.values());
   }

   public TipoFormateador getTipo(Integer idTipo) {
      for (TipoFormateador tipo : TipoFormateador.values()) {
	 if (tipo.getId().equals(idTipo)) {
	    return tipo;
	 }
      }
      return null;
   }

}
