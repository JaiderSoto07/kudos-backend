# Notificacion

[Inicio](../../../README.md) · [MER](../README.md) · [Modelo de dominio: Notificacion](../../01-modelo-dominio/objetos/Notificacion.md) · [Diagrama de clases](../../02-diagrama-clases/README.md)

| Campo | Valor |
|---|---|
| Entidad | Notificacion |
| Tabla | `notificacion` |
| Descripción | Aviso dentro de la aplicación dirigido a un usuario sobre algo que le concierne. |

## Atributos

| Nombre | id_notificacion | id_destinatario | tipo | mensaje | tipo_referencia | id_referencia | fecha_creacion | leida | fecha_lectura |
|---|---|---|---|---|---|---|---|---|---|
| Tipo de dato | uniqueidentifier | uniqueidentifier | varchar | nvarchar | varchar | uniqueidentifier | datetime2 | bit | datetime2 |
| Longitud |  |  | 19 | 500 | 40 |  |  |  |  |
| Cantidad dígitos parte entera |  |  |  |  |  |  |  |  |  |
| Cantidad dígitos parte decimal (Precisión) |  |  |  |  |  |  |  |  |  |
| ¿Es autoincremento? | NO | NO | NO | NO | NO | NO | NO | NO | NO |
| ¿Es obligatorio? | SI | SI | SI | SI | NO | NO | SI | SI | NO |
| ¿Es llave primaria o es parte de ella? | SI | NO | NO | NO | NO | NO | NO | NO | NO |
| Llave Foránea · ¿Es llave foránea? | NO | SI | NO | NO | NO | NO | NO | NO | NO |
| Llave Foránea · Entidad |  | usuario |  |  |  |  |  |  |  |
| Llave Foránea · Columna(s) |  | id_usuario |  |  |  |  |  |  |  |
| Restricciones de unicidad · ¿Posee restricción de unicidad o es parte de ella? | NO | NO | NO | NO | NO | NO | NO | NO | NO |
| Restricciones de unicidad · Nombre restricción unicidad a la que pertenece |  |  |  |  |  |  |  |  |  |
| Restricciones de chequeo · ¿Posee restricción de chequeo o es parte de ella? | NO | NO | SI | NO | NO | NO | NO | SI | SI |
| Restricciones de chequeo · Nombre restricción chequeo a la que pertenece |  |  | ck_notificacion_tipo |  |  |  |  | ck_notificacion_lectura | ck_notificacion_lectura |
| Restricciones de índice · ¿Posee restricción de índice o es parte de ella? | NO | SI | NO | NO | NO | NO | NO | SI | NO |
| Restricciones de índice · Tipo |  | No agrupado |  |  |  |  |  | No agrupado |  |
| Restricciones de índice · Nombre restricción índice a la que pertenece |  | ix_notificacion_destinatario |  |  |  |  |  | ix_notificacion_destinatario |  |
| Valor por defecto | newid() |  |  |  |  |  | sysdatetime() | 0 |  |
| Atributo del objeto de dominio | id | destinatario | tipo | mensaje | tipoReferencia | idReferencia | fechaCreacion | leida | fechaLectura |
| Descripción del campo | Identificador único de la notificación | Usuario que recibe el aviso | Tipo de aviso | Texto del aviso | Tipo del objeto relacionado | Identificador del objeto relacionado | Fecha y hora de creación | Indica si el destinatario ya la leyó | Fecha y hora de lectura |

## Restricciones

| Nombre | Tipo | Columnas | Definición |
|---|---|---|---|
| pk_notificacion | Llave primaria | id_notificacion | `primary key` |
| fk_notificacion_id_destinatario | Llave foránea | id_destinatario | `foreign key (id_destinatario) references usuario(id_usuario)` |
| ck_notificacion_tipo | Chequeo | tipo | `check (tipo in ('KUDO_RECIBIDO', 'REPLICA_RECIBIDA', 'SOLICITUD_APROBADA', 'SOLICITUD_RECHAZADA', 'SOLICITUD_PENDIENTE', 'CONTENIDO_VETADO', 'REPORTE_RECHAZADO', 'CODIGO_VERIFICACION', 'CUENTA_SUSPENDIDA', 'CUENTA_REACTIVADA'))` |
| ck_notificacion_lectura | Chequeo | leida, fecha_lectura | `check ((leida = 0 and fecha_lectura is null) or (leida = 1 and fecha_lectura is not null))` |
| ix_notificacion_destinatario | Índice | id_destinatario, leida | `(id_destinatario, leida, fecha_creacion desc)` |

El script completo está en [`sql/crear-tablas-kudos.sql`](../../../sql/crear-tablas-kudos.sql).
