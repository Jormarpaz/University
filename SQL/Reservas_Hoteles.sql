
# Fichero para la solución del Caso Práctico de "Bases de datos". 

# Modifíquelo para incluir el codigo necesario para llevar a cabo los pasos necesarios para
# resolver los distintos apartados. Debe incluir comentarios con explicaciones del código
# propuesto.


-- Si existe, borrar la base de datos Cadena_hotelera:

DROP DATABASE IF EXISTS Cadena_hotelera;

-- Crear la base de datos Cadena_hotelera:

CREATE DATABASE IF NOT EXISTS Cadena_hotelera;

-- Usar la base de datos Cadena_hotelera: 

USE Cadena_hotelera;


#################################################################################################
#    Crear el esquema de la base de datos con las tablas que se extraen del diagrama E-R 
#    presentado y del propio enunciado del problema.
#################################################################################################
# Apartado 1: Crear la tabla Hoteles
#################################################################################################
create table Hoteles
		(
		CodHotel VARCHAR(10) primary key not null unique, #Un identificador para cada hotel
        Nombre VARCHAR(30) not null, #Nombre del hotel
        Direccion VARCHAR(50), #Direccion fisica del hotel
        Ciudad VARCHAR(30) not null, #Ciudad donde esta el hotel
        FecInaug DATE, #Fecha Inauguracion
        Categoria DECIMAL(1,0) not null check (Categoria >= 1 and Categoria <= 5), #Categoria del Hotel, 1 a 5
        NumHabitac DECIMAL(3,0) not null check (NumHabitac > 0) #Numero total de habitaciones disponibles
        );


#################################################################################################
# Apartado 2: Crear la tabla Clientes
#################################################################################################
create table Clientes
		( 
        NIF VARCHAR(9) primary key not null unique, #Número de Identificación Fiscal del cliente, que actúa como un identificador único
        Nombre VARCHAR(30) not null, #Nombre del cliente
        Apellido1 VARCHAR(30) not null, #Primer apellido del cliente
        Apellido2 VARCHAR(30), #Segundo apellido del cliente, que puede ser opcional.
        FecNacim DATE not null, # Fecha de nacimiento del cliente.
        Direccion VARCHAR(50), #Dirección del domicilio del cliente
        Ciudad VARCHAR(30) not null # Ciudad del domicilio del cliente
        );


#################################################################################################
# Apartado 3: Crear la tabla Reservas
#################################################################################################
create table Reservas
		(
        CodReserva VARCHAR(10) primary key not null unique, #Código único identificativo para cada reserva.
		CodHotel VARCHAR(10) not null, #Código del hotel donde se realiza la reserva, que enlaza con la tabla Hoteles.
        foreign key (CodHotel) references Hoteles(CodHotel) on delete cascade,
 		NIF VARCHAR(9) not null, #Número de Identificación Fiscal del cliente que realiza la reserva, enlazando con la tabla Clientes.
        foreign key (NIF) references Clientes(NIF) on delete cascade,
 		FechaEntrada DATE ,
        check (FechaEntrada >= FechaReserva), #La fecha en que el cliente planea comenzar su estancia.
        FechaReserva DATE not null, #La fecha en que se realizó la reserva.
 		NumNoches DECIMAL(3,0) not null check (NumNoches > 0), #Número de noches que el cliente se quedará.
 		NumHabitac DECIMAL(3,0) not null check (NumHabitac > 0), #Número de habitaciones reservadas.
 		NumAdultos DECIMAL(4,0) not null default 2 check(NumAdultos > 0), #Número de adultos incluidos en la reserva.
 		NumNinyos DECIMAL(4,0) not null default 0 check(NumNinyos >= 0), #Número de niños incluidos en la reserva.
 		PrecioNochHab DECIMAL(6,2) not null check (PrecioNochHab > 0) #Precio por noche y por habitación
        );


################################################################################################
# Poblar las tres tablas creadas con los datos proporcionados en "Cadena Hotelera - Datos.sql" 
# y también listados a continuación.
#################################################################################################

