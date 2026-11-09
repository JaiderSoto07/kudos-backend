# Usuario

[Inicio](../../../README.md) · [Diagrama de clases](../README.md) · [Modelo de dominio: Usuario](../../01-modelo-dominio/objetos/Usuario.md) · [Entidad MER: Usuario](../../03-modelo-entidad-relacion/entidades/Usuario.md)

| Campo | Valor |
|---|---|
| Clase | Usuario |
| Estereotipo | class (entidad de dominio) |
| Descripción | Persona de la comunidad universitaria que tiene una cuenta en la plataforma. Su rol institucional vigente se conoce por su AsignacionRol vigente. |
| Modificador acceso | public |
| Tipo clase | final |
| Paquete | `co.edu.uco.kudos.dominio` |

## Relaciones

| Relación | Tipo relación | Estructura |
|---|---|---|
| Es usada por | Composición | AsignacionRol |
| Es usada por | Asociación | VinculacionPrograma |
| Es usada por | Asociación | SolicitudRegistro |
| Es usada por | Composición | CodigoVerificacion |
| Es usada por | Asociación | Kudo |
| Es usada por | Asociación | Replica |
| Es usada por | Asociación | Calificacion |
| Es usada por | Asociación | Reporte |
| Es usada por | Asociación | Moderacion |
| Es usada por | Asociación | Auditoria |
| Es usada por | Asociación | Notificacion |
| Necesita de | Composición (objeto de valor) | CorreoInstitucional |
| Necesita de | Composición (objeto de valor) | NumeroCelular |
| Necesita de | Dependencia (enumeración) | EstadoCuenta |

## Atributos

| Modificador de acceso | Nombre | Tipo de dato | Tipo de dato · ¿Cuál? | Tipo modificación atributo | Tipo acceso atributo | Valor por defecto | Descripción atributo |
|---|---|---|---|---|---|---|---|
| private | id | Otro | UUID | final | no static | 00000000-0000-0000-0000-000000000000 | Identificador único del usuario |
| private | nombres | String |  | no final | no static | "" (cadena vacía) | Nombres de la persona |
| private | apellidos | String |  | no final | no static | "" (cadena vacía) | Apellidos de la persona |
| private | correoInstitucional | Otro | CorreoInstitucional | no final | no static | Instancia vacía de CorreoInstitucional | Correo institucional con el que se registra e ingresa |
| private | correoVerificado | boolean/Boolean |  | no final | no static | false | Indica si el correo institucional fue validado con un código |
| private | numeroCelular | Otro | NumeroCelular | no final | no static | Instancia vacía de NumeroCelular | Número celular de contacto |
| private | celularVerificado | boolean/Boolean |  | no final | no static | false | Indica si el celular fue validado con un código |
| private | contrasena | String |  | no final | no static | "" (cadena vacía) | Hash de la contraseña de acceso |
| private | aprobada | boolean/Boolean |  | no final | no static | false | Indica si la autoridad correspondiente aprobó la cuenta |
| private | estadoCuenta | Otro | EstadoCuenta | no final | no static | PENDIENTE_APROBACION | Estado del ciclo de vida de la cuenta |
| private | fechaRegistro | Otro | LocalDateTime | final | no static | Fecha actual | Fecha y hora en que se creó la cuenta |

## Constructores

| Modificador de acceso | Nombre | Tipo constructor | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Parámetros · ¿Cuál? | Parámetros · Tipo parámetro | Parámetros · Descripción | Excepciones |
|---|---|---|---|---|---|---|---|---|---|
| private | Usuario | Parametrizado | Constructor privado: crea la instancia copiando los valores ya saneados que reunió el Builder. Solo se invoca desde `Builder.build()`, así nadie crea un objeto a medio construir. | builder | Otro | Builder | Por valor | Builder con los valores de los atributos. | |

## Métodos accesores

Solo hay métodos de lectura (getters). No hay setters públicos: el estado cambia únicamente a través de los métodos de negocio, que validan las reglas (ARQ-007, ARQ-008).

