-- Crea las tablas de UCO Kudos en SQL Server.
-- Ejecutar después de crear-usuario-kudos.sql. Las restricciones de negocio críticas se refuerzan aquí
-- (unicidad, índices únicos filtrados, chequeos y llaves foráneas), además de validarse en el dominio (RNF-040).
-- Los índices únicos filtrados exigen QUOTED_IDENTIFIER y ANSI_NULLS activos (sqlcmd los trae apagados por defecto).

use Kudos;
go

set quoted_identifier on;
set ansi_nulls on;
go

-- Persona de la comunidad universitaria que tiene una cuenta en la plataforma. Su rol institucional vigente se conoce por su AsignacionRol vigente.
create table usuario (
id_usuario uniqueidentifier primary key not null default newid(),
nombres nvarchar(60) not null,
apellidos nvarchar(60) not null,
correo_institucional varchar(100) not null,
correo_verificado bit not null default 0,
numero_celular varchar(10) not null,
celular_verificado bit not null default 0,
contrasena varchar(60) not null,
aprobada bit not null default 0,
estado_cuenta varchar(22) not null default 'PENDIENTE_APROBACION',
fecha_registro datetime2(0) not null default sysdatetime(),

constraint uk_usuario_correo_institucional
  unique (correo_institucional),
constraint uk_usuario_numero_celular
  unique (numero_celular),
constraint ck_usuario_correo_institucional
  check (correo_institucional like '_%@_%._%' and correo_institucional = lower(correo_institucional)),
constraint ck_usuario_numero_celular
  check (len(numero_celular) = 10 and numero_celular like '3%' and numero_celular not like '%[^0-9]%'),
constraint ck_usuario_estado_cuenta
  check (estado_cuenta in ('PENDIENTE_APROBACION', 'PENDIENTE_VERIFICACION', 'ACTIVA', 'SUSPENDIDA', 'DESACTIVADA')),
constraint ck_usuario_cuenta_activa
  check (estado_cuenta <> 'ACTIVA' or (aprobada = 1 and correo_verificado = 1 and celular_verificado = 1))
);
go

-- Valor de configuración de la plataforma que administran los Administradores (RN-130, D-11).
create table parametro_global (
id_parametro_global uniqueidentifier primary key not null default newid(),
clave varchar(50) not null,
valor nvarchar(250) not null,
tipo_dato varchar(6) not null,
descripcion nvarchar(250) not null,
fecha_modificacion datetime2(0) not null default sysdatetime(),

constraint uk_parametro_global_clave
  unique (clave),
constraint ck_parametro_global_tipo_dato
  check (tipo_dato in ('TEXTO', 'ENTERO')),
constraint ck_parametro_global_valor_entero
  check (tipo_dato <> 'ENTERO' or valor not like '%[^0-9]%')
);
go

-- Unidad académica de la Universidad que agrupa Programas Académicos y es acompañada por un Coordinador de Facultad.
create table facultad (
id_facultad uniqueidentifier primary key not null default newid(),
nombre nvarchar(80) not null,
estado varchar(15) not null default 'EN_CONFORMACION',
fecha_creacion datetime2(0) not null default sysdatetime(),

constraint uk_facultad_nombre
  unique (nombre),
constraint ck_facultad_nombre
  check (len(nombre) >= 3),
constraint ck_facultad_estado
  check (estado in ('EN_CONFORMACION', 'ACTIVA', 'INACTIVA'))
);
go

-- Programa de formación que pertenece a exactamente una Facultad y es acompañado por un Coordinador de Programa.
create table programa_academico (
id_programa_academico uniqueidentifier primary key not null default newid(),
nombre nvarchar(100) not null,
id_facultad uniqueidentifier not null,
activo bit not null default 1,
fecha_creacion datetime2(0) not null default sysdatetime(),

constraint fk_programa_academico_id_facultad
  foreign key (id_facultad) references facultad(id_facultad),
constraint uk_programa_academico_nombre
  unique (nombre),
constraint ck_programa_academico_nombre
  check (len(nombre) >= 3)
);
create index ix_programa_academico_facultad on programa_academico (id_facultad);
go

-- Clasificación institucional de los reconocimientos (RN-132). Puede estar activa o inactiva; solo las activas se usan para Kudos nuevos.
create table categoria (
id_categoria uniqueidentifier primary key not null default newid(),
nombre nvarchar(50) not null,
descripcion nvarchar(250) not null,
activa bit not null default 1,
fecha_creacion datetime2(0) not null default sysdatetime(),

constraint uk_categoria_nombre
  unique (nombre),
constraint ck_categoria_textos
  check (len(nombre) >= 3 and len(descripcion) >= 10)
);
go

