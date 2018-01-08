/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.view;

import javax.swing.event.DocumentEvent;

/**
 *
 * @author alvaro
 */
public interface  MiDocumentListener extends javax.swing.event.DocumentListener{

    @Override
    public default void insertUpdate(DocumentEvent e) {
        eventoCambioTexto( e);
    }

    @Override
    public default void removeUpdate(DocumentEvent e) {
        eventoCambioTexto( e);
    }

    @Override
    public default void changedUpdate(DocumentEvent e) {
        eventoCambioTexto( e);
    }
    
    abstract void eventoCambioTexto(DocumentEvent e);
    
}
