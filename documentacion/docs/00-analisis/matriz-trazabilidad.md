# Matriz de trazabilidad

[Inicio](../../README.md) · [Requisitos](requisitos.md) · [Reglas de negocio](reglas-de-negocio.md) · [Casos de uso](casos-de-uso.md) · [Modelo anémico](../01-modelo-dominio/README.md)

Relaciona **requisito → reglas → caso de uso → objeto de dominio → componente → prueba → evidencia**, como pide el documento 07 §4 (RNF-034, RNF-035).

> **Estado:** actualizada con los nombres **reales** del código (back-end `co.edu.uco.kudos`, front-end `src/modules`). Los requisitos marcados ⛔ son complementarios (🆕, propuestos en el análisis) y quedan fuera del alcance de esta versión. Los resultados de las pruebas están en [`07-calidad-y-pruebas`](../07-calidad-y-pruebas/README.md).

**Componentes**: `XxxFachada` es la fachada del módulo (transacción), que llama a `XxxNegocioImpl` y a sus reglas (`XxxRule`). Entre paréntesis va el endpoint (todos empiezan por `/api/v1`). Las pantallas del front están en `src/modules/<modulo>/views` y `components`.
**Pruebas**: **PU** = prueba unitaria del dominio (JUnit, `XxxDominioTest`). **PF** = prueba del negocio y sus reglas con la fábrica de DAO falsa en memoria (JUnit, `XxxNegocioTest`). **PA** = carpeta de la colección Bruno. **PV** = prueba del front-end (Vitest, `src/__tests__/*.spec.js`).

---

## 1. Usuarios, identidad y acceso

