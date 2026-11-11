package co.edu.uco.kudos.negocio;

import co.edu.uco.kudos.dominio.CategoriaDominio;
import co.edu.uco.kudos.dominio.EstadoCuenta;
import co.edu.uco.kudos.dominio.KudoDominio;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.entidad.KudoEntidad;
import co.edu.uco.kudos.entidad.ReporteEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.negocio.falsos.DAOFactoryFalsa;
import co.edu.uco.kudos.negocio.negocio.ModeracionNegocio;
import co.edu.uco.kudos.negocio.negocio.ReporteNegocio;
import co.edu.uco.kudos.negocio.negocio.impl.KudoNegocioImpl;
import co.edu.uco.kudos.negocio.negocio.impl.ModeracionNegocioImpl;
import co.edu.uco.kudos.negocio.negocio.impl.ReplicaNegocioImpl;
import co.edu.uco.kudos.negocio.negocio.impl.ReporteNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Negocio de moderación: revisar, vetar, liberar e histórico de vetos (CU-013 a CU-015, CU-036, CU-037, D-21, D-24)")
class ModeracionNegocioTest {

    private static final String MOTIVO_VETO = "Contiene burlas hacia la destinataria";
    private static final String MOTIVO_LIBERACION = "Revisado: no incumple las normas de convivencia";

    private DAOFactoryFalsa daoFactory;
    private ModeracionNegocio negocio;
    private ReporteNegocio reportes;
    private UUID ana;
    private UUID luis;
    private UUID marta;
    private UUID pedro;
    private UUID rector;
    private UUID coordinador;
    private UUID idKudo;
    private UUID idReplica;

    @BeforeEach
    void preparar() {
        daoFactory = new DAOFactoryFalsa();
        ana = daoFactory.registrarPersona("ana@gmail.com", Rol.ESTUDIANTE, EstadoCuenta.ACTIVA);
        luis = daoFactory.registrarPersona("luis@gmail.com", Rol.DOCENTE, EstadoCuenta.ACTIVA);
        marta = daoFactory.registrarPersona("marta@gmail.com", Rol.ESTUDIANTE, EstadoCuenta.ACTIVA);
        pedro = daoFactory.registrarPersona("pedro@gmail.com", Rol.ESTUDIANTE, EstadoCuenta.ACTIVA);
        rector = daoFactory.registrarPersona("rector@gmail.com", Rol.RECTOR, EstadoCuenta.ACTIVA);
        coordinador = daoFactory.registrarPersona("coordinador@gmail.com", Rol.COORDINADOR_ACADEMICO, EstadoCuenta.ACTIVA);
        var liderazgo = daoFactory.registrarCategoria("Liderazgo", true);
        new KudoNegocioImpl(daoFactory).crear(ana, new KudoDominio.Builder()
                .destinatario(new UsuarioDominio.Builder().id(luis).build())
                .categoria(new CategoriaDominio.Builder().id(liderazgo).build())
                .contenido("Gracias por tu apoyo en el semillero.")
                .build());
        idKudo = daoFactory.getKudoDAOFalso().getKudos().getFirst().getId();
        new ReplicaNegocioImpl(daoFactory).responderKudo(luis, idKudo, "Gracias, Ana.");
        idReplica = daoFactory.getReplicaDAOFalso().getReplicas().getFirst().getId();
        daoFactory.getNotificacionDAOFalso().getNotificaciones().clear();
        negocio = new ModeracionNegocioImpl(daoFactory);
        reportes = new ReporteNegocioImpl(daoFactory);
    }

    private void assertRechazo(String mensaje, Executable operacion) {
        var excepcion = assertThrows(KudosExcepcion.class, operacion);
        assertEquals(mensaje, excepcion.getMensajeUsuario());
    }

    private String estadoKudo() {
        return daoFactory.getKudoDAOFalso().consultarPorId(idKudo).getEstado();
    }

    private String estadoReplica() {
        return daoFactory.getReplicaDAOFalso().consultarPorId(idReplica).getEstado();
    }

    private long reportesEn(String estado) {
        return daoFactory.getReporteDAOFalso().getReportes().stream().filter(reporte -> estado.equals(reporte.getEstado())).count();
    }

    @Test
    @DisplayName("Iniciar la revisión crea la moderación EN_CURSO y pasa el Kudo y sus reportes a EN_REVISION")
    void iniciarRevision() {
        reportes.reportarKudo(marta, idKudo, "SPAM", "");
        reportes.reportarKudo(pedro, idKudo, "ACOSO", "");

        negocio.iniciarRevisionKudo(rector, idKudo);

        assertEquals("EN_REVISION", estadoKudo());
        var moderacion = daoFactory.getModeracionDAOFalso().getModeraciones().getFirst();
        assertEquals("EN_CURSO", moderacion.getEstado());
        assertEquals(rector, moderacion.getModerador().getId());
        assertEquals(2, reportesEn("EN_REVISION"));
        assertTrue(daoFactory.getReporteDAOFalso().getReportes().stream().allMatch(reporte -> moderacion.getId().equals(reporte.getModeracion().getId())));
        assertEquals("RevisionIniciada", daoFactory.getAuditoriaDAOFalso().getRegistros().getLast().getAccion());
    }

