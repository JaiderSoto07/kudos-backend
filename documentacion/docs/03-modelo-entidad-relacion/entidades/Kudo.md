# Kudo

[Inicio](../../../README.md) · [MER](../README.md) · [Modelo de dominio: Kudo](../../01-modelo-dominio/objetos/Kudo.md) · [Diagrama de clases](../../02-diagrama-clases/README.md)

| Campo | Valor |
|---|---|
| Entidad | Kudo |
| Tabla | `kudo` |
| Descripción | Reconocimiento formal que una persona otorga a otra persona de la comunidad universitaria. Es la raíz de su conversación. |

## Atributos

| Nombre | id_kudo | id_autor | id_destinatario | id_categoria | contenido | fecha_creacion | estado | cantidad_visualizaciones | promedio_calificacion | cantidad_calificaciones |
|---|---|---|---|---|---|---|---|---|---|---|
| Tipo de dato | uniqueidentifier | uniqueidentifier | uniqueidentifier | uniqueidentifier | nvarchar | datetime2 | varchar | int | decimal | int |
| Longitud |  |  |  |  | max |  | 11 |  |  |  |
| Cantidad dígitos parte entera |  |  |  |  |  |  |  | 10 | 1 | 10 |
| Cantidad dígitos parte decimal (Precisión) |  |  |  |  |  |  |  |  | 2 |  |
| ¿Es autoincremento? | NO | NO | NO | NO | NO | NO | NO | NO | NO | NO |
| ¿Es obligatorio? | SI | SI | SI | SI | SI | SI | SI | SI | SI | SI |
| ¿Es llave primaria o es parte de ella? | SI | NO | NO | NO | NO | NO | NO | NO | NO | NO |
| Llave Foránea · ¿Es llave foránea? | NO | SI | SI | SI | NO | NO | NO | NO | NO | NO |
| Llave Foránea · Entidad |  | usuario | usuario | categoria |  |  |  |  |  |  |
| Llave Foránea · Columna(s) |  | id_usuario | id_usuario | id_categoria |  |  |  |  |  |  |
| Restricciones de unicidad · ¿Posee restricción de unicidad o es parte de ella? | NO | NO | NO | NO | NO | NO | NO | NO | NO | NO |
| Restricciones de unicidad · Nombre restricción unicidad a la que pertenece |  |  |  |  |  |  |  |  |  |  |
| Restricciones de chequeo · ¿Posee restricción de chequeo o es parte de ella? | NO | SI | SI | NO | SI | NO | SI | SI | SI | SI |
| Restricciones de chequeo · Nombre restricción chequeo a la que pertenece |  | ck_kudo_autor_destinatario | ck_kudo_autor_destinatario |  | ck_kudo_contenido |  | ck_kudo_estado | ck_kudo_indicadores | ck_kudo_indicadores | ck_kudo_indicadores |
| Restricciones de índice · ¿Posee restricción de índice o es parte de ella? | NO | SI | SI | SI | NO | SI | SI | NO | NO | NO |
| Restricciones de índice · Tipo |  | No agrupado | No agrupado | No agrupado |  | No agrupado | No agrupado |  |  |  |
| Restricciones de índice · Nombre restricción índice a la que pertenece |  | ix_kudo_autor | ix_kudo_destinatario | ix_kudo_categoria |  | ix_kudo_muro | ix_kudo_muro |  |  |  |
| Valor por defecto | newid() |  |  |  |  | sysdatetime() | 'CREADO' | 0 | 0 | 0 |
| Atributo del objeto de dominio | id | autor | destinatario | categoria | contenido | fechaCreacion | estado | cantidadVisualizaciones | promedioCalificacion | cantidadCalificaciones |
| Descripción del campo | Identificador único del Kudo | Usuario que otorga el reconocimiento (RN-147) | Usuario que recibe el reconocimiento (RN-148) | Categoría del reconocimiento (RN-149) | Texto del reconocimiento | Fecha y hora de publicación (RN-150) | Estado del ciclo de vida del contenido | Número de veces que ha sido consultado (RN-229, D-25) | Promedio de estrellas de sus calificaciones activas | Número de calificaciones activas |

## Restricciones

| Nombre | Tipo | Columnas | Definición |
|---|---|---|---|
| pk_kudo | Llave primaria | id_kudo | `primary key` |
| fk_kudo_id_autor | Llave foránea | id_autor | `foreign key (id_autor) references usuario(id_usuario)` |
| fk_kudo_id_destinatario | Llave foránea | id_destinatario | `foreign key (id_destinatario) references usuario(id_usuario)` |
| fk_kudo_id_categoria | Llave foránea | id_categoria | `foreign key (id_categoria) references categoria(id_categoria)` |
| ck_kudo_autor_destinatario | Chequeo | id_autor, id_destinatario | `check (id_autor <> id_destinatario)` |
| ck_kudo_contenido | Chequeo | contenido | `check (len(ltrim(rtrim(contenido))) > 0)` |
| ck_kudo_estado | Chequeo | estado | `check (estado in ('CREADO', 'VISIBLE', 'REPORTADO', 'EN_REVISION', 'VETADO'))` |
| ck_kudo_indicadores | Chequeo | cantidad_visualizaciones, promedio_calificacion, cantidad_calificaciones | `check (cantidad_visualizaciones >= 0 and promedio_calificacion between 0 and 5 and cantidad_calificaciones >= 0)` |
| ix_kudo_muro | Índice | estado, fecha_creacion | `(estado, fecha_creacion desc)` |
| ix_kudo_autor | Índice | id_autor | `(id_autor)` |
| ix_kudo_destinatario | Índice | id_destinatario | `(id_destinatario)` |
| ix_kudo_categoria | Índice | id_categoria | `(id_categoria)` |

El script completo está en [`sql/crear-tablas-kudos.sql`](../../../sql/crear-tablas-kudos.sql).
