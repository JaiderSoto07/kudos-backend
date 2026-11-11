# Decisiones de diseño

[Inicio](../../README.md) · [Requisitos](requisitos.md) · [Reglas de negocio](reglas-de-negocio.md) · [Matriz de trazabilidad](matriz-trazabilidad.md)

El enunciado deja vacíos y tiene algunas contradicciones. Este documento registra cada uno, la decisión tomada y su justificación. Así el modelo y el código se apoyan en criterios explícitos y defendibles en la sustentación (DOC-008, RNF-032).

Cada decisión tiene un código **D-xx**. El modelo, el código y las pruebas lo referencian. Los códigos se asignaron en el orden en que se detectó cada caso y aquí se agrupan por tema; el [resumen](#resumen-de-decisiones) al final las lista en orden numérico.

---

## 1. Contradicciones del enunciado

### D-01 — Quién gestiona categorías

| | |
|---|---|
| **Conflicto** | RN-134 dice "*Solo* los Administradores podrán crear categorías", pero RN-135 y RN-136 permiten crear también al Rector y al Coordinador Académico. |
| **Decisión** | Pueden crear, modificar, activar y desactivar categorías: **Administrador, Rector y Coordinador Académico**. Nadie más. |
| **Justificación** | RN-141, RN-142, RN-143 y la Matriz de Gestión de Categorías (doc. 10 §8) nombran estos tres roles. RN-134 se interpreta como un error de redacción. |

### D-02 — Quién puede vetar

| | |
|---|---|
| **Conflicto** | RN-193 dice "*Solo* el Rector" y RN-194 dice "*Solo* el Coordinador Académico". |
| **Decisión** | Pueden vetar Kudos y réplicas **el Rector y el Coordinador Académico**. Nadie más. |
| **Justificación** | RN-243, INV-033 y la Matriz de Moderación (doc. 10 §7) lo confirman. |

### D-06 — El Coordinador de Programa tiene un solo programa

| | |
|---|---|
| **Conflicto** | El glosario dice que gestiona estudiantes y docentes "de sus Programas" (plural). INV-009 dice "exactamente un Programa Académico". |
| **Decisión** | Un Coordinador de Programa está asociado a **exactamente un** Programa. |
| **Justificación** | Los invariantes (INV-009, RN-023) prevalecen sobre la redacción del glosario. |

---

## 2. Vacíos del enunciado

### D-03 — Quién aprueba a los Docentes

| | |
|---|---|
| **Vacío** | La cadena de aprobación (RN-090 a RN-095) no menciona a los Docentes. |
| **Decisión** | Los Docentes los aprueba el **Coordinador del Programa** al que solicitan vincularse. |
| **Justificación** | La Matriz de Gestión (doc. 10 §6) dice que el Coordinador de Programa gestiona "Docentes y Estudiantes". Por analogía con RN-090, quien gestiona aprueba. |

### D-04 — Quién crea Facultades y Programas

| | |
|---|---|
| **Vacío** | RF-015 y RF-020 exigen crear Facultades y Programas, pero no dicen quién. |
| **Decisión** | El **Coordinador Académico** crea y modifica Facultades y Programas. Los Coordinadores de Facultad consultan los Programas de su Facultad (RN-118). |
| **Justificación** | Es la autoridad académica de toda la Universidad (RN-121, RN-122). El Administrador es técnico (RN-131) y no gobierna la estructura académica. |

### D-05 — Facultad sin programas (RN-045)

| | |
|---|---|
| **Vacío** | "Una Facultad puede existir sin Programas únicamente durante procesos de creación institucional". |
| **Decisión** | La Facultad tiene estado: `EN_CONFORMACION` → `ACTIVA` → `INACTIVA`. Nace en `EN_CONFORMACION` y pasa a `ACTIVA` cuando se le crea su primer Programa. Solo una Facultad `ACTIVA` puede tener Coordinador de Facultad (INV-010). |
| **Alcance** | En esta versión una Facultad `INACTIVA` no se reactiva: si la Universidad la retoma, se crea de nuevo. Se conserva su historia (D-28). |
| **Justificación** | Convierte una regla de texto en un **ciclo de vida verificable** por el propio objeto (objetos ricos, ARQ-047). |

### D-07 — Suspender y desactivar cuentas

| | |
|---|---|
| **Vacío** | RN-216 y RN-217 exigen eventos de cuenta suspendida y desactivada, pero no dicen quién ni cuándo. |
| **Decisión** | **Suspender** es temporal y reversible: la cuenta se puede reactivar. **Desactivar** es definitivo para ese rol y conserva todo el historial. Lo hace la autoridad que **gestiona** al usuario según la Matriz de Gestión (por ejemplo, el Coord. de Programa a sus estudiantes). Ambas acciones exigen un motivo y quedan auditadas. |
| **Precisión** | Un Docente o Estudiante puede estar vinculado a varios Programas. Su cuenta la puede suspender, reactivar o desactivar el Coordinador de **cualquiera** de los Programas donde tenga un vínculo vigente: RN-112 permite gestionar a quienes pertenecen a su programa y RN-114 solo prohíbe gestionar a quienes pertenecen *exclusivamente* a otros. Para retirar a la persona solo de su programa, el coordinador usa la desvinculación (D-16). |
| **Justificación** | Respeta POL-004 (cada actor actúa en su ámbito) y RN-098 y RN-099 (auditoría de activación y desactivación). |

### D-08 — Quiénes son las "autoridades autorizadas"

| | |
|---|---|
| **Vacío** | CU-012 (consultar reportes) y CU-015 (histórico de vetos) hablan de "autoridades autorizadas". |
| **Decisión** | Consultan todos los reportes y el histórico de vetos **el Rector y el Coordinador Académico**, porque son quienes deciden la moderación. Cualquier usuario puede consultar los reportes que él mismo hizo. |
| **Justificación** | Quien decide necesita ver. Los demás roles no participan en moderación (RN-244 a RN-248). |

### D-09 — Autenticación

| | |
|---|---|
| **Vacío** | RNF-001 exige autenticación, pero no define el mecanismo. Sigma usaba Google OAuth. |
| **Decisión** | Login con **correo institucional y contraseña**. La contraseña se guarda con hash BCrypt y la sesión la maneja Spring Security. No se usa Google OAuth. |
| **Justificación** | El enunciado exige verificar correo **y celular** con códigos propios (RN-077 a RN-082). Además, la cuenta solo entra si está aprobada y verificada (RN-085 a RN-089). Ese control debe estar en nuestro dominio, no en un proveedor externo. |

### D-10 — Cómo se verifican correo y celular

| | |
|---|---|
| **Vacío** | No hay proveedores reales de correo ni de SMS. |
| **Decisión** | Al registrarse, o al cambiar el correo o el celular, se genera un **código de verificación** de 6 dígitos que vence en 15 minutos. El envío pasa por una interfaz `EnviadorNotificacion`. En este proyecto la implementación es **simulada**: el código queda en la bandeja de notificaciones y en el log. |
| **Justificación** | Por inversión de dependencias (ARQ-029), el dominio no conoce SMTP ni SMS. Pasar a un proveedor real solo exige otra implementación de la interfaz. Esto es un **evento de integración** (doc. 01 §12). |

### D-11 — Parámetros globales

| | |
|---|---|
| **Vacío** | RN-130 dice que el Administrador gestiona "parámetros globales", sin listarlos. |
| **Decisión** | Hay una tabla de parámetros administrables. Ejemplos: dominio del correo institucional (`uco.edu.co`), vigencia del código de verificación (15 min) e intentos máximos de verificación (5). |
| **Justificación** | Da contenido real a RN-130 y evita valores fijos en el código (OCP). |

### D-12 — Formato de los datos de contacto

| | |
|---|---|
| **Decisión** | **Correo**: debe terminar en `@` + el dominio institucional del parámetro. Se guarda en minúsculas y sin espacios. **Celular**: número colombiano de 10 dígitos que empieza por `3`. |
| **Justificación** | Correo y celular son **objetos de valor** (`CorreoInstitucional`, `NumeroCelular`). Se validan a sí mismos al construirse, así que nunca existe uno inválido. |

---

## 3. Roles y estructura organizacional

### D-13 — Exclusividad de roles por diseño

| | |
|---|---|
| **Problema** | RN-001 a RN-038 son 38 reglas de "X no puede ser Y". |
| **Decisión** | El `Usuario` tiene **un solo** atributo de rol activo y guarda su historial en `AsignacionRol`. Como no puede tener dos roles a la vez, ninguna combinación es posible por construcción. No hacen falta 30 validaciones "si es Rector no puede ser Docente". |
| **Justificación** | Es el mejor tipo de invariante: el que **la estructura hace imposible de violar** (KISS, ARQ-039). Se refuerza en base de datos: una sola asignación de rol vigente por usuario. |

### D-14 — Unicidad de Rector, Coordinador Académico y coordinadores

| | |
|---|---|
| **Decisión** | Hay dos niveles de control. **Dominio**: una regla de negocio verifica que no exista otro titular activo antes de asignar. **Base de datos**: índices únicos filtrados de SQL Server, por ejemplo `UNIQUE ... WHERE vigente = 1`. |
| **Justificación** | INV-001 a INV-004. El doble control también protege ante concurrencia (ARQ-063). |

### D-15 — Sustitución de coordinadores

| | |
|---|---|
| **Decisión** | Aprobar un nuevo coordinador para una Facultad o Programa que ya tiene uno es una **sustitución**. Se cierra la asignación anterior (`fechaFin`) y se abre la nueva. El coordinador saliente queda **sin rol vigente** y su cuenta pasa a `DESACTIVADA`, con su historial intacto. Más adelante puede volver a registrarse con otro rol. |
| **Justificación** | RN-043, RN-044, RN-051 y RN-052 piden historial y auditoría. RN-220 y RN-223 piden los eventos de reemplazo. |

### D-16 — Vínculos de Docentes y Estudiantes con Programas

| | |
|---|---|
| **Decisión** | La solicitud de registro indica **un** Programa inicial y la aprueba su coordinador. Los vínculos adicionales los crea el coordinador de cada Programa. Desvincular **no borra**: cierra el vínculo (`fechaFin`) y conserva el historial. Si un Docente o Estudiante queda sin vínculos vigentes, su cuenta se suspende. |
| **Justificación** | RN-053 a RN-057, RN-061 a RN-065, INV-011 a INV-016 y el ámbito del coordinador (RN-112 a RN-115). |

### D-17 — Solicitud de Registro y activación

| | |
|---|---|
| **Decisión** | **Estudiante, Docente y los tres tipos de coordinador** se autorregistran. Se crea una `SolicitudRegistro` en estado `PENDIENTE` → `APROBADA` o `RECHAZADA`, que decide la autoridad correspondiente. **Rector y Administrador** no se autorregistran: los registra un Administrador (RN-094, RN-095). Para todos, la cuenta pasa a `ACTIVA` solo cuando se cumplen a la vez aprobación + correo verificado + celular verificado (RN-084 a RN-089). Nadie puede aprobar su propia solicitud (RN-108). |
| **Estados de la cuenta** | `PENDIENTE_APROBACION`, `PENDIENTE_VERIFICACION`, `ACTIVA`, `SUSPENDIDA` y `DESACTIVADA`. |

### D-18 — Orden de arranque del sistema

| | |
|---|---|
| **Decisión** | El script de datos iniciales crea **el primer Administrador**. Desde ahí el flujo es: Admin registra Rector → Coord. Académico se registra y el Rector lo aprueba → el Coord. Académico crea Facultades y Programas → se registran y aprueban los coordinadores → docentes y estudiantes. |
| **Justificación** | La cadena de aprobación necesita una raíz. RN-095 dice que los Administradores los registran otros Administradores, así que el primero tiene que venir de la instalación. |

---

## 4. Contenido: Kudos, réplicas, calificaciones y reportes

### D-19 — Restricciones de sentido común sobre el Kudo

| | |
|---|---|
| **Decisión** | 1) Nadie se da un Kudo a sí mismo. 2) El destinatario debe tener cuenta `ACTIVA`. 3) La categoría debe estar activa (RN-144). 4) El contenido no puede estar vacío. 5) **No se edita ni se elimina** un Kudo publicado. |
| **Justificación** | Un "reconocimiento" a uno mismo no tiene sentido. Prohibir la edición y el borrado garantiza trazabilidad y contenido completo (RN-151, RN-154, INV-024). |

