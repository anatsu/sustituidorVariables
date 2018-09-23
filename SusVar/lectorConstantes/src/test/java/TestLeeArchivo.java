
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import mx.net.alvatroz.sustituidorvariables.bo.ElementoTraductorBo;
import mx.net.alvatroz.sustituidorvariables.lectorconstantes.service.LectorVariablesServices;
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
   
   @Test
   public void testLeeArchivo() 
   {
      LectorVariablesServices lvs = new LectorVariablesServices();
      try( InputStream is = ClassLoader.getSystemResource("archivo1.pkb").openStream() 
	 
	 ){
	 List<ElementoTraductorBo> elementos = lvs.leeArchivo(is);
	 
	 elementos.forEach(  e -> LOG.debug("El valor es: {} => {} ", e.getConstante(), e.getValor()));
	 
	 
      }catch( Exception e)
      {
	 LOG.error("Fallo ", e);
      }
      
   }
}
