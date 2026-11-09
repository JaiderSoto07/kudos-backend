# SolicitudRegistro

[Inicio](../../../README.md) · [Diagrama de clases](../README.md) · [Modelo de dominio: SolicitudRegistro](../../01-modelo-dominio/objetos/SolicitudRegistro.md) · [Entidad MER: SolicitudRegistro](../../03-modelo-entidad-relacion/entidades/SolicitudRegistro.md)

| Campo | Valor |
|---|---|
| Clase | SolicitudRegistro |
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

## Métodos

Métodos de negocio: salen de las responsabilidades del [modelo enriquecido](../../01-modelo-dominio/objetos/SolicitudRegistro.md#responsabilidades). Las consultas se marcan `static`, como en Sigma: en la implementación las resuelve la capa de negocio con su DAO.

| Modificador de acceso | Nombre | Tipo acceso método | Tipo modificación método | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Parámetros · Descripción | Tipo dato de retorno | Excepciones (políticas que puede incumplir) |
|---|---|---|---|---|---|---|---|---|---|
| public | crear | no static | final | Registra la petición de rol de una persona que se autorregistra. | solicitante | Usuario | Persona registrada. | SolicitudRegistro | P-SRE-001: Se solicita el rol de Administrador o Rector.<br>P-SRE-002: Falta la Facultad o el Programa o no está activo.<br>P-SRE-003: Ya tiene una solicitud pendiente. |
|  |  |  |  |  | rol | Rol | Rol solicitado. |  |  |
|  |  |  |  |  | facultad | Facultad | Si es Coordinador de Facultad. |  |  |
|  |  |  |  |  | programa | ProgramaAcademico | Si es Coordinador de Programa, Docente o Estudiante. |  |  |
| public | aprobar | no static | final | La autoridad competente aprueba la solicitud; se asigna el rol y se aprueba la cuenta. | actor | Usuario | Autoridad que aprueba. | SolicitudRegistro | P-SRE-005: La solicitud ya fue decidida.<br>P-SRE-006: El actor no puede aprobar ese rol o la solicitud no es de su ámbito.<br>P-SRE-007: El actor es el solicitante.<br>P-SRE-008: Alguna de esas operaciones no es válida. |
| public | rechazar | no static | final | La autoridad competente rechaza la solicitud con un motivo. | actor | Usuario | Autoridad que rechaza. | SolicitudRegistro | P-SRE-005: La solicitud ya fue decidida.<br>P-SRE-006: El actor no puede aprobar ese rol o la solicitud no es de su ámbito.<br>P-SRE-007: El actor es el solicitante.<br>P-SRE-010: Falta el motivo del rechazo. |
|  |  |  |  |  | motivo | String | Razón del rechazo. |  |  |
| public | consultarPendientes | static | final | Lista las solicitudes que la autoridad puede decidir. | actor | Usuario | Autoridad que consulta. | Lista de SolicitudRegistro |  |

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