-- Registro histórico de cada rol institucional que ha tenido un usuario. Para los coordinadores también indica qué Facultad o Programa acompaña. Solo una asignación por usuario puede estar vigente.
create table asignacion_rol (
id_asignacion_rol uniqueidentifier primary key not null default newid(),
id_usuario uniqueidentifier not null,
rol varchar(21) not null,
id_facultad uniqueidentifier null,
id_programa uniqueidentifier null,
fecha_inicio datetime2(0) not null default sysdatetime(),
fecha_fin datetime2(0) null,
motivo_fin nvarchar(250) null,

constraint fk_asignacion_rol_id_usuario
  foreign key (id_usuario) references usuario(id_usuario),
constraint fk_asignacion_rol_id_facultad
  foreign key (id_facultad) references facultad(id_facultad),
constraint fk_asignacion_rol_id_programa
  foreign key (id_programa) references programa_academico(id_programa_academico),
constraint ck_asignacion_rol_rol
  check (rol in ('ADMINISTRADOR', 'RECTOR', 'COORDINADOR_ACADEMICO', 'COORDINADOR_FACULTAD', 'COORDINADOR_PROGRAMA', 'DOCENTE', 'ESTUDIANTE')),
constraint ck_asignacion_rol_ambito
  check ((rol = 'COORDINADOR_FACULTAD' and id_facultad is not null and id_programa is null) or (rol = 'COORDINADOR_PROGRAMA' and id_programa is not null and id_facultad is null) or (rol not in ('COORDINADOR_FACULTAD', 'COORDINADOR_PROGRAMA') and id_facultad is null and id_programa is null)),
constraint ck_asignacion_rol_cierre
  check ((fecha_fin is null and motivo_fin is null) or (fecha_fin is not null and fecha_fin >= fecha_inicio and motivo_fin is not null))
);
create unique index ux_asignacion_rol_usuario_vigente on asignacion_rol (id_usuario) where fecha_fin is null;
create unique index ux_asignacion_rol_cargo_unico_vigente on asignacion_rol (rol) where fecha_fin is null and rol in ('RECTOR', 'COORDINADOR_ACADEMICO');
create unique index ux_asignacion_rol_facultad_vigente on asignacion_rol (id_facultad) where fecha_fin is null and id_facultad is not null;
create unique index ux_asignacion_rol_programa_vigente on asignacion_rol (id_programa) where fecha_fin is null and id_programa is not null;
go

-- Código temporal que se envía al correo o al celular de un usuario para validar que le pertenece.
create table codigo_verificacion (
id_codigo_verificacion uniqueidentifier primary key not null default newid(),
id_usuario uniqueidentifier not null,
canal varchar(7) not null,
destino varchar(100) not null,
codigo varchar(60) not null,
fecha_generacion datetime2(0) not null default sysdatetime(),
fecha_expiracion datetime2(0) not null,
intentos_fallidos int not null default 0,
usado bit not null default 0,

constraint fk_codigo_verificacion_id_usuario
  foreign key (id_usuario) references usuario(id_usuario),
constraint ck_codigo_verificacion_canal
  check (canal in ('CORREO', 'CELULAR')),
constraint ck_codigo_verificacion_vigencia
  check (fecha_expiracion > fecha_generacion),
constraint ck_codigo_verificacion_intentos
  check (intentos_fallidos >= 0)
);
create index ix_codigo_verificacion_usuario_canal on codigo_verificacion (id_usuario, canal);
go

