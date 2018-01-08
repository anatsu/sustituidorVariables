/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.bo;

/**
 *
 * @author alvaro
 */

public class Traductor {
 
    private boolean seEncontroLaPalabraCompleta(int indiceFinal, StringBuilder query) {

        if (indiceFinal + 1 >= query.length()) {
            
            return true;

        } else {
            
            return !Character.isJavaIdentifierPart(query.charAt(indiceFinal + 1));
        }

    }

    /**
     * Traduce de un query sustituyendo las palabras por su respectivo valor
     * considerando la busqueda de la palabra considerando delimitaciones de palabra
     * reservada.
     * @param queryOriginal Query original donde se sustituye
     * @param palabra Palabra que se busca en el texto
     * @param valor Valor conque se sustituira en la cadena
     * @return Una cadena de la misma longitud 
     */
    public StringBuilder getTraduccion(StringBuilder queryOriginal, String palabra, String valor) {
        StringBuilder salida = new StringBuilder(queryOriginal);

        

        int indicePal = 0;
        int i = 0;
        

        while (i < salida.length()) {
            

            
            // si la letra de la palabra coincide con lo que se busca
            if ( ( i == 0 
                    || indicePal > 0
                    || (    indicePal == 0 
                            && !Character.isJavaIdentifierPart(salida.charAt(i- 1))
                       )
                 ) &&  salida.charAt(i) == palabra.charAt(indicePal)) {
                
        
                indicePal++;

                // si ya llego al final
                if (indicePal == palabra.length()) {
        
                    if (seEncontroLaPalabraCompleta(i , salida)) {
        
                        // sustituye la palabra.
        
                        salida.replace(i - palabra.length()+1, i+1, valor);
                        i = i - palabra.length() + valor.length()+1;
                        // reinicia la busqueda de palabras
                        indicePal = 0;
                    }else
                    { // como no se coincidio se reinicia el proceso en el indice adelante de done comenzo a coindir
                        
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
                    
                }
                

            }
            i++;
        }
        
        return salida;

    }
}
