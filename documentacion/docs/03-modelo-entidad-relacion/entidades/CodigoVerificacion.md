# CodigoVerificacion

[Inicio](../../../README.md) · [MER](../README.md) · [Modelo de dominio: CodigoVerificacion](../../01-modelo-dominio/objetos/CodigoVerificacion.md) · [Diagrama de clases](../../02-diagrama-clases/README.md)

| Campo | Valor |
|---|---|
| Entidad | CodigoVerificacion |
| Tabla | `codigo_verificacion` |
| Descripción | Código temporal que se envía al correo o al celular de un usuario para validar que le pertenece. |

## Atributos

| Nombre | id_codigo_verificacion | id_usuario | canal | destino | codigo | fecha_generacion | fecha_expiracion | intentos_fallidos | usado |
|---|---|---|---|---|---|---|---|---|---|
| Tipo de dato | uniqueidentifier | uniqueidentifier | varchar | varchar | varchar | datetime2 | datetime2 | int | bit |
| Longitud |  |  | 7 | 100 | 60 |  |  |  |  |
| Cantidad dígitos parte entera |  |  |  |  |  |  |  | 10 |  |
| Cantidad dígitos parte decimal (Precisión) |  |  |  |  |  |  |  |  |  |
| ¿Es autoincremento? | NO | NO | NO | NO | NO | NO | NO | NO | NO |
| ¿Es obligatorio? | SI | SI | SI | SI | SI | SI | SI | SI | SI |
| ¿Es llave primaria o es parte de ella? | SI | NO | NO | NO | NO | NO | NO | NO | NO |
| Llave Foránea · ¿Es llave foránea? | NO | SI | NO | NO | NO | NO | NO | NO | NO |
| Llave Foránea · Entidad |  | usuario |  |  |  |  |  |  |  |
| Llave Foránea · Columna(s) |  | id_usuario |  |  |  |  |  |  |  |
| Restricciones de unicidad · ¿Posee restricción de unicidad o es parte de ella? | NO | NO | NO | NO | NO | NO | NO | NO | NO |
| Restricciones de unicidad · Nombre restricción unicidad a la que pertenece |  |  |  |  |  |  |  |  |  |
| Restricciones de chequeo · ¿Posee restricción de chequeo o es parte de ella? | NO | NO | SI | NO | NO | SI | SI | SI | NO |
| Restricciones de chequeo · Nombre restricción chequeo a la que pertenece |  |  | ck_codigo_verificacion_canal |  |  | ck_codigo_verificacion_vigencia | ck_codigo_verificacion_vigencia | ck_codigo_verificacion_intentos |  |
| Restricciones de índice · ¿Posee restricción de índice o es parte de ella? | NO | SI | SI | NO | NO | NO | NO | NO | NO |
| Restricciones de índice · Tipo |  | No agrupado | No agrupado |  |  |  |  |  |  |
| Restricciones de índice · Nombre restricción índice a la que pertenece |  | ix_codigo_verificacion_usuario_canal | ix_codigo_verificacion_usuario_canal |  |  |  |  |  |  |
| Valor por defecto | newid() |  |  |  |  | sysdatetime() |  | 0 | 0 |
| Atributo del objeto de dominio | id | usuario | canal | destino | codigo | fechaGeneracion | fechaExpiracion | intentosFallidos | usado |
| Descripción del campo | Identificador único del código | Usuario que debe verificar el dato | Dato que se verifica | Valor exacto al que se envió el código | Código de 6 dígitos generado con SecureRandom | Fecha y hora de generación | Fecha y hora en que vence | Número de intentos con código incorrecto | Indica si el código ya se usó con éxito |

## Restricciones

| Nombre | Tipo | Columnas | Definición |
|---|---|---|---|
| pk_codigo_verificacion | Llave primaria | id_codigo_verificacion | `primary key` |
| fk_codigo_verificacion_id_usuario | Llave foránea | id_usuario | `foreign key (id_usuario) references usuario(id_usuario)` |
| ck_codigo_verificacion_canal | Chequeo | canal | `check (canal in ('CORREO', 'CELULAR'))` |
| ck_codigo_verificacion_vigencia | Chequeo | fecha_generacion, fecha_expiracion | `check (fecha_expiracion > fecha_generacion)` |
| ck_codigo_verificacion_intentos | Chequeo | intentos_fallidos | `check (intentos_fallidos >= 0)` |
| ix_codigo_verificacion_usuario_canal | Índice | id_usuario, canal | `(id_usuario, canal)` |

El script completo está en [`sql/crear-tablas-kudos.sql`](../../../sql/crear-tablas-kudos.sql).
