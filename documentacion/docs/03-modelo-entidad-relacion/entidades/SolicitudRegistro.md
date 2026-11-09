# SolicitudRegistro

[Inicio](../../../README.md) · [MER](../README.md) · [Modelo de dominio: SolicitudRegistro](../../01-modelo-dominio/objetos/SolicitudRegistro.md) · [Diagrama de clases](../../02-diagrama-clases/README.md)

| Campo | Valor |
|---|---|
| Entidad | SolicitudRegistro |
| Tabla | `solicitud_registro` |
| Descripción | Petición que hace una persona al registrarse para obtener un rol institucional. La decide (aprueba o rechaza) la autoridad que corresponde según la cadena de aprobación. |

## Atributos

| Nombre | id_solicitud_registro | id_solicitante | rol_solicitado | id_facultad | id_programa | estado | fecha_solicitud | id_decidida_por | fecha_decision | motivo_rechazo |
|---|---|---|---|---|---|---|---|---|---|---|
| Tipo de dato | uniqueidentifier | uniqueidentifier | varchar | uniqueidentifier | uniqueidentifier | varchar | datetime2 | uniqueidentifier | datetime2 | nvarchar |
| Longitud |  |  | 21 |  |  | 9 |  |  |  | 250 |
| Cantidad dígitos parte entera |  |  |  |  |  |  |  |  |  |  |
| Cantidad dígitos parte decimal (Precisión) |  |  |  |  |  |  |  |  |  |  |
| ¿Es autoincremento? | NO | NO | NO | NO | NO | NO | NO | NO | NO | NO |
| ¿Es obligatorio? | SI | SI | SI | NO | NO | SI | SI | NO | NO | NO |
| ¿Es llave primaria o es parte de ella? | SI | NO | NO | NO | NO | NO | NO | NO | NO | NO |
| Llave Foránea · ¿Es llave foránea? | NO | SI | NO | SI | SI | NO | NO | SI | NO | NO |
| Llave Foránea · Entidad |  | usuario |  | facultad | programa_academico |  |  | usuario |  |  |
| Llave Foránea · Columna(s) |  | id_usuario |  | id_facultad | id_programa_academico |  |  | id_usuario |  |  |
| Restricciones de unicidad · ¿Posee restricción de unicidad o es parte de ella? | NO | SI | NO | NO | NO | NO | NO | NO | NO | NO |
| Restricciones de unicidad · Nombre restricción unicidad a la que pertenece |  | ux_solicitud_registro_pendiente |  |  |  |  |  |  |  |  |
| Restricciones de chequeo · ¿Posee restricción de chequeo o es parte de ella? | NO | SI | SI | SI | SI | SI | NO | SI | SI | SI |
| Restricciones de chequeo · Nombre restricción chequeo a la que pertenece |  | ck_solicitud_registro_no_autoaprobacion | ck_solicitud_registro_rol_solicitado<br>ck_solicitud_registro_ambito | ck_solicitud_registro_ambito | ck_solicitud_registro_ambito | ck_solicitud_registro_estado<br>ck_solicitud_registro_decision |  | ck_solicitud_registro_decision<br>ck_solicitud_registro_no_autoaprobacion | ck_solicitud_registro_decision | ck_solicitud_registro_decision |
| Restricciones de índice · ¿Posee restricción de índice o es parte de ella? | NO | SI | NO | NO | NO | NO | NO | NO | NO | NO |
| Restricciones de índice · Tipo |  | Único filtrado |  |  |  |  |  |  |  |  |
| Restricciones de índice · Nombre restricción índice a la que pertenece |  | ux_solicitud_registro_pendiente |  |  |  |  |  |  |  |  |
| Valor por defecto | newid() |  |  |  |  | 'PENDIENTE' | sysdatetime() |  |  |  |
| Atributo del objeto de dominio | id | solicitante | rolSolicitado | facultad | programa | estado | fechaSolicitud | decididaPor | fechaDecision | motivoRechazo |
| Descripción del campo | Identificador único de la solicitud | Usuario que solicita el rol | Rol institucional que solicita | Facultad que solicita coordinar | Programa al que pertenece o que solicita coordinar | Estado de la solicitud | Fecha y hora de la solicitud | Autoridad que aprobó o rechazó | Fecha y hora de la decisión | Razón del rechazo |

## Restricciones

| Nombre | Tipo | Columnas | Definición |
|---|---|---|---|
| pk_solicitud_registro | Llave primaria | id_solicitud_registro | `primary key` |
| fk_solicitud_registro_id_solicitante | Llave foránea | id_solicitante | `foreign key (id_solicitante) references usuario(id_usuario)` |
| fk_solicitud_registro_id_facultad | Llave foránea | id_facultad | `foreign key (id_facultad) references facultad(id_facultad)` |
| fk_solicitud_registro_id_programa | Llave foránea | id_programa | `foreign key (id_programa) references programa_academico(id_programa_academico)` |
| fk_solicitud_registro_id_decidida_por | Llave foránea | id_decidida_por | `foreign key (id_decidida_por) references usuario(id_usuario)` |
| ck_solicitud_registro_rol_solicitado | Chequeo | rol_solicitado | `check (rol_solicitado in ('COORDINADOR_ACADEMICO', 'COORDINADOR_FACULTAD', 'COORDINADOR_PROGRAMA', 'DOCENTE', 'ESTUDIANTE'))` |
| ck_solicitud_registro_ambito | Chequeo | rol_solicitado, id_facultad, id_programa | `check ((rol_solicitado = 'COORDINADOR_FACULTAD' and id_facultad is not null and id_programa is null) or (rol_solicitado in ('COORDINADOR_PROGRAMA', 'DOCENTE', 'ESTUDIANTE') and id_programa is not null and id_facultad is null) or (rol_solicitado = 'COORDINADOR_ACADEMICO' and id_facultad is null and id_programa is null))` |
| ck_solicitud_registro_estado | Chequeo | estado | `check (estado in ('PENDIENTE', 'APROBADA', 'RECHAZADA'))` |
| ck_solicitud_registro_decision | Chequeo | estado, id_decidida_por, fecha_decision, motivo_rechazo | `check ((estado = 'PENDIENTE' and id_decidida_por is null and fecha_decision is null and motivo_rechazo is null) or (estado = 'APROBADA' and id_decidida_por is not null and fecha_decision is not null) or (estado = 'RECHAZADA' and id_decidida_por is not null and fecha_decision is not null and motivo_rechazo is not null))` |
| ck_solicitud_registro_no_autoaprobacion | Chequeo | id_solicitante, id_decidida_por | `check (id_decidida_por is null or id_decidida_por <> id_solicitante)` |
| ux_solicitud_registro_pendiente | Índice único filtrado | id_solicitante | `(id_solicitante) where estado = 'PENDIENTE'` |

El script completo está en [`sql/crear-tablas-kudos.sql`](../../../sql/crear-tablas-kudos.sql).
