# Calificacion

[Inicio](../../../README.md) · [Modelo de dominio](../README.md) · [Diagrama de clases](../../02-diagrama-clases/README.md) · [Modelo entidad-relación](../../03-modelo-entidad-relacion/README.md)

> Valoración de 1 a 5 estrellas que un usuario asigna a un Kudo o a una réplica. Cada usuario tiene como máximo una calificación activa por contenido.

**Línea de vida:** 7

[Muestreo](#muestreo) · [Caracterización](#caracterización) · [Eventos](#eventos) · [Políticas](#políticas)

## Muestreo

### Atributos

| Atributo | Descripción | Tipo de dato | Obligatorio |
|---|---|---|---|
| id | Identificador único de la calificación | UUID | Sí |
| autor | Usuario que califica (RN-183) | [Usuario](Usuario.md) | Sí |
| kudo | Kudo calificado | [Kudo](Kudo.md) | Condicional |
| replica | Réplica calificada | [Replica](Replica.md) | Condicional |
| estrellas | Valoración asignada (RN-177) | [Estrellas](../README.md#objetos-de-valor) | Sí |
| fechaCalificacion | Fecha y hora en que se registró (RN-182) | FechaHora | Sí |
| fechaModificacion | Fecha y hora del último cambio de estrellas (RN-237) | FechaHora | No |
| activa | Indica si es la calificación activa del usuario para ese contenido | Lógico | Sí |

### Datos de muestra

_4 registros de ejemplo. Los identificadores se abrevian (u-01, k-01…) para facilitar la lectura; en el sistema son UUID._

| id | autor | kudo | replica | estrellas | fechaCalificacion | fechaModificacion | activa |
|---|---|---|---|---|---|---|---|
| ca-01 | u-04 Luisa Ospina | k-01 |  | 5 | 2026-11-05 11:01 |  | Sí |
| ca-02 | u-05 Juan Pablo Zuluaga | k-01 |  | 4 | 2026-11-05 13:00 | 2026-11-06 08:00 | Sí |
| ca-03 | u-03 Carlos Restrepo | k-01 |  | 5 | 2026-11-05 15:00 |  | Sí |
| ca-04 | u-07 Santiago Henao |  | r-01 | 5 | 2026-11-05 11:21 |  | Sí |

### Relaciones

| Relación | Con | Cardinalidad | Tipo |
|---|---|---|---|
| Valora-> | [Kudo](Kudo.md) | Calificacion 0..* — 0..1 Kudo | Asociación |
| Valora-> | [Replica](Replica.md) | Calificacion 0..* — 0..1 Replica | Asociación |
| Es asignada por-> | [Usuario](Usuario.md) | Calificacion 0..* — 1 Usuario | Asociación |

## Caracterización

### Caracterización del objeto

| Aspecto | Descripción |
|---|---|
| **¿Qué representa?** | La valoración de 1 a 5 estrellas que una persona asigna a un Kudo o a una réplica. |
| **Tipo de objeto** | Entidad raíz de agregado |
| **Agregado** | Calificacion (raíz). |
| **¿Qué conoce? (información que administra)** | Su autor, el contenido calificado, las estrellas, cuándo se registró, cuándo se modificó y si está activa. |
| **¿Qué hace? (responsabilidades)** | • Calificar contenido<br>• Modificar calificación<br>• Consultar valoraciones |
| **¿Qué decisiones toma?** | • Que apunta exactamente a un contenido.<br>• Que modificarla conserva el valor anterior para auditoría. |
| **¿Qué decisiones delega?** | • La validación de la escala a Estrellas.<br>• La unicidad por persona y contenido a una regla de negocio.<br>• El recálculo del promedio a Kudo o Replica (a través del evento de calificación). |
| **¿Con quién colabora?** | Usuario, Kudo, Replica, Estrellas |
| **¿Qué eventos genera?** | • Calificación registrada<br>• Calificación actualizada<br>• Valoraciones consultadas |
| **¿Qué invariantes protege?** | • INV-030: escala de 1 a 5.<br>• INV-031/INV-032: una calificación activa por persona y contenido. |

### Caracterización de atributos

| Nombre atributo | id | autor | kudo | replica | estrellas | fechaCalificacion | fechaModificacion | activa |
|---|---|---|---|---|---|---|---|---|
| Descripción atributo | Identificador único de la calificación | Usuario que califica (RN-183) | Kudo calificado | Réplica calificada | Valoración asignada (RN-177) | Fecha y hora en que se registró (RN-182) | Fecha y hora del último cambio de estrellas (RN-237) | Indica si es la calificación activa del usuario para ese contenido |
| Identificador del objeto de dominio | Sí | No | No | No | No | No | No | No |
| Tipo de dato | UUID | Usuario | Kudo | Replica | Estrellas | FechaHora | FechaHora | Lógico |
| Obligatorio | Sí | Sí | Condicional | Condicional | Sí | Sí | No | Sí |
| Longitud mínima (si aplica) |  |  |  |  |  |  |  |  |
| Longitud máxima (si aplica) |  |  |  |  |  |  |  |  |
| Precisión (si aplica) |  |  |  |  |  |  |  |  |
| Rango inicial (si aplica) |  |  |  |  | 1 |  |  |  |
| Rango final (si aplica) |  |  |  |  | 5 |  |  |  |
| Formato (si aplica) |  |  |  |  | Entero |  |  |  |
| Reglas especiales (si aplica) | Autogenerado | Cuenta ACTIVA; no puede ser el autor del contenido (D-23) | Exactamente uno entre kudo y replica (RN-184) | Exactamente uno entre kudo y replica | Entero de 1 a 5 | Asignada por el sistema |  |  |
| Dato calculado | No | No | No | No | No | No | No | No |
| Quitar espacios en blanco al inicio y al fin | No | No | No | No | No | No | No | No |
| Dato sensible | No | No | No | No | No | No | No | No |
| Valor por defecto (si aplica) |  |  |  |  |  | Fecha actual |  | Sí |
| Auto generado | Sí | No | No | No | No | Sí | No | No |
| Calificar contenido | No requerido | Requerido | Requerido | Requerido | Requerido | No requerido | No requerido | No requerido |
| Modificar calificación | Requerido | No requerido | No requerido | No requerido | Requerido | No requerido | No requerido | No requerido |
| Consultar valoraciones | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar |

### Combinaciones únicas

| Nombre combinación única | Descripción | Atributos involucrados |
|---|---|---|
| UQ-CAL-1 | Un usuario tiene máximo una calificación activa por Kudo (RN-180) | autor, kudo, activa = Sí |
| UQ-CAL-2 | Un usuario tiene máximo una calificación activa por réplica (RN-181) | autor, replica, activa = Sí |

### Responsabilidades

| Responsabilidad | Descripción | Entradas · Parámetro | Entradas · Tipo de dato | Entradas · Descripción | Salida · Tipo de dato | Salida · Descripción | Políticas · Política | Políticas · Descripción | Excepción · Detalle | Excepción · ¿Qué hago? |
|---|---|---|---|---|---|---|---|---|---|---|
| **Calificar contenido** (`calificar()`) | Registra la valoración de un usuario sobre un Kudo o una réplica. | autor | Usuario | Quien califica. | Calificacion | Calificación activa. | P-CAL-001 | Asegurar que quien califica tenga cuenta ACTIVA. | El usuario no está activo. | Rechazar la calificación. |
|  |  | contenido | Kudo o Replica | Contenido calificado. |  |  | P-CAL-002 | Asegurar que la calificación apunte exactamente a un Kudo o a una réplica. | No indica contenido o indica ambos. | Rechazar la calificación. |
|  |  | estrellas | Estrellas | De 1 a 5. |  |  | P-CAL-003 | Asegurar que el contenido admita interacción (no esté vetado). | El contenido está vetado. | Rechazar la calificación. |
|  |  |  |  |  |  |  | P-CAL-004 | Asegurar que nadie califique su propio contenido. | El usuario es el autor del contenido. | Rechazar la calificación. |
|  |  |  |  |  |  |  | P-CAL-005 | Asegurar que las estrellas sean un entero de 1 a 5. | El valor está fuera de la escala. | Rechazar la calificación. |
|  |  |  |  |  |  |  | P-CAL-006 | Si el usuario ya tiene una calificación activa sobre el contenido, modificarla en lugar de crear otra. |  |  |
|  |  |  |  |  |  |  | P-CAL-007 | Al registrar o modificar una calificación, recalcular el promedio y la cantidad del contenido. |  |  |
| **Modificar calificación** (`modificarEstrellas()`) | Cambia las estrellas de la calificación activa del usuario. | estrellas | Estrellas | Nuevo valor. | Calificacion | Calificación modificada. | P-CAL-005 | Asegurar que las estrellas sean un entero de 1 a 5. | El valor está fuera de la escala. | Rechazar la calificación. |
|  |  |  |  |  |  |  | P-CAL-008 | Asegurar que solo el autor modifique su calificación activa y registrar el valor anterior y el nuevo. | La calificación no es del usuario o no está activa. | Rechazar la modificación. |
|  |  |  |  |  |  |  | P-CAL-007 | Al registrar o modificar una calificación, recalcular el promedio y la cantidad del contenido. |  |  |
| **Consultar valoraciones** (`consultarValoraciones()`) | Muestra el resumen de calificaciones de un contenido. | contenido | UUID | Kudo o réplica. | ResumenValoraciones | Promedio, cantidad, distribución y calificación propia. | P-CAL-009 | Las valoraciones muestran promedio, cantidad, distribución por estrellas y la calificación del propio usuario. |  |  |

## Eventos

| Actor | Acción | Descripción | Objeto de dominio | Información externa · Información | Información externa · Descripción | Políticas · Número | Sistema externo | Evento | Referencia en el enunciado |
|---|---|---|---|---|---|---|---|---|---|
| Usuario activo | Calificar contenido | Registra la valoración de un usuario sobre un Kudo o una réplica. | Calificacion | Calificación activa existente | Para modificar en lugar de duplicar. | P-CAL-001 |  | Calificación registrada | EVT-032 / RN-236 |
|  |  |  |  |  |  | P-CAL-002 |  |  |  |
|  |  |  |  |  |  | P-CAL-003 |  |  |  |
|  |  |  |  |  |  | P-CAL-004 |  |  |  |
|  |  |  |  |  |  | P-CAL-005 |  |  |  |
|  |  |  |  |  |  | P-CAL-006 |  |  |  |
|  |  |  |  |  |  | P-CAL-007 |  |  |  |
| Usuario activo | Modificar calificación | Cambia las estrellas de la calificación activa del usuario. | Calificacion |  |  | P-CAL-005 |  | Calificación actualizada | EVT-033 / RN-237 |
|  |  |  |  |  |  | P-CAL-008 |  |  |  |
|  |  |  |  |  |  | P-CAL-007 |  |  |  |
| Usuario activo | Consultar valoraciones | Muestra el resumen de calificaciones de un contenido. | Calificacion |  |  | P-CAL-009 |  | Valoraciones consultadas | CU-009 |

## Políticas

| Número política | Descripción política | Objeto responsable | Regla referenciada |
|---|---|---|---|
| P-CAL-001 | Asegurar que quien califica tenga cuenta ACTIVA. | Usuario | RN-183 |
| P-CAL-002 | Asegurar que la calificación apunte exactamente a un Kudo o a una réplica. | Calificacion | RN-184 |
| P-CAL-003 | Asegurar que el contenido admita interacción (no esté vetado). | Kudo / Replica | P-KUD-018, P-RPL-014 |
| P-CAL-004 | Asegurar que nadie califique su propio contenido. | Calificacion | D-23 |
| P-CAL-005 | Asegurar que las estrellas sean un entero de 1 a 5. | Estrellas | RN-177, INV-030 |
| P-CAL-006 | Si el usuario ya tiene una calificación activa sobre el contenido, modificarla en lugar de crear otra. | Calificacion | RN-178 a RN-181, INV-031, INV-032, P-CAL-008 |
| P-CAL-007 | Al registrar o modificar una calificación, recalcular el promedio y la cantidad del contenido. | Kudo / Replica | RN-185, RN-238, P-KUD-017, P-RPL-013 |
| P-CAL-008 | Asegurar que solo el autor modifique su calificación activa y registrar el valor anterior y el nuevo. | Calificacion | RN-237, D-23 |
| P-CAL-009 | Las valoraciones muestran promedio, cantidad, distribución por estrellas y la calificación del propio usuario. | Calificacion | CU-009 |
