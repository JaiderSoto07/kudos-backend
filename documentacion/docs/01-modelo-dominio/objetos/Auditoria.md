# Auditoria

[Inicio](../../../README.md) · [Modelo de dominio](../README.md) · [Diagrama de clases](../../02-diagrama-clases/README.md) · [Modelo entidad-relación](../../03-modelo-entidad-relacion/README.md)

> Registro histórico e inmutable de una operación relevante realizada en la plataforma. Permite reconstruir quién hizo qué, cuándo, sobre qué objeto y por qué.

**Línea de vida:** 2

[Muestreo](#muestreo) · [Caracterización](#caracterización) · [Eventos](#eventos) · [Políticas](#políticas)

## Muestreo

### Atributos

| Atributo | Descripción | Tipo de dato | Obligatorio |
|---|---|---|---|
| id | Identificador único del registro | UUID | Sí |
| fechaHora | Momento en que ocurrió la operación | FechaHora | Sí |
| actor | Usuario que realizó la operación | [Usuario](Usuario.md) | No |
| accion | Evento de dominio registrado | Texto | Sí |
| tipoObjeto | Tipo del objeto afectado | Texto | Sí |
| idObjeto | Identificador del objeto afectado | UUID | Sí |
| estadoAnterior | Estado o valor antes de la operación | Texto | No |
| estadoNuevo | Estado o valor después de la operación | Texto | No |
| motivo | Motivo declarado por el actor | Texto | No |
| detalle | Información adicional del evento | Texto | No |

### Datos de muestra

_4 registros de ejemplo. Los identificadores se abrevian (u-01, k-01…) para facilitar la lectura; en el sistema son UUID._

| id | fechaHora | actor | accion | tipoObjeto | idObjeto | estadoAnterior | estadoNuevo | motivo | detalle |
|---|---|---|---|---|---|---|---|---|---|
| au-01 | 2026-11-04 09:00 | u-03 | COORDINADOR_FACULTAD_REEMPLAZADO | Facultad | f-01 | Coordinador: u-10 | Coordinador: u-04 | Aprobación de nueva coordinadora | {solicitud: sr-02} |
| au-02 | 2026-11-05 10:00 | u-06 | KUDO_PUBLICADO | Kudo | k-01 | CREADO | VISIBLE |  | {destinatario: u-07, categoria: c-01} |
| au-03 | 2026-11-06 08:00 | u-05 | CALIFICACION_ACTUALIZADA | Calificacion | ca-02 | 3 estrellas | 4 estrellas |  |  |
| au-04 | 2026-11-07 08:20 | u-03 | KUDO_VETADO | Kudo | k-04 | EN_REVISION | VETADO | El contenido contiene lenguaje ofensivo contra un estudiante. | {moderacion: m-01, reportes: [rp-02, rp-03]} |

### Relaciones

| Relación | Con | Cardinalidad | Tipo |
|---|---|---|---|
| Registra acción de-> | [Usuario](Usuario.md) | Auditoria 0..* — 0..1 Usuario | Asociación |

## Caracterización

### Caracterización del objeto

| Aspecto | Descripción |
|---|---|
| **¿Qué representa?** | El registro histórico de una operación relevante, que permite reconstruir quién hizo qué, cuándo, sobre qué y por qué. |
| **Tipo de objeto** | Entidad raíz de agregado (inmutable) |
| **Agregado** | Auditoria (raíz). |
| **¿Qué conoce? (información que administra)** | La fecha, el actor, la acción, el objeto afectado, el estado anterior y nuevo, el motivo y el detalle. |
| **¿Qué hace? (responsabilidades)** | • Registrar operación<br>• Consultar auditoría<br>• Consultar actividad propia |
| **¿Qué decisiones toma?** | • Nada cambia una vez registrado (inmutable). |
| **¿Qué decisiones delega?** | • Lo registra la capa de negocio (auditoriaNegocio.registrar) dentro de la misma transacción de la operación (D-26). |
| **¿Con quién colabora?** | Usuario |
| **¿Qué eventos genera?** | • Operación auditada<br>• Auditoría consultada<br>• Actividad propia consultada |
| **¿Qué invariantes protege?** | • POL-006/POL-007: las operaciones críticas son rastreables y reconstruibles.<br>• RN-096 a RN-100. |

### Caracterización de atributos

| Nombre atributo | id | fechaHora | actor | accion | tipoObjeto | idObjeto | estadoAnterior | estadoNuevo | motivo | detalle |
|---|---|---|---|---|---|---|---|---|---|---|
| Descripción atributo | Identificador único del registro | Momento en que ocurrió la operación | Usuario que realizó la operación | Evento de dominio registrado | Tipo del objeto afectado | Identificador del objeto afectado | Estado o valor antes de la operación | Estado o valor después de la operación | Motivo declarado por el actor | Información adicional del evento |
| Identificador del objeto de dominio | Sí | No | No | No | No | No | No | No | No | No |
| Tipo de dato | UUID | FechaHora | Usuario | Texto | Texto | UUID | Texto | Texto | Texto | Texto |
| Obligatorio | Sí | Sí | No | Sí | Sí | Sí | No | No | No | No |
| Longitud mínima (si aplica) |  |  |  | 1 | 1 |  | 0 | 0 | 0 | 0 |
| Longitud máxima (si aplica) |  |  |  | 60 | 40 |  | 500 | 500 | 500 | Sin límite |
| Precisión (si aplica) |  |  |  |  |  |  |  |  |  |  |
| Rango inicial (si aplica) |  |  |  |  |  |  |  |  |  |  |
| Rango final (si aplica) |  |  |  |  |  |  |  |  |  |  |
| Formato (si aplica) |  |  |  | MAYÚSCULAS_CON_GUION_BAJO |  |  |  |  |  | JSON |
| Reglas especiales (si aplica) | Autogenerado | Asignada por el sistema | Vacío cuando la realiza el propio sistema | Código del evento, ej. KUDO_VETADO (máx. 60) | Ej. Kudo, Usuario, Facultad (máx. 40) |  | Máx. 500 | Máx. 500 | Máx. 500 | Sin límite (JSON) |
| Dato calculado | No | No | No | No | No | No | No | No | No | No |
| Quitar espacios en blanco al inicio y al fin | No | No | No | Sí | Sí | No | Sí | Sí | Sí | Sí |
| Dato sensible | No | No | No | No | No | No | No | No | No | No |
| Valor por defecto (si aplica) |  | Fecha actual |  |  |  |  |  |  |  |  |
| Auto generado | Sí | Sí | No | No | No | No | No | No | No | No |
| Registrar operación | No requerido | No requerido | Requerido | Requerido | Requerido | Requerido | Requerido | Requerido | Requerido | Requerido |
| Consultar auditoría | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar |
| Consultar actividad propia | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | No mostrar |

### Combinaciones únicas

No tiene combinaciones únicas además de su identificador.

### Responsabilidades

| Responsabilidad | Descripción | Entradas · Parámetro | Entradas · Tipo de dato | Entradas · Descripción | Salida · Tipo de dato | Salida · Descripción | Políticas · Política | Políticas · Descripción | Excepción · Detalle | Excepción · ¿Qué hago? |
|---|---|---|---|---|---|---|---|---|---|---|
| **Registrar operación** (`registrar()`) | Crea el registro histórico de un evento de dominio (lo llama cada negocio). | datos | AuditoriaDominio | Actor, acción, objeto, estados, motivo y detalle. | Auditoria | Registro de auditoría. | P-AUD-001 | Todo evento de dominio relevante genera un registro de auditoría. |  |  |
|  |  |  |  |  |  |  | P-AUD-002 | Un registro de auditoría no se modifica ni se elimina. | Se intenta modificar un registro. | Rechazar la operación (no existe). |
|  |  |  |  |  |  |  | P-AUD-003 | Guardar actor, acción, objeto, estado anterior, estado nuevo y motivo, para reconstruir la decisión. |  |  |
| **Consultar auditoría** (`consultar()`) | Lista los registros de auditoría del ámbito del actor. | actor | Usuario | Autoridad que consulta. | Página de Auditoria | Registros. | P-AUD-004 | Cada autoridad consulta la auditoría de su ámbito; Rector y Coordinador Académico consultan toda. | El actor no tiene permiso. | Rechazar la consulta. |
|  |  | filtro | FiltroAuditoria | Fecha, actor, acción, objeto. |  |  | P-AUD-005 | La consulta de auditoría es paginada y filtrable por fecha, actor, acción y objeto. |  |  |
| **Consultar actividad propia** (`consultarActividadPropia()`) | Lista la actividad del propio usuario. | usuario | Usuario | Usuario que consulta. | Página de Auditoria | Actividad del usuario. | P-AUD-006 | La actividad propia incluye los registros donde el usuario es el actor o el objeto afectado. |  |  |

## Eventos

| Actor | Acción | Descripción | Objeto de dominio | Información externa · Información | Información externa · Descripción | Políticas · Número | Sistema externo | Evento | Referencia en el enunciado |
|---|---|---|---|---|---|---|---|---|---|
| Sistema (capa de negocio) | Registrar operación | Crea el registro histórico de un evento de dominio (lo llama cada negocio). | Auditoria |  |  | P-AUD-001 |  | Operación auditada | RN-096 a RN-100 |
|  |  |  |  |  |  | P-AUD-002 |  |  |  |
|  |  |  |  |  |  | P-AUD-003 |  |  |  |
| Autoridad | Consultar auditoría | Lista los registros de auditoría del ámbito del actor. | Auditoria |  |  | P-AUD-004 |  | Auditoría consultada | RF-070 |
|  |  |  |  |  |  | P-AUD-005 |  |  |  |
| Usuario activo | Consultar actividad propia | Lista la actividad del propio usuario. | Auditoria |  |  | P-AUD-006 |  | Actividad propia consultada | RN-106 |

## Políticas

| Número política | Descripción política | Objeto responsable | Regla referenciada |
|---|---|---|---|
| P-AUD-001 | Todo evento de dominio relevante genera un registro de auditoría. | Capa de negocio (auditoriaNegocio.registrar) | RN-096 a RN-100, RN-160, POL-006, D-26 |
| P-AUD-002 | Un registro de auditoría no se modifica ni se elimina. | Auditoria | POL-007, D-28 |
| P-AUD-003 | Guardar actor, acción, objeto, estado anterior, estado nuevo y motivo, para reconstruir la decisión. | Auditoria | RN-250, POL-007 |
| P-AUD-004 | Cada autoridad consulta la auditoría de su ámbito; Rector y Coordinador Académico consultan toda. | AsignacionRol (ámbito) | RN-116, RN-128 |
| P-AUD-005 | La consulta de auditoría es paginada y filtrable por fecha, actor, acción y objeto. | Auditoria | RF-070, RNF-010 |
| P-AUD-006 | La actividad propia incluye los registros donde el usuario es el actor o el objeto afectado. | Auditoria | RN-106 |

## En el código

| Capa | Clase |
|---|---|
| Dominio (atributos y comportamiento) | `AuditoriaDominio` (`co.edu.uco.kudos.dominio`) |
| Entidad (tabla) | `AuditoriaEntidad` |
| DAO | `AuditoriaDAO` + implementación SQL Server |
| DTO (API) | AuditoriaDto, PaginaAuditoriaDto |
| Negocio | AuditoriaNegocioImpl (lo llaman todos los negocios) |
