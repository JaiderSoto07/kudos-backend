-- Datos de demostración de UCO Kudos (opcional). Ejecutar después de datos-iniciales-kudos.sql.
-- Crea una estructura académica y usuarios ACTIVOS de cada rol para probar la plataforma y para la sustentación.
-- Todos los usuarios de demostración tienen la contraseña Kudos2026*.

use Kudos;
go

set quoted_identifier on;
set ansi_nulls on;
go

declare @rector uniqueidentifier = newid(), @academico uniqueidentifier = newid(), @coordFacultad uniqueidentifier = newid(),
        @coordPrograma uniqueidentifier = newid(), @docente uniqueidentifier = newid(), @estudiante1 uniqueidentifier = newid(),
        @estudiante2 uniqueidentifier = newid(), @pendiente uniqueidentifier = newid();
declare @ingenieria uniqueidentifier = newid(), @economicas uniqueidentifier = newid(), @salud uniqueidentifier = newid(), @derecho uniqueidentifier = newid();
declare @sistemas uniqueidentifier = newid(), @industrial uniqueidentifier = newid(), @electronica uniqueidentifier = newid(),
        @administracion uniqueidentifier = newid(), @contaduria uniqueidentifier = newid(), @enfermeria uniqueidentifier = newid();
declare @k1 uniqueidentifier = newid(), @k2 uniqueidentifier = newid(), @k3 uniqueidentifier = newid();
declare @r1 uniqueidentifier = newid(), @r2 uniqueidentifier = newid(), @r3 uniqueidentifier = newid();
declare @excelencia uniqueidentifier = (select id_categoria from categoria where nombre = N'Excelencia académica');
declare @valores uniqueidentifier = (select id_categoria from categoria where nombre = N'Valores institucionales');
declare @liderazgo uniqueidentifier = (select id_categoria from categoria where nombre = N'Liderazgo');

insert into usuario (id_usuario, nombres, apellidos, correo_institucional, correo_verificado, numero_celular, celular_verificado, contrasena, aprobada, estado_cuenta) values
(@rector, N'María Fernanda', N'Gómez Arango', 'mgomez@uco.edu.co', 1, '3104567890', 1, '$2a$10$UcoKudosPlataformaSaledWBSYILlSOw.OTRuopx3TD3bGCYZqWa', 1, 'ACTIVA'),
(@academico, N'Carlos Andrés', N'Restrepo Mejía', 'crestrepo@uco.edu.co', 1, '3127788990', 1, '$2a$10$UcoKudosPlataformaSaledWBSYILlSOw.OTRuopx3TD3bGCYZqWa', 1, 'ACTIVA'),
(@coordFacultad, N'Luisa Fernanda', N'Ospina Duque', 'lospina@uco.edu.co', 1, '3154433221', 1, '$2a$10$UcoKudosPlataformaSaledWBSYILlSOw.OTRuopx3TD3bGCYZqWa', 1, 'ACTIVA'),
(@coordPrograma, N'Juan Pablo', N'Zuluaga Henao', 'jzuluaga@uco.edu.co', 1, '3186655443', 1, '$2a$10$UcoKudosPlataformaSaledWBSYILlSOw.OTRuopx3TD3bGCYZqWa', 1, 'ACTIVA'),
(@docente, N'Andrea Marcela', N'Giraldo Ríos', 'agiraldo@uco.edu.co', 1, '3017766554', 1, '$2a$10$UcoKudosPlataformaSaledWBSYILlSOw.OTRuopx3TD3bGCYZqWa', 1, 'ACTIVA'),
(@estudiante1, N'Santiago', N'Henao Cardona', 'shenao@uco.edu.co', 1, '3209988776', 1, '$2a$10$UcoKudosPlataformaSaledWBSYILlSOw.OTRuopx3TD3bGCYZqWa', 1, 'ACTIVA'),
(@estudiante2, N'Valentina', N'Cardona López', 'vcardona@uco.edu.co', 1, '3051239876', 1, '$2a$10$UcoKudosPlataformaSaledWBSYILlSOw.OTRuopx3TD3bGCYZqWa', 1, 'ACTIVA'),
(@pendiente, N'Mateo', N'Arango Valencia', 'marango@uco.edu.co', 0, '3112345678', 0, '$2a$10$UcoKudosPlataformaSaledWBSYILlSOw.OTRuopx3TD3bGCYZqWa', 0, 'PENDIENTE_APROBACION');

