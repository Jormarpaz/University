############ 1. CREAR EL ESQUEMA DE LA BASE DE DATOS AUTOMOVILES ############

-- 1.1 Si existe, borrar la base de datos Automoviles:

DROP DATABASE IF EXISTS AUTOMOVILES;

-- 1.2 Crear la base de datos Automoviles:

CREATE DATABASE IF NOT EXISTS AUTOMOVILES;

-- 1.3 Usar la base de datos Automóviles: 

USE AUTOMOVILES;

# Para la creación de las tablas de la base de datos tener en cuenta:
#   Los siguientes atributos son números enteros sin signo: cifm, cifc, codcoche, dni, ID y cantidad
#   Los siguientes atributos son cadenas de texto de un máximo de 30 caracteres: nombre, apellidos, ciudad, modelo y color
#   Salvo ventas.color y distribución.cantidad, ningún atributo puede tomar valor null
#   Las claves primarías y las claves externas mirarlas en el diagrama de esquema proporcionado.

# https://dev.mysql.com/doc/refman/8.0/en/creating-tables.html

-- 1.4 Crear la relación marcas(cifm,nombre,ciudad)

# DROP TABLE IF EXISTS MARCAS;
CREATE TABLE marcas (
  cifm int UNSIGNED,
  nombre varchar(30) NOT NULL,
  ciudad varchar(30) NOT NULL,
  PRIMARY KEY  (cifm)
);


-- 1.5 Crear la relación concesionarios(cifc,nombre,ciudad)

# DROP TABLE IF EXISTS concesionarios;
CREATE TABLE concesionarios (
  cifc int UNSIGNED,
  nombre varchar(30) NOT NULL,
  ciudad varchar(30) NOT NULL,
  PRIMARY KEY (cifc)
);


-- 1.6 Crear la relación coches(codcoche,nombre,modelo)

# DROP TABLE IF EXISTS coches;
CREATE TABLE coches (
  codcoche int UNSIGNED,
  nombre varchar(30) NOT NULL,
  modelo varchar(30) NOT NULL,
  PRIMARY KEY (codcoche)
); 


-- 1.7 Crear la relación clientes(dni,nombre,apellidos,ciudad)

# DROP TABLE IF EXISTS clientes;
CREATE TABLE clientes (
  dni int UNSIGNED,
  nombre varchar(30) NOT NULL,
  apellidos varchar(30) NOT NULL,
  ciudad varchar(30) NOT NULL,
  PRIMARY KEY (dni)
);


-- 1.8 Crear la relación ventas(ID,cifc,dni,codcoche,color)

# DROP TABLE IF EXISTS ventas;
CREATE TABLE ventas (
  ID int UNSIGNED,
  cifc int UNSIGNED,
  dni int UNSIGNED,
  codcoche int UNSIGNED,
  color VARCHAR(30),
  PRIMARY KEY (ID),
  FOREIGN KEY (cifc) REFERENCES CONCESIONARIOS(cifc),
  FOREIGN KEY (dni) REFERENCES CLIENTES(dni),
  FOREIGN KEY (codcoche) REFERENCES COCHES(codcoche)
);


-- 1.9 Crear la relación marco(cifm,codcoche)

# DROP TABLE IF EXISTS marco;
CREATE TABLE marco (
  cifm int UNSIGNED,
  codcoche int UNSIGNED,
  PRIMARY KEY (cifm,codcoche),
  FOREIGN KEY (cifm) REFERENCES MARCAS(cifm),
  FOREIGN KEY (codcoche) REFERENCES COCHES(codcoche)
);



-- 1.10 Crear la relación distribución(cifc,codcoche,cantidad)

# DROP TABLE IF EXISTS distribución;
CREATE TABLE distribución (
  cifc int UNSIGNED,
  codcoche int UNSIGNED,
  cantidad int UNSIGNED,
  PRIMARY KEY (cifc,codcoche),
  FOREIGN KEY (cifc) REFERENCES CONCESIONARIOS(cifc),
  FOREIGN KEY (codcoche) REFERENCES COCHES(codcoche)
);


