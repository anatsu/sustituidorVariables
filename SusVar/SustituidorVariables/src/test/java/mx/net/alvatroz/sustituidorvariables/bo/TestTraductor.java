/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.bo;


import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author alvaro
 */
public class TestTraductor {

    private final static Logger LOG = LoggerFactory.getLogger(TestTraductor.class);
    @Test
    @Ignore
    public void testTraductor() {
        LOG.debug("traduciendo");

        Assert.assertEquals("select * from c3fiscal.taalgofeo", getTraduccion("select * from c3fiscal.taalgofeo", "necaxa", "10"));
        
        Assert.assertEquals("select * from c3fiscal.taalgofeo where PaFialgo = 10 and PaFiNecaxa = 2", getTraduccion("select * from c3fiscal.taalgofeo where PaFialgo = 10 and PaFiNecaxa = 2", "Necaxa", "10"));
        
        Assert.assertEquals("select * from c3fiscal.taalgofeo where PaFialgo = 10 and 10 = 2", getTraduccion("select * from c3fiscal.taalgofeo where PaFialgo = 10 and PaFiNecaxa = 2", "PaFiNecaxa", "10"));
        Assert.assertEquals("", getTraduccion("", "hola", "1"));
        Assert.assertEquals("select * "
                + "            from c3fiscal.taalgofeo "
                + "           where PaFialgo=10 "
                + "             and 10=10"
                , getTraduccion(
                            "select * "
                + "            from c3fiscal.taalgofeo "
                + "           where PaFialgo=10 "
                + "             and PaFiNecaxa=PaFiNecaxa", "PaFiNecaxa", "10"));
        
            Assert.assertEquals("select * "
                + "            from c3fiscal.taalgofeo "
                + "           where PaFialgo=10 "
                + "             and 10=10 + 10"
                , getTraduccion(
                            "select * "
                + "            from c3fiscal.taalgofeo "
                + "           where PaFialgo=10 "
                + "             and PaFiNecaxa=PaFiNecaxa + PaFiNecaxa", "PaFiNecaxa", "10"));

    }

    public boolean seEncontroLaPalabraCompleta(int indiceFinal, StringBuilder query) {

        if (indiceFinal + 1 >= query.length()) {
            LOG.debug("Esta al final de la cadena");
            return true;

        } else {
            LOG.debug("Verificando el caracter {} ", query.charAt(indiceFinal + 1));
            return !Character.isJavaIdentifierPart(query.charAt(indiceFinal + 1));
        }

    }

    public String getTraduccion(String queryOriginal, String palabra, String valor) {
        StringBuilder salida = new StringBuilder(queryOriginal);

        

        int indicePal = 0;
        int i = 0;
        LOG.debug("indice maximo {}", salida.length() -1 );

        while (i < salida.length()) {
            

            
            // si la letra de la palabra coincide con lo que se busca
            if ( ( i == 0 
                    || indicePal > 0
                    || (    indicePal == 0 
                            && !Character.isJavaIdentifierPart(salida.charAt(i- 1))
                       )
                 ) &&  salida.charAt(i) == palabra.charAt(indicePal)) {
                
                LOG.debug("Se encontro igualdad en {} y en la palabra {} el caracter {}", i, indicePal, palabra.charAt(indicePal));
                indicePal++;

                // si ya llego al final
                if (indicePal == palabra.length()) {
                    LOG.debug("Se coincidio con la longitud");
                    if (seEncontroLaPalabraCompleta(i , salida)) {
                        LOG.debug("Tiene la palabra completa");
                        // sustituye la palabra.
                        LOG.debug("Reemplazando. i = {}, longitud palabravieja = {}, longitud palabra nueva {},  posicion nueva {}"
                                , i
                                , palabra.length()
                                , valor.length()
                                , i - palabra.length() + valor.length() );
                        salida.replace(i - palabra.length()+1, i+1, valor);
                        i = i - palabra.length() + valor.length()+1;
                        // reinicia la busqueda de palabras
                        indicePal = 0;
                    }else
                    { // como no se coincidio se reinicia el proceso en el indice adelante de done comenzo a coindir
                        LOG.debug("No tiene la palabra completa");
                        i = i - (indicePal - 1);
                        indicePal = 0;
                        
                    }

                }

            } else {
                if( indicePal > 0 )
                {
                    
                    // se retrocede un lugar			
                    i = i - (indicePal - 1);
                    indicePal = 0;
                    LOG.debug("Regresando a {}", i);
                }
                

            }
            i++;
        }
        
        return salida.toString();

    }

}
