# Requisitos funcionales y no funcionales

[Inicio](../../README.md) · [Reglas de negocio](reglas-de-negocio.md) · [Decisiones de diseño](decisiones-de-diseno.md) · [Casos de uso](casos-de-uso.md) · [Matriz de trazabilidad](matriz-trazabilidad.md)

El documento 07 del enunciado entrega un catálogo mínimo de requisitos y pide **refinarlos, complementarlos, consolidarlos, trazarlos y validarlos**. Aquí se hace así:

- Los requisitos **RF-001 a RF-077** y **RNF-001 a RNF-035** conservan el código del enunciado. Su texto se **refina**: se precisa qué hace el sistema, con qué condición y qué regla aplica.
- Los requisitos **RF-078 en adelante** y **RNF-036 en adelante** son **complementados**: el enunciado los exige de forma implícita en reglas, eventos o casos de uso, pero no los lista como requisito. Se marcan con 🆕.
- La relación de cada requisito con reglas, casos de uso, clases y pruebas está en la [matriz de trazabilidad](matriz-trazabilidad.md).
- **Alcance de la versión entregada:** están implementados **todos** los requisitos funcionales del enunciado (RF-001 a RF-077) y los complementarios, salvo cuatro complementarios marcados ⛔ que quedan como **trabajo futuro**: RF-082 (historial de actividad propia), RF-089 (suspender, reactivar o desactivar cuentas a mano; la suspensión y reactivación automáticas sí existen, D-07), RF-096 (indicadores) y RF-097 (pantalla de parámetros globales).

---

## 1. Requisitos funcionales

### 1.1 Gestión de usuarios, identidad y acceso

| Código | Requisito refinado | Reglas / decisiones |
|---|---|---|
| RF-001 | El sistema debe permitir que una persona se registre indicando nombres, apellidos, celular, rol solicitado y la Facultad o Programa que corresponda. El nombre y el correo los entrega Google al ingresar (D-09); el correo debe ser de un dominio institucional. El registro crea la cuenta en estado `PENDIENTE_APROBACION` y una `SolicitudRegistro` pendiente. | RN-069 a RN-076, D-17 |
| RF-002 | El sistema debe exigir el correo institucional en el registro y rechazar correos cuyo dominio no sea el institucional (parámetro `DOMINIO_CORREO`). | RN-069, RN-071, POL-001, D-12 |
| RF-003 | El sistema debe exigir un número celular colombiano válido (10 dígitos que inician en 3) en el registro. | RN-070, RN-072, POL-002, D-12 |
| RF-004 | El sistema debe rechazar el registro o el cambio de correo si ya existe otro usuario con ese correo. | RN-073 |
| RF-005 | El sistema debe rechazar el registro o el cambio de celular si ya existe otro usuario con ese número. | RN-074 |
| RF-006 | El sistema debe validar el correo institucional. Como el ingreso es con Google, el correo queda verificado cuando Google confirma que la persona es dueña de esa cuenta; si el usuario cambia su correo, se verifica al ingresar con la nueva cuenta. | RN-077, RN-079, D-09, D-10 |
| RF-007 | El sistema debe permitir validar el número celular con un código de 6 dígitos con las mismas condiciones del RF-006. | RN-078, RN-080, D-10 |
| RF-008 | El sistema debe permitir a cada usuario consultar su información personal. También debe permitir a las autoridades consultar la de los usuarios de su ámbito. | RN-101, RN-112 a RN-127 |
| RF-009 | El sistema debe permitir a cada usuario actualizar sus nombres, apellidos, correo y celular. No debe permitir modificar el propio rol. | RN-102, RN-104, RN-105, RN-107, RN-109 |
| RF-010 | Al cambiar el correo, el sistema debe marcarlo como no verificado y generar una nueva validación pendiente. Mientras el nuevo correo no se valide, la cuenta deja de estar `ACTIVA`. | RN-081, RN-110, D-17 |
| RF-011 | Al cambiar el celular, el sistema debe marcarlo como no verificado y generar una nueva validación pendiente, con el mismo efecto del RF-010. | RN-082, RN-111 |
| RF-078 🆕 | El sistema debe permitir iniciar sesión con la cuenta de Google y dar acceso a las funciones **solo** a cuentas `ACTIVA`, es decir, aprobadas y con correo y celular verificados. Si la cuenta no está activa, debe indicar qué le falta. | RN-085 a RN-089, POL-003, D-09 |
| RF-079 🆕 | El sistema debe permitir cerrar sesión. | RNF-001 |
| RF-080 🆕 | El sistema debe permitir solicitar un nuevo código de verificación. Al hacerlo, invalida los códigos anteriores del mismo canal. | D-10 |
| RF-081 🆕 | El sistema debe permitir a cada usuario consultar el estado de sus validaciones (correo, celular y aprobación). | RN-103 |
| RF-082 🆕 ⛔ | El sistema debe permitir a cada usuario consultar su historial de actividad (Kudos emitidos y recibidos, réplicas, calificaciones, reportes y cambios a su cuenta). | RN-106 |

