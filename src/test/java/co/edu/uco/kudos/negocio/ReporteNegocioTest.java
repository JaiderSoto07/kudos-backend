package co.edu.uco.kudos.negocio;

import co.edu.uco.kudos.dominio.CategoriaDominio;
import co.edu.uco.kudos.dominio.EstadoCuenta;
import co.edu.uco.kudos.dominio.EstadoReporte;
import co.edu.uco.kudos.dominio.KudoDominio;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.entidad.ReporteEntidad;
import co.edu.uco.kudos.negocio.falsos.DAOFactoryFalsa;
import co.edu.uco.kudos.negocio.negocio.ModeracionNegocio;
import co.edu.uco.kudos.negocio.negocio.ReporteNegocio;
import co.edu.uco.kudos.negocio.negocio.impl.KudoNegocioImpl;
import co.edu.uco.kudos.negocio.negocio.impl.ModeracionNegocioImpl;
import co.edu.uco.kudos.negocio.negocio.impl.ReplicaNegocioImpl;
import co.edu.uco.kudos.negocio.negocio.impl.ReporteNegocioImpl;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Negocio de reportes: reportar Kudos y réplicas, mis reportes y la bandeja del moderador (CU-010 a CU-012, D-24)")
class ReporteNegocioTest {

    private static final String MOTIVO_DECISION = "Revisado por el comité de convivencia";