INSERT INTO Hoteles (CodHotel, Nombre, Direccion, Ciudad, FecInaug, Categoria, NumHabitac) VALUES
('H001', 'Hotel Sol', '123 Calle Sol', 'Madrid', '2000-05-20', 4, 100),
('H002', 'Hotel Luna', '456 Calle Luna', 'Barcelona', '2010-06-15', 5, 150),
('H003', 'Hotel Estrella', '789 Calle Estrella', 'Valencia', '2015-03-30', 3, 50),
('H004', 'Hotel Cometa', '321 Calle Cometa', 'Sevilla', '2020-01-22', 2, 30),
('H005', 'Hotel Galaxia', '654 Calle Galaxia', 'Madrid', '2005-12-05', 4, 80);

INSERT INTO Clientes (NIF, Nombre, Apellido1, Apellido2, FecNacim, Direccion, Ciudad) VALUES
('12345678A', 'Ana', 'García', 'López', '1985-04-15', '111 Avda Prado', 'Madrid'),
('87654321B', 'Luis', 'Martín', 'Santos', '1972-08-25', '222 Avda Olmos', 'Barcelona'),
('11223344C', 'Carmen', 'Ruiz', 'Diaz', '1990-01-10', '333 Avda Mar', 'Valencia'),
('44332211D', 'Jorge', 'Fernández', 'Moreno', '1965-07-20', '444 Avda Sierra', 'Sevilla'),
('55667788E', 'Laura', 'Torres', 'Nieto', '1979-12-30', '555 Avda Sol', 'Madrid'),
('23456789F', 'Sofía', 'Pérez', 'Gómez', '1993-03-22', '666 Avda Júpiter', 'Madrid'),
('34567890G', 'Carlos', 'Jiménez', 'Fernández', '1980-11-11', '777 Avda Saturno', 'Barcelona'),
('45678901H', 'Marta', 'López', 'Martínez', '1976-02-05', '888 Avda Neptuno', 'Valencia'),
('56789012I', 'Antonio', 'García', 'Romero', '1964-09-09', '999 Avda Urano', 'Sevilla'),
('67890123J', 'Beatriz', 'Vega', 'Prieto', '1989-12-16', '1010 Avda Plutón', 'Madrid');

