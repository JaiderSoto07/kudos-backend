# Reporte

[Inicio](../../../README.md) · [MER](../README.md) · [Modelo de dominio: Reporte](../../01-modelo-dominio/objetos/Reporte.md) · [Diagrama de clases](../../02-diagrama-clases/README.md)

| Campo | Valor |
|---|---|
| Entidad | Reporte |
| Tabla | `reporte` |
| Descripción | Solicitud de revisión que un usuario hace sobre un Kudo o una réplica que considera inapropiado. |

## Atributos

| Nombre | id_reporte | id_reportante | id_kudo | id_replica | motivo | descripcion | fecha_reporte | estado | id_moderacion |
|---|---|---|---|---|---|---|---|---|---|
| Tipo de dato | uniqueidentifier | uniqueidentifier | uniqueidentifier | uniqueidentifier | varchar | nvarchar | datetime2 | varchar | uniqueidentifier |
| Longitud |  |  |  |  | 17 | 500 |  | 11 |  |
| Cantidad dígitos parte entera |  |  |  |  |  |  |  |  |  |
| Cantidad dígitos parte decimal (Precisión) |  |  |  |  |  |  |  |  |  |
| ¿Es autoincremento? | NO | NO | NO | NO | NO | NO | NO | NO | NO |
| ¿Es obligatorio? | SI | SI | NO | NO | SI | NO | SI | SI | NO |
| ¿Es llave primaria o es parte de ella? | SI | NO | NO | NO | NO | NO | NO | NO | NO |
| Llave Foránea · ¿Es llave foránea? | NO | SI | SI | SI | NO | NO | NO | NO | SI |
| Llave Foránea · Entidad |  | usuario | kudo | replica |  |  |  |  | moderacion |
| Llave Foránea · Columna(s) |  | id_usuario | id_kudo | id_replica |  |  |  |  | id_moderacion |
| Restricciones de unicidad · ¿Posee restricción de unicidad o es parte de ella? | NO | SI | SI | SI | NO | NO | NO | NO | NO |
| Restricciones de unicidad · Nombre restricción unicidad a la que pertenece |  | ux_reporte_kudo_abierto<br>ux_reporte_replica_abierto | ux_reporte_kudo_abierto | ux_reporte_replica_abierto |  |  |  |  |  |
| Restricciones de chequeo · ¿Posee restricción de chequeo o es parte de ella? | NO | NO | SI | SI | SI | SI | NO | SI | NO |
| Restricciones de chequeo · Nombre restricción chequeo a la que pertenece |  |  | ck_reporte_contenido | ck_reporte_contenido | ck_reporte_motivo<br>ck_reporte_descripcion | ck_reporte_descripcion |  | ck_reporte_estado |  |
| Restricciones de índice · ¿Posee restricción de índice o es parte de ella? | NO | SI | SI | SI | NO | NO | NO | SI | NO |
| Restricciones de índice · Tipo |  | Único filtrado<br>Único filtrado | Único filtrado | Único filtrado |  |  |  | No agrupado |  |
| Restricciones de índice · Nombre restricción índice a la que pertenece |  | ux_reporte_kudo_abierto<br>ux_reporte_replica_abierto | ux_reporte_kudo_abierto | ux_reporte_replica_abierto |  |  |  | ix_reporte_estado |  |
| Valor por defecto | newid() |  |  |  |  |  | sysdatetime() | 'PENDIENTE' |  |
| Atributo del objeto de dominio | id | reportante | kudo | replica | motivo | descripcion | fechaReporte | estado | moderacion |
| Descripción del campo | Identificador único del reporte | Usuario que reporta (RN-188) | Kudo reportado | Réplica reportada | Motivo del reporte (RN-190) | Explicación del reportante | Fecha y hora del reporte (RN-189) | Estado del reporte | Moderación que resolvió el reporte |

## Restricciones

| Nombre | Tipo | Columnas | Definición |
|---|---|---|---|
| pk_reporte | Llave primaria | id_reporte | `primary key` |
| fk_reporte_id_reportante | Llave foránea | id_reportante | `foreign key (id_reportante) references usuario(id_usuario)` |
| fk_reporte_id_kudo | Llave foránea | id_kudo | `foreign key (id_kudo) references kudo(id_kudo)` |
| fk_reporte_id_replica | Llave foránea | id_replica | `foreign key (id_replica) references replica(id_replica)` |
| fk_reporte_id_moderacion | Llave foránea | id_moderacion | `foreign key (id_moderacion) references moderacion(id_moderacion)` |
| ck_reporte_contenido | Chequeo | id_kudo, id_replica | `check ((id_kudo is not null and id_replica is null) or (id_kudo is null and id_replica is not null))` |
| ck_reporte_motivo | Chequeo | motivo | `check (motivo in ('LENGUAJE_OFENSIVO', 'ACOSO', 'DISCRIMINACION', 'SPAM', 'INFORMACION_FALSA', 'OTRO'))` |
| ck_reporte_descripcion | Chequeo | motivo, descripcion | `check (motivo <> 'OTRO' or (descripcion is not null and len(ltrim(rtrim(descripcion))) > 0))` |
| ck_reporte_estado | Chequeo | estado | `check (estado in ('PENDIENTE', 'EN_REVISION', 'ACEPTADO', 'RECHAZADO'))` |
| ux_reporte_kudo_abierto | Índice único filtrado | id_reportante, id_kudo | `(id_reportante, id_kudo) where estado in ('PENDIENTE', 'EN_REVISION') and id_kudo is not null` |
| ux_reporte_replica_abierto | Índice único filtrado | id_reportante, id_replica | `(id_reportante, id_replica) where estado in ('PENDIENTE', 'EN_REVISION') and id_replica is not null` |
| ix_reporte_estado | Índice | estado | `(estado, fecha_reporte)` |

El script completo está en [`sql/crear-tablas-kudos.sql`](../../../sql/crear-tablas-kudos.sql).