-- 1.11 Mostrar el listado con el nombre las relaciones de la Base de datos Automóviles

SHOW TABLES;

-- 1.12 Mostrar los detalles de la definición de cada una de las relaciones creadas 

DESC MARCAS;
DESC CONCESIONARIOS;
DESC COCHES;
DESC CLIENTES;
DESC Ventas;
DESC Marco;
DESC Distribucion;


############ 2. HACER MODIFICACIONES EN EL ESQUEMA ############

# Se hacen modificaciones y luego se deshacen para dejar el esquema igual
# https://dev.mysql.com/doc/refman/8.0/en/alter-table.html

-- 2.1 Modificar el nombre de la tabla "distribución" por "distribuciones"

RENAME TABLE Distribución TO Distribuciones;

-- 2.2 Comprobar mostrando el listado con el nombre las relaciones de la Base de datos Automóviles

SHOW TABLES;

-- 2.3 Añadir el atributo precio a la relación distribuciones como 12 digitos, 2 de ellos decimales.

ALTER TABLE distribuciones ADD precio FLOAT(12,2);

-- 2.4 Comprobar mostrando los detalles de la relación distribuciones

DESC distribuciones; 

-- 2.5 Cambiar el nombre al atributo "precio" por "PVP" pero con el mismo dominio

ALTER TABLE distribuciones CHANGE COLUMN precio PVP FLOAT(12,2);

-- 2.6 Comprobar mostrando los detalles de la relación distribuciones

DESC distribuciones;

-- 2.7 Eliminar el atributo "PVP" a la relación distribuciones

ALTER TABLE distribuciones DROP PVP;

-- 2.8 Modificar el nombre de la tabla "distribuciones" por "distribución"

RENAME TABLE distribuciones TO distribución;


############ 3. POBLAR LA BASES DE DATOS AUTOMOVILES ############

-- 3.1 Añadir las siguientes tuplas a la relación "marcas"

/*
 (1,'Seat','Madrid')
 (2,'Renault','Barcelona')
 (3,'Citroen','Valencia')
 (4,'Audi','Madrid')
 (5,'Opel','Bilbao')
 (6,'BMW','Madrid')
*/

INSERT INTO MARCAS (cifm,nombre,ciudad) VALUES 
 (1,'Seat','Madrid'),
 (2,'Renault','Barcelona'),
 (3,'Citroen','Valencia'),
 (4,'Audi','Madrid'),
 (5,'Opel','Bilbao'),
 (6,'BMW','Madrid');


-- 3.2 Añadir las siguientes tuplas a la relación "concesioarios"

/*
 (1,'Acar','Madrid')
 (2,'Bcar','Madrid')
 (3,'Ccar','Barcelona')
 (4,'Dcar','Valencia')
 (5,'Ecar','Bilbao')
 (6,'Mcar','Salamanca')
*/

 INSERT INTO CONCESIONARIOS (cifc,nombre,ciudad) VALUES 
 (1,'Acar','Madrid'),
 (2,'Bcar','Madrid'),
 (3,'Ccar','Barcelona'),
 (4,'Dcar','Valencia'),
 (5,'Ecar','Bilbao'),
 (6,'Mcar','Salamanca');


-- 3.3 Añadir las siguientes tuplas a la relación "coches"

/*
 (1,'Ibiza','GTX')
 (2,'Ibiza','GTI')
 (3,'Ibiza','GTD')
 (4,'Toledo','GTD')
 (5,'Córdoba','GTI')
 (6,'Megane','1.6')
 (7,'Megane','GTI')
 (8,'Laguna','GTD')
 (9,'Laguna','TD')
 (10,'Zx','16V')
 (11,'Zx','TD')
 (12,'Aantia','GTD')
 (13,'A4','1.8')
 (14,'A4','2.8')
 (15,'Astra','CARAVAN')
 (16,'Astra','GTI')
 (17,'Corsa','1.4')
 (18,'300','316i')
 (19,'500','525i')
 (20,'700','750i')
*/


