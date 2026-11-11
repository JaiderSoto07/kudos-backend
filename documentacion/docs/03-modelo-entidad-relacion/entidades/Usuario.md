# Usuario

[Inicio](../../../README.md) · [MER](../README.md) · [Modelo de dominio: Usuario](../../01-modelo-dominio/objetos/Usuario.md) · [Diagrama de clases](../../02-diagrama-clases/README.md)

| Campo | Valor |
|---|---|
| Entidad | Usuario |
| Tabla | `usuario` |
| Descripción | Persona de la comunidad universitaria que tiene una cuenta en la plataforma. Su rol institucional vigente se conoce por su AsignacionRol vigente. |

## Atributos

| Nombre | id_usuario | nombres | apellidos | correo_institucional | correo_verificado | numero_celular | celular_verificado | aprobada | estado_cuenta | fecha_registro |
|---|---|---|---|---|---|---|---|---|---|---|
| Tipo de dato | uniqueidentifier | nvarchar | nvarchar | nvarchar | bit | nvarchar | bit | bit | varchar | datetime2 |
| Longitud |  | 60 | 60 | 100 |  | 10 |  |  | 22 |  |
| Cantidad dígitos parte entera |  |  |  |  |  |  |  |  |  |  |
| Cantidad dígitos parte decimal (Precisión) |  |  |  |  |  |  |  |  |  |  |
| ¿Es autoincremento? | NO | NO | NO | NO | NO | NO | NO | NO | NO | NO |
| ¿Es obligatorio? | SI | SI | SI | SI | SI | SI | SI | SI | SI | SI |
| ¿Es llave primaria o es parte de ella? | SI | NO | NO | NO | NO | NO | NO | NO | NO | NO |
| Llave Foránea · ¿Es llave foránea? | NO | NO | NO | NO | NO | NO | NO | NO | NO | NO |
| Llave Foránea · Entidad |  |  |  |  |  |  |  |  |  |  |
| Llave Foránea · Columna(s) |  |  |  |  |  |  |  |  |  |  |
| Restricciones de unicidad · ¿Posee restricción de unicidad o es parte de ella? | NO | NO | NO | SI | NO | SI | NO | NO | NO | NO |
| Restricciones de unicidad · Nombre restricción unicidad a la que pertenece |  |  |  | uk_usuario_correo_institucional |  | uk_usuario_numero_celular |  |  |  |  |
| Restricciones de chequeo · ¿Posee restricción de chequeo o es parte de ella? | NO | NO | NO | SI | SI | SI | SI | SI | SI | NO |
| Restricciones de chequeo · Nombre restricción chequeo a la que pertenece |  |  |  | ck_usuario_correo_institucional | ck_usuario_cuenta_activa | ck_usuario_numero_celular | ck_usuario_cuenta_activa | ck_usuario_cuenta_activa | ck_usuario_estado_cuenta<br>ck_usuario_cuenta_activa |  |
| Restricciones de índice · ¿Posee restricción de índice o es parte de ella? | NO | NO | NO | NO | NO | NO | NO | NO | NO | NO |
| Restricciones de índice · Tipo |  |  |  |  |  |  |  |  |  |  |
| Restricciones de índice · Nombre restricción índice a la que pertenece |  |  |  |  |  |  |  |  |  |  |
| Valor por defecto | newid() |  |  |  | 0 |  | 0 | 0 | 'PENDIENTE_APROBACION' | sysdatetime() |
| Atributo del objeto de dominio | id | nombres | apellidos | correoInstitucional | correoVerificado | numeroCelular | celularVerificado | aprobada | estadoCuenta | fechaRegistro |
| Descripción del campo | Identificador único del usuario | Nombres de la persona | Apellidos de la persona | Correo de la cuenta de Google con la que se registra e ingresa | Indica si el correo fue validado al ingresar con Google | Número celular de contacto | Indica si el celular fue validado con un código | Indica si la autoridad correspondiente aprobó la cuenta | Estado del ciclo de vida de la cuenta | Fecha y hora en que se creó la cuenta |

## Restricciones

| Nombre | Tipo | Columnas | Definición |
|---|---|---|---|
| pk_usuario | Llave primaria | id_usuario | `primary key` |
| uk_usuario_correo_institucional | Unicidad | correo_institucional | `unique (correo_institucional)` |
| uk_usuario_numero_celular | Unicidad | numero_celular | `unique (numero_celular)` |
| ck_usuario_correo_institucional | Chequeo | correo_institucional | `check (correo_institucional like '_%@_%._%' and correo_institucional = lower(correo_institucional))` |
| ck_usuario_numero_celular | Chequeo | numero_celular | `check (len(numero_celular) = 10 and numero_celular like '3%' and numero_celular not like '%[^0-9]%')` |
| ck_usuario_estado_cuenta | Chequeo | estado_cuenta | `check (estado_cuenta in ('PENDIENTE_APROBACION', 'PENDIENTE_VERIFICACION', 'ACTIVA', 'SUSPENDIDA', 'DESACTIVADA'))` |
| ck_usuario_cuenta_activa | Chequeo | estado_cuenta, aprobada, correo_verificado, celular_verificado | `check (estado_cuenta <> 'ACTIVA' or (aprobada = 1 and correo_verificado = 1 and celular_verificado = 1))` |

El script completo está en [`sql/crear-tablas-kudos.sql`](../../../sql/crear-tablas-kudos.sql).