| RF | Reglas / decisiones | CU | Objeto de dominio | Componente (back) | Pantalla (front) | Prueba |
|---|---|---|---|---|---|---|
| RF-001 | RN-069 a RN-076, D-17 | CU-016 | Usuario, SolicitudRegistro | RegistroFachada → `RegistroNegocioImpl.registrarSolicitud` (`POST /registros`) | RegistroView | PF: RegistroNegocioTest; PV: Identidad.spec; PA: Registro |
| RF-002 | RN-069, RN-071, POL-001, D-12 | CU-016 | Usuario, ParametroGlobal | `ValidarCorreoInstitucionalRule` (parámetro `DOMINIO_CORREO`) | RegistroView | PF: ReglasIdentidadTest (dominio inválido, mayúsculas, espacios) |
| RF-003 | RN-070, RN-072, POL-002, D-12 | CU-016 | Usuario | `ValidarCelularRule` | RegistroView, PerfilView | PF: ReglasIdentidadTest; PV: identidadValidations.spec |
| RF-004 | RN-073 | CU-016, CU-020 | Usuario | `AsegurarCorreoNoRepetidoRule` | RegistroView, PerfilView | PF: RegistroNegocioTest, CuentaNegocioTest (correo repetido) |
| RF-005 | RN-074 | CU-016, CU-020 | Usuario | `AsegurarCelularNoRepetidoRule` | RegistroView, PerfilView | PF: RegistroNegocioTest, CuentaNegocioTest (celular repetido) |
| RF-006 | RN-077, RN-079, D-10 | CU-017 | CodigoVerificacion, Usuario | Google (OAuth2) + `AutenticacionNegocioImpl.confirmarCorreoVerificadoPorGoogle` | LoginView | PF: AutenticacionNegocioTest |
| RF-007 | RN-078, RN-080, D-10 | CU-017 | CodigoVerificacion, Usuario | VerificacionFachada → `VerificacionNegocioImpl.verificarCelular` + `AsegurarCodigoVigenteRule` (`POST /verificaciones/celular`) | EstadoCuentaView | PU: UsuarioDominioTest; PF: VerificacionNegocioTest (errado, vencido, intentos); PA: Verificacion |
| RF-008 | RN-101, RN-112 a RN-127 | CU-020, CU-034 | Usuario | CuentaFachada → `CuentaNegocioImpl` (`GET /cuenta`); vínculos de un Programa (`GET /programas/{id}/vinculos`) | PerfilView, ProgramaDetalleView | PF: CuentaNegocioTest; PA: Cuenta |
| RF-009 | RN-102, RN-104, RN-105, RN-107, RN-109 | CU-020 | Usuario | `CuentaNegocioImpl.actualizarDatosPersonales` (`PUT /cuenta/datos-personales`) | PerfilView | PF: CuentaNegocioTest; PV: Identidad.spec |
| RF-010 | RN-081, RN-110 | CU-020 | Usuario | `CuentaNegocioImpl.cambiarCorreo` + `usuario.cambiarCorreo()` (`PUT /cuenta/correo`) | PerfilView | PU: UsuarioDominioTest; PF: CuentaNegocioTest; PV: Identidad.spec (cierra la sesión) |
| RF-011 | RN-082, RN-111 | CU-020 | Usuario | `CuentaNegocioImpl.cambiarCelular` + `usuario.cambiarCelular()` (`PUT /cuenta/celular`) | PerfilView | PU: UsuarioDominioTest; PF: CuentaNegocioTest |
| RF-078 | RN-085 a RN-089, POL-003, D-09 | CU-018 | Usuario | `SeguridadConfiguracion` (OAuth2) + AutenticacionFachada → `AsegurarUsuarioRegistradoRule`, `AsegurarCuentaActivaRule` (`GET /autenticacion`) | LoginView, EstadoCuentaView | PF: AutenticacionNegocioTest; PV: Sesion.spec; PA: Autenticacion |
| RF-079 | RNF-001 | CU-019 | — | `SeguridadConfiguracion` (`POST /autenticacion/salir`) | AppTopbar | PV: Sesion.spec (salir) |
| RF-080 | D-10 | CU-017 | CodigoVerificacion | `VerificacionNegocioImpl.reenviarCodigoCelular` (`POST /verificaciones/celular/reenvio`) | EstadoCuentaView | PF: VerificacionNegocioTest (reenviar invalida anteriores) |
| RF-081 | RN-103 | CU-020 | Usuario | `CuentaNegocioImpl` (`GET /cuenta`: correo, celular, aprobación, solicitud) | EstadoCuentaView | PF: CuentaNegocioTest; PV: Sesion.spec |
| RF-082 | RN-106 | CU-021 | Auditoria | ⛔ Fuera del alcance de esta versión | — | — |

## 2. Roles y aprobaciones

