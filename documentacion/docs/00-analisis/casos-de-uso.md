# Casos de uso

[Inicio](../../README.md) · [Requisitos](requisitos.md) · [Matriz de trazabilidad](matriz-trazabilidad.md) · [Decisiones](decisiones-de-diseno.md)

- **CU-001 a CU-015** vienen del enunciado (documento 05) y se completan con precondiciones, flujos alternos y eventos.
- **CU-016 en adelante** 🆕 surgen de los requisitos complementados y de las reglas organizacionales.

El diagrama UML de casos de uso está en [`04-diagrama-casos-uso`](../04-diagrama-casos-uso/README.md).

**Convenciones**
- *Usuario activo* = cualquier actor con cuenta `ACTIVA`, sea cual sea su rol.
- *Moderador* = Rector o Coordinador Académico (D-02).
- Toda operación que cambia el estado del sistema produce un **evento** que queda en auditoría (D-26).
- Cuando una regla falla, la operación **no cambia nada** y muestra un mensaje que explica el motivo, igual que en Sigma.

---

## 1. Catálogo

| Código | Caso de uso | Actor principal | Módulo | RF |
|---|---|---|---|---|
| CU-001 | Crear Kudo | Usuario activo | Kudos | RF-046, RF-051, RF-052 |
| CU-002 | Consultar Kudo | Usuario activo | Kudos | RF-047 |
| CU-003 | Consultar historial de Kudos | Usuario activo | Kudos | RF-048 a RF-050 |
| CU-004 | Crear réplica sobre Kudo | Usuario activo | Réplicas | RF-053 |
| CU-005 | Responder réplica | Usuario activo | Réplicas | RF-054 |
| CU-006 | Consultar conversación | Usuario activo | Réplicas | RF-055, RF-056 |
| CU-007 | Calificar Kudo | Usuario activo | Calificaciones | RF-057, RF-059, RF-060, RF-091, RF-092 |
| CU-008 | Calificar réplica | Usuario activo | Calificaciones | RF-058 a RF-060, RF-091, RF-092 |
| CU-009 | Consultar valoraciones | Usuario activo | Calificaciones | RF-047 |
| CU-010 | Reportar Kudo | Usuario activo | Reportes | RF-061, RF-063 |
| CU-011 | Reportar réplica | Usuario activo | Reportes | RF-062, RF-063 |
| CU-012 | Consultar reportes | Moderador | Reportes | RF-064 |
| CU-013 | Vetar Kudo | Moderador | Moderación | RF-065, RF-067 |
| CU-014 | Vetar réplica | Moderador | Moderación | RF-066, RF-067 |
| CU-015 | Consultar histórico de vetos | Moderador | Moderación | RF-068 |
| CU-016 🆕 | Registrarse (solicitar cuenta) | Persona no registrada | Identidad | RF-001 a RF-005, RF-025, RF-030 |
| CU-017 🆕 | Verificar correo o celular | Usuario registrado | Identidad | RF-006, RF-007, RF-080 |
| CU-018 🆕 | Iniciar sesión | Usuario registrado | Identidad | RF-078 |
| CU-019 🆕 | Cerrar sesión | Usuario activo | Identidad | RF-079 |
| CU-020 🆕 | Consultar y actualizar información personal | Usuario activo | Identidad | RF-008 a RF-011, RF-081 |
| CU-021 🆕 | Consultar historial de actividad propia | Usuario activo | Identidad | RF-082 |
| CU-022 🆕 | Aprobar solicitud de registro | Autoridad aprobadora | Aprobaciones | RF-035 a RF-038, RF-087, RF-088 |
| CU-023 🆕 | Rechazar solicitud de registro | Autoridad aprobadora | Aprobaciones | RF-086 a RF-088 |
| CU-024 🆕 | Registrar Rector | Administrador | Aprobaciones | RF-039 |
| CU-025 🆕 | Registrar Administrador | Administrador | Aprobaciones | RF-040 |
| CU-026 🆕 | Suspender o reactivar cuenta | Autoridad que gestiona | Cuentas | RF-089 |
| CU-027 🆕 | Desactivar cuenta | Autoridad que gestiona | Cuentas | RF-089 |
| CU-028 🆕 | Crear Facultad | Coordinador Académico | Organización | RF-015 |
| CU-029 🆕 | Modificar o inactivar Facultad | Coordinador Académico | Organización | RF-017, RF-083 |
| CU-030 🆕 | Crear Programa Académico | Coordinador Académico | Organización | RF-020 |
| CU-031 🆕 | Modificar o inactivar Programa | Coordinador Académico | Organización | RF-022, RF-084 |
| CU-032 🆕 | Vincular docente o estudiante a Programa | Coordinador de Programa | Organización | RF-026, RF-031 |
| CU-033 🆕 | Desvincular docente o estudiante de Programa | Coordinador de Programa | Organización | RF-085, RF-029, RF-034 |
| CU-034 🆕 | Consultar estructura organizacional | Usuario activo | Organización | RF-016, RF-018, RF-019, RF-021, RF-023, RF-024, RF-027, RF-028, RF-032, RF-033, RF-072 a RF-077 |
| CU-035 🆕 | Gestionar categorías | Admin, Rector, Coord. Académico | Categorías | RF-041 a RF-045 |
| CU-036 🆕 | Iniciar revisión de contenido | Moderador | Moderación | RF-093 |
| CU-037 🆕 | Liberar contenido revisado | Moderador | Moderación | RF-094 |
| CU-038 🆕 | Consultar auditoría | Autoridades (según ámbito) | Auditoría | RF-069 a RF-071 |
| CU-039 🆕 | Consultar notificaciones | Usuario activo | Notificaciones | RF-095 |
| CU-040 🆕 | Consultar indicadores | Usuario activo | Indicadores | RF-096 |
| CU-041 🆕 | Gestionar parámetros globales | Administrador | Configuración | RF-097 |
| CU-042 🆕 | Consultar muro de Kudos | Usuario activo | Kudos | RF-090 |

