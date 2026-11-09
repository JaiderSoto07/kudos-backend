# Replica

[Inicio](../../../README.md) · [MER](../README.md) · [Modelo de dominio: Replica](../../01-modelo-dominio/objetos/Replica.md) · [Diagrama de clases](../../02-diagrama-clases/README.md)

| Campo | Valor |
|---|---|
| Entidad | Replica |
| Tabla | `replica` |
| Descripción | Intervención en la conversación de un Kudo. Su padre es el Kudo (réplica de primer nivel) u otra réplica (respuesta). |

## Atributos

| Nombre | id_replica | id_autor | id_kudo | id_replica_padre | contenido | fecha_creacion | estado | promedio_calificacion | cantidad_calificaciones |
|---|---|---|---|---|---|---|---|---|---|
| Tipo de dato | uniqueidentifier | uniqueidentifier | uniqueidentifier | uniqueidentifier | nvarchar | datetime2 | varchar | decimal | int |
| Longitud |  |  |  |  | max |  | 11 |  |  |
| Cantidad dígitos parte entera |  |  |  |  |  |  |  | 1 | 10 |
| Cantidad dígitos parte decimal (Precisión) |  |  |  |  |  |  |  | 2 |  |
| ¿Es autoincremento? | NO | NO | NO | NO | NO | NO | NO | NO | NO |
| ¿Es obligatorio? | SI | SI | SI | NO | SI | SI | SI | SI | SI |
| ¿Es llave primaria o es parte de ella? | SI | NO | NO | NO | NO | NO | NO | NO | NO |
| Llave Foránea · ¿Es llave foránea? | NO | SI | SI | SI | NO | NO | NO | NO | NO |
| Llave Foránea · Entidad |  | usuario | kudo | replica |  |  |  |  |  |
| Llave Foránea · Columna(s) |  | id_usuario | id_kudo | id_replica |  |  |  |  |  |
| Restricciones de unicidad · ¿Posee restricción de unicidad o es parte de ella? | NO | NO | NO | NO | NO | NO | NO | NO | NO |
| Restricciones de unicidad · Nombre restricción unicidad a la que pertenece |  |  |  |  |  |  |  |  |  |
| Restricciones de chequeo · ¿Posee restricción de chequeo o es parte de ella? | SI | NO | NO | SI | SI | NO | SI | SI | SI |
| Restricciones de chequeo · Nombre restricción chequeo a la que pertenece | ck_replica_padre |  |  | ck_replica_padre | ck_replica_contenido |  | ck_replica_estado | ck_replica_indicadores | ck_replica_indicadores |
| Restricciones de índice · ¿Posee restricción de índice o es parte de ella? | NO | NO | SI | SI | NO | NO | NO | NO | NO |
| Restricciones de índice · Tipo |  |  | No agrupado | No agrupado |  |  |  |  |  |
| Restricciones de índice · Nombre restricción índice a la que pertenece |  |  | ix_replica_kudo | ix_replica_padre |  |  |  |  |  |
| Valor por defecto | newid() |  |  |  |  | sysdatetime() | 'CREADO' | 0 | 0 |
| Atributo del objeto de dominio | id | autor | kudo | replicaPadre | contenido | fechaCreacion | estado | promedioCalificacion | cantidadCalificaciones |
| Descripción del campo | Identificador único de la réplica | Usuario que escribe la réplica (RN-168) | Kudo raíz de la conversación | Réplica a la que responde (RN-162) | Texto de la réplica | Fecha y hora de publicación (RN-169) | Estado del ciclo de vida del contenido | Promedio de estrellas de sus calificaciones activas | Número de calificaciones activas |

## Restricciones

| Nombre | Tipo | Columnas | Definición |
|---|---|---|---|
| pk_replica | Llave primaria | id_replica | `primary key` |
| fk_replica_id_autor | Llave foránea | id_autor | `foreign key (id_autor) references usuario(id_usuario)` |
| fk_replica_id_kudo | Llave foránea | id_kudo | `foreign key (id_kudo) references kudo(id_kudo)` |
| fk_replica_id_replica_padre | Llave foránea | id_replica_padre | `foreign key (id_replica_padre) references replica(id_replica)` |
| ck_replica_padre | Chequeo | id_replica, id_replica_padre | `check (id_replica_padre is null or id_replica_padre <> id_replica)` |
| ck_replica_contenido | Chequeo | contenido | `check (len(ltrim(rtrim(contenido))) > 0)` |
| ck_replica_estado | Chequeo | estado | `check (estado in ('CREADO', 'VISIBLE', 'REPORTADO', 'EN_REVISION', 'VETADO'))` |
| ck_replica_indicadores | Chequeo | promedio_calificacion, cantidad_calificaciones | `check (promedio_calificacion between 0 and 5 and cantidad_calificaciones >= 0)` |
| ix_replica_kudo | Índice | id_kudo | `(id_kudo, fecha_creacion)` |
| ix_replica_padre | Índice | id_replica_padre | `(id_replica_padre)` |

El script completo está en [`sql/crear-tablas-kudos.sql`](../../../sql/crear-tablas-kudos.sql).
