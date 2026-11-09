# Diagrama de clases

[Inicio](../../README.md) · [Modelo de dominio](../01-modelo-dominio/README.md) · [Modelo entidad-relación](../03-modelo-entidad-relacion/README.md) · [Casos de uso](../04-diagrama-casos-uso/README.md)

Diagrama de clases del dominio y documentación de cada clase, igual que en Sigma: relaciones, atributos, constructores, métodos accesores, métodos de negocio y su `Builder`.

## Diagrama

[![Diagrama de clases](../../assets/diagramas/diagrama-clases.svg)](../../assets/diagramas/diagrama-clases.svg)

> Haz clic en la imagen para verla en tamaño completo. También disponible en [PNG](../../assets/diagramas/diagrama-clases.png). Fuente editable en draw.io: [`fuentes/diagrama-clases.drawio`](../../fuentes/diagrama-clases.drawio).

**Cómo leerlo**

- Flecha abierta: navegabilidad. La clase que guarda la referencia apunta a la clase referenciada; la etiqueta indica el atributo, por ejemplo `Kudo --> Categoria : categoria`.
- Rombo relleno: composición; la parte pertenece al todo.
- Los objetos de valor (verde) y las enumeraciones (amarillo) aparecen a la derecha y se usan como tipos de atributo.
- A diferencia de Sigma, los métodos no son solo `registrar/consultar`: son comportamiento del dominio (`kudo.vetar()`, `usuario.intentarActivar()`), porque el enunciado exige objetos ricos (ARQ-008, ARQ-047, D-29).

<details>
<summary>Versión en texto del diagrama (Mermaid)</summary>

