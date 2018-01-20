/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.view;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.annotation.PostConstruct;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import mx.net.alvatroz.sustituidorvariables.bo.AdministradorAgrupadoresBo;
import mx.net.alvatroz.sustituidorvariables.bo.AgrupadorBo;
import mx.net.alvatroz.sustituidorvariables.bo.ElementoTraductorBo;
import mx.net.alvatroz.sustituidorvariables.bo.exception.AgrupadorYaExisteException;
import mx.net.alvatroz.sustituidorvariables.bo.exception.FormateadorInexistenteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author alvaro
 */
@Controller
public class FrameView extends FrameContenedor{
    private final static Logger LOG = LoggerFactory.getLogger(FrameView.class);
    
    
    @Autowired
    private AdministradorAgrupadoresBo administradorAgrupadoresBo;

    
    
    @PostConstruct    
    public void init()
    {
        
        super.inicializaPosiciones();        
        addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                administradorAgrupadoresBo.persisteCambios();
            }            
        });
        
        botonTraducir.addActionListener( (evt)->{
            
            LOG.debug( "Elemento elegido {} " , comboAgrupadores.getSelectedItem());
            if( comboAgrupadores.getSelectedItem() != null )
            {
	       try{
                AgrupadorBo agrupador = administradorAgrupadoresBo.getAgrupador(comboAgrupadores.getSelectedItem()+"");
                String textoTraducido = administradorAgrupadoresBo.traduce( agrupador, txtAreaTextoATraducir.getText());
                txtTextoTraducido.setText(textoTraducido);
	       }catch( FormateadorInexistenteException e)
	       {
		  LOG.error("Ocurrio un error al pintar un elemento {}", e);
		  JOptionPane.showMessageDialog(this, e.getMessage());
	       }
            }
            
        });
        
        botonAgregarAgrupador.addActionListener((ActionEvent ) -> {
            String agrupadorNuevo = JOptionPane.showInputDialog(RESOURCE_BUNDLE.getString("nombreAgrupador"));
            
            try{
                if( agrupadorNuevo != null && !agrupadorNuevo.isEmpty())
                {
                    administradorAgrupadoresBo.agregaAgrupador(agrupadorNuevo);
                    comboAgrupadores.addItem(agrupadorNuevo); 
                }
                
                
                
            }catch( AgrupadorYaExisteException ex)
            {
                LOG.error("Ya existe el agrupador ", ex);
                JOptionPane.showMessageDialog(botonAgregarAgrupador, ex.getMessage());
            }
                
            
        });
        
        comboAgrupadores.addActionListener( (ActionEvent)->{
            LOG.debug( "Elemento elegido {} " , comboAgrupadores.getSelectedItem());
            AgrupadorBo agrupador = administradorAgrupadoresBo.getAgrupador(comboAgrupadores.getSelectedItem()+"");
            LOG.debug("Agrupador recuperado {}", agrupador);                        
            tablaElementosTraductor.clearSelection();
            repintaTabla(agrupador);
            
        });
        
        administradorAgrupadoresBo.getNombresAgrupadores().stream().forEach((agrupador) -> {
            comboAgrupadores.addItem(agrupador);
        });
        
        botonAgregarElemento.addActionListener(e -> {
            eventoAgregarElementoATabla();
        });
        jItemAgregar.addActionListener( e-> {
            eventoAgregarElementoATabla();
        }
        );
        jItemEliminar.addActionListener( e->{
            final AgrupadorBo agrupador = administradorAgrupadoresBo.getAgrupador(comboAgrupadores.getSelectedItem()+"");
            
            if (agrupador != null) {
                
                int filaElegida = tablaElementosTraductor.getSelectedRow();
                LOG.debug(" Elemento elegido {}", filaElegida );
                
                if( filaElegida != -1 )
                {
                    ElementoTraductorBo elemento = ((ConstantesTableModel)tablaElementosTraductor.getModel()).getElemento( filaElegida );
                    LOG.debug("El elemento eliminado es {}", elemento);
                    LOG.debug("Elementos en el modelo {}", agrupador.getElementos());
                    boolean eliminado = agrupador.eliminaElemento(elemento);
                    LOG.debug("Elemento eliminado {}", eliminado);
                    
                    repintaTabla( agrupador);
                }
                                
                        
                
                
            }
        });
        
        popupMenuTabla.addPopupMenuListener( new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                 
                Point mousePoint = MouseInfo.getPointerInfo().getLocation();

                // recupera la fila en base a un punto
                int rowAtPoint = tablaElementosTraductor.rowAtPoint(SwingUtilities.convertPoint(popupMenuTabla, mousePoint, tablaElementosTraductor));

                LOG.debug("Hay algo elegido {}", rowAtPoint);
                
                jItemEliminar.setEnabled(  rowAtPoint > -1 );
                
                if (rowAtPoint > -1) {
                    tablaElementosTraductor.setRowSelectionInterval(rowAtPoint, rowAtPoint);
                }
                else
                {
                    tablaElementosTraductor.clearSelection();                    
                }
                
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                LOG.debug("Pop up invisible");
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                
            }
        });
        
        
        
        
        
    }

    private void eventoAgregarElementoATabla() {
        LOG.debug( "Elemento elegido {} " , comboAgrupadores.getSelectedItem());
        if( comboAgrupadores.getSelectedItem() != null )
        {
            AgrupadorBo agrupador = administradorAgrupadoresBo.getAgrupador(comboAgrupadores.getSelectedItem()+"");
            agrupador.agregaElemento();
            LOG.debug("El tamaño de los elementos es {}", agrupador.getElementos().size());
            
            repintaTabla( agrupador);
            
        }
    }
    
   
    
    
    
    
    
    
}
