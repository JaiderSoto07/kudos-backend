# Matriz de trazabilidad

[Inicio](../../README.md) · [Requisitos](requisitos.md) · [Reglas de negocio](reglas-de-negocio.md) · [Casos de uso](casos-de-uso.md) · [Modelo anémico](../01-modelo-dominio/README.md)

Relaciona **requisito → reglas → caso de uso → objeto de dominio → componente → prueba → evidencia**, como pide el documento 07 §4 (RNF-034, RNF-035).

> **Estado:** las columnas *Componente (back)* y *Prueba* tienen los nombres **planeados** según la arquitectura del curso. Se confirman al implementar, y la columna *Evidencia* se llena en [`07-calidad-y-pruebas`](../07-calidad-y-pruebas/README.md).

**Componentes**: `XxxFachada` es la fachada del módulo, que llama a `XxxNegocio` y a sus reglas. Las pantallas del front están en `modules/<modulo>/views`.
**Pruebas**: **PU** = prueba unitaria del dominio o de una regla (JUnit). **PF** = prueba de fachada o negocio con DAO falso. **PA** = petición en la colección Bruno. **PV** = prueba del front (Vitest).

---

## 1. Usuarios, identidad y acceso

| RF | Reglas / decisiones | CU | Objeto de dominio | Componente (back) | Pantalla (front) | Prueba |
|---|---|---|---|---|---|---|
| RF-001 | RN-069 a RN-076, D-17 | CU-016 | Usuario, SolicitudRegistro | UsuarioFachada.registrar | RegistroView | PF: registrar usuario válido; PA: Registro |
| RF-002 | RN-069, RN-071, POL-001, D-12 | CU-016 | CorreoInstitucional | — (objeto de valor) | RegistroView | PU: CorreoInstitucionalTest (dominio inválido, mayúsculas, espacios) |
| RF-003 | RN-070, RN-072, POL-002, D-12 | CU-016 | NumeroCelular | — (objeto de valor) | RegistroView | PU: NumeroCelularTest |
| RF-004 | RN-073 | CU-016, CU-020 | Usuario | AsegurarCorreoNoRegistradoRule | — | PF: correo duplicado rechazado |
| RF-005 | RN-074 | CU-016, CU-020 | Usuario | AsegurarCelularNoRegistradoRule | — | PF: celular duplicado rechazado |
| RF-006 | RN-077, RN-079, D-10 | CU-017 | CodigoVerificacion, Usuario | VerificacionFachada.verificar | VerificacionView | PU: CodigoVerificacionTest (vencido, intentos, usado) |
| RF-007 | RN-078, RN-080, D-10 | CU-017 | CodigoVerificacion, Usuario | VerificacionFachada.verificar | VerificacionView | PU: CodigoVerificacionTest |
| RF-008 | RN-101, RN-112 a RN-127 | CU-020, CU-034 | Usuario | UsuarioFachada.consultar | PerfilView | PA: Consultar mi perfil |
| RF-009 | RN-102, RN-104, RN-105, RN-107, RN-109 | CU-020 | Usuario | UsuarioFachada.actualizarDatosPersonales | PerfilView | PU: UsuarioTest.actualizarDatos |
| RF-010 | RN-081, RN-110 | CU-020 | Usuario | UsuarioFachada.actualizarDatosPersonales | PerfilView | PU: cambiarCorreo desverifica y desactiva |
| RF-011 | RN-082, RN-111 | CU-020 | Usuario | UsuarioFachada.actualizarDatosPersonales | PerfilView | PU: cambiarCelular desverifica y desactiva |
| RF-078 | RN-085 a RN-089, POL-003, D-09 | CU-018 | Usuario | AutenticacionFachada.iniciarSesion | LoginView | PU: puedeIniciarSesion por estado; PA: Login |
| RF-079 | RNF-001 | CU-019 | — | SeguridadConfiguracion | AppTopbar | PA: Logout |
| RF-080 | D-10 | CU-017 | CodigoVerificacion | VerificacionFachada.reenviarCodigo | VerificacionView | PF: reenviar invalida anteriores |
| RF-081 | RN-103 | CU-020 | Usuario | UsuarioFachada.consultarEstadoValidaciones | PerfilView | PA |
| RF-082 | RN-106 | CU-021 | Auditoria | AuditoriaFachada.consultarActividadPropia | MiActividadView | PA |