    @Test
    @DisplayName("Solo un moderador revisa, y solo un contenido REPORTADO sin otra revisión en curso")
    void rechazosRevision() {
        assertRechazo(CatalogoMensajes.Moderacion.ACTOR_NO_ES_MODERADOR, () -> negocio.iniciarRevisionKudo(luis, idKudo));
        assertRechazo(CatalogoMensajes.Moderacion.CONTENIDO_SIN_REPORTES, () -> negocio.iniciarRevisionKudo(rector, idKudo));
        assertRechazo(CatalogoMensajes.Kudo.KUDO_NO_EXISTE, () -> negocio.iniciarRevisionKudo(rector, UtilId.generarId()));
        assertRechazo(CatalogoMensajes.Replica.REPLICA_NO_EXISTE, () -> negocio.iniciarRevisionReplica(rector, UtilId.generarId()));

        reportes.reportarKudo(marta, idKudo, "SPAM", "");
        negocio.iniciarRevisionKudo(coordinador, idKudo);
        assertRechazo(CatalogoMensajes.Moderacion.REVISION_EN_CURSO, () -> negocio.iniciarRevisionKudo(rector, idKudo));
        assertEquals(1, daoFactory.getModeracionDAOFalso().getModeraciones().size());
    }

    @Test
    @DisplayName("Vetar tras la revisión cierra esa moderación, acepta los reportes, avisa al autor y lo audita")
    void vetarTrasRevision() {
        reportes.reportarKudo(marta, idKudo, "ACOSO", "");
        negocio.iniciarRevisionKudo(coordinador, idKudo);

        negocio.vetarKudo(rector, idKudo, MOTIVO_VETO);

        assertEquals("VETADO", estadoKudo());
        assertEquals(1, daoFactory.getModeracionDAOFalso().getModeraciones().size());
        var moderacion = daoFactory.getModeracionDAOFalso().getModeraciones().getFirst();
        assertEquals("CERRADA", moderacion.getEstado());
        assertEquals("VETAR", moderacion.getDecision());
        assertEquals(MOTIVO_VETO, moderacion.getMotivoDecision());
        assertEquals(rector, moderacion.getModerador().getId());
        assertFalse(UtilFecha.esValorDefecto(moderacion.getFechaCierre()));
        assertEquals(1, reportesEn("ACEPTADO"));
        var notificacion = daoFactory.getNotificacionDAOFalso().getNotificaciones().getFirst();
        assertEquals("CONTENIDO_VETADO", notificacion.getTipo());
        assertEquals(ana, notificacion.getDestinatario().getId());
        assertTrue(notificacion.getMensaje().endsWith(MOTIVO_VETO));
        var auditoria = daoFactory.getAuditoriaDAOFalso().getRegistros().getLast();
        assertEquals("KudoVetado", auditoria.getAccion());
        assertEquals("EN_REVISION", auditoria.getEstadoAnterior());
        assertEquals(MOTIVO_VETO, auditoria.getMotivo());
    }

    @Test
    @DisplayName("Un veto directo crea y cierra la moderación; acepta los reportes pendientes y salta los ya resueltos (CU-013)")
    void vetoDirecto() {
        negocio.vetarReplica(rector, idReplica, MOTIVO_VETO);

        assertEquals("VETADO", estadoReplica());
        var moderacion = daoFactory.getModeracionDAOFalso().getModeraciones().getFirst();
        assertEquals("CERRADA", moderacion.getEstado());
        assertEquals(idReplica, moderacion.getReplica().getId());
        assertEquals(luis, daoFactory.getNotificacionDAOFalso().getNotificaciones().getFirst().getDestinatario().getId());
        assertEquals("ReplicaVetada", daoFactory.getAuditoriaDAOFalso().getRegistros().getLast().getAccion());

        reportes.reportarKudo(marta, idKudo, "SPAM", "");
        negocio.iniciarRevisionKudo(rector, idKudo);
        negocio.liberarKudo(rector, idKudo, MOTIVO_LIBERACION);
        reportes.reportarKudo(pedro, idKudo, "ACOSO", "");

        negocio.vetarKudo(coordinador, idKudo, MOTIVO_VETO);

        assertEquals("VETADO", estadoKudo());
        assertEquals(1, reportesEn("RECHAZADO"));
        assertEquals(1, reportesEn("ACEPTADO"));
    }

