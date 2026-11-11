# AsignacionRol

[Inicio](../../../README.md) · [Diagrama de clases](../README.md) · [Modelo de dominio: AsignacionRol](../../01-modelo-dominio/objetos/AsignacionRol.md) · [Entidad MER: AsignacionRol](../../03-modelo-entidad-relacion/entidades/AsignacionRol.md)

| Campo | Valor |
|---|---|
| Clase | AsignacionRol |
| Clase en el código | `AsignacionRolDominio` |
| Estereotipo | class (entidad de dominio) |
| Descripción | Registro histórico de cada rol institucional que ha tenido un usuario. Para los coordinadores también indica qué Facultad o Programa acompaña. Solo una asignación por usuario puede estar vigente. |
| Modificador acceso | public |
| Tipo clase | final |
| Paquete | `co.edu.uco.kudos.dominio` |

> El dato calculado `vigente` no es un atributo almacenado: se obtiene con su método de negocio (por ejemplo `estaVigente()`).

## Relaciones

| Relación | Tipo relación | Estructura |
|---|---|---|
| Necesita de | Composición | Usuario |
| Necesita de | Asociación | Facultad |
| Necesita de | Asociación | ProgramaAcademico |
| Necesita de | Dependencia (enumeración) | Rol |

## Atributos

| Modificador de acceso | Nombre | Tipo de dato | Tipo de dato · ¿Cuál? | Tipo modificación atributo | Tipo acceso atributo | Valor por defecto | Descripción atributo |
|---|---|---|---|---|---|---|---|
| private | id | Otro | UUID | final | no static | 00000000-0000-0000-0000-000000000000 | Identificador único de la asignación |
| private | usuario | Otro | Usuario | final | no static | Instancia por defecto de Usuario | Usuario al que se asigna el rol |
| private | rol | Otro | Rol | final | no static | ADMINISTRADOR | Rol institucional asignado |
| private | facultad | Otro | Facultad | final | no static | Instancia por defecto de Facultad | Facultad que acompaña el Coordinador de Facultad |
| private | programa | Otro | ProgramaAcademico | final | no static | Instancia por defecto de ProgramaAcademico | Programa que acompaña el Coordinador de Programa |
| private | fechaInicio | Otro | LocalDateTime | final | no static | Fecha actual | Fecha y hora en que inicia la vigencia del rol |
| private | fechaFin | Otro | LocalDateTime | no final | no static | Fecha por defecto (UtilFecha) | Fecha y hora en que termina la vigencia del rol |
| private | motivoFin | String |  | no final | no static | "" (cadena vacía) | Razón por la que terminó la asignación |

## Constructores

| Modificador de acceso | Nombre | Tipo constructor | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Parámetros · ¿Cuál? | Parámetros · Tipo parámetro | Parámetros · Descripción | Excepciones |
|---|---|---|---|---|---|---|---|---|---|
| private | AsignacionRol | Parametrizado | Constructor privado: crea la instancia copiando los valores ya saneados que reunió el Builder. Solo se invoca desde `Builder.build()`, así nadie crea un objeto a medio construir. | builder | Otro | Builder | Por valor | Builder con los valores de los atributos. | |

## Métodos accesores

Solo hay métodos de lectura (getters). No hay setters públicos: el estado cambia únicamente a través de los métodos de negocio, que validan las reglas (ARQ-007, ARQ-008).

| Modificador de acceso | Nombre | Tipo método accesor | Descripción | Tipo dato de retorno · Tipo de dato | Tipo dato de retorno · ¿Cuál? |
|---|---|---|---|---|---|
| public | getId | getter | Obtiene el valor del atributo id. | Otro | UUID |
| public | getUsuario | getter | Obtiene el valor del atributo usuario. | Otro | Usuario |
| public | getRol | getter | Obtiene el valor del atributo rol. | Otro | Rol |
| public | getFacultad | getter | Obtiene el valor del atributo facultad. | Otro | Facultad |
| public | getPrograma | getter | Obtiene el valor del atributo programa. | Otro | ProgramaAcademico |
| public | getFechaInicio | getter | Obtiene el valor del atributo fechaInicio. | Otro | LocalDateTime |
| public | getFechaFin | getter | Obtiene el valor del atributo fechaFin. | Otro | LocalDateTime |
| public | getMotivoFin | getter | Obtiene el valor del atributo motivoFin. | String |  |

