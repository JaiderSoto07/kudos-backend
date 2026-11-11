# Diagramas de comportamiento

[Inicio](../../README.md) · [Arquitectura](../05-arquitectura/README.md) · [Casos de uso](../04-diagrama-casos-uso/README.md) · [Modelo de dominio](../01-modelo-dominio/README.md)

Diagramas UML que muestran **cómo se comporta** el sistema: los ciclos de vida de los objetos (UML-005 estado), los procesos de negocio (UML-004 actividad) y la colaboración entre capas y objetos en los casos de uso principales (UML-003 secuencia).

> Fuentes editables en draw.io: [`diagramas-estado.drawio`](../../fuentes/diagramas-estado.drawio), [`diagramas-actividad.drawio`](../../fuentes/diagramas-actividad.drawio) y [`diagramas-secuencia.drawio`](../../fuentes/diagramas-secuencia.drawio), con una página por diagrama.

## Contenido

- **Diagramas de estado**: Estado de Kudo y Réplica, Estado de la cuenta, Estado del reporte, Estado de la solicitud de registro, Estado de la Facultad, Estado de la moderación
- **Diagramas de actividad**: Registro y activación de una cuenta, Moderación de contenido, Publicar un Kudo y conversar
- **Diagramas de secuencia**: Crear Kudo, Aprobar solicitud de registro, Vetar contenido, Verificar celular y activar la cuenta, Ingresar con Google

## 1. Diagramas de estado (UML-005)

### Estado de Kudo y Réplica

Ciclo de vida del contenido (RN-201 a RN-209, D-20). Se implementa con el patrón State: la enumeración EstadoContenido decide cada transición y KudoDominio / ReplicaDominio solo delegan. Un reporte que llega durante la revisión no cambia el estado.

[![Estado de Kudo y Réplica](../../assets/diagramas/diagramas-estado-1.svg)](../../assets/diagramas/diagramas-estado-1.svg)

> También disponible en [PNG](../../assets/diagramas/diagramas-estado-1.png).

<details>
<summary>Versión en texto del diagrama (Mermaid)</summary>

```mermaid
stateDiagram-v2
    [*] --> CREADO
    CREADO --> VISIBLE : publicar()
    VISIBLE --> REPORTADO : reportar() [primer reporte]
    REPORTADO --> REPORTADO : reportar() [otro reporte]
    REPORTADO --> EN_REVISION : iniciarRevision() [moderador]
    EN_REVISION --> VISIBLE : liberar() [moderador + motivo]
    VISIBLE --> VETADO : vetar()
    REPORTADO --> VETADO : vetar()
    EN_REVISION --> VETADO : vetar()
    VETADO --> [*]
```

</details>

### Estado de la cuenta

Ciclo de vida de la cuenta (RN-084 a RN-089, RN-216, RN-217, D-07, D-17), métodos de UsuarioDominio. El autorregistro nace en PENDIENTE_APROBACION; el Rector y los Administradores que registra un Administrador nacen ya aprobados en PENDIENTE_VERIFICACION. La suspensión y la reactivación las hace automáticamente la desvinculación y la nueva vinculación; desactivar existe en el dominio, pero su pantalla queda fuera del alcance.

[![Estado de la cuenta](../../assets/diagramas/diagramas-estado-2.svg)](../../assets/diagramas/diagramas-estado-2.svg)

> También disponible en [PNG](../../assets/diagramas/diagramas-estado-2.png).

<details>
<summary>Versión en texto del diagrama (Mermaid)</summary>