    @Test
    @DisplayName("Al vetar se exige motivo de 10 a 500 caracteres y no se veta dos veces; el aviso largo se recorta a 500")
    void rechazosVeto() {
        assertRechazo(CatalogoMensajes.Moderacion.MOTIVO_DECISION_OBLIGATORIO, () -> negocio.vetarKudo(rector, idKudo, " "));
        assertRechazo(CatalogoMensajes.Moderacion.MOTIVO_DECISION_LONGITUD_INVALIDA, () -> negocio.vetarKudo(rector, idKudo, "Feo"));
        assertRechazo(CatalogoMensajes.Moderacion.MOTIVO_DECISION_LONGITUD_INVALIDA, () -> negocio.vetarKudo(rector, idKudo, "a".repeat(501)));
        assertRechazo(CatalogoMensajes.Moderacion.ACTOR_NO_ES_MODERADOR, () -> negocio.vetarKudo(marta, idKudo, MOTIVO_VETO));
        assertEquals("VISIBLE", estadoKudo());

        negocio.vetarKudo(rector, idKudo, "a".repeat(500));
        var mensaje = daoFactory.getNotificacionDAOFalso().getNotificaciones().getFirst().getMensaje();
        assertEquals(500, mensaje.length());
        assertTrue(mensaje.endsWith("..."));

        assertRechazo(CatalogoMensajes.Moderacion.CONTENIDO_YA_VETADO, () -> negocio.vetarKudo(rector, idKudo, MOTIVO_VETO));
        assertRechazo(CatalogoMensajes.Moderacion.CONTENIDO_NO_EN_REVISION, () -> negocio.liberarKudo(rector, idKudo, MOTIVO_LIBERACION));
    }

    @Test
    @DisplayName("Liberar devuelve el contenido a VISIBLE, rechaza los reportes y avisa una vez a cada reportante (CU-037)")
    void liberar() {
        reportes.reportarKudo(marta, idKudo, "SPAM", "");
        reportes.reportarKudo(pedro, idKudo, "ACOSO", "");
        negocio.iniciarRevisionKudo(rector, idKudo);
        daoFactory.getReporteDAOFalso().crear(new ReporteEntidad.Builder()
                .id(UtilId.generarId())
                .reportante(new UsuarioEntidad.Builder().id(marta).build())
                .kudo(new KudoEntidad.Builder().id(idKudo).build())
                .motivo("OTRO")
                .descripcion("Duplicado cargado a mano")
                .fechaReporte(UtilFecha.fechaActual())
                .estado("PENDIENTE")
                .build());

        negocio.liberarKudo(rector, idKudo, MOTIVO_LIBERACION);

        assertEquals("VISIBLE", estadoKudo());
        assertEquals(3, reportesEn("RECHAZADO"));
        var notificaciones = daoFactory.getNotificacionDAOFalso().getNotificaciones();
        assertEquals(2, notificaciones.size());
        assertTrue(notificaciones.stream().allMatch(notificacion -> "REPORTE_RECHAZADO".equals(notificacion.getTipo())));
        assertEquals("LIBERAR", daoFactory.getModeracionDAOFalso().getModeraciones().getFirst().getDecision());
        assertEquals("ContenidoLiberado", daoFactory.getAuditoriaDAOFalso().getRegistros().getLast().getAccion());

        reportes.reportarKudo(marta, idKudo, "ACOSO", "");
        negocio.iniciarRevisionKudo(rector, idKudo);
        assertEquals(1, reportesEn("EN_REVISION"));
        assertEquals(3, reportesEn("RECHAZADO"));
        assertEquals(2, daoFactory.getModeracionDAOFalso().getModeraciones().size());
    }

    @Test
    @DisplayName("Una réplica también se revisa y se libera")
    void revisarYLiberarReplica() {
        reportes.reportarReplica(marta, idReplica, "SPAM", "");

        negocio.iniciarRevisionReplica(coordinador, idReplica);
        assertEquals("EN_REVISION", estadoReplica());

        negocio.liberarReplica(coordinador, idReplica, MOTIVO_LIBERACION);
        assertEquals("VISIBLE", estadoReplica());
        assertEquals(1, reportesEn("RECHAZADO"));
        assertEquals(marta, daoFactory.getNotificacionDAOFalso().getNotificaciones().getFirst().getDestinatario().getId());
    }

    @Test
    @DisplayName("El histórico trae los vetos con el contenido original y sus reportes resueltos; solo lo ve un moderador (CU-015)")
    void historicoDeVetos() {
        reportes.reportarKudo(marta, idKudo, "SPAM", "");
        reportes.reportarKudo(pedro, idKudo, "ACOSO", "");
        negocio.vetarKudo(rector, idKudo, MOTIVO_VETO);
        negocio.vetarReplica(coordinador, idReplica, MOTIVO_VETO);

        assertRechazo(CatalogoMensajes.Moderacion.ACTOR_NO_ES_MODERADOR, () -> negocio.consultarVetos(ana));
        var vetos = negocio.consultarVetos(rector);

        assertEquals(2, vetos.size());
        var vetoKudo = vetos.stream().filter(veto -> veto.esSobreKudo()).findFirst().orElseThrow();
        assertEquals("Gracias por tu apoyo en el semillero.", vetoKudo.getKudo().getContenido());
        assertEquals(2, negocio.consultarReportesResueltos(vetoKudo.getId()).size());
        var vetoReplica = vetos.stream().filter(veto -> !veto.esSobreKudo()).findFirst().orElseThrow();
        assertEquals("Gracias, Ana.", vetoReplica.getReplica().getContenido());
        assertTrue(negocio.consultarReportesResueltos(vetoReplica.getId()).isEmpty());
    }
}
