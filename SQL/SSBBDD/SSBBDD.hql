create database if not exists convocatoriasdb
Comment 'Base de datos de Convocatorias' 
Location '/user/$(whoami)/convocatoriasdb' 
With dbproperties ('Creada por' = 'User', 'Creada el' = '22-Dic-2024'); 

#######################################################################

use convocatoriasdb; 

CREATE TABLE IF NOT EXISTS convocatorias 
( 
    Numero_Procedimiento STRING,
    Numero_SAF INT,
    Descripcion_SAF STRING,
    Numero_UOC INT,
    Descripcion_UOC STRING,
    Tipo_Procedimiento STRING,
    Modalidad STRING,
    Apartado_Directa STRING,
    Ejercicio INT,
    Fecha_Publicacion STRING,
    Fecha_Apertura STRING,
    Etapa STRING,
    Alcance STRING,
    Nombre_Procedimiento STRING,
    Objeto_Procedimiento STRING,
    Monto_Estimado FLOAT
) 
COMMENT 'Tabla de Convocatorias' 
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY ','  


#######################################################################