```mermaid
stateDiagram-v2
    [*] --> PENDIENTE_APROBACION : registrarSolicitud()
    PENDIENTE_APROBACION --> PENDIENTE_VERIFICACION : aprobar() [faltan verificaciones]
    PENDIENTE_APROBACION --> ACTIVA : aprobar() [correo y celular verificados]
    PENDIENTE_VERIFICACION --> ACTIVA : verificarCelular() / verificarCorreo() (Google) [aprobada y ambos verificados]
    ACTIVA --> PENDIENTE_VERIFICACION : cambiarCorreo() / cambiarCelular()
    ACTIVA --> SUSPENDIDA : suspender() [sin vínculos vigentes]
    SUSPENDIDA --> ACTIVA : reactivar() [nuevo vínculo]
    ACTIVA --> DESACTIVADA : desactivar()
    SUSPENDIDA --> DESACTIVADA : desactivar()
    PENDIENTE_VERIFICACION --> DESACTIVADA : desactivar()
    DESACTIVADA --> [*]
```

</details>

### Estado del reporte

Ciclo de vida del reporte (RN-210 a RN-212, D-24), métodos de ReporteDominio. Si el contenido ya está en revisión, el reporte nace directamente EN_REVISION.

[![Estado del reporte](../../assets/diagramas/diagramas-estado-3.svg)](../../assets/diagramas/diagramas-estado-3.svg)

> También disponible en [PNG](../../assets/diagramas/diagramas-estado-3.png).

<details>
<summary>Versión en texto del diagrama (Mermaid)</summary>

```mermaid
stateDiagram-v2
    [*] --> PENDIENTE : reportarKudo() / reportarReplica()
    PENDIENTE --> EN_REVISION : pasarARevision(idModeracion) [se inicia la revisión]
    EN_REVISION --> ACEPTADO : aceptar(idModeracion) [contenido vetado]
    EN_REVISION --> RECHAZADO : rechazar(idModeracion) [contenido liberado]
    PENDIENTE --> ACEPTADO : aceptar(idModeracion) [veto directo]
    ACEPTADO --> [*]
    RECHAZADO --> [*]
```

</details>

### Estado de la solicitud de registro

Ciclo de vida de la solicitud (RN-090 a RN-093, RN-108, D-17).

[![Estado de la solicitud de registro](../../assets/diagramas/diagramas-estado-4.svg)](../../assets/diagramas/diagramas-estado-4.svg)

> También disponible en [PNG](../../assets/diagramas/diagramas-estado-4.png).

<details>
<summary>Versión en texto del diagrama (Mermaid)</summary>

```mermaid
stateDiagram-v2
    [*] --> PENDIENTE : registrarSolicitud()
    PENDIENTE --> APROBADA : aprobar(autoridad) [autoridad competente y ≠ solicitante]
    PENDIENTE --> RECHAZADA : rechazar(autoridad, motivo)
    APROBADA --> [*]
    RECHAZADA --> [*]
```

</details>

### Estado de la Facultad

Ciclo de vida de la Facultad (RN-045, D-05).

[![Estado de la Facultad](../../assets/diagramas/diagramas-estado-5.svg)](../../assets/diagramas/diagramas-estado-5.svg)

> También disponible en [PNG](../../assets/diagramas/diagramas-estado-5.png).

<details>
<summary>Versión en texto del diagrama (Mermaid)</summary>

```mermaid
stateDiagram-v2
    [*] --> EN_CONFORMACION : registrar()
    EN_CONFORMACION --> ACTIVA : activar() [se crea el primer programa]
    ACTIVA --> INACTIVA : inactivar() [sin programas activos]
    INACTIVA --> [*]
```

</details>

### Estado de la moderación

Ciclo de vida de la moderación (RN-239, RN-240, RN-250), métodos de ModeracionDominio. ModeracionNegocioImpl la crea al iniciar la revisión; en un veto directo la crea y la cierra en el mismo paso.

[![Estado de la moderación](../../assets/diagramas/diagramas-estado-6.svg)](../../assets/diagramas/diagramas-estado-6.svg)

> También disponible en [PNG](../../assets/diagramas/diagramas-estado-6.png).

<details>
<summary>Versión en texto del diagrama (Mermaid)</summary>

```mermaid
stateDiagram-v2
    [*] --> EN_CURSO : iniciarRevision()
    EN_CURSO --> CERRADA : vetar(motivo) / liberar(motivo)
    CERRADA --> [*]
```