| RF | Reglas / decisiones | CU | Objeto de dominio | Componente (back) | Pantalla (front) | Prueba |
|---|---|---|---|---|---|---|
| RF-012 | RN-090 a RN-095, D-17 | CU-016, CU-022, CU-024, CU-025 | Rol, AsignacionRol, SolicitudRegistro | RegistroFachada (autorregistro) + AutoridadFachada (Rector y Administrador) | RegistroView, RegistrarAutoridadView | PF: RegistroNegocioTest, AutoridadNegocioTest |
| RF-013 | RN-001 a RN-004, INV-005, D-13 | CU-022 | Usuario, AsignacionRol | `AsegurarRolUnicoVigenteRule` + índice único filtrado | — | PF: SolicitudNegocioTest, AutoridadNegocioTest |
| RF-014 | RN-005 a RN-038, INV-001 a INV-006, D-14 | CU-022, CU-024 | AsignacionRol | `AsegurarRolUnicoVigenteRule`, `CargoNegocioImpl.cerrarCargoVigente` (sustitución D-15) | SolicitudesView | PF: SolicitudNegocioTest (sustitución, Rector duplicado) |
| RF-035 | RN-090, D-03 | CU-022 | SolicitudRegistro | SolicitudFachada → `SolicitudNegocioImpl.aprobar` + `asignacion.puedeDecidir()` (`POST /solicitudes/{id}/aprobacion`) | SolicitudesView | PU: OrganizacionDominioTest; PF: SolicitudNegocioTest; PV: Aprobaciones.spec; PA: Solicitudes |
| RF-036 | RN-091, RN-117 | CU-022 | SolicitudRegistro | Ídem RF-035 (ámbito de Facultad) | SolicitudesView | PF: SolicitudNegocioTest |
| RF-037 | RN-092, RN-121 | CU-022 | SolicitudRegistro | Ídem RF-035 (Coord. Académico) | SolicitudesView | PF: SolicitudNegocioTest |
| RF-038 | RN-093, RN-126 | CU-022 | SolicitudRegistro | Ídem RF-035 (Rector) | SolicitudesView | PF: SolicitudNegocioTest |
| RF-039 | RN-094, RN-129, INV-001 | CU-024 | Usuario, AsignacionRol | AutoridadFachada → `AutoridadNegocioImpl.registrarAutoridad` (`POST /autoridades`) | RegistrarAutoridadView | PF: AutoridadNegocioTest; PV: Aprobaciones.spec; PA: Autoridades |
| RF-040 | RN-095, RN-129 | CU-025 | Usuario, AsignacionRol | Ídem RF-039 (rol Administrador) | RegistrarAutoridadView | PF: AutoridadNegocioTest |
| RF-086 | D-17 | CU-023 | SolicitudRegistro | `SolicitudNegocioImpl.rechazar` + `ValidarMotivoRechazoRule` (`POST /solicitudes/{id}/rechazo`) | SolicitudesView (RechazoModal) | PF: SolicitudNegocioTest; PV: Aprobaciones.spec |
| RF-087 | POL-004 | CU-022, CU-023 | SolicitudRegistro, AsignacionRol | `SolicitudNegocioImpl.consultarPendientes` (`GET /solicitudes/pendientes`) | SolicitudesView | PF: SolicitudNegocioTest (solo su ámbito) |
| RF-088 | RN-108, POL-004, POL-005 | CU-022 | SolicitudRegistro | `AsegurarNoEsPropiaSolicitudRule`, `AsegurarAutoridadCompetenteRule` | — | PF: SolicitudNegocioTest |
| RF-089 | RN-216, RN-217, D-07 | CU-026, CU-027 | Usuario | Parcial: suspensión y reactivación automáticas en `VinculacionNegocioImpl`. A mano: ⛔ Fuera del alcance de esta versión | — | PF: VinculacionNegocioTest |

## 3. Estructura organizacional

