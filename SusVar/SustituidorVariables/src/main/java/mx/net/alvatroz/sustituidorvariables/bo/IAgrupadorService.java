/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.net.alvatroz.sustituidorvariables.bo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import mx.net.alvatroz.sustituidorvariablescomundto.AgrupadorDto;
import mx.net.alvatroz.sustituidorvariablescomundto.AgrupadorSinElementosDto;

/**
 * Servicio para recuperacion de agrupadores
 * @author alvaro
 */
public interface IAgrupadorService {
   
   /**
    * Guarda el agrupador y todos sus elementos
    * @param agrupadorCompleto 
    */
   void guardaAgrupador( AgrupadorDto agrupadorCompleto);
   
   /**
    * Realiza procesos al iniciar la aplicación.
    * Prepara el Sistema de persistencia
    */
   void iniciaAplicacion();
   
   /**
    * Carga la información de un agrupador
    * @param agrupadorSinElementos Agrupador que solo tiene id y nombre no tiene su nombre al completo
    * @return Un Agrupador con todos y sus elementos
    */
   AgrupadorDto cargaAgrupador( AgrupadorSinElementosDto agrupadorSinElementos);
   
   /**
    * Elimina todos los datos del agrupador
    * @param idAgrupador Identificador del agrupador no debe ser nulo
    */
   void eliminaAgrupador( Long idAgrupador);
   
   
   /**
    * Recupera la lista de agrupadores que se han dado de alta
    * @return Una lista de agrupadores (solo sus nombres y sus identificadores)
    */
   List<AgrupadorSinElementosDto> getListaAgrupadoresDadosDeAlta();
   
   /**
    * Genera un nuevo agrupador sin elementos en la memoria. Asigna un identificador
    * que no debe colisionar con ningun otro agrupador. Si se desea generar muchos
    * agrupadores es mejor generar un metodo que realize la actividad explicitamente
    * en lugar de usar este
    * @param nombre Nombre del agrupador sin elementos
    * @return Un nuevo agrupador sin elementos
    */
   AgrupadorSinElementosDto generaNuevoAgrupador( String nombre);
   
   /**
    * Genera un nuevo agrupador sin elementos y lo agrega  a la lista que se recibe por parametro.
    * Si se intenta agregar un agrupador cuyo nombre ya este en la lista se generará un excepcion
    * @param listaAgrupadores Lista de agrupadores sin elementos. No debe ser nula pero se vale que este vacia
    * @param nombreNuevoAgrupador Nombre del nuevo agrupador que se agrega
    */
   void agregaNuevoAgrupadorSinElementos( List<AgrupadorSinElementosDto> listaAgrupadores, String nombreNuevoAgrupador);
   
   /**
    * Agrega un nuevo elemento a un agrupador
    * @param agrupador agrega un nuevo elemento traductor
    */
   void agregaNuevoElementoTraductor( AgrupadorDto agrupador);
   
   /**
    * Agrega al agrupador los elementos encontrados en un archivo
    * @param agrupador agrupador al que se le agregan elementos
    * @param archivo archivo con el cual se agregan
    * @throws java.io.FileNotFoundException
    */
   void agregaAgrupadoresDesdeArchivo( AgrupadorDto agrupador, File archivo) throws FileNotFoundException, IOException;
}
