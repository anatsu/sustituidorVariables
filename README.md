# sustituidorVariables
Este proyecto es para sustituir variables.
En algunos centros de trabajo se solicita generar codígo PLSQL sin tener datos en duro (usando constantes)

csgValor1 CONSTANT NUMBER(1):= 1;


Y en las consultas del paquete:

SELECT *
  FROM ESQUEMA.TAALGO
 WHERE x = csgValor1
 
 
 Cuando hay que realizar modificaciones puede resultar tedioso probar individualmente las consultas del paquete. 
 Como alternativa a esto, este proyecto genera una ventana donde puede sustituirse la consulta individual por las constantes 
 en el paquete.
 
 Los valores de las constantes quedan almacenados en una base de datos H2 para sustituir nuevamente las constantes de una consulta.
 
 Al momento actual hay soporte para resaltado de sintaxis y se espera que con el tiempo pueda leerse un archivo con el 
 package body del paquete así como ejecutar consultas directamente en el editor.
 
 