insert into facultad (id_facultad, nombre, estado) values
(@ingenieria, N'Ingeniería', 'ACTIVA'),
(@economicas, N'Ciencias Económicas, Administrativas y Contables', 'ACTIVA'),
(@salud, N'Ciencias de la Salud', 'ACTIVA'),
(@derecho, N'Derecho', 'EN_CONFORMACION');

insert into programa_academico (id_programa_academico, nombre, id_facultad) values
(@sistemas, N'Ingeniería de Sistemas', @ingenieria),
(@industrial, N'Ingeniería Industrial', @ingenieria),
(@electronica, N'Ingeniería Electrónica', @ingenieria),
(@administracion, N'Administración de Empresas', @economicas),
(@contaduria, N'Contaduría Pública', @economicas),
(@enfermeria, N'Enfermería', @salud);

insert into asignacion_rol (id_usuario, rol, id_facultad, id_programa) values
(@rector, 'RECTOR', null, null),
(@academico, 'COORDINADOR_ACADEMICO', null, null),
(@coordFacultad, 'COORDINADOR_FACULTAD', @ingenieria, null),
(@coordPrograma, 'COORDINADOR_PROGRAMA', null, @sistemas),
(@docente, 'DOCENTE', null, null),
(@estudiante1, 'ESTUDIANTE', null, null),
(@estudiante2, 'ESTUDIANTE', null, null);

insert into vinculacion_programa (id_usuario, id_programa, tipo_vinculo) values
(@docente, @sistemas, 'DOCENTE'),
(@docente, @industrial, 'DOCENTE'),
(@estudiante1, @sistemas, 'ESTUDIANTE'),
(@estudiante2, @sistemas, 'ESTUDIANTE');

insert into solicitud_registro (id_solicitante, rol_solicitado, id_programa) values (@pendiente, 'ESTUDIANTE', @sistemas);

insert into kudo (id_kudo, id_autor, id_destinatario, id_categoria, contenido, estado, cantidad_visualizaciones, promedio_calificacion, cantidad_calificaciones) values
(@k1, @docente, @estudiante1, @excelencia, N'Santiago presentó el mejor proyecto final del curso de Diseño Orientado a Objetos: un modelo de dominio claro, bien justificado y una implementación limpia. ¡Felicitaciones!', 'VISIBLE', 12, 4.67, 3),
(@k2, @estudiante1, @docente, @valores, N'Gracias profe Andrea por acompañarnos los sábados en las tutorías y por su paciencia con todo el grupo.', 'VISIBLE', 5, 5.00, 1),
(@k3, @coordPrograma, @coordFacultad, @liderazgo, N'Luisa organizó la feria de proyectos de la Facultad y logró que todos los programas participaran.', 'VISIBLE', 3, 0, 0);

insert into replica (id_replica, id_autor, id_kudo, id_replica_padre, contenido, estado) values
(@r1, @coordFacultad, @k1, null, N'¡Felicitaciones Santiago! Orgullo de la Facultad.', 'VISIBLE'),
(@r2, @estudiante1, @k1, @r1, N'Muchas gracias, profe Luisa.', 'VISIBLE'),
(@r3, @docente, @k1, @r2, N'¡Te lo ganaste!', 'VISIBLE');

insert into calificacion (id_autor, id_kudo, id_replica, estrellas) values
(@coordFacultad, @k1, null, 5),
(@coordPrograma, @k1, null, 4),
(@academico, @k1, null, 5),
(@coordPrograma, @k2, null, 5),
(@estudiante1, null, @r1, 5);

update replica set promedio_calificacion = 5.00, cantidad_calificaciones = 1 where id_replica = @r1;
go
