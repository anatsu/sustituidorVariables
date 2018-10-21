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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import mx.net.alvatroz.sustituidorvariables.bo.IAgrupadorService;
import mx.net.alvatroz.sustituidorvariables.bo.TraductorService;
import mx.net.alvatroz.sustituidorvariables.bo.exception.AgrupadorYaExisteException;
import mx.net.alvatroz.sustituidorvariables.bo.exception.FormateadorInexistenteException;
import mx.net.alvatroz.sustituidorvariables.dto.FormFrameDto;
import mx.net.alvatroz.sustituidorvariablescomundto.AgrupadorSinElementosDto;
import mx.net.alvatroz.sustituidorvariablescomundto.ElementoTraductorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author alvaro
 */
@Controller
public class FrameView extends FrameContenedor {

   private final static Logger LOG = LoggerFactory.getLogger(FrameView.class);

   @Autowired
   private IAgrupadorService agrupadorSevice;
   
   @Autowired
   private TraductorService traductorService;

   private JFileChooser jfc;
   
   FormFrameDto form = new FormFrameDto();
      
   
   @PostConstruct
   public void init() {

      super.inicializaPosiciones();

      agrupadorSevice.iniciaAplicacion();
      addWindowListener(new WindowAdapter() {
	 @Override
	 public void windowClosing(WindowEvent e) {
	    LOG.info("Guardando el agrupador actual antes de salir. El agrupador actual es: {}", form.getAgrupadorActual());
	    if( form.getAgrupadorActual() != null)
	    {
	       agrupadorSevice.guardaAgrupador( form.getAgrupadorActual());
	    }
	    
	    
	 }
      });

      botonTraducir.addActionListener((evt) -> {

	 LOG.debug("Elemento elegido {} ", comboAgrupadores.getSelectedItem());
	 if (comboAgrupadores.getSelectedItem() != null) {
	    try {
	       
	       String textoATraducir = txtAreaTextoATraducir.getText();
	       String textoTraducido = traductorService.traduce(textoATraducir, form.getAgrupadorActual());
	       txtTextoTraducido.setText(textoTraducido);
	    } catch (FormateadorInexistenteException e) {
	       LOG.error("Ocurrio un error al pintar un elemento {}", e);
	       JOptionPane.showMessageDialog(this, e.getMessage());
	    }
	 }

      });

      botonAgregarAgrupador.addActionListener((ActionEvent) -> {
	 String agrupadorNuevo = JOptionPane.showInputDialog(RESOURCE_BUNDLE.getString("nombreAgrupador"));

	 try {
	    LOG.debug("Agrupador nuevo {}", agrupadorNuevo);
	    agrupadorSevice.agregaNuevoAgrupadorSinElementos(form.getAgrupadorSinElementos(), agrupadorNuevo);
	    LOG.debug("La lista de los agrupadores es: {}", form.getAgrupadorSinElementos());
	    comboAgrupadores.addItem(agrupadorNuevo);
	    
	 } catch (AgrupadorYaExisteException ex) {
	    LOG.error("Ya existe el agrupador ", ex);
	    JOptionPane.showMessageDialog(botonAgregarAgrupador, ex.getMessage());
	 }

      });

      comboAgrupadores.addActionListener((ActionEvent) -> {
	 LOG.debug("Guardando información del agrupador actual");
	 if( form.getAgrupadorActual() != null){	    	 
	    agrupadorSevice.guardaAgrupador( form.getAgrupadorActual());
	 }
	 LOG.debug("Cargando el nuevo agrupador {} ", comboAgrupadores.getSelectedItem());
	 int indice = comboAgrupadores.getSelectedIndex();
	 AgrupadorSinElementosDto agrupadorSinElementos = form.getAgrupadorSinElementos().get(indice);
	 form.setAgrupadorActual(agrupadorSevice.cargaAgrupador(agrupadorSinElementos));

	 tablaElementosTraductor.clearSelection();
	 repintaTabla(form.getAgrupadorActual());
	 
      });

      
      form.setAgrupadorSinElementos( agrupadorSevice.getListaAgrupadoresDadosDeAlta());
      if( !form.getAgrupadorSinElementos().isEmpty())
      {	 
	 form.setAgrupadorActual( agrupadorSevice.cargaAgrupador(form.getAgrupadorSinElementos().get(0))); 
      }
      
      form.getAgrupadorSinElementos().forEach( agrupadorSinElem -> comboAgrupadores.addItem(agrupadorSinElem.getFcnombreAgrupador()));
            
      botonAgregarElemento.addActionListener(e -> {
	 eventoAgregarElementoATabla();
      });
      jItemAgregar.addActionListener(e -> {
	 eventoAgregarElementoATabla();
      }
      );
      jItemEliminar.addActionListener(e -> {
	 

	 

	    int filaElegida = tablaElementosTraductor.getSelectedRow();
	    LOG.debug(" Elemento elegido {}", filaElegida);

	    if (filaElegida != -1) {
	    	       	       	       
	       LOG.debug("Se elimina la fila elegida");
	       ElementoTraductorDto elemento = ((ConstantesTableModel) tablaElementosTraductor.getModel()).getElemento(filaElegida);
	       
	       form.getAgrupadorActual().getElementosTraductor().removeIf( elem-> elem.getId().equals( elemento.getId()));
	       
	       repintaTabla(form.getAgrupadorActual());
	    }

	 
      });

      popupMenuTabla.addPopupMenuListener(new PopupMenuListener() {
	 @Override
	 public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

	    Point mousePoint = MouseInfo.getPointerInfo().getLocation();

	    // recupera la fila en base a un punto
	    int rowAtPoint = tablaElementosTraductor.rowAtPoint(SwingUtilities.convertPoint(popupMenuTabla, mousePoint, tablaElementosTraductor));

	    LOG.debug("Hay algo elegido {}", rowAtPoint);

	    jItemEliminar.setEnabled(rowAtPoint > -1);

	    if (rowAtPoint > -1) {
	       tablaElementosTraductor.setRowSelectionInterval(rowAtPoint, rowAtPoint);
	    } else {
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

      jMenuItemCargarDesdeArchivo.addActionListener(l -> {
	 eventoCargarDesdeArchivo();
      });

   }

   private void eventoCargarDesdeArchivo() {
      jfc = new JFileChooser(new File(System.getProperty("user.home")));
      jfc.setFileFilter( new FileNameExtensionFilter(RESOURCE_BUNDLE.getString("jFileChooser.filtroExt"), "sql","pks", "pkb"));
      int eleccion = jfc.showOpenDialog(this);

      if (JFileChooser.APPROVE_OPTION == eleccion) {
	 File archivo = jfc.getSelectedFile();
	 if (archivo != null) {
	    try{
	       
	       agrupadorSevice.agregaAgrupadoresDesdeArchivo(form.getAgrupadorActual(), archivo);
	       
	       repintaTabla(form.getAgrupadorActual());
	    } catch (FileNotFoundException e) {
	       LOG.error("El archivo no se encuntra ", e);
	       JOptionPane.showMessageDialog(this, e.getMessage());
	    } catch (IOException e){
	       LOG.error("Error en la lectura del archivo ", e);
	       JOptionPane.showMessageDialog(this, e.getMessage());
	    }
	 }
      }

   }

   private void eventoAgregarElementoATabla() {
      LOG.debug("Elemento elegido {} ", comboAgrupadores.getSelectedItem());
      if (comboAgrupadores.getSelectedItem() != null) {
	 
	 agrupadorSevice.agregaNuevoElementoTraductor(form.getAgrupadorActual());
	 
	 LOG.debug("El tamaño de los elementos es {}", form.getAgrupadorActual().getElementosTraductor().size());
	 repintaTabla(form.getAgrupadorActual());

      }
   }

}
