# Kudo

[Inicio](../../../README.md) · [Modelo de dominio](../README.md) · [Diagrama de clases](../../02-diagrama-clases/README.md) · [Modelo entidad-relación](../../03-modelo-entidad-relacion/README.md)

> Reconocimiento formal que una persona otorga a otra persona de la comunidad universitaria. Es la raíz de su conversación.

**Línea de vida:** 5

[Muestreo](#muestreo) · [Caracterización](#caracterización) · [Eventos](#eventos) · [Políticas](#políticas)

## Muestreo

### Atributos

| Atributo | Descripción | Tipo de dato | Obligatorio |
|---|---|---|---|
| id | Identificador único del Kudo | UUID | Sí |
| autor | Usuario que otorga el reconocimiento (RN-147) | [Usuario](Usuario.md) | Sí |
| destinatario | Usuario que recibe el reconocimiento (RN-148) | [Usuario](Usuario.md) | Sí |
| categoria | Categoría del reconocimiento (RN-149) | [Categoria](Categoria.md) | Sí |
| contenido | Texto del reconocimiento | [ContenidoTexto](../README.md#objetos-de-valor) | Sí |
| fechaCreacion | Fecha y hora de publicación (RN-150) | FechaHora | Sí |
| estado | Estado del ciclo de vida del contenido | [EstadoContenido](../README.md#enumeraciones) | Sí |
| cantidadVisualizaciones | Número de veces que ha sido consultado (RN-229, D-25) | Entero | Sí |
| promedioCalificacion | Promedio de estrellas de sus calificaciones activas | Decimal | Sí |
| cantidadCalificaciones | Número de calificaciones activas | Entero | Sí |

### Datos de muestra

_4 registros de ejemplo. Los identificadores se abrevian (u-01, k-01…) para facilitar la lectura; en el sistema son UUID._

| id | autor | destinatario | categoria | contenido | fechaCreacion | estado | cantidadVisualizaciones | promedioCalificacion | cantidadCalificaciones |
|---|---|---|---|---|---|---|---|---|---|
| k-01 | u-06 Andrea Giraldo | u-07 Santiago Henao | Excelencia académica | Santiago presentó el mejor proyecto final del curso de Diseño Orientado a Objetos... | 2026-11-05 10:00 | VISIBLE | 34 | 4.67 | 3 |
| k-02 | u-07 Santiago Henao | u-06 Andrea Giraldo | Valores institucionales | Gracias profe Andrea por acompañarnos los sábados en las tutorías... | 2026-11-05 18:30 | VISIBLE | 21 | 5.00 | 2 |
| k-03 | u-05 Juan Pablo Zuluaga | u-04 Luisa Ospina | Liderazgo | Luisa organizó la feria de proyectos de la Facultad... | 2026-11-06 09:15 | REPORTADO | 12 | 4.00 | 1 |
| k-04 | u-08 Valentina Cardona | u-07 Santiago Henao | Trabajo en equipo | (contenido con lenguaje ofensivo) | 2026-11-06 20:00 | VETADO | 9 | 0 | 0 |

### Relaciones

| Relación | Con | Cardinalidad | Tipo |
|---|---|---|---|
| Es otorgado por-> | [Usuario](Usuario.md) | Kudo 0..* — 1 Usuario | Asociación |
| Reconoce a-> | [Usuario](Usuario.md) | Kudo 0..* — 1 Usuario | Asociación |
| Pertenece a-> | [Categoria](Categoria.md) | Kudo 0..* — 1 Categoria | Asociación |
| Contiene-> | [Replica](Replica.md) | Kudo 1 — 0..* Replica | Composición |
| Recibe-> | [Calificacion](Calificacion.md) | Kudo 0..1 — 0..* Calificacion | Asociación |
| Recibe-> | [Reporte](Reporte.md) | Kudo 0..1 — 0..* Reporte | Asociación |
| Es revisado en-> | [Moderacion](Moderacion.md) | Kudo 0..1 — 0..* Moderacion | Asociación |

## Caracterización

### Caracterización del objeto

| Aspecto | Descripción |
|---|---|
| **¿Qué representa?** | Un reconocimiento formal que una persona otorga a otra de la comunidad universitaria; es la raíz de su conversación. |
| **Tipo de objeto** | Entidad raíz de agregado |
| **Agregado** | Kudo (raíz). Las réplicas son un agregado aparte que referencia al Kudo: la conversación crece sin límite (RN-163) y cargarla completa para cada operación no escalaría. |
| **¿Qué conoce? (información que administra)** | Su autor, su destinatario, su categoría, su contenido completo, su fecha, su estado, sus visualizaciones y el resumen de sus calificaciones. |
| **¿Qué hace? (responsabilidades)** | • Crear Kudo<br>• Consultar Kudo<br>• Consultar muro<br>• Consultar historial de Kudos<br>• Marcar reportado<br>• Iniciar revisión<br>• Liberar Kudo<br>• Vetar Kudo<br>• Decidir si admite interacción<br>• Actualizar resumen de calificaciones |
| **¿Qué decisiones toma?** | • Si admite interacción (réplicas, calificaciones y reportes): solo si no está VETADO.<br>• Qué transición de estado es válida (State: publicar, reportar, revisar, liberar, vetar).<br>• Su promedio de calificaciones. |
| **¿Qué decisiones delega?** | • La validación del contenido a ContenidoTexto.<br>• Las reglas de cada estado a los objetos de estado (patrón State).<br>• La verificación de que la categoría está activa a Categoria. |
| **¿Con quién colabora?** | Usuario, Categoria, ContenidoTexto, EstadoContenido, Replica, Calificacion, Reporte, Moderacion |
| **¿Qué eventos genera?** | • Kudo creado (publicado)<br>• Kudo consultado<br>• Muro consultado<br>• Historial de Kudos consultado<br>• Kudo reportado<br>• Revisión iniciada<br>• Contenido liberado<br>• Kudo vetado<br>• Indicador recalculado |
| **¿Qué invariantes protege?** | • INV-020 a INV-024: un autor, un destinatario, fecha, categoría válida y contenido completo.<br>• RN-201 a RN-205: ciclo de vida del contenido.<br>• D-19: el destinatario es distinto del autor. |

### Caracterización de atributos

| Nombre atributo | id | autor | destinatario | categoria | contenido | fechaCreacion | estado | cantidadVisualizaciones | promedioCalificacion | cantidadCalificaciones |
|---|---|---|---|---|---|---|---|---|---|---|
| Descripción atributo | Identificador único del Kudo | Usuario que otorga el reconocimiento (RN-147) | Usuario que recibe el reconocimiento (RN-148) | Categoría del reconocimiento (RN-149) | Texto del reconocimiento | Fecha y hora de publicación (RN-150) | Estado del ciclo de vida del contenido | Número de veces que ha sido consultado (RN-229, D-25) | Promedio de estrellas de sus calificaciones activas | Número de calificaciones activas |
| Identificador del objeto de dominio | Sí | No | No | No | No | No | No | No | No | No |
| Tipo de dato | UUID | Usuario | Usuario | Categoria | ContenidoTexto | FechaHora | EstadoContenido | Entero | Decimal | Entero |
| Obligatorio | Sí | Sí | Sí | Sí | Sí | Sí | Sí | Sí | Sí | Sí |
| Longitud mínima (si aplica) |  |  |  |  | 1 |  |  |  |  |  |
| Longitud máxima (si aplica) |  |  |  |  | Sin límite |  |  |  |  |  |
| Precisión (si aplica) |  |  |  |  |  |  |  |  | 2 |  |
| Rango inicial (si aplica) |  |  |  |  |  |  |  | 0 | 0 | 0 |
| Rango final (si aplica) |  |  |  |  |  |  |  |  | 5 |  |
| Formato (si aplica) |  |  |  |  | Texto libre |  |  |  |  |  |
| Reglas especiales (si aplica) | Autogenerado | Cuenta ACTIVA | Cuenta ACTIVA; distinto del autor (D-19) | Debe estar activa al crear el Kudo (RN-144); se conserva aunque luego se inactive (RN-145) | Sin límite de longitud (RN-152); no puede estar vacío ni solo espacios; nunca se trunca (RN-153) | Asignada por el sistema | Inicia CREADO y al publicarse pasa a VISIBLE (RN-201, D-20) | ≥ 0 | 0 a 5, dos decimales; 0 si no tiene calificaciones | ≥ 0 |
| Dato calculado | No | No | No | No | No | No | No | Sí | Sí | Sí |
| Quitar espacios en blanco al inicio y al fin | No | No | No | No | Sí | No | No | No | No | No |
| Dato sensible | No | No | No | No | No | No | No | No | No | No |
| Valor por defecto (si aplica) |  |  |  |  |  | Fecha actual | CREADO | 0 | 0 | 0 |
| Auto generado | Sí | No | No | No | No | Sí | No | Sí | Sí | Sí |
| Crear Kudo | No requerido | Requerido | Requerido | Requerido | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido |
| Consultar Kudo | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar |
| Consultar muro | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar |
| Consultar historial de Kudos | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar | Mostrar |
| Marcar reportado | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido |
| Iniciar revisión | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido |
| Liberar Kudo | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido |
| Vetar Kudo | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido |
| Decidir si admite interacción | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido |
| Actualizar resumen de calificaciones | Requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido | No requerido |

### Combinaciones únicas

No tiene combinaciones únicas además de su identificador.

### Responsabilidades

| Responsabilidad | Descripción | Entradas · Parámetro | Entradas · Tipo de dato | Entradas · Descripción | Salida · Tipo de dato | Salida · Descripción | Políticas · Política | Políticas · Descripción | Excepción · Detalle | Excepción · ¿Qué hago? |
|---|---|---|---|---|---|---|---|---|---|---|
| **Crear Kudo** (`crear()`) | Crea y publica un reconocimiento de un usuario activo a otro. | autor | Usuario | Quien reconoce. | Kudo | Kudo VISIBLE. | P-KUD-001 | Asegurar que el autor tenga cuenta ACTIVA. | El autor no está activo. | Rechazar la creación. |
|  |  | destinatario | Usuario | Quien es reconocido. |  |  | P-KUD-002 | Asegurar que el destinatario tenga cuenta ACTIVA y sea distinto del autor. | El destinatario no está activo o es el mismo autor. | Rechazar la creación e informar el motivo. |
|  |  | categoria | Categoria | Categoría activa. |  |  | P-KUD-003 | Asegurar que la categoría exista y esté activa al crear el Kudo. | La categoría no existe o está inactiva. | Rechazar la creación y pedir otra categoría. |
|  |  | contenido | ContenidoTexto | Texto del reconocimiento. |  |  | P-KUD-004 | Asegurar que el contenido no esté vacío; se guarda completo, sin límite y sin truncarlo. | El contenido está vacío. | Rechazar la creación. |
|  |  |  |  |  |  |  | P-KUD-005 | La fecha de creación la asigna el sistema. |  |  |
|  |  |  |  |  |  |  | P-KUD-006 | Todo Kudo inicia en CREADO y al publicarse pasa a VISIBLE. |  |  |
|  |  |  |  |  |  |  | P-KUD-007 | Notificar al destinatario que recibió un Kudo. |  |  |
| **Consultar Kudo** (`registrarVisualizacion()`) | Muestra el Kudo completo y registra la visualización. | id | UUID | Kudo consultado. | Kudo | Kudo con su conversación y calificaciones. | P-KUD-008 | Mostrar el contenido completo del Kudo. |  |  |
|  |  | actor | Usuario | Usuario que consulta. |  |  | P-KUD-009 | Si el Kudo está vetado, ocultar su contenido a los usuarios; los moderadores ven el texto y el motivo del veto. |  |  |
|  |  |  |  |  |  |  | P-KUD-010 | Cada consulta de un Kudo incrementa su contador de visualizaciones. |  |  |
| **Consultar muro** (`consultarMuro()`) | Lista los Kudos no vetados, del más reciente al más antiguo. | pagina | int | Página. | Página de Kudo | Kudos del muro. | P-KUD-011 | El muro muestra los Kudos no vetados, del más reciente al más antiguo, paginados. | No hay Kudos para mostrar. | Informar que aún no hay reconocimientos. |
|  |  | filtro | FiltroKudo | Categoría o persona (opcional). |  |  |  |  |  |  |
| **Consultar historial de Kudos** (`consultarHistorial()`) | Lista los Kudos recibidos y emitidos por una persona. | persona | UUID | Persona consultada. | Lista de Kudo | Kudos recibidos y emitidos. | P-KUD-012 | El historial de una persona muestra sus Kudos recibidos y emitidos. |  |  |
| **Marcar reportado** (`marcarReportado()`) | Registra que el Kudo recibió un reporte. | Ninguno | - | No requiere datos de entrada. | Kudo | Kudo REPORTADO (o sin cambio). | P-KUD-013 | Un Kudo VISIBLE pasa a REPORTADO con su primer reporte; si ya está REPORTADO o EN_REVISION, conserva su estado. | El Kudo está vetado. | Rechazar el reporte. |
| **Iniciar revisión** (`iniciarRevision()`) | El Kudo entra en revisión de un moderador. | Ninguno | - | No requiere datos de entrada. | Kudo | Kudo EN_REVISION. | P-KUD-014 | Solo un Kudo REPORTADO puede pasar a EN_REVISION. | El Kudo no está reportado. | Rechazar el inicio de la revisión. |
| **Liberar Kudo** (`liberar()`) | El Kudo vuelve a estar visible tras la revisión. | Ninguno | - | No requiere datos de entrada. | Kudo | Kudo VISIBLE. | P-KUD-015 | Solo un Kudo EN_REVISION puede liberarse; vuelve a VISIBLE. | El Kudo no está en revisión. | Rechazar la liberación. |
| **Vetar Kudo** (`vetar()`) | Inhabilita el Kudo de forma definitiva. | Ninguno | - | No requiere datos de entrada. | Kudo | Kudo VETADO. | P-KUD-016 | Un Kudo no vetado puede vetarse; VETADO es final y cierra su conversación. | El Kudo ya está vetado. | Rechazar el veto. |
| **Decidir si admite interacción** (`admiteInteraccion()`) | Indica si el Kudo acepta réplicas, calificaciones y reportes. | Ninguno | - | No requiere datos de entrada. | boolean | Verdadero si no está vetado. | P-KUD-018 | Un Kudo admite réplicas, calificaciones y reportes solo si no está VETADO. | El Kudo está vetado. | Rechazar la interacción e informar que el contenido fue vetado. |
| **Actualizar resumen de calificaciones** (`actualizarResumenCalificaciones()`) | Recalcula el promedio y la cantidad de calificaciones activas. | calificaciones | Lista de Estrellas | Calificaciones activas. | Kudo | Kudo con resumen actualizado. | P-KUD-017 | El promedio de calificaciones es el promedio de las calificaciones activas, con dos decimales. |  |  |

## Eventos

| Actor | Acción | Descripción | Objeto de dominio | Información externa · Información | Información externa · Descripción | Políticas · Número | Sistema externo | Evento | Referencia en el enunciado |
|---|---|---|---|---|---|---|---|---|---|
| Usuario activo | Crear Kudo | Crea y publica un reconocimiento de un usuario activo a otro. | Kudo | Categorías activas | Opciones válidas. | P-KUD-001 |  | Kudo creado (publicado) | EVT-024 / RN-228 |
|  |  |  |  | Usuarios activos | Posibles destinatarios. | P-KUD-002 |  |  |  |
|  |  |  |  |  |  | P-KUD-003 |  |  |  |
|  |  |  |  |  |  | P-KUD-004 |  |  |  |
|  |  |  |  |  |  | P-KUD-005 |  |  |  |
|  |  |  |  |  |  | P-KUD-006 |  |  |  |
|  |  |  |  |  |  | P-KUD-007 |  |  |  |
| Usuario activo | Consultar Kudo | Muestra el Kudo completo y registra la visualización. | Kudo |  |  | P-KUD-008 |  | Kudo consultado | EVT-025 / RN-229 |
|  |  |  |  |  |  | P-KUD-009 |  |  |  |
|  |  |  |  |  |  | P-KUD-010 |  |  |  |
| Usuario activo | Consultar muro | Lista los Kudos no vetados, del más reciente al más antiguo. | Kudo |  |  | P-KUD-011 |  | Muro consultado | RF-090 |
| Usuario activo | Consultar historial de Kudos | Lista los Kudos recibidos y emitidos por una persona. | Kudo |  |  | P-KUD-012 |  | Historial de Kudos consultado | RF-048 a RF-050 |
| Sistema (al crear un reporte) | Marcar reportado | Registra que el Kudo recibió un reporte. | Kudo |  |  | P-KUD-013 |  | Kudo reportado | EVT-026 / RN-230 |
| Sistema (desde ServicioModeracion) | Iniciar revisión | El Kudo entra en revisión de un moderador. | Kudo |  |  | P-KUD-014 |  | Revisión iniciada | EVT-035 / RN-239 |
| Sistema (desde ServicioModeracion) | Liberar Kudo | El Kudo vuelve a estar visible tras la revisión. | Kudo |  |  | P-KUD-015 |  | Contenido liberado | EVT-037 / RN-242 |
| Sistema (desde ServicioModeracion) | Vetar Kudo | Inhabilita el Kudo de forma definitiva. | Kudo |  |  | P-KUD-016 |  | Kudo vetado | EVT-027 / RN-231 |
| Sistema | Decidir si admite interacción | Indica si el Kudo acepta réplicas, calificaciones y reportes. | Kudo |  |  | P-KUD-018 |  | (decisión, sin evento) |  |
| Sistema (suscriptor de calificaciones) | Actualizar resumen de calificaciones | Recalcula el promedio y la cantidad de calificaciones activas. | Kudo |  |  | P-KUD-017 |  | Indicador recalculado | EVT-034 / RN-238 |

## Políticas

| Número política | Descripción política | Objeto responsable | Regla referenciada |
|---|---|---|---|
| P-KUD-001 | Asegurar que el autor tenga cuenta ACTIVA. | Usuario | RN-147, RN-058, RN-066 |
| P-KUD-002 | Asegurar que el destinatario tenga cuenta ACTIVA y sea distinto del autor. | Kudo | RN-148, D-19 |
| P-KUD-003 | Asegurar que la categoría exista y esté activa al crear el Kudo. | Categoria | RN-144, RN-149, INV-019, P-CAT-007 |
| P-KUD-004 | Asegurar que el contenido no esté vacío; se guarda completo, sin límite y sin truncarlo. | ContenidoTexto | RN-152, RN-153, INV-024 |
| P-KUD-005 | La fecha de creación la asigna el sistema. | Kudo | RN-150, INV-022 |
| P-KUD-006 | Todo Kudo inicia en CREADO y al publicarse pasa a VISIBLE. | EstadoContenido | RN-201, D-20 |
| P-KUD-007 | Notificar al destinatario que recibió un Kudo. | Notificacion | D-27 |
| P-KUD-008 | Mostrar el contenido completo del Kudo. | Kudo | RN-154, RN-155 |
| P-KUD-009 | Si el Kudo está vetado, ocultar su contenido a los usuarios; los moderadores ven el texto y el motivo del veto. | Kudo | RN-205, D-21 |
| P-KUD-010 | Cada consulta de un Kudo incrementa su contador de visualizaciones. | Kudo | RN-229, D-25 |
| P-KUD-011 | El muro muestra los Kudos no vetados, del más reciente al más antiguo, paginados. | Kudo | RN-155, RNF-010 |
| P-KUD-012 | El historial de una persona muestra sus Kudos recibidos y emitidos. | Kudo | RF-048 a RF-050 |
| P-KUD-013 | Un Kudo VISIBLE pasa a REPORTADO con su primer reporte; si ya está REPORTADO o EN_REVISION, conserva su estado. | EstadoContenido | RN-202, D-20 |
| P-KUD-014 | Solo un Kudo REPORTADO puede pasar a EN_REVISION. | EstadoContenido | RN-203 |
| P-KUD-015 | Solo un Kudo EN_REVISION puede liberarse; vuelve a VISIBLE. | EstadoContenido | RN-242, D-20 |
| P-KUD-016 | Un Kudo no vetado puede vetarse; VETADO es final y cierra su conversación. | EstadoContenido | RN-204, RN-205, D-21 |
| P-KUD-017 | El promedio de calificaciones es el promedio de las calificaciones activas, con dos decimales. | Kudo | RN-185, RN-238 |
| P-KUD-018 | Un Kudo admite réplicas, calificaciones y reportes solo si no está VETADO. | EstadoContenido | RN-205, D-21 |
