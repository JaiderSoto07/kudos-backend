# AsignacionRol

[Inicio](../../../README.md) · [Modelo de dominio](../README.md) · [Diagrama de clases](../../02-diagrama-clases/README.md) · [Modelo entidad-relación](../../03-modelo-entidad-relacion/README.md)

> Registro histórico de cada rol institucional que ha tenido un usuario. Para los coordinadores también indica qué Facultad o Programa acompaña. Solo una asignación por usuario puede estar vigente.

**Línea de vida:** 2

[Muestreo](#muestreo) · [Caracterización](#caracterización) · [Eventos](#eventos) · [Políticas](#políticas)

## Muestreo

### Atributos

| Atributo | Descripción | Tipo de dato | Obligatorio |
|---|---|---|---|
| id | Identificador único de la asignación | UUID | Sí |
| usuario | Usuario al que se asigna el rol | [Usuario](Usuario.md) | Sí |
| rol | Rol institucional asignado | [Rol](../README.md#enumeraciones) | Sí |
| facultad | Facultad que acompaña el Coordinador de Facultad | [Facultad](Facultad.md) | Condicional |
| programa | Programa que acompaña el Coordinador de Programa | [ProgramaAcademico](ProgramaAcademico.md) | Condicional |
| fechaInicio | Fecha y hora en que inicia la vigencia del rol | FechaHora | Sí |
| fechaFin | Fecha y hora en que termina la vigencia del rol | FechaHora | No |
| vigente | Indica si la asignación está vigente | Lógico | Sí |
| motivoFin | Razón por la que terminó la asignación | Texto | No |

### Datos de muestra

_8 registros de ejemplo. Los identificadores se abrevian (u-01, k-01…) para facilitar la lectura; en el sistema son UUID._

| id | usuario | rol | facultad | programa | fechaInicio | fechaFin | vigente | motivoFin |
|---|---|---|---|---|---|---|---|---|
| ar-01 | u-01 Sistema Administrador | ADMINISTRADOR |  |  | 2026-11-01 08:00 |  | Sí |  |
| ar-02 | u-02 María Fernanda Gómez | RECTOR |  |  | 2026-11-01 09:00 |  | Sí |  |
| ar-03 | u-03 Carlos Andrés Restrepo | COORDINADOR_ACADEMICO |  |  | 2026-11-01 10:30 |  | Sí |  |
| ar-04 | u-10 Ricardo Mesa | COORDINADOR_FACULTAD | Ingeniería |  | 2026-11-02 08:00 | 2026-11-04 09:00 | No | Reemplazado por nuevo coordinador |
| ar-05 | u-04 Luisa Fernanda Ospina | COORDINADOR_FACULTAD | Ingeniería |  | 2026-11-04 09:00 |  | Sí |  |
| ar-06 | u-05 Juan Pablo Zuluaga | COORDINADOR_PROGRAMA |  | Ingeniería de Sistemas | 2026-11-04 10:00 |  | Sí |  |
| ar-07 | u-06 Andrea Marcela Giraldo | DOCENTE |  |  | 2026-11-04 11:00 |  | Sí |  |
| ar-08 | u-07 Santiago Henao | ESTUDIANTE |  |  | 2026-11-04 15:00 |  | Sí |  |

### Relaciones

| Relación | Con | Cardinalidad | Tipo |
|---|---|---|---|
| Es asignada a-> | [Usuario](Usuario.md) | AsignacionRol 0..* — 1 Usuario | Composición |
| Asigna coordinación de-> | [Facultad](Facultad.md) | AsignacionRol 0..* — 0..1 Facultad | Asociación |
| Asigna coordinación de-> | [ProgramaAcademico](ProgramaAcademico.md) | AsignacionRol 0..* — 0..1 ProgramaAcademico | Asociación |

## Caracterización

### Caracterización del objeto

| Aspecto | Descripción |
|---|---|
| **¿Qué representa?** | El periodo durante el cual una persona ejerce un rol institucional y, si es coordinador, la Facultad o el Programa que acompaña. |
| **Tipo de objeto** | Entidad interna de agregado |
| **Agregado** | Usuario (entidad interna). |
| **¿Qué conoce? (información que administra)** | El usuario, el rol, la Facultad o el Programa, las fechas de inicio y fin y el motivo de cierre. |
| **¿Qué hace? (responsabilidades)** | • Asignar rol<br>• Cerrar asignación<br>• Consultar titular vigente<br>• Consultar historial de coordinadores |
| **¿Qué decisiones toma?** | • Si está vigente.<br>• Si corresponde a la coordinación de una Facultad o Programa dado.<br>• Si los datos son coherentes con el rol (Facultad solo para Coordinador de Facultad, Programa solo para Coordinador de Programa). |
| **¿Qué decisiones delega?** | • La unicidad del Rector, del Coordinador Académico y de los coordinadores a reglas que consultan las asignaciones vigentes.<br>• La sustitución del coordinador anterior al servicio de aprobación. |
| **¿Con quién colabora?** | Usuario, Facultad, ProgramaAcademico, Rol |
| **¿Qué eventos genera?** | • Rol asignado / coordinador asignado<br>• Asignación cerrada / coordinador reemplazado<br>• Titular consultado<br>• Historial de coordinadores consultado |
| **¿Qué invariantes protege?** | • INV-001 a INV-004: un solo titular vigente por cargo.<br>• INV-009/INV-010: el coordinador acompaña exactamente un Programa o Facultad activos.<br>• RN-043/RN-051: el historial de coordinadores se conserva. |

### Caracterización de atributos

| Nombre atributo | id | usuario | rol | facultad | programa | fechaInicio | fechaFin | vigente | motivoFin |
|---|---|---|---|---|---|---|---|---|---|
| Descripción atributo | Identificador único de la asignación | Usuario al que se asigna el rol | Rol institucional asignado | Facultad que acompaña el Coordinador de Facultad | Programa que acompaña el Coordinador de Programa | Fecha y hora en que inicia la vigencia del rol | Fecha y hora en que termina la vigencia del rol | Indica si la asignación está vigente | Razón por la que terminó la asignación |
| Identificador del objeto de dominio | Sí | No | No | No | No | No | No | No | No |
| Tipo de dato | UUID | Usuario | Rol | Facultad | ProgramaAcademico | FechaHora | FechaHora | Lógico | Texto |
| Obligatorio | Sí | Sí | Sí | Condicional | Condicional | Sí | No | Sí | No |
| Longitud mínima (si aplica) |  |  |  |  |  |  |  |  | 0 |
| Longitud máxima (si aplica) |  |  |  |  |  |  |  |  | 250 |
| Precisión (si aplica) |  |  |  |  |  |  |  |  |  |
| Rango inicial (si aplica) |  |  |  |  |  |  |  |  |  |
| Rango final (si aplica) |  |  |  |  |  |  |  |  |  |
| Formato (si aplica) |  |  |  |  |  |  |  |  |  |
| Reglas especiales (si aplica) | Autogenerado |  | Uno de los 7 roles del catálogo | Obligatoria solo si rol = COORDINADOR_FACULTAD; vacía en otro caso. La Facultad debe estar ACTIVA (INV-010) | Obligatorio solo si rol = COORDINADOR_PROGRAMA; vacío en otro caso. El Programa debe estar activo (INV-009) | Asignada por el sistema | Vacía mientras está vigente; ≥ fechaInicio | Sí cuando fechaFin está vacía | Máx. 250; obligatorio cuando se diligencia fechaFin (ej. 'Reemplazado por nuevo coordinador') |
| Dato calculado | No | No | No | No | No | No | No | Sí | No |
| Quitar espacios en blanco al inicio y al fin | No | No | No | No | No | No | No | No | Sí |
| Dato sensible | No | No | No | No | No | No | No | No | No |
| Valor por defecto (si aplica) |  |  |  |  |  | Fecha actual |  | Sí |  |
| Auto generado | Sí | No | No | No | No | Sí | No | Sí | No |
| Asignar rol | No requerido | Requerido | Requerido | Requerido | Requerido | No requerido | No requerido | No requerido | No requerido |
| Cerrar asignación | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | Requerido |
| Consultar titular vigente | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | No mostrar |
| Consultar historial de coordinadores | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar |

### Combinaciones únicas

| Nombre combinación única | Descripción | Atributos involucrados |
|---|---|---|
| UQ-ASR-1 | Un usuario tiene como máximo una asignación vigente (RN-001, INV-005) | usuario, vigente = Sí |
| UQ-ASR-2 | Solo puede existir un Rector vigente (INV-001) | rol = RECTOR, vigente = Sí |
| UQ-ASR-3 | Solo puede existir un Coordinador Académico vigente (INV-002) | rol = COORDINADOR_ACADEMICO, vigente = Sí |
| UQ-ASR-4 | Cada Facultad tiene máximo un Coordinador de Facultad vigente (INV-003) | facultad, vigente = Sí |
| UQ-ASR-5 | Cada Programa tiene máximo un Coordinador de Programa vigente (INV-004) | programa, vigente = Sí |

### Responsabilidades

| Responsabilidad | Descripción | Entradas · Parámetro | Entradas · Tipo de dato | Entradas · Descripción | Salida · Tipo de dato | Salida · Descripción | Políticas · Política | Políticas · Descripción | Excepción · Detalle | Excepción · ¿Qué hago? |
|---|---|---|---|---|---|---|---|---|---|---|
| **Asignar rol** (`asignar()`) | Abre la asignación vigente de un rol para un usuario; para coordinadores indica la Facultad o el Programa. | usuario | Usuario | Usuario que recibe el rol. | AsignacionRol | Asignación vigente. | P-ASR-001 | Asegurar que el usuario no tenga otra asignación de rol vigente. | El usuario ya tiene un rol vigente. | Rechazar la asignación e informar el rol que tiene. |
|  |  | rol | Rol | Rol asignado. |  |  | P-ASR-002 | Asegurar que no exista otro Rector u otro Coordinador Académico vigente. | Ya existe un titular vigente del cargo. | Rechazar la asignación e informar quién es el titular vigente. |
|  |  | facultad | Facultad | Solo Coordinador de Facultad. |  |  | P-ASR-003 | Asegurar la coherencia del rol: el Coordinador de Facultad indica una Facultad ACTIVA, el Coordinador de Programa un Programa activo, y los demás roles no indican ninguno. | Falta la Facultad o el Programa, o no está activo. | Rechazar la asignación e informar el dato que falta. |
|  |  | programa | ProgramaAcademico | Solo Coordinador de Programa. |  |  | P-ASR-004 | Si la Facultad o el Programa ya tiene coordinador vigente, sustituirlo: cerrar su asignación, desactivar su cuenta y registrar el reemplazo. |  |  |
|  |  |  |  |  |  |  | P-ASR-005 | Registrar en auditoría toda asignación y todo cierre de rol. |  |  |
| **Cerrar asignación** (`cerrar()`) | Termina la vigencia de una asignación de rol sin borrarla. | motivo | String | Razón del cierre. | AsignacionRol | Asignación cerrada. | P-ASR-006 | Asegurar que solo se cierre una asignación vigente, con motivo y fecha de fin no anterior a la de inicio; la asignación cerrada se conserva. | La asignación ya está cerrada o falta el motivo. | Rechazar el cierre. |
|  |  |  |  |  |  |  | P-ASR-005 | Registrar en auditoría toda asignación y todo cierre de rol. |  |  |
| **Consultar titular vigente** (`consultarTitularVigente()`) | Indica quién ocupa hoy un cargo: Rector, Coordinador Académico o coordinador de una Facultad o Programa. | rol | Rol | Cargo consultado. | AsignacionRol | Asignación vigente del cargo. | P-ASR-007 | Al consultar el titular de un cargo, mostrar solo la asignación vigente. | No hay titular vigente. | Informar que el cargo está vacante. |
|  |  | ambito | UUID | Facultad o Programa, si aplica. |  |  |  |  |  |  |
| **Consultar historial de coordinadores** (`consultarHistorialCoordinadores()`) | Lista las asignaciones vigentes y cerradas de la coordinación de una Facultad o Programa. | ambito | UUID | Facultad o Programa. | Lista de AsignacionRol | Historial de coordinadores. | P-ASR-008 | El historial de coordinadores incluye las asignaciones vigentes y cerradas, en orden cronológico. |  |  |

## Eventos

| Actor | Acción | Descripción | Objeto de dominio | Información externa · Información | Información externa · Descripción | Políticas · Número | Sistema externo | Evento | Referencia en el enunciado |
|---|---|---|---|---|---|---|---|---|---|
| Sistema (al aprobar o registrar) | Asignar rol | Abre la asignación vigente de un rol para un usuario; para coordinadores indica la Facultad o el Programa. | AsignacionRol | Asignaciones vigentes | Para verificar unicidad y sustitución. | P-ASR-001 |  | Rol asignado / coordinador asignado | EVT-010, EVT-012, EVT-014, EVT-015, RN-219, RN-222 |
|  |  |  |  |  |  | P-ASR-002 |  |  |  |
|  |  |  |  |  |  | P-ASR-003 |  |  |  |
|  |  |  |  |  |  | P-ASR-004 |  |  |  |
|  |  |  |  |  |  | P-ASR-005 |  |  |  |
| Sistema (sustitución o desactivación) | Cerrar asignación | Termina la vigencia de una asignación de rol sin borrarla. | AsignacionRol |  |  | P-ASR-006 |  | Asignación cerrada / coordinador reemplazado | EVT-011, EVT-013, RN-220, RN-223 |
|  |  |  |  |  |  | P-ASR-005 |  |  |  |
| Usuario activo | Consultar titular vigente | Indica quién ocupa hoy un cargo: Rector, Coordinador Académico o coordinador de una Facultad o Programa. | AsignacionRol |  |  | P-ASR-007 |  | Titular consultado | RF-018, RF-023, RF-076, RF-077 |
| Coordinador Académico / Coordinador de Facultad | Consultar historial de coordinadores | Lista las asignaciones vigentes y cerradas de la coordinación de una Facultad o Programa. | AsignacionRol |  |  | P-ASR-008 |  | Historial de coordinadores consultado | RF-019, RF-024 |

## Políticas

| Número política | Descripción política | Objeto responsable | Regla referenciada |
|---|---|---|---|
| P-ASR-001 | Asegurar que el usuario no tenga otra asignación de rol vigente. | AsignacionRol | RN-001 a RN-004, INV-005, INV-006, D-13 |
| P-ASR-002 | Asegurar que no exista otro Rector u otro Coordinador Académico vigente. | AsignacionRol | INV-001, INV-002, RN-005, RN-011, D-14 |
| P-ASR-003 | Asegurar la coherencia del rol: el Coordinador de Facultad indica una Facultad ACTIVA, el Coordinador de Programa un Programa activo, y los demás roles no indican ninguno. | AsignacionRol | INV-009, INV-010, D-05, P-FAC-006 |
| P-ASR-004 | Si la Facultad o el Programa ya tiene coordinador vigente, sustituirlo: cerrar su asignación, desactivar su cuenta y registrar el reemplazo. | ServicioAprobacion | RN-043, RN-044, RN-051, RN-052, RN-220, RN-223, D-15 |
| P-ASR-005 | Registrar en auditoría toda asignación y todo cierre de rol. | Auditoria | RN-044, RN-052, RN-096, RN-124 |
| P-ASR-006 | Asegurar que solo se cierre una asignación vigente, con motivo y fecha de fin no anterior a la de inicio; la asignación cerrada se conserva. | AsignacionRol | RN-043, RN-051, D-28 |
| P-ASR-007 | Al consultar el titular de un cargo, mostrar solo la asignación vigente. | AsignacionRol | RF-018, RF-023, RF-076, RF-077 |
| P-ASR-008 | El historial de coordinadores incluye las asignaciones vigentes y cerradas, en orden cronológico. | AsignacionRol | RN-043, RN-051, RF-019, RF-024 |

## En el código

| Capa | Clase |
|---|---|
| Dominio (atributos y comportamiento) | `AsignacionRolDominio` (`co.edu.uco.kudos.dominio`) |
| Entidad (tabla) | `AsignacionRolEntidad` |
| DAO | `AsignacionRolDAO` + implementación SQL Server |
| DTO (API) | CargoDto |
| Negocio | CargoNegocioImpl, SolicitudNegocioImpl, AutoridadNegocioImpl |
