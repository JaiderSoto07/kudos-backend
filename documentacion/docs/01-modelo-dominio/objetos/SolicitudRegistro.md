# SolicitudRegistro

[Inicio](../../../README.md) · [Modelo de dominio](../README.md) · [Diagrama de clases](../../02-diagrama-clases/README.md) · [Modelo entidad-relación](../../03-modelo-entidad-relacion/README.md)

> Petición que hace una persona al registrarse para obtener un rol institucional. La decide (aprueba o rechaza) la autoridad que corresponde según la cadena de aprobación.

**Línea de vida:** 4

[Muestreo](#muestreo) · [Caracterización](#caracterización) · [Eventos](#eventos) · [Políticas](#políticas)

## Muestreo

### Atributos

| Atributo | Descripción | Tipo de dato | Obligatorio |
|---|---|---|---|
| id | Identificador único de la solicitud | UUID | Sí |
| solicitante | Usuario que solicita el rol | [Usuario](Usuario.md) | Sí |
| rolSolicitado | Rol institucional que solicita | [Rol](../README.md#enumeraciones) | Sí |
| facultad | Facultad que solicita coordinar | [Facultad](Facultad.md) | Condicional |
| programa | Programa al que pertenece o que solicita coordinar | [ProgramaAcademico](ProgramaAcademico.md) | Condicional |
| estado | Estado de la solicitud | [EstadoSolicitud](../README.md#enumeraciones) | Sí |
| fechaSolicitud | Fecha y hora de la solicitud | FechaHora | Sí |
| decididaPor | Autoridad que aprobó o rechazó | [Usuario](Usuario.md) | No |
| fechaDecision | Fecha y hora de la decisión | FechaHora | No |
| motivoRechazo | Razón del rechazo | Texto | No |

### Datos de muestra

_5 registros de ejemplo. Los identificadores se abrevian (u-01, k-01…) para facilitar la lectura; en el sistema son UUID._

| id | solicitante | rolSolicitado | facultad | programa | estado | fechaSolicitud | decididaPor | fechaDecision | motivoRechazo |
|---|---|---|---|---|---|---|---|---|---|
| sr-01 | u-03 Carlos Restrepo | COORDINADOR_ACADEMICO |  |  | APROBADA | 2026-11-01 10:00 | u-02 | 2026-11-01 10:30 |  |
| sr-02 | u-04 Luisa Ospina | COORDINADOR_FACULTAD | Ingeniería |  | APROBADA | 2026-11-02 08:30 | u-03 | 2026-11-04 09:00 |  |
| sr-03 | u-07 Santiago Henao | ESTUDIANTE |  | Ingeniería de Sistemas | APROBADA | 2026-11-03 14:20 | u-05 | 2026-11-04 15:00 |  |
| sr-04 | u-13 Pedro Ruiz | DOCENTE |  | Ingeniería de Sistemas | RECHAZADA | 2026-11-05 09:00 | u-05 | 2026-11-05 12:00 | No figura en la planta docente del programa |
| sr-05 | u-09 Mateo Arango | ESTUDIANTE |  | Ingeniería de Sistemas | PENDIENTE | 2026-11-07 10:05 |  |  |  |

### Relaciones

| Relación | Con | Cardinalidad | Tipo |
|---|---|---|---|
| Es presentada por-> | [Usuario](Usuario.md) | SolicitudRegistro 0..* — 1 Usuario | Asociación |
| Es decidida por-> | [Usuario](Usuario.md) | SolicitudRegistro 0..* — 0..1 Usuario | Asociación |
| Solicita coordinar-> | [Facultad](Facultad.md) | SolicitudRegistro 0..* — 0..1 Facultad | Asociación |
| Solicita ingresar a-> | [ProgramaAcademico](ProgramaAcademico.md) | SolicitudRegistro 0..* — 0..1 ProgramaAcademico | Asociación |

## Caracterización

### Caracterización del objeto

| Aspecto | Descripción |
|---|---|
| **¿Qué representa?** | La petición de una persona para obtener un rol institucional y la decisión que tomó la autoridad competente. |
| **Tipo de objeto** | Entidad raíz de agregado |
| **Agregado** | SolicitudRegistro (raíz). |
| **¿Qué conoce? (información que administra)** | El solicitante, el rol solicitado, la Facultad o el Programa, el estado, quién decidió, cuándo y por qué se rechazó. |
| **¿Qué hace? (responsabilidades)** | • Crear solicitud<br>• Aprobar solicitud<br>• Rechazar solicitud<br>• Consultar solicitudes pendientes |
| **¿Qué decisiones toma?** | • Si está pendiente.<br>• Si un usuario dado puede decidirla (rol aprobador correcto, ámbito y no ser el solicitante).<br>• Que rechazar exige motivo. |
| **¿Qué decisiones delega?** | • Asignar el rol, vincular al programa y sustituir coordinadores al servicio de aprobación.<br>• Aprobar la cuenta a Usuario. |
| **¿Con quién colabora?** | Usuario, Rol, Facultad, ProgramaAcademico, AsignacionRol, VinculacionPrograma |
| **¿Qué eventos genera?** | • Solicitud de registro creada<br>• Usuario aprobado<br>• Solicitud rechazada<br>• Solicitudes pendientes consultadas |
| **¿Qué invariantes protege?** | • RN-090 a RN-093: la aprueba la autoridad correspondiente.<br>• RN-108: nadie se aprueba a sí mismo.<br>• UQ-SOL-1: una sola solicitud pendiente por persona. |

### Caracterización de atributos

| Nombre atributo | id | solicitante | rolSolicitado | facultad | programa | estado | fechaSolicitud | decididaPor | fechaDecision | motivoRechazo |
|---|---|---|---|---|---|---|---|---|---|---|
| Descripción atributo | Identificador único de la solicitud | Usuario que solicita el rol | Rol institucional que solicita | Facultad que solicita coordinar | Programa al que pertenece o que solicita coordinar | Estado de la solicitud | Fecha y hora de la solicitud | Autoridad que aprobó o rechazó | Fecha y hora de la decisión | Razón del rechazo |
| Identificador del objeto de dominio | Sí | No | No | No | No | No | No | No | No | No |
| Tipo de dato | UUID | Usuario | Rol | Facultad | ProgramaAcademico | EstadoSolicitud | FechaHora | Usuario | FechaHora | Texto |
| Obligatorio | Sí | Sí | Sí | Condicional | Condicional | Sí | Sí | No | No | No |
| Longitud mínima (si aplica) |  |  |  |  |  |  |  |  |  | 0 |
| Longitud máxima (si aplica) |  |  |  |  |  |  |  |  |  | 250 |
| Precisión (si aplica) |  |  |  |  |  |  |  |  |  |  |
| Rango inicial (si aplica) |  |  |  |  |  |  |  |  |  |  |
| Rango final (si aplica) |  |  |  |  |  |  |  |  |  |  |
| Formato (si aplica) |  |  |  |  |  |  |  |  |  |  |
| Reglas especiales (si aplica) | Autogenerado |  | No puede ser ADMINISTRADOR ni RECTOR (D-17) | Obligatoria solo si rolSolicitado = COORDINADOR_FACULTAD | Obligatorio si rolSolicitado es COORDINADOR_PROGRAMA, DOCENTE o ESTUDIANTE |  | Asignada por el sistema | Obligatorio al decidir; distinto del solicitante (RN-108); debe tener el rol aprobador y el ámbito correspondiente (RN-090 a RN-093) | Obligatoria al decidir | Máx. 250; obligatorio si estado = RECHAZADA |
| Dato calculado | No | No | No | No | No | No | No | No | No | No |
| Quitar espacios en blanco al inicio y al fin | No | No | No | No | No | No | No | No | No | Sí |
| Dato sensible | No | No | No | No | No | No | No | No | No | No |
| Valor por defecto (si aplica) |  |  |  |  |  | PENDIENTE | Fecha actual |  |  |  |
| Auto generado | Sí | No | No | No | No | No | Sí | No | No | No |
| Crear solicitud | No requerido | Requerido | Requerido | Requerido | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido |
| Aprobar solicitud | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | Requerido | No requerido | No requerido |
| Rechazar solicitud | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | Requerido | No requerido | Requerido |
| Consultar solicitudes pendientes | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | No mostrar | No mostrar | No mostrar |

### Combinaciones únicas

| Nombre combinación única | Descripción | Atributos involucrados |
|---|---|---|
| UQ-SOL-1 | Un solicitante tiene como máximo una solicitud pendiente | solicitante, estado = PENDIENTE |

### Responsabilidades

| Responsabilidad | Descripción | Entradas · Parámetro | Entradas · Tipo de dato | Entradas · Descripción | Salida · Tipo de dato | Salida · Descripción | Políticas · Política | Políticas · Descripción | Excepción · Detalle | Excepción · ¿Qué hago? |
|---|---|---|---|---|---|---|---|---|---|---|
| **Crear solicitud** (`crear()`) | Registra la petición de rol de una persona que se autorregistra. | solicitante | Usuario | Persona registrada. | SolicitudRegistro | Solicitud PENDIENTE. | P-SRE-001 | Asegurar que el rol solicitado sea autorregistrable: Coordinador Académico, Coordinador de Facultad, Coordinador de Programa, Docente o Estudiante. | Se solicita el rol de Administrador o Rector. | Rechazar la solicitud e informar que esos roles los registra un Administrador. |
|  |  | rol | Rol | Rol solicitado. |  |  | P-SRE-002 | Asegurar que se indique la Facultad (Coordinador de Facultad) o el Programa (Coordinador de Programa, Docente y Estudiante) y que estén activos. | Falta la Facultad o el Programa o no está activo. | Rechazar la solicitud e informar el dato que falta. |
|  |  | facultad | Facultad | Si es Coordinador de Facultad. |  |  | P-SRE-003 | Asegurar que el solicitante no tenga otra solicitud pendiente. | Ya tiene una solicitud pendiente. | Rechazar la solicitud e informar que espere la decisión. |
|  |  | programa | ProgramaAcademico | Si es Coordinador de Programa, Docente o Estudiante. |  |  | P-SRE-004 | Notificar a la autoridad aprobadora que tiene una solicitud por decidir. |  |  |
| **Aprobar solicitud** (`aprobar()`) | La autoridad competente aprueba la solicitud; se asigna el rol y se aprueba la cuenta. | actor | Usuario | Autoridad que aprueba. | SolicitudRegistro | Solicitud APROBADA. | P-SRE-005 | Asegurar que solo se decida una solicitud pendiente. | La solicitud ya fue decidida. | Rechazar la operación e informar la decisión tomada. |
|  |  |  |  |  |  |  | P-SRE-006 | Asegurar que quien decide tenga el rol aprobador del rol solicitado y que la solicitud sea de su ámbito. | El actor no puede aprobar ese rol o la solicitud no es de su ámbito. | Rechazar la decisión e informar que no tiene permiso. |
|  |  |  |  |  |  |  | P-SRE-007 | Asegurar que nadie decida su propia solicitud. | El actor es el solicitante. | Rechazar la decisión. |
|  |  |  |  |  |  |  | P-SRE-008 | Al aprobar, aprobar la cuenta, asignar el rol (con sustitución si aplica) y, si es docente o estudiante, vincularlo al programa. | Alguna de esas operaciones no es válida. | Rechazar la aprobación completa (una sola transacción). |
|  |  |  |  |  |  |  | P-SRE-009 | Registrar en auditoría toda aprobación y todo rechazo, y notificar al solicitante. |  |  |
| **Rechazar solicitud** (`rechazar()`) | La autoridad competente rechaza la solicitud con un motivo. | actor | Usuario | Autoridad que rechaza. | SolicitudRegistro | Solicitud RECHAZADA. | P-SRE-005 | Asegurar que solo se decida una solicitud pendiente. | La solicitud ya fue decidida. | Rechazar la operación e informar la decisión tomada. |
|  |  | motivo | String | Razón del rechazo. |  |  | P-SRE-006 | Asegurar que quien decide tenga el rol aprobador del rol solicitado y que la solicitud sea de su ámbito. | El actor no puede aprobar ese rol o la solicitud no es de su ámbito. | Rechazar la decisión e informar que no tiene permiso. |
|  |  |  |  |  |  |  | P-SRE-007 | Asegurar que nadie decida su propia solicitud. | El actor es el solicitante. | Rechazar la decisión. |
|  |  |  |  |  |  |  | P-SRE-010 | Asegurar que el rechazo tenga motivo. | Falta el motivo del rechazo. | Rechazar la operación e informar que el motivo es obligatorio. |
|  |  |  |  |  |  |  | P-SRE-009 | Registrar en auditoría toda aprobación y todo rechazo, y notificar al solicitante. |  |  |
| **Consultar solicitudes pendientes** (`consultarPendientes()`) | Lista las solicitudes que la autoridad puede decidir. | actor | Usuario | Autoridad que consulta. | Lista de SolicitudRegistro | Solicitudes pendientes de su ámbito. | P-SRE-011 | Mostrar a cada autoridad solo las solicitudes pendientes que puede decidir. |  |  |

## Eventos

| Actor | Acción | Descripción | Objeto de dominio | Información externa · Información | Información externa · Descripción | Políticas · Número | Sistema externo | Evento | Referencia en el enunciado |
|---|---|---|---|---|---|---|---|---|---|
| Persona no registrada | Crear solicitud | Registra la petición de rol de una persona que se autorregistra. | SolicitudRegistro | Facultades y programas activos | Opciones válidas para la solicitud. | P-SRE-001 |  | Solicitud de registro creada | RN-213 |
|  |  |  |  |  |  | P-SRE-002 |  |  |  |
|  |  |  |  |  |  | P-SRE-003 |  |  |  |
|  |  |  |  |  |  | P-SRE-004 |  |  |  |
| Autoridad | Aprobar solicitud | La autoridad competente aprueba la solicitud; se asigna el rol y se aprueba la cuenta. | SolicitudRegistro | Matriz de aprobaciones | Qué rol aprueba a qué rol. | P-SRE-005 |  | Usuario aprobado | EVT-004 / RN-214 |
|  |  |  |  | Coordinación vigente | Para detectar una sustitución. | P-SRE-006 |  |  |  |
|  |  |  |  |  |  | P-SRE-007 |  |  |  |
|  |  |  |  |  |  | P-SRE-008 |  |  |  |
|  |  |  |  |  |  | P-SRE-009 |  |  |  |
| Autoridad | Rechazar solicitud | La autoridad competente rechaza la solicitud con un motivo. | SolicitudRegistro | Matriz de aprobaciones | Qué rol aprueba a qué rol. | P-SRE-005 |  | Solicitud rechazada |  |
|  |  |  |  |  |  | P-SRE-006 |  |  |  |
|  |  |  |  |  |  | P-SRE-007 |  |  |  |
|  |  |  |  |  |  | P-SRE-010 |  |  |  |
|  |  |  |  |  |  | P-SRE-009 |  |  |  |
| Autoridad | Consultar solicitudes pendientes | Lista las solicitudes que la autoridad puede decidir. | SolicitudRegistro |  |  | P-SRE-011 |  | Solicitudes pendientes consultadas | RF-087 |

## Políticas

| Número política | Descripción política | Objeto responsable | Regla referenciada |
|---|---|---|---|
| P-SRE-001 | Asegurar que el rol solicitado sea autorregistrable: Coordinador Académico, Coordinador de Facultad, Coordinador de Programa, Docente o Estudiante. | SolicitudRegistro | RN-094, RN-095, D-17 |
| P-SRE-002 | Asegurar que se indique la Facultad (Coordinador de Facultad) o el Programa (Coordinador de Programa, Docente y Estudiante) y que estén activos. | SolicitudRegistro | INV-009, INV-010, D-05 |
| P-SRE-003 | Asegurar que el solicitante no tenga otra solicitud pendiente. | SolicitudRegistro | UQ-SOL-1 |
| P-SRE-004 | Notificar a la autoridad aprobadora que tiene una solicitud por decidir. | Notificacion | D-27 |
| P-SRE-005 | Asegurar que solo se decida una solicitud pendiente. | SolicitudRegistro | D-17 |
| P-SRE-006 | Asegurar que quien decide tenga el rol aprobador del rol solicitado y que la solicitud sea de su ámbito. | Rol / PoliticaAmbito | RN-090 a RN-093, RN-117, RN-121, RN-126, POL-004 |
| P-SRE-007 | Asegurar que nadie decida su propia solicitud. | SolicitudRegistro | RN-108 |
| P-SRE-008 | Al aprobar, aprobar la cuenta, asignar el rol (con sustitución si aplica) y, si es docente o estudiante, vincularlo al programa. | ServicioAprobacion | RN-090 a RN-093, P-USU-014, P-ASR-001 a P-ASR-004, P-VPR-001 |
| P-SRE-009 | Registrar en auditoría toda aprobación y todo rechazo, y notificar al solicitante. | Auditoria | RN-096, RNF-017, D-27 |
| P-SRE-010 | Asegurar que el rechazo tenga motivo. | SolicitudRegistro | D-17 |
| P-SRE-011 | Mostrar a cada autoridad solo las solicitudes pendientes que puede decidir. | PoliticaAmbito | POL-004, RF-087 |