---

## 2. Especificaciones

### CU-001 Crear Kudo

| Campo | Contenido |
|---|---|
| **Actor** | Usuario activo |
| **Objetivo** | Otorgar un reconocimiento a otra persona de la comunidad |
| **Precondiciones** | El actor tiene sesión iniciada y cuenta `ACTIVA` |
| **Postcondición** | Existe un Kudo `VISIBLE` en el muro. El destinatario recibe una notificación |
| **Evento** | `KudoPublicado` (EVT-024, RN-228) |

**Flujo principal**
1. El actor inicia la creación de un Kudo.
2. El sistema muestra las categorías **activas** y un buscador de personas activas.
3. El actor selecciona el destinatario.
4. El actor selecciona la categoría.
5. El actor escribe el contenido, de longitud libre.
6. El actor confirma.
7. El sistema valida las reglas, registra el Kudo en estado `CREADO` y lo publica de inmediato, con lo que pasa a `VISIBLE`.
8. El sistema notifica al destinatario y muestra el Kudo publicado.

**Flujos alternos y excepciones**
- 7a. El destinatario es el propio actor → se rechaza: *"No puedes otorgarte un Kudo a ti mismo"* (D-19).
- 7b. El destinatario no tiene cuenta activa → se rechaza (D-19).
- 7c. La categoría fue desactivada mientras el actor escribía → se rechaza y se pide elegir otra (RN-144).
- 7d. El contenido está vacío o solo tiene espacios → se rechaza (INV-024).

**Reglas**: RN-146 a RN-155, RN-201, INV-017, INV-020 a INV-024, D-19, D-20.

---

### CU-002 Consultar Kudo

| Campo | Contenido |
|---|---|
| **Actor** | Usuario activo |
| **Precondiciones** | El Kudo existe |
| **Evento** | `KudoConsultado` (EVT-025): incrementa `cantidadVisualizaciones` (D-25) |

**Flujo principal**
1. El actor selecciona un Kudo del muro, de su historial o de una notificación.
2. El sistema muestra: autor, destinatario, fecha, categoría, **contenido completo**, estado, promedio y número de calificaciones, la calificación del propio actor si existe, y la conversación (CU-006).

**Alternos**
- 2a. El Kudo está `VETADO` → los usuarios ven *"Contenido vetado por moderación"* sin el texto. Los moderadores ven el texto original y el motivo del veto (D-21).

---

