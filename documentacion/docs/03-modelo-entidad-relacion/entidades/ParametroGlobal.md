# ParametroGlobal

[Inicio](../../../README.md) · [MER](../README.md) · [Modelo de dominio: ParametroGlobal](../../01-modelo-dominio/objetos/ParametroGlobal.md) · [Diagrama de clases](../../02-diagrama-clases/README.md)

| Campo | Valor |
|---|---|
| Entidad | ParametroGlobal |
| Tabla | `parametro_global` |
| Descripción | Valor de configuración de la plataforma que administran los Administradores (RN-130, D-11). |

## Atributos

| Nombre | id_parametro_global | clave | valor | tipo_dato | descripcion | fecha_modificacion |
|---|---|---|---|---|---|---|
| Tipo de dato | uniqueidentifier | varchar | nvarchar | varchar | nvarchar | datetime2 |
| Longitud |  | 50 | 250 | 6 | 250 |  |
| Cantidad dígitos parte entera |  |  |  |  |  |  |
| Cantidad dígitos parte decimal (Precisión) |  |  |  |  |  |  |
| ¿Es autoincremento? | NO | NO | NO | NO | NO | NO |
| ¿Es obligatorio? | SI | SI | SI | SI | SI | SI |
| ¿Es llave primaria o es parte de ella? | SI | NO | NO | NO | NO | NO |
| Llave Foránea · ¿Es llave foránea? | NO | NO | NO | NO | NO | NO |
| Llave Foránea · Entidad |  |  |  |  |  |  |
| Llave Foránea · Columna(s) |  |  |  |  |  |  |
| Restricciones de unicidad · ¿Posee restricción de unicidad o es parte de ella? | NO | SI | NO | NO | NO | NO |
| Restricciones de unicidad · Nombre restricción unicidad a la que pertenece |  | uk_parametro_global_clave |  |  |  |  |
| Restricciones de chequeo · ¿Posee restricción de chequeo o es parte de ella? | NO | NO | SI | SI | NO | NO |
| Restricciones de chequeo · Nombre restricción chequeo a la que pertenece |  |  | ck_parametro_global_valor_entero | ck_parametro_global_tipo_dato<br>ck_parametro_global_valor_entero |  |  |
| Restricciones de índice · ¿Posee restricción de índice o es parte de ella? | NO | NO | NO | NO | NO | NO |
| Restricciones de índice · Tipo |  |  |  |  |  |  |
| Restricciones de índice · Nombre restricción índice a la que pertenece |  |  |  |  |  |  |
| Valor por defecto | newid() |  |  |  |  | sysdatetime() |
| Atributo del objeto de dominio | id | clave | valor | tipoDato | descripcion | fechaModificacion |
| Descripción del campo | Identificador único del parámetro | Nombre técnico del parámetro | Valor vigente del parámetro | Tipo de dato del valor | Para qué sirve el parámetro | Fecha y hora del último cambio |

## Restricciones

| Nombre | Tipo | Columnas | Definición |
|---|---|---|---|
| pk_parametro_global | Llave primaria | id_parametro_global | `primary key` |
| uk_parametro_global_clave | Unicidad | clave | `unique (clave)` |
| ck_parametro_global_tipo_dato | Chequeo | tipo_dato | `check (tipo_dato in ('TEXTO', 'ENTERO'))` |
| ck_parametro_global_valor_entero | Chequeo | tipo_dato, valor | `check (tipo_dato <> 'ENTERO' or valor not like '%[^0-9]%')` |

El script completo está en [`sql/crear-tablas-kudos.sql`](../../../sql/crear-tablas-kudos.sql).