| Modificador de acceso | Nombre | Tipo método accesor | Descripción | Tipo dato de retorno · Tipo de dato | Tipo dato de retorno · ¿Cuál? |
|---|---|---|---|---|---|
| public | getId | getter | Obtiene el valor del atributo id. | Otro | UUID |
| public | getNombres | getter | Obtiene el valor del atributo nombres. | String |  |
| public | getApellidos | getter | Obtiene el valor del atributo apellidos. | String |  |
| public | getCorreoInstitucional | getter | Obtiene el valor del atributo correoInstitucional. | Otro | CorreoInstitucional |
| public | isCorreoVerificado | getter | Obtiene el valor del atributo correoVerificado. | boolean/Boolean |  |
| public | getNumeroCelular | getter | Obtiene el valor del atributo numeroCelular. | Otro | NumeroCelular |
| public | isCelularVerificado | getter | Obtiene el valor del atributo celularVerificado. | boolean/Boolean |  |
| public | getContrasena | getter | Obtiene el valor del atributo contrasena. | String |  |
| public | isAprobada | getter | Obtiene el valor del atributo aprobada. | boolean/Boolean |  |
| public | getEstadoCuenta | getter | Obtiene el valor del atributo estadoCuenta. | Otro | EstadoCuenta |
| public | getFechaRegistro | getter | Obtiene el valor del atributo fechaRegistro. | Otro | LocalDateTime |

## Métodos