### 1.2 Gestión de roles

| Código | Requisito refinado | Reglas / decisiones |
|---|---|---|
| RF-012 | El sistema debe permitir registrar usuarios bajo cualquiera de los 7 roles institucionales, según el flujo que corresponda a cada uno: autorregistro con aprobación, o registro directo por un Administrador. | RN-090 a RN-095, D-17 |
| RF-013 | El sistema debe garantizar que una persona tenga como máximo **un** rol vigente. | RN-001 a RN-004, INV-005, D-13 |
| RF-014 | El sistema debe impedir la coexistencia de roles incompatibles. También debe impedir un segundo Rector, un segundo Coordinador Académico y más de un coordinador por Facultad o Programa. | RN-005 a RN-038, INV-001 a INV-006, D-14 |

### 1.3 Gestión de Facultades

| Código | Requisito refinado | Reglas / decisiones |
|---|---|---|
| RF-015 | El sistema debe permitir al Coordinador Académico crear Facultades con nombre único. Nacen en estado `EN_CONFORMACION`. | RN-039, RN-040, RN-045, D-04, D-05 |
| RF-016 | El sistema debe permitir consultar Facultades. El Coordinador Académico ve todas; el Coordinador de Facultad ve la suya. | RN-122 |
| RF-017 | El sistema debe permitir al Coordinador Académico modificar el nombre de una Facultad. | D-04 |
| RF-018 | El sistema debe permitir consultar el Coordinador de Facultad vigente de una Facultad. | RN-042 |
| RF-019 | El sistema debe permitir consultar el historial de Coordinadores de una Facultad, con fechas de inicio y fin. | RN-043, RN-044 |
| RF-083 🆕 | El sistema debe permitir al Coordinador Académico inactivar una Facultad. No se permite si tiene Programas activos. | D-05, D-28 |

### 1.4 Gestión de Programas Académicos

| Código | Requisito refinado | Reglas / decisiones |
|---|---|---|
| RF-020 | El sistema debe permitir al Coordinador Académico crear Programas Académicos con nombre único, asociados a exactamente una Facultad no inactiva. El primer Programa de una Facultad la pasa a `ACTIVA`. | RN-046 a RN-049, D-04, D-05 |
| RF-021 | El sistema debe permitir consultar Programas Académicos. El Coordinador de Facultad ve los de su Facultad (RN-118). | RN-118 |
| RF-022 | El sistema debe permitir al Coordinador Académico modificar el nombre de un Programa. | D-04 |
| RF-023 | El sistema debe permitir consultar el Coordinador de Programa vigente de un Programa. | RN-050 |
| RF-024 | El sistema debe permitir consultar el historial de Coordinadores de un Programa. | RN-051, RN-052 |
| RF-084 🆕 | El sistema debe permitir al Coordinador Académico inactivar un Programa. No se permite si tiene vínculos vigentes. | D-28 |

### 1.5 Gestión de Estudiantes y Docentes

