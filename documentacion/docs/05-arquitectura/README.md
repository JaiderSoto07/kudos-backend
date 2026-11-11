# Arquitectura

[Inicio](../../README.md) · [Patrones utilizados](patrones.md) · [API REST](api-rest.md) · [Diagramas de comportamiento](../06-diagramas-comportamiento/README.md) · [Decisiones de diseño](../00-analisis/decisiones-de-diseno.md)

Este documento cubre la sección 12 (Arquitectura) del contenido mínimo; la sección 13 (Patrones) está en [patrones.md](patrones.md) y la lista completa de endpoints en [api-rest.md](api-rest.md). Responde a los requisitos ARQ-001 a ARQ-080 y RNF-031 a RNF-033. Todo lo que se describe aquí **existe en el código** entregado.

---

## 1. Estilo arquitectónico: capas del curso con dominio enriquecido

La solución usa el **estilo por capas visto en el curso**, el mismo del proyecto de referencia (Sigma — Pizzería UCO): `controlador → fachada → negocio → dao`. Lo acompañan los paquetes `dominio`, `dto`, `entidad`, los ensambladores (*assemblers*) y `transversal` (ARQ-001, D-29).

Hay **una diferencia deliberada** con Sigma. En Sigma los objetos de `dominio` solo tenían Builder y getters, es decir, eran anémicos. Aquí los objetos de dominio **tienen comportamiento**: `kudo.vetar()`, `usuario.intentarActivar()`, `reporte.aceptar(idModeracion)`, `asignacion.puedeDecidir(solicitud)`. El enunciado lo exige (ARQ-008, ARQ-047, ARQ-049: "no se espera una solución basada exclusivamente en getters y setters").

[![Arquitectura por capas](../../assets/diagramas/arquitectura-capas.svg)](../../assets/diagramas/arquitectura-capas.svg)

> También disponible en [PNG](../../assets/diagramas/arquitectura-capas.png). Fuente editable: [`fuentes/arquitectura-capas.drawio`](../../fuentes/arquitectura-capas.drawio).

### ¿Por qué este estilo? (ARQ-002)

| Criterio | Cómo lo cumple |
|---|---|
| Separación de responsabilidades (ARQ-003, ARQ-041) | Cada capa tiene una sola razón para cambiar: HTTP, transacción, caso de uso y reglas, comportamiento del dominio o SQL. |
| Bajo acoplamiento (ARQ-004, ARQ-016) | Cada capa conoce a la siguiente a través de **interfaces**: `KudoFachada`, `KudoNegocio`, `KudoDAO`, `DAOFactory`. |
| Evolución (ARQ-005, ARQ-077) | Cambiar SQL Server por otro motor solo exige otra implementación de `DAOFactory`; pasar del envío simulado de códigos a uno real, otra implementación de `EnviadorNotificacion`. |
| Testabilidad (ARQ-068) | El negocio recibe su `DAOFactory` por constructor, así las pruebas usan `DAOFactoryFalsa` (DAO en memoria) sin base de datos. |
| Conocido por el equipo y el docente | Es el estilo del curso y de Sigma, así que se puede explicar y modificar en vivo (SUS-009, SUS-011). |

---

## 2. Capas y responsabilidades

