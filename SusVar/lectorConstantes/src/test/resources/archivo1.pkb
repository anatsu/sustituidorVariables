   csgAlgo CONSTANT varchar2(5):='je suis un homme';-- hola

set serveroutput on
set verify off

accept radius prompt 'Please enter radius of the circle: '

declare
v_input number(8);
v_output number(8);
v_line varchar2(80);
csgCadena CONSTANT VARCHAR2(90):= 'Mi cadena'; -- esta es mi cadena
csgCadena2 CONSTANT VARCHAR2( 2 CHAR):= 'a';  -- esta es la a
csgCadena3 CONSTANT C3FISCAL.TABLITA.CAMPO%TYPE:= 'hola';  -- esta es hola
csgEntero1  CONSTANT NUMBER(1):= 1 ; -- es el 1
csgEntero2  CONSTANT NUMBER(2):= 20; 
csgExpresionRegular CONSTANT VARCHAR2(4 CHAR):= '[^a|b(cf)]';

v_pi CONSTANT number(8) :=3.14;

salary_increase CONSTANT number (3) := 10; 

   pi constant number := 3.141592654;
    edept CONSTANT varchar2(15) := 'Web Developer';
   csgAlgo CONSTANT varchar2(5):='je suis un homme';-- hola
    
   csgNegativo CONSTANT NUMBER(2):= -1;
   csgNegativo2 CONSTANT NUMBER(5,6):= -1.3;
begin
v_output := v_pi*&radius**2;
v_line := 
'The area of a circle with radius '||'&radius'||' is '||to_char(v_output);
dbms_output.put_line(v_line);
end;
/