## 2. Roles y aprobaciones

| RF | Reglas / decisiones | CU | Objeto de dominio | Componente (back) | Pantalla (front) | Prueba |
|---|---|---|---|---|---|---|
| RF-012 | RN-090 a RN-095, D-17 | CU-016, CU-022, CU-024, CU-025 | Rol, AsignacionRol, SolicitudRegistro | SolicitudRegistroFachada, UsuarioFachada | SolicitudesView | PU: RolTest.rolAprobador |
| RF-013 | RN-001 a RN-004, INV-005, D-13 | CU-022 | Usuario, AsignacionRol | AsignacionRol.asignar + AsegurarRolUnicoVigenteRule | — | PU: no permite segunda asignación vigente |
| RF-014 | RN-005 a RN-038, INV-001 a INV-006, D-14 | CU-022, CU-024 | AsignacionRol | AsegurarRolUnicoVigenteRule | — | PF: segundo Rector / Coord. Académico rechazado |
| RF-035 | RN-090, D-03 | CU-022 | SolicitudRegistro | SolicitudRegistroFachada.aprobar | SolicitudesView | PF: Coord. Programa aprueba estudiante de su programa |
| RF-036 | RN-091, RN-117 | CU-022 | SolicitudRegistro | SolicitudRegistroFachada.aprobar | SolicitudesView | PF: Coord. Facultad fuera de su Facultad rechazado |
| RF-037 | RN-092, RN-121 | CU-022 | SolicitudRegistro | SolicitudRegistroFachada.aprobar | SolicitudesView | PF |
| RF-038 | RN-093, RN-126 | CU-022 | SolicitudRegistro | SolicitudRegistroFachada.aprobar | SolicitudesView | PF |
| RF-039 | RN-094, RN-129, INV-001 | CU-024 | Usuario, AsignacionRol | UsuarioFachada.registrarRector | GestionUsuariosView | PF: Rector duplicado rechazado |
| RF-040 | RN-095, RN-129 | CU-025 | Usuario, AsignacionRol | UsuarioFachada.registrarAdministrador | GestionUsuariosView | PA |
| RF-086 | D-17 | CU-023 | SolicitudRegistro | SolicitudRegistroFachada.rechazar | SolicitudesView | PU: rechazar exige motivo |
| RF-087 | POL-004 | CU-022, CU-023 | SolicitudRegistro, PoliticaAmbito | SolicitudRegistroFachada.consultarPendientes | SolicitudesView | PF: solo solicitudes del ámbito |
| RF-088 | RN-108, POL-004, POL-005 | CU-022 | SolicitudRegistro | AsegurarAutoridadCompetenteRule | — | PU: autoaprobación rechazada |
| RF-089 | RN-216, RN-217, D-07 | CU-026, CU-027 | Usuario | UsuarioFachada.suspender / reactivar / desactivar | GestionUsuariosView | PU: transiciones de EstadoCuenta |

## 3. Estructura organizacional

