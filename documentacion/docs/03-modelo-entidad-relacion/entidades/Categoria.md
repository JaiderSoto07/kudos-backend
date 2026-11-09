# Categoria

[Inicio](../../../README.md) · [MER](../README.md) · [Modelo de dominio: Categoria](../../01-modelo-dominio/objetos/Categoria.md) · [Diagrama de clases](../../02-diagrama-clases/README.md)

| Campo | Valor |
|---|---|
| Entidad | Categoria |
| Tabla | `categoria` |
| Descripción | Clasificación institucional de los reconocimientos (RN-132). Puede estar activa o inactiva; solo las activas se usan para Kudos nuevos. |

## Atributos

| Nombre | id_categoria | nombre | descripcion | activa | fecha_creacion |
|---|---|---|---|---|---|
| Tipo de dato | uniqueidentifier | nvarchar | nvarchar | bit | datetime2 |
| Longitud |  | 50 | 250 |  |  |
| Cantidad dígitos parte entera |  |  |  |  |  |
| Cantidad dígitos parte decimal (Precisión) |  |  |  |  |  |
| ¿Es autoincremento? | NO | NO | NO | NO | NO |
| ¿Es obligatorio? | SI | SI | SI | SI | SI |
| ¿Es llave primaria o es parte de ella? | SI | NO | NO | NO | NO |
| Llave Foránea · ¿Es llave foránea? | NO | NO | NO | NO | NO |
| Llave Foránea · Entidad |  |  |  |  |  |
| Llave Foránea · Columna(s) |  |  |  |  |  |
| Restricciones de unicidad · ¿Posee restricción de unicidad o es parte de ella? | NO | SI | NO | NO | NO |
| Restricciones de unicidad · Nombre restricción unicidad a la que pertenece |  | uk_categoria_nombre |  |  |  |
| Restricciones de chequeo · ¿Posee restricción de chequeo o es parte de ella? | NO | SI | SI | NO | NO |
| Restricciones de chequeo · Nombre restricción chequeo a la que pertenece |  | ck_categoria_textos | ck_categoria_textos |  |  |
| Restricciones de índice · ¿Posee restricción de índice o es parte de ella? | NO | NO | NO | NO | NO |
| Restricciones de índice · Tipo |  |  |  |  |  |
| Restricciones de índice · Nombre restricción índice a la que pertenece |  |  |  |  |  |
| Valor por defecto | newid() |  |  | 1 | sysdatetime() |
| Atributo del objeto de dominio | id | nombre | descripcion | activa | fechaCreacion |
| Descripción del campo | Identificador único de la categoría | Nombre de la categoría | Qué tipo de comportamiento o logro reconoce | Indica si puede usarse para Kudos nuevos (RN-133, RN-144) | Fecha y hora de creación |

## Restricciones

| Nombre | Tipo | Columnas | Definición |
|---|---|---|---|
| pk_categoria | Llave primaria | id_categoria | `primary key` |
| uk_categoria_nombre | Unicidad | nombre | `unique (nombre)` |
| ck_categoria_textos | Chequeo | nombre, descripcion | `check (len(nombre) >= 3 and len(descripcion) >= 10)` |

El script completo está en [`sql/crear-tablas-kudos.sql`](../../../sql/crear-tablas-kudos.sql).
