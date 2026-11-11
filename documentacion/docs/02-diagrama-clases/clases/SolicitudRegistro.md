# SolicitudRegistro

[Inicio](../../../README.md) · [Diagrama de clases](../README.md) · [Modelo de dominio: SolicitudRegistro](../../01-modelo-dominio/objetos/SolicitudRegistro.md) · [Entidad MER: SolicitudRegistro](../../03-modelo-entidad-relacion/entidades/SolicitudRegistro.md)

| Campo | Valor |
|---|---|
| Clase | SolicitudRegistro |
| Clase en el código | `SolicitudRegistroDominio` |
| Estereotipo | class (entidad de dominio) |
| Descripción | Petición que hace una persona al registrarse para obtener un rol institucional. La decide (aprueba o rechaza) la autoridad que corresponde según la cadena de aprobación. |
| Modificador acceso | public |
| Tipo clase | final |
| Paquete | `co.edu.uco.kudos.dominio` |

## Relaciones

| Relación | Tipo relación | Estructura |
|---|---|---|
| Necesita de | Asociación | Usuario |
| Necesita de | Asociación | Facultad |
| Necesita de | Asociación | ProgramaAcademico |
| Necesita de | Dependencia (enumeración) | Rol |
| Necesita de | Dependencia (enumeración) | EstadoSolicitud |

## Atributos

| Modificador de acceso | Nombre | Tipo de dato | Tipo de dato · ¿Cuál? | Tipo modificación atributo | Tipo acceso atributo | Valor por defecto | Descripción atributo |
|---|---|---|---|---|---|---|---|
| private | id | Otro | UUID | final | no static | 00000000-0000-0000-0000-000000000000 | Identificador único de la solicitud |
| private | solicitante | Otro | Usuario | final | no static | Instancia por defecto de Usuario | Usuario que solicita el rol |
| private | rolSolicitado | Otro | Rol | final | no static | ADMINISTRADOR | Rol institucional que solicita |
| private | facultad | Otro | Facultad | final | no static | Instancia por defecto de Facultad | Facultad que solicita coordinar |
| private | programa | Otro | ProgramaAcademico | final | no static | Instancia por defecto de ProgramaAcademico | Programa al que pertenece o que solicita coordinar |
| private | estado | Otro | EstadoSolicitud | no final | no static | PENDIENTE | Estado de la solicitud |
| private | fechaSolicitud | Otro | LocalDateTime | final | no static | Fecha actual | Fecha y hora de la solicitud |
| private | decididaPor | Otro | Usuario | no final | no static | Instancia por defecto de Usuario | Autoridad que aprobó o rechazó |
| private | fechaDecision | Otro | LocalDateTime | no final | no static | Fecha por defecto (UtilFecha) | Fecha y hora de la decisión |
| private | motivoRechazo | String |  | no final | no static | "" (cadena vacía) | Razón del rechazo |

## Constructores

| Modificador de acceso | Nombre | Tipo constructor | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Parámetros · ¿Cuál? | Parámetros · Tipo parámetro | Parámetros · Descripción | Excepciones |
|---|---|---|---|---|---|---|---|---|---|
| private | SolicitudRegistro | Parametrizado | Constructor privado: crea la instancia copiando los valores ya saneados que reunió el Builder. Solo se invoca desde `Builder.build()`, así nadie crea un objeto a medio construir. | builder | Otro | Builder | Por valor | Builder con los valores de los atributos. | |

## Métodos accesores

Solo hay métodos de lectura (getters). No hay setters públicos: el estado cambia únicamente a través de los métodos de negocio, que validan las reglas (ARQ-007, ARQ-008).

