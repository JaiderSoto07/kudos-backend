package co.edu.uco.kudos.negocio;

import co.edu.uco.kudos.dominio.EstadoCuenta;
import co.edu.uco.kudos.dominio.EstadoFacultad;
import co.edu.uco.kudos.dominio.EstadoSolicitud;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.entidad.AsignacionRolEntidad;
import co.edu.uco.kudos.entidad.FacultadEntidad;
import co.edu.uco.kudos.entidad.ProgramaAcademicoEntidad;
import co.edu.uco.kudos.entidad.SolicitudRegistroEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.negocio.falsos.DAOFactoryFalsa;
import co.edu.uco.kudos.negocio.negocio.SolicitudNegocio;
import co.edu.uco.kudos.negocio.negocio.impl.SolicitudNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosAutorizacionExcepcion;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Negocio de solicitudes: aprobar y rechazar según el rol y el ámbito (CU-022 a CU-025, RF-035 a RF-038, RF-086 a RF-088, D-03, D-15, D-16)")
class SolicitudNegocioTest {

    private static final String MOTIVO = "El celular no corresponde a la persona que solicita.";

    private DAOFactoryFalsa daoFactory;
    private SolicitudNegocio negocio;
    private UUID idFacultad;
    private UUID idOtraFacultad;
    private UUID idPrograma;
    private UUID idOtroPrograma;
    private UUID coordinadorPrograma;
    private UUID coordinadorOtroPrograma;
    private UUID coordinadorFacultad;
    private UUID coordinadorAcademico;
    private UUID rector;

    @BeforeEach
    void preparar() {
        daoFactory = new DAOFactoryFalsa();
        idFacultad = daoFactory.registrarFacultad(EstadoFacultad.ACTIVA);
        idOtraFacultad = daoFactory.registrarFacultad(EstadoFacultad.ACTIVA);
        idPrograma = daoFactory.registrarPrograma(idFacultad);
        idOtroPrograma = daoFactory.registrarPrograma(idOtraFacultad);
        coordinadorPrograma = daoFactory.registrarAutoridad(Rol.COORDINADOR_PROGRAMA, null, idPrograma);
        coordinadorOtroPrograma = daoFactory.registrarAutoridad(Rol.COORDINADOR_PROGRAMA, null, idOtroPrograma);
        coordinadorFacultad = daoFactory.registrarAutoridad(Rol.COORDINADOR_FACULTAD, idFacultad, null);
        coordinadorAcademico = daoFactory.registrarAutoridad(Rol.COORDINADOR_ACADEMICO, null, null);
        rector = daoFactory.registrarAutoridad(Rol.RECTOR, null, null);
        negocio = new SolicitudNegocioImpl(daoFactory);
    }

    private UUID crearSolicitud(Rol rol, UUID facultad, UUID programa, boolean celularVerificado) {
        var idSolicitante = UtilId.generarId();
        daoFactory.getUsuarioDAOFalso().crear(new UsuarioEntidad.Builder()
                .id(idSolicitante)
                .nombres("Sofía")
                .apellidos("Henao")
                .correoInstitucional(idSolicitante + "@gmail.com")
                .correoVerificado(true)
                .celularVerificado(celularVerificado)
                .aprobada(false)
                .estadoCuenta(EstadoCuenta.PENDIENTE_APROBACION.name())
                .build());
        return crearSolicitudDe(idSolicitante, rol, facultad, programa);
    }

    private UUID crearSolicitudDe(UUID idSolicitante, Rol rol, UUID facultad, UUID programa) {
        var idSolicitud = UtilId.generarId();
        var programaEntidad = programa == null ? new ProgramaAcademicoEntidad.Builder().build() : daoFactory.consultarPrograma(programa);
        daoFactory.getSolicitudRegistroDAOFalso().crear(new SolicitudRegistroEntidad.Builder()
                .id(idSolicitud)
                .solicitante(daoFactory.getUsuarioDAOFalso().consultarPorId(idSolicitante))
                .rolSolicitado(rol.name())
                .facultad(new FacultadEntidad.Builder().id(facultad).build())
                .programa(programaEntidad)
                .estado(EstadoSolicitud.PENDIENTE.name())
                .fechaSolicitud(UtilFecha.fechaActual())
                .build());
        return idSolicitud;
    }

    private SolicitudRegistroEntidad solicitud(UUID idSolicitud) {
        return daoFactory.getSolicitudRegistroDAOFalso().consultarPorId(idSolicitud);
    }