### CU-003 Consultar historial de Kudos

| **Actor** | Usuario activo |
|---|---|
| **Flujo** | 1. El actor selecciona una persona (o a sí mismo). 2. El sistema muestra, paginados, los Kudos **recibidos** y **emitidos** por esa persona, con filtro por categoría. Los vetados se muestran como vetados. |
| **RF** | RF-048 a RF-050 |

---

### CU-004 Crear réplica sobre Kudo

| Campo | Contenido |
|---|---|
| **Actor** | Usuario activo |
| **Precondiciones** | El Kudo existe y **no está `VETADO`** |
| **Postcondición** | La conversación se amplía. El autor y el destinatario del Kudo reciben una notificación (nunca quien responde) |
| **Evento** | `ReplicaPublicada` (EVT-028, RN-232) |

**Flujo principal**
1. El actor selecciona el Kudo.
2. El actor elige "Responder".
3. El actor escribe el contenido.
4. El actor confirma.
5. El sistema crea la réplica con el Kudo como padre, la publica (`VISIBLE`) y notifica al autor y al destinatario del Kudo.

**Excepciones**: 5a. El Kudo fue vetado → se rechaza: *"La conversación está cerrada"* (D-21). 5b. Contenido vacío → se rechaza.

**Reglas**: RN-156, RN-161, RN-163 a RN-170, RN-206, INV-025 a INV-027.

---

### CU-005 Responder réplica

| Campo | Contenido |
|---|---|
| **Actor** | Usuario activo |
| **Precondiciones** | La réplica existe, **no está `VETADA`** y su Kudo no está `VETADO` |
| **Postcondición** | Se crea una réplica hija y el autor de la réplica respondida recibe una notificación |
| **Eventos** | `ReplicaPublicada` (EVT-028) y `ReplicaRespondida` (EVT-029, RN-233) |

**Flujo principal**: 1. El actor selecciona la réplica. 2. Escribe la respuesta. 3. Confirma. 4. El sistema crea la réplica con `replicaPadre` = la réplica seleccionada y `kudo` = el mismo Kudo raíz, y la publica.

**Reglas**: RN-162, RN-164, RN-167, INV-026 a INV-029, D-22. El padre se fija al crear y no cambia nunca, así que no puede haber ciclos.

---

### CU-006 Consultar conversación

| **Actor** | Usuario activo |
|---|---|
| **Flujo** | 1. El actor abre un Kudo. 2. El sistema arma el **árbol completo** de réplicas en orden cronológico dentro de cada nivel, con sangría por nivel y opción de contraer o expandir (RNF-037). Cada nodo muestra autor, fecha, contenido, estado y promedio de calificación. |
| **Alternos** | 2a. Las réplicas vetadas se muestran como *"Contenido vetado"* y sus respuestas siguen visibles (D-21). |
| **Patrón** | Composite (D-22) |

---

### CU-007 / CU-008 Calificar Kudo o réplica

| Campo | Contenido |
|---|---|
| **Actor** | Usuario activo |
| **Precondiciones** | El contenido existe, no está `VETADO` y su autor no es el actor |
| **Postcondición** | El actor tiene **una** calificación activa sobre el contenido y el promedio del contenido queda recalculado |
| **Eventos** | `CalificacionRegistrada` (EVT-032) o `CalificacionActualizada` (EVT-033), y luego `IndicadorRecalculado` (EVT-034) |

**Flujo principal**
1. El actor elige de 1 a 5 estrellas.
2. El sistema verifica que no tenga ya una calificación activa sobre ese contenido.
3. El sistema registra la calificación.
4. El sistema recalcula el promedio y la cantidad.

**Alternos**
- 2a. El actor ya tiene una calificación activa → el sistema **modifica** las estrellas, guarda `fechaModificacion` y genera `CalificacionActualizada` con el valor anterior y el nuevo (RN-237, D-23).
- 1a. El valor está fuera de 1 a 5 → se rechaza (INV-030).
- Pre. El actor es el autor del contenido → se rechaza (D-23).

**Reglas**: RN-175 a RN-185, INV-030 a INV-032.

---

### CU-009 Consultar valoraciones

