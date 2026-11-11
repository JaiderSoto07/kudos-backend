package co.edu.uco.kudos.negocio;

import co.edu.uco.kudos.dominio.DecisionModeracion;
import co.edu.uco.kudos.dominio.EstadoContenido;
import co.edu.uco.kudos.dominio.EstadoModeracion;
import co.edu.uco.kudos.dominio.EstadoReporte;
import co.edu.uco.kudos.dominio.KudoDominio;
import co.edu.uco.kudos.dominio.ModeracionDominio;
import co.edu.uco.kudos.dominio.MotivoReporte;
import co.edu.uco.kudos.dominio.ReplicaDominio;
import co.edu.uco.kudos.dominio.ReporteDominio;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.dto.DecisionDto;
import co.edu.uco.kudos.dto.ReporteDto;
import co.edu.uco.kudos.entidad.ModeracionEntidad;
import co.edu.uco.kudos.entidad.ReporteEntidad;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.ContenidoReportadoDtoAssembler;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.KudoDtoAssembler;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.ReporteDtoAssembler;
import co.edu.uco.kudos.negocio.fachada.assembler.impl.VetoDtoAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.ModeracionEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.ReporteEntidadAssembler;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Ensambladores de reportes, moderaciones, bandeja e histórico de vetos (M6)")
class AssemblersModeracionTest {

    private final UsuarioDominio ana = new UsuarioDominio.Builder().id(UtilId.generarId()).nombres("Ana").apellidos("Pérez").build();
    private final UsuarioDominio marta = new UsuarioDominio.Builder().id(UtilId.generarId()).nombres("Marta").apellidos("Gómez").build();
    private final UsuarioDominio rector = new UsuarioDominio.Builder().id(UtilId.generarId()).nombres("Carlos").apellidos("Rector").build();

    private KudoDominio kudo(EstadoContenido estado) {
        return new KudoDominio.Builder().id(UtilId.generarId()).autor(ana).contenido("Gracias por todo").estado(estado).build();
    }

    private ReplicaDominio replica(KudoDominio kudo) {
        return new ReplicaDominio.Builder().id(UtilId.generarId()).autor(ana).kudo(kudo).contenido("Una réplica").estado(EstadoContenido.REPORTADO).build();
    }

    private ReporteDominio reporte(KudoDominio kudo, ReplicaDominio replica, MotivoReporte motivo) {
        return new ReporteDominio.Builder()
                .id(UtilId.generarId())
                .reportante(marta)
                .kudo(kudo)
                .replica(replica)
                .motivo(motivo)
                .descripcion("Detalle")
                .fechaReporte(UtilFecha.fechaActual())
                .estado(EstadoReporte.EN_REVISION)
                .idModeracion(UtilId.generarId())
                .build();
    }

    @Test
    @DisplayName("Reporte y moderación pasan de dominio a entidad y de vuelta; la decisión vacía vuelve como nula y los nulos dan valores por defecto")
    void entidades() {
        var reporte = reporte(kudo(EstadoContenido.REPORTADO), null, MotivoReporte.ACOSO);
        var reporteDeVuelta = ReporteEntidadAssembler.getInstance().convertirADominio(ReporteEntidadAssembler.getInstance().convertirAEntidad(reporte));
        assertEquals(reporte.getId(), reporteDeVuelta.getId());
        assertEquals(marta.getId(), reporteDeVuelta.getReportante().getId());
        assertEquals(reporte.getKudo().getId(), reporteDeVuelta.getKudo().getId());
        assertEquals(MotivoReporte.ACOSO, reporteDeVuelta.getMotivo());
        assertEquals("Detalle", reporteDeVuelta.getDescripcion());
        assertEquals(EstadoReporte.EN_REVISION, reporteDeVuelta.getEstado());
        assertEquals(reporte.getIdModeracion(), reporteDeVuelta.getIdModeracion());
        assertEquals(reporte.getFechaReporte(), reporteDeVuelta.getFechaReporte());
        assertTrue(UtilId.esValorDefecto(ReporteEntidadAssembler.getInstance().convertirAEntidad(null).getId()));
        assertTrue(UtilId.esValorDefecto(ReporteEntidadAssembler.getInstance().convertirADominio((ReporteEntidad) null).getId()));
        assertTrue(ReporteEntidadAssembler.getInstance().convertirADominio((List<ReporteEntidad>) null).isEmpty());
        assertEquals(1, ReporteEntidadAssembler.getInstance().convertirADominio(List.of(new ReporteEntidad.Builder().build())).size());

        var enCurso = new ModeracionDominio.Builder()
                .id(UtilId.generarId())
                .replica(replica(kudo(EstadoContenido.VISIBLE)))
                .moderador(rector)
                .fechaInicio(UtilFecha.fechaActual())
                .build();
        var enCursoDeVuelta = ModeracionEntidadAssembler.getInstance().convertirADominio(ModeracionEntidadAssembler.getInstance().convertirAEntidad(enCurso));
        assertEquals(EstadoModeracion.EN_CURSO, enCursoDeVuelta.getEstado());
        assertNull(enCursoDeVuelta.getDecision());
        assertEquals(enCurso.getReplica().getId(), enCursoDeVuelta.getReplica().getId());
        assertEquals(rector.getId(), enCursoDeVuelta.getModerador().getId());
        assertEquals(enCurso.getFechaInicio(), enCursoDeVuelta.getFechaInicio());

        enCurso.vetar("Lenguaje ofensivo comprobado");
        var cerrada = ModeracionEntidadAssembler.getInstance().convertirADominio(ModeracionEntidadAssembler.getInstance().convertirAEntidad(enCurso));
        assertEquals(EstadoModeracion.CERRADA, cerrada.getEstado());
        assertEquals(DecisionModeracion.VETAR, cerrada.getDecision());
        assertEquals("Lenguaje ofensivo comprobado", cerrada.getMotivoDecision());
        assertFalse(UtilFecha.esValorDefecto(cerrada.getFechaCierre()));
        assertTrue(UtilId.esValorDefecto(ModeracionEntidadAssembler.getInstance().convertirAEntidad(null).getId()));
        assertTrue(UtilId.esValorDefecto(ModeracionEntidadAssembler.getInstance().convertirADominio((ModeracionEntidad) null).getId()));
        assertTrue(ModeracionEntidadAssembler.getInstance().convertirADominio((List<ModeracionEntidad>) null).isEmpty());
        assertEquals(1, ModeracionEntidadAssembler.getInstance().convertirADominio(List.of(new ModeracionEntidad.Builder().build())).size());
    }