    private UsuarioEntidad usuario(UUID idUsuario) {
        return daoFactory.getUsuarioDAOFalso().consultarPorId(idUsuario);
    }

    private List<AsignacionRolEntidad> asignacionesDe(UUID idUsuario) {
        return daoFactory.getAsignacionRolDAOFalso().getAsignaciones().stream()
                .filter(a -> a.getUsuario().getId().equals(idUsuario))
                .toList();
    }

    private void assertRechazo(Class<? extends KudosExcepcion> tipo, String mensaje, Executable operacion) {
        var excepcion = assertThrows(tipo, operacion);
        assertEquals(mensaje, excepcion.getMensajeUsuario());
    }

    @Test
    @DisplayName("Cada autoridad ve solo las solicitudes pendientes de su rol aprobador y de su ámbito (RF-087)")
    void pendientesSegunAmbito() {
        var estudiante = crearSolicitud(Rol.ESTUDIANTE, null, idPrograma, true);
        crearSolicitud(Rol.ESTUDIANTE, null, idOtroPrograma, true);
        var coordPrograma = crearSolicitud(Rol.COORDINADOR_PROGRAMA, null, idPrograma, true);
        crearSolicitud(Rol.COORDINADOR_PROGRAMA, null, idOtroPrograma, true);
        var coordFacultad = crearSolicitud(Rol.COORDINADOR_FACULTAD, idFacultad, null, true);
        var coordAcademico = crearSolicitud(Rol.COORDINADOR_ACADEMICO, null, null, true);

        assertEquals(List.of(estudiante), negocio.consultarPendientes(coordinadorPrograma).stream().map(s -> s.getId()).toList());
        assertEquals(List.of(coordPrograma), negocio.consultarPendientes(coordinadorFacultad).stream().map(s -> s.getId()).toList());
        assertEquals(List.of(coordFacultad), negocio.consultarPendientes(coordinadorAcademico).stream().map(s -> s.getId()).toList());
        assertEquals(List.of(coordAcademico), negocio.consultarPendientes(rector).stream().map(s -> s.getId()).toList());
    }

    @Test
    @DisplayName("Un rol que no aprueba solicitudes no puede consultarlas (403)")
    void sinRolAprobador() {
        var docente = daoFactory.registrarAutoridad(Rol.DOCENTE, null, null);

        assertRechazo(KudosAutorizacionExcepcion.class, CatalogoMensajes.Aprobacion.ACTOR_NO_APRUEBA_SOLICITUDES,
                () -> negocio.consultarPendientes(docente));
    }

    @Test
    @DisplayName("Aprobar a un estudiante le asigna el rol, lo vincula a su Programa, activa la cuenta y le avisa")
    void aprobarEstudiante() {
        var idSolicitud = crearSolicitud(Rol.ESTUDIANTE, null, idPrograma, true);
        var idEstudiante = solicitud(idSolicitud).getSolicitante().getId();

        negocio.aprobar(coordinadorPrograma, idSolicitud);

        assertEquals("APROBADA", solicitud(idSolicitud).getEstado());
        assertEquals(coordinadorPrograma, solicitud(idSolicitud).getDecididaPor().getId());
        var asignacion = asignacionesDe(idEstudiante).getFirst();
        assertEquals("ESTUDIANTE", asignacion.getRol());
        assertTrue(UtilId.esValorDefecto(asignacion.getPrograma().getId()));
        var vinculacion = daoFactory.getVinculacionProgramaDAOFalso().getVinculaciones().getFirst();
        assertEquals(idPrograma, vinculacion.getPrograma().getId());
        assertEquals("ESTUDIANTE", vinculacion.getTipoVinculo());
        assertEquals("ACTIVA", usuario(idEstudiante).getEstadoCuenta());
        assertTrue(usuario(idEstudiante).isAprobada());
        var notificacion = daoFactory.getNotificacionDAOFalso().getNotificaciones().getFirst();
        assertEquals("SOLICITUD_APROBADA", notificacion.getTipo());
        assertEquals(idEstudiante, notificacion.getDestinatario().getId());
        assertEquals("SolicitudAprobada", daoFactory.getAuditoriaDAOFalso().getRegistros().getLast().getAccion());
    }

