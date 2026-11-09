# Moderacion

[Inicio](../../../README.md) · [MER](../README.md) · [Modelo de dominio: Moderacion](../../01-modelo-dominio/objetos/Moderacion.md) · [Diagrama de clases](../../02-diagrama-clases/README.md)

| Campo | Valor |
|---|---|
| Entidad | Moderacion |
| Tabla | `moderacion` |
| Descripción | Revisión y decisión institucional sobre un Kudo o una réplica, tomada por el Rector o el Coordinador Académico. El histórico de vetos son las moderaciones con decisión VETAR. |

## Atributos

| Nombre | id_moderacion | id_kudo | id_replica | id_moderador | estado | fecha_inicio | fecha_cierre | decision | motivo_decision |
|---|---|---|---|---|---|---|---|---|---|
| Tipo de dato | uniqueidentifier | uniqueidentifier | uniqueidentifier | uniqueidentifier | varchar | datetime2 | datetime2 | varchar | nvarchar |
| Longitud |  |  |  |  | 8 |  |  | 7 | 500 |
| Cantidad dígitos parte entera |  |  |  |  |  |  |  |  |  |
| Cantidad dígitos parte decimal (Precisión) |  |  |  |  |  |  |  |  |  |
| ¿Es autoincremento? | NO | NO | NO | NO | NO | NO | NO | NO | NO |
| ¿Es obligatorio? | SI | NO | NO | SI | SI | SI | NO | NO | NO |
| ¿Es llave primaria o es parte de ella? | SI | NO | NO | NO | NO | NO | NO | NO | NO |
| Llave Foránea · ¿Es llave foránea? | NO | SI | SI | SI | NO | NO | NO | NO | NO |
| Llave Foránea · Entidad |  | kudo | replica | usuario |  |  |  |  |  |
| Llave Foránea · Columna(s) |  | id_kudo | id_replica | id_usuario |  |  |  |  |  |
| Restricciones de unicidad · ¿Posee restricción de unicidad o es parte de ella? | NO | SI | SI | NO | NO | NO | NO | NO | NO |
| Restricciones de unicidad · Nombre restricción unicidad a la que pertenece |  | ux_moderacion_kudo_en_curso | ux_moderacion_replica_en_curso |  |  |  |  |  |  |
| Restricciones de chequeo · ¿Posee restricción de chequeo o es parte de ella? | NO | SI | SI | NO | SI | SI | SI | SI | SI |
| Restricciones de chequeo · Nombre restricción chequeo a la que pertenece |  | ck_moderacion_contenido | ck_moderacion_contenido |  | ck_moderacion_estado<br>ck_moderacion_cierre | ck_moderacion_cierre | ck_moderacion_cierre | ck_moderacion_decision<br>ck_moderacion_cierre | ck_moderacion_cierre |
| Restricciones de índice · ¿Posee restricción de índice o es parte de ella? | NO | SI | SI | NO | NO | NO | NO | NO | NO |
| Restricciones de índice · Tipo |  | Único filtrado | Único filtrado |  |  |  |  |  |  |
| Restricciones de índice · Nombre restricción índice a la que pertenece |  | ux_moderacion_kudo_en_curso | ux_moderacion_replica_en_curso |  |  |  |  |  |  |
| Valor por defecto | newid() |  |  |  | 'EN_CURSO' | sysdatetime() |  |  |  |
| Atributo del objeto de dominio | id | kudo | replica | moderador | estado | fechaInicio | fechaCierre | decision | motivoDecision |
| Descripción del campo | Identificador único de la moderación | Kudo moderado | Réplica moderada | Autoridad que revisa y decide | Estado de la moderación | Fecha y hora en que inicia la revisión (RN-239) | Fecha y hora de la decisión (RN-240) | Decisión tomada | Justificación de la decisión (RN-250) |

## Restricciones

| Nombre | Tipo | Columnas | Definición |
|---|---|---|---|
| pk_moderacion | Llave primaria | id_moderacion | `primary key` |
| fk_moderacion_id_kudo | Llave foránea | id_kudo | `foreign key (id_kudo) references kudo(id_kudo)` |
| fk_moderacion_id_replica | Llave foránea | id_replica | `foreign key (id_replica) references replica(id_replica)` |
| fk_moderacion_id_moderador | Llave foránea | id_moderador | `foreign key (id_moderador) references usuario(id_usuario)` |
| ck_moderacion_contenido | Chequeo | id_kudo, id_replica | `check ((id_kudo is not null and id_replica is null) or (id_kudo is null and id_replica is not null))` |
| ck_moderacion_estado | Chequeo | estado | `check (estado in ('EN_CURSO', 'CERRADA'))` |
| ck_moderacion_decision | Chequeo | decision | `check (decision is null or decision in ('VETAR', 'LIBERAR'))` |
| ck_moderacion_cierre | Chequeo | estado, fecha_inicio, fecha_cierre, decision, motivo_decision | `check ((estado = 'EN_CURSO' and fecha_cierre is null and decision is null and motivo_decision is null) or (estado = 'CERRADA' and fecha_cierre is not null and fecha_cierre >= fecha_inicio and decision is not null and motivo_decision is not null and len(motivo_decision) >= 10))` |
| ux_moderacion_kudo_en_curso | Índice único filtrado | id_kudo | `(id_kudo) where estado = 'EN_CURSO' and id_kudo is not null` |
| ux_moderacion_replica_en_curso | Índice único filtrado | id_replica | `(id_replica) where estado = 'EN_CURSO' and id_replica is not null` |

El script completo está en [`sql/crear-tablas-kudos.sql`](../../../sql/crear-tablas-kudos.sql).
