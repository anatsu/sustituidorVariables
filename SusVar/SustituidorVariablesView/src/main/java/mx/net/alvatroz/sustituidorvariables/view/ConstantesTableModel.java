/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import mx.net.alvatroz.sustituidorvariablescomundto.ElementoTraductorDto;
import mx.net.alvatroz.sustituidorvariablescomundto.TipoFormateador;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Usado para pintar la tabla de elementos que se muestra en pantalla
 * @author alvaro
 */
public class ConstantesTableModel
        extends DefaultTableModel implements Serializable {

    private final static Logger LOG = LoggerFactory.getLogger(ConstantesTableModel.class);
    private List<ElementoTraductorDto> listaElementos;
    
    private List<JTextField> txtConstantes;
    private List<JComboBox> combosTipo;
    private List<JTextField> txtValores;
    private final Object sincronizador = new Object();

    private final static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("i18n", Locale.getDefault());
    
    public ConstantesTableModel(List<ElementoTraductorDto> listaElementos) {
        super();
        setListaElementos(listaElementos);

    }
    public ConstantesTableModel(Collection<ElementoTraductorDto> listaElementos) {
        super();
        setListaElementos(new ArrayList<>(listaElementos));

    }

    private void setListaElementos(List<ElementoTraductorDto> listaElementos) {
        LOG.debug("Preparanado elementos {}", listaElementos);

        synchronized (sincronizador) {

            this.listaElementos = listaElementos;

            txtConstantes = new ArrayList<>(listaElementos.size());
            combosTipo = new ArrayList<>(listaElementos.size());
            txtValores = new ArrayList<>(listaElementos.size());
            listaElementos.stream().forEach((ElementoTraductorDto elem) -> {
                JTextField txtConst = new JTextField( elem.getConstante());
                JTextField txtValor = new JTextField( elem.getValor());
                JComboBox cmb = new JComboBox(new Object[]{
                     TipoFormateador.CADENA, TipoFormateador.NUMERO, TipoFormateador.FECHA, TipoFormateador.FECHAYHORA}
                );
                if( elem.getTipo() != null )
                {
                    cmb.setSelectedItem( elem.getTipo());
                }
                
                if( elem.getValor() == null )
                {
                    elem.setValor("");
                }
                
                txtConstantes.add(txtConst);
                combosTipo.add(cmb);
                txtValores.add(txtValor);
                
                
                
                txtConst.getDocument().addDocumentListener((MiDocumentListener) (e) -> {
                    elem.setConstante(txtConst.getText());
                });
                
                
                
                txtValor.getDocument().addDocumentListener( (MiDocumentListener)(e) -> {
                    elem.setValor( txtValor.getText());
                });
                
                cmb.addActionListener((e) -> {
                    elem.setTipo((TipoFormateador) cmb.getSelectedItem());
                    LOG.debug("eligiendo el tipo {}", elem.getTipo());
                });
                
                // inicializa el combo con un valor
                if( elem.getTipo() == null)
                {
                    cmb.setSelectedIndex(0);
                }
                
                
            });
        }

    }

    

    @Override
    public int getRowCount() {

        return listaElementos == null ? 0 : this.listaElementos.size();

    }

    @Override
    public int getColumnCount() {

        return 3;

    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            // constante
            case 0:
                return JTextField.class;
            // combo
            case 1:
                return JComboBox.class;
            // otro textfield
            case 2:
                return JTextField.class;
        }

        return null;
    }

    @Override
    public String getColumnName(int column) {

        switch( column)
        {
            case 0 : return RESOURCE_BUNDLE.getString("encabezado.constante");
            case 1 : return RESOURCE_BUNDLE.getString("encabezado.tipo");
            case 2 : return RESOURCE_BUNDLE.getString("encabezado.valor");
            default: return "";
        }
        

    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return txtConstantes.get(rowIndex);
            case 1:
                return combosTipo.get(rowIndex);
            case 2:
                return txtValores.get(rowIndex);
        }
        return null;
    }
    
    /**
     * Retorna el elemento que se encuentra rendereado en la fila
     * @param fila
     * @return 
     */
    
    public ElementoTraductorDto getElemento( int fila)
    {
        ElementoTraductorDto elemento = null;
        
        if( fila < listaElementos.size())
        {
            elemento = listaElementos.get(fila);
        }
        
        return elemento;
        
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        LOG.debug("Asignando valor {} en fila {} columna {}", aValue, row, column);
        
    }

    
}