| Código | Requisito refinado | Reglas / decisiones |
|---|---|---|
| RF-025 | El sistema debe permitir registrar estudiantes mediante autorregistro y solicitud a un Programa. | RN-058, RN-090 |
| RF-026 | El sistema debe permitir al Coordinador de Programa vincular a su Programa un estudiante ya registrado. Así un estudiante puede pertenecer a varios Programas. | RN-053, RN-112 |
| RF-027 | El sistema debe permitir consultar los Programas vigentes e históricos de un estudiante. | RN-054 |
| RF-028 | El sistema debe permitir consultar los estudiantes vigentes de un Programa. | RN-055 |
| RF-029 | El sistema debe conservar la trazabilidad de vinculaciones y desvinculaciones estudiante-programa: quién, cuándo y por qué. | RN-056, RN-057, INV-012, INV-013 |
| RF-030 | El sistema debe permitir registrar docentes mediante autorregistro y solicitud a un Programa. | RN-066, D-03 |
| RF-031 | El sistema debe permitir al Coordinador de Programa vincular a su Programa un docente ya registrado. | RN-061, RN-113 |
| RF-032 | El sistema debe permitir consultar los Programas de un docente. | RN-062 |
| RF-033 | El sistema debe permitir consultar los docentes de un Programa. | RN-063 |
| RF-034 | El sistema debe conservar la trazabilidad de las asociaciones docente-programa. | RN-064, RN-065, INV-015, INV-016 |
| RF-085 🆕 | El sistema debe permitir al Coordinador de Programa desvincular de su Programa a un estudiante o docente, indicando un motivo. Si la persona queda sin vínculos vigentes, su cuenta se suspende. | RN-057, RN-065, RN-114, RN-115, D-16 |

### 1.6 Gestión de aprobaciones y estado de cuentas

| Código | Requisito refinado | Reglas / decisiones |
|---|---|---|
| RF-035 | El sistema debe permitir al Coordinador de Programa aprobar solicitudes de estudiantes y docentes de **su** Programa. | RN-090, D-03 |
| RF-036 | El sistema debe permitir al Coordinador de Facultad aprobar solicitudes de Coordinadores de Programa de Programas de **su** Facultad. | RN-091, RN-117 |
| RF-037 | El sistema debe permitir al Coordinador Académico aprobar solicitudes de Coordinadores de Facultad. | RN-092, RN-121 |
| RF-038 | El sistema debe permitir al Rector aprobar solicitudes de Coordinadores Académicos. | RN-093, RN-126 |
| RF-039 | El sistema debe permitir a un Administrador registrar al Rector. No se permite si ya existe un Rector vigente; para cambiarlo, primero se desactiva el actual. | RN-094, RN-129, INV-001 |
| RF-040 | El sistema debe permitir a un Administrador registrar otros Administradores. | RN-095, RN-129 |
| RF-086 🆕 | El sistema debe permitir a la autoridad competente rechazar una solicitud con motivo obligatorio. | RN-212 (por analogía), D-17 |
| RF-087 🆕 | El sistema debe mostrar a cada autoridad únicamente las solicitudes pendientes de su ámbito. | POL-004, RN-112 a RN-127 |
| RF-088 🆕 | El sistema debe impedir que un usuario apruebe su propia solicitud o decida sobre usuarios fuera de su ámbito. | RN-108, POL-004, POL-005 |
| RF-089 🆕 ⛔ | El sistema debe permitir a la autoridad que gestiona a un usuario suspenderlo, reactivarlo o desactivarlo, indicando motivo. | RN-216, RN-217, D-07 |

### 1.7 Gestión de categorías

| Código | Requisito refinado | Reglas / decisiones |
|---|---|---|
| RF-041 | El sistema debe permitir crear categorías con nombre único y descripción **solo** a Administrador, Rector y Coordinador Académico. | RN-134 a RN-140, D-01 |
| RF-042 | El sistema debe permitir a esos mismos roles modificar el nombre y la descripción de una categoría. Los Kudos existentes conservan su categoría. | RN-141, RN-145 |
| RF-043 | El sistema debe permitir a esos mismos roles activar una categoría inactiva. | RN-142 |
| RF-044 | El sistema debe permitir a esos mismos roles desactivar una categoría activa. Una categoría desactivada no puede usarse para Kudos nuevos. | RN-143, RN-144 |
| RF-045 | El sistema debe permitir consultar categorías. Todos los usuarios ven las activas; los gestores ven también las inactivas. | INV-018 |

### 1.8 Gestión de Kudos