Métodos de negocio: salen de las responsabilidades del [modelo enriquecido](../../01-modelo-dominio/objetos/Usuario.md#responsabilidades). Las consultas se marcan `static`, como en Sigma: en la implementación las resuelve la capa de negocio con su DAO.

| Modificador de acceso | Nombre | Tipo acceso método | Tipo modificación método | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Parámetros · Descripción | Tipo dato de retorno | Excepciones (políticas que puede incumplir) |
|---|---|---|---|---|---|---|---|---|---|
| public | registrar | no static | final | Crea la cuenta de una persona que se autorregistra con su identidad institucional. | datos | Usuario | Nombres, apellidos, correo, celular y contraseña. | Usuario | P-USU-001: Los nombres o apellidos están vacíos o superan 60 caracteres.<br>P-USU-002: El correo no tiene formato válido o no es institucional.<br>P-USU-003: Ya existe un usuario con ese correo.<br>P-USU-004: El celular no tiene el formato esperado.<br>P-USU-005: Ya existe un usuario con ese celular.<br>P-USU-006: La contraseña es débil.<br>P-USU-007: No fue posible generar o enviar los códigos.<br>P-USU-008: El identificador generado ya existe. |
| public | registrarPorAdministrador | no static | final | Un Administrador registra directamente a un Rector o a otro Administrador, que queda aprobado. | datos | Usuario | Datos personales del nuevo usuario. | Usuario | P-USU-001: Los nombres o apellidos están vacíos o superan 60 caracteres.<br>P-USU-002: El correo no tiene formato válido o no es institucional.<br>P-USU-003: Ya existe un usuario con ese correo.<br>P-USU-004: El celular no tiene el formato esperado.<br>P-USU-005: Ya existe un usuario con ese celular.<br>P-USU-006: La contraseña es débil.<br>P-USU-009: El actor no es Administrador.<br>P-USU-010: Ya existe un Rector vigente. |
|  |  |  |  |  | rol | Rol | RECTOR o ADMINISTRADOR. |  |  |
|  |  |  |  |  | actor | Usuario | Administrador que registra. |  |  |
| public | verificarCorreo | no static | final | Marca el correo como verificado con el código recibido. | codigo | String | Código de 6 dígitos recibido en el correo. | Usuario | P-USU-012: El código es incorrecto, está vencido o fue invalidado. |
| public | verificarCelular | no static | final | Marca el celular como verificado con el código recibido. | codigo | String | Código de 6 dígitos recibido por SMS. | Usuario | P-USU-012: El código es incorrecto, está vencido o fue invalidado. |
| public | aprobar | no static | final | Registra que la autoridad competente aprobó la cuenta. |  |  |  | Usuario | P-USU-014: La cuenta ya fue aprobada o está desactivada. |
| public | intentarActivar | no static | final | Pasa la cuenta a ACTIVA si está aprobada y tiene correo y celular verificados. |  |  |  | boolean | P-USU-015: Falta alguna de las tres condiciones. |
| public | puedeIniciarSesion | no static | final | Decide si la persona puede ingresar con su correo y contraseña. | correo | CorreoInstitucional | Correo institucional. | ResultadoIngreso | P-USU-016: Correo o contraseña incorrectos.<br>P-USU-017: La cuenta no está activa. |
|  |  |  |  |  | contrasena | String | Contraseña digitada. |  |  |
| public | actualizarDatosPersonales | no static | final | Modifica los nombres y apellidos del propio usuario. | datos | Usuario | Nuevos nombres y apellidos. | Usuario | P-USU-001: Los nombres o apellidos están vacíos o superan 60 caracteres.<br>P-USU-018: Se intenta cambiar el rol desde la información personal. |
| public | cambiarCorreo | no static | final | Cambia el correo institucional y exige verificarlo de nuevo. | correo | CorreoInstitucional | Nuevo correo institucional. | Usuario | P-USU-002: El correo no tiene formato válido o no es institucional.<br>P-USU-003: Ya existe un usuario con ese correo. |
| public | cambiarCelular | no static | final | Cambia el número celular y exige verificarlo de nuevo. | celular | NumeroCelular | Nuevo número celular. | Usuario | P-USU-004: El celular no tiene el formato esperado.<br>P-USU-005: Ya existe un usuario con ese celular. |
| public | suspender | no static | final | Bloquea temporalmente la cuenta de un usuario del ámbito del actor. | actor | Usuario | Autoridad que suspende. | Usuario | P-USU-022: El usuario no pertenece al ámbito del actor.<br>P-USU-023: La cuenta ya está suspendida o desactivada, o falta el motivo.<br>P-USU-024: El actor es el mismo usuario. |
|  |  |  |  |  | motivo | String | Razón de la suspensión. |  |  |
| public | reactivar | no static | final | Levanta la suspensión de una cuenta. | actor | Usuario | Autoridad que reactiva. | Usuario | P-USU-022: El usuario no pertenece al ámbito del actor.<br>P-USU-025: La cuenta no está suspendida. |
|  |  |  |  |  | motivo | String | Razón de la reactivación. |  |  |
| public | desactivar | no static | final | Cierra definitivamente la cuenta para su rol, conservando su historial. | actor | Usuario | Autoridad que desactiva. | Usuario | P-USU-022: El usuario no pertenece al ámbito del actor.<br>P-USU-024: El actor es el mismo usuario.<br>P-USU-026: La cuenta ya está desactivada o falta el motivo. |
|  |  |  |  |  | motivo | String | Razón de la desactivación. |  |  |
| public | tieneRolVigente | no static | final | Indica si el usuario ejerce hoy un rol dado; lo usan las políticas de autorización. | rol | Rol | Rol que se verifica. | boolean |  |
| public | consultar | static | final | Muestra los datos del usuario, su rol, sus vínculos y el estado de sus validaciones. | id | UUID | Identificador del usuario. | Usuario | P-USU-027: El actor no tiene permiso sobre ese usuario. |

## Builder (clase interna public static)

Patrón **Builder**, igual que en Sigma: reúne los valores, los sanea (nulos a valor por defecto, textos sin espacios sobrantes) y construye el objeto con el constructor privado.

| Modificador de acceso | Nombre | Tipo acceso método | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Tipo dato de retorno |
|---|---|---|---|---|---|---|
| public | Builder | no static | Constructor del Builder: asigna a cada atributo su valor por defecto. | | | |
| public | id | no static | Asigna el identificador recibido; si es nulo, asigna el identificador por defecto. | id | UUID | Builder |
| public | nombres | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | nombres | String | Builder |
| public | apellidos | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | apellidos | String | Builder |
| public | correoInstitucional | no static | Asigna el valor recibido; si es nulo, asigna: Instancia vacía de CorreoInstitucional. | correoInstitucional | CorreoInstitucional | Builder |
| public | correoVerificado | no static | Asigna el valor recibido. | correoVerificado | boolean | Builder |
| public | numeroCelular | no static | Asigna el valor recibido; si es nulo, asigna: Instancia vacía de NumeroCelular. | numeroCelular | NumeroCelular | Builder |
| public | celularVerificado | no static | Asigna el valor recibido. | celularVerificado | boolean | Builder |
| public | contrasena | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | contrasena | String | Builder |
| public | aprobada | no static | Asigna el valor recibido. | aprobada | boolean | Builder |
| public | estadoCuenta | no static | Asigna el valor recibido; si es nulo, asigna: PENDIENTE_APROBACION. | estadoCuenta | EstadoCuenta | Builder |
| public | fechaRegistro | no static | Asigna la fecha recibida; si es nula, asigna la fecha por defecto. | fechaRegistro | LocalDateTime | Builder |
| public | build | no static | Crea la instancia de Usuario con los valores reunidos, usando su constructor privado. | | | Usuario |
