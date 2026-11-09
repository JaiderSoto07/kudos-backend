# VinculacionPrograma

[Inicio](../../../README.md) · [MER](../README.md) · [Modelo de dominio: VinculacionPrograma](../../01-modelo-dominio/objetos/VinculacionPrograma.md) · [Diagrama de clases](../../02-diagrama-clases/README.md)

| Campo | Valor |
|---|---|
| Entidad | VinculacionPrograma |
| Tabla | `vinculacion_programa` |
| Descripción | Vínculo, con historial, entre un Docente o Estudiante y un Programa Académico. Resuelve la relación muchos a muchos y conserva la trazabilidad de vinculaciones y desvinculaciones. |

## Atributos

| Nombre | id_vinculacion_programa | id_usuario | id_programa | tipo_vinculo | fecha_inicio | fecha_fin | motivo_desvinculacion |
|---|---|---|---|---|---|---|---|
| Tipo de dato | uniqueidentifier | uniqueidentifier | uniqueidentifier | varchar | datetime2 | datetime2 | nvarchar |
| Longitud |  |  |  | 10 |  |  | 250 |
| Cantidad dígitos parte entera |  |  |  |  |  |  |  |
| Cantidad dígitos parte decimal (Precisión) |  |  |  |  |  |  |  |
| ¿Es autoincremento? | NO | NO | NO | NO | NO | NO | NO |
| ¿Es obligatorio? | SI | SI | SI | SI | SI | NO | NO |
| ¿Es llave primaria o es parte de ella? | SI | NO | NO | NO | NO | NO | NO |
| Llave Foránea · ¿Es llave foránea? | NO | SI | SI | NO | NO | NO | NO |
| Llave Foránea · Entidad |  | usuario | programa_academico |  |  |  |  |
| Llave Foránea · Columna(s) |  | id_usuario | id_programa_academico |  |  |  |  |
| Restricciones de unicidad · ¿Posee restricción de unicidad o es parte de ella? | NO | SI | SI | NO | NO | NO | NO |
| Restricciones de unicidad · Nombre restricción unicidad a la que pertenece |  | ux_vinculacion_programa_vigente | ux_vinculacion_programa_vigente |  |  |  |  |
| Restricciones de chequeo · ¿Posee restricción de chequeo o es parte de ella? | NO | NO | NO | SI | SI | SI | SI |
| Restricciones de chequeo · Nombre restricción chequeo a la que pertenece |  |  |  | ck_vinculacion_programa_tipo_vinculo | ck_vinculacion_programa_cierre | ck_vinculacion_programa_cierre | ck_vinculacion_programa_cierre |
| Restricciones de índice · ¿Posee restricción de índice o es parte de ella? | NO | SI | SI | NO | NO | NO | NO |
| Restricciones de índice · Tipo |  | Único filtrado | No agrupado<br>Único filtrado |  |  |  |  |
| Restricciones de índice · Nombre restricción índice a la que pertenece |  | ux_vinculacion_programa_vigente | ix_vinculacion_programa_programa<br>ux_vinculacion_programa_vigente |  |  |  |  |
| Valor por defecto | newid() |  |  |  | sysdatetime() |  |  |
| Atributo del objeto de dominio | id | usuario | programa | tipoVinculo | fechaInicio | fechaFin | motivoDesvinculacion |
| Descripción del campo | Identificador único del vínculo | Docente o Estudiante vinculado | Programa al que se vincula | Calidad en la que participa en el programa | Fecha y hora de vinculación | Fecha y hora de desvinculación | Razón de la desvinculación |

## Restricciones

| Nombre | Tipo | Columnas | Definición |
|---|---|---|---|
| pk_vinculacion_programa | Llave primaria | id_vinculacion_programa | `primary key` |
| fk_vinculacion_programa_id_usuario | Llave foránea | id_usuario | `foreign key (id_usuario) references usuario(id_usuario)` |
| fk_vinculacion_programa_id_programa | Llave foránea | id_programa | `foreign key (id_programa) references programa_academico(id_programa_academico)` |
| ck_vinculacion_programa_tipo_vinculo | Chequeo | tipo_vinculo | `check (tipo_vinculo in ('DOCENTE', 'ESTUDIANTE'))` |
| ck_vinculacion_programa_cierre | Chequeo | fecha_inicio, fecha_fin, motivo_desvinculacion | `check ((fecha_fin is null and motivo_desvinculacion is null) or (fecha_fin is not null and fecha_fin >= fecha_inicio and motivo_desvinculacion is not null))` |
| ux_vinculacion_programa_vigente | Índice único filtrado | id_usuario, id_programa | `(id_usuario, id_programa) where fecha_fin is null` |
| ix_vinculacion_programa_programa | Índice | id_programa | `(id_programa)` |

El script completo está en [`sql/crear-tablas-kudos.sql`](../../../sql/crear-tablas-kudos.sql).