| RF | Reglas / decisiones | CU | Objeto de dominio | Componente (back) | Pantalla (front) | Prueba |
|---|---|---|---|---|---|---|
| RF-015 | RN-039, RN-040, RN-045, D-04, D-05 | CU-028 | Facultad | FacultadFachada → `FacultadNegocioImpl.registrar` (`POST /facultades`) | FacultadesView | PF: OrganizacionNegocioTest; PV: Organizacion.spec; PA: Facultades |
| RF-016 | RN-122 | CU-034 | Facultad | `FacultadNegocioImpl.consultar` + `AsegurarPuedeVerFacultadRule` (`GET /facultades`) | FacultadesView | PF: OrganizacionNegocioTest |
| RF-017 | D-04 | CU-029 | Facultad | `FacultadNegocioImpl.modificar` (`PUT /facultades/{id}`) | FacultadesView | PF: OrganizacionNegocioTest |
| RF-018 | RN-042 | CU-034 | AsignacionRol | `CargoNegocioImpl.consultarHistorial` (`GET /facultades/{id}/coordinadores`) | FacultadesView | PF: OrganizacionNegocioTest |
| RF-019 | RN-043, RN-044 | CU-034 | AsignacionRol | Ídem RF-018 (con fechas de inicio y fin) | FacultadesView (historial) | PV: Organizacion.spec |
| RF-083 | D-05, D-28 | CU-029 | Facultad | `FacultadNegocioImpl.inactivar` + `AsegurarFacultadSinProgramasActivosRule` | FacultadesView | PF: OrganizacionNegocioTest; PV: Organizacion.spec |
| RF-020 | RN-046 a RN-049, D-05 | CU-030 | ProgramaAcademico, Facultad | ProgramaFachada → `ProgramaNegocioImpl.registrar` + `AsegurarFacultadAdmiteProgramasRule` (`POST /programas`) | ProgramasView | PF: OrganizacionNegocioTest; PV: Organizacion.spec; PA: Programas |
| RF-021 | RN-118 | CU-034 | ProgramaAcademico | `ProgramaNegocioImpl.consultar` + `AsegurarPuedeVerProgramaRule` (`GET /programas`) | ProgramasView | PF: OrganizacionNegocioTest |
| RF-022 | D-04 | CU-031 | ProgramaAcademico | `ProgramaNegocioImpl.modificar` (`PUT /programas/{id}`) | ProgramasView | PF: OrganizacionNegocioTest |
| RF-023 | RN-050 | CU-034 | AsignacionRol | `CargoNegocioImpl.consultarHistorial` (`GET /programas/{id}/coordinadores`) | ProgramaDetalleView | PF: OrganizacionNegocioTest |
| RF-024 | RN-051, RN-052 | CU-034 | AsignacionRol | Ídem RF-023 | ProgramaDetalleView | PV: Organizacion.spec |
| RF-084 | D-28 | CU-031 | ProgramaAcademico | `ProgramaNegocioImpl.inactivar` + `AsegurarProgramaSinVinculosRule` | ProgramasView | PF: OrganizacionNegocioTest |
| RF-025 | RN-058, RN-090 | CU-016 | SolicitudRegistro | RegistroFachada (rol Estudiante con Programa) | RegistroView | PF: RegistroNegocioTest |
| RF-026 | RN-053, RN-112 | CU-032 | VinculacionPrograma | VinculacionFachada → `VinculacionNegocioImpl.vincular` + `AsegurarActorCoordinaProgramaRule` (`POST /programas/{id}/vinculos`) | ProgramaDetalleView | PF: VinculacionNegocioTest; PV: Organizacion.spec; PA: Vinculos |
| RF-027 | RN-054 | CU-034 | VinculacionPrograma | `GET /usuarios/{id}/vinculos`, `GET /cuenta/vinculos` | PerfilView (Mis Programas) | PF: VinculacionNegocioTest |
| RF-028 | RN-055 | CU-034 | VinculacionPrograma | `GET /programas/{id}/vinculos` | ProgramaDetalleView | PF: VinculacionNegocioTest |
| RF-029 | RN-056, RN-057, INV-012, INV-013 | CU-032, CU-033 | VinculacionPrograma | `VinculacionPrograma` con fechas, motivo y auditoría (`PersonaVinculada`, `PersonaDesvinculada`) | ProgramaDetalleView | PF: VinculacionNegocioTest |
| RF-030 | RN-066, D-03 | CU-016 | SolicitudRegistro | RegistroFachada (rol Docente con Programa) | RegistroView | PF: RegistroNegocioTest |
| RF-031 | RN-061, RN-113 | CU-032 | VinculacionPrograma | Ídem RF-026 (tipo Docente) + `AsegurarRolCoincideConVinculoRule` | ProgramaDetalleView | PF: VinculacionNegocioTest |
| RF-032 | RN-062 | CU-034 | VinculacionPrograma | Ídem RF-027 | PerfilView | PF: VinculacionNegocioTest |
| RF-033 | RN-063 | CU-034 | VinculacionPrograma | Ídem RF-028 | ProgramaDetalleView | PF: VinculacionNegocioTest |
| RF-034 | RN-064, RN-065, INV-015, INV-016 | CU-032, CU-033 | VinculacionPrograma | Ídem RF-029 | ProgramaDetalleView | PF: VinculacionNegocioTest |
| RF-085 | RN-057, RN-065, RN-114, RN-115, D-16 | CU-033 | VinculacionPrograma, Usuario | `VinculacionNegocioImpl.desvincular` + `ValidarMotivoDesvinculacionRule` (`POST /vinculos/{id}/desvinculacion`) | ProgramaDetalleView | PF: VinculacionNegocioTest; PV: Organizacion.spec |
| RF-072 a RF-077 | RN-054, RN-062, INV-001, INV-002, INV-009, INV-010 | CU-034 | AsignacionRol, VinculacionPrograma | `VinculacionFachadaImpl.consultarEstructura` → `CargoNegocioImpl.consultarCargosVigentes` (`GET /estructura`) | EstructuraView | PF: VinculacionNegocioTest; PV: Organizacion.spec; PA: Estructura |