## Métodos de negocio

Métodos **reales** de `AsignacionRolDominio` (comportamiento del dominio rico, D-29).

| Modificador de acceso | Nombre | Parámetros | Tipo dato de retorno | Descripción |
|---|---|---|---|---|
| public | estaVigente | — | boolean | Indica si no tiene fecha de fin. |
| public | cerrar | String motivo | void | Termina la vigencia de una asignación de rol sin borrarla. |
| public | puedeVerFacultad | UUID idFacultad | boolean | Indica si la autoridad puede consultar esa Facultad según su ámbito. |
| public | puedeVerPrograma | ProgramaAcademicoDominio programaConsultado | boolean | Indica si la autoridad puede consultar ese Programa según su ámbito. |
| public | coordinaPrograma | UUID idPrograma | boolean | Indica si es el Coordinador de Programa de ese Programa. |
| public | puedeDecidir | SolicitudRegistroDominio solicitud | boolean | Indica si la autoridad puede aprobar o rechazar esa solicitud (rol aprobador y ámbito). |

## Operaciones del caso de uso (capa de negocio)

Responsabilidades del [modelo enriquecido](../../01-modelo-dominio/objetos/AsignacionRol.md#responsabilidades) que coordinan varios objetos o consultan la base de datos. Las implementa: CargoNegocioImpl, SolicitudNegocioImpl, AutoridadNegocioImpl.

| Operación | Descripción | Políticas que protege |
|---|---|---|
| asignar | Abre la asignación vigente de un rol para un usuario; para coordinadores indica la Facultad o el Programa. | P-ASR-001, P-ASR-002, P-ASR-003, P-ASR-004, P-ASR-005 |
| consultarTitularVigente | Indica quién ocupa hoy un cargo: Rector, Coordinador Académico o coordinador de una Facultad o Programa. | P-ASR-007 |
| consultarHistorialCoordinadores | Lista las asignaciones vigentes y cerradas de la coordinación de una Facultad o Programa. | P-ASR-008 |

## Builder (clase interna public static)

Patrón **Builder**, igual que en Sigma: reúne los valores, los sanea (nulos a valor por defecto, textos sin espacios sobrantes) y construye el objeto con el constructor privado.

| Modificador de acceso | Nombre | Tipo acceso método | Descripción | Parámetros · Nombre | Parámetros · Tipo de dato | Tipo dato de retorno |
|---|---|---|---|---|---|---|
| public | Builder | no static | Constructor del Builder: asigna a cada atributo su valor por defecto. | | | |
| public | id | no static | Asigna el identificador recibido; si es nulo, asigna el identificador por defecto. | id | UUID | Builder |
| public | usuario | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Usuario. | usuario | Usuario | Builder |
| public | rol | no static | Asigna el valor recibido; si es nulo, asigna: ADMINISTRADOR. | rol | Rol | Builder |
| public | facultad | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de Facultad. | facultad | Facultad | Builder |
| public | programa | no static | Asigna el valor recibido; si es nulo, asigna: Instancia por defecto de ProgramaAcademico. | programa | ProgramaAcademico | Builder |
| public | fechaInicio | no static | Asigna la fecha recibida; si es nula, asigna la fecha por defecto. | fechaInicio | LocalDateTime | Builder |
| public | fechaFin | no static | Asigna la fecha recibida; si es nula, asigna la fecha por defecto. | fechaFin | LocalDateTime | Builder |
| public | motivoFin | no static | Asigna el texto recibido sin espacios en blanco al inicio y al final; si es nulo, asigna una cadena vacía. | motivoFin | String | Builder |
| public | build | no static | Crea la instancia de AsignacionRol con los valores reunidos, usando su constructor privado. | | | AsignacionRol |