| Código | Requisito refinado | Reglas / decisiones |
|---|---|---|
| RF-046 | El sistema debe permitir a cualquier usuario activo crear un Kudo. Se indica el destinatario (activo y distinto de sí mismo), una categoría activa y un contenido no vacío. El Kudo queda publicado (`VISIBLE`) de inmediato. | RN-146 a RN-150, CU-001, D-19, D-20 |
| RF-047 | El sistema debe permitir consultar un Kudo con autor, destinatario, fecha, categoría, contenido completo, conversación y calificaciones. Cada consulta incrementa su contador de visualizaciones. | RN-154, RN-155, RN-229, CU-002, D-25 |
| RF-048 | El sistema debe permitir consultar el historial de Kudos de una persona (emitidos y recibidos). | CU-003 |
| RF-049 | El sistema debe permitir consultar los Kudos recibidos por una persona. | CU-003 |
| RF-050 | El sistema debe permitir consultar los Kudos emitidos por una persona. | CU-003 |
| RF-051 | El sistema debe asociar cada Kudo a exactamente una categoría y conservarla aunque esta se desactive o modifique después. | RN-149, RN-145, INV-017 |
| RF-052 | El sistema debe almacenar y mostrar el contenido de Kudos y réplicas **sin límite de longitud y sin truncarlo**. | RN-152, RN-153, RN-170, INV-024 |
| RF-090 🆕 | El sistema debe ofrecer un muro con los Kudos visibles, del más reciente al más antiguo, paginado y filtrable por categoría y por persona. | RN-155, RNF-010 |

### 1.9 Gestión de réplicas

| Código | Requisito refinado | Reglas / decisiones |
|---|---|---|
| RF-053 | El sistema debe permitir a cualquier usuario activo crear réplicas sobre un Kudo no vetado. | RN-156, RN-161, CU-004 |
| RF-054 | El sistema debe permitir crear réplicas sobre otras réplicas no vetadas del mismo Kudo. | RN-162, CU-005 |
| RF-055 | El sistema debe permitir consultar la conversación completa de un Kudo como árbol. Las réplicas vetadas se muestran como "Contenido vetado" y conservan sus respuestas. | CU-006, D-21, D-22 |
| RF-056 | El sistema debe soportar conversaciones de profundidad y tamaño ilimitados, sin límite de réplicas por persona. | RN-163 a RN-167 |

### 1.10 Gestión de calificaciones

| Código | Requisito refinado | Reglas / decisiones |
|---|---|---|
| RF-057 | El sistema debe permitir calificar Kudos no vetados que no sean propios. | RN-157, RN-175, CU-007, D-23 |
| RF-058 | El sistema debe permitir calificar réplicas no vetadas que no sean propias. | RN-171, RN-176, CU-008 |
| RF-059 | El sistema debe usar una escala entera de 1 a 5 estrellas. | RN-177, INV-030 |
| RF-060 | El sistema debe impedir dos calificaciones activas de un mismo usuario sobre el mismo contenido. Si el usuario vuelve a calificar, se modifica su calificación existente. | RN-178 a RN-181, INV-031, INV-032 |
| RF-091 🆕 | El sistema debe permitir modificar una calificación propia y registrar el cambio con el valor anterior y el nuevo. | RN-237, D-23 |
| RF-092 🆕 | El sistema debe recalcular el promedio y la cantidad de calificaciones del contenido cada vez que se registra o modifica una calificación. | RN-185, RN-238 |

### 1.11 Gestión de reportes

| Código | Requisito refinado | Reglas / decisiones |
|---|---|---|
| RF-061 | El sistema debe permitir a cualquier usuario activo reportar un Kudo no vetado que no sea propio. Si el Kudo estaba `VISIBLE`, pasa a `REPORTADO`. | RN-158, RN-186, RN-202, CU-010 |
| RF-062 | El sistema debe permitir reportar réplicas en las mismas condiciones. | RN-172, RN-187, RN-207, CU-011 |
| RF-063 | El sistema debe almacenar en el reporte el reportante, la fecha y un motivo del catálogo. Si el motivo es "Otro", la descripción es obligatoria. | RN-188 a RN-190, D-24 |
| RF-064 | El sistema debe permitir al Rector y al Coordinador Académico consultar los reportes, filtrando por estado. Cada usuario puede consultar los reportes que hizo. | CU-012, D-08 |

