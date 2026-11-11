# Calificacion

[Inicio](../../../README.md) · [MER](../README.md) · [Modelo de dominio: Calificacion](../../01-modelo-dominio/objetos/Calificacion.md) · [Diagrama de clases](../../02-diagrama-clases/README.md)

| Campo | Valor |
|---|---|
| Entidad | Calificacion |
| Tabla | `calificacion` |
| Descripción | Valoración de 1 a 5 estrellas que un usuario asigna a un Kudo o a una réplica. Cada usuario tiene como máximo una calificación activa por contenido. |

## Atributos

| Nombre | id_calificacion | id_autor | id_kudo | id_replica | estrellas | fecha_calificacion | fecha_modificacion | activa |
|---|---|---|---|---|---|---|---|---|
| Tipo de dato | uniqueidentifier | uniqueidentifier | uniqueidentifier | uniqueidentifier | int | datetime2 | datetime2 | bit |
| Longitud |  |  |  |  |  |  |  |  |
| Cantidad dígitos parte entera |  |  |  |  | 10 |  |  |  |
| Cantidad dígitos parte decimal (Precisión) |  |  |  |  |  |  |  |  |
| ¿Es autoincremento? | NO | NO | NO | NO | NO | NO | NO | NO |
| ¿Es obligatorio? | SI | SI | NO | NO | SI | SI | NO | SI |
| ¿Es llave primaria o es parte de ella? | SI | NO | NO | NO | NO | NO | NO | NO |
| Llave Foránea · ¿Es llave foránea? | NO | SI | SI | SI | NO | NO | NO | NO |
| Llave Foránea · Entidad |  | usuario | kudo | replica |  |  |  |  |
| Llave Foránea · Columna(s) |  | id_usuario | id_kudo | id_replica |  |  |  |  |
| Restricciones de unicidad · ¿Posee restricción de unicidad o es parte de ella? | NO | SI | SI | SI | NO | NO | NO | NO |
| Restricciones de unicidad · Nombre restricción unicidad a la que pertenece |  | ux_calificacion_kudo_activa<br>ux_calificacion_replica_activa | ux_calificacion_kudo_activa | ux_calificacion_replica_activa |  |  |  |  |
| Restricciones de chequeo · ¿Posee restricción de chequeo o es parte de ella? | NO | NO | SI | SI | SI | NO | NO | NO |
| Restricciones de chequeo · Nombre restricción chequeo a la que pertenece |  |  | ck_calificacion_contenido | ck_calificacion_contenido | ck_calificacion_estrellas |  |  |  |
| Restricciones de índice · ¿Posee restricción de índice o es parte de ella? | NO | SI | SI | SI | NO | NO | NO | NO |
| Restricciones de índice · Tipo |  | Único filtrado<br>Único filtrado | Único filtrado | Único filtrado |  |  |  |  |
| Restricciones de índice · Nombre restricción índice a la que pertenece |  | ux_calificacion_kudo_activa<br>ux_calificacion_replica_activa | ux_calificacion_kudo_activa | ux_calificacion_replica_activa |  |  |  |  |
| Valor por defecto | newid() |  |  |  |  | sysdatetime() |  | 1 |
| Atributo del objeto de dominio | id | autor | kudo | replica | estrellas | fechaCalificacion | fechaModificacion | activa |
| Descripción del campo | Identificador único de la calificación | Usuario que califica (RN-183) | Kudo calificado | Réplica calificada | Valoración asignada (RN-177) | Fecha y hora en que se registró (RN-182) | Fecha y hora del último cambio de estrellas (RN-237) | Indica si es la calificación activa del usuario para ese contenido |

## Restricciones

| Nombre | Tipo | Columnas | Definición |
|---|---|---|---|
| pk_calificacion | Llave primaria | id_calificacion | `primary key` |
| fk_calificacion_id_autor | Llave foránea | id_autor | `foreign key (id_autor) references usuario(id_usuario)` |
| fk_calificacion_id_kudo | Llave foránea | id_kudo | `foreign key (id_kudo) references kudo(id_kudo)` |
| fk_calificacion_id_replica | Llave foránea | id_replica | `foreign key (id_replica) references replica(id_replica)` |
| ck_calificacion_contenido | Chequeo | id_kudo, id_replica | `check ((id_kudo is not null and id_replica is null) or (id_kudo is null and id_replica is not null))` |
| ck_calificacion_estrellas | Chequeo | estrellas | `check (estrellas between 1 and 5)` |
| ux_calificacion_kudo_activa | Índice único filtrado | id_autor, id_kudo | `(id_autor, id_kudo) where activa = 1 and id_kudo is not null` |
| ux_calificacion_replica_activa | Índice único filtrado | id_autor, id_replica | `(id_autor, id_replica) where activa = 1 and id_replica is not null` |

El script completo está en [`sql/crear-tablas-kudos.sql`](../../../sql/crear-tablas-kudos.sql).
