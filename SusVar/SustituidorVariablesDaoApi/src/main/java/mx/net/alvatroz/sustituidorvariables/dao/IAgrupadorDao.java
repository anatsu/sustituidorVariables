/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.dao;

import java.util.List;
import mx.net.alvatroz.sustituidorvariablescomundto.AgrupadorDto;
import mx.net.alvatroz.sustituidorvariablescomundto.AgrupadorSinElementosDto;
import mx.net.alvatroz.sustituidorvariablescomundto.ElementoTraductorDto;

/**
 *
 * @author alvaro
 */
public interface IAgrupadorDao {
   
   /**
    * Recupera una lista de agrupadores. Un agrupador tiene un ID y un nombre
    * @return Una lista de los agrupadores utilizados
    */
   List<AgrupadorSinElementosDto> getAgrupadores();
   
   /**
    * Recupera una lista de elementos usados para traducir un agrupador
    * @param idAgrupador Identificador del agrupador no 
    * @return Lista con los elementos utilizados para traducir
    */
   List<ElementoTraductorDto> getElementosDeAgrupador( Long idAgrupador);
   
   /**
    * Realiza el guardado de un nuevo agrupador
    * @param agrupadorSoloNombre 
    */
   void guardaNuevoAgruapador( AgrupadorSinElementosDto agrupadorSoloNombre);
   
   /**
    * Realiza la actualización del nombre de un elemento traductor
    * @param agrupadorSoloNombre 
    */
   void actualizaNombreAgrupador( AgrupadorSinElementosDto agrupadorSoloNombre);
   
   /**
    * Guarda los elementos de un agrupador
    * @param elementos Lista de elementos para traducción
    * @param idAgrupador Identificador del agrupador
    */
   void guardaElementosDeAgrupador( List<ElementoTraductorDto> elementos, Long idAgrupador);
   
   /**
    * Elimina un agrupador
    * @param idAgrupador identificador del agrupador
    */
   void eliminaAgrupador( Long idAgrupador);
   
   /**
    * Elimina todos los elementos de un agrupador
    * @param idAgrupador Identificador del agrupador
    */
   void elminaElementosXIdAgrupador( Long idAgrupador);
   /**
    * Usado para inicializar la persistencia las implementaciones podrían no hacer
    * nada si no es necesario
    */
   void inicializaPersistencia();
 
   /**
    * Incida si es necesario inicializar la BD
    * @return true si se debe inicializar, false en otro caso
    */
   boolean estaInicializadaLaPersistencia();
   
   /**
    * Regresa el número de la unidad de persistencaia
    * @return 0 para la primer version.
    */
   Integer getVersionPersistencia();
   
   /**
    * Solo actualiza el número de la version
    * @param numeroVersion 
    */
   void actualizaAVersion( Integer numeroVersion);
   
   /**
    * Ejecuta la actualizacion de la version
    * @param numeroVersion 
    */
   void ejecutaActualizacion( Integer numeroVersion);
}