### 1.12 Gestión de moderación

| Código | Requisito refinado | Reglas / decisiones |
|---|---|---|
| RF-065 | El sistema debe permitir al Rector o al Coordinador Académico vetar un Kudo con motivo obligatorio. El veto acepta los reportes abiertos y cierra la conversación. | RN-159, RN-191, RN-193, RN-194, CU-013, D-21 |
| RF-066 | El sistema debe permitir al Rector o al Coordinador Académico vetar una réplica con motivo obligatorio. | RN-173, RN-192, RN-195, RN-196, CU-014 |
| RF-067 | El sistema debe rechazar cualquier intento de veto o revisión hecho por un rol distinto de Rector o Coordinador Académico. | RN-197 a RN-200, RN-243 a RN-248, INV-033 |
| RF-068 | El sistema debe conservar el historial de moderación (moderaciones, decisiones, motivos y reportes resueltos) y permitir consultar el histórico de vetos. | RN-249, RN-250, CU-015, INV-035 |
| RF-093 🆕 | El sistema debe permitir a un moderador iniciar la revisión de un contenido reportado. El contenido y sus reportes pendientes pasan a `EN_REVISION`. | RN-203, RN-208, RN-211, RN-239 |
| RF-094 🆕 | El sistema debe permitir al moderador liberar un contenido en revisión. El contenido vuelve a `VISIBLE` y sus reportes quedan `RECHAZADO`. | RN-212, RN-240, RN-242 |

### 1.13 Gestión de auditoría, notificaciones e indicadores

| Código | Requisito refinado | Reglas / decisiones |
|---|---|---|
| RF-069 | El sistema debe registrar en auditoría todos los eventos relevantes del dominio con fecha, actor, acción, objeto, estado anterior y nuevo, y motivo. | RN-096 a RN-100, RN-160, POL-006, D-26 |
| RF-070 | El sistema debe permitir consultar la auditoría filtrando por fecha, actor, acción y objeto, según el ámbito del usuario. | RN-116, RN-128 |
| RF-071 | El sistema debe conservar la trazabilidad histórica: no hay borrado físico de información de negocio. | POL-007, D-28 |
| RF-095 🆕 | El sistema debe generar notificaciones dentro de la aplicación para los usuarios afectados por un evento, y permitir consultarlas y marcarlas como leídas. | D-27 |
| RF-096 🆕 ⛔ | El sistema debe ofrecer indicadores: Kudos por categoría, por Facultad y por Programa; personas más reconocidas; Kudos mejor calificados; y participación (Kudos, réplicas y calificaciones por periodo). | RN-185, doc. 01 §5 "medir participación" |
| RF-097 🆕 ⛔ | El sistema debe permitir a los Administradores consultar y modificar los parámetros globales. | RN-130, RN-131, D-11 |

### 1.14 Consultas organizacionales

| Código | Requisito refinado | Reglas / decisiones |
|---|---|---|
| RF-072 | Debe ser posible conocer qué Programas estudia un estudiante. | RN-054 |
| RF-073 | Debe ser posible conocer en qué Programas enseña un docente. | RN-062 |
| RF-074 | Debe ser posible conocer qué Programa acompaña un Coordinador de Programa. | INV-009 |
| RF-075 | Debe ser posible conocer qué Facultad acompaña un Coordinador de Facultad. | INV-010 |
| RF-076 | Debe ser posible conocer quién es el Rector vigente. | INV-001 |
| RF-077 | Debe ser posible conocer quién es el Coordinador Académico vigente. | INV-002 |

---

## 2. Requisitos no funcionales

Cada requisito se refina con un **criterio verificable**: cómo se demuestra que se cumple.

### 2.1 Seguridad