| Capa / paquete | Responsabilidad | Qué **no** hace | Depende de |
|---|---|---|---|
| `configuracion` | `SeguridadConfiguracion` (Spring Security): ingreso con Google (OAuth2), sesión, 401 para `/api/**` sin sesión, salida. | No decide permisos por rol: eso es del negocio. | Spring Security |
| `controlador` | `XxxControlador` recibe la petición, convierte JSON ↔ DTO, obtiene el actor de la sesión (`obtenerIdActor`) y devuelve la respuesta uniforme. `AbstractRestController` (padre de todos) traduce las excepciones a códigos HTTP. | No valida reglas de negocio ni abre transacciones. | `negocio.fachada` (interfaces), `dto` |
| `negocio.fachada` | `XxxFachadaImpl` es el punto de entrada de cada caso de uso: obtiene la `DAOFactory`, **abre, confirma o cancela la transacción** y convierte DTO ↔ dominio con los `XxxDtoAssembler`. Traduce errores inesperados a `KudosFachadaExcepcion`. | No contiene reglas. | `negocio.negocio`, `dto` |
| `negocio.negocio` | `XxxNegocioImpl` orquesta el caso de uso: ejecuta las **reglas** (`XxxRule`), pide al dominio que actúe, guarda con los DAO (convirtiendo dominio ↔ entidad con los `XxxEntidadAssembler`) y registra la **auditoría** y las **notificaciones** en la misma transacción. | No conoce HTTP ni SQL. | `dominio`, `dao` (interfaces), `negocio.enviador` |
| `negocio.negocio.reglas` | Una clase por regla (`AsegurarDestinatarioDistintoRule`, `ValidarEstrellasRule`…), singleton con `obtenerInstancia()` y método `ejecutar(...)`. 64 reglas en 12 carpetas por módulo. | No guarda nada. | `dominio`, `dao` (solo las que consultan) |
| `dominio` | El modelo enriquecido: `XxxDominio` con Builder y comportamiento, enumeraciones y `EstadoContenido` (patrón State). Protege sus invariantes con `KudosDominioExcepcion`. | No conoce Spring, JDBC ni HTTP: es Java puro. | `transversal` |
| `dao` | Interfaces genéricas (`CrearDAO`, `ConsultarDAO`, `ActualizarDAO`, `ConsultarActivosDAO`), una interfaz por entidad (`KudoDAO`…) y la fábrica abstracta `DAOFactory`. Implementación `SqlServerDAOFactory` + `XxxSqlServerDAO` con JDBC y HikariCP. | No decide reglas. | `entidad`, `transversal` |
| `dto` / `entidad` | Estructuras de datos de la API y de la tabla. Las entidades tienen Builder; los DTO, setters que limpian los valores. | — | `transversal` |
| `transversal` | Jerarquía de excepciones por capa, `CatalogoMensajes` y utilitarios (`UtilTexto`, `UtilId`, `UtilFecha`, `UtilObjeto`, `UtilEnum`, `UtilNumero`, `UtilSql`), como en Sigma. | — | — |

**Regla de dependencias**: las flechas van siempre hacia abajo. El `dominio` no depende de ninguna capa técnica (ARQ-042), así que se prueba con JUnit puro.

### Paquetes del back-end (reales)

```
co.edu.uco.kudos
├── KudosApplication.java
├── configuracion/                 ← SeguridadConfiguracion
├── controlador/                   ← AbstractRestController
│   ├── impl/                      ← 17 controladores: KudoControlador, ModeracionControlador, AuditoriaControlador…
│   └── respuesta/                 ← Respuesta, RespuestaOk, RespuestaError, EstadoRespuesta
├── negocio/
│   ├── fachada/                   ← interfaces XxxFachada
│   │   ├── impl/                  ← XxxFachadaImpl (transacción)
│   │   └── assembler/             ← DTOAssembler + impl/ (KudoDtoAssembler, ConversacionDtoAssembler…)
│   └── negocio/                   ← interfaces XxxNegocio
│       ├── impl/                  ← XxxNegocioImpl (casos de uso)
│       ├── assembler/             ← EntidadAssembler + impl/
│       ├── reglas/                ← Rule + impl/<módulo>/ (kudo/, moderacion/, organizacion/…)
│       └── enviador/              ← EnviadorNotificacion + impl/EnviadorNotificacionSimulado
├── dominio/                       ← UsuarioDominio, KudoDominio, ReplicaDominio… + enumeraciones (EstadoContenido, Rol…)
├── dto/                           ← KudoDto, ReplicaDto, ReporteDto, AuditoriaDto…
├── entidad/                       ← KudoEntidad, UsuarioEntidad…
├── dao/
│   ├── datos/                     ← CrearDAO, ConsultarDAO, ActualizarDAO, ConsultarActivosDAO
│   │   └── entidad/               ← SqlDAO (base de los DAO SQL), KudoDAO, UsuarioDAO… + sqlserver/ (KudoSqlServerDAO…)
│   └── factoria/                  ← DAOFactory, enums/FactoriaEnum, impl/SqlServerDAOFactory, ConexionSqlServer
└── transversal/                   ← excepciones/, catalogo/CatalogoMensajes, utilitario/
```