    @Test
    @DisplayName("El DTO del reporte identifica el contenido y oculta el texto si fue vetado; el Kudo vetado también se oculta")
    void reporteDto() {
        var kudoVisible = kudo(EstadoContenido.REPORTADO);
        var dtoKudo = ReporteDtoAssembler.getInstance().convertirADTO(reporte(kudoVisible, null, MotivoReporte.SPAM));
        assertEquals(ReporteDtoAssembler.TIPO_KUDO, dtoKudo.getTipoContenido());
        assertEquals(kudoVisible.getId(), dtoKudo.getIdContenido());
        assertEquals(kudoVisible.getId(), dtoKudo.getIdKudo());
        assertEquals("Gracias por todo", dtoKudo.getContenido());
        assertEquals("Ana Pérez", dtoKudo.getNombreAutorContenido());
        assertEquals("Marta Gómez", dtoKudo.getNombreReportante());
        assertEquals("SPAM", dtoKudo.getMotivo());
        assertEquals("Detalle", dtoKudo.getDescripcion());
        assertEquals("EN_REVISION", dtoKudo.getEstado());
        assertFalse(UtilId.esValorDefecto(dtoKudo.getId()));
        assertFalse(UtilFecha.esValorDefecto(dtoKudo.getFechaReporte()));

        var kudoVetado = kudo(EstadoContenido.VETADO);
        var replicaVetada = new ReplicaDominio.Builder().id(UtilId.generarId()).autor(ana).kudo(kudoVetado).contenido("Ofensa").estado(EstadoContenido.VETADO).build();
        var dtoReplica = ReporteDtoAssembler.getInstance().convertirADTO(reporte(null, replicaVetada, MotivoReporte.OTRO));
        assertEquals(ReporteDtoAssembler.TIPO_REPLICA, dtoReplica.getTipoContenido());
        assertEquals(replicaVetada.getId(), dtoReplica.getIdContenido());
        assertEquals(kudoVetado.getId(), dtoReplica.getIdKudo());
        assertEquals(CatalogoMensajes.Replica.CONTENIDO_VETADO, dtoReplica.getContenido());

        var dtoKudoVetado = KudoDtoAssembler.getInstance().convertirADTO(kudoVetado);
        assertTrue(dtoKudoVetado.isVetado());
        assertEquals(CatalogoMensajes.Replica.CONTENIDO_VETADO, dtoKudoVetado.getContenido());
        assertFalse(KudoDtoAssembler.getInstance().convertirADTO(kudoVisible).isVetado());

        var peticion = new ReporteDto();
        peticion.setMotivo("acoso");
        peticion.setDescripcion("  Me insulta  ");
        var dominio = ReporteDtoAssembler.getInstance().convertirADominio(peticion);
        assertEquals(MotivoReporte.ACOSO, dominio.getMotivo());
        assertEquals("Me insulta", dominio.getDescripcion());
        assertEquals(MotivoReporte.OTRO, ReporteDtoAssembler.getInstance().convertirADominio(null).getMotivo());
        assertTrue(ReporteDtoAssembler.getInstance().convertirADTO((List<ReporteDominio>) null).isEmpty());
        assertEquals(1, ReporteDtoAssembler.getInstance().convertirADTO(List.of(reporte(kudoVisible, null, MotivoReporte.SPAM))).size());
        assertEquals(ReporteDtoAssembler.TIPO_REPLICA, ReporteDtoAssembler.getInstance().convertirADTO((ReporteDominio) null).getTipoContenido());

        var decision = new DecisionDto();
        decision.setMotivo("  Motivo de la decisión  ");
        assertEquals("Motivo de la decisión", decision.getMotivo());
    }

