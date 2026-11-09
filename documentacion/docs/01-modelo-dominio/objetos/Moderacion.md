# Moderacion

[Inicio](../../../README.md) · [Modelo de dominio](../README.md) · [Diagrama de clases](../../02-diagrama-clases/README.md) · [Modelo entidad-relación](../../03-modelo-entidad-relacion/README.md)

> Revisión y decisión institucional sobre un Kudo o una réplica, tomada por el Rector o el Coordinador Académico. El histórico de vetos son las moderaciones con decisión VETAR.

**Línea de vida:** 7

[Muestreo](#muestreo) · [Caracterización](#caracterización) · [Eventos](#eventos) · [Políticas](#políticas)

## Muestreo

### Atributos

| Atributo | Descripción | Tipo de dato | Obligatorio |
|---|---|---|---|
| id | Identificador único de la moderación | UUID | Sí |
| kudo | Kudo moderado | [Kudo](Kudo.md) | Condicional |
| replica | Réplica moderada | [Replica](Replica.md) | Condicional |
| moderador | Autoridad que revisa y decide | [Usuario](Usuario.md) | Sí |
| estado | Estado de la moderación | [EstadoModeracion](../README.md#enumeraciones) | Sí |
| fechaInicio | Fecha y hora en que inicia la revisión (RN-239) | FechaHora | Sí |
| fechaCierre | Fecha y hora de la decisión (RN-240) | FechaHora | No |
| decision | Decisión tomada | [DecisionModeracion](../README.md#enumeraciones) | No |
| motivoDecision | Justificación de la decisión (RN-250) | Texto | No |

### Datos de muestra

_2 registros de ejemplo. Los identificadores se abrevian (u-01, k-01…) para facilitar la lectura; en el sistema son UUID._

| id | kudo | replica | moderador | estado | fechaInicio | fechaCierre | decision | motivoDecision |
|---|---|---|---|---|---|---|---|---|
| m-01 | k-04 |  | u-03 Carlos Restrepo | CERRADA | 2026-11-07 08:00 | 2026-11-07 08:20 | VETAR | El contenido contiene lenguaje ofensivo contra un estudiante. |
| m-02 |  | r-04 | u-02 María Fernanda Gómez | EN_CURSO | 2026-11-07 09:00 |  |  |  |

### Relaciones

| Relación | Con | Cardinalidad | Tipo |
|---|---|---|---|
| Decide sobre-> | [Kudo](Kudo.md) | Moderacion 0..* — 0..1 Kudo | Asociación |
| Decide sobre-> | [Replica](Replica.md) | Moderacion 0..* — 0..1 Replica | Asociación |
| Es realizada por-> | [Usuario](Usuario.md) | Moderacion 0..* — 1 Usuario | Asociación |
| Resuelve-> | [Reporte](Reporte.md) | Moderacion 0..1 — 0..* Reporte | Asociación |

## Caracterización

### Caracterización del objeto

| Aspecto | Descripción |
|---|---|
| **¿Qué representa?** | La revisión y la decisión institucional (vetar o liberar) sobre un Kudo o una réplica. |
| **Tipo de objeto** | Entidad raíz de agregado |
| **Agregado** | Moderacion (raíz). El servicio de moderación coordina en una transacción la moderación, el contenido y sus reportes. |
| **¿Qué conoce? (información que administra)** | El contenido moderado, el moderador, el estado, las fechas, la decisión y su motivo. |
| **¿Qué hace? (responsabilidades)** | • Iniciar revisión<br>• Vetar contenido<br>• Liberar contenido<br>• Consultar histórico de vetos |
| **¿Qué decisiones toma?** | • Si está en curso.<br>• Que cerrar exige decisión y motivo. |
| **¿Qué decisiones delega?** | • La verificación de que el actor es moderador a la política de moderación.<br>• El cambio de estado del contenido a Kudo o Replica.<br>• Aceptar o rechazar los reportes a Reporte. |
| **¿Con quién colabora?** | Usuario, Kudo, Replica, Reporte, DecisionModeracion |
| **¿Qué eventos genera?** | • Revisión iniciada<br>• Contenido vetado y revisión cerrada<br>• Contenido liberado y revisión cerrada<br>• Histórico de vetos consultado |
| **¿Qué invariantes protege?** | • INV-033: solo Rector y Coordinador Académico moderan.<br>• INV-034/INV-035/RN-249/RN-250: todo veto queda auditado con la evidencia de la decisión. |

### Caracterización de atributos

| Nombre atributo | id | kudo | replica | moderador | estado | fechaInicio | fechaCierre | decision | motivoDecision |
|---|---|---|---|---|---|---|---|---|---|
| Descripción atributo | Identificador único de la moderación | Kudo moderado | Réplica moderada | Autoridad que revisa y decide | Estado de la moderación | Fecha y hora en que inicia la revisión (RN-239) | Fecha y hora de la decisión (RN-240) | Decisión tomada | Justificación de la decisión (RN-250) |
| Identificador del objeto de dominio | Sí | No | No | No | No | No | No | No | No |
| Tipo de dato | UUID | Kudo | Replica | Usuario | EstadoModeracion | FechaHora | FechaHora | DecisionModeracion | Texto |
| Obligatorio | Sí | Condicional | Condicional | Sí | Sí | Sí | No | No | No |
| Longitud mínima (si aplica) |  |  |  |  |  |  |  |  | 10 |
| Longitud máxima (si aplica) |  |  |  |  |  |  |  |  | 500 |
| Precisión (si aplica) |  |  |  |  |  |  |  |  |  |
| Rango inicial (si aplica) |  |  |  |  |  |  |  |  |  |
| Rango final (si aplica) |  |  |  |  |  |  |  |  |  |
| Formato (si aplica) |  |  |  |  |  |  |  |  |  |
| Reglas especiales (si aplica) | Autogenerado | Exactamente uno entre kudo y replica | Exactamente uno entre kudo y replica | Rol vigente RECTOR o COORDINADOR_ACADEMICO (RN-243, D-02) | EN_CURSO al iniciar; CERRADA al decidir | Asignada por el sistema | Obligatoria cuando estado = CERRADA | Obligatoria cuando estado = CERRADA | 10 a 500 caracteres; obligatoria cuando estado = CERRADA |
| Dato calculado | No | No | No | No | No | No | No | No | No |
| Quitar espacios en blanco al inicio y al fin | No | No | No | No | No | No | No | No | Sí |
| Dato sensible | No | No | No | No | No | No | No | No | No |
| Valor por defecto (si aplica) |  |  |  |  | EN_CURSO | Fecha actual |  |  |  |
| Auto generado | Sí | No | No | No | No | Sí | No | No | No |
| Iniciar revisión | No requerido | Requerido | Requerido | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido |
| Vetar contenido | No requerido | No requerido | No requerido | Requerido | No requerido | No requerido | No requerido | Requerido | Requerido |
| Liberar contenido | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | Requerido | Requerido |
| Consultar histórico de vetos | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar |

### Combinaciones únicas

| Nombre combinación única | Descripción | Atributos involucrados |
|---|---|---|
| UQ-MOD-1 | Un contenido tiene máximo una moderación en curso | kudo / replica, estado = EN_CURSO |

### Responsabilidades

| Responsabilidad | Descripción | Entradas · Parámetro | Entradas · Tipo de dato | Entradas · Descripción | Salida · Tipo de dato | Salida · Descripción | Políticas · Política | Políticas · Descripción | Excepción · Detalle | Excepción · ¿Qué hago? |
|---|---|---|---|---|---|---|---|---|---|---|
| **Iniciar revisión** (`iniciar()`) | Un moderador abre la revisión de un contenido reportado. | contenido | Kudo o Replica | Contenido a revisar. | Moderacion | Moderación EN_CURSO. | P-MOD-001 | Asegurar que el moderador tenga rol vigente RECTOR o COORDINADOR_ACADEMICO. | El actor no puede moderar. | Rechazar la operación y registrar el intento. |
|  |  | moderador | Usuario | Rector o Coordinador Académico. |  |  | P-MOD-002 | Asegurar que el contenido esté REPORTADO y no tenga otra moderación en curso. | El contenido no está reportado o ya está en revisión. | Rechazar el inicio de la revisión. |
|  |  |  |  |  |  |  | P-MOD-003 | Al iniciar la revisión, el contenido y sus reportes pendientes pasan a EN_REVISION. |  |  |
| **Vetar contenido** (`vetar()`) | El moderador veta el contenido y cierra la revisión. | moderador | Usuario | Rector o Coordinador Académico. | Moderacion | Moderación CERRADA con decisión VETAR. | P-MOD-001 | Asegurar que el moderador tenga rol vigente RECTOR o COORDINADOR_ACADEMICO. | El actor no puede moderar. | Rechazar la operación y registrar el intento. |
|  |  | motivo | String | Justificación. |  |  | P-MOD-004 | Asegurar que el contenido no esté vetado; si no hay moderación en curso, se crea y se cierra en el mismo acto (veto directo). | El contenido ya está vetado. | Rechazar el veto. |
|  |  |  |  |  |  |  | P-MOD-005 | Asegurar que la decisión tenga un motivo de 10 a 500 caracteres. | Falta el motivo o es muy corto. | Rechazar la decisión. |
|  |  |  |  |  |  |  | P-MOD-006 | Al vetar: el contenido pasa a VETADO, sus reportes abiertos a ACEPTADO, se notifica al autor y se audita la decisión. |  |  |
| **Liberar contenido** (`liberar()`) | El moderador libera el contenido y cierra la revisión. | moderador | Usuario | Rector o Coordinador Académico. | Moderacion | Moderación CERRADA con decisión LIBERAR. | P-MOD-001 | Asegurar que el moderador tenga rol vigente RECTOR o COORDINADOR_ACADEMICO. | El actor no puede moderar. | Rechazar la operación y registrar el intento. |
|  |  | motivo | String | Justificación. |  |  | P-MOD-007 | Asegurar que exista una moderación en curso para liberar el contenido. | El contenido no está en revisión. | Rechazar la liberación. |
|  |  |  |  |  |  |  | P-MOD-005 | Asegurar que la decisión tenga un motivo de 10 a 500 caracteres. | Falta el motivo o es muy corto. | Rechazar la decisión. |
|  |  |  |  |  |  |  | P-MOD-008 | Al liberar: el contenido vuelve a VISIBLE, sus reportes pasan a RECHAZADO y se notifica a los reportantes. |  |  |
| **Consultar histórico de vetos** (`consultarHistoricoVetos()`) | Lista las moderaciones que terminaron en veto. | moderador | Usuario | Quien consulta. | Lista de Moderacion | Vetos realizados. | P-MOD-009 | El histórico de vetos lo consultan solo los moderadores y muestra el contenido original, el motivo y los reportes resueltos. |  |  |
|  |  | filtro | FiltroModeracion | Fecha, moderador, tipo de contenido. |  |  |  |  |  |  |

## Eventos

| Actor | Acción | Descripción | Objeto de dominio | Información externa · Información | Información externa · Descripción | Políticas · Número | Sistema externo | Evento | Referencia en el enunciado |
|---|---|---|---|---|---|---|---|---|---|
| Moderador | Iniciar revisión | Un moderador abre la revisión de un contenido reportado. | Moderacion | Reportes pendientes del contenido | Pasan a revisión. | P-MOD-001 |  | Revisión iniciada | EVT-035 / RN-239 |
|  |  |  |  |  |  | P-MOD-002 |  |  |  |
|  |  |  |  |  |  | P-MOD-003 |  |  |  |
| Moderador | Vetar contenido | El moderador veta el contenido y cierra la revisión. | Moderacion | Reportes abiertos del contenido | Pasan a ACEPTADO. | P-MOD-001 |  | Contenido vetado y revisión cerrada | EVT-027, EVT-031, EVT-036, EVT-038, RN-241 |
|  |  |  |  |  |  | P-MOD-004 |  |  |  |
|  |  |  |  |  |  | P-MOD-005 |  |  |  |
|  |  |  |  |  |  | P-MOD-006 |  |  |  |
| Moderador | Liberar contenido | El moderador libera el contenido y cierra la revisión. | Moderacion | Reportes abiertos del contenido | Pasan a RECHAZADO. | P-MOD-001 |  | Contenido liberado y revisión cerrada | EVT-036, EVT-037, RN-242 |
|  |  |  |  |  |  | P-MOD-007 |  |  |  |
|  |  |  |  |  |  | P-MOD-005 |  |  |  |
|  |  |  |  |  |  | P-MOD-008 |  |  |  |
| Moderador | Consultar histórico de vetos | Lista las moderaciones que terminaron en veto. | Moderacion |  |  | P-MOD-009 |  | Histórico de vetos consultado | CU-015 |

## Políticas

| Número política | Descripción política | Objeto responsable | Regla referenciada |
|---|---|---|---|
| P-MOD-001 | Asegurar que el moderador tenga rol vigente RECTOR o COORDINADOR_ACADEMICO. | PoliticaModeracion | RN-193 a RN-200, RN-243 a RN-248, INV-033, D-02 |
| P-MOD-002 | Asegurar que el contenido esté REPORTADO y no tenga otra moderación en curso. | Moderacion | RN-203, RN-208, UQ-MOD-1 |
| P-MOD-003 | Al iniciar la revisión, el contenido y sus reportes pendientes pasan a EN_REVISION. | ServicioModeracion | RN-203, RN-208, RN-211, P-KUD-014, P-RPL-010, P-RPT-007 |
| P-MOD-004 | Asegurar que el contenido no esté vetado; si no hay moderación en curso, se crea y se cierra en el mismo acto (veto directo). | Moderacion | RN-204, RN-209 |
| P-MOD-005 | Asegurar que la decisión tenga un motivo de 10 a 500 caracteres. | Moderacion | RN-250 |
| P-MOD-006 | Al vetar: el contenido pasa a VETADO, sus reportes abiertos a ACEPTADO, se notifica al autor y se audita la decisión. | ServicioModeracion | RN-205, RN-212, RN-249, INV-034, INV-035, P-KUD-016, P-RPL-012, P-RPT-008 |
| P-MOD-007 | Asegurar que exista una moderación en curso para liberar el contenido. | Moderacion | RN-240 |
| P-MOD-008 | Al liberar: el contenido vuelve a VISIBLE, sus reportes pasan a RECHAZADO y se notifica a los reportantes. | ServicioModeracion | RN-212, RN-242, P-KUD-015, P-RPL-011, P-RPT-009 |
| P-MOD-009 | El histórico de vetos lo consultan solo los moderadores y muestra el contenido original, el motivo y los reportes resueltos. | PoliticaModeracion | CU-015, D-08 |