## 4. Categorías

| RF | Reglas / decisiones | CU | Objeto de dominio | Componente (back) | Pantalla (front) | Prueba |
|---|---|---|---|---|---|---|
| RF-041 | RN-134 a RN-140, D-01 | CU-035 | Categoria | CategoriaFachada → `CategoriaNegocioImpl` + `AsegurarNombreCategoriaNoRepetidoRule`, `AsegurarActorTieneRolRule` (`POST /categorias`) | CategoriasView | PF: CategoriaNegocioTest; PV: Categorias.spec; PA: Categorias |
| RF-042 | RN-141, RN-145 | CU-035 | Categoria | `PUT /categorias/{id}` | CategoriasView | PF: CategoriaNegocioTest |
| RF-043 | RN-142 | CU-035 | Categoria | `POST /categorias/{id}/activacion` | CategoriasView | PF: CategoriaNegocioTest |
| RF-044 | RN-143, RN-144 | CU-035 | Categoria | `POST /categorias/{id}/desactivacion` | CategoriasView | PF: CategoriaNegocioTest |
| RF-045 | INV-018 | CU-035, CU-001 | Categoria | `GET /categorias` (activas) y `GET /categorias/todas` (gestores) | CategoriasView, MuroView | PF: CategoriaNegocioTest; PV: Categorias.spec |

## 5. Kudos y réplicas

