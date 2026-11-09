# Reporte

[Inicio](../../../README.md) · [Modelo de dominio](../README.md) · [Diagrama de clases](../../02-diagrama-clases/README.md) · [Modelo entidad-relación](../../03-modelo-entidad-relacion/README.md)

> Solicitud de revisión que un usuario hace sobre un Kudo o una réplica que considera inapropiado.

**Línea de vida:** 7

[Muestreo](#muestreo) · [Caracterización](#caracterización) · [Eventos](#eventos) · [Políticas](#políticas)

## Muestreo

### Atributos

| Atributo | Descripción | Tipo de dato | Obligatorio |
|---|---|---|---|
| id | Identificador único del reporte | UUID | Sí |
| reportante | Usuario que reporta (RN-188) | [Usuario](Usuario.md) | Sí |
| kudo | Kudo reportado | [Kudo](Kudo.md) | Condicional |
| replica | Réplica reportada | [Replica](Replica.md) | Condicional |
| motivo | Motivo del reporte (RN-190) | [MotivoReporte](../README.md#enumeraciones) | Sí |
| descripcion | Explicación del reportante | Texto | Condicional |
| fechaReporte | Fecha y hora del reporte (RN-189) | FechaHora | Sí |
| estado | Estado del reporte | [EstadoReporte](../README.md#enumeraciones) | Sí |
| moderacion | Moderación que resolvió el reporte | [Moderacion](Moderacion.md) | No |

### Datos de muestra

_4 registros de ejemplo. Los identificadores se abrevian (u-01, k-01…) para facilitar la lectura; en el sistema son UUID._

| id | reportante | kudo | replica | motivo | descripcion | fechaReporte | estado | moderacion |
|---|---|---|---|---|---|---|---|---|
| rp-01 | u-06 Andrea Giraldo | k-03 |  | INFORMACION_FALSA |  | 2026-11-06 10:00 | PENDIENTE |  |
| rp-02 | u-07 Santiago Henao | k-04 |  | LENGUAJE_OFENSIVO |  | 2026-11-06 20:10 | ACEPTADO | m-01 |
| rp-03 | u-05 Juan Pablo Zuluaga | k-04 |  | ACOSO |  | 2026-11-06 20:30 | ACEPTADO | m-01 |
| rp-04 | u-06 Andrea Giraldo |  | r-04 | SPAM |  | 2026-11-06 07:30 | EN_REVISION | m-02 |

### Relaciones

| Relación | Con | Cardinalidad | Tipo |
|---|---|---|---|
| Denuncia-> | [Kudo](Kudo.md) | Reporte 0..* — 0..1 Kudo | Asociación |
| Denuncia-> | [Replica](Replica.md) | Reporte 0..* — 0..1 Replica | Asociación |
| Es hecho por-> | [Usuario](Usuario.md) | Reporte 0..* — 1 Usuario | Asociación |
| Es resuelto en-> | [Moderacion](Moderacion.md) | Reporte 0..* — 0..1 Moderacion | Asociación |

## Caracterización

### Caracterización del objeto

| Aspecto | Descripción |
|---|---|
| **¿Qué representa?** | La solicitud de un usuario para que la autoridad revise un contenido que considera inapropiado. |
| **Tipo de objeto** | Entidad raíz de agregado |
| **Agregado** | Reporte (raíz). |
| **¿Qué conoce? (información que administra)** | El reportante, el contenido reportado, el motivo, la descripción, la fecha, su estado y la moderación que lo resolvió. |
| **¿Qué hace? (responsabilidades)** | • Reportar contenido<br>• Pasar a revisión<br>• Aceptar reporte<br>• Rechazar reporte<br>• Consultar reportes |
| **¿Qué decisiones toma?** | • Si está abierto (PENDIENTE o EN_REVISION).<br>• Qué transición de estado es válida.<br>• Que el motivo OTRO exige descripción. |
| **¿Qué decisiones delega?** | • Marcar el contenido como reportado a Kudo o Replica.<br>• La unicidad de reportes abiertos a una regla de negocio. |
| **¿Con quién colabora?** | Usuario, Kudo, Replica, Moderacion, MotivoReporte |
| **¿Qué eventos genera?** | • Kudo reportado / réplica reportada<br>• Revisión iniciada<br>• Revisión cerrada<br>• Reportes consultados |
| **¿Qué invariantes protege?** | • RN-188 a RN-190: reportante, fecha y motivo.<br>• RN-210 a RN-212: ciclo de vida del reporte. |

### Caracterización de atributos

| Nombre atributo | id | reportante | kudo | replica | motivo | descripcion | fechaReporte | estado | moderacion |
|---|---|---|---|---|---|---|---|---|---|
| Descripción atributo | Identificador único del reporte | Usuario que reporta (RN-188) | Kudo reportado | Réplica reportada | Motivo del reporte (RN-190) | Explicación del reportante | Fecha y hora del reporte (RN-189) | Estado del reporte | Moderación que resolvió el reporte |
| Identificador del objeto de dominio | Sí | No | No | No | No | No | No | No | No |
| Tipo de dato | UUID | Usuario | Kudo | Replica | MotivoReporte | Texto | FechaHora | EstadoReporte | Moderacion |
| Obligatorio | Sí | Sí | Condicional | Condicional | Sí | Condicional | Sí | Sí | No |
| Longitud mínima (si aplica) |  |  |  |  |  | 0 |  |  |  |
| Longitud máxima (si aplica) |  |  |  |  |  | 500 |  |  |  |
| Precisión (si aplica) |  |  |  |  |  |  |  |  |  |
| Rango inicial (si aplica) |  |  |  |  |  |  |  |  |  |
| Rango final (si aplica) |  |  |  |  |  |  |  |  |  |
| Formato (si aplica) |  |  |  |  |  |  |  |  |  |
| Reglas especiales (si aplica) | Autogenerado | Cuenta ACTIVA; no puede ser el autor del contenido (D-24) | Exactamente uno entre kudo y replica; no VETADO | Exactamente uno entre kudo y replica; no VETADA | Catálogo de CU-010 | Máx. 500; obligatoria si motivo = OTRO | Asignada por el sistema | RN-210 a RN-212 | Se asigna al iniciar la revisión |
| Dato calculado | No | No | No | No | No | No | No | No | No |
| Quitar espacios en blanco al inicio y al fin | No | No | No | No | No | Sí | No | No | No |
| Dato sensible | No | No | No | No | No | No | No | No | No |
| Valor por defecto (si aplica) |  |  |  |  |  |  | Fecha actual | PENDIENTE |  |
| Auto generado | Sí | No | No | No | No | No | Sí | No | No |
| Reportar contenido | No requerido | Requerido | Requerido | Requerido | Requerido | Requerido | No requerido | No requerido | No requerido |
| Pasar a revisión | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | Requerido |
| Aceptar reporte | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido |
| Rechazar reporte | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido |
| Consultar reportes | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar |

### Combinaciones únicas

| Nombre combinación única | Descripción | Atributos involucrados |
|---|---|---|
| UQ-REP-1 | Un usuario no puede tener dos reportes abiertos sobre el mismo Kudo | reportante, kudo, estado ∈ {PENDIENTE, EN_REVISION} |
| UQ-REP-2 | Un usuario no puede tener dos reportes abiertos sobre la misma réplica | reportante, replica, estado ∈ {PENDIENTE, EN_REVISION} |

### Responsabilidades

| Responsabilidad | Descripción | Entradas · Parámetro | Entradas · Tipo de dato | Entradas · Descripción | Salida · Tipo de dato | Salida · Descripción | Políticas · Política | Políticas · Descripción | Excepción · Detalle | Excepción · ¿Qué hago? |
|---|---|---|---|---|---|---|---|---|---|---|
| **Reportar contenido** (`crear()`) | Registra el reporte de un usuario sobre un Kudo o una réplica. | reportante | Usuario | Quien reporta. | Reporte | Reporte PENDIENTE. | P-RPT-001 | Asegurar que el reportante tenga cuenta ACTIVA. | El usuario no está activo. | Rechazar el reporte. |
|  |  | contenido | Kudo o Replica | Contenido reportado. |  |  | P-RPT-002 | Asegurar que el reporte apunte exactamente a un Kudo o a una réplica. | No indica contenido o indica ambos. | Rechazar el reporte. |
|  |  | motivo | MotivoReporte | Motivo. |  |  | P-RPT-003 | Asegurar que el contenido no esté vetado ni sea del propio reportante. | El contenido está vetado o es propio. | Rechazar el reporte. |
|  |  | descripcion | String | Obligatoria si el motivo es OTRO. |  |  | P-RPT-004 | Asegurar que el motivo sea del catálogo; si es OTRO, la descripción es obligatoria. | Falta el motivo o la descripción. | Rechazar el reporte. |
|  |  |  |  |  |  |  | P-RPT-005 | Asegurar que el usuario no tenga otro reporte abierto sobre el mismo contenido. | Ya existe un reporte abierto. | Rechazar e informar que su reporte está en trámite. |
|  |  |  |  |  |  |  | P-RPT-006 | Todo reporte inicia PENDIENTE (o EN_REVISION si el contenido ya está en revisión) y marca el contenido como reportado. |  |  |
| **Pasar a revisión** (`pasarARevision()`) | El reporte entra en la moderación en curso. | moderacion | Moderacion | Moderación en curso. | Reporte | Reporte EN_REVISION. | P-RPT-007 | Solo un reporte PENDIENTE pasa a EN_REVISION. |  |  |
| **Aceptar reporte** (`aceptar()`) | El reporte queda aceptado porque el contenido fue vetado. | Ninguno | - | No requiere datos de entrada. | Reporte | Reporte ACEPTADO. | P-RPT-008 | Un reporte abierto pasa a ACEPTADO cuando la moderación veta el contenido. | El reporte ya fue resuelto. | Ignorar el reporte ya resuelto. |
| **Rechazar reporte** (`rechazar()`) | El reporte queda rechazado porque el contenido fue liberado. | Ninguno | - | No requiere datos de entrada. | Reporte | Reporte RECHAZADO. | P-RPT-009 | Un reporte abierto pasa a RECHAZADO cuando la moderación libera el contenido; se notifica al reportante. | El reporte ya fue resuelto. | Ignorar el reporte ya resuelto. |
| **Consultar reportes** (`consultar()`) | Lista los reportes según el permiso del actor. | actor | Usuario | Usuario que consulta. | Página de Reporte | Reportes. | P-RPT-010 | Los moderadores consultan todos los reportes; los demás usuarios, solo los suyos. |  |  |
|  |  | filtro | FiltroReporte | Estado, motivo, fecha. |  |  |  |  |  |  |

## Eventos

| Actor | Acción | Descripción | Objeto de dominio | Información externa · Información | Información externa · Descripción | Políticas · Número | Sistema externo | Evento | Referencia en el enunciado |
|---|---|---|---|---|---|---|---|---|---|
| Usuario activo | Reportar contenido | Registra el reporte de un usuario sobre un Kudo o una réplica. | Reporte |  |  | P-RPT-001 |  | Kudo reportado / réplica reportada | EVT-026, EVT-030 |
|  |  |  |  |  |  | P-RPT-002 |  |  |  |
|  |  |  |  |  |  | P-RPT-003 |  |  |  |
|  |  |  |  |  |  | P-RPT-004 |  |  |  |
|  |  |  |  |  |  | P-RPT-005 |  |  |  |
|  |  |  |  |  |  | P-RPT-006 |  |  |  |
| Sistema (desde ServicioModeracion) | Pasar a revisión | El reporte entra en la moderación en curso. | Reporte |  |  | P-RPT-007 |  | Revisión iniciada | EVT-035 |
| Sistema (desde ServicioModeracion) | Aceptar reporte | El reporte queda aceptado porque el contenido fue vetado. | Reporte |  |  | P-RPT-008 |  | Revisión cerrada | EVT-036 |
| Sistema (desde ServicioModeracion) | Rechazar reporte | El reporte queda rechazado porque el contenido fue liberado. | Reporte |  |  | P-RPT-009 |  | Revisión cerrada | EVT-036 |
| Moderador / Usuario activo | Consultar reportes | Lista los reportes según el permiso del actor. | Reporte |  |  | P-RPT-010 |  | Reportes consultados | CU-012 |

## Políticas

| Número política | Descripción política | Objeto responsable | Regla referenciada |
|---|---|---|---|
| P-RPT-001 | Asegurar que el reportante tenga cuenta ACTIVA. | Usuario | RN-186, RN-187 |
| P-RPT-002 | Asegurar que el reporte apunte exactamente a un Kudo o a una réplica. | Reporte | RN-158, RN-172 |
| P-RPT-003 | Asegurar que el contenido no esté vetado ni sea del propio reportante. | Reporte | D-24, P-KUD-018, P-RPL-014 |
| P-RPT-004 | Asegurar que el motivo sea del catálogo; si es OTRO, la descripción es obligatoria. | Reporte | RN-190, CU-010, D-24 |
| P-RPT-005 | Asegurar que el usuario no tenga otro reporte abierto sobre el mismo contenido. | Reporte | UQ-REP-1, UQ-REP-2, D-24 |
| P-RPT-006 | Todo reporte inicia PENDIENTE (o EN_REVISION si el contenido ya está en revisión) y marca el contenido como reportado. | Reporte | RN-210, P-KUD-013, P-RPL-009 |
| P-RPT-007 | Solo un reporte PENDIENTE pasa a EN_REVISION. | Reporte | RN-211 |
| P-RPT-008 | Un reporte abierto pasa a ACEPTADO cuando la moderación veta el contenido. | Reporte | RN-212 |
| P-RPT-009 | Un reporte abierto pasa a RECHAZADO cuando la moderación libera el contenido; se notifica al reportante. | Reporte | RN-212, D-27 |
| P-RPT-010 | Los moderadores consultan todos los reportes; los demás usuarios, solo los suyos. | PoliticaModeracion | CU-012, D-08 |
