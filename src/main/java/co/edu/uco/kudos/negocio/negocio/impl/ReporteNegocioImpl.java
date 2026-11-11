package co.edu.uco.kudos.negocio.negocio.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.AuditoriaDominio;
import co.edu.uco.kudos.dominio.EstadoContenido;
import co.edu.uco.kudos.dominio.EstadoModeracion;
import co.edu.uco.kudos.dominio.EstadoReporte;
import co.edu.uco.kudos.dominio.KudoDominio;
import co.edu.uco.kudos.dominio.MotivoReporte;
import co.edu.uco.kudos.dominio.ReplicaDominio;
import co.edu.uco.kudos.dominio.ReporteDominio;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.entidad.KudoEntidad;
import co.edu.uco.kudos.entidad.ModeracionEntidad;
import co.edu.uco.kudos.entidad.ReplicaEntidad;
import co.edu.uco.kudos.entidad.ReporteEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.negocio.negocio.AuditoriaNegocio;
import co.edu.uco.kudos.negocio.negocio.ReporteNegocio;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.KudoEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.ReplicaEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.ReporteEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.autorizacion.AsegurarActorTieneRolRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.conversacion.AsegurarKudoAdmiteInteraccionRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.conversacion.AsegurarReplicaExisteRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.kudo.AsegurarKudoExisteRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.moderacion.AsegurarContenidoNoVetadoRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.moderacion.AsegurarNoReportaContenidoPropioRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.moderacion.AsegurarSinReporteAbiertoRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.moderacion.ValidarMotivoReporteRule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReporteNegocioImpl implements ReporteNegocio {

    private static final String TIPO_KUDO = "Kudo";
    private static final String TIPO_REPLICA = "Replica";
    private static final String ACCION_KUDO_REPORTADO = "KudoReportado";
    private static final String ACCION_REPLICA_REPORTADA = "ReplicaReportada";

    private final DAOFactory daoFactory;
    private final AuditoriaNegocio auditoriaNegocio;

    public ReporteNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.auditoriaNegocio = new AuditoriaNegocioImpl(daoFactory);
    }

    @Override
    public void reportarKudo(UUID idActor, UUID idKudo, String motivo, String descripcion) {
        var kudo = consultarKudo(idKudo);
        AsegurarKudoExisteRule.obtenerInstancia().ejecutar(kudo);
        AsegurarContenidoNoVetadoRule.obtenerInstancia().ejecutar(kudo.getEstado());
        AsegurarNoReportaContenidoPropioRule.obtenerInstancia().ejecutar(idActor, kudo.getAutor().getId());
        ValidarMotivoReporteRule.obtenerInstancia().ejecutar(motivo, descripcion);
        AsegurarSinReporteAbiertoRule.obtenerInstancia().ejecutar(daoFactory, idActor, kudo.getId(), null);

        var estadoAnterior = kudo.getEstado();
        kudo.reportar();
        daoFactory.obtenerKudoDAO().actualizar(kudo.getId(), KudoEntidadAssembler.getInstance().convertirAEntidad(kudo));
        registrarReporte(idActor, kudo, null, kudo.getEstado(), motivo, descripcion);
        registrarAuditoria(idActor, ACCION_KUDO_REPORTADO, TIPO_KUDO, kudo.getId(), estadoAnterior, kudo.getEstado(), motivo);
    }

    @Override
    public void reportarReplica(UUID idActor, UUID idReplica, String motivo, String descripcion) {
        var replica = consultarReplica(idReplica);
        AsegurarReplicaExisteRule.obtenerInstancia().ejecutar(replica);
        AsegurarKudoAdmiteInteraccionRule.obtenerInstancia().ejecutar(consultarKudo(replica.getKudo().getId()));
        AsegurarContenidoNoVetadoRule.obtenerInstancia().ejecutar(replica.getEstado());
        AsegurarNoReportaContenidoPropioRule.obtenerInstancia().ejecutar(idActor, replica.getAutor().getId());
        ValidarMotivoReporteRule.obtenerInstancia().ejecutar(motivo, descripcion);
        AsegurarSinReporteAbiertoRule.obtenerInstancia().ejecutar(daoFactory, idActor, null, replica.getId());

        var estadoAnterior = replica.getEstado();
        replica.reportar();
        daoFactory.obtenerReplicaDAO().actualizar(replica.getId(), ReplicaEntidadAssembler.getInstance().convertirAEntidad(replica));
        registrarReporte(idActor, null, replica, replica.getEstado(), motivo, descripcion);
        registrarAuditoria(idActor, ACCION_REPLICA_REPORTADA, TIPO_REPLICA, replica.getId(), estadoAnterior, replica.getEstado(), motivo);
    }

    @Override
    public List<ReporteDominio> consultarMisReportes(UUID idActor) {
        var filtro = new ReporteEntidad.Builder()
                .reportante(new UsuarioEntidad.Builder().id(idActor).build())
                .build();
        return completarContenido(ReporteEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerReporteDAO().consultarPorFiltro(filtro)));
    }

    @Override
    public List<ReporteDominio> consultarReportesAbiertos(UUID idActor) {
        AsegurarActorTieneRolRule.obtenerInstancia().ejecutar(idActor, daoFactory, ModeracionNegocioImpl.ROLES_MODERADORES,
                CatalogoMensajes.Moderacion.ACTOR_NO_ES_MODERADOR);
        var abiertos = new ArrayList<ReporteEntidad>();
        abiertos.addAll(daoFactory.obtenerReporteDAO().consultarPorFiltro(new ReporteEntidad.Builder().estado(EstadoReporte.PENDIENTE.name()).build()));
        abiertos.addAll(daoFactory.obtenerReporteDAO().consultarPorFiltro(new ReporteEntidad.Builder().estado(EstadoReporte.EN_REVISION.name()).build()));
        abiertos.sort((primero, segundo) -> segundo.getFechaReporte().compareTo(primero.getFechaReporte()));
        return completarContenido(ReporteEntidadAssembler.getInstance().convertirADominio(abiertos));
    }

    private void registrarReporte(UUID idActor, KudoDominio kudo, ReplicaDominio replica, EstadoContenido estadoContenido, String motivo,
            String descripcion) {
        var enRevision = estadoContenido == EstadoContenido.EN_REVISION;
        var reporte = new ReporteDominio.Builder()
                .id(UtilId.generarId())
                .reportante(new UsuarioDominio.Builder().id(idActor).build())
                .kudo(kudo)
                .replica(replica)
                .motivo(UtilEnum.obtenerValor(MotivoReporte.class, motivo, MotivoReporte.OTRO))
                .descripcion(descripcion)
                .fechaReporte(UtilFecha.fechaActual())
                .estado(enRevision ? EstadoReporte.EN_REVISION : EstadoReporte.PENDIENTE)
                .idModeracion(enRevision ? consultarModeracionEnCurso(kudo, replica) : null)
                .build();
        daoFactory.obtenerReporteDAO().crear(ReporteEntidadAssembler.getInstance().convertirAEntidad(reporte));
    }

    private UUID consultarModeracionEnCurso(KudoDominio kudo, ReplicaDominio replica) {
        var filtro = new ModeracionEntidad.Builder()
                .kudo(KudoEntidadAssembler.getInstance().convertirAEntidad(kudo))
                .replica(ReplicaEntidadAssembler.getInstance().convertirAEntidad(replica))
                .estado(EstadoModeracion.EN_CURSO.name())
                .build();
        return daoFactory.obtenerModeracionDAO().consultarPorFiltro(filtro).getFirst().getId();
    }

    private List<ReporteDominio> completarContenido(List<ReporteDominio> reportes) {
        var completos = new ArrayList<ReporteDominio>();
        for (var reporte : reportes) {
            completos.add(new ReporteDominio.Builder()
                    .id(reporte.getId())
                    .reportante(reporte.getReportante())
                    .kudo(reporte.esSobreKudo() ? consultarKudo(reporte.getKudo().getId()) : null)
                    .replica(reporte.esSobreKudo() ? null : consultarReplica(reporte.getReplica().getId()))
                    .motivo(reporte.getMotivo())
                    .descripcion(reporte.getDescripcion())
                    .fechaReporte(reporte.getFechaReporte())
                    .estado(reporte.getEstado())
                    .idModeracion(reporte.getIdModeracion())
                    .build());
        }
        return completos;
    }

    private KudoDominio consultarKudo(UUID idKudo) {
        return KudoEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerKudoDAO().consultarPorId(UtilId.valorDefecto(idKudo)));
    }

    private ReplicaDominio consultarReplica(UUID idReplica) {
        return ReplicaEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerReplicaDAO().consultarPorId(UtilId.valorDefecto(idReplica)));
    }

    private void registrarAuditoria(UUID idActor, String accion, String tipoObjeto, UUID idObjeto, EstadoContenido estadoAnterior,
            EstadoContenido estadoNuevo, String motivo) {
        var auditoria = new AuditoriaDominio.Builder()
                .actor(new UsuarioDominio.Builder().id(idActor).build())
                .accion(accion)
                .tipoObjeto(tipoObjeto)
                .idObjeto(idObjeto)
                .estadoAnterior(estadoAnterior.name())
                .estadoNuevo(estadoNuevo.name())
                .motivo(motivo)
                .build();
        auditoriaNegocio.registrar(auditoria);
    }
}