| RF | Reglas / decisiones | CU | Objeto de dominio | Componente (back) | Pantalla (front) | Prueba |
|---|---|---|---|---|---|---|
| RF-046 | RN-146 a RN-150, D-19, D-20 | CU-001 | Kudo, Categoria, Usuario | KudoFachada → `KudoNegocioImpl.crear` + `AsegurarDestinatarioDistintoRule`, `AsegurarDestinatarioActivoRule`, `AsegurarCategoriaDisponibleRule`, `ValidarContenidoRule` (`POST /kudos`) | MuroView (CrearKudoModal) | PU: KudoDominioTest; PF: KudoNegocioTest; PV: Kudos.spec; PA: Kudos |
| RF-047 | RN-154, RN-155, RN-229, D-25 | CU-002 | Kudo | `KudoNegocioImpl.consultar` + `kudo.registrarVisualizacion()` (`GET /kudos/{id}`) | KudoDetalleView | PF: KudoNegocioTest; PV: Kudos.spec |
| RF-048 a RF-050 | — | CU-003 | Kudo | `KudoNegocioImpl.consultarHistorial` (`GET /usuarios/{id}/kudos?tipo=`) | HistorialKudosView | PF: KudoNegocioTest; PV: Kudos.spec |
| RF-051 | RN-149, RN-145, INV-017 | CU-001 | Kudo, Categoria | El Kudo guarda `id_categoria` y la categoría nunca se borra (D-28) | KudoCard | PF: KudoNegocioTest |
| RF-052 | RN-152, RN-153, RN-170, INV-024 | CU-001, CU-004 | Kudo, Replica | Columnas `nvarchar(max)`; el front muestra el texto completo ("Ver más") | KudoCard, ReplicaNodo | Prueba con la base real: 22 000 y 6 599 caracteres sin recorte |
| RF-090 | RN-155, RNF-010 | CU-042 | Kudo | `KudoNegocioImpl.consultarMuro` + `KudoFachadaImpl.armarPagina` (`GET /kudos?categoria=&persona=&pagina=`) | MuroView | PF: KudoNegocioTest; PV: Kudos.spec |
| RF-053 | RN-156, RN-161 | CU-004 | Replica, Kudo | ReplicaFachada → `ReplicaNegocioImpl.responderKudo` + `AsegurarKudoAdmiteInteraccionRule` (`POST /kudos/{id}/replicas`) | KudoDetalleView (ConversacionArbol) | PF: ReplicaNegocioTest; PV: Conversacion.spec; PA: Conversacion |
| RF-054 | RN-162 | CU-005 | Replica | `ReplicaNegocioImpl.responderReplica` + `AsegurarReplicaAdmiteInteraccionRule` (`POST /replicas/{id}/respuestas`) | ReplicaNodo | PF: ReplicaNegocioTest; PV: Conversacion.spec |
| RF-055 | D-21, D-22 | CU-006 | Replica (árbol por `idReplicaPadre`) | `ConversacionDtoAssembler.armarArbol` (`GET /kudos/{id}/conversacion`) | ConversacionArbol, ReplicaNodo (recursivo) | PF: ReplicaNegocioTest, AssemblersConversacionTest; PV: Conversacion.spec |
| RF-056 | RN-163 a RN-167 | CU-005, CU-006 | Replica | Sin límites: el padre se fija al crear (D-22) | ReplicaNodo (sangría hasta el nivel 5) | PF: ReplicaNegocioTest (16 niveles); base real: 9 niveles |

## 6. Calificaciones, reportes y moderación

