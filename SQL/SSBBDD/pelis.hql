create database if not exists pelisdb
Comment 'Base de datos de Peliculas y Series' 
Location '/user/$(whoami)/pelisdb' 
With dbproperties ('Creada por' = 'User', 'Creada el' = '04-Ene-2025'); 

#######################################################################

use pelisdb; 

CREATE TABLE IF NOT EXISTS imdb_movies
( 
    title STRING,
    year STRING,
    certificate STRING,
    duration STRING,
    genre STRING,
    rating STRING,
    description STRING,
    stars STRING,
    votes STRING
) 
COMMENT 'Tabla de Peliculas' 
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY ','
TBLPROPERTIES ("skip.header.line.count"="1");

#######################################################################

use pelisdb; 

CREATE TABLE IF NOT EXISTS tv_shows
( 
    index STRING,
    id STRING,
    title STRING,
    year STRING,
    age STRING,
    imdb_rating STRING,
    rotten_tomatoes STRING,
    netflix STRING,
    hulu STRING,
    prime_video STRING,
    disney_plus STRING,
    type STRING
) 
COMMENT 'Tabla de Series' 
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY ','
TBLPROPERTIES ("skip.header.line.count"="1");