INSERT INTO COCHES (codcoche,nombre,modelo) VALUES 
 (1,'Ibiza','GTX'),
 (2,'Ibiza','GTI'),
 (3,'Ibiza','GTD'),
 (4,'Toledo','GTD'),
 (5,'Córdoba','GTI'),
 (6,'Megane','1.6'),
 (7,'Megane','GTI'),
 (8,'Laguna','GTD'),
 (9,'Laguna','TD'),
 (10,'Zx','16V'),
 (11,'Zx','TD'),
 (12,'Aantia','GTD'),
 (13,'A4','1.8'),
 (14,'A4','2.8'),
 (15,'Astra','CARAVAN'),
 (16,'Astra','GTI'),
 (17,'Corsa','1.4'),
 (18,'300','316i'),
 (19,'500','525i'),
 (20,'700','750i');


-- 3.4 Añadir las siguientes tuplas a la relación "clientes"

/*
 (1,'Luis','García','Madrid'),
 (2,'Antonio','López','Valencia'),
 (3,'Juan','Martín','Madrid'),
 (4,'María','García','Madrid'),
 (5,'Javier','Gonzalez','Barcelona'),
 (6,'Ana','López','Barcelona');
*/

INSERT INTO clientes (dni,nombre,apellidos,ciudad) VALUES 
 (1,'Luis','García','Madrid'),
 (2,'Antonio','López','Valencia'),
 (3,'Juan','Martín','Madrid'),
 (4,'María','García','Madrid'),
 (5,'Javier','Gonzalez','Barcelona'),
 (6,'Ana','López','Barcelona'),
 (10,'Pepe','Pérez','Luarca'),
 (20,'Pablo','García','Luarca'),
 (30,'Antonio','González','Luarca'),
 (15,'Marisa','Capo','Madrid'),
 (25,'María','orozco','Sevilla');


-- 3.5 Añadir las siguientes tuplas a la relación "ventas"

/*
 (1234,1,1,1,'Blanco')
 (1235,1,2,5,'Rojo')
 (1236,2,1,6,'Rojo')
 (1237,2,3,8,'Blanco')
 (1238,3,4,11,'Rojo')
 (1239,4,5,14,'Verde')
*/

INSERT INTO ventas (ID,cifc,dni,codcoche,color) VALUES 
 (1234,1,1,1,'Blanco'),
 (1235,1,2,5,'Rojo'),
 (1236,2,1,6,'Rojo'),
 (1237,2,3,8,'Blanco'),
 (1238,3,4,11,'Rojo'),
 (1239,4,5,14,'Verde');

-- 3.6 Añadir las siguientes tuplas a la relación "marco"

/*
 (1,1)
 (1,2)
 (1,3)
 (1,4)
 (1,5)
 (2,6)
 (2,7)
 (2,8)
 (2,9)
 (3,10)
 (3,11)
 (3,12)
 (4,13)
 (4,14)
 (5,15)
 (5,16)
 (5,17)
 (6,18)
 (6,19)
 (6,20)
*/


INSERT INTO marco (cifm,codcoche) VALUES 
 (1,1),
 (1,2),
 (1,3),
 (1,4),
 (1,5),
 (2,6),
 (2,7),
 (2,8),
 (2,9),
 (3,10),
 (3,11),
 (3,12),
 (4,13),
 (4,14),
 (5,15),
 (5,16),
 (5,17),
 (6,18),
 (6,19),
 (6,20);

-- 3.7 Añadir las siguientes tuplas a la relación "distribución"

/*
 (1,1,6)
 (1,5,6)
 (1,6,6)
 (2,6,6)
 (2,8,7)
 (2,9,7)
 (3,10,6)
 (3,11,6)
 (3,12,6)
 (4,13,7)
 (4,14,6)
 (5,15,7)
 (5,16,17)
 (5,17,5)
 (6,1,6)
 (6,2,NULL)
*/

