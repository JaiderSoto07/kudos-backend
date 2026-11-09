-- Datos iniciales de UCO Kudos: parámetros globales, primer Administrador (D-18) y categorías base.
-- La contraseña del Administrador inicial es Kudos2026* (hash BCrypt). Debe cambiarse después del primer ingreso.

use Kudos;
go

set quoted_identifier on;
set ansi_nulls on;
go

insert into parametro_global (clave, valor, tipo_dato, descripcion) values
('DOMINIO_CORREO', 'uco.edu.co', 'TEXTO', N'Dominio obligatorio del correo institucional'),
('VIGENCIA_CODIGO_MINUTOS', '15', 'ENTERO', N'Minutos de vigencia de un código de verificación'),
('MAX_INTENTOS_VERIFICACION', '5', 'ENTERO', N'Intentos fallidos permitidos por código de verificación');

declare @admin uniqueidentifier = newid();

insert into usuario (id_usuario, nombres, apellidos, correo_institucional, correo_verificado, numero_celular, celular_verificado,
                     contrasena, aprobada, estado_cuenta) values
(@admin, N'Administrador', N'UCO Kudos', 'admin.kudos@uco.edu.co', 1, '3000000000', 1,
 '$2a$10$UcoKudosPlataformaSaledWBSYILlSOw.OTRuopx3TD3bGCYZqWa', 1, 'ACTIVA');

insert into asignacion_rol (id_usuario, rol) values (@admin, 'ADMINISTRADOR');

insert into categoria (nombre, descripcion) values
(N'Excelencia académica', N'Reconoce logros académicos sobresalientes en clase, investigación o evaluaciones.'),
(N'Trabajo en equipo', N'Reconoce la colaboración y el apoyo a compañeros para alcanzar metas comunes.'),
(N'Servicio a la comunidad', N'Reconoce aportes a la comunidad universitaria o a la región.'),
(N'Liderazgo', N'Reconoce a quien inspira y guía a otros con el ejemplo.'),
(N'Valores institucionales', N'Reconoce comportamientos alineados con los valores católicos y el proyecto educativo de la UCO.');
go
