/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import mx.net.alvatroz.sustituidorvariables.bo.AgrupadorBo;
import mx.net.alvatroz.sustituidorvariables.bo.ElementoTraductorBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * crea los elementos y les asigna una posicion.
 * No agrega listeners a los mismos
 * @author alvaro
 */

public class FrameContenedor extends JFrame
{
    private final static Logger LOG = LoggerFactory.getLogger(FrameContenedor.class);
    protected final JButton botonTraducir = new JButton(RESOURCE_BUNDLE.getString("btnTraducir.nombre"));
    protected final JButton botonAgregarElemento = new JButton(RESOURCE_BUNDLE.getString("btnElementoTraductor.nombre"));
    protected final JTextArea txtAreaTextoATraducir = new  JTextArea();
    protected final JTextArea txtTextoTraducido = new JTextArea("");
    
    
    protected final JComboBox<String> comboAgrupadores = new JComboBox<>();
    protected final JButton botonAgregarAgrupador = new JButton(RESOURCE_BUNDLE.getString("btnAgregarAgrupador.nombre"));
    //protected final ConstantesTableModel tableModel = new ConstantesTableModel(Collections.EMPTY_LIST);
    protected final JTable tablaElementosTraductor = new JTable( new ConstantesTableModel(Collections.EMPTY_LIST)  );
    protected final JMenuItem jItemAgregar = new JMenuItem(RESOURCE_BUNDLE.getString("jItem.agregar"));
    protected final JMenuItem jItemEliminar = new JMenuItem(RESOURCE_BUNDLE.getString("jItem.eliminar"));
    protected final JPopupMenu popupMenuTabla = new JPopupMenu("PopUpTabla");
    
    protected final static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("i18n", Locale.getDefault());

    
    
    public FrameContenedor() throws HeadlessException {
        super(RESOURCE_BUNDLE.getString("tituloPantalla"));
    }
    
    
    public void inicializaPosiciones()
    {        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(getPanelNorte(), BorderLayout.NORTH);
        getContentPane().add(getPanelCentral(), BorderLayout.CENTER);
        getContentPane().add(getPanelConfigurador(), BorderLayout.SOUTH);
    }
    
    public JPanel getPanelNorte()
    {
        JPanel panelNorte = new JPanel( new GridLayout(1, 4));
        panelNorte.add( comboAgrupadores);
        panelNorte.add( botonAgregarAgrupador );
        panelNorte.add( botonAgregarElemento );
        panelNorte.add( botonTraducir );
        
        return panelNorte;
        
    }
    
    public JPanel getPanelCentral()
    {
        JPanel panelSuperior = new JPanel( new GridLayout(1, 2));
        panelSuperior.add( new JScrollPane(txtAreaTextoATraducir));
        panelSuperior.add( new JScrollPane(txtTextoTraducido));
        
        
        return panelSuperior;
    }
    
    public JPanel getPanelConfigurador()
    {
        JPanel panelConfigurador = new JPanel();
        panelConfigurador.setLayout(new GridLayout());
        
        
        JScrollPane scroll = new JScrollPane(tablaElementosTraductor);        
        panelConfigurador.add( scroll);
        panelConfigurador.setPreferredSize( new Dimension( 10, 200));
        
                
        
        tablaElementosTraductor.setDefaultRenderer(Component.class, new ComponentRender());
        tablaElementosTraductor.setDefaultEditor(Component.class, new MiTableCellEditor());
        tablaElementosTraductor.setRowHeight(25);
        
                
        
        
        
        popupMenuTabla.add(jItemAgregar);
        popupMenuTabla.add(jItemEliminar);
        
        
        
                
        tablaElementosTraductor.setComponentPopupMenu(popupMenuTabla);
        

        
        
        return panelConfigurador;
    }
    
    public void repintaTabla()
    {
        
        tablaElementosTraductor.repaint();
        tablaElementosTraductor.updateUI();
    }
    public void repintaTabla( ConstantesTableModel tableModel)
    {
        tablaElementosTraductor.setModel(tableModel);
        repintaTabla();
    }
    public void repintaTabla( List<ElementoTraductorBo> lista )
    {
        tablaElementosTraductor.setModel( new ConstantesTableModel(lista));
        repintaTabla();
    }
    public void repintaTabla(AgrupadorBo agrupadorBo )
    {
        tablaElementosTraductor.setModel( new ConstantesTableModel( agrupadorBo.getElementos()));
        repintaTabla();
    }
    
}