    @Test
    @DisplayName("Si falta verificar el celular, aprobar deja la cuenta pendiente de verificación (RN-084 a RN-089)")
    void aprobarSinCelularVerificado() {
        var idSolicitud = crearSolicitud(Rol.DOCENTE, null, idPrograma, false);
        var idDocente = solicitud(idSolicitud).getSolicitante().getId();

        negocio.aprobar(coordinadorPrograma, idSolicitud);

        assertEquals("PENDIENTE_VERIFICACION", usuario(idDocente).getEstadoCuenta());
        assertEquals("DOCENTE", daoFactory.getVinculacionProgramaDAOFalso().getVinculaciones().getFirst().getTipoVinculo());
    }

    @Test
    @DisplayName("Aprobar un nuevo Coordinador de Programa sustituye al actual: cierra su cargo y desactiva su cuenta (D-15)")
    void sustituirCoordinadorDePrograma() {
        var idSolicitud = crearSolicitud(Rol.COORDINADOR_PROGRAMA, null, idPrograma, true);
        var idNuevo = solicitud(idSolicitud).getSolicitante().getId();

        negocio.aprobar(coordinadorFacultad, idSolicitud);

        var asignacionAnterior = asignacionesDe(coordinadorPrograma).getFirst();
        assertFalse(UtilFecha.esValorDefecto(asignacionAnterior.getFechaFin()));
        assertEquals(CatalogoMensajes.Aprobacion.MOTIVO_SUSTITUCION, asignacionAnterior.getMotivoFin());
        assertEquals("DESACTIVADA", usuario(coordinadorPrograma).getEstadoCuenta());
        var asignacionNueva = asignacionesDe(idNuevo).getFirst();
        assertEquals(idPrograma, asignacionNueva.getPrograma().getId());
        assertTrue(UtilFecha.esValorDefecto(asignacionNueva.getFechaFin()));
        assertEquals("ACTIVA", usuario(coordinadorOtroPrograma).getEstadoCuenta());
        assertTrue(daoFactory.getVinculacionProgramaDAOFalso().getVinculaciones().isEmpty());
    }

    @Test
    @DisplayName("Aprobar un nuevo Coordinador Académico también es sustitución (decisión a de M2)")
    void sustituirCoordinadorAcademico() {
        var idSolicitud = crearSolicitud(Rol.COORDINADOR_ACADEMICO, null, null, true);

        negocio.aprobar(rector, idSolicitud);

        assertEquals("DESACTIVADA", usuario(coordinadorAcademico).getEstadoCuenta());
        var vigentes = daoFactory.getAsignacionRolDAOFalso().getAsignaciones().stream()
                .filter(a -> "COORDINADOR_ACADEMICO".equals(a.getRol()) && UtilFecha.esValorDefecto(a.getFechaFin()))
                .toList();
        assertEquals(1, vigentes.size());
        assertEquals(solicitud(idSolicitud).getSolicitante().getId(), vigentes.getFirst().getUsuario().getId());
    }

    @Test
    @DisplayName("El Coordinador Académico aprueba Coordinadores de Facultad sin tocar a los de otras Facultades")
    void aprobarCoordinadorDeFacultad() {
        var idSolicitud = crearSolicitud(Rol.COORDINADOR_FACULTAD, idOtraFacultad, null, true);

        negocio.aprobar(coordinadorAcademico, idSolicitud);

        var idNuevo = solicitud(idSolicitud).getSolicitante().getId();
        assertEquals(idOtraFacultad, asignacionesDe(idNuevo).getFirst().getFacultad().getId());
        assertEquals("ACTIVA", usuario(coordinadorFacultad).getEstadoCuenta());
    }

    @Test
    @DisplayName("Fuera del rol aprobador o del ámbito se responde 403 (RF-088, POL-004)")
    void autoridadNoCompetente() {
        var idSolicitud = crearSolicitud(Rol.ESTUDIANTE, null, idPrograma, true);
        var idCoordinadorDePrograma = crearSolicitud(Rol.COORDINADOR_PROGRAMA, null, idOtroPrograma, true);

        assertRechazo(KudosAutorizacionExcepcion.class, CatalogoMensajes.Aprobacion.AUTORIDAD_NO_COMPETENTE,
                () -> negocio.aprobar(coordinadorOtroPrograma, idSolicitud));
        assertRechazo(KudosAutorizacionExcepcion.class, CatalogoMensajes.Aprobacion.AUTORIDAD_NO_COMPETENTE,
                () -> negocio.aprobar(coordinadorFacultad, idSolicitud));
        assertRechazo(KudosAutorizacionExcepcion.class, CatalogoMensajes.Aprobacion.AUTORIDAD_NO_COMPETENTE,
                () -> negocio.aprobar(coordinadorFacultad, idCoordinadorDePrograma));
        assertEquals("PENDIENTE", solicitud(idSolicitud).getEstado());
    }

