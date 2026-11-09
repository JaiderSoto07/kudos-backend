# Auditoria

[Inicio](../../../README.md) · [MER](../README.md) · [Modelo de dominio: Auditoria](../../01-modelo-dominio/objetos/Auditoria.md) · [Diagrama de clases](../../02-diagrama-clases/README.md)

| Campo | Valor |
|---|---|
| Entidad | Auditoria |
| Tabla | `auditoria` |
| Descripción | Registro histórico e inmutable de una operación relevante realizada en la plataforma. Permite reconstruir quién hizo qué, cuándo, sobre qué objeto y por qué. |

## Atributos

| Nombre | id_auditoria | fecha_hora | id_actor | accion | tipo_objeto | id_objeto | estado_anterior | estado_nuevo | motivo | detalle |
|---|---|---|---|---|---|---|---|---|---|---|
| Tipo de dato | uniqueidentifier | datetime2 | uniqueidentifier | varchar | varchar | uniqueidentifier | nvarchar | nvarchar | nvarchar | nvarchar |
| Longitud |  |  |  | 60 | 40 |  | 500 | 500 | 500 | max |
| Cantidad dígitos parte entera |  |  |  |  |  |  |  |  |  |  |
| Cantidad dígitos parte decimal (Precisión) |  |  |  |  |  |  |  |  |  |  |
| ¿Es autoincremento? | NO | NO | NO | NO | NO | NO | NO | NO | NO | NO |
| ¿Es obligatorio? | SI | SI | NO | SI | SI | SI | NO | NO | NO | NO |
| ¿Es llave primaria o es parte de ella? | SI | NO | NO | NO | NO | NO | NO | NO | NO | NO |
| Llave Foránea · ¿Es llave foránea? | NO | NO | SI | NO | NO | NO | NO | NO | NO | NO |
| Llave Foránea · Entidad |  |  | usuario |  |  |  |  |  |  |  |
| Llave Foránea · Columna(s) |  |  | id_usuario |  |  |  |  |  |  |  |
| Restricciones de unicidad · ¿Posee restricción de unicidad o es parte de ella? | NO | NO | NO | NO | NO | NO | NO | NO | NO | NO |
| Restricciones de unicidad · Nombre restricción unicidad a la que pertenece |  |  |  |  |  |  |  |  |  |  |
| Restricciones de chequeo · ¿Posee restricción de chequeo o es parte de ella? | NO | NO | NO | NO | NO | NO | NO | NO | NO | NO |
| Restricciones de chequeo · Nombre restricción chequeo a la que pertenece |  |  |  |  |  |  |  |  |  |  |
| Restricciones de índice · ¿Posee restricción de índice o es parte de ella? | NO | SI | SI | NO | SI | SI | NO | NO | NO | NO |
| Restricciones de índice · Tipo |  | No agrupado | No agrupado |  | No agrupado | No agrupado |  |  |  |  |
| Restricciones de índice · Nombre restricción índice a la que pertenece |  | ix_auditoria_fecha | ix_auditoria_actor |  | ix_auditoria_objeto | ix_auditoria_objeto |  |  |  |  |
| Valor por defecto | newid() | sysdatetime() |  |  |  |  |  |  |  |  |
| Atributo del objeto de dominio | id | fechaHora | actor | accion | tipoObjeto | idObjeto | estadoAnterior | estadoNuevo | motivo | detalle |
| Descripción del campo | Identificador único del registro | Momento en que ocurrió la operación | Usuario que realizó la operación | Evento de dominio registrado | Tipo del objeto afectado | Identificador del objeto afectado | Estado o valor antes de la operación | Estado o valor después de la operación | Motivo declarado por el actor | Información adicional del evento |

## Restricciones

| Nombre | Tipo | Columnas | Definición |
|---|---|---|---|
| pk_auditoria | Llave primaria | id_auditoria | `primary key` |
| fk_auditoria_id_actor | Llave foránea | id_actor | `foreign key (id_actor) references usuario(id_usuario)` |
| ix_auditoria_fecha | Índice | fecha_hora | `(fecha_hora desc)` |
| ix_auditoria_objeto | Índice | tipo_objeto, id_objeto | `(tipo_objeto, id_objeto)` |
| ix_auditoria_actor | Índice | id_actor | `(id_actor)` |

El script completo está en [`sql/crear-tablas-kudos.sql`](../../../sql/crear-tablas-kudos.sql).