| RF | Reglas / decisiones | CU | Objeto de dominio | Componente (back) | Pantalla (front) | Prueba |
|---|---|---|---|---|---|---|
| RF-015 | RN-039, RN-040, RN-045, D-04, D-05 | CU-028 | Facultad | FacultadFachada.crear | FacultadesView | PU: FacultadTest (nace EN_CONFORMACION) |
| RF-016 | RN-122 | CU-034 | Facultad | FacultadFachada.consultar | FacultadesView | PA |
| RF-017 | D-04 | CU-029 | Facultad | FacultadFachada.modificar | FacultadesView | PF: nombre duplicado |
| RF-018 | RN-042 | CU-034 | AsignacionRol | FacultadFachada.consultarCoordinadorVigente | FacultadDetalleView | PA |
| RF-019 | RN-043, RN-044 | CU-034 | AsignacionRol | FacultadFachada.consultarHistorialCoordinadores | FacultadDetalleView | PF: historial tras reemplazo |
| RF-083 | D-05, D-28 | CU-029 | Facultad | FacultadFachada.inactivar | FacultadesView | PU: no inactiva con programas activos |
| RF-020 | RN-046 a RN-049, D-05 | CU-030 | ProgramaAcademico, Facultad | ProgramaAcademicoFachada.crear | ProgramasView | PU: primer programa activa la Facultad |
| RF-021 | RN-118 | CU-034 | ProgramaAcademico | ProgramaAcademicoFachada.consultar | ProgramasView | PA |
| RF-022 | D-04 | CU-031 | ProgramaAcademico | ProgramaAcademicoFachada.modificar | ProgramasView | PF |
| RF-023 | RN-050 | CU-034 | AsignacionRol | ProgramaAcademicoFachada.consultarCoordinadorVigente | ProgramaDetalleView | PA |
| RF-024 | RN-051, RN-052 | CU-034 | AsignacionRol | ProgramaAcademicoFachada.consultarHistorialCoordinadores | ProgramaDetalleView | PF |
| RF-084 | D-28 | CU-031 | ProgramaAcademico | ProgramaAcademicoFachada.inactivar | ProgramasView | PU |
| RF-025 | RN-058, RN-090 | CU-016 | SolicitudRegistro | UsuarioFachada.registrar | RegistroView | PA |
| RF-026 | RN-053, RN-112 | CU-032 | VinculacionPrograma | VinculacionProgramaFachada.vincular | ProgramaDetalleView | PF: vínculo duplicado rechazado |
| RF-027 | RN-054 | CU-034 | VinculacionPrograma | VinculacionProgramaFachada.consultarPorUsuario | PerfilView | PA |
| RF-028 | RN-055 | CU-034 | VinculacionPrograma | VinculacionProgramaFachada.consultarPorPrograma | ProgramaDetalleView | PA |
| RF-029 | RN-056, RN-057, INV-012, INV-013 | CU-032, CU-033 | VinculacionPrograma | VinculacionProgramaFachada | — | PU: desvincular conserva registro |
| RF-030 | RN-066, D-03 | CU-016 | SolicitudRegistro | UsuarioFachada.registrar | RegistroView | PA |
| RF-031 | RN-061, RN-113 | CU-032 | VinculacionPrograma | VinculacionProgramaFachada.vincular | ProgramaDetalleView | PF |
| RF-032 | RN-062 | CU-034 | VinculacionPrograma | VinculacionProgramaFachada.consultarPorUsuario | PerfilView | PA |
| RF-033 | RN-063 | CU-034 | VinculacionPrograma | VinculacionProgramaFachada.consultarPorPrograma | ProgramaDetalleView | PA |
| RF-034 | RN-064, RN-065, INV-015, INV-016 | CU-032, CU-033 | VinculacionPrograma | VinculacionProgramaFachada | — | PU |
| RF-085 | RN-057, RN-065, RN-114, RN-115, D-16 | CU-033 | VinculacionPrograma, Usuario | VinculacionProgramaFachada.desvincular | ProgramaDetalleView | PF: suspende si no quedan vínculos |
| RF-072 a RF-077 | RN-054, RN-062, INV-001, INV-002, INV-009, INV-010 | CU-034 | AsignacionRol, VinculacionPrograma | OrganizacionFachada (consultas) | EstructuraView | PA |

## 4. Categorías

| RF | Reglas / decisiones | CU | Objeto de dominio | Componente (back) | Pantalla (front) | Prueba |
|---|---|---|---|---|---|---|
| RF-041 | RN-134 a RN-140, D-01 | CU-035 | Categoria | CategoriaFachada.crear | CategoriasView | PF: Coord. Facultad no puede crear |
| RF-042 | RN-141, RN-145 | CU-035 | Categoria | CategoriaFachada.modificar | CategoriasView | PF |
| RF-043 | RN-142 | CU-035 | Categoria | CategoriaFachada.activar | CategoriasView | PU: CategoriaTest.activar |
| RF-044 | RN-143, RN-144 | CU-035 | Categoria | CategoriaFachada.desactivar | CategoriasView | PU: CategoriaTest.desactivar |
| RF-045 | INV-018 | CU-035, CU-001 | Categoria | CategoriaFachada.consultar | CategoriasView, CrearKudo | PA |

## 5. Kudos y réplicas

