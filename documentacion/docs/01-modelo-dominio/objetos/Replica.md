# Replica

[Inicio](../../../README.md) · [Modelo de dominio](../README.md) · [Diagrama de clases](../../02-diagrama-clases/README.md) · [Modelo entidad-relación](../../03-modelo-entidad-relacion/README.md)

> Intervención en la conversación de un Kudo. Su padre es el Kudo (réplica de primer nivel) u otra réplica (respuesta).

**Línea de vida:** 6

[Muestreo](#muestreo) · [Caracterización](#caracterización) · [Eventos](#eventos) · [Políticas](#políticas)

## Muestreo

### Atributos

| Atributo | Descripción | Tipo de dato | Obligatorio |
|---|---|---|---|
| id | Identificador único de la réplica | UUID | Sí |
| autor | Usuario que escribe la réplica (RN-168) | [Usuario](Usuario.md) | Sí |
| kudo | Kudo raíz de la conversación | [Kudo](Kudo.md) | Sí |
| replicaPadre | Réplica a la que responde (RN-162) | [Replica](Replica.md) | No |
| contenido | Texto de la réplica | [ContenidoTexto](../README.md#objetos-de-valor) | Sí |
| fechaCreacion | Fecha y hora de publicación (RN-169) | FechaHora | Sí |
| estado | Estado del ciclo de vida del contenido | [EstadoContenido](../README.md#enumeraciones) | Sí |
| promedioCalificacion | Promedio de estrellas de sus calificaciones activas | Decimal | Sí |
| cantidadCalificaciones | Número de calificaciones activas | Entero | Sí |

### Datos de muestra

_4 registros de ejemplo. Los identificadores se abrevian (u-01, k-01…) para facilitar la lectura; en el sistema son UUID._

| id | autor | kudo | replicaPadre | contenido | fechaCreacion | estado | promedioCalificacion | cantidadCalificaciones |
|---|---|---|---|---|---|---|---|---|
| r-01 | u-04 Luisa Ospina | k-01 |  | ¡Felicitaciones Santiago! Orgullo de la Facultad. | 2026-11-05 11:00 | VISIBLE | 5.00 | 1 |
| r-02 | u-07 Santiago Henao | k-01 | r-01 | Muchas gracias, profe Luisa. | 2026-11-05 11:20 | VISIBLE | 0 | 0 |
| r-03 | u-06 Andrea Giraldo | k-01 | r-02 | ¡Te lo ganaste! | 2026-11-05 12:00 | VISIBLE | 0 | 0 |
| r-04 | u-09 Mateo Arango | k-02 |  | (réplica con spam) | 2026-11-06 07:00 | EN_REVISION | 0 | 0 |

### Relaciones

| Relación | Con | Cardinalidad | Tipo |
|---|---|---|---|
| Pertenece a-> | [Kudo](Kudo.md) | Replica 0..* — 1 Kudo | Composición |
| Es respondida por / Responde a | [Replica](Replica.md) | 0..1 — 0..* | Asociación |
| Es escrita por-> | [Usuario](Usuario.md) | Replica 0..* — 1 Usuario | Asociación |
| Recibe-> | [Calificacion](Calificacion.md) | Replica 0..1 — 0..* Calificacion | Asociación |
| Recibe-> | [Reporte](Reporte.md) | Replica 0..1 — 0..* Reporte | Asociación |
| Es revisada en-> | [Moderacion](Moderacion.md) | Replica 0..1 — 0..* Moderacion | Asociación |

## Caracterización

### Caracterización del objeto

| Aspecto | Descripción |
|---|---|
| **¿Qué representa?** | Una intervención en la conversación de un Kudo, que responde al Kudo o a otra réplica. |
| **Tipo de objeto** | Entidad raíz de agregado |
| **Agregado** | Replica (raíz). Referencia al Kudo raíz y a su réplica padre por identificador. |
| **¿Qué conoce? (información que administra)** | Su autor, su Kudo raíz, su réplica padre, su contenido, su fecha, su estado y el resumen de sus calificaciones. |
| **¿Qué hace? (responsabilidades)** | • Responder Kudo<br>• Responder réplica<br>• Consultar conversación<br>• Marcar reportada<br>• Iniciar revisión<br>• Liberar réplica<br>• Vetar réplica<br>• Decidir si admite interacción<br>• Actualizar resumen de calificaciones |
| **¿Qué decisiones toma?** | • Si admite interacción (no VETADA).<br>• Qué transición de estado es válida (State).<br>• Su promedio de calificaciones. |
| **¿Qué decisiones delega?** | • La validación del contenido a ContenidoTexto.<br>• La verificación de que el Kudo admite interacción a Kudo.<br>• El armado del árbol de la conversación a Conversacion (patrón Composite). |
| **¿Con quién colabora?** | Usuario, Kudo, Replica, ContenidoTexto, EstadoContenido, Calificacion, Reporte, Moderacion |
| **¿Qué eventos genera?** | • Réplica creada<br>• Réplica creada y réplica respondida<br>• Conversación consultada<br>• Réplica reportada<br>• Revisión iniciada<br>• Contenido liberado<br>• Réplica vetada<br>• Indicador recalculado |
| **¿Qué invariantes protege?** | • INV-025 a INV-029: un autor, un único padre (Kudo o réplica), sin autorreferencia ni ciclos.<br>• RN-206 a RN-209: ciclo de vida del contenido. |

### Caracterización de atributos

| Nombre atributo | id | autor | kudo | replicaPadre | contenido | fechaCreacion | estado | promedioCalificacion | cantidadCalificaciones |
|---|---|---|---|---|---|---|---|---|---|
| Descripción atributo | Identificador único de la réplica | Usuario que escribe la réplica (RN-168) | Kudo raíz de la conversación | Réplica a la que responde (RN-162) | Texto de la réplica | Fecha y hora de publicación (RN-169) | Estado del ciclo de vida del contenido | Promedio de estrellas de sus calificaciones activas | Número de calificaciones activas |
| Identificador del objeto de dominio | Sí | No | No | No | No | No | No | No | No |
| Tipo de dato | UUID | Usuario | Kudo | Replica | ContenidoTexto | FechaHora | EstadoContenido | Decimal | Entero |
| Obligatorio | Sí | Sí | Sí | No | Sí | Sí | Sí | Sí | Sí |
| Longitud mínima (si aplica) |  |  |  |  | 1 |  |  |  |  |
| Longitud máxima (si aplica) |  |  |  |  | Sin límite |  |  |  |  |
| Precisión (si aplica) |  |  |  |  |  |  |  | 2 |  |
| Rango inicial (si aplica) |  |  |  |  |  |  |  | 0 | 0 |
| Rango final (si aplica) |  |  |  |  |  |  |  | 5 |  |
| Formato (si aplica) |  |  |  |  | Texto libre |  |  |  |  |
| Reglas especiales (si aplica) | Autogenerado | Cuenta ACTIVA | No puede estar VETADO al crear la réplica (D-21) | Vacía si responde directamente al Kudo. Si existe: pertenece al mismo Kudo, no está VETADA y nunca cambia (INV-026 a INV-029) | Sin límite de longitud (RN-170); no vacío | Asignada por el sistema | Inicia CREADA y al publicarse pasa a VISIBLE (RN-206) | 0 a 5, dos decimales | ≥ 0 |
| Dato calculado | No | No | No | No | No | No | No | Sí | Sí |
| Quitar espacios en blanco al inicio y al fin | No | No | No | No | Sí | No | No | No | No |
| Dato sensible | No | No | No | No | No | No | No | No | No |
| Valor por defecto (si aplica) |  |  |  |  |  | Fecha actual | CREADO | 0 | 0 |
| Auto generado | Sí | No | No | No | No | Sí | No | Sí | Sí |
| Responder Kudo | No requerido | Requerido | Requerido | No requerido | Requerido | No requerido | No requerido | No requerido | No requerido |
| Responder réplica | No requerido | Requerido | Requerido | Requerido | Requerido | No requerido | No requerido | No requerido | No requerido |
| Consultar conversación | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar |
| Marcar reportada | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido |
| Iniciar revisión | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido |
| Liberar réplica | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido |
| Vetar réplica | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido |
| Decidir si admite interacción | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido |
| Actualizar resumen de calificaciones | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido |

### Combinaciones únicas

No tiene combinaciones únicas además de su identificador.

### Responsabilidades

| Responsabilidad | Descripción | Entradas · Parámetro | Entradas · Tipo de dato | Entradas · Descripción | Salida · Tipo de dato | Salida · Descripción | Políticas · Política | Políticas · Descripción | Excepción · Detalle | Excepción · ¿Qué hago? |
|---|---|---|---|---|---|---|---|---|---|---|
| **Responder Kudo** (`responderKudo()`) | Agrega una réplica de primer nivel a la conversación de un Kudo. | autor | Usuario | Quien responde. | Replica | Réplica VISIBLE. | P-RPL-001 | Asegurar que el autor tenga cuenta ACTIVA. | El autor no está activo. | Rechazar la réplica. |
|  |  | kudo | Kudo | Kudo raíz. |  |  | P-RPL-002 | Asegurar que el Kudo raíz admita interacción (no esté vetado). | La conversación está cerrada. | Rechazar la réplica. |
|  |  | contenido | ContenidoTexto | Texto. |  |  | P-RPL-003 | Asegurar que el contenido no esté vacío; se guarda completo y sin límite. | El contenido está vacío. | Rechazar la réplica. |
|  |  |  |  |  |  |  | P-RPL-004 | Toda réplica inicia en CREADA y al publicarse pasa a VISIBLE. |  |  |
|  |  |  |  |  |  |  | P-RPL-005 | Notificar al autor del Kudo que recibió una réplica. |  |  |
| **Responder réplica** (`responderReplica()`) | Agrega una respuesta a una réplica existente. | autor | Usuario | Quien responde. | Replica | Réplica hija VISIBLE. | P-RPL-001 | Asegurar que el autor tenga cuenta ACTIVA. | El autor no está activo. | Rechazar la réplica. |
|  |  | padre | Replica | Réplica respondida. |  |  | P-RPL-002 | Asegurar que el Kudo raíz admita interacción (no esté vetado). | La conversación está cerrada. | Rechazar la réplica. |
|  |  | contenido | ContenidoTexto | Texto. |  |  | P-RPL-003 | Asegurar que el contenido no esté vacío; se guarda completo y sin límite. | El contenido está vacío. | Rechazar la réplica. |
|  |  |  |  |  |  |  | P-RPL-004 | Toda réplica inicia en CREADA y al publicarse pasa a VISIBLE. |  |  |
|  |  |  |  |  |  |  | P-RPL-006 | Asegurar que la réplica padre exista, no esté vetada y pertenezca al mismo Kudo; el padre se fija al crear y no cambia. | La réplica padre no existe, está vetada o es de otro Kudo. | Rechazar la respuesta. |
|  |  |  |  |  |  |  | P-RPL-007 | Notificar al autor de la réplica respondida. |  |  |
| **Consultar conversación** (`consultarConversacion()`) | Arma el árbol completo de réplicas de un Kudo. | kudo | UUID | Kudo raíz. | Conversacion | Árbol de réplicas. | P-RPL-008 | La conversación se muestra como árbol completo en orden cronológico; las réplicas vetadas aparecen como "Contenido vetado" y conservan sus respuestas. |  |  |
| **Marcar reportada** (`marcarReportada()`) | Registra que la réplica recibió un reporte. | Ninguno | - | No requiere datos de entrada. | Replica | Réplica REPORTADA (o sin cambio). | P-RPL-009 | Una réplica VISIBLE pasa a REPORTADA con su primer reporte. | La réplica está vetada. | Rechazar el reporte. |
| **Iniciar revisión** (`iniciarRevision()`) | La réplica entra en revisión. | Ninguno | - | No requiere datos de entrada. | Replica | Réplica EN_REVISION. | P-RPL-010 | Solo una réplica REPORTADA puede pasar a EN_REVISION. | La réplica no está reportada. | Rechazar el inicio de la revisión. |
| **Liberar réplica** (`liberar()`) | La réplica vuelve a estar visible. | Ninguno | - | No requiere datos de entrada. | Replica | Réplica VISIBLE. | P-RPL-011 | Solo una réplica EN_REVISION puede liberarse; vuelve a VISIBLE. | La réplica no está en revisión. | Rechazar la liberación. |
| **Vetar réplica** (`vetar()`) | Inhabilita la réplica de forma definitiva. | Ninguno | - | No requiere datos de entrada. | Replica | Réplica VETADA. | P-RPL-012 | Una réplica no vetada puede vetarse; VETADA es final. | La réplica ya está vetada. | Rechazar el veto. |
| **Decidir si admite interacción** (`admiteInteraccion()`) | Indica si la réplica acepta respuestas, calificaciones y reportes. | Ninguno | - | No requiere datos de entrada. | boolean | Verdadero si no está vetada. | P-RPL-014 | Una réplica admite respuestas, calificaciones y reportes solo si no está VETADA. | La réplica está vetada. | Rechazar la interacción. |
| **Actualizar resumen de calificaciones** (`actualizarResumenCalificaciones()`) | Recalcula el promedio y la cantidad de calificaciones activas. | calificaciones | Lista de Estrellas | Calificaciones activas. | Replica | Réplica con resumen actualizado. | P-RPL-013 | El promedio de calificaciones es el promedio de las calificaciones activas, con dos decimales. |  |  |

## Eventos

| Actor | Acción | Descripción | Objeto de dominio | Información externa · Información | Información externa · Descripción | Políticas · Número | Sistema externo | Evento | Referencia en el enunciado |
|---|---|---|---|---|---|---|---|---|---|
| Usuario activo | Responder Kudo | Agrega una réplica de primer nivel a la conversación de un Kudo. | Replica |  |  | P-RPL-001 |  | Réplica creada | EVT-028 / RN-232 |
|  |  |  |  |  |  | P-RPL-002 |  |  |  |
|  |  |  |  |  |  | P-RPL-003 |  |  |  |
|  |  |  |  |  |  | P-RPL-004 |  |  |  |
|  |  |  |  |  |  | P-RPL-005 |  |  |  |
| Usuario activo | Responder réplica | Agrega una respuesta a una réplica existente. | Replica |  |  | P-RPL-001 |  | Réplica creada y réplica respondida | EVT-028, EVT-029 / RN-233 |
|  |  |  |  |  |  | P-RPL-002 |  |  |  |
|  |  |  |  |  |  | P-RPL-003 |  |  |  |
|  |  |  |  |  |  | P-RPL-004 |  |  |  |
|  |  |  |  |  |  | P-RPL-006 |  |  |  |
|  |  |  |  |  |  | P-RPL-007 |  |  |  |
| Usuario activo | Consultar conversación | Arma el árbol completo de réplicas de un Kudo. | Replica |  |  | P-RPL-008 |  | Conversación consultada | RF-055 |
| Sistema (al crear un reporte) | Marcar reportada | Registra que la réplica recibió un reporte. | Replica |  |  | P-RPL-009 |  | Réplica reportada | EVT-030 / RN-234 |
| Sistema (desde ServicioModeracion) | Iniciar revisión | La réplica entra en revisión. | Replica |  |  | P-RPL-010 |  | Revisión iniciada | EVT-035 / RN-239 |
| Sistema (desde ServicioModeracion) | Liberar réplica | La réplica vuelve a estar visible. | Replica |  |  | P-RPL-011 |  | Contenido liberado | EVT-037 / RN-242 |
| Sistema (desde ServicioModeracion) | Vetar réplica | Inhabilita la réplica de forma definitiva. | Replica |  |  | P-RPL-012 |  | Réplica vetada | EVT-031 / RN-235 |
| Sistema | Decidir si admite interacción | Indica si la réplica acepta respuestas, calificaciones y reportes. | Replica |  |  | P-RPL-014 |  | (decisión, sin evento) |  |
| Sistema (suscriptor de calificaciones) | Actualizar resumen de calificaciones | Recalcula el promedio y la cantidad de calificaciones activas. | Replica |  |  | P-RPL-013 |  | Indicador recalculado | EVT-034 / RN-238 |

## Políticas

| Número política | Descripción política | Objeto responsable | Regla referenciada |
|---|---|---|---|
| P-RPL-001 | Asegurar que el autor tenga cuenta ACTIVA. | Usuario | RN-168 |
| P-RPL-002 | Asegurar que el Kudo raíz admita interacción (no esté vetado). | Kudo | RN-205, D-21, P-KUD-018 |
| P-RPL-003 | Asegurar que el contenido no esté vacío; se guarda completo y sin límite. | ContenidoTexto | RN-170 |
| P-RPL-004 | Toda réplica inicia en CREADA y al publicarse pasa a VISIBLE. | EstadoContenido | RN-206 |
| P-RPL-005 | Notificar al autor del Kudo que recibió una réplica. | Notificacion | D-27 |
| P-RPL-006 | Asegurar que la réplica padre exista, no esté vetada y pertenezca al mismo Kudo; el padre se fija al crear y no cambia. | Replica | INV-026 a INV-029, D-22 |
| P-RPL-007 | Notificar al autor de la réplica respondida. | Notificacion | RN-233, D-27 |
| P-RPL-008 | La conversación se muestra como árbol completo en orden cronológico; las réplicas vetadas aparecen como "Contenido vetado" y conservan sus respuestas. | Conversacion | RN-163, RN-164, D-21, D-22 |
| P-RPL-009 | Una réplica VISIBLE pasa a REPORTADA con su primer reporte. | EstadoContenido | RN-207 |
| P-RPL-010 | Solo una réplica REPORTADA puede pasar a EN_REVISION. | EstadoContenido | RN-208 |
| P-RPL-011 | Solo una réplica EN_REVISION puede liberarse; vuelve a VISIBLE. | EstadoContenido | RN-242 |
| P-RPL-012 | Una réplica no vetada puede vetarse; VETADA es final. | EstadoContenido | RN-209, D-21 |
| P-RPL-013 | El promedio de calificaciones es el promedio de las calificaciones activas, con dos decimales. | Replica | RN-185, RN-238 |
| P-RPL-014 | Una réplica admite respuestas, calificaciones y reportes solo si no está VETADA. | EstadoContenido | D-21 |
