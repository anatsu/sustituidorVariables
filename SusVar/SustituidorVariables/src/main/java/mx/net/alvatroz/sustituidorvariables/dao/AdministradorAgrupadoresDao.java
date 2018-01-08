/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import mx.net.alvatroz.sustituidorvariables.bo.AgrupadorBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 *
 * @author alvaro
 */
@Repository
public class AdministradorAgrupadoresDao {
    
    private final static Logger LOG = LoggerFactory.getLogger(AdministradorAgrupadoresDao.class);
    private final String ARCHIVO = "objetos.dat";
    
    public void escribe( Collection<AgrupadorBo> coleccion) throws IOException
    {
        LOG.debug("Escribiendo {} objetos", coleccion.size());
        try (ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream(new File(ARCHIVO)))) {
            oos.writeObject(coleccion);
            oos.flush();
        }
    }
    
    public Set<AgrupadorBo> lee() throws IOException, ClassNotFoundException
    {
        Set<AgrupadorBo> list;
        try (ObjectInputStream ois = new ObjectInputStream( new FileInputStream(new File(ARCHIVO)))) 
        {
            list = new LinkedHashSet<>((Collection<AgrupadorBo>) ois.readObject());
        }
        return list;
    }
    
}