La correspondencia entre cada objeto del dominio y sus clases (dominio, entidad, DTO, DAO y negocio) está en el [modelo de dominio](../01-modelo-dominio/README.md#correspondencia-con-el-código).

---

## 3. Recorrido de una petición

Ejemplo: **crear un Kudo** (CU-001). El [diagrama de secuencia](../06-diagramas-comportamiento/README.md#crear-kudo) lo muestra completo.

1. El front-end envía `POST /api/v1/kudos` con destinatario, categoría y contenido. Spring Security verifica la cookie de sesión: sin sesión responde 401.
2. `KudoControlador` recibe el `KudoDto`, obtiene el id del actor con `obtenerIdActor(...)` (exige cuenta `ACTIVA`) y llama `fachada.crear(idActor, dto)`.
3. `KudoFachadaImpl` obtiene la `DAOFactory`, **inicia la transacción** y convierte el DTO a dominio con `KudoDtoAssembler`.
4. `KudoNegocioImpl` ejecuta las reglas: `AsegurarDestinatarioDistintoRule`, `AsegurarDestinatarioActivoRule`, `AsegurarCategoriaDisponibleRule` y `ValidarContenidoRule`. Construye el `KudoDominio` en CREADO y llama `kudo.publicar()`: el patrón State lo pasa a VISIBLE.
5. El negocio convierte el dominio a entidad con `KudoEntidadAssembler` y lo guarda con `KudoDAO.crear()`. Luego crea la notificación `KUDO_RECIBIDO` y registra la auditoría `KudoPublicado`.
6. La fachada **confirma la transacción**. Si algo falla, la cancela y no queda nada a medias. En el `finally` cierra la conexión.
7. El controlador responde `200` con `RespuestaOk` ("Tu Kudo quedó publicado en el muro.").

---

## 4. Transacciones

- **La fachada define el límite transaccional** de cada caso de uso, igual que en Sigma: `iniciarTransaccion()`, `confirmarTransaccion()` o `cancelarTransaccion()`, y `cerrarConexion()` en el `finally`.
- Cuando un caso de uso toca varios objetos (aprobar una solicitud, vetar un contenido), su `XxxNegocioImpl` los coordina dentro de la misma transacción. Ver la [tabla de agregados](../01-modelo-dominio/README.md#agregados).
- La **auditoría y las notificaciones** se registran **dentro** de la transacción (D-26): si algo falla, se cancela todo. Así nunca existe una operación sin su registro (RN-096 a RN-100), ni un registro de algo que no pasó.
- En `ModeracionFachadaImpl` y `NotificacionFachadaImpl` el bloque de transacción está escrito una sola vez en `ejecutarEnTransaccion(Consumer<XxxNegocio>)`, al que cada método le pasa su operación.
- Las consultas no abren transacción: solo abren y cierran la conexión.

## 5. Manejo de errores (RNF-006, ARQ-076)

| Elemento | Descripción |
|---|---|
| `KudosExcepcion` | Clase padre con capa, mensaje para el usuario, mensaje técnico y excepción raíz, igual que `PizzeriaExcepcion` en Sigma. |
| Una excepción por capa | `KudosControladorExcepcion`, `KudosFachadaExcepcion`, `KudosNegocioExcepcion`, `KudosDominioExcepcion`, `KudosDatosExcepcion`, `KudosDtoExcepcion`, `KudosEntidadExcepcion`, `KudosTransversalExcepcion` y `KudosAutorizacionExcepcion` (sin permiso por rol o ámbito). |
| `CatalogoMensajes` | Mensajes para el usuario agrupados por módulo. Un buen mensaje dice qué falló y qué hacer. |
| `AbstractRestController` | Regla incumplida → **400**. Sin permiso por rol o ámbito → **403**. Recurso inexistente → **404**. Error inesperado → **500** con un mensaje genérico. El detalle técnico va al log (SLF4J), nunca a la respuesta. |
| `Respuesta` | Forma uniforme de todas las respuestas: `correlationId`, `timestamp`, `datos`, `mensajes` y `estado` (`RespuestaOk` / `RespuestaError`). |

## 6. Seguridad (RNF-001 a RNF-004, D-09)

- **Autenticación con Google** (OAuth2 / OpenID Connect), aprobada por el docente. Spring Security hace el ingreso en `/oauth2/authorization/google`; Google confirma el correo. **No se guardan contraseñas.**
- **La cuenta y los permisos están en nuestra base de datos**: al ingresar, `GET /api/v1/autenticacion` busca el correo. Si no existe, el front muestra el registro; si existe sin estar `ACTIVA`, muestra lo que falta (aprobación, verificación del celular). Cada operación vuelve a exigir cuenta `ACTIVA` con `obtenerIdActor(...)`.
- **Sesión** con la cookie `JSESSIONID`, `HttpOnly` y `SameSite=Lax`, que expira tras 30 minutos de inactividad. Sin sesión, todo `/api/**` responde 401. La salida es `POST /api/v1/autenticacion/salir`.
- **Autorización en dos niveles**, en el back-end:
  1. **Por rol**: `AsegurarActorTieneRolRule` con la lista de roles de cada operación (por ejemplo `ROLES_MODERADORES`); si falla, 403.
  2. **Por ámbito**: la `AsignacionRol` vigente responde `puedeDecidir`, `puedeVerFacultad`, `puedeVerPrograma` y `coordinaPrograma` (POL-004, RN-112 a RN-131); la auditoría filtra por ámbito (CU-038).
- La autorización **no** se confía al front-end: el menú y las rutas ocultan opciones por comodidad, pero el back-end vuelve a validar todo.
- `csrf` está deshabilitado igual que en Sigma: la API solo recibe JSON, no habilita CORS y la cookie es `SameSite=Lax`. Sonar lo muestra como *Security Hotspot* revisado.
- Las credenciales de la base de datos (`basedatos.properties`) y de Google (`oauth.properties`) están fuera del control de versiones, como en Sigma.
- El front-end corre en `localhost:5173` y llama al back-end mediante el *proxy* de Vite, así que no hace falta abrir CORS.

## 7. Auditoría y notificaciones (D-26, D-27)

Cada operación relevante registra un **evento de dominio** con nombre en pasado (`KudoPublicado`, `SolicitudAprobada`, `KudoVetado`, `CalificacionActualizada`…; 35 acciones). Lo hace el negocio con `auditoriaNegocio.registrar(...)`, y si la operación afecta a alguien, `notificacionNegocio.notificar(...)`, en la misma transacción. El catálogo de eventos está en el [modelo de dominio](../01-modelo-dominio/README.md#catálogo-de-eventos-de-dominio).

- **Consulta de la auditoría** (`GET /auditoria`): filtros por fecha, actor, acción y objeto, en páginas de 20 y **según el ámbito**: Rector y Coordinador Académico ven todo; el Administrador, lo técnico y de cuentas; cada coordinador, lo hecho por él y por las personas de su ámbito (RN-116).
- **Notificaciones** (`/notificaciones`): campana con contador en la barra superior; se marcan como leídas una a una o todas; los códigos de verificación no aparecen en la campana.
- Se evaluó el patrón **Observer** (publicador y suscriptores) y se descartó: registrar desde el negocio es más simple y explícito y queda en la misma transacción ([patrones](patrones.md#patrones-evaluados-y-descartados)).

## 8. Persistencia (D-30, ARQ-062 a ARQ-064)

- **JDBC + HikariCP sobre SQL Server**, sin JPA, como en Sigma. Cada `XxxSqlServerDAO` escribe su SQL con `PreparedStatement`, lo que evita la inyección SQL.
- `DAOFactory` es una **fábrica abstracta**: entrega todos los DAO sobre la misma conexión, y por tanto la misma transacción.
- La base de datos **refuerza** las reglas críticas: llaves foráneas, chequeos e **índices únicos filtrados** (un solo rol vigente, una calificación activa por persona y contenido, un reporte abierto por persona y contenido, una moderación en curso por contenido). Ver la [validación de las restricciones](../03-modelo-entidad-relacion/README.md#validación-de-las-restricciones).
- No hay borrado físico (D-28): los DAO no tienen operación de eliminar.
- El muro de Kudos (10 por página) y la auditoría (20 por página) son paginados en la fachada (RNF-010).

---

## 9. API REST

Base `/api/v1`, 64 endpoints, todos con la respuesta uniforme y todos con sesión. La lista completa, con cuerpo, caso de uso y quién puede usarlo, está en **[api-rest.md](api-rest.md)**. Las peticiones de ejemplo se entregan como colección de **Bruno** (`bruno/Kudos`, 16 carpetas), igual que en Sigma.

---

## 10. Front-end (Vue 3)

Misma organización del front-end de Sigma: un módulo por área del negocio y componentes de interfaz reutilizables. Repositorio `kudos-frontend`.

```
src
├── api/                 ← http.js (fetch con la cookie, respuesta uniforme, 401 → sesión vencida),
│                           adaptadores.js (DTO ↔ objetos de la pantalla), consultas.js, config.js
├── components/
│   ├── layout/          ← AppSidebar (menú según el rol, menu.js), AppTopbar (campana y usuario)
│   ├── ui/              ← BaseButton, BaseModal, BaseTable, BasePagination, BaseSelect, StatusBadge…
│   └── icons/           ← AppIcon
├── layouts/             ← MainLayout
├── modules/
│   ├── autenticacion/   ← LoginView, EstadoCuentaView
│   ├── identidad/       ← RegistroView, PerfilView (Mi cuenta), verificación del celular
│   ├── solicitudes/     ← SolicitudesView, RechazoModal
│   ├── autoridades/     ← RegistrarAutoridadView
│   ├── organizacion/    ← FacultadesView, ProgramasView, ProgramaDetalleView, EstructuraView
│   ├── categorias/      ← CategoriasView
│   ├── kudos/           ← MuroView (inicio), KudoDetalleView, HistorialKudosView, KudoCard, CrearKudoModal,
│   │                       ConversacionArbol, ReplicaNodo (recursivo), CalificacionEstrellas, ValoracionesResumen
│   ├── moderacion/      ← ModeracionView (bandeja e histórico), MisReportesView, ReportarModal, DecisionModal
│   ├── auditoria/       ← AuditoriaView
│   └── notificaciones/  ← CampanaNotificaciones + notificacionStore
│   (cada módulo con views/, components/, services/ y, si los necesita, stores/ y utils/)
├── router/              ← rutas con meta.roles y guardas: requiere sesión, cuenta activa y rol permitido
├── shared/domain/       ← roles, estados y listas de roles por función (ROLES_MODERADORES…)
└── stores/              ← sesionStore (usuario actual, rol, tieneRol)
```

| Requisito de interfaz | Cómo se cumple |
|---|---|
| Comprender el estado del sistema (ARQ-065, RNF-036) | Pantalla de estado de la cuenta, insignias de estado, avisos de éxito y error con el mensaje del back-end, campana de notificaciones. |
| Navegar conversaciones (ARQ-066, RNF-037) | `ReplicaNodo` es un componente **recursivo**: sangría hasta el nivel 5 (después muestra "en respuesta a *Nombre*"), contraer y expandir, y responder, calificar o reportar en cada nodo. |
| Kudos extensos (ARQ-067, RNF-038) | `KudoCard` recorta el texto **solo visualmente** y ofrece "Ver más". El detalle muestra el contenido completo. |
| Menú según rol | `menuSegunRol` muestra las opciones del rol vigente; las rutas tienen `meta.roles`. El back-end vuelve a validar cada operación. |
| Pruebas (RNF-023) | Vitest con un servidor falso que imita la API (`src/__tests__/servidorFalso.js`). |

---

## 11. Principios de diseño aplicados

### SOLID (RNF-019, ARQ-022 a ARQ-030)

| Principio | Dónde se ve |
|---|---|
| **S** — Responsabilidad única | Cada capa tiene una razón de cambio. Cada regla es una clase (`ValidarEstrellasRule`). La fachada solo maneja la transacción; el negocio, el caso de uso. |
| **O** — Abierto/cerrado | Una nueva regla es una nueva clase `Rule` que el negocio llama, sin cambiar las demás. Un nuevo estado del contenido se agrega en `EstadoContenido` sin tocar `KudoDominio`. Un nuevo motor de base de datos es una nueva `DAOFactory`. |
| **L** — Sustitución de Liskov | `SqlServerDAOFactory` y `DAOFactoryFalsa` (pruebas) son intercambiables donde se espera una `DAOFactory`. `EnviadorNotificacionSimulado` sustituye a cualquier `EnviadorNotificacion`. |
| **I** — Segregación de interfaces | Los DAO combinan interfaces pequeñas (`CrearDAO`, `ConsultarDAO`, `ActualizarDAO`) según lo que necesita cada entidad: `AuditoriaDAO` no tiene `actualizar`, porque la auditoría no se modifica. |
| **D** — Inversión de dependencias | El negocio depende de `DAOFactory`, `XxxDAO` y `EnviadorNotificacion` (abstracciones), no de JDBC ni de un proveedor de SMS. |

### Otros principios (ARQ-031 a ARQ-057)

| Principio | Aplicación |
|---|---|
| **Tell, Don't Ask** (ARQ-043) | Se le dice al objeto qué hacer (`kudo.vetar()`) en vez de preguntarle su estado y decidir afuera (`if (kudo.getEstado() != VETADO) kudo.setEstado(VETADO)`). |
| **Experto en información** | La `AsignacionRol` responde si una autoridad puede decidir una solicitud (`puedeDecidir`), porque es quien conoce el rol y el ámbito. |
| **Ley de Demeter** (ARQ-045) | `kudo.admiteInteraccion()` en lugar de preguntar por el estado desde afuera. |
| **Composición sobre herencia** (ARQ-014, ARQ-015) | Los roles son `AsignacionRol` (composición), no subclases de `Usuario`. Ver la [justificación](../01-modelo-dominio/README.md#por-qué-rector-docente-estudiante-no-son-clases). |
| **DRY** (ARQ-037) | Una sola regla de permisos (`AsegurarActorTieneRolRule`) para todos los módulos; la normalización de búsquedas en `UtilTexto.normalizarParaBuscar`; la autoría de las operaciones en la auditoría (D-31). |
| **KISS** (ARQ-039) | La exclusividad de roles se garantiza con **un solo** rol vigente. Se evitaron patrones sin un problema concreto ([patrones descartados](patrones.md#patrones-evaluados-y-descartados)). |
| **Lenguaje del dominio** (ARQ-055 a ARQ-057) | Clases y métodos con los nombres del negocio, en español: `KudoDominio`, `ReplicaDominio`, `vetar`, `aprobar`, `desvincular`. |
| **Clean Code** (ARQ-031 a ARQ-036) | Nombres expresivos, métodos cortos, sin comentarios en el código, constantes con nombre y mensajes en `CatalogoMensajes`. |

---

## 12. Resumen de decisiones arquitectónicas

| Decisión | Alternativa descartada | Motivo | Referencia |
|---|---|---|---|
| Capas del curso con dominio enriquecido | Dominio anémico como Sigma | El enunciado exige objetos ricos | D-29 |
| JDBC + HikariCP | JPA / Hibernate | Estilo del curso; control explícito del SQL y de la transacción | D-30 |
| Transacción en la fachada | Transacción en el controlador o en cada DAO | Un caso de uso es una unidad de trabajo | §4 |
| Ingreso con Google + cuenta y permisos en nuestra base | Usuario y contraseña propios | No guardar contraseñas; aprobado por el docente; las reglas de la cuenta siguen en nuestro dominio | D-09 |
| Auditoría registrada desde el negocio | Observer (publicador y suscriptores) | Más simple y explícito; misma transacción | D-26 |
| Envío de códigos simulado detrás de una interfaz | Integrar un proveedor real de SMS | Fuera del alcance; se cambia sin tocar el dominio | D-10 |
| Reglas reforzadas en la base de datos | Validar solo en Java | Defensa en profundidad e integridad ante concurrencia | D-14, RNF-040 |