-- Petición que hace una persona al registrarse para obtener un rol institucional. La decide (aprueba o rechaza) la autoridad que corresponde según la cadena de aprobación.
create table solicitud_registro (
id_solicitud_registro uniqueidentifier primary key not null default newid(),
id_solicitante uniqueidentifier not null,
rol_solicitado varchar(21) not null,
id_facultad uniqueidentifier null,
id_programa uniqueidentifier null,
estado varchar(9) not null default 'PENDIENTE',
fecha_solicitud datetime2(0) not null default sysdatetime(),
id_decidida_por uniqueidentifier null,
fecha_decision datetime2(0) null,
motivo_rechazo nvarchar(250) null,

constraint fk_solicitud_registro_id_solicitante
  foreign key (id_solicitante) references usuario(id_usuario),
constraint fk_solicitud_registro_id_facultad
  foreign key (id_facultad) references facultad(id_facultad),
constraint fk_solicitud_registro_id_programa
  foreign key (id_programa) references programa_academico(id_programa_academico),
constraint fk_solicitud_registro_id_decidida_por
  foreign key (id_decidida_por) references usuario(id_usuario),
constraint ck_solicitud_registro_rol_solicitado
  check (rol_solicitado in ('COORDINADOR_ACADEMICO', 'COORDINADOR_FACULTAD', 'COORDINADOR_PROGRAMA', 'DOCENTE', 'ESTUDIANTE')),
constraint ck_solicitud_registro_ambito
  check ((rol_solicitado = 'COORDINADOR_FACULTAD' and id_facultad is not null and id_programa is null) or (rol_solicitado in ('COORDINADOR_PROGRAMA', 'DOCENTE', 'ESTUDIANTE') and id_programa is not null and id_facultad is null) or (rol_solicitado = 'COORDINADOR_ACADEMICO' and id_facultad is null and id_programa is null)),
constraint ck_solicitud_registro_estado
  check (estado in ('PENDIENTE', 'APROBADA', 'RECHAZADA')),
constraint ck_solicitud_registro_decision
  check ((estado = 'PENDIENTE' and id_decidida_por is null and fecha_decision is null and motivo_rechazo is null) or (estado = 'APROBADA' and id_decidida_por is not null and fecha_decision is not null) or (estado = 'RECHAZADA' and id_decidida_por is not null and fecha_decision is not null and motivo_rechazo is not null)),
constraint ck_solicitud_registro_no_autoaprobacion
  check (id_decidida_por is null or id_decidida_por <> id_solicitante)
);
create unique index ux_solicitud_registro_pendiente on solicitud_registro (id_solicitante) where estado = 'PENDIENTE';
go

-- Vínculo, con historial, entre un Docente o Estudiante y un Programa Académico. Resuelve la relación muchos a muchos y conserva la trazabilidad de vinculaciones y desvinculaciones.
create table vinculacion_programa (
id_vinculacion_programa uniqueidentifier primary key not null default newid(),
id_usuario uniqueidentifier not null,
id_programa uniqueidentifier not null,
tipo_vinculo varchar(10) not null,
fecha_inicio datetime2(0) not null default sysdatetime(),
fecha_fin datetime2(0) null,
motivo_desvinculacion nvarchar(250) null,

constraint fk_vinculacion_programa_id_usuario
  foreign key (id_usuario) references usuario(id_usuario),
constraint fk_vinculacion_programa_id_programa
  foreign key (id_programa) references programa_academico(id_programa_academico),
constraint ck_vinculacion_programa_tipo_vinculo
  check (tipo_vinculo in ('DOCENTE', 'ESTUDIANTE')),
constraint ck_vinculacion_programa_cierre
  check ((fecha_fin is null and motivo_desvinculacion is null) or (fecha_fin is not null and fecha_fin >= fecha_inicio and motivo_desvinculacion is not null))
);
create unique index ux_vinculacion_programa_vigente on vinculacion_programa (id_usuario, id_programa) where fecha_fin is null;
create index ix_vinculacion_programa_programa on vinculacion_programa (id_programa);
go

-- Reconocimiento formal que una persona otorga a otra persona de la comunidad universitaria. Es la raíz de su conversación.
create table kudo (
id_kudo uniqueidentifier primary key not null default newid(),
id_autor uniqueidentifier not null,
id_destinatario uniqueidentifier not null,
id_categoria uniqueidentifier not null,
contenido nvarchar(max) not null,
fecha_creacion datetime2(0) not null default sysdatetime(),
estado varchar(11) not null default 'CREADO',
cantidad_visualizaciones int not null default 0,
promedio_calificacion decimal(3,2) not null default 0,
cantidad_calificaciones int not null default 0,

constraint fk_kudo_id_autor
  foreign key (id_autor) references usuario(id_usuario),
constraint fk_kudo_id_destinatario
  foreign key (id_destinatario) references usuario(id_usuario),
constraint fk_kudo_id_categoria
  foreign key (id_categoria) references categoria(id_categoria),
constraint ck_kudo_autor_destinatario
  check (id_autor <> id_destinatario),
constraint ck_kudo_contenido
  check (len(ltrim(rtrim(contenido))) > 0),
constraint ck_kudo_estado
  check (estado in ('CREADO', 'VISIBLE', 'REPORTADO', 'EN_REVISION', 'VETADO')),
constraint ck_kudo_indicadores
  check (cantidad_visualizaciones >= 0 and promedio_calificacion between 0 and 5 and cantidad_calificaciones >= 0)
);
create index ix_kudo_muro on kudo (estado, fecha_creacion desc);
create index ix_kudo_autor on kudo (id_autor);
create index ix_kudo_destinatario on kudo (id_destinatario);
create index ix_kudo_categoria on kudo (id_categoria);
go