    @Test
    @DisplayName("La bandeja agrupa los reportes por contenido con su cantidad; una lista nula da una bandeja vacía")
    void bandeja() {
        var kudoReportado = kudo(EstadoContenido.EN_REVISION);
        var replicaReportada = replica(kudoReportado);
        var reportes = List.of(
                reporte(kudoReportado, null, MotivoReporte.SPAM),
                reporte(null, replicaReportada, MotivoReporte.ACOSO),
                reporte(kudoReportado, null, MotivoReporte.ACOSO));

        var bandeja = ContenidoReportadoDtoAssembler.getInstance().agrupar(reportes);

        assertEquals(2, bandeja.size());
        var grupoKudo = bandeja.getFirst();
        assertEquals(ReporteDtoAssembler.TIPO_KUDO, grupoKudo.getTipoContenido());
        assertEquals(kudoReportado.getId(), grupoKudo.getIdContenido());
        assertEquals(kudoReportado.getId(), grupoKudo.getIdKudo());
        assertEquals("Gracias por todo", grupoKudo.getContenido());
        assertEquals("Ana Pérez", grupoKudo.getNombreAutor());
        assertEquals("EN_REVISION", grupoKudo.getEstadoContenido());
        assertEquals(2, grupoKudo.getCantidadReportes());
        assertEquals(2, grupoKudo.getReportes().size());
        assertFalse(UtilFecha.esValorDefecto(grupoKudo.getFechaUltimoReporte()));
        var grupoReplica = bandeja.getLast();
        assertEquals("REPORTADO", grupoReplica.getEstadoContenido());
        assertEquals(1, grupoReplica.getCantidadReportes());
        assertTrue(ContenidoReportadoDtoAssembler.getInstance().agrupar(null).isEmpty());
    }

    @Test
    @DisplayName("El histórico muestra el texto original del contenido vetado, el moderador, el motivo y cuántos reportes resolvió")
    void historico() {
        var kudoVetado = kudo(EstadoContenido.VETADO);
        var vetoKudo = new ModeracionDominio.Builder().id(UtilId.generarId()).kudo(kudoVetado).moderador(rector).fechaInicio(UtilFecha.fechaActual()).build();
        vetoKudo.vetar("Contenido ofensivo comprobado");

        var dto = VetoDtoAssembler.getInstance().armar(vetoKudo, List.of(reporte(kudoVetado, null, MotivoReporte.SPAM)));

        assertEquals(vetoKudo.getId(), dto.getIdModeracion());
        assertEquals(ReporteDtoAssembler.TIPO_KUDO, dto.getTipoContenido());
        assertEquals(kudoVetado.getId(), dto.getIdContenido());
        assertEquals(kudoVetado.getId(), dto.getIdKudo());
        assertEquals("Gracias por todo", dto.getContenido());
        assertEquals("Ana Pérez", dto.getNombreAutor());
        assertEquals("Carlos Rector", dto.getNombreModerador());
        assertEquals("Contenido ofensivo comprobado", dto.getMotivo());
        assertFalse(UtilFecha.esValorDefecto(dto.getFechaVeto()));
        assertEquals(1, dto.getCantidadReportes());

        var replicaVetada = replica(kudoVetado);
        var vetoReplica = new ModeracionDominio.Builder().id(UtilId.generarId()).replica(replicaVetada).moderador(rector).build();
        var dtoReplica = VetoDtoAssembler.getInstance().armar(vetoReplica, null);
        assertEquals(ReporteDtoAssembler.TIPO_REPLICA, dtoReplica.getTipoContenido());
        assertEquals(replicaVetada.getId(), dtoReplica.getIdContenido());
        assertEquals(kudoVetado.getId(), dtoReplica.getIdKudo());
        assertEquals("Una réplica", dtoReplica.getContenido());
        assertEquals(0, dtoReplica.getCantidadReportes());
        assertEquals(ReporteDtoAssembler.TIPO_REPLICA, VetoDtoAssembler.getInstance().armar(null, null).getTipoContenido());
    }
}
