
 drop database bdBancoNlogonia;
 create database bdBancoNlogonia;
 use bdBancoNlogonia;

 
 create table Cliente(
    id int primary key AUTO_INCREMENT ,
    nombre varchar(50),
    apellido varchar(50),
    direccion varchar(50),
    numTelefono varchar(100)
    );

    create table Tarjeta(
    id int primary key AUTO_INCREMENT,
    facilitador varchar(50),
    num_tarjeta varchar(50),
    fecha_vencimiento date,
    id_cliente int,
    foreign key(id_cliente) references Cliente(id),
    tipo_tarjeta varchar(50) 
    );
   

create table Transaccion(
    id int primary key AUTO_INCREMENT,
    nombre varchar(50),
    precio decimal(10,2),
    descripcion varchar(100),
    id_tarjeta int,
    fecha datetime,
    foreign key (id_tarjeta) references Tarjeta(id) 
);
    
INSERT INTO Cliente (nombre, apellido, direccion, numTelefono) VALUES
('Juan', 'Pérez', 'Calle Principal 123', '123-456-7890'),
('María', 'González', 'Avenida Central 456', '234-567-8901'),
('Pedro', 'Martínez', 'Plaza Mayor 789', '345-678-9012'),
('Ana', 'López', 'Boulevard Secundario 234', '456-789-0123'),
('Carlos', 'Rodríguez', 'Carrera Principal 567', '567-890-1234');

INSERT INTO Tarjeta (facilitador, num_tarjeta, fecha_vencimiento, id_cliente, tipo_tarjeta) VALUES
('Visa', '1234567812345678', '2024-12-31', 1, 'Crédito'),
('Mastercard', '9876543210987654', '2023-09-30', 2, 'Débito'),
('American Express', '1111222233334444', '2025-06-30', 3, 'Crédito'),
('Visa', '5555666677778888', '2024-08-31', 4, 'Débito'),
('Mastercard', '9999888877776666', '2023-11-30', 5, 'Crédito');

INSERT INTO Transaccion (nombre, precio, descripcion, id_tarjeta, fecha) VALUES
('Compra en línea', 120.50, 'Artículos de electrónica', 1, '2024-07-08 14:30:00'),
('Pago de servicios', 50.25, 'Factura de luz', 2, '2024-07-07 10:15:00'),
('Compra en tienda', 75.00, 'Ropa y accesorios', 3, '2024-07-06 16:45:00'),
('Retiro de efectivo', 200.00, 'Cajero automático', 4, '2024-07-05 12:00:00'),
('Compra en línea', 300.75, 'Libros y material educativo', 5, '2024-07-04 18:00:00');


INSERT INTO Transaccion (nombre, precio, descripcion, id_tarjeta, fecha) VALUES
('Compra de alimentos', 75.50, 'Compra mensual de alimentos en supermercado', 1, '2024-05-05 10:30:00'),
('Gasolina', 50.25, 'Llenado de tanque de gasolina', 2, '2024-05-12 15:45:00'),
('Compra en línea', 120.75, 'Compra de ropa en tienda en línea', 3, '2024-05-18 09:00:00'),
('Restaurante', 85.20, 'Cena en restaurante con amigos', 4, '2024-05-25 20:00:00'),
('Servicios públicos', 110.00, 'Pago mensual de servicios públicos', 5, '2024-06-01 12:00:00');

INSERT INTO Transaccion (nombre, precio, descripcion, id_tarjeta, fecha) VALUES
('Compra de alimentos', 75.50, 'Compra mensual de alimentos en supermercado', 
  (SELECT id FROM Tarjeta WHERE num_tarjeta = '1234567812345678'), '2024-05-05 10:30:00'),

('Gasolina', 50.25, 'Llenado de tanque de gasolina', 
  (SELECT id FROM Tarjeta WHERE num_tarjeta = '1234567812345678'), '2024-05-12 15:45:00'),

('Compra en línea', 120.75, 'Compra de ropa en tienda en línea', 
  (SELECT id FROM Tarjeta WHERE num_tarjeta = '1234567812345678'), '2024-05-18 09:00:00'),

('Restaurante', 85.20, 'Cena en restaurante con amigos', 
  (SELECT id FROM Tarjeta WHERE num_tarjeta = '1234567812345678'), '2024-05-25 20:00:00'),

('Servicios públicos', 110.00, 'Pago mensual de servicios públicos', 
  (SELECT id FROM Tarjeta WHERE num_tarjeta = '1234567812345678'), '2024-06-01 12:00:00');
show tables