</details>


## 2. Diagramas de actividad (UML-004)

### Registro y activación de una cuenta

Desde que la persona ingresa con Google por primera vez hasta que puede usar la aplicación (CU-016, CU-017, CU-018, CU-022, RN-084 a RN-093).

[![Registro y activación de una cuenta](../../assets/diagramas/diagramas-actividad-1.svg)](../../assets/diagramas/diagramas-actividad-1.svg)

> También disponible en [PNG](../../assets/diagramas/diagramas-actividad-1.png).

<details>
<summary>Versión en texto del diagrama (Mermaid)</summary>

```mermaid
flowchart TD
    i((Inicio))
    a1("Ingresar con Google; el correo no existe: completar nombres, celular, rol y programa o facultad")
    d1{"¿Datos válidos y correo/celular únicos?"}
    a2("Mostrar el error y no registrar")
    a3("Crear cuenta PENDIENTE_APROBACION (correo verificado por Google) y solicitud PENDIENTE; enviar código al celular")
    a4("Notificar a la autoridad aprobadora")
    a5("La persona verifica el celular con el código")
    a6("La autoridad revisa la solicitud")
    d2{"¿Aprueba?"}
    a7("Rechazar con motivo y notificar")
    a8("Asignar rol (sustituir coordinador si aplica) y aprobar la cuenta")
    d3{"¿Aprobada y con correo y celular verificados?"}
    a9("Cuenta ACTIVA: usa la aplicación con el menú de su rol")
    a10("Cuenta PENDIENTE_VERIFICACION: al verificar el dato faltante se vuelve a evaluar (CU-017)")
    f((Fin))
    f2((Fin))
    f3((Fin))
    f4((Fin))
    i --> a1
    a1 --> d1
    d1 -->|no| a2
    a2 --> f2
    d1 -->|sí| a3
    a3 --> a4
    a4 --> a5
    a4 --> a6
    a6 --> d2
    d2 -->|no| a7
    a7 --> f3
    d2 -->|sí| a8
    a8 --> d3
    a5 --> d3
    d3 -->|sí| a9
    d3 -->|no| a10
    a10 --> f4
    a9 --> f
```

</details>

### Moderación de contenido

Desde el reporte de un contenido hasta la decisión del moderador (CU-010 a CU-014, CU-036, CU-037).

[![Moderación de contenido](../../assets/diagramas/diagramas-actividad-2.svg)](../../assets/diagramas/diagramas-actividad-2.svg)

> También disponible en [PNG](../../assets/diagramas/diagramas-actividad-2.png).

<details>
<summary>Versión en texto del diagrama (Mermaid)</summary>

```mermaid
flowchart TD
    i((Inicio))
    a1("Un usuario reporta un Kudo o una réplica con motivo")
    d1{"¿Contenido vetado, propio o con reporte abierto del mismo usuario?"}
    a2("Rechazar el reporte")
    a3("Registrar reporte PENDIENTE; si el contenido estaba VISIBLE pasa a REPORTADO (sigue visible)")
    a4("El moderador (Rector o Coord. Académico) inicia la revisión")
    a5("Contenido y reportes pendientes pasan a EN_REVISION")
    d2{"¿El contenido incumple las políticas?"}
    a6("Vetar: contenido VETADO, reportes ACEPTADO, notificar al autor")
    a7("Liberar: contenido VISIBLE, reportes RECHAZADO, notificar a reportantes")
    a8("Cerrar la moderación con decisión y motivo; registrar auditoría")
    f((Fin))
    f2((Fin))
    i --> a1
    a1 --> d1
    d1 -->|sí| a2
    a2 --> f2
    d1 -->|no| a3
    a3 --> a4
    a4 --> a5
    a5 --> d2
    d2 -->|sí| a6
    d2 -->|no| a7
    a6 --> a8
    a7 --> a8
    a8 --> f
```

</details>

### Publicar un Kudo y conversar

Creación de un Kudo, réplicas y calificaciones (CU-001, CU-004, CU-005, CU-007).

[![Publicar un Kudo y conversar](../../assets/diagramas/diagramas-actividad-3.svg)](../../assets/diagramas/diagramas-actividad-3.svg)

> También disponible en [PNG](../../assets/diagramas/diagramas-actividad-3.png).

<details>
<summary>Versión en texto del diagrama (Mermaid)</summary>

```mermaid
flowchart TD
    i((Inicio))
    a1("Elegir destinatario, categoría activa y escribir el contenido")
    d1{"¿Destinatario activo y distinto del autor, categoría activa y contenido no vacío?"}
    a2("Mostrar el error")
    a3("Crear el Kudo (CREADO) y publicarlo (VISIBLE)")
    a4("Auditar (KudoPublicado) y notificar al destinatario (KUDO_RECIBIDO)")
    a5("Otros usuarios responden o califican")
    d2{"¿El Kudo o la réplica admite interacción (no vetado)?"}
    a6("Rechazar: la conversación está cerrada")
    a7("Registrar la réplica o la calificación (una activa por usuario) y recalcular el promedio")
    f((Fin))
    f2((Fin))
    f3((Fin))
    i --> a1
    a1 --> d1
    d1 -->|no| a2
    a2 --> f2
    d1 -->|sí| a3
    a3 --> a4
    a4 --> a5
    a5 --> d2
    d2 -->|no| a6
    a6 --> f3
    d2 -->|sí| a7
    a7 --> f
```

</details>


## 3. Diagramas de secuencia (UML-003)

### Crear Kudo

CU-001. Recorrido real por las capas: controlador, fachada (transacción), negocio, reglas, dominio (patrón State), DAO, notificación y auditoría en la misma transacción.

[![Crear Kudo](../../assets/diagramas/diagramas-secuencia-1.svg)](../../assets/diagramas/diagramas-secuencia-1.svg)

> También disponible en [PNG](../../assets/diagramas/diagramas-secuencia-1.png).

<details>
<summary>Versión en texto del diagrama (Mermaid)</summary>

```mermaid
sequenceDiagram
    autonumber
    actor P0 as Usuario activo
    participant P1 as KudoControlador
    participant P2 as KudoFachadaImpl
    participant P3 as KudoNegocioImpl
    participant P4 as Reglas (XxxRule)
    participant P5 as KudoDominio
    participant P6 as DAOFactory / KudoDAO
    participant P7 as Notificación y auditoría
    P0->>P1: POST /api/v1/kudos (KudoDto)
    P1->>P2: crear(idActor, kudoDto)
    P2->>P6: iniciarTransaccion()
    P2->>P3: crear(idActor, kudoDominio)  [KudoDtoAssembler]
    P3->>P4: AsegurarDestinatarioDistintoRule, AsegurarDestinatarioActivoRule, AsegurarCategoriaDisponibleRule, ValidarContenidoRule
    P3->>P5: new KudoDominio.Builder()...build()  [CREADO]
    P5->>P5: publicar()  [State: CREADO → VISIBLE]
    P3->>P6: crear(kudoEntidad)  [KudoEntidadAssembler]
    P3->>P7: notificar(KUDO_RECIBIDO) y registrar(KudoPublicado)
    P2->>P6: confirmarTransaccion() / cerrarConexion()
    P1-->>P0: 200 OK + RespuestaOk (Tu Kudo quedó publicado)
```

</details>

### Aprobar solicitud de registro

CU-022. SolicitudNegocioImpl coordina varios objetos en una sola transacción, incluida la sustitución de un coordinador.

[![Aprobar solicitud de registro](../../assets/diagramas/diagramas-secuencia-2.svg)](../../assets/diagramas/diagramas-secuencia-2.svg)

> También disponible en [PNG](../../assets/diagramas/diagramas-secuencia-2.png).

<details>
<summary>Versión en texto del diagrama (Mermaid)</summary>

```mermaid
sequenceDiagram
    autonumber
    actor P0 as Autoridad
    participant P1 as SolicitudControlador
    participant P2 as SolicitudFachadaImpl
    participant P3 as SolicitudNegocioImpl
    participant P4 as Reglas (XxxRule)
    participant P5 as SolicitudRegistroDominio
    participant P6 as CargoNegocioImpl
    participant P7 as UsuarioDominio
    participant P8 as DAO
    participant P9 as Notificación y auditoría
    P0->>P1: POST /api/v1/solicitudes/{id}/aprobacion
    P1->>P2: aprobar(idActor, idSolicitud)
    P2->>P3: aprobar(idActor, idSolicitud)  [transacción]
    P3->>P4: AsegurarSolicitudExisteRule, AsegurarSolicitudPendienteRule, AsegurarNoEsPropiaSolicitudRule, AsegurarAutoridadCompetenteRule (asignacion.puedeDecidir)
    P3->>P5: aprobar(autoridad)  [PENDIENTE → APROBADA]
    P3->>P6: cerrarCargoVigente(...)  [si es coordinador: sustitución]
    P3->>P8: crear asignación de rol (y vínculo si es docente o estudiante)
    P3->>P7: aprobar()  [→ PENDIENTE_VERIFICACION y intentarActivar()]
    P3->>P8: actualizar solicitud y usuario
    P3->>P9: notificar(SOLICITUD_APROBADA) y registrar(SolicitudAprobada)
    P1-->>P0: 200 OK
```

</details>

### Vetar contenido

CU-013 / CU-014. La decisión cambia la moderación, el contenido y sus reportes en una transacción; si no había revisión en curso es un veto directo.

[![Vetar contenido](../../assets/diagramas/diagramas-secuencia-3.svg)](../../assets/diagramas/diagramas-secuencia-3.svg)

> También disponible en [PNG](../../assets/diagramas/diagramas-secuencia-3.png).

<details>
<summary>Versión en texto del diagrama (Mermaid)</summary>

```mermaid
sequenceDiagram
    autonumber
    actor P0 as Moderador
    participant P1 as ModeracionControlador
    participant P2 as ModeracionFachadaImpl
    participant P3 as ModeracionNegocioImpl
    participant P4 as Reglas (XxxRule)
    participant P5 as KudoDominio
    participant P6 as ModeracionDominio
    participant P7 as ReporteDominio
    participant P8 as DAO
    participant P9 as Notificación y auditoría
    P0->>P1: POST /api/v1/moderacion/kudos/{id}/veto (DecisionDto: motivo)
    P1->>P2: vetarKudo(idActor, idKudo, decisionDto)
    P2->>P3: ejecutarEnTransaccion(negocio -> negocio.vetarKudo(...))
    P3->>P4: AsegurarActorTieneRolRule (ROLES_MODERADORES), AsegurarKudoExisteRule, AsegurarContenidoNoVetadoRule, ValidarMotivoDecisionRule
    P3->>P5: vetar()  [State: → VETADO]
    P3->>P6: vetar(motivo)  [cierra la revisión en curso o crea una: veto directo]
    P3->>P7: aceptar(idModeracion) de cada reporte abierto
    P3->>P8: actualizar Kudo, moderación y reportes
    P3->>P9: notificar(CONTENIDO_VETADO) al autor y registrar(KudoVetado)
    P1-->>P0: 200 OK (El contenido quedó vetado)
```

</details>

### Verificar celular y activar la cuenta

CU-017. El código lo valida AsegurarCodigoVigenteRule y la activación la decide UsuarioDominio.

[![Verificar celular y activar la cuenta](../../assets/diagramas/diagramas-secuencia-4.svg)](../../assets/diagramas/diagramas-secuencia-4.svg)

> También disponible en [PNG](../../assets/diagramas/diagramas-secuencia-4.png).