| RF | Reglas / decisiones | CU | Objeto de dominio | Componente (back) | Pantalla (front) | Prueba |
|---|---|---|---|---|---|---|
| RF-057 | RN-157, RN-175, D-23 | CU-007 | Calificacion, Kudo | CalificacionFachada → `CalificacionNegocioImpl.calificarKudo` + `AsegurarContenidoAjenoRule` (`PUT /kudos/{id}/calificacion`) | KudoDetalleView (CalificacionEstrellas) | PF: CalificacionNegocioTest; PV: Conversacion.spec; PA: Conversacion |
| RF-058 | RN-171, RN-176 | CU-008 | Calificacion, Replica | `CalificacionNegocioImpl.calificarReplica` (`PUT /replicas/{id}/calificacion`) | ReplicaNodo | PF: CalificacionNegocioTest |
| RF-059 | RN-177, INV-030 | CU-007, CU-008 | Calificacion | `ValidarEstrellasRule` (1 a 5) + `check` en la tabla | CalificacionEstrellas | PF: CalificacionNegocioTest |
| RF-060 | RN-178 a RN-181, INV-031, INV-032 | CU-007, CU-008 | Calificacion | `calificacion.modificarEstrellas()` + índices únicos filtrados | CalificacionEstrellas | PF: CalificacionNegocioTest (modifica, no duplica) |
| RF-091 | RN-237, D-23 | CU-007, CU-008 | Calificacion | Auditoría `CalificacionActualizada` con valor anterior y nuevo | CalificacionEstrellas | PF: CalificacionNegocioTest |
| RF-092 | RN-185, RN-238 | CU-007, CU-008 | Kudo, Replica (resumen de calificaciones) | `kudo.actualizarResumenCalificaciones()` / `replica.actualizarResumenCalificaciones()` | ValoracionesResumen | PU: KudoDominioTest; PF: CalificacionNegocioTest |
| RF-061 | RN-158, RN-186, RN-202 | CU-010 | Reporte, Kudo | ReporteFachada → `ReporteNegocioImpl.reportarKudo` + `AsegurarNoReportaContenidoPropioRule`, `AsegurarSinReporteAbiertoRule` (`POST /kudos/{id}/reportes`) | KudoDetalleView (ReportarModal) | PU: KudoDominioTest; PF: ReporteNegocioTest; PV: Moderacion.spec; PA: Moderacion |
| RF-062 | RN-172, RN-187, RN-207 | CU-011 | Reporte, Replica | `ReporteNegocioImpl.reportarReplica` (`POST /replicas/{id}/reportes`) | ReplicaNodo (ReportarModal) | PF: ReporteNegocioTest; PV: Moderacion.spec |
| RF-063 | RN-188 a RN-190, D-24 | CU-010, CU-011 | Reporte, MotivoReporte | `ValidarMotivoReporteRule` ("Otro" exige descripción) | ReportarModal | PF: ReporteNegocioTest; PV: Moderacion.spec |
| RF-064 | D-08 | CU-012 | Reporte | `GET /moderacion/reportes` (moderador, agrupados por contenido) y `GET /reportes/mios` | ModeracionView, MisReportesView | PF: ReporteNegocioTest; PV: Moderacion.spec |
| RF-065 | RN-159, RN-191, RN-193, RN-194, D-21 | CU-013 | Moderacion, Kudo, Reporte | ModeracionFachada → `ModeracionNegocioImpl.vetarKudo` + `ValidarMotivoDecisionRule` (`POST /moderacion/kudos/{id}/veto`) | ModeracionView, KudoDetalleView (DecisionModal) | PU: ModeracionDominioTest; PF: ModeracionNegocioTest; PV: Moderacion.spec; PA: Moderacion |
| RF-066 | RN-173, RN-192, RN-195, RN-196 | CU-014 | Moderacion, Replica | `ModeracionNegocioImpl.vetarReplica` (`POST /moderacion/replicas/{id}/veto`) | ModeracionView, ReplicaNodo | PF: ModeracionNegocioTest; PV: Moderacion.spec |
| RF-067 | RN-197 a RN-200, RN-243 a RN-248, INV-033 | CU-013, CU-014, CU-036 | Moderacion | `AsegurarActorTieneRolRule` con `ROLES_MODERADORES` (403) | Menú y ruta según el rol | PF: ModeracionNegocioTest; PV: Moderacion.spec |
| RF-068 | RN-249, RN-250, INV-035 | CU-015 | Moderacion | `ModeracionNegocioImpl.consultarVetos` + `VetoDtoAssembler` (`GET /moderacion/vetos`) | ModeracionView (histórico) | PF: ModeracionNegocioTest; PV: Moderacion.spec |
| RF-093 | RN-203, RN-208, RN-211, RN-239 | CU-036 | Moderacion, Reporte | `ModeracionNegocioImpl.iniciarRevisionKudo/Replica` + `AsegurarContenidoReportadoRule` | ModeracionView | PF: ModeracionNegocioTest; PV: Moderacion.spec |
| RF-094 | RN-212, RN-240, RN-242 | CU-037 | Moderacion, Reporte | `ModeracionNegocioImpl.liberarKudo/Replica` + `AsegurarContenidoEnRevisionRule` | ModeracionView | PF: ModeracionNegocioTest; PV: Moderacion.spec |

## 7. Auditoría, notificaciones, indicadores y configuración

| RF | Reglas / decisiones | CU | Objeto de dominio | Componente (back) | Pantalla (front) | Prueba |
|---|---|---|---|---|---|---|
| RF-069 | RN-096 a RN-100, RN-160, POL-006, D-26 | Todos | Auditoria | `AuditoriaNegocioImpl.registrar`, llamado por cada negocio en la misma transacción (D-26) | — | PF: cada prueba de negocio verifica su acción de auditoría |
| RF-070 | RN-116, RN-128 | CU-038 | Auditoria | AuditoriaFachada → `AuditoriaNegocioImpl.consultar` + `ValidarRangoFechasRule` (`GET /auditoria`) | AuditoriaView | PF: AuditoriaNegocioTest (ámbito por rol, filtros); PV: Auditoria.spec; PA: Auditoria |
| RF-071 | POL-007, D-28 | Todos | — | No existen operaciones de borrado físico (D-28) | — | Revisión: los DAO no tienen `delete` |
| RF-095 | D-27 | CU-039 | Notificacion | `NotificacionNegocioImpl` (notificar, consultarMias, marcarLeida, marcarTodasLeidas) (`/notificaciones`) | CampanaNotificaciones (AppTopbar) | PF: NotificacionNegocioTest; PV: Auditoria.spec; PA: Notificaciones |
| RF-096 | RN-185 | CU-040 | Indicadores (consultas) | ⛔ Fuera del alcance de esta versión (se muestran promedio y cantidad de calificaciones por contenido) | — | — |
| RF-097 | RN-130, RN-131, D-11 | CU-041 | ParametroGlobal | ⛔ Fuera del alcance de esta versión (los parámetros se leen de la base de datos) | — | — |

---

## 8. Cobertura de reglas de negocio por objeto

Muestra dónde se protege cada grupo de reglas del enunciado.

| Grupo de reglas | Objeto(s) responsable(s) | Mecanismo |
|---|---|---|
| RN-001 a RN-038 (exclusividad de roles) | Usuario, AsignacionRol | Estructura (un solo rol vigente) + índice único filtrado (D-13, D-14) |
| RN-039 a RN-052 (Facultades y Programas) | Facultad, ProgramaAcademico, AsignacionRol | Estados de Facultad, historial de asignaciones |
| RN-053 a RN-068 (estudiantes y docentes) | VinculacionPrograma | Vigencias con historial |
| RN-069 a RN-089 (registro, verificación, activación) | Usuario, CodigoVerificacion | Google verifica el correo; reglas `ValidarCorreoInstitucionalRule`, `ValidarCelularRule`, `AsegurarCodigoVigenteRule` + `usuario.intentarActivar()` |
| RN-090 a RN-095, RN-108 (aprobaciones) | SolicitudRegistro, AsignacionRol | `asignacion.puedeDecidir(solicitud)` (experto en información) + reglas de aprobación |
| RN-096 a RN-100, RN-249, RN-250 (auditoría) | Auditoria | Registro desde el negocio en la misma transacción (D-26) |
| RN-101 a RN-111 (información personal) | Usuario | Métodos de actualización con evento |
| RN-112 a RN-131 (ámbitos de autoridad) | AsignacionRol | `AsegurarActorTieneRolRule` + métodos `puedeVerFacultad`, `puedeVerPrograma`, `coordinaPrograma` |
| RN-132 a RN-145 (categorías) | Categoria | `categoria.activar()` / `desactivar()` con validación de estado + reglas de categoría |
| RN-146 a RN-160, RN-201 a RN-205 (Kudos) | Kudo, EstadoContenido | State (D-20) |
| RN-161 a RN-174, RN-206 a RN-209 (réplicas) | Replica | Árbol por `idReplicaPadre` (D-22) + State |
| RN-175 a RN-185 (calificaciones) | Calificacion | `ValidarEstrellasRule` + una calificación activa (índice único filtrado) |
| RN-186 a RN-190, RN-210 a RN-212 (reportes) | Reporte | Estados del reporte |
| RN-191 a RN-200, RN-243 a RN-248 (moderación) | Moderacion | Solo Rector y Coord. Académico (`ROLES_MODERADORES`, D-02) |
| RN-213 a RN-242 (eventos) | Eventos de dominio | [Catálogo de eventos](../01-modelo-dominio/README.md#catálogo-de-eventos-de-dominio) |

El detalle **regla por regla** (RN-001 a RN-250, INV-001 a INV-035 y POL-001 a POL-007) está en el [catálogo de reglas de negocio](reglas-de-negocio.md).
