
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import mx.net.alvatroz.sustituidorvariables.bo.ElementoTraductorBo;
import mx.net.alvatroz.sustituidorvariables.bo.TipoFormateador;
import mx.net.alvatroz.sustituidorvariables.lectorconstantes.service.LectorVariablesServices;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alvaro
 */
public class TestLeeArchivo {
   private final static Logger LOG = LoggerFactory.getLogger(TestLeeArchivo.class);
   
   
   public void compruebaElemento( ElementoTraductorBo e , String valor, TipoFormateador tipoEsperado )
   {
      Assert.assertEquals(valor, e.getValor());
      Assert.assertEquals(tipoEsperado, e.getTipo());
      
   }
   
   @Test
   public void testLeeArchivo() 
   {
      LectorVariablesServices lvs = new LectorVariablesServices();
      try( InputStream is = ClassLoader.getSystemResource("archivo1.pkb").openStream() 
	 
	 ){
	 List<ElementoTraductorBo> elementos = lvs.leeArchivo(is);
	 
	 Map<String, ElementoTraductorBo> mapaLeidos = new HashMap<>();
	 
	 elementos.forEach( e -> mapaLeidos.put(e.getConstante(), e));
	 
	 
	 compruebaElemento(mapaLeidos.get("csgNegativo"), "-1", TipoFormateador.NUMERO);
	 compruebaElemento(mapaLeidos.get("csgNegativo2"), "-1.3", TipoFormateador.NUMERO);
	 compruebaElemento(mapaLeidos.get("csgCadena"), "Mi cadena", TipoFormateador.CADENA);
	 compruebaElemento(mapaLeidos.get("csgCadena2"), "a", TipoFormateador.CADENA);
	 compruebaElemento(mapaLeidos.get("csgCadena3"), "hola", TipoFormateador.CADENA);
	 compruebaElemento(mapaLeidos.get("csgEntero1"), "1", TipoFormateador.NUMERO);
	 compruebaElemento(mapaLeidos.get("csgEntero2"), "20", TipoFormateador.NUMERO);
	 compruebaElemento(mapaLeidos.get("csgExpresionRegular"), "[^a|b(cf)]", TipoFormateador.CADENA);
	 compruebaElemento(mapaLeidos.get("v_pi"), "3.14", TipoFormateador.NUMERO);
	 compruebaElemento(mapaLeidos.get("salary_increase"), "10", TipoFormateador.NUMERO);
	 compruebaElemento(mapaLeidos.get("pi"), "3.141592654", TipoFormateador.NUMERO);
	 compruebaElemento(mapaLeidos.get("edept"), "Web Developer", TipoFormateador.CADENA);
	 compruebaElemento(mapaLeidos.get("csgAlgo"), "je suis un homme", TipoFormateador.CADENA);
	 
	 
	 
	 
	 elementos.forEach(  e -> LOG.debug("El valor es: {} => {} ", e.getConstante(), e.getValor()));
	 
	 
      }catch( Exception e)
      {
	 LOG.error("Fallo ", e);
      }
      
   }
}
