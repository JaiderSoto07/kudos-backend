# AsignacionRol

[Inicio](../../../README.md) · [MER](../README.md) · [Modelo de dominio: AsignacionRol](../../01-modelo-dominio/objetos/AsignacionRol.md) · [Diagrama de clases](../../02-diagrama-clases/README.md)

| Campo | Valor |
|---|---|
| Entidad | AsignacionRol |
| Tabla | `asignacion_rol` |
| Descripción | Registro histórico de cada rol institucional que ha tenido un usuario. Para los coordinadores también indica qué Facultad o Programa acompaña. Solo una asignación por usuario puede estar vigente. |

## Atributos

| Nombre | id_asignacion_rol | id_usuario | rol | id_facultad | id_programa | fecha_inicio | fecha_fin | motivo_fin |
|---|---|---|---|---|---|---|---|---|
| Tipo de dato | uniqueidentifier | uniqueidentifier | varchar | uniqueidentifier | uniqueidentifier | datetime2 | datetime2 | nvarchar |
| Longitud |  |  | 21 |  |  |  |  | 250 |
| Cantidad dígitos parte entera |  |  |  |  |  |  |  |  |
| Cantidad dígitos parte decimal (Precisión) |  |  |  |  |  |  |  |  |
| ¿Es autoincremento? | NO | NO | NO | NO | NO | NO | NO | NO |
| ¿Es obligatorio? | SI | SI | SI | NO | NO | SI | NO | NO |
| ¿Es llave primaria o es parte de ella? | SI | NO | NO | NO | NO | NO | NO | NO |
| Llave Foránea · ¿Es llave foránea? | NO | SI | NO | SI | SI | NO | NO | NO |
| Llave Foránea · Entidad |  | usuario |  | facultad | programa_academico |  |  |  |
| Llave Foránea · Columna(s) |  | id_usuario |  | id_facultad | id_programa_academico |  |  |  |
| Restricciones de unicidad · ¿Posee restricción de unicidad o es parte de ella? | NO | SI | SI | SI | SI | NO | NO | NO |
| Restricciones de unicidad · Nombre restricción unicidad a la que pertenece |  | ux_asignacion_rol_usuario_vigente | ux_asignacion_rol_cargo_unico_vigente | ux_asignacion_rol_facultad_vigente | ux_asignacion_rol_programa_vigente |  |  |  |
| Restricciones de chequeo · ¿Posee restricción de chequeo o es parte de ella? | NO | NO | SI | SI | SI | SI | SI | SI |
| Restricciones de chequeo · Nombre restricción chequeo a la que pertenece |  |  | ck_asignacion_rol_rol<br>ck_asignacion_rol_ambito | ck_asignacion_rol_ambito | ck_asignacion_rol_ambito | ck_asignacion_rol_cierre | ck_asignacion_rol_cierre | ck_asignacion_rol_cierre |
| Restricciones de índice · ¿Posee restricción de índice o es parte de ella? | NO | SI | SI | SI | SI | NO | NO | NO |
| Restricciones de índice · Tipo |  | Único filtrado | Único filtrado | Único filtrado | Único filtrado |  |  |  |
| Restricciones de índice · Nombre restricción índice a la que pertenece |  | ux_asignacion_rol_usuario_vigente | ux_asignacion_rol_cargo_unico_vigente | ux_asignacion_rol_facultad_vigente | ux_asignacion_rol_programa_vigente |  |  |  |
| Valor por defecto | newid() |  |  |  |  | sysdatetime() |  |  |
| Atributo del objeto de dominio | id | usuario | rol | facultad | programa | fechaInicio | fechaFin | motivoFin |
| Descripción del campo | Identificador único de la asignación | Usuario al que se asigna el rol | Rol institucional asignado | Facultad que acompaña el Coordinador de Facultad | Programa que acompaña el Coordinador de Programa | Fecha y hora en que inicia la vigencia del rol | Fecha y hora en que termina la vigencia del rol | Razón por la que terminó la asignación |

## Restricciones

| Nombre | Tipo | Columnas | Definición |
|---|---|---|---|
| pk_asignacion_rol | Llave primaria | id_asignacion_rol | `primary key` |
| fk_asignacion_rol_id_usuario | Llave foránea | id_usuario | `foreign key (id_usuario) references usuario(id_usuario)` |
| fk_asignacion_rol_id_facultad | Llave foránea | id_facultad | `foreign key (id_facultad) references facultad(id_facultad)` |
| fk_asignacion_rol_id_programa | Llave foránea | id_programa | `foreign key (id_programa) references programa_academico(id_programa_academico)` |
| ck_asignacion_rol_rol | Chequeo | rol | `check (rol in ('ADMINISTRADOR', 'RECTOR', 'COORDINADOR_ACADEMICO', 'COORDINADOR_FACULTAD', 'COORDINADOR_PROGRAMA', 'DOCENTE', 'ESTUDIANTE'))` |
| ck_asignacion_rol_ambito | Chequeo | rol, id_facultad, id_programa | `check ((rol = 'COORDINADOR_FACULTAD' and id_facultad is not null and id_programa is null) or (rol = 'COORDINADOR_PROGRAMA' and id_programa is not null and id_facultad is null) or (rol not in ('COORDINADOR_FACULTAD', 'COORDINADOR_PROGRAMA') and id_facultad is null and id_programa is null))` |
| ck_asignacion_rol_cierre | Chequeo | fecha_inicio, fecha_fin, motivo_fin | `check ((fecha_fin is null and motivo_fin is null) or (fecha_fin is not null and fecha_fin >= fecha_inicio and motivo_fin is not null))` |
| ux_asignacion_rol_usuario_vigente | Índice único filtrado | id_usuario | `(id_usuario) where fecha_fin is null` |
| ux_asignacion_rol_cargo_unico_vigente | Índice único filtrado | rol | `(rol) where fecha_fin is null and rol in ('RECTOR', 'COORDINADOR_ACADEMICO')` |
| ux_asignacion_rol_facultad_vigente | Índice único filtrado | id_facultad | `(id_facultad) where fecha_fin is null and id_facultad is not null` |
| ux_asignacion_rol_programa_vigente | Índice único filtrado | id_programa | `(id_programa) where fecha_fin is null and id_programa is not null` |

El script completo está en [`sql/crear-tablas-kudos.sql`](../../../sql/crear-tablas-kudos.sql).
