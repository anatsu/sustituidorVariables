/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.bo;

import java.util.Collection;
import java.util.Set;
import javax.annotation.Resource;
import mx.net.alvatroz.sustituidorvariables.ApplicationTest;
import mx.net.alvatroz.sustituidorvariables.bo.exception.AgrupadorSinNombreException;
import mx.net.alvatroz.sustituidorvariables.bo.exception.AgrupadorYaExisteException;
import mx.net.alvatroz.sustituidorvariables.dao.AdministradorAgrupadoresDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author alvaro
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ApplicationTest.class})
public class AgrupadorBoTest {

    private final static Logger LOG = LoggerFactory.getLogger(AgrupadorBoTest.class);

    @Resource
    private AdministradorAgrupadoresBo administradorAgrupadores;
    
    @Resource
    private AdministradorAgrupadoresDao dao;

    @Test
    public void testVerfificaNombreVacio() {
        try {
            try {
                administradorAgrupadores.agregaAgrupador(null);
                Assert.fail("Es necesario que el agrupador tenga un nombre");
            } catch (AgrupadorSinNombreException e) {

            }
            administradorAgrupadores.agregaAgrupador(" ");
            Assert.fail("El agrupador no puede tener un nombre vacio");
        } catch (AgrupadorSinNombreException e) {

        }
    }

    @Test
    public void testRegistraAgrupador() {
        administradorAgrupadores.agregaAgrupador("agrupa1");
        try {
            try {
                administradorAgrupadores.agregaAgrupador("agrupa1");
                Assert.fail("El agrupador no puede estar dos veces");
            } catch (AgrupadorYaExisteException e) {

            }
            administradorAgrupadores.agregaAgrupador(" agrupa1 ");
            Assert.fail("El agrupador no puede estar dos veces");
        } catch (AgrupadorYaExisteException e) {

        }

    }
    
    @Test
    public void testCorroboraRecuperacion()
    {
        administradorAgrupadores.agregaAgrupador("miAgrapador");
        administradorAgrupadores.agregaAgrupador("otroAgrapador");
        Assert.assertNotNull(administradorAgrupadores.getAgrupador("miAgrapador"));
        Assert.assertNotNull(administradorAgrupadores.getAgrupador("otroAgrapador"));
        AgrupadorBo agrupador = administradorAgrupadores.getAgrupador("miAgrapador");
        agrupador.agregaElemento();
        Set<ElementoTraductorBo> elementos = agrupador.getElementos();
        Assert.assertEquals(1, elementos.size());
        
    }
    
    @Test
    public void testTraduce()
    {
        administradorAgrupadores.agregaAgrupador("elAgrupador");
        AgrupadorBo agrupador = administradorAgrupadores.getAgrupador("elAgrupador");
        Assert.assertEquals("", administradorAgrupadores.traduce(agrupador, ""));
        ElementoTraductorBo elementoAgregado = agrupador.agregaElemento();
        Assert.assertEquals("", administradorAgrupadores.traduce(agrupador, ""));
        elementoAgregado.setConstante("csgNada");
        Assert.assertEquals(" SELECT * FROM DUAL WHERE X = csgNada", administradorAgrupadores.traduce(agrupador, " SELECT * FROM DUAL WHERE X = csgNada"));
        elementoAgregado.setValor("hola");
        Assert.assertEquals(" SELECT * FROM DUAL WHERE X = csgNada", administradorAgrupadores.traduce(agrupador, " SELECT * FROM DUAL WHERE X = csgNada"));
        elementoAgregado.setTipo(TipoFormateador.CADENA);
        Assert.assertEquals(" SELECT * FROM DUAL WHERE X = 'hola'", administradorAgrupadores.traduce(agrupador, " SELECT * FROM DUAL WHERE X = csgNada"));
        ElementoTraductorBo elementoAgregadoFecha = agrupador.agregaElemento();
        elementoAgregadoFecha.setTipo(TipoFormateador.FECHA);
        elementoAgregadoFecha.setConstante("csgFecha ");
        elementoAgregadoFecha.setValor( "2016-01-06");	
        Assert.assertEquals(" SELECT * FROM DUAL WHERE X = 'hola' and fecha = TO_DATE( '2016-01-06', 'dd-MM-YYYY') "
                , administradorAgrupadores.traduce(agrupador, " SELECT * FROM DUAL WHERE X = csgNada and fecha = csgFecha "));
        
        LOG.debug("Traducido {}", administradorAgrupadores.traduce(agrupador, " SELECT * FROM DUAL WHERE X = csgNada and fecha = csgFecha+ csgFecha+ csgFecha"));
        Assert.assertEquals(" SELECT * FROM DUAL WHERE X = 'hola' and fecha = TO_DATE( '2016-01-06', 'dd-MM-YYYY')+ TO_DATE( '2016-01-06', 'dd-MM-YYYY')+ TO_DATE( '2016-01-06', 'dd-MM-YYYY')"
                , administradorAgrupadores.traduce(agrupador, " SELECT * FROM DUAL WHERE X = csgNada and fecha = csgFecha+ csgFecha+ csgFecha"));
        
        
    }
    
    @Test
    public void testEscribe()
    {
        LOG.debug("Probando escritura");
        administradorAgrupadores.agregaAgrupador("jajaja");
        administradorAgrupadores.agregaAgrupador("lero lero");
        AgrupadorBo agrupador = administradorAgrupadores.getAgrupador("jajaja");
        ElementoTraductorBo elem = agrupador.agregaElemento();
        elem.setConstante("constante");
        elem.setTipo(TipoFormateador.NUMERO);
        elem.setValor("10");
        
        try
        {
            dao.escribe(administradorAgrupadores.agrupadores);
            
            Collection<AgrupadorBo> lista  = dao.lee();
            Assert.assertNotNull(lista);
            
            for( AgrupadorBo agrupa : lista )
            {
                LOG.debug("Agrupador {}", agrupa);
            }
        }catch( Exception e)
        {
            LOG.error("Fallo", e);
            Assert.fail(e.getMessage());
        }
        LOG.debug("Fin de la escritura");
    }
    
    public void testInicializa()
    {
       
    }
}