| **Actor** | Usuario activo |
|---|---|
| **Flujo** | El sistema muestra para un Kudo o réplica: promedio, número de calificaciones, distribución por estrellas (cuántas de 1, de 2… de 5) y la calificación del propio actor. |

---

### CU-010 / CU-011 Reportar Kudo o réplica

| Campo | Contenido |
|---|---|
| **Actor** | Usuario activo |
| **Precondiciones** | El contenido existe, no está `VETADO` y no es propio. El actor no tiene otro reporte abierto sobre él |
| **Postcondición** | Existe un reporte `PENDIENTE`. Si el contenido estaba `VISIBLE`, pasa a `REPORTADO` |
| **Eventos** | `KudoReportado` (EVT-026, RN-230) o `ReplicaReportada` (EVT-030, RN-234) |

**Flujo principal**: 1. El actor elige "Reportar". 2. Selecciona el motivo: Lenguaje ofensivo, Acoso, Discriminación, Spam, Información falsa u Otro. 3. Agrega una descripción, opcional salvo para "Otro". 4. Confirma. 5. El sistema registra el reporte y actualiza el estado del contenido.

**Alternos**
- 5a. El contenido ya está `REPORTADO` o `EN_REVISION` → se registra el reporte y el estado no cambia. Si hay una revisión en curso, el reporte entra directo a `EN_REVISION`.
- 3a. Motivo "Otro" sin descripción → se rechaza.

**Reglas**: RN-158, RN-172, RN-186 a RN-190, RN-202, RN-207, RN-210, D-24.

---

### CU-012 Consultar reportes

| **Actor** | Moderador. Cualquier usuario activo puede consultar sus propios reportes |
|---|---|
| **Flujo** | El sistema muestra los reportes paginados y filtrables por estado, motivo, tipo de contenido y fecha. También los agrupa por contenido, con la cantidad de reportes de cada uno (D-08). |

---

### CU-013 / CU-014 Vetar Kudo o réplica

| Campo | Contenido |
|---|---|
| **Actor** | Moderador (Rector o Coordinador Académico) |
| **Precondiciones** | El contenido no está `VETADO` |
| **Postcondición** | El contenido queda `VETADO`. La moderación se cierra con decisión `VETAR` y sus reportes abiertos quedan `ACEPTADO`. El autor recibe una notificación. Si es un Kudo, su conversación queda cerrada |
| **Eventos** | `KudoVetado` (EVT-027, RN-231) o `ReplicaVetada` (EVT-031, RN-235); `ContenidoVetado` (RN-241); `RevisionCerrada` (EVT-036) |

**Flujo principal**
1. El moderador selecciona el contenido, normalmente desde la lista de reportes o desde una revisión en curso.
2. Elige "Vetar".
3. Escribe el motivo (10 a 500 caracteres).
4. Confirma.
5. El sistema cierra la moderación en curso con decisión `VETAR`, o crea y cierra una si es un veto directo.
6. El sistema pasa el contenido a `VETADO` y acepta todos sus reportes abiertos.
7. El sistema notifica al autor.

**Excepciones**
- 4a. El actor no es moderador → se rechaza y el intento queda registrado (RN-197 a RN-200, RN-243).
- 3a. Falta el motivo → se rechaza (RN-250).
- Pre. El contenido ya está vetado → se rechaza.

**Reglas**: RN-191 a RN-200, RN-204, RN-205, RN-209, RN-243 a RN-250, INV-033 a INV-035, D-21.

---

### CU-015 Consultar histórico de vetos

| **Actor** | Moderador |
|---|---|
| **Flujo** | El sistema lista las moderaciones con decisión `VETAR`: contenido (con su texto original), autor del contenido, moderador, fecha, motivo y reportes resueltos. Se puede filtrar por fecha, moderador y tipo de contenido. |

---

### CU-016 Registrarse (solicitar cuenta) 🆕

| Campo | Contenido |
|---|---|
| **Actor** | Persona no registrada |
| **Postcondición** | Existe un `Usuario` en `PENDIENTE_APROBACION`, con una `SolicitudRegistro` `PENDIENTE` y códigos de verificación enviados por correo y celular |
| **Eventos** | `UsuarioRegistrado` (EVT-001, RN-213) |