INSERT INTO distribución (cifc,codcoche,cantidad) VALUES 
 (1,1,6),
 (1,5,6),
 (1,6,6),
 (2,6,6),
 (2,8,7),
 (2,9,7),
 (3,10,6),
 (3,11,6),
 (3,12,6),
 (4,13,7),
 (4,14,6),
 (5,15,7),
 (5,16,17),
 (5,17,5),
 (6,1,6),
 (6,2,NULL);


-- 3.8 Verificar el contenido de todas las tablas

select * from marcas;
select * from concesionarios;
select * from coches;
select * from clientes;
select * from ventas;
select * from marco;
select * from distribución;


############ 4. MODIFICAR CONTENIDO TUPLAS AUTOMOVILES ############

-- 4.1 Actualice todos los coches vendidos de color Rojo, por color Granate.

SET SQL_SAFE_UPDATES=0;

UPDATE ventas SET color='Granate' WHERE color='Rojo';

-- 4.2 Actualice los coches de color verdes por verde metalizado.

UPDATE ventas SET color='Verde metalizado' WHERE color='Verde';


-- 4.3 En la tabla de CLIENTES, cambie el nombre de Luis por Luisa (dni=1)

UPDATE clientes SET nombre='Luisa' WHERE dni=1;

-- 4.4 Listar el contenido de las relaciones ventas y clientes

SELECT * FROM ventas;
SELECT * FROM clientes;

-- 4.5 Deshacer los cambios hechos en los pasos anteriores. Dejar "Rojo", "Verde" y "Luis"

UPDATE ventas SET color='Rojo' WHERE color='Granate';
UPDATE ventas SET color='Verde' WHERE color='Verde metalizado';
UPDATE clientes SET nombre='Luis' WHERE dni=1;

############ 5. BORRAR TUPLAS ############

-- Ejecutar el siguiente comando para permitir borrados sin usar clave primaria en en Where

SET SQL_SAFE_UPDATES=0;


-- 5.1 Crear tabla temporal con las tuplas a borrar (para restituir después) con las tuplas de ventas con color="Blanco"
--     Listar contenidos de ventas y de borrados_ventas

CREATE TEMPORARY TABLE borrados_ventas SELECT * FROM ventas where color="Blanco";
select * from ventas;
select * from borrados_ventas;


-- 5.2 Borrar las tuplas de ventas con color="Blanco" y listar contenido de ventas

delete from ventas where color="Blanco";
select * from ventas;


-- 5.4 Volver a añadir las tuplas borradas

insert into ventas select * from borrados_ventas;


-- Ejecutar el siguiente comando para restituir el estado de esa variable del sistema

SET SQL_SAFE_UPDATES=1;



############ 6. CONSULTAS SENCILLAS ############

-- 6.1  Obtenga todos los campos de todos los clientes de Madrid.
SELECT * FROM CLIENTES WHERE ciudad='Madrid';

-- 6.2  Muestre todas las ciudades distintas de las marcas.
SELECT DISTINCT ciudad FROM MARCAS;

-- 6.3  Obtenga los nombres de todas las marcas de coches y ordénelas alfabéticamente.
SELECT nombre FROM MARCAS ORDER BY nombre;

-- 6.4  Cuente el número de clientes que existen por ciudad.
SELECT ciudad,COUNT(dni) FROM CLIENTES GROUP BY ciudad;

-- 6.5  Cuente el número de coches de un mismo modelo.
SELECT modelo, COUNT(codcoche) FROM COCHES GROUP BY modelo;

-- 6.6  Calcule el número de coches recibidos en total por un concesionario.
SELECT cifc, SUM(cantidad) FROM Distribucion GROUP BY cifc;

-- 6.7  Obtenga la mayor cantidad de coches recibida por un concesionario.
SELECT cifc, MAX(cantidad) FROM Distribucion GROUP BY cifc;