### D-20 — Ciclo de vida de Kudo y Réplica

```
CREADO ──publicar──► VISIBLE ──reportar──► REPORTADO ──iniciar revisión──► EN_REVISION
                        │                      │                              │
                        │                      │                    ┌─────────┴─────────┐
                        └──────────vetar───────┴───────vetar───────►│      VETADO       │ (final)
                                                                    └───────────────────┘
                                                     EN_REVISION ──liberar──► VISIBLE
```

| | |
|---|---|
| **Decisión** | `CREADO` es transitorio: al confirmarse la publicación pasa de inmediato a `VISIBLE` (CU-001 paso 7). Pasa a `REPORTADO` con el primer reporte pendiente. Un moderador lo lleva a `EN_REVISION`. Desde revisión queda **vetado** (los reportes se aceptan) o se **libera** (vuelve a `VISIBLE` y los reportes se rechazan). Un moderador puede vetar en cualquier estado no final (RN-204). `VETADO` es final. Réplica y Kudo comparten el mismo ciclo de vida. |
| **Patrón** | **State**: cada estado decide qué transiciones permite, lo que evita `if/switch` dispersos. Ver la sección de patrones. |

### D-21 — Efectos del veto

| | |
|---|---|
| **Decisión** | El contenido vetado deja de mostrarse (aparece como *"Contenido vetado por moderación"*). Ya no admite réplicas, calificaciones ni reportes nuevos (RN-205). Si se veta un Kudo, su conversación se **cierra** pero se conserva. Si se veta una réplica, sus respuestas siguen visibles para no romper el árbol. Todo veto exige un motivo y queda auditado (INV-034, INV-035, RN-250). |