| RF | Reglas / decisiones | CU | Objeto de dominio | Componente (back) | Pantalla (front) | Prueba |
|---|---|---|---|---|---|---|
| RF-046 | RN-146 a RN-150, D-19, D-20 | CU-001 | Kudo, Categoria, Usuario | KudoFachada.crear | CrearKudoModal | PU: KudoTest (autoreconocimiento, categoría inactiva, publicar) |
| RF-047 | RN-154, RN-155, RN-229, D-25 | CU-002 | Kudo | KudoFachada.consultar | KudoDetalleView | PF: incrementa visualizaciones |
| RF-048 a RF-050 | — | CU-003 | Kudo | KudoFachada.consultarHistorial | HistorialKudosView | PA |
| RF-051 | RN-149, RN-145, INV-017 | CU-001 | Kudo, Categoria | KudoFachada.crear | — | PF: Kudo conserva categoría desactivada |
| RF-052 | RN-152, RN-153, RN-170, INV-024 | CU-001, CU-004 | ContenidoTexto | — (objeto de valor) | KudoCard ("Ver más") | PU: ContenidoTextoTest (texto de 100.000 caracteres intacto) |
| RF-090 | RN-155, RNF-010 | CU-042 | Kudo | KudoFachada.consultarMuro | MuroView | PA: paginación y filtros |
| RF-053 | RN-156, RN-161 | CU-004 | Replica, Kudo | ReplicaFachada.responderKudo | ConversacionArbol | PU: no responde Kudo vetado |
| RF-054 | RN-162 | CU-005 | Replica | ReplicaFachada.responderReplica | ConversacionArbol | PU: padre de otro Kudo rechazado |
| RF-055 | D-21, D-22 | CU-006 | Conversacion (Composite) | ReplicaFachada.consultarConversacion | ConversacionArbol | PU: arma árbol de varios niveles |
| RF-056 | RN-163 a RN-167 | CU-005, CU-006 | Replica | — | ConversacionArbol | PU: profundidad 50 sin error |

## 6. Calificaciones, reportes y moderación

| RF | Reglas / decisiones | CU | Objeto de dominio | Componente (back) | Pantalla (front) | Prueba |
|---|---|---|---|---|---|---|
| RF-057 | RN-157, RN-175, D-23 | CU-007 | Calificacion, Kudo | CalificacionFachada.calificarKudo | CalificacionEstrellas | PU: no califica contenido propio |
| RF-058 | RN-171, RN-176 | CU-008 | Calificacion, Replica | CalificacionFachada.calificarReplica | CalificacionEstrellas | PU |
| RF-059 | RN-177, INV-030 | CU-007, CU-008 | Estrellas | — (objeto de valor) | CalificacionEstrellas | PU: EstrellasTest (0 y 6 rechazados) |
| RF-060 | RN-178 a RN-181, INV-031, INV-032 | CU-007, CU-008 | Calificacion | CalificacionFachada | — | PF: segunda calificación modifica la existente |
| RF-091 | RN-237, D-23 | CU-007, CU-008 | Calificacion | CalificacionFachada | — | PU: modificar registra valor anterior |
| RF-092 | RN-185, RN-238 | CU-007, CU-008 | Kudo, Replica (resumen de calificaciones) | IndicadorSuscriptor | — | PU: promedio recalculado |
| RF-061 | RN-158, RN-186, RN-202 | CU-010 | Reporte, Kudo | ReporteFachada.reportarKudo | ReportarModal | PU: VISIBLE → REPORTADO |
| RF-062 | RN-172, RN-187, RN-207 | CU-011 | Reporte, Replica | ReporteFachada.reportarReplica | ReportarModal | PU |
| RF-063 | RN-188 a RN-190, D-24 | CU-010, CU-011 | Reporte, MotivoReporte | ReporteFachada | ReportarModal | PU: OTRO sin descripción rechazado |
| RF-064 | D-08 | CU-012 | Reporte | ReporteFachada.consultar | ReportesView | PF: no moderador solo ve los suyos |
| RF-065 | RN-159, RN-191, RN-193, RN-194, D-21 | CU-013 | Moderacion, Kudo, Reporte | ModeracionFachada.vetarKudo | ReportesView, KudoDetalleView | PU: EstadoContenido (State) |
| RF-066 | RN-173, RN-192, RN-195, RN-196 | CU-014 | Moderacion, Replica | ModeracionFachada.vetarReplica | ConversacionArbol | PU |
| RF-067 | RN-197 a RN-200, RN-243 a RN-248, INV-033 | CU-013, CU-014, CU-036 | PoliticaModeracion | AsegurarModeradorRule | — | PF: Admin, Coord. Facultad, Docente rechazados |
| RF-068 | RN-249, RN-250, INV-035 | CU-015 | Moderacion | ModeracionFachada.consultarHistoricoVetos | HistoricoVetosView | PA |
| RF-093 | RN-203, RN-208, RN-211, RN-239 | CU-036 | Moderacion, Reporte | ModeracionFachada.iniciarRevision | ReportesView | PU: REPORTADO → EN_REVISION |
| RF-094 | RN-212, RN-240, RN-242 | CU-037 | Moderacion, Reporte | ModeracionFachada.liberar | ReportesView | PU: EN_REVISION → VISIBLE, reportes rechazados |