**Flujo principal**
1. La persona ingresa nombres, apellidos, correo institucional, celular y contraseña.
2. Selecciona el rol que solicita: Coordinador Académico, Coordinador de Facultad, Coordinador de Programa, Docente o Estudiante.
3. Si es Coordinador de Facultad, selecciona la Facultad (solo `ACTIVA`). Si es Coordinador de Programa, Docente o Estudiante, selecciona el Programa (solo activos).
4. Confirma.
5. El sistema valida los datos, crea la cuenta y la solicitud, y envía los códigos de verificación (D-10).
6. El sistema notifica a la autoridad aprobadora (D-27).
7. El sistema informa que debe verificar sus datos y esperar la aprobación.

**Excepciones**
- 5a. El correo no tiene el dominio institucional → se rechaza (RN-069).
- 5b. El correo o el celular ya están registrados → se rechaza (RN-073, RN-074).
- 5c. El celular tiene formato inválido → se rechaza.
- 5d. La contraseña es débil → se rechaza.

**Reglas**: RN-069 a RN-080, RN-090 a RN-093, D-12, D-17.

---

### CU-017 Verificar correo o celular 🆕

| Campo | Contenido |
|---|---|
| **Actor** | Usuario registrado (aún sin sesión, o con sesión después de cambiar un dato) |
| **Postcondición** | El dato queda verificado. Si además la cuenta está aprobada y el otro dato está verificado, la cuenta pasa a `ACTIVA` |
| **Eventos** | `CorreoVerificado` (EVT-002) o `CelularVerificado` (EVT-003); `CuentaActivada` (EVT-005, RN-215) si aplica |

**Flujo principal**: 1. El usuario ingresa su correo y el código recibido en el canal correspondiente. 2. El sistema compara el código con el hash del código vigente de ese canal. 3. Marca el código como usado y el dato como verificado. 4. Intenta activar la cuenta.

**Alternos**
- 2a. Código incorrecto → suma un intento fallido. Al 5.º intento el código se invalida (D-10).
- 2b. Código vencido o invalidado → se rechaza y se ofrece "Reenviar código" (RF-080), lo que invalida los anteriores.
- 2c. El destino del código ya no coincide con el dato actual (el usuario lo cambió) → se rechaza.

**Reglas**: RN-077 a RN-089, POL-003.

---

### CU-018 Iniciar sesión 🆕

| **Actor** | Usuario registrado |
|---|---|
| **Flujo** | 1. Ingresa correo y contraseña. 2. El sistema verifica las credenciales. 3. Verifica que la cuenta esté `ACTIVA`. 4. Crea la sesión y muestra el muro con el menú de su rol. |
| **Excepciones** | 2a. Credenciales inválidas → mensaje genérico, sin revelar si el correo existe. 3a. Cuenta no activa → se rechaza indicando qué falta: aprobación, verificación de correo o de celular, o si está suspendida o desactivada (RN-089). |

### CU-019 Cerrar sesión 🆕

| **Actor** | Usuario activo |
|---|---|
| **Flujo** | 1. El actor elige "Cerrar sesión". 2. El sistema invalida la sesión y la cookie. 3. Muestra la pantalla de ingreso. |
| **Postcondición** | Las peticiones siguientes a `/api/**` responden 401 hasta que vuelva a iniciar sesión (RNF-001). |

---

### CU-020 Consultar y actualizar información personal 🆕

| Campo | Contenido |
|---|---|
| **Actor** | Usuario activo |
| **Eventos** | `DatosPersonalesModificados` (RN-109); `CorreoModificado` o `CelularModificado` (RN-083) |

**Flujo principal**: 1. El actor consulta sus datos, su rol, sus vínculos y el estado de sus validaciones. 2. Modifica nombres, apellidos, correo o celular. 3. Confirma. 4. El sistema valida y guarda los cambios, y deja auditoría con valor anterior y nuevo.

**Alternos**
- 4a. Si cambió el correo o el celular: ese dato queda **no verificado**, se envía un nuevo código y la cuenta pasa a `PENDIENTE_VERIFICACION`. La sesión actual se cierra (RN-081, RN-082, RN-110, RN-111).
- 2a. Intenta cambiar su rol → no hay opción para hacerlo (RN-107).

### CU-021 Consultar historial de actividad propia 🆕