### D-22 — Árbol de réplicas sin ciclos

| | |
|---|---|
| **Decisión** | Una réplica guarda su **padre**, que es un Kudo u otra réplica (INV-027), y el **Kudo raíz** de su conversación. El padre se fija al crearla y **nunca cambia**. Como el padre siempre existe antes que la hija, ni una autorreferencia (INV-028) ni un ciclo (INV-029) son posibles por construcción. |
| **Patrón** | **Composite**: Kudo y Réplica comparten la interfaz `ContenidoConversable`, que define qué se puede responder, calificar y reportar. La conversación se arma como árbol recursivo. |

### D-23 — Calificaciones

| | |
|---|---|
| **Decisión** | Es un objeto de valor `Estrellas` (1–5). Hay **una** calificación activa por usuario y contenido (RN-178 a RN-181). Si el usuario vuelve a calificar, **se modifica** la existente: se genera el evento "Calificación actualizada" y se guarda el valor anterior en la auditoría. Nadie califica su propio contenido. No se puede calificar contenido vetado. El atributo `activa` existe porque el enunciado habla de "calificación **activa**" (RN-180, RN-181) y deja abierta la opción de retirar una calificación en el futuro; en esta versión una calificación se **modifica**, no se retira, así que permanece activa. |
| **Indicadores (RN-185)** | Promedio de estrellas y número de calificaciones por contenido. Kudos recibidos y emitidos por persona. Kudos por categoría y por Facultad o Programa. Se recalculan con el evento "Calificación registrada/actualizada" (RN-238). |