| Código | Requisito | Criterio verificable |
|---|---|---|
| RNF-001 | Autenticación | Ingreso con Google (OAuth2 / OpenID Connect) mediante Spring Security, aprobado por el docente. La sesión se maneja con la cookie `JSESSIONID` `HttpOnly` y `SameSite=Lax` y expira tras 30 minutos de inactividad. Todos los endpoints `/api/**` responden 401 sin sesión. |
| RNF-002 | Autorización basada en roles | Cada operación verifica el rol vigente y el **ámbito**: Facultad o Programa propios. Hay pruebas que demuestran el rechazo por rol y por ámbito. |
| RNF-003 | Protección de información sensible | No hay contraseñas: la autenticación la hace Google. Los códigos de verificación se generan con `SecureRandom`, vencen a los 15 minutos y admiten 5 intentos. Como el envío es simulado (D-10), el código se muestra en un aviso de demostración. Las credenciales de la base de datos y de Google van en archivos fuera del control de versiones, como en Sigma. |
| RNF-004 | Impedir accesos no autorizados | Las cuentas no `ACTIVA` no inician sesión. Los intentos rechazados responden 403 con un mensaje claro y quedan registrados en el log. |

### 2.2 Disponibilidad y manejo de errores

| Código | Requisito | Criterio verificable |
|---|---|---|
| RNF-005 | Disponibilidad para usuarios autorizados | La aplicación arranca con un solo comando por componente (manual de puesta a punto) y valida la conexión a la base de datos al iniciar. |
| RNF-006 | Errores controlados | Toda excepción se traduce en una respuesta uniforme con mensaje para el usuario, sin detalles técnicos, y `correlationId`, igual que `Respuesta` en Sigma. Hay jerarquía de excepciones por capa y catálogo de mensajes. Errores de negocio → 400, sin permiso → 403, no encontrado → 404, inesperado → 500. |

### 2.3 Mantenibilidad

| Código | Requisito | Criterio verificable |
|---|---|---|
| RNF-007 | Mantenible | Arquitectura por capas con dependencias en un solo sentido. Las clases tienen una responsabilidad, no hay métodos de más de 30 líneas y no hay duplicación marcada por el análisis estático. |
| RNF-008 | Incorporar nuevas funcionalidades | Agregar un suscriptor de eventos, un estado de contenido o una regla no requiere modificar las clases existentes (OCP). Se demuestra en la sustentación. |
| RNF-009 | Minimizar dependencias | Solo se usan las dependencias del stack del curso: Spring Boot web y security, JDBC, HikariCP y el driver de SQL Server. El dominio no depende de Spring. |

### 2.4 Escalabilidad

| Código | Requisito | Criterio verificable |
|---|---|---|
| RNF-010 | Crecimiento del volumen de información | Los listados (muro, conversaciones, auditoría, reportes) son **paginados**, con 20 elementos por defecto. Hay índices en las llaves foráneas y en los filtros frecuentes. El contenido sin límite se guarda en `nvarchar(max)`. |
| RNF-011 | Crecimiento del número de usuarios | El back-end no guarda estado propio entre peticiones, salvo la sesión. Usa pool de conexiones HikariCP. |

### 2.5 Observabilidad y auditabilidad

| Código | Requisito | Criterio verificable |
|---|---|---|
| RNF-012 | Trazabilidad de operaciones relevantes | Cada evento de dominio genera un registro de `Auditoria` (D-26). |
| RNF-013 | Errores registrables | Logs con SLF4J: capa, mensaje técnico y excepción raíz, como en `AbstractRestController` de Sigma. |
| RNF-014 | Acciones administrativas auditables | Aprobaciones, rechazos, suspensiones, cambios de coordinador, categorías y parámetros generan auditoría. |
| RNF-015 | Todo cambio relevante registrado | Auditoría con estado anterior y nuevo. |
| RNF-016 | Decisiones de moderación registradas | `Moderacion` + auditoría con motivo y reportes resueltos (RN-250). |
| RNF-017 | Aprobaciones registradas | `SolicitudRegistro` decidida + auditoría. |

### 2.6 Calidad del software