    private DAOFactoryFalsa daoFactory;
    private ReporteNegocio negocio;
    private ModeracionNegocio moderacion;
    private UUID ana;
    private UUID luis;
    private UUID marta;
    private UUID pedro;
    private UUID rector;
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
        var liderazgo = daoFactory.registrarCategoria("Liderazgo", true);
        new KudoNegocioImpl(daoFactory).crear(ana, new KudoDominio.Builder()
                .destinatario(new UsuarioDominio.Builder().id(luis).build())
                .categoria(new CategoriaDominio.Builder().id(liderazgo).build())
                .contenido("Gracias por tu apoyo en el semillero.")
                .build());
        idKudo = daoFactory.getKudoDAOFalso().getKudos().getFirst().getId();
        new ReplicaNegocioImpl(daoFactory).responderKudo(luis, idKudo, "Gracias, Ana.");
        idReplica = daoFactory.getReplicaDAOFalso().getReplicas().getFirst().getId();
        negocio = new ReporteNegocioImpl(daoFactory);
        moderacion = new ModeracionNegocioImpl(daoFactory);
    }

    private void assertRechazo(String mensaje, Executable operacion) {
        var excepcion = assertThrows(KudosExcepcion.class, operacion);
        assertEquals(mensaje, excepcion.getMensajeUsuario());
    }

    private String estadoKudo() {
        return daoFactory.getKudoDAOFalso().consultarPorId(idKudo).getEstado();
    }

    private ReporteEntidad ultimoReporte() {
        return daoFactory.getReporteDAOFalso().getReportes().getLast();
    }

    @Test
    @DisplayName("Reportar un Kudo VISIBLE crea el reporte PENDIENTE, pasa el Kudo a REPORTADO y lo audita; otro reporte no cambia el estado")
    void reportarKudo() {
        negocio.reportarKudo(marta, idKudo, "LENGUAJE_OFENSIVO", "");

        assertEquals("REPORTADO", estadoKudo());
        var reporte = ultimoReporte();
        assertEquals("PENDIENTE", reporte.getEstado());
        assertEquals("LENGUAJE_OFENSIVO", reporte.getMotivo());
        assertEquals(marta, reporte.getReportante().getId());
        assertEquals(idKudo, reporte.getKudo().getId());
        assertTrue(UtilId.esValorDefecto(reporte.getModeracion().getId()));
        var auditoria = daoFactory.getAuditoriaDAOFalso().getRegistros().getLast();
        assertEquals("KudoReportado", auditoria.getAccion());
        assertEquals("VISIBLE", auditoria.getEstadoAnterior());
        assertEquals("REPORTADO", auditoria.getEstadoNuevo());

        negocio.reportarKudo(pedro, idKudo, "OTRO", "Se burla de la destinataria.");

        assertEquals("REPORTADO", estadoKudo());
        assertEquals(2, daoFactory.getReporteDAOFalso().getReportes().size());
    }

    @Test
    @DisplayName("Un reporte que llega durante la revisión entra directo EN_REVISION con la moderación en curso (CU-010, 5a)")
    void reporteDuranteRevision() {
        negocio.reportarKudo(marta, idKudo, "ACOSO", "");
        moderacion.iniciarRevisionKudo(rector, idKudo);

        negocio.reportarKudo(pedro, idKudo, "SPAM", "");

        assertEquals("EN_REVISION", estadoKudo());
        assertEquals("EN_REVISION", ultimoReporte().getEstado());
        assertEquals(daoFactory.getModeracionDAOFalso().getModeraciones().getFirst().getId(), ultimoReporte().getModeracion().getId());
    }

    @Test
    @DisplayName("Reportar una réplica la pasa a REPORTADO y audita ReplicaReportada")
    void reportarReplica() {
        negocio.reportarReplica(marta, idReplica, "DISCRIMINACION", "");

        assertEquals("REPORTADO", daoFactory.getReplicaDAOFalso().consultarPorId(idReplica).getEstado());
        assertEquals(idReplica, ultimoReporte().getReplica().getId());
        assertTrue(UtilId.esValorDefecto(ultimoReporte().getKudo().getId()));
        assertEquals("ReplicaReportada", daoFactory.getAuditoriaDAOFalso().getRegistros().getLast().getAccion());
    }

    @Test
    @DisplayName("Se rechaza reportar lo propio, sin motivo, \"Otro\" sin descripción, una descripción larga, dos veces o algo inexistente")
    void rechazos() {
        assertRechazo(CatalogoMensajes.Moderacion.CONTENIDO_PROPIO, () -> negocio.reportarKudo(ana, idKudo, "SPAM", ""));
        assertRechazo(CatalogoMensajes.Moderacion.CONTENIDO_PROPIO, () -> negocio.reportarReplica(luis, idReplica, "SPAM", ""));
        assertRechazo(CatalogoMensajes.Moderacion.MOTIVO_OBLIGATORIO, () -> negocio.reportarKudo(marta, idKudo, "", ""));
        assertRechazo(CatalogoMensajes.Moderacion.MOTIVO_OBLIGATORIO, () -> negocio.reportarKudo(marta, idKudo, "CHISME", ""));
        assertRechazo(CatalogoMensajes.Moderacion.DESCRIPCION_OBLIGATORIA, () -> negocio.reportarKudo(marta, idKudo, "OTRO", "   "));
        assertRechazo(CatalogoMensajes.Moderacion.DESCRIPCION_MUY_LARGA, () -> negocio.reportarKudo(marta, idKudo, "SPAM", "a".repeat(501)));
        assertRechazo(CatalogoMensajes.Kudo.KUDO_NO_EXISTE, () -> negocio.reportarKudo(marta, UtilId.generarId(), "SPAM", ""));
        assertRechazo(CatalogoMensajes.Replica.REPLICA_NO_EXISTE, () -> negocio.reportarReplica(marta, UtilId.generarId(), "SPAM", ""));
        assertTrue(daoFactory.getReporteDAOFalso().getReportes().isEmpty());

        negocio.reportarKudo(marta, idKudo, "SPAM", "a".repeat(500));
        assertRechazo(CatalogoMensajes.Moderacion.REPORTE_ABIERTO, () -> negocio.reportarKudo(marta, idKudo, "ACOSO", ""));
        assertEquals(1, daoFactory.getReporteDAOFalso().getReportes().size());
    }

    @Test
    @DisplayName("Nadie reporta contenido vetado ni réplicas de un Kudo vetado; tras liberar se puede volver a reportar")
    void contenidoVetadoYLiberado() {
        negocio.reportarKudo(marta, idKudo, "SPAM", "");
        moderacion.iniciarRevisionKudo(rector, idKudo);
        moderacion.liberarKudo(rector, idKudo, MOTIVO_DECISION);

        negocio.reportarKudo(marta, idKudo, "ACOSO", "");
        assertEquals("PENDIENTE", ultimoReporte().getEstado());

        moderacion.vetarReplica(rector, idReplica, MOTIVO_DECISION);
        assertRechazo(CatalogoMensajes.Moderacion.CONTENIDO_YA_VETADO, () -> negocio.reportarReplica(marta, idReplica, "SPAM", ""));

        moderacion.vetarKudo(rector, idKudo, MOTIVO_DECISION);
        assertRechazo(CatalogoMensajes.Moderacion.CONTENIDO_YA_VETADO, () -> negocio.reportarKudo(pedro, idKudo, "SPAM", ""));
        assertRechazo(CatalogoMensajes.Replica.KUDO_VETADO, () -> negocio.reportarReplica(pedro, idReplica, "SPAM", ""));
    }

    @Test
    @DisplayName("Mis reportes trae los del actor con su contenido; la bandeja solo la ve un moderador y trae los abiertos")
    void consultas() {
        negocio.reportarKudo(marta, idKudo, "SPAM", "");
        negocio.reportarReplica(marta, idReplica, "ACOSO", "");
        negocio.reportarKudo(pedro, idKudo, "ACOSO", "");

        var mios = negocio.consultarMisReportes(marta);
        assertEquals(2, mios.size());
        assertTrue(mios.stream().anyMatch(reporte -> reporte.esSobreKudo() && "Gracias por tu apoyo en el semillero.".equals(reporte.getKudo().getContenido())));
        assertTrue(mios.stream().anyMatch(reporte -> !reporte.esSobreKudo() && "Gracias, Ana.".equals(reporte.getReplica().getContenido())));

        assertRechazo(CatalogoMensajes.Moderacion.ACTOR_NO_ES_MODERADOR, () -> negocio.consultarReportesAbiertos(luis));

        moderacion.iniciarRevisionKudo(rector, idKudo);
        moderacion.iniciarRevisionReplica(rector, idReplica);
        moderacion.liberarReplica(rector, idReplica, MOTIVO_DECISION);
        negocio.reportarReplica(pedro, idReplica, "SPAM", "");
        var abiertos = negocio.consultarReportesAbiertos(rector);
        assertEquals(3, abiertos.size());
        assertEquals(2, abiertos.stream().filter(reporte -> reporte.getEstado() == EstadoReporte.EN_REVISION).count());
        assertEquals(1, abiertos.stream().filter(reporte -> reporte.getEstado() == EstadoReporte.PENDIENTE).count());
        assertTrue(abiertos.stream().allMatch(reporte -> reporte.estaAbierto()));
    }
}