-- Intervención en la conversación de un Kudo. Su padre es el Kudo (réplica de primer nivel) u otra réplica (respuesta).
create table replica (
id_replica uniqueidentifier primary key not null default newid(),
id_autor uniqueidentifier not null,
id_kudo uniqueidentifier not null,
id_replica_padre uniqueidentifier null,
contenido nvarchar(max) not null,
fecha_creacion datetime2(0) not null default sysdatetime(),
estado varchar(11) not null default 'CREADO',
promedio_calificacion decimal(3,2) not null default 0,
cantidad_calificaciones int not null default 0,

constraint fk_replica_id_autor
  foreign key (id_autor) references usuario(id_usuario),
constraint fk_replica_id_kudo
  foreign key (id_kudo) references kudo(id_kudo),
constraint fk_replica_id_replica_padre
  foreign key (id_replica_padre) references replica(id_replica),
constraint ck_replica_padre
  check (id_replica_padre is null or id_replica_padre <> id_replica),
constraint ck_replica_contenido
  check (len(ltrim(rtrim(contenido))) > 0),
constraint ck_replica_estado
  check (estado in ('CREADO', 'VISIBLE', 'REPORTADO', 'EN_REVISION', 'VETADO')),
constraint ck_replica_indicadores
  check (promedio_calificacion between 0 and 5 and cantidad_calificaciones >= 0)
);
create index ix_replica_kudo on replica (id_kudo, fecha_creacion);
create index ix_replica_padre on replica (id_replica_padre);
go

-- Revisión y decisión institucional sobre un Kudo o una réplica, tomada por el Rector o el Coordinador Académico. El histórico de vetos son las moderaciones con decisión VETAR.
create table moderacion (
id_moderacion uniqueidentifier primary key not null default newid(),
id_kudo uniqueidentifier null,
id_replica uniqueidentifier null,
id_moderador uniqueidentifier not null,
estado varchar(8) not null default 'EN_CURSO',
fecha_inicio datetime2(0) not null default sysdatetime(),
fecha_cierre datetime2(0) null,
decision varchar(7) null,
motivo_decision nvarchar(500) null,

constraint fk_moderacion_id_kudo
  foreign key (id_kudo) references kudo(id_kudo),
constraint fk_moderacion_id_replica
  foreign key (id_replica) references replica(id_replica),
constraint fk_moderacion_id_moderador
  foreign key (id_moderador) references usuario(id_usuario),
constraint ck_moderacion_contenido
  check ((id_kudo is not null and id_replica is null) or (id_kudo is null and id_replica is not null)),
constraint ck_moderacion_estado
  check (estado in ('EN_CURSO', 'CERRADA')),
constraint ck_moderacion_decision
  check (decision is null or decision in ('VETAR', 'LIBERAR')),
constraint ck_moderacion_cierre
  check ((estado = 'EN_CURSO' and fecha_cierre is null and decision is null and motivo_decision is null) or (estado = 'CERRADA' and fecha_cierre is not null and fecha_cierre >= fecha_inicio and decision is not null and motivo_decision is not null and len(motivo_decision) >= 10))
);
create unique index ux_moderacion_kudo_en_curso on moderacion (id_kudo) where estado = 'EN_CURSO' and id_kudo is not null;
create unique index ux_moderacion_replica_en_curso on moderacion (id_replica) where estado = 'EN_CURSO' and id_replica is not null;
go

-- Solicitud de revisión que un usuario hace sobre un Kudo o una réplica que considera inapropiado.
create table reporte (
id_reporte uniqueidentifier primary key not null default newid(),
id_reportante uniqueidentifier not null,
id_kudo uniqueidentifier null,
id_replica uniqueidentifier null,
motivo varchar(17) not null,
descripcion nvarchar(500) null,
fecha_reporte datetime2(0) not null default sysdatetime(),
estado varchar(11) not null default 'PENDIENTE',
id_moderacion uniqueidentifier null,

constraint fk_reporte_id_reportante
  foreign key (id_reportante) references usuario(id_usuario),
constraint fk_reporte_id_kudo
  foreign key (id_kudo) references kudo(id_kudo),
constraint fk_reporte_id_replica
  foreign key (id_replica) references replica(id_replica),
constraint fk_reporte_id_moderacion
  foreign key (id_moderacion) references moderacion(id_moderacion),
constraint ck_reporte_contenido
  check ((id_kudo is not null and id_replica is null) or (id_kudo is null and id_replica is not null)),
constraint ck_reporte_motivo
  check (motivo in ('LENGUAJE_OFENSIVO', 'ACOSO', 'DISCRIMINACION', 'SPAM', 'INFORMACION_FALSA', 'OTRO')),
constraint ck_reporte_descripcion
  check (motivo <> 'OTRO' or (descripcion is not null and len(ltrim(rtrim(descripcion))) > 0)),
constraint ck_reporte_estado
  check (estado in ('PENDIENTE', 'EN_REVISION', 'ACEPTADO', 'RECHAZADO'))
);
create unique index ux_reporte_kudo_abierto on reporte (id_reportante, id_kudo) where estado in ('PENDIENTE', 'EN_REVISION') and id_kudo is not null;
create unique index ux_reporte_replica_abierto on reporte (id_reportante, id_replica) where estado in ('PENDIENTE', 'EN_REVISION') and id_replica is not null;
create index ix_reporte_estado on reporte (estado, fecha_reporte);
go