| **Actor** | Usuario activo |
|---|---|
| **Flujo** | 1. El actor abre "Mi actividad". 2. El sistema muestra, paginados y del más reciente al más antiguo, los registros de auditoría en los que el actor es quien actuó o el objeto afectado es su cuenta: Kudos emitidos, réplicas, calificaciones, reportes, cambios de datos, aprobación y cambios de estado de la cuenta. |
| **Reglas** | RN-106, P-AUD-006 |

---

### CU-022 Aprobar solicitud de registro 🆕

| Campo | Contenido |
|---|---|
| **Actor** | Autoridad aprobadora según el rol solicitado (Matriz de aprobaciones, doc. 10 §5) |
| **Precondiciones** | La solicitud está `PENDIENTE`, es del ámbito del actor y el actor no es el solicitante |
| **Postcondición** | La solicitud queda `APROBADA` y el usuario `aprobada = Sí`. Se crea la `AsignacionRol` vigente; si es docente o estudiante, también la `VinculacionPrograma`. Si ya tenía sus datos verificados, la cuenta pasa a `ACTIVA` |
| **Eventos** | `UsuarioAprobado` (EVT-004, RN-214). Además, según el rol: `CoordinadorAcademicoAsignado` (EVT-014), `CoordinadorFacultadAsignado` o `Reemplazado` (EVT-010/011), `CoordinadorProgramaAsignado` o `Reemplazado` (EVT-012/013), `EstudianteVinculado` o `DocenteVinculado` (EVT-016/018). `CuentaActivada` si aplica |

**Flujo principal**
1. La autoridad consulta las solicitudes pendientes de su ámbito.
2. Selecciona una y revisa los datos.
3. Elige "Aprobar".
4. El sistema verifica el ámbito y las reglas de unicidad.
5. El sistema registra la decisión, asigna el rol, intenta activar la cuenta y notifica al solicitante.

**Alternos**
- 4a. **Sustitución**: si se aprueba un Coordinador de Facultad o de Programa y la Facultad o el Programa ya tiene coordinador vigente, se cierra la asignación anterior, se desactiva la cuenta del saliente y se genera el evento de **reemplazo** (D-15).
- 4b. Si es Coordinador Académico y ya existe uno vigente → se rechaza. Primero debe desactivarse el actual (INV-002).
- 4c. La solicitud no es del ámbito del actor → se rechaza (POL-004).
- 4d. El actor es el solicitante → se rechaza (RN-108).

**Reglas**: RN-084, RN-090 a RN-093, RN-096, RN-108, RN-112 a RN-127, INV-001 a INV-010, D-03, D-14, D-15, D-17.

---

### CU-023 Rechazar solicitud de registro 🆕

| **Actor** | Autoridad aprobadora |
|---|---|
| **Flujo** | 1. Selecciona una solicitud pendiente de su ámbito. 2. Elige "Rechazar" y escribe el motivo. 3. El sistema la marca `RECHAZADA` y notifica al solicitante. La cuenta queda sin rol y la persona puede presentar una nueva solicitud. |
| **Evento** | `SolicitudRechazada` |

---

### CU-024 Registrar Rector 🆕 / CU-025 Registrar Administrador 🆕

| **Actor** | Administrador |
|---|---|
| **Flujo** | 1. Ingresa los datos personales del nuevo Rector o Administrador. 2. El sistema crea el usuario ya **aprobado**, con su `AsignacionRol` vigente, y envía los códigos de verificación. La cuenta queda `PENDIENTE_VERIFICACION` hasta que verifique correo y celular. |
| **Excepciones** | Si ya existe un Rector vigente → se rechaza (INV-001). Datos duplicados → se rechaza. |
| **Eventos** | `UsuarioRegistrado`, `UsuarioAprobado`, `RectorAsignado` (EVT-015) |
| **Reglas** | RN-094, RN-095, RN-129 |

---

### CU-026 Suspender o reactivar cuenta 🆕 / CU-027 Desactivar cuenta 🆕