### D-24 — Reportes

| | |
|---|---|
| **Decisión** | El motivo es un catálogo (CU-010): Lenguaje ofensivo, Acoso, Discriminación, Spam, Información falsa u Otro. "Otro" exige descripción. Un usuario no puede tener dos reportes `PENDIENTE` sobre el mismo contenido ni reportar su propio contenido. Estados: `PENDIENTE` → `EN_REVISION` → `ACEPTADO` o `RECHAZADO` (RN-210 a RN-212). La decisión del moderador sobre un contenido resuelve **todos** sus reportes abiertos. |

### D-25 — Evento "Kudo consultado" (RN-229)

| | |
|---|---|
| **Decisión** | Se registra como un **contador de visualizaciones** del Kudo, no como una fila de auditoría por cada vista. |
| **Justificación** | Una fila de auditoría por lectura inflaría la tabla sin aportar a la reconstrucción de decisiones (RNF-010). El evento existe y alimenta un indicador. |

---

## 5. Trazabilidad y notificaciones

### D-26 — Auditoría por eventos de dominio

| | |
|---|---|
| **Decisión** | Cada operación relevante produce un **evento de dominio**, por ejemplo `KudoPublicado` o `ContenidoVetado`. Un publicador entrega el evento a quien le interese: **auditoría**, **notificaciones** e **indicadores**. Un registro de auditoría guarda: fecha, actor, acción, tipo y id del objeto, estado anterior y nuevo, motivo y detalle. |
| **Patrón** | **Observer** (publicador/suscriptores). La lógica de negocio no sabe que existe la auditoría, así que agregar un nuevo suscriptor no modifica el negocio (OCP, SRP). |