-- Valoración de 1 a 5 estrellas que un usuario asigna a un Kudo o a una réplica. Cada usuario tiene como máximo una calificación activa por contenido.
create table calificacion (
id_calificacion uniqueidentifier primary key not null default newid(),
id_autor uniqueidentifier not null,
id_kudo uniqueidentifier null,
id_replica uniqueidentifier null,
estrellas tinyint not null,
fecha_calificacion datetime2(0) not null default sysdatetime(),
fecha_modificacion datetime2(0) null,
activa bit not null default 1,

constraint fk_calificacion_id_autor
  foreign key (id_autor) references usuario(id_usuario),
constraint fk_calificacion_id_kudo
  foreign key (id_kudo) references kudo(id_kudo),
constraint fk_calificacion_id_replica
  foreign key (id_replica) references replica(id_replica),
constraint ck_calificacion_contenido
  check ((id_kudo is not null and id_replica is null) or (id_kudo is null and id_replica is not null)),
constraint ck_calificacion_estrellas
  check (estrellas between 1 and 5)
);
create unique index ux_calificacion_kudo_activa on calificacion (id_autor, id_kudo) where activa = 1 and id_kudo is not null;
create unique index ux_calificacion_replica_activa on calificacion (id_autor, id_replica) where activa = 1 and id_replica is not null;
go

-- Registro histórico e inmutable de una operación relevante realizada en la plataforma. Permite reconstruir quién hizo qué, cuándo, sobre qué objeto y por qué.
create table auditoria (
id_auditoria uniqueidentifier primary key not null default newid(),
fecha_hora datetime2(0) not null default sysdatetime(),
id_actor uniqueidentifier null,
accion varchar(60) not null,
tipo_objeto varchar(40) not null,
id_objeto uniqueidentifier not null,
estado_anterior nvarchar(500) null,
estado_nuevo nvarchar(500) null,
motivo nvarchar(500) null,
detalle nvarchar(max) null,

constraint fk_auditoria_id_actor
  foreign key (id_actor) references usuario(id_usuario)
);
create index ix_auditoria_fecha on auditoria (fecha_hora desc);
create index ix_auditoria_objeto on auditoria (tipo_objeto, id_objeto);
create index ix_auditoria_actor on auditoria (id_actor);
go

-- Aviso dentro de la aplicación dirigido a un usuario sobre algo que le concierne.
create table notificacion (
id_notificacion uniqueidentifier primary key not null default newid(),
id_destinatario uniqueidentifier not null,
tipo varchar(19) not null,
mensaje nvarchar(500) not null,
tipo_referencia varchar(40) null,
id_referencia uniqueidentifier null,
fecha_creacion datetime2(0) not null default sysdatetime(),
leida bit not null default 0,
fecha_lectura datetime2(0) null,

constraint fk_notificacion_id_destinatario
  foreign key (id_destinatario) references usuario(id_usuario),
constraint ck_notificacion_tipo
  check (tipo in ('KUDO_RECIBIDO', 'REPLICA_RECIBIDA', 'SOLICITUD_APROBADA', 'SOLICITUD_RECHAZADA', 'SOLICITUD_PENDIENTE', 'CONTENIDO_VETADO', 'CODIGO_VERIFICACION', 'CUENTA_SUSPENDIDA', 'CUENTA_REACTIVADA')),
constraint ck_notificacion_lectura
  check ((leida = 0 and fecha_lectura is null) or (leida = 1 and fecha_lectura is not null))
);
create index ix_notificacion_destinatario on notificacion (id_destinatario, leida, fecha_creacion desc);
go