| **Actor** | La autoridad que gestiona al usuario (Matriz de gestión, doc. 10 §6) |
|---|---|
| **Flujo** | 1. Selecciona un usuario de su ámbito. 2. Elige suspender, reactivar o desactivar, y escribe el motivo. 3. El sistema cambia el estado de la cuenta. Si es una desactivación, cierra la asignación de rol y los vínculos vigentes. 4. Notifica al usuario. |
| **Excepciones** | Fuera de su ámbito → se rechaza. No puede aplicarse sobre sí mismo. Una cuenta desactivada no se reactiva. |
| **Eventos** | `CuentaSuspendida` (EVT-006), `CuentaActivada` (EVT-005), `CuentaDesactivada` (EVT-007) |
| **Reglas** | RN-098, RN-099, RN-216, RN-217, D-07 |

---

### CU-028 Crear Facultad 🆕

| **Actor** | Coordinador Académico |
|---|---|
| **Flujo** | 1. Ingresa el nombre. 2. El sistema valida que sea único y crea la Facultad en `EN_CONFORMACION`. |
| **Evento** | `FacultadCreada` (EVT-008, RN-218) |
| **Reglas** | RN-039, RN-040, RN-045, D-04, D-05 |

### CU-029 Modificar o inactivar Facultad 🆕

| **Actor** | Coordinador Académico |
|---|---|
| **Flujo** | Modificar el nombre (único) o inactivar la Facultad. La inactivación solo procede si no tiene Programas activos. |
| **Eventos** | `FacultadModificada`, `FacultadInactivada` |

### CU-030 Crear Programa Académico 🆕

| **Actor** | Coordinador Académico |
|---|---|
| **Flujo** | 1. Ingresa el nombre y selecciona la Facultad (no inactiva). 2. El sistema valida que el nombre sea único y crea el Programa activo. 3. Si la Facultad estaba `EN_CONFORMACION`, pasa a `ACTIVA`. |
| **Eventos** | `ProgramaCreado` (EVT-009, RN-221); `FacultadActivada` si aplica |
| **Reglas** | RN-046 a RN-049, D-05 |

### CU-031 Modificar o inactivar Programa 🆕

| **Actor** | Coordinador Académico |
|---|---|
| **Flujo** | Modificar el nombre o inactivar el Programa. La inactivación solo procede si no tiene vínculos vigentes. |

---

### CU-032 Vincular docente o estudiante a Programa 🆕

| Campo | Contenido |
|---|---|
| **Actor** | Coordinador de Programa |
| **Precondiciones** | La persona tiene rol vigente `DOCENTE` o `ESTUDIANTE`. El Programa es el del coordinador y está activo |
| **Evento** | `EstudianteVinculado` (EVT-016) o `DocenteVinculado` (EVT-018) |

**Flujo**: 1. El coordinador busca a la persona. 2. Elige "Vincular a mi programa". 3. El sistema crea la `VinculacionPrograma` vigente. Si la cuenta estaba suspendida por falta de vínculos, la reactiva.

**Excepciones**: ya tiene un vínculo vigente con el Programa → se rechaza. Rol distinto de docente o estudiante → se rechaza.

**Reglas**: RN-053, RN-061, RN-112, RN-113, INV-011, INV-014, D-16.

### CU-033 Desvincular docente o estudiante de Programa 🆕

| **Actor** | Coordinador de Programa |
|---|---|
| **Flujo** | 1. Selecciona a una persona vinculada a **su** Programa. 2. Escribe el motivo. 3. El sistema cierra el vínculo (`fechaFin` y motivo) **sin borrarlo**; quién lo hizo queda en la auditoría (D-31). 4. Si la persona queda sin vínculos vigentes, suspende la cuenta. |
| **Eventos** | `EstudianteDesvinculado` (EVT-017) o `DocenteDesvinculado` (EVT-019); `CuentaSuspendida` si aplica |
| **Reglas** | RN-057, RN-065, RN-114, RN-115, INV-013, INV-016 |

---

### CU-034 Consultar estructura organizacional 🆕

Agrupa las consultas de RF-016 a RF-033 y RF-072 a RF-077:

| Consulta | Quién |
|---|---|
| Rector vigente y Coordinador Académico vigente | Todos |
| Facultades con su coordinador vigente | Todos (detalle completo: Coord. Académico) |
| Historial de coordinadores de una Facultad | Coord. Académico, Coord. de esa Facultad |
| Programas de una Facultad con su coordinador | Todos (gestión: Coord. Académico y Coord. de Facultad) |
| Historial de coordinadores de un Programa | Coord. Académico, Coord. de Facultad, Coord. del Programa |
| Estudiantes y docentes de un Programa | Coord. del Programa, Coord. de su Facultad, Coord. Académico |
| Programas de un estudiante o docente (vigentes e históricos) | La propia persona y sus coordinadores |
| Programa o Facultad que acompaña un coordinador | Todos |

---

### CU-035 Gestionar categorías 🆕

| Campo | Contenido |
|---|---|
| **Actor** | Administrador, Rector o Coordinador Académico (D-01) |
| **Operaciones** | Crear (nombre único, descripción, queda activa), modificar, activar y desactivar |
| **Eventos** | `CategoriaCreada` (EVT-020), `CategoriaModificada` (EVT-021), `CategoriaActivada` (EVT-022), `CategoriaDesactivada` (EVT-023) |
| **Excepciones** | Otro rol → se rechaza (RN-137 a RN-140). Nombre duplicado → se rechaza. Activar una ya activa o desactivar una ya inactiva → se informa. |
| **Reglas** | RN-132 a RN-145, INV-018, INV-019 |

---

### CU-036 Iniciar revisión de contenido 🆕

| Campo | Contenido |
|---|---|
| **Actor** | Moderador |
| **Precondiciones** | El contenido está `REPORTADO` y no tiene una moderación en curso |
| **Postcondición** | Existe una moderación `EN_CURSO`. El contenido y sus reportes pendientes quedan `EN_REVISION` |
| **Evento** | `RevisionIniciada` (EVT-035, RN-239) |
| **Reglas** | RN-203, RN-208, RN-211 |

### CU-037 Liberar contenido revisado 🆕

| Campo | Contenido |
|---|---|
| **Actor** | Moderador |
| **Precondiciones** | El contenido está `EN_REVISION` |
| **Flujo** | 1. El moderador elige "Liberar" y escribe el motivo. 2. El sistema cierra la moderación con decisión `LIBERAR`. 3. El contenido vuelve a `VISIBLE`. 4. Los reportes quedan `RECHAZADO` y se notifica a los reportantes. |
| **Eventos** | `ContenidoLiberado` (EVT-037, RN-242), `RevisionCerrada` (EVT-036, RN-240) |

---

### CU-038 Consultar auditoría 🆕

| **Actor** | Autoridades, según su ámbito |
|---|---|
| **Flujo** | El sistema lista los registros de auditoría filtrables por fecha, actor, acción y tipo de objeto. **Ámbito**: el Administrador ve los registros técnicos y de cuentas que gestiona. El Rector y el Coord. Académico ven todos. Cada coordinador ve los de su ámbito (RN-116). Cada usuario ve su propia actividad (CU-021). |

### CU-039 Consultar notificaciones 🆕

| **Actor** | Usuario activo |
|---|---|
| **Flujo** | El sistema muestra sus notificaciones, las no leídas primero, con contador. Al abrir una se marca como leída y se navega al objeto relacionado. |

### CU-040 Consultar indicadores 🆕

| **Actor** | Usuario activo (las autoridades ven también los de su ámbito) |
|---|---|
| **Flujo** | El sistema muestra: Kudos por categoría, Kudos por Facultad y Programa del destinatario, personas más reconocidas, Kudos mejor calificados y participación por periodo. |

### CU-041 Gestionar parámetros globales 🆕

| **Actor** | Administrador |
|---|---|
| **Flujo** | 1. Consulta los parámetros. 2. Modifica el valor de uno; el sistema valida que concuerde con su tipo de dato. 3. Se audita el cambio con valor anterior y nuevo. |
| **Reglas** | RN-130, RN-131, D-11 |

### CU-042 Consultar muro de Kudos 🆕

| **Actor** | Usuario activo |
|---|---|
| **Flujo** | El sistema muestra los Kudos visibles, reportados o en revisión (los vetados no aparecen en el muro), del más reciente al más antiguo, paginados de a 20. Se pueden filtrar por categoría y por persona. Los Kudos largos se recortan **solo visualmente**, con "Ver más" (RNF-038). |