    @Test
    @DisplayName("Nadie decide su propia solicitud, ni una que no existe o ya fue decidida (RN-108)")
    void validacionesDeLaDecision() {
        var propia = crearSolicitudDe(coordinadorPrograma, Rol.ESTUDIANTE, null, idPrograma);
        var idSolicitud = crearSolicitud(Rol.ESTUDIANTE, null, idPrograma, true);
        var inexistente = UtilId.generarId();
        negocio.aprobar(coordinadorPrograma, idSolicitud);

        assertRechazo(KudosAutorizacionExcepcion.class, CatalogoMensajes.Aprobacion.SOLICITUD_PROPIA,
                () -> negocio.aprobar(coordinadorPrograma, propia));
        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Aprobacion.SOLICITUD_NO_EXISTE,
                () -> negocio.aprobar(coordinadorPrograma, inexistente));
        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Aprobacion.SOLICITUD_YA_DECIDIDA,
                () -> negocio.rechazar(coordinadorPrograma, idSolicitud, MOTIVO));
        assertTrue(negocio.consultarPendientes(coordinadorPrograma).isEmpty());
    }

    @Test
    @DisplayName("Rechazar exige motivo de 10 a 250 caracteres, avisa con el motivo y no asigna rol (RF-086)")
    void rechazar() {
        var idSolicitud = crearSolicitud(Rol.ESTUDIANTE, null, idPrograma, true);
        var idEstudiante = solicitud(idSolicitud).getSolicitante().getId();

        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Aprobacion.MOTIVO_RECHAZO_OBLIGATORIO,
                () -> negocio.rechazar(coordinadorPrograma, idSolicitud, "  "));
        assertRechazo(KudosExcepcion.class, CatalogoMensajes.Aprobacion.MOTIVO_RECHAZO_LONGITUD_INVALIDA,
                () -> negocio.rechazar(coordinadorPrograma, idSolicitud, "Corto"));

        negocio.rechazar(coordinadorPrograma, idSolicitud, " " + MOTIVO + " ");

        assertEquals("RECHAZADA", solicitud(idSolicitud).getEstado());
        assertEquals(MOTIVO, solicitud(idSolicitud).getMotivoRechazo());
        assertEquals("PENDIENTE_APROBACION", usuario(idEstudiante).getEstadoCuenta());
        assertTrue(asignacionesDe(idEstudiante).isEmpty());
        var notificacion = daoFactory.getNotificacionDAOFalso().getNotificaciones().getFirst();
        assertEquals("SOLICITUD_RECHAZADA", notificacion.getTipo());
        assertTrue(notificacion.getMensaje().contains(MOTIVO));
    }

    @Test
    @DisplayName("Al llegar una solicitud se avisa solo a la autoridad competente (SOLICITUD_PENDIENTE)")
    void avisarALaAutoridad() {
        var idSolicitud = crearSolicitud(Rol.ESTUDIANTE, null, idPrograma, true);
        var idSinCoordinador = crearSolicitud(Rol.ESTUDIANTE, null, daoFactory.registrarPrograma(idFacultad), true);

        negocio.notificarAutoridades(idSolicitud);
        negocio.notificarAutoridades(idSinCoordinador);

        var notificaciones = daoFactory.getNotificacionDAOFalso().getNotificaciones();
        assertEquals(1, notificaciones.size());
        assertEquals(coordinadorPrograma, notificaciones.getFirst().getDestinatario().getId());
        assertEquals("SOLICITUD_PENDIENTE", notificaciones.getFirst().getTipo());
        assertTrue(notificaciones.getFirst().getMensaje().contains("Sofía Henao"));
        assertTrue(notificaciones.getFirst().getMensaje().contains("Estudiante"));
    }

    @ParameterizedTest(name = "{0} tiene nombre legible")
    @EnumSource(Rol.class)
    @DisplayName("Cada rol tiene un nombre legible para los mensajes y solo los tres coordinadores son coordinadores")
    void nombresDeRoles(Rol rol) {
        assertFalse(rol.nombreLegible().isBlank());
        assertEquals(rol.name().startsWith("COORDINADOR"), rol.esCoordinador());
    }
}