### D-27 — Notificaciones

| | |
|---|---|
| **Decisión** | Son notificaciones dentro de la aplicación: Kudo recibido, respuesta recibida, solicitud aprobada o rechazada, contenido vetado, reporte rechazado (el contenido reportado se liberó) y códigos de verificación (D-10). Se marcan como leídas. |

### D-28 — Nada se borra

| | |
|---|---|
| **Decisión** | Usuarios, Facultades, Programas, Categorías, vínculos, Kudos y réplicas **no se eliminan físicamente**. Se inactivan, cierran o vetan. |
| **Justificación** | POL-007 (reconstruir decisiones históricas) y RN-100, RN-145 e INV-013. |

### D-31 — Quién hizo cada cosa lo dice la auditoría

| | |
|---|---|
| **Problema** | Guardar en cada entidad quién la creó o modificó (`creadaPor` en Facultad, Programa y Categoría, `asignadoPor` en la asignación de rol, `vinculadoPor`/`desvinculadoPor` en los vínculos, `modificadoPor` en los parámetros) repite lo que ya registra la auditoría. Además convierte a `Usuario` en un objeto conectado con casi todo el modelo. |
| **Decisión** | La autoría de las operaciones se consulta en `Auditoria`, que guarda actor, acción, objeto, fecha y motivo (D-26). Solo se conserva como atributo cuando el autor **es parte de la decisión de negocio**: `SolicitudRegistro.decididaPor` (quién aprobó o rechazó) y `Moderacion.moderador` (quién decidió el veto). |
| **Justificación** | Una sola fuente de verdad (DRY, ARQ-037), menor acoplamiento de `Usuario` (ARQ-016) y un modelo de dominio más legible. |

---

## 6. Arquitectura y tecnología

### D-29 — Estilo arquitectónico: capas del curso con dominio enriquecido