## 7. Auditoría, notificaciones, indicadores y configuración

| RF | Reglas / decisiones | CU | Objeto de dominio | Componente (back) | Pantalla (front) | Prueba |
|---|---|---|---|---|---|---|
| RF-069 | RN-096 a RN-100, RN-160, POL-006, D-26 | Todos | Auditoria, EventoDominio | PublicadorEventos, AuditoriaSuscriptor | — | PU: cada evento genera auditoría |
| RF-070 | RN-116, RN-128 | CU-038 | Auditoria | AuditoriaFachada.consultar | AuditoriaView | PF: filtro por ámbito |
| RF-071 | POL-007, D-28 | Todos | — | (no existen operaciones de borrado físico) | — | Revisión: no hay DELETE en los DAO de negocio |
| RF-095 | D-27 | CU-039 | Notificacion | NotificacionSuscriptor, NotificacionFachada | NotificacionesView | PU: marcarLeida |
| RF-096 | RN-185 | CU-040 | Indicadores (consultas) | IndicadorFachada | IndicadoresView | PA |
| RF-097 | RN-130, RN-131, D-11 | CU-041 | ParametroGlobal | ParametroGlobalFachada | ParametrosView | PU: valor no entero rechazado |

---

## 8. Cobertura de reglas de negocio por objeto

Muestra dónde se protege cada grupo de reglas del enunciado.

| Grupo de reglas | Objeto(s) responsable(s) | Mecanismo |
|---|---|---|
| RN-001 a RN-038 (exclusividad de roles) | Usuario, AsignacionRol | Estructura (un solo rol vigente) + índice único filtrado (D-13, D-14) |
| RN-039 a RN-052 (Facultades y Programas) | Facultad, ProgramaAcademico, AsignacionRol | Estados de Facultad, historial de asignaciones |
| RN-053 a RN-068 (estudiantes y docentes) | VinculacionPrograma | Vigencias con historial |
| RN-069 a RN-089 (registro, verificación, activación) | Usuario, CorreoInstitucional, NumeroCelular, CodigoVerificacion | Objetos de valor + `Usuario.intentarActivar()` |
| RN-090 a RN-095, RN-108 (aprobaciones) | SolicitudRegistro, Rol | El rol conoce a su aprobador + política de ámbito |
| RN-096 a RN-100, RN-249, RN-250 (auditoría) | Auditoria | Observer sobre eventos de dominio (D-26) |
| RN-101 a RN-111 (información personal) | Usuario | Métodos de actualización con evento |
| RN-112 a RN-131 (ámbitos de autoridad) | PoliticaAmbito | Política de autorización por rol y ámbito |
| RN-132 a RN-145 (categorías) | Categoria, PoliticaGestionCategorias | Activar o desactivar con validación de estado |
| RN-146 a RN-160, RN-201 a RN-205 (Kudos) | Kudo, EstadoContenido | State (D-20) |
| RN-161 a RN-174, RN-206 a RN-209 (réplicas) | Replica, Conversacion | Composite (D-22) + State |
| RN-175 a RN-185 (calificaciones) | Calificacion, Estrellas | Objeto de valor + unicidad |
| RN-186 a RN-190, RN-210 a RN-212 (reportes) | Reporte | Estados del reporte |
| RN-191 a RN-200, RN-243 a RN-248 (moderación) | Moderacion, PoliticaModeracion | Solo Rector y Coord. Académico (D-02) |
| RN-213 a RN-242 (eventos) | Eventos de dominio | [Catálogo de eventos](../01-modelo-dominio/README.md#catálogo-de-eventos-de-dominio) |

El detalle **regla por regla** (RN-001 a RN-250, INV-001 a INV-035 y POL-001 a POL-007) está en el [catálogo de reglas de negocio](reglas-de-negocio.md).