INSERT INTO Reservas (CodReserva, CodHotel, NIF, FechaEntrada, FechaReserva, NumNoches, NumHabitac, NumAdultos, NumNinyos, PrecioNochHab) VALUES
('R001', 'H001', '12345678A', '2023-04-01', '2023-03-20', 3, 2, 2, 1, 100.00),
('R002', 'H002', '87654321B', '2023-04-15', '2023-03-25', 5, 1, 2, 2, 150.00),
('R003', 'H003', '11223344C', '2023-05-10', '2023-04-20', 2, 3, 1, 0, 90.00),
('R004', 'H004', '44332211D', '2022-12-25', '2022-11-30', 7, 1, 2, 3, 60.00),
('R005', 'H001', '55667788E', '2023-04-20', '2023-03-15', 4, 1, 1, 0, 120.00),
('R006', 'H005', '12345678A', '2023-04-25', '2023-04-10', 3, 1, 2, 2, 110.00),
('R007', 'H001', '11223344C', '2023-03-15', '2023-02-20', 5, 2, 1, 1, 110.00),
('R008', 'H002', '44332211D', '2023-06-05', '2023-05-20', 3, 1, 2, 0, 160.00),
('R009', 'H003', '12345678A', '2022-12-10', '2022-11-25', 4, 3, 2, 2, 85.00),
('R010', 'H004', '87654321B', '2022-11-15', '2022-10-30', 2, 1, 2, 1, 70.00),
('R011', 'H005', '55667788E', '2023-07-01', '2023-06-15', 6, 2, 2, 3, 95.00),
('R012', 'H001', '11223344C', '2023-08-20', '2023-07-25', 7, 1, 1, 0, 100.00),
('R013', 'H002', '44332211D', '2023-09-05', '2023-08-01', 10, 2, 2, 2, 180.00),
('R014', 'H003', '12345678A', '2022-07-25', '2022-07-10', 5, 2, 2, 0, 75.00),
('R015', 'H004', '55667788E', '2023-12-25', '2023-12-01', 14, 1, 2, 2, 65.00),
('R016', 'H005', '44332211D', '2023-10-10', '2023-09-20', 3, 1, 1, 1, 105.00),
('R017', 'H002', '87654321B', '2023-01-15', '2022-12-20', 8, 3, 3, 0, 140.00),
('R018', 'H001', '11223344C', '2023-11-01', '2023-10-10', 4, 1, 1, 0, 130.00),
('R019', 'H003', '12345678A', '2023-02-10', '2023-01-15', 3, 1, 2, 1, 90.00),
('R020', 'H004', '44332211D', '2023-05-20', '2023-04-25', 6, 2, 2, 3, 80.00),
('R021', 'H001', '23456789F', '2024-01-05', '2023-12-20', 4, 1, 2, 1, 100.00),
('R022', 'H002', '34567890G', '2024-02-15', '2024-01-30', 3, 2, 2, 0, 160.00),
('R023', 'H003', '45678901H', '2024-03-10', '2024-02-20', 7, 1, 1, 2, 85.00),
('R024', 'H004', '56789012I', '2024-04-20', '2024-03-25', 5, 1, 2, 3, 70.00),
('R025', 'H005', '67890123J', '2024-05-01', '2024-04-15', 6, 2, 2, 2, 95.00),
('R026', 'H001', '23456789F', '2024-06-25', '2024-06-10', 3, 1, 1, 0, 120.00),
('R027', 'H002', '34567890G', '2024-07-15', '2024-06-30', 8, 1, 2, 1, 150.00),
('R028', 'H003', '45678901H', '2024-08-05', '2024-07-20', 2, 3, 2, 0, 90.00),
('R029', 'H004', '56789012I', '2024-09-10', '2024-08-25', 10, 2, 2, 2, 80.00),
('R030', 'H005', '67890123J', '2024-10-20', '2024-09-30', 14, 1, 1, 1, 105.00),
('R031', 'H002', '34567890G', '2024-11-15', '2024-10-31', 5, 2, 3, 0, 140.00),
('R032', 'H001', '23456789F', '2024-12-10', '2024-11-25', 7, 1, 2, 1, 130.00),
('R033', 'H001', '23456789F', '2024-05-10', '2024-04-14', 5, 1, 2, 1, 110.00),
('R034', 'H002', '34567890G', '2024-06-15', '2024-04-13', 3, 2, 2, 0, 160.00),
('R035', 'H003', '45678901H', '2024-07-20', '2024-04-12', 7, 1, 1, 2, 85.00),
('R036', 'H004', '56789012I', '2024-08-05', '2024-04-11', 5, 1, 2, 3, 70.00),
('R037', 'H005', '67890123J', '2024-05-25', '2024-04-10', 6, 2, 2, 2, 95.00),
('R038', 'H001', '23456789F', '2024-06-30', '2024-04-09', 3, 1, 1, 0, 120.00),
('R039', 'H002', '34567890G', '2024-07-15', '2024-04-08', 8, 1, 2, 1, 150.00),
('R040', 'H003', '45678901H', '2024-08-25', '2024-04-07', 2, 3, 2, 0, 90.00),
('R041', 'H004', '56789012I', '2024-05-05', '2024-04-06', 10, 2, 2, 2, 80.00),
('R042', 'H005', '67890123J', '2024-06-20', '2024-04-05', 14, 1, 1, 1, 105.00),
('R043', 'H002', '34567890G', '2024-07-30', '2024-04-04', 5, 2, 3, 0, 140.00),
('R044', 'H001', '23456789F', '2024-08-10', '2024-04-03', 7, 1, 2, 1, 130.00);


################################################################################################# 
# Apartado 4: CONSULTA Listar Hoteles de Alta Categoría

# Esta consulta selecciona todos los hoteles que tienen una categoría de 4 o 5, mostrando el
# CodHotel, nombre, ciudad y categoría. 
#################################################################################################

select CodHotel, Nombre, Ciudad, Categoria
from Hoteles
where Hoteles.Categoria > 3 ;


################################################################################################# 
# Apartado 5: CONSULTA Contar Clientes por Ciudad

# Esta consulta cuenta cuántos clientes hay de cada ciudad, ordenados de forma descendente según
# ese número, lo cual es útil para entender la distribución geográfica de la clientela.
#################################################################################################

select Ciudad, Count(*) as NumClientes
from Clientes
group by Ciudad
order by NumClientes DESC;




################################################################################################# 
# Apartado 6: CONSULTA Detalle de las ocupaciones de los próximos 30 días

# Esta consulta recupera detalles de las reservas realizadas para entrar en los próximos 30 días, 
# incluyendo el código de la reserva, el código del hotel, la fecha de reserva, la fecha de 
# entrada, y el número de noches. El resultado ordenado por fecha de entrada ascendente.
#################################################################################################