| | |
|---|---|
| **Decisión** | Se usa la misma arquitectura por capas del proyecto de referencia del curso (Sigma — Pizzería UCO): `controlador → fachada → negocio → dao`, con los paquetes `dominio`, `entidad`, `dto`, ensambladores y `transversal`. |
| **Cambio respecto a Sigma** | En Sigma los objetos de `dominio` solo tienen Builder y getters, es decir, son **anémicos**. El enunciado penaliza eso (ARQ-008, ARQ-047, ARQ-049). En Kudos los objetos de dominio **tienen comportamiento**: por ejemplo `kudo.vetar(moderador, motivo)`, `reporte.aceptar()` o `cuenta.intentarActivar()`. Las clases `Rule` se mantienen solo para las validaciones que **necesitan consultar la base de datos**, como la unicidad de correo o que no exista otro Rector. |
| **Justificación** | Es el estilo visto en el curso (ARQ-001), y enriquecido cumple los criterios de objetos ricos y Tell, Don't Ask (ARQ-043). |

### D-30 — Persistencia

| | |
|---|---|
| **Decisión** | **SQL Server** con **JDBC + HikariCP**, sin JPA, igual que Sigma. La fábrica abstracta `DAOFactory` permite cambiar de motor. La transacción la controla la fachada. |
| **Justificación** | Mantiene el estilo del curso. La fachada define el límite transaccional de cada caso de uso, que coincide con el agregado que modifica. |

---

## Resumen de decisiones

| Código | Tema | Reglas relacionadas |
|---|---|---|
| D-01 | Categorías: Admin, Rector y Coord. Académico | RN-134 a RN-143 |
| D-02 | Vetos: Rector y Coord. Académico | RN-193 a RN-200, RN-243 |
| D-03 | Docentes aprobados por el Coord. de Programa | RN-090, matriz de gestión |
| D-04 | El Coord. Académico crea Facultades y Programas | RF-015, RF-020 |
| D-05 | Estados de Facultad | RN-045, INV-010 |
| D-06 | Coord. de Programa con un solo programa | INV-009 |
| D-07 | Suspender y desactivar | RN-216, RN-217 |
| D-08 | Autoridades de moderación | CU-012, CU-015 |
| D-09 | Login propio con BCrypt | RNF-001 |
| D-10 | Códigos de verificación (envío simulado) | RN-077 a RN-082 |
| D-11 | Parámetros globales | RN-130 |
| D-12 | Objetos de valor para correo y celular | RN-069 a RN-076 |
| D-13 | Un solo rol activo por estructura | RN-001 a RN-038 |
| D-14 | Unicidad con regla de negocio e índice filtrado | INV-001 a INV-004 |
| D-15 | Sustitución de coordinadores | RN-043, RN-044, RN-220, RN-223 |
| D-16 | Vínculos con Programas | RN-053 a RN-065 |
| D-17 | Solicitud de registro y estados de cuenta | RN-084 a RN-095, RN-108 |
| D-18 | Primer Administrador por script | RN-095 |
| D-19 | Restricciones del Kudo | RN-146 a RN-154 |
| D-20 | Ciclo de vida del contenido (State) | RN-201 a RN-209 |
| D-21 | Efectos del veto | RN-205, INV-034, INV-035 |
| D-22 | Árbol de réplicas (Composite) | INV-025 a INV-029 |
| D-23 | Calificaciones e indicadores | RN-175 a RN-185 |
| D-24 | Reportes | RN-186 a RN-190, RN-210 a RN-212 |
| D-25 | Contador de visualizaciones | RN-229 |
| D-26 | Auditoría por eventos (Observer) | RN-096 a RN-100, RN-249, RN-250 |
| D-27 | Notificaciones | Entidad Notificación |
| D-28 | No hay borrado físico | POL-007, RN-100 |
| D-29 | Capas del curso con dominio rico | ARQ-001, ARQ-047 |
| D-30 | SQL Server + JDBC | ARQ-062 a ARQ-064 |
| D-31 | Autoría de operaciones en la auditoría | RN-100, ARQ-016, ARQ-037 |