<details>
<summary>Versión en texto del diagrama (Mermaid)</summary>

```mermaid
sequenceDiagram
    autonumber
    actor P0 as Usuario registrado
    participant P1 as VerificacionControlador
    participant P2 as VerificacionFachadaImpl
    participant P3 as VerificacionNegocioImpl
    participant P4 as AsegurarCodigoVigenteRule
    participant P5 as CodigoVerificacionDominio
    participant P6 as UsuarioDominio
    participant P7 as DAO
    P0->>P1: POST /api/v1/verificaciones/celular (CodigoVerificacionDto)
    P1->>P2: verificarCelular(idUsuario, codigoDto)
    P2->>P3: verificarCelular(idUsuario, código)  [transacción]
    P3->>P7: consultar el código pendiente del celular
    P3->>P4: ejecutar(código, ahora, máximo de intentos)
    P3->>P5: marcarUsado()  [o registrarIntentoFallido() si no coincide]
    P3->>P6: verificarCelular()
    P6->>P6: intentarActivar()  [aprobada + correo + celular → ACTIVA]
    P3->>P7: actualizar código y usuario, registrar(CelularVerificado)
    P1-->>P0: 200 OK + estado de la cuenta
```

</details>

### Ingresar con Google

CU-018 y D-09. Google autentica a la persona; la aplicación decide con su propia base de datos qué puede hacer.

[![Ingresar con Google](../../assets/diagramas/diagramas-secuencia-5.svg)](../../assets/diagramas/diagramas-secuencia-5.svg)

> También disponible en [PNG](../../assets/diagramas/diagramas-secuencia-5.png).

<details>
<summary>Versión en texto del diagrama (Mermaid)</summary>

```mermaid
sequenceDiagram
    autonumber
    actor P0 as Persona
    participant P1 as Front-end (Vue)
    participant P2 as Spring Security
    participant P3 as Google
    participant P4 as AutenticacionControlador
    participant P5 as AutenticacionFachadaImpl
    participant P6 as AutenticacionNegocioImpl
    participant P7 as UsuarioDAO
    P0->>P1: Clic en "Ingresar con Google"
    P1->>P2: /oauth2/authorization/google
    P2->>P3: Redirección OAuth2 / OpenID Connect
    P3-->>P2: Código de autorización → token con el correo verificado
    P2-->>P1: Cookie de sesión JSESSIONID (HttpOnly, SameSite=Lax) y redirección
    P1->>P4: GET /api/v1/autenticacion
    P4->>P5: consultarUsuarioActual(correo de Google)
    P5->>P6: consultarUsuarioPorCorreo(correo) y confirmarCorreoVerificadoPorGoogle(usuario)
    P6->>P7: consultarPorFiltro(correo)
    P4-->>P1: UsuarioDto: registrado, estado de la cuenta y rol vigente
    P1-->>P0: Registro (si no existe), estado de la cuenta (si no está ACTIVA) o muro con el menú de su rol
```

</details>


## Cómo se relacionan con el resto del diseño

- Cada **estado** corresponde a una enumeración del [modelo de dominio](../01-modelo-dominio/README.md#enumeraciones) y cada transición es un método de negocio de la clase (por ejemplo `vetar()` en [Kudo](../02-diagrama-clases/clases/Kudo.md)). El ciclo del contenido se implementa con el patrón **State** ([patrones](../05-arquitectura/patrones.md)).
- Cada **actividad** recorre varios casos de uso de la [especificación](../00-analisis/casos-de-uso.md) y muestra dónde se aplican las políticas.
- Cada **secuencia** sigue las capas de la [arquitectura](../05-arquitectura/README.md): controlador → fachada (transacción) → negocio (`XxxNegocioImpl` y sus reglas `XxxRule`) → dominio (`XxxDominio`) → DAO. Los nombres de los participantes son las clases reales del código. La notificación y la auditoría las registra el negocio dentro de la misma transacción (D-26).