select CodReserva, CodHotel, FechaReserva, FechaEntrada, NumNoches
from Reservas
where FechaEntrada between CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY)
order by FechaEntrada ASC;

################################################################################################# 
# Apartado 7: CONSULTA Ingresos futuros por hotel

# Calcular los ingresos futuros esperados de cada hotel basados en las reservas que aún no han 
# comenzado, utilizando el número de habitaciones reservadas, número de noches y el precio por noche.
#################################################################################################

select Reservas.CodHotel, Hoteles.Nombre, SUM(Reservas.NumHabitac * Reservas.NumNoches * Reservas.PrecioNochHab) AS IngresosEsperados
from Reservas
inner join Hoteles ON Reservas.CodHotel = Hoteles.CodHotel
where Reservas.FechaEntrada > CURDATE()
group by Reservas.CodHotel, Hoteles.Nombre;

################################################################################################# 
# Apartado 8: CONSULTA Clientes frecuentes por ciudad (3 o más reservas)

# Identificar los clientes que han realizado tres o más reservas en hoteles de una misma ciudad.
# Lista el nombre y apellido1 del cliente, la ciudad de los hoteles y el número total de reservas
# en hoteles de esa ciudad.
#################################################################################################

select Clientes.Nombre,Clientes.Apellido1,Hoteles.Ciudad, Count(*) AS TotalReservas
from Reservas
inner join Hoteles ON Reservas.CodHotel = Hoteles.CodHotel
inner join Clientes ON Reservas.NIF = Clientes.NIF
group by Clientes.Nombre, Clientes.Apellido1, Hoteles.Ciudad
having COUNT(*) >= 3
order by TotalReservas DESC;


################################################################################################# 
# Apartado 9: CONSULTA Reservas para entrar en un hotel en el mismo mes de su inauguración

# Listar todas las reservas cuya entrada coincide con el mes de aniversario de la inauguración 
# del hotel, mostrando el nombre del hotel, fecha de inauguraración, nombre del cliente 
# y la fecha de entrada. El mes de la fecha de entrada tiene que coincidir con el mes de la
# fecha de inauguración.
#################################################################################################

select Hoteles.Nombre, Hoteles.FecInaug, Clientes.Nombre, Reservas.FechaEntrada
from Reservas
inner join Hoteles ON Reservas.CodHotel = Hoteles.CodHotel
inner join Clientes ON Reservas.NIF = Clientes.NIF
where MONTH(Reservas.FechaEntrada) = Month(Hoteles.FecInaug)
order by Reservas.FechaEntrada;

################################################################################################# 
# Apartado 10: CONSULTA Listado hoteles con meses de alta ocupación en 2023

#  Listar los hoteles que tuvieron una ocupación de más del 2% en cualquier mes de 2023 
#  mostrando nombre del hotel, año, mes y el porcentaje de ocupación. El porcentaje
#  de ocupación en un mes se calcula como el número de habitaciones reservadas dividido por el
#  numero de habitaciones del hotel * 100. 
# Se pide un dato tan pequeño de ocupación porque hay dadas de altas pocas reservas
#################################################################################################

select Hoteles.Nombre AS Nombre_Hotel, YEAR(Reservas.FechaEntrada) AS Año, MONTH(Reservas.FechaEntrada) AS Mes, (COUNT(Reservas.CodReserva) / (SELECT COUNT(*) FROM Hoteles,Reservas WHERE Hoteles.CodHotel = Reservas.CodHotel) * 100) AS Porcentaje_Ocupacion
from Reservas
inner join Hoteles ON Reservas.CodHotel = Hoteles.CodHotel
where YEAR(Reservas.FechaEntrada) = 2023
group by Hoteles.Nombre, YEAR(Reservas.FechaEntrada), MONTH(Reservas.FechaEntrada), Hoteles.NumHabitac
having Porcentaje_Ocupacion > 2;

################################################################################################# 
# Apartado 11: CONSULTA Clientes sin reservas recientes

#  Encontrar clientes que no hayan hecho ninguna reserva en el último año. Mostrar nombre y apellido1
#################################################################################################

select Nombre,Apellido1
from Clientes
where NIF NOT IN 
(
	select Distinct NIF
    from Reservas
    where FechaEntrada Between DATE_SUB(CURDATE(),INTERVAL 1 YEAR) AND CURDATE()
) ;