| Código | Requisito | Criterio verificable |
|---|---|---|
| RNF-018 | Clean Code | Nombres expresivos en español del dominio, métodos cortos y sin comentarios compensatorios. Se verifica con el análisis estático. |
| RNF-019 | SOLID | Cada principio se justifica con ejemplos concretos en [Arquitectura](../05-arquitectura/README.md). |
| RNF-020 | Bajo acoplamiento | Las capas dependen de interfaces: fachada, negocio, DAO (`DAOFactory`), ensambladores y `EnviadorNotificacion`. |
| RNF-021 | Alta cohesión | Cada objeto de dominio protege sus propias reglas (Tell, Don't Ask). |
| RNF-022 | Separación de responsabilidades | Controlador = HTTP; fachada = transacción y conversión; negocio = reglas; DAO = SQL; dominio = comportamiento. |

### 2.7 Pruebas y análisis estático

| Código | Requisito | Criterio verificable |
|---|---|---|
| RNF-023 | Verificable mediante pruebas | Pruebas unitarias JUnit 5 del dominio y las reglas, pruebas de la fachada con DAO falsos y colección Bruno de la API. En el front, Vitest para stores y validaciones. |
| RNF-024 | Diseño testeable | El negocio recibe su `DAOFactory` por constructor, así las pruebas usan una fábrica falsa en memoria y no necesitan base de datos. Meta: **≥ 80 % de cobertura**, medida con JaCoCo (resultado real en [Calidad y pruebas](../07-calidad-y-pruebas/README.md)). |
| RNF-025 | Análisis estático de ambos repositorios | Back: SonarQube Community (o SonarLint) + JaCoCo. Front: ESLint + oxlint, como en Sigma. |
| RNF-026 | Evidencias de ejecución | Capturas o reportes exportados en `07-calidad-y-pruebas/evidencias/`. |
| RNF-027 | Hallazgos y correcciones documentados | Tabla hallazgo → causa → corrección → antes/después en [`07-calidad-y-pruebas`](../07-calidad-y-pruebas/README.md). |

### 2.8 Documentación, arquitectura y trazabilidad académica

| Código | Requisito | Criterio verificable |
|---|---|---|
| RNF-028 | Documentación en Markdown | Todo `documentacion/` en `.md`. Los diagramas van en Mermaid dentro del `.md`, con su fuente editable `.drawio`. |
| RNF-029 | Documentación técnica | Arquitectura, modelo de datos, API (endpoints) y decisiones. |
| RNF-030 | Manual de puesta a punto | Requisitos previos, instalación, configuración, variables de entorno, ejecución, pruebas y despliegue (MAN-001 a MAN-007). |
| RNF-031 | Estilo arquitectónico del curso | Capas `controlador → fachada → negocio → dao` (D-29). |
| RNF-032 | Decisiones justificadas | [Decisiones de diseño](decisiones-de-diseno.md) y sección de arquitectura. |
| RNF-033 | Coherencia con la orientación a objetos | Dominio rico, polimorfismo con el patrón State (`EstadoContenido`) y composición sobre herencia. |
| RNF-034 | Análisis relacionado con diseño e implementación | Los códigos RN, D, CU y RF aparecen en clases, reglas y pruebas. |
| RNF-035 | Trazabilidad requisitos-diseño-solución | [Matriz de trazabilidad](matriz-trazabilidad.md). |

### 2.9 Complementados

| Código | Requisito | Criterio verificable |
|---|---|---|
| RNF-036 🆕 | Usabilidad: el usuario comprende el estado del sistema | Interfaz en español. Muestra con insignias el estado de cuentas, contenidos, reportes y solicitudes. Cada operación responde con un mensaje claro de éxito o error (ARQ-065). |
| RNF-037 🆕 | Navegación de conversaciones | El árbol de réplicas se muestra con sangría por nivel, se puede contraer y expandir, y hay una acción "responder" en cada nodo (ARQ-066). |
| RNF-038 🆕 | Kudos extensos | En el muro los Kudos largos se muestran recortados **visualmente** con "Ver más". El contenido completo siempre está disponible y nunca se trunca en los datos (ARQ-067, RN-153). |
| RNF-039 🆕 | Compatibilidad | Funciona en las versiones actuales de Chrome, Edge y Firefox, con diseño adaptable desde 360 px de ancho. |
| RNF-040 🆕 | Integridad de datos | Las restricciones de negocio críticas se refuerzan en la base de datos: `UNIQUE`, índices únicos filtrados, `CHECK` y llaves foráneas (ARQ-063, ARQ-064). |
