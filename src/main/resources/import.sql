INSERT INTO rol (nombre) VALUES ('CLIENTE_ROL'), ('ADMIN_ROL'), ('GESTOR_ROL');
insert into usuario (id, apellidos, correo, cuenta_bancaria, nombre, password, username) values (2000, 'Admin', 'admin@admin.com', '30956654381546347500', 'Admin', '$2a$11$xV7Qt3OIgwXg6efZQxQfUOavr/22WFK2Sm5KFEnZ/NCGZc/2TxtRm', 'admin');
insert into usuario_rol (usr_id, rol_id) values (2000, 2);
insert into usuario (id, apellidos, correo, cuenta_bancaria, nombre, password, username) values (2001, 'Gestor', 'gestor@gestor.com', '30956654381546347501', 'Gestor', '$2a$11$Pvoym9dIepZpyvdxcMf8o.034uGYVIFrpplevbS7hWslpaZPIhFmW', 'gestor');
insert into usuario_rol (usr_id, rol_id) values (2001, 3);