-- 6.8  Obtenga la menor cantidad de coches recibidos por un concesionario.
SELECT cifc, MIN(cantidad) FROM Distribucion GROUP BY cifc;

-- 6.9  Obtenga el nombre de todos los concesionarios que hayan recibido al menos 10 coches y ordénelos descendentemente.
SELECT nombre FROM CONCESIONARIOS WHERE cifc IN(SELECT cifc FROM Distribucion WHERE cantidad>=10) ORDER BY nombre DESC;

-- 6.10 Obtenga el cifc de todos los concesionarios que han adquirido más de 10 coches o menos de 5.
SELECT cifc FROM Distribucion WHERE cantidad>10 OR cantidad<5;

-- 6.11 Obtenga el nombre y el apellidos de aquellos clientes que han comprado coches rojos.
SELECT nombre, apellidos FROM CLIENTES WHERE dni IN (SELECT dni FROM Ventas WHERE color='rojo');

-- 6.12 Obtenga los nombres de aquellas marcas y concesionarios que sean de la misma ciudad.
SELECT CONCESIONARIOS.nombre, MARCAS.nombre FROM CONCESIONARIOS, MARCAS WHERE CONCESIONARIOS.ciudad=MARCAS.ciudad;

-- 6.13 Obtenga el cifc y el dni de aquellos clientes y concesionarios que pertenezcan a la misma ciudad.
SELECT cifc, dni FROM CONCESIONARIOS, CLIENTES WHERE CONCESIONARIOS.ciudad = CLIENTES.ciudad;

-- 6.14  Obtener el codcoche de los coches que han sido adquiridos por un cliente de 'Madrid' en un concesionario de 'Madrid'.
SELECT codcoche FROM Ventas, CLIENTES, CONCESIONARIOS
WHERE CONCESIONARIOS.ciudad='Madrid' AND CLIENTES.ciudad='Madrid'
AND Ventas.dni = CLIENTES.dni AND Ventas.cifc = CONCESIONARIOS.cifc;

-- 6.15  Concatene el nombre y modelo del coche y llámelo clavecoche
SELECT nombre, modelo, CONCAT(nombre, '-', modelo) AS clavecoche FROM COCHES;


############ 7. CONSULTAS COMBINADAS ############


-- 7.1  Muestre el nombre de las marcas, clientes y concesionarioS que pertenezcan a Madrid
SELECT nombre FROM MARCAS WHERE ciudad='Madrid'
UNION
SELECT nombre FROM CLIENTES WHERE ciudad='Madrid'
UNION
SELECT nombre FROM CONCESIONARIOS WHERE ciudad='Madrid';

-- 7.2  Selecciona todos los codcoche que tengan modelo gtd o gti y que hayan sido distribuidos con una cantidad mayor que 5.
SELECT codcoche FROM COCHES WHERE modelo='gtd' or modelo='gti'
UNION
SELECT codcoche FROM Distribucion WHERE cantidad>5;

-- 7.3  Selecciona todos los apellidos distintos que no pertenezcan a Madrid y todos los modelos de los coches que sean gtd
SELECT DISTINCT apellidos FROM CLIENTES WHERE ciudad!='Madrid'
UNION
SELECT DISTINCT modelo FROM COCHES;

-- 7.4 Consulte los apellidos (no importa si se repiten o no) de clientes que no sean madrileños y los modelos de coche.
SELECT apellidos FROM CLIENTES WHERE ciudad!='Madrid'
UNION ALL
SELECT modelo FROM COCHES;

-- 7.5 Mostrar dni de clientes y de la tabla ventas repetidos
SELECT dni FROM CLIENTES
UNION ALL
SELECT dni FROM Ventas;

-- 7.6 Mostrar dni de clientes y de la tabla ventas sin repetir
SELECT dni FROM CLIENTES
UNION
SELECT dni FROM Ventas;

-- 7.7  Muestre las ciudades sin repetir de aquellos clientes que hayan comprado un coche blanco.
SELECT DISTINCT ciudad FROM CLIENTES WHERE dni in (SELECT dni FROM Ventas WHERE color='blanco');

-- 7.8  Consulte el nombre y apellidos de aquellos clientes que compraron un coche modelo GTD.
SELECT nombre, apellidos FROM CLIENTES WHERE dni in 
(SELECT dni FROM Ventas WHERE codcoche IN
(SELECT codcoche FROM COCHES WHERE modelo='GTD'));

-- 7.9  Muestre las ciudades sin repetir de aquellos clientes que no hayan comprado un coche blanco.
SELECT DISTINCT ciudad FROM CLIENTES WHERE dni NOT IN (SELECT dni FROM Ventas WHERE color='blanco');

-- 7.10  Consulte el nombre y apellidos de aquellos clientes que compraron un coche modelo GTD.
SELECT nombre, apellidos FROM CLIENTES WHERE dni in 
(SELECT dni FROM Ventas WHERE codcoche NOT IN
(SELECT codcoche FROM COCHES WHERE modelo='GTD'));

-- 7.11  Consulte el nombre y el apellidos de aquellos clientes que nunca han comprado un coche.
SELECT nombre, apellidos FROM CLIENTES WHERE dni NOT IN (SELECT dni FROM Ventas);


-- 7.12 Obtener el nombre, apellido y dni de los clientes cuyo número de dni sea mayor que el de algunos de los clientes de Luarca
 select T.nombre, T.apellidos, T.dni
 from clientes as T ,  clientes as S
 where T.dni > S.dni and S.ciudad = 'Luarca';


############ 8. TRIGGER ############

# Mediante el mecanismo de trigger llevar cuenta de los cambios en las relaciones clientes y ventas
# https://www.mysqltutorial.org/mysql-triggers/mysql-after-update-trigger/

-- 8.1 Crear la relación cambios con los atributos fecha (tipo DATE), hora (tipo TIME), usuario (varchar(100))y texto (varchar(100)). 

# DROP TABLE cambios;
create table cambios(
fecha date,
hora time,
usuario varchar(100),
texto varchar(100)
);

-- 8.2 Crear disparador que al borrar una tupla en la relación ventas añada una tupla en la relación cambios con
--     la información de la fecha, hora, usuario y valores borrados de la relación ventas.

# DROP TRIGGER Borrado_venta;
DELIMITER $$
CREATE DEFINER = CURRENT_USER TRIGGER Borrado_venta AFTER DELETE ON `ventas` FOR EACH ROW
BEGIN
insert into cambios(fecha,hora,usuario,texto) values (curdate(),curtime(),current_user(),
  concat('Borrada venta con ID: ', OLD.ID, ', CIFC: ', OLD.cifc, ', DNI: ', OLD.DNI, ', CODCOCHE: ', OLD.CODCOCHE, ', Color: ', OLD.COLOR)) ;
END$$
DELIMITER ;

-- 8.3 Insertar y luego borrar una tupla en ventas y mostrar el contenido de la relación cambios

INSERT INTO ventas (id,cifc,dni,codcoche,color) VALUES  (8888,1,1,1,'Azul');
DELETE FROM VENTAS WHERE id=8888;


############ 9. FUNCIONES ############
# https://dev.mysql.com/doc/refman/8.0/en/create-procedure.html

-- Modificar para poder crear funciones

SET GLOBAL log_bin_trust_function_creators = 1;


-- 9.1 Crear función 'NumVendidos' que devuelva los coches vendidos por un concesionario a partir de su cifc

DELIMITER //
create function NumVendidos (cifc_concesionario int) returns int
begin
    declare numero int;
    select count(*) into numero 
    from ventas
    where ventas.cifc = cifc_concesionario;
    return numero;
END //

-- 9.2 Listado de todos los concesionarios y los coches vendidos

select *, NumVendidos(cifc)
from concesionarios;













