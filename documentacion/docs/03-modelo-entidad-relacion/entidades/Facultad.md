# Facultad

[Inicio](../../../README.md) · [MER](../README.md) · [Modelo de dominio: Facultad](../../01-modelo-dominio/objetos/Facultad.md) · [Diagrama de clases](../../02-diagrama-clases/README.md)

| Campo | Valor |
|---|---|
| Entidad | Facultad |
| Tabla | `facultad` |
| Descripción | Unidad académica de la Universidad que agrupa Programas Académicos y es acompañada por un Coordinador de Facultad. |

## Atributos

| Nombre | id_facultad | nombre | estado | fecha_creacion |
|---|---|---|---|---|
| Tipo de dato | uniqueidentifier | nvarchar | varchar | datetime2 |
| Longitud |  | 80 | 15 |  |
| Cantidad dígitos parte entera |  |  |  |  |
| Cantidad dígitos parte decimal (Precisión) |  |  |  |  |
| ¿Es autoincremento? | NO | NO | NO | NO |
| ¿Es obligatorio? | SI | SI | SI | SI |
| ¿Es llave primaria o es parte de ella? | SI | NO | NO | NO |
| Llave Foránea · ¿Es llave foránea? | NO | NO | NO | NO |
| Llave Foránea · Entidad |  |  |  |  |
| Llave Foránea · Columna(s) |  |  |  |  |
| Restricciones de unicidad · ¿Posee restricción de unicidad o es parte de ella? | NO | SI | NO | NO |
| Restricciones de unicidad · Nombre restricción unicidad a la que pertenece |  | uk_facultad_nombre |  |  |
| Restricciones de chequeo · ¿Posee restricción de chequeo o es parte de ella? | NO | SI | SI | NO |
| Restricciones de chequeo · Nombre restricción chequeo a la que pertenece |  | ck_facultad_nombre | ck_facultad_estado |  |
| Restricciones de índice · ¿Posee restricción de índice o es parte de ella? | NO | NO | NO | NO |
| Restricciones de índice · Tipo |  |  |  |  |
| Restricciones de índice · Nombre restricción índice a la que pertenece |  |  |  |  |
| Valor por defecto | newid() |  | 'EN_CONFORMACION' | sysdatetime() |
| Atributo del objeto de dominio | id | nombre | estado | fechaCreacion |
| Descripción del campo | Identificador único de la Facultad (RN-039) | Nombre oficial de la Facultad (RN-040) | Estado de la Facultad | Fecha y hora de creación |

## Restricciones

| Nombre | Tipo | Columnas | Definición |
|---|---|---|---|
| pk_facultad | Llave primaria | id_facultad | `primary key` |
| uk_facultad_nombre | Unicidad | nombre | `unique (nombre)` |
| ck_facultad_nombre | Chequeo | nombre | `check (len(nombre) >= 3)` |
| ck_facultad_estado | Chequeo | estado | `check (estado in ('EN_CONFORMACION', 'ACTIVA', 'INACTIVA'))` |

El script completo está en [`sql/crear-tablas-kudos.sql`](../../../sql/crear-tablas-kudos.sql).