| Modificador de acceso | Nombre | Tipo método accesor | Descripción | Tipo dato de retorno · Tipo de dato | Tipo dato de retorno · ¿Cuál? |
|---|---|---|---|---|---|
| public | getId | getter | Obtiene el valor del atributo id. | Otro | UUID |
| public | getSolicitante | getter | Obtiene el valor del atributo solicitante. | Otro | Usuario |
| public | getRolSolicitado | getter | Obtiene el valor del atributo rolSolicitado. | Otro | Rol |
| public | getFacultad | getter | Obtiene el valor del atributo facultad. | Otro | Facultad |
| public | getPrograma | getter | Obtiene el valor del atributo programa. | Otro | ProgramaAcademico |
| public | getEstado | getter | Obtiene el valor del atributo estado. | Otro | EstadoSolicitud |
| public | getFechaSolicitud | getter | Obtiene el valor del atributo fechaSolicitud. | Otro | LocalDateTime |
| public | getDecididaPor | getter | Obtiene el valor del atributo decididaPor. | Otro | Usuario |
| public | getFechaDecision | getter | Obtiene el valor del atributo fechaDecision. | Otro | LocalDateTime |
| public | getMotivoRechazo | getter | Obtiene el valor del atributo motivoRechazo. | String |  |

## Métodos de negocio

Métodos **reales** de `SolicitudRegistroDominio` (comportamiento del dominio rico, D-29).

| Modificador de acceso | Nombre | Parámetros | Tipo dato de retorno | Descripción |
|---|---|---|---|---|
| public | estaPendiente | — | boolean | Indica si la solicitud sigue PENDIENTE. |
| public | aprobar | UsuarioDominio autoridad | void | La autoridad competente aprueba la solicitud; se asigna el rol y se aprueba la cuenta. |
| public | rechazar | UsuarioDominio autoridad, String motivo | void | La autoridad competente rechaza la solicitud con un motivo. |

## Operaciones del caso de uso (capa de negocio)

Responsabilidades del [modelo enriquecido](../../01-modelo-dominio/objetos/SolicitudRegistro.md#responsabilidades) que coordinan varios objetos o consultan la base de datos. Las implementa: RegistroNegocioImpl, SolicitudNegocioImpl.

| Operación | Descripción | Políticas que protege |
|---|---|---|
| crear | Registra la petición de rol de una persona que se autorregistra. | P-SRE-001, P-SRE-002, P-SRE-003, P-SRE-004 |
| consultarPendientes | Lista las solicitudes que la autoridad puede decidir. | P-SRE-011 |

## Builder (clase interna public static)

Patrón **Builder**, igual que en Sigma: reúne los valores, los sanea (nulos a valor por defecto, textos sin espacios sobrantes) y construye el objeto con el constructor privado.

| Modificador de acceso | Nombre | Tipo acceso método | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Tipo dato de retorno |
|---|---|---|---|---|---|---|
| public | Builder | no static | Constructor del Builder: asigna a cada atributo su valor por defecto. | | | |
| public | id | no static | Asigna el identificador recibido; si es nulo, asigna el identificador por defecto. | id | UUID | Builder |
| public | solicitante | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Usuario. | solicitante | Usuario | Builder |
| public | rolSolicitado | no static | Asigna el valor recibido; si es nulo, asigna: ADMINISTRADOR. | rolSolicitado | Rol | Builder |
| public | facultad | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Facultad. | facultad | Facultad | Builder |
| public | programa | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de ProgramaAcademico. | programa | ProgramaAcademico | Builder |
| public | estado | no static | Asigna el valor recibido; si es nulo, asigna: PENDIENTE. | estado | EstadoSolicitud | Builder |
| public | fechaSolicitud | no static | Asigna la fecha recibida; si es nula, asigna la fecha por defecto. | fechaSolicitud | LocalDateTime | Builder |
| public | decididaPor | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Usuario. | decididaPor | Usuario | Builder |
| public | fechaDecision | no static | Asigna la fecha recibida; si es nula, asigna la fecha por defecto. | fechaDecision | LocalDateTime | Builder |
| public | motivoRechazo | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | motivoRechazo | String | Builder |
| public | build | no static | Crea la instancia de SolicitudRegistro con los valores reunidos, usando su constructor privado. | | | SolicitudRegistro |