```mermaid
classDiagram
direction LR
class ParametroGlobal {
  +UUID id
  +String clave
  +String valor
  +TipoParametro tipoDato
  +String descripcion
  +LocalDateTime fechaModificacion
  +cambiarValor()
  +consultar()
  +valorComoEntero()
}
class CodigoVerificacion {
  +UUID id
  +Usuario usuario
  +CanalVerificacion canal
  +String destino
  +String codigo
  +LocalDateTime fechaGeneracion
  +LocalDateTime fechaExpiracion
  +int intentosFallidos
  +boolean usado
  +generar()
  +verificar()
  +invalidar()
}
class Notificacion {
  +UUID id
  +Usuario destinatario
  +TipoNotificacion tipo
  +String mensaje
  +String tipoReferencia
  +UUID idReferencia
  +LocalDateTime fechaCreacion
  +boolean leida
  +LocalDateTime fechaLectura
  +crear()
  +marcarLeida()
  +consultar()
}
class Auditoria {
  +UUID id
  +LocalDateTime fechaHora
  +Usuario actor
  +String accion
  +String tipoObjeto
  +UUID idObjeto
  +String estadoAnterior
  +String estadoNuevo
  +String motivo
  +String detalle
  +registrarDesdeEvento()
  +consultar()
  +consultarActividadPropia()
}
class Categoria {
  +UUID id
  +String nombre
  +String descripcion
  +boolean activa
  +LocalDateTime fechaCreacion
  +crear()
  +modificar()
  +activar()
  +desactivar()
  +consultar()
}
class Facultad {
  +UUID id
  +String nombre
  +EstadoFacultad estado
  +LocalDateTime fechaCreacion
  +crear()
  +renombrar()
  +activar()
  +inactivar()
  +consultar()
}
class AsignacionRol {
  +UUID id
  +Usuario usuario
  +Rol rol
  +Facultad facultad
  +ProgramaAcademico programa
  +LocalDateTime fechaInicio
  +LocalDateTime fechaFin
  +boolean vigente
  +String motivoFin
  +asignar()
  +cerrar()
  +consultarTitularVigente()
  +consultarHistorialCoordinadores()
}
class Usuario {
  +UUID id
  +String nombres
  +String apellidos
  +CorreoInstitucional correoInstitucional
  +boolean correoVerificado
  +NumeroCelular numeroCelular
  +boolean celularVerificado
  +String contrasena
  +boolean aprobada
  +EstadoCuenta estadoCuenta
  +LocalDateTime fechaRegistro
  +registrar()
  +registrarPorAdministrador()
  +verificarCorreo()
  +verificarCelular()
  +aprobar()
  +intentarActivar()
  +puedeIniciarSesion()
  +actualizarDatosPersonales()
  +cambiarCorreo()
  +cambiarCelular()
  +suspender()
  +reactivar()
  +desactivar()
  +tieneRolVigente()
  +consultar()
}
class Kudo {
  +UUID id
  +Usuario autor
  +Usuario destinatario
  +Categoria categoria
  +ContenidoTexto contenido
  +LocalDateTime fechaCreacion
  +EstadoContenido estado
  +int cantidadVisualizaciones
  +BigDecimal promedioCalificacion
  +int cantidadCalificaciones
  +crear()
  +registrarVisualizacion()
  +consultarMuro()
  +consultarHistorial()
  +marcarReportado()
  +iniciarRevision()
  +liberar()
  +vetar()
  +admiteInteraccion()
  +actualizarResumenCalificaciones()
}
class ProgramaAcademico {
  +UUID id
  +String nombre
  +Facultad facultad
  +boolean activo
  +LocalDateTime fechaCreacion
  +crear()
  +renombrar()
  +inactivar()
  +consultar()
}
class SolicitudRegistro {
  +UUID id
  +Usuario solicitante
  +Rol rolSolicitado
  +Facultad facultad
  +ProgramaAcademico programa
  +EstadoSolicitud estado
  +LocalDateTime fechaSolicitud
  +Usuario decididaPor
  +LocalDateTime fechaDecision
  +String motivoRechazo
  +crear()
  +aprobar()
  +rechazar()
  +consultarPendientes()
}
class VinculacionPrograma {
  +UUID id
  +Usuario usuario
  +ProgramaAcademico programa
  +TipoVinculo tipoVinculo
  +LocalDateTime fechaInicio
  +LocalDateTime fechaFin
  +boolean vigente
  +String motivoDesvinculacion
  +vincular()
  +desvincular()
  +consultarPorUsuario()
  +consultarPorPrograma()
}
class Replica {
  +UUID id
  +Usuario autor
  +Kudo kudo
  +Replica replicaPadre
  +ContenidoTexto contenido
  +LocalDateTime fechaCreacion
  +EstadoContenido estado
  +BigDecimal promedioCalificacion
  +int cantidadCalificaciones
  +responderKudo()
  +responderReplica()
  +consultarConversacion()
  +marcarReportada()
  +iniciarRevision()
  +liberar()
  +vetar()
  +admiteInteraccion()
  +actualizarResumenCalificaciones()
}
class Calificacion {
  +UUID id
  +Usuario autor
  +Kudo kudo
  +Replica replica
  +Estrellas estrellas
  +LocalDateTime fechaCalificacion
  +LocalDateTime fechaModificacion
  +boolean activa
  +calificar()
  +modificarEstrellas()
  +consultarValoraciones()
}
class Moderacion {
  +UUID id
  +Kudo kudo
  +Replica replica
  +Usuario moderador
  +EstadoModeracion estado
  +LocalDateTime fechaInicio
  +LocalDateTime fechaCierre
  +DecisionModeracion decision
  +String motivoDecision
  +iniciar()
  +vetar()
  +liberar()
  +consultarHistoricoVetos()
}
class Reporte {
  +UUID id
  +Usuario reportante
  +Kudo kudo
  +Replica replica
  +MotivoReporte motivo
  +String descripcion
  +LocalDateTime fechaReporte
  +EstadoReporte estado
  +Moderacion moderacion
  +crear()
  +pasarARevision()
  +aceptar()
  +rechazar()
  +consultar()
}
class CorreoInstitucional {
  <<ValueObject>>
  -String valor
  +crear()
  +obtenerValor()
}
class NumeroCelular {
  <<ValueObject>>
  -String valor
  +crear()
  +obtenerValor()
}
class ContenidoTexto {
  <<ValueObject>>
  -String valor
  +crear()
  +obtenerValor()
}
class Estrellas {
  <<ValueObject>>
  -int valor
  +crear()
  +obtenerValor()
}
Usuario "1" *-- "0..*" AsignacionRol : Tiene
AsignacionRol "0..*" --> "0..1" Facultad : facultad
AsignacionRol "0..*" --> "0..1" ProgramaAcademico : programa
Facultad "1" *-- "0..*" ProgramaAcademico : Agrupa
VinculacionPrograma "0..*" --> "1" Usuario : usuario
VinculacionPrograma "0..*" --> "1" ProgramaAcademico : programa
SolicitudRegistro "0..*" --> "1" Usuario : solicitante
SolicitudRegistro "0..*" --> "0..1" Usuario : decididaPor
SolicitudRegistro "0..*" --> "0..1" Facultad : facultad
SolicitudRegistro "0..*" --> "0..1" ProgramaAcademico : programa
Usuario "1" *-- "0..*" CodigoVerificacion : Verifica con
Kudo "0..*" --> "1" Usuario : autor
Kudo "0..*" --> "1" Usuario : destinatario
Kudo "0..*" --> "1" Categoria : categoria
Kudo "1" *-- "0..*" Replica : Contiene
Replica "0..*" --> "0..1" Replica : replicaPadre
Replica "0..*" --> "1" Usuario : autor
Calificacion "0..*" --> "0..1" Kudo : kudo
Calificacion "0..*" --> "0..1" Replica : replica
Calificacion "0..*" --> "1" Usuario : autor
Reporte "0..*" --> "0..1" Kudo : kudo
Reporte "0..*" --> "0..1" Replica : replica
Reporte "0..*" --> "1" Usuario : reportante
Moderacion "0..*" --> "0..1" Kudo : kudo
Moderacion "0..*" --> "0..1" Replica : replica
Moderacion "0..*" --> "1" Usuario : moderador
Reporte "0..*" --> "0..1" Moderacion : moderacion
Auditoria "0..*" --> "0..1" Usuario : actor
Notificacion "0..*" --> "1" Usuario : destinatario
Usuario ..> CorreoInstitucional
Usuario ..> NumeroCelular
Kudo ..> ContenidoTexto
Replica ..> ContenidoTexto
Calificacion ..> Estrellas
```

</details>


### Enumeraciones

<details>
<summary>Versión en texto del diagrama (Mermaid)</summary>

```mermaid
classDiagram
direction LR
class Rol {
  <<enumeration>>
  ADMINISTRADOR
  RECTOR
  COORDINADOR_ACADEMICO
  COORDINADOR_FACULTAD
  COORDINADOR_PROGRAMA
  DOCENTE
  ESTUDIANTE
  +rolAprobador()
}
class EstadoCuenta {
  <<enumeration>>
  PENDIENTE_APROBACION
  PENDIENTE_VERIFICACION
  ACTIVA
  SUSPENDIDA
  DESACTIVADA
}
class EstadoFacultad {
  <<enumeration>>
  EN_CONFORMACION
  ACTIVA
  INACTIVA
}
class TipoVinculo {
  <<enumeration>>
  DOCENTE
  ESTUDIANTE
}
class EstadoSolicitud {
  <<enumeration>>
  PENDIENTE
  APROBADA
  RECHAZADA
}
class CanalVerificacion {
  <<enumeration>>
  CORREO
  CELULAR
}
class EstadoContenido {
  <<enumeration>>
  CREADO
  VISIBLE
  REPORTADO
  EN_REVISION
  VETADO
}
class MotivoReporte {
  <<enumeration>>
  LENGUAJE_OFENSIVO
  ACOSO
  DISCRIMINACION
  SPAM
  INFORMACION_FALSA
  OTRO
}
class EstadoReporte {
  <<enumeration>>
  PENDIENTE
  EN_REVISION
  ACEPTADO
  RECHAZADO
}
class EstadoModeracion {
  <<enumeration>>
  EN_CURSO
  CERRADA
}
class DecisionModeracion {
  <<enumeration>>
  VETAR
  LIBERAR
}
class TipoNotificacion {
  <<enumeration>>
  KUDO_RECIBIDO
  REPLICA_RECIBIDA
  SOLICITUD_APROBADA
  SOLICITUD_RECHAZADA
  SOLICITUD_PENDIENTE
  CONTENIDO_VETADO
  CODIGO_VERIFICACION
  CUENTA_SUSPENDIDA
  CUENTA_REACTIVADA
}
class TipoParametro {
  <<enumeration>>
  TEXTO
  ENTERO
}
```

</details>


> **Clases de soporte del diseño** que no aparecen en el diagrama principal para no recargarlo: `Conversacion`, `NodoConversacion` e interfaz `ContenidoConversable` (Composite), `PoliticaAmbito`, `PoliticaModeracion` y `PoliticaGestionCategorias` (Specification), `ServicioAprobacion` y `ServicioModeracion` (servicios de dominio), y `PublicadorEventos`, `EventoDominio` y sus suscriptores (Observer). Se describen en [patrones](../05-arquitectura/patrones.md).


## Clases

| Clase | Estereotipo | Descripción |
|---|---|---|
| [ParametroGlobal](clases/ParametroGlobal.md) | class | Valor de configuración de la plataforma que administran los Administradores (RN-130, D-11). |
| [CodigoVerificacion](clases/CodigoVerificacion.md) | class | Código temporal que se envía al correo o al celular de un usuario para validar que le pertenece. |
| [Notificacion](clases/Notificacion.md) | class | Aviso dentro de la aplicación dirigido a un usuario sobre algo que le concierne. |
| [Auditoria](clases/Auditoria.md) | class | Registro histórico e inmutable de una operación relevante realizada en la plataforma. Permite reconstruir quién hizo qué, cuándo, sobre qué objeto y por qué. |
| [Categoria](clases/Categoria.md) | class | Clasificación institucional de los reconocimientos (RN-132). Puede estar activa o inactiva; solo las activas se usan para Kudos nuevos. |
| [Facultad](clases/Facultad.md) | class | Unidad académica de la Universidad que agrupa Programas Académicos y es acompañada por un Coordinador de Facultad. |
| [AsignacionRol](clases/AsignacionRol.md) | class | Registro histórico de cada rol institucional que ha tenido un usuario. Para los coordinadores también indica qué Facultad o Programa acompaña. Solo una asignación por usuario puede estar vigente. |
| [Usuario](clases/Usuario.md) | class | Persona de la comunidad universitaria que tiene una cuenta en la plataforma. Su rol institucional vigente se conoce por su AsignacionRol vigente. |
| [Kudo](clases/Kudo.md) | class | Reconocimiento formal que una persona otorga a otra persona de la comunidad universitaria. Es la raíz de su conversación. |
| [ProgramaAcademico](clases/ProgramaAcademico.md) | class | Programa de formación que pertenece a exactamente una Facultad y es acompañado por un Coordinador de Programa. |
| [SolicitudRegistro](clases/SolicitudRegistro.md) | class | Petición que hace una persona al registrarse para obtener un rol institucional. La decide (aprueba o rechaza) la autoridad que corresponde según la cadena de aprobación. |
| [VinculacionPrograma](clases/VinculacionPrograma.md) | class | Vínculo, con historial, entre un Docente o Estudiante y un Programa Académico. Resuelve la relación muchos a muchos y conserva la trazabilidad de vinculaciones y desvinculaciones. |
| [Replica](clases/Replica.md) | class | Intervención en la conversación de un Kudo. Su padre es el Kudo (réplica de primer nivel) u otra réplica (respuesta). |
| [Calificacion](clases/Calificacion.md) | class | Valoración de 1 a 5 estrellas que un usuario asigna a un Kudo o a una réplica. Cada usuario tiene como máximo una calificación activa por contenido. |
| [Moderacion](clases/Moderacion.md) | class | Revisión y decisión institucional sobre un Kudo o una réplica, tomada por el Rector o el Coordinador Académico. El histórico de vetos son las moderaciones con decisión VETAR. |
| [Reporte](clases/Reporte.md) | class | Solicitud de revisión que un usuario hace sobre un Kudo o una réplica que considera inapropiado. |
| [CorreoInstitucional](clases/CorreoInstitucional.md) | value object | Formato de correo, dominio institucional, normalizado a minúsculas. Usado en Usuario.correoInstitucional. |
| [NumeroCelular](clases/NumeroCelular.md) | value object | 10 dígitos, inicia en 3. Usado en Usuario.numeroCelular. |
| [ContenidoTexto](clases/ContenidoTexto.md) | value object | No vacío, sin límite de longitud, nunca se trunca. Usado en Kudo.contenido, Replica.contenido. |
| [Estrellas](clases/Estrellas.md) | value object | Entero de 1 a 5. Usado en Calificacion.estrellas. |
| [Rol](../01-modelo-dominio/README.md#rol) | enumeration | Rol institucional. Cada valor conoce qué rol lo aprueba. |
| [EstadoCuenta](../01-modelo-dominio/README.md#estadocuenta) | enumeration | Ciclo de vida de la cuenta (D-17). |
| [EstadoFacultad](../01-modelo-dominio/README.md#estadofacultad) | enumeration | Ciclo de vida de la Facultad (D-05). |
| [TipoVinculo](../01-modelo-dominio/README.md#tipovinculo) | enumeration | Calidad del vínculo con el programa. |
| [EstadoSolicitud](../01-modelo-dominio/README.md#estadosolicitud) | enumeration | Estados de la solicitud de registro. |
| [CanalVerificacion](../01-modelo-dominio/README.md#canalverificacion) | enumeration | Dato de contacto que se verifica. |
| [EstadoContenido](../01-modelo-dominio/README.md#estadocontenido) | enumeration | Ciclo de vida de Kudos y réplicas (D-20). |
| [MotivoReporte](../01-modelo-dominio/README.md#motivoreporte) | enumeration | Motivos de reporte (CU-010). |
| [EstadoReporte](../01-modelo-dominio/README.md#estadoreporte) | enumeration | Estados del reporte (RN-210 a RN-212). |
| [EstadoModeracion](../01-modelo-dominio/README.md#estadomoderacion) | enumeration | Estados de la moderación. |
| [DecisionModeracion](../01-modelo-dominio/README.md#decisionmoderacion) | enumeration | Decisión del moderador. |
| [TipoNotificacion](../01-modelo-dominio/README.md#tiponotificacion) | enumeration | Tipos de aviso (D-27). |
| [TipoParametro](../01-modelo-dominio/README.md#tipoparametro) | enumeration | Tipo de dato de un parámetro global. |
