# ProgramaAcademico

[Inicio](../../../README.md) · [MER](../README.md) · [Modelo de dominio: ProgramaAcademico](../../01-modelo-dominio/objetos/ProgramaAcademico.md) · [Diagrama de clases](../../02-diagrama-clases/README.md)

| Campo | Valor |
|---|---|
| Entidad | ProgramaAcademico |
| Tabla | `programa_academico` |
| Descripción | Programa de formación que pertenece a exactamente una Facultad y es acompañado por un Coordinador de Programa. |

## Atributos

| Nombre | id_programa_academico | nombre | id_facultad | activo | fecha_creacion |
|---|---|---|---|---|---|
| Tipo de dato | uniqueidentifier | nvarchar | uniqueidentifier | bit | datetime2 |
| Longitud |  | 100 |  |  |  |
| Cantidad dígitos parte entera |  |  |  |  |  |
| Cantidad dígitos parte decimal (Precisión) |  |  |  |  |  |
| ¿Es autoincremento? | NO | NO | NO | NO | NO |
| ¿Es obligatorio? | SI | SI | SI | SI | SI |
| ¿Es llave primaria o es parte de ella? | SI | NO | NO | NO | NO |
| Llave Foránea · ¿Es llave foránea? | NO | NO | SI | NO | NO |
| Llave Foránea · Entidad |  |  | facultad |  |  |
| Llave Foránea · Columna(s) |  |  | id_facultad |  |  |
| Restricciones de unicidad · ¿Posee restricción de unicidad o es parte de ella? | NO | SI | NO | NO | NO |
| Restricciones de unicidad · Nombre restricción unicidad a la que pertenece |  | uk_programa_academico_nombre |  |  |  |
| Restricciones de chequeo · ¿Posee restricción de chequeo o es parte de ella? | NO | SI | NO | NO | NO |
| Restricciones de chequeo · Nombre restricción chequeo a la que pertenece |  | ck_programa_academico_nombre |  |  |  |
| Restricciones de índice · ¿Posee restricción de índice o es parte de ella? | NO | NO | SI | NO | NO |
| Restricciones de índice · Tipo |  |  | No agrupado |  |  |
| Restricciones de índice · Nombre restricción índice a la que pertenece |  |  | ix_programa_academico_facultad |  |  |
| Valor por defecto | newid() |  |  | 1 | sysdatetime() |
| Atributo del objeto de dominio | id | nombre | facultad | activo | fechaCreacion |
| Descripción del campo | Identificador único del Programa (RN-047) | Nombre oficial del Programa (RN-048) | Facultad a la que pertenece (RN-046, RN-049) | Indica si el Programa está activo | Fecha y hora de creación |

## Restricciones

| Nombre | Tipo | Columnas | Definición |
|---|---|---|---|
| pk_programa_academico | Llave primaria | id_programa_academico | `primary key` |
| fk_programa_academico_id_facultad | Llave foránea | id_facultad | `foreign key (id_facultad) references facultad(id_facultad)` |
| uk_programa_academico_nombre | Unicidad | nombre | `unique (nombre)` |
| ck_programa_academico_nombre | Chequeo | nombre | `check (len(nombre) >= 3)` |
| ix_programa_academico_facultad | Índice | id_facultad | `(id_facultad)` |

El script completo está en [`sql/crear-tablas-kudos.sql`](../../../sql/crear-tablas-kudos.sql).