################################################################################################# 
# Apartado 12: CONSULTA Listado de hoteles y coste medio de habitación

#  Listado de todos los hoteles con el nombre, categoria y precio medio de la habitación. 
#  Ordenar por el precio medio descendente.
#################################################################################################

select Nombre, Categoria, AVG(Reservas.PrecioNochHab) AS PrecioMedio
from Hoteles
inner join Reservas ON Hoteles.CodHotel = Reservas.CodHotel
group by Hoteles.Nombre, Hoteles.Categoria
order by PrecioMedio DESC;

################################################################################################# 
# Apartado 13: VISTA VistaDetalleReservas con toda la información clave de las reservas

/*
Tablas Involucradas:
   Reservas: Contiene todos los detalles de las reservas hechas por los clientes.
   Hoteles: Incluye información sobre los hoteles donde se realizan las reservas.
   Clientes: Almacena datos sobre los clientes que han hecho las reservas.

Campos de la vista:

   CodReserva: El código identificador de la reserva.
   HotelNombre: El nombre del hotel asociado a la reserva.
   HotelCiudad: La ciudad donde está ubicado el hotel.
   ClienteNombre: El nombre del cliente que realizó la reserva.
   ClienteApellido1: El primer apellido del cliente.
   FechaEntrada: La fecha en la que el cliente tiene previsto iniciar su estancia.
   NumNoches: Número de noches que el cliente se hospedará en el hotel.
   NumHabitac: Número de habitaciones reservadas.
   NumAdultos: Número de adultos incluidos en la reserva.
   NumNinyos: Número de niños incluidos en la reserva.
   PrecioNochHab: Precio por noche y por habitación.
*/

#################################################################################################

create view VistaDetalleReservas AS 
select Reservas.CodReserva, Hoteles.Nombre AS Hoteles, Hoteles.Ciudad AS Ciudades, Clientes.Nombre AS Clientes, CLientes.Apellido1 AS Apellidos, Reservas.FechaEntrada, Reservas.NumNoches,
Reservas.NumHabitac, Reservas.NumAdultos, Reservas.NumNinyos, Reservas.PrecioNochHab
from Reservas
inner join Hoteles ON Reservas.CodHotel = Hoteles.CodHotel
inner join Clientes ON Reservas.NIF = Clientes.NIF;

################################################################################################# 
# Apartado 14: CONSULTA Reservas realizadas en los últimos 30 días

#  Utilizar la vista VistaDetalleReservas para realizar una consulta que encuentre todas las 
#  reservas con entrada en los próximos 30 días. Mostrar todos los campos de la vista.
#################################################################################################

select *
from VistaDetalleReservas
where FechaEntrada between CURDATE() AND DATE_ADD(CURDATE(),INTERVAL 30 DAY);

################################################################################################# 
# Apartado 15: FUNCION CostoReserva() que calcule el costo de una reserva

/*
Crear la función CostoReserva() que tome como entrada el código de una reserva (CodReserva) y 
devuelva el costo de esa reserva específica. La función calculará el costo total de la reserva 
multiplicando el número de noches, el número de habitaciones y el precio por noche por habitación, 
todo a partir del código de reserva proporcionado.
*/
#################################################################################################

SET GLOBAL
log_bin_trust_function_creators = 1;

delimiter //
create function CostoReserva (f_CodReserva VARCHAR(10)) returns DECIMAL(10,2)
begin 
	declare f_Costo DECIMAL(10,2);
	select (NumNoches * NumHabitac * PrecioNochHab) INTO f_Costo 
    from VistaDetalleReservas 
    where CodReserva = f_CodReserva;
	return f_Costo;
end; //
delimiter ;


################################################################################################# 
# Apartado 16: CONSULTA Costo medio de las reservas de cada cliente

/*
Empleando en la consulta la función CostoReserva() creada en el apartado anterior, listar 
el nombre de todos los clientes, el numero de reservas  que ha hecho y el costo medio de 
las reservas que ha hecho y el costo total de las reservas hechas.
*/
#################################################################################################

select Clientes, Count(CodReserva) AS TotalReservas, AVG(CostoReserva(CodReserva)) AS CostoMedio, SUM(CostoReserva(CodReserva)) AS CostoTotal
from VistaDetalleReservas
group by Clientes;










