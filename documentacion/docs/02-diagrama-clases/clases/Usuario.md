# Usuario

[Inicio](../../../README.md) · [Diagrama de clases](../README.md) · [Modelo de dominio: Usuario](../../01-modelo-dominio/objetos/Usuario.md) · [Entidad MER: Usuario](../../03-modelo-entidad-relacion/entidades/Usuario.md)

| Campo | Valor |
|---|---|
| Clase | Usuario |
| Clase en el código | `UsuarioDominio` |
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
| Necesita de | Dependencia (enumeración) | EstadoCuenta |

## Atributos

| Modificador de acceso | Nombre | Tipo de dato | Tipo de dato · ¿Cuál? | Tipo modificación atributo | Tipo acceso atributo | Valor por defecto | Descripción atributo |
|---|---|---|---|---|---|---|---|
| private | id | Otro | UUID | final | no static | 00000000-0000-0000-0000-000000000000 | Identificador único del usuario |
| private | nombres | String |  | no final | no static | "" (cadena vacía) | Nombres de la persona |
| private | apellidos | String |  | no final | no static | "" (cadena vacía) | Apellidos de la persona |
| private | correoInstitucional | String |  | no final | no static | "" (cadena vacía) | Correo de la cuenta de Google con la que se registra e ingresa |
| private | correoVerificado | boolean/Boolean |  | no final | no static | false | Indica si el correo fue validado al ingresar con Google |
| private | numeroCelular | String |  | no final | no static | "" (cadena vacía) | Número celular de contacto |
| private | celularVerificado | boolean/Boolean |  | no final | no static | false | Indica si el celular fue validado con un código |
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
| public | getCorreoInstitucional | getter | Obtiene el valor del atributo correoInstitucional. | String |  |
| public | isCorreoVerificado | getter | Obtiene el valor del atributo correoVerificado. | boolean/Boolean |  |
| public | getNumeroCelular | getter | Obtiene el valor del atributo numeroCelular. | String |  |
| public | isCelularVerificado | getter | Obtiene el valor del atributo celularVerificado. | boolean/Boolean |  |
| public | isAprobada | getter | Obtiene el valor del atributo aprobada. | boolean/Boolean |  |
| public | getEstadoCuenta | getter | Obtiene el valor del atributo estadoCuenta. | Otro | EstadoCuenta |
| public | getFechaRegistro | getter | Obtiene el valor del atributo fechaRegistro. | Otro | LocalDateTime |

## Métodos de negocio

Métodos **reales** de `UsuarioDominio` (comportamiento del dominio rico, D-29).

| Modificador de acceso | Nombre | Parámetros | Tipo dato de retorno | Descripción |
|---|---|---|---|---|
| public | aprobar | — | void | Registra que la autoridad competente aprobó la cuenta. |
| public | verificarCorreo | — | void | Marca el correo como verificado con el código recibido. |
| public | verificarCelular | — | void | Marca el celular como verificado con el código recibido. |
| public | intentarActivar | — | boolean | Pasa la cuenta a ACTIVA si está aprobada y tiene correo y celular verificados. |
| public | estaActiva | — | boolean | Indica si la cuenta está ACTIVA. |
| public | motivoParaNoIngresar | — | String | Explica qué le falta a la cuenta para usar la aplicación. |
| public | actualizarDatosPersonales | String nuevosNombres, String nuevosApellidos | void | Modifica los nombres y apellidos del propio usuario. |
| public | cambiarCorreo | String nuevoCorreo | void | Cambia el correo institucional y exige verificarlo de nuevo. |
| public | cambiarCelular | String nuevoCelular | void | Cambia el número celular y exige verificarlo de nuevo. |
| public | suspender | — | void | Bloquea temporalmente la cuenta de un usuario del ámbito del actor. |
| public | reactivar | — | void | Levanta la suspensión de una cuenta. |
| public | desactivar | — | void | Cierra definitivamente la cuenta para su rol, conservando su historial. |
| public | nombreCompleto | — | String | Nombres y apellidos unidos. |

## Operaciones del caso de uso (capa de negocio)

Responsabilidades del [modelo enriquecido](../../01-modelo-dominio/objetos/Usuario.md#responsabilidades) que coordinan varios objetos o consultan la base de datos. Las implementa: RegistroNegocioImpl, VerificacionNegocioImpl, CuentaNegocioImpl, AutenticacionNegocioImpl, AutoridadNegocioImpl, PersonaNegocioImpl.

| Operación | Descripción | Políticas que protege |
|---|---|---|
| registrar | Crea la cuenta de una persona que se autorregistra con su identidad institucional. | P-USU-001, P-USU-002, P-USU-003, P-USU-004, P-USU-005, P-USU-006, P-USU-007, P-USU-008 |
| registrarPorAdministrador | Un Administrador registra directamente a un Rector o a otro Administrador, que queda aprobado. | P-USU-001, P-USU-002, P-USU-003, P-USU-004, P-USU-005, P-USU-006, P-USU-009, P-USU-010, P-USU-011 |
| puedeIniciarSesion | Decide si la persona autenticada con Google puede usar la plataforma. | P-USU-016, P-USU-017 |
| tieneRolVigente | Indica si el usuario ejerce hoy un rol dado; lo usan las políticas de autorización. | P-USU-029 |
| consultar | Muestra los datos del usuario, su rol, sus vínculos y el estado de sus validaciones. | P-USU-027, P-USU-028 |

## Builder (clase interna public static)

Patrón **Builder**, igual que en Sigma: reúne los valores, los sanea (nulos a valor por defecto, textos sin espacios sobrantes) y construye el objeto con el constructor privado.

| Modificador de acceso | Nombre | Tipo acceso método | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Tipo dato de retorno |
|---|---|---|---|---|---|---|
| public | Builder | no static | Constructor del Builder: asigna a cada atributo su valor por defecto. | | | |
| public | id | no static | Asigna el identificador recibido; si es nulo, asigna el identificador por defecto. | id | UUID | Builder |
| public | nombres | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | nombres | String | Builder |
| public | apellidos | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | apellidos | String | Builder |
| public | correoInstitucional | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | correoInstitucional | String | Builder |
| public | correoVerificado | no static | Asigna el valor recibido. | correoVerificado | boolean | Builder |
| public | numeroCelular | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | numeroCelular | String | Builder |
| public | celularVerificado | no static | Asigna el valor recibido. | celularVerificado | boolean | Builder |
| public | aprobada | no static | Asigna el valor recibido. | aprobada | boolean | Builder |
| public | estadoCuenta | no static | Asigna el valor recibido; si es nulo, asigna: PENDIENTE_APROBACION. | estadoCuenta | EstadoCuenta | Builder |
| public | fechaRegistro | no static | Asigna la fecha recibida; si es nula, asigna la fecha por defecto. | fechaRegistro | LocalDateTime | Builder |
| public | build | no static | Crea la instancia de Usuario con los valores reunidos, usando su constructor privado. | | | Usuario |
