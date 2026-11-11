package co.edu.uco.kudos.negocio.negocio.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.AuditoriaDominio;
import co.edu.uco.kudos.dominio.DecisionModeracion;
import co.edu.uco.kudos.dominio.EstadoContenido;
import co.edu.uco.kudos.dominio.EstadoModeracion;
import co.edu.uco.kudos.dominio.EstadoReporte;
import co.edu.uco.kudos.dominio.KudoDominio;
import co.edu.uco.kudos.dominio.ModeracionDominio;
import co.edu.uco.kudos.dominio.NotificacionDominio;
import co.edu.uco.kudos.dominio.ReplicaDominio;
import co.edu.uco.kudos.dominio.ReporteDominio;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dominio.TipoNotificacion;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.entidad.ModeracionEntidad;
import co.edu.uco.kudos.entidad.ReporteEntidad;
import co.edu.uco.kudos.negocio.negocio.AuditoriaNegocio;
import co.edu.uco.kudos.negocio.negocio.ModeracionNegocio;
import co.edu.uco.kudos.negocio.negocio.NotificacionNegocio;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.KudoEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.ModeracionEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.ReplicaEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.ReporteEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.autorizacion.AsegurarActorTieneRolRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.conversacion.AsegurarReplicaExisteRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.kudo.AsegurarKudoExisteRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.moderacion.AsegurarContenidoEnRevisionRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.moderacion.AsegurarContenidoNoVetadoRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.moderacion.AsegurarContenidoReportadoRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.moderacion.ValidarMotivoDecisionRule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ModeracionNegocioImpl implements ModeracionNegocio {

    static final List<Rol> ROLES_MODERADORES = List.of(Rol.RECTOR, Rol.COORDINADOR_ACADEMICO);

    private static final String TIPO_KUDO = "Kudo";
    private static final String TIPO_REPLICA = "Replica";
    private static final String ACCION_REVISION_INICIADA = "RevisionIniciada";
    private static final String ACCION_KUDO_VETADO = "KudoVetado";
    private static final String ACCION_REPLICA_VETADA = "ReplicaVetada";
    private static final String ACCION_CONTENIDO_LIBERADO = "ContenidoLiberado";

    private final DAOFactory daoFactory;
    private final NotificacionNegocio notificacionNegocio;
    private final AuditoriaNegocio auditoriaNegocio;

    public ModeracionNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.notificacionNegocio = new NotificacionNegocioImpl(daoFactory);
        this.auditoriaNegocio = new AuditoriaNegocioImpl(daoFactory);
    }

    @Override
    public void iniciarRevisionKudo(UUID idActor, UUID idKudo) {
        asegurarModerador(idActor);
        var kudo = consultarKudo(idKudo);
        AsegurarKudoExisteRule.obtenerInstancia().ejecutar(kudo);
        AsegurarContenidoReportadoRule.obtenerInstancia().ejecutar(kudo.getEstado());

        var estadoAnterior = kudo.getEstado();
        kudo.iniciarRevision();
        guardarKudo(kudo);
        var moderacion = abrirModeracion(idActor, kudo, null);
        pasarReportesARevision(kudo, null, moderacion.getId());
        registrarAuditoria(idActor, ACCION_REVISION_INICIADA, TIPO_KUDO, kudo.getId(), estadoAnterior, kudo.getEstado(), UtilTexto.VACIO);
    }

    @Override
    public void iniciarRevisionReplica(UUID idActor, UUID idReplica) {
        asegurarModerador(idActor);
        var replica = consultarReplica(idReplica);
        AsegurarReplicaExisteRule.obtenerInstancia().ejecutar(replica);
        AsegurarContenidoReportadoRule.obtenerInstancia().ejecutar(replica.getEstado());

        var estadoAnterior = replica.getEstado();
        replica.iniciarRevision();
        guardarReplica(replica);
        var moderacion = abrirModeracion(idActor, null, replica);
        pasarReportesARevision(null, replica, moderacion.getId());
        registrarAuditoria(idActor, ACCION_REVISION_INICIADA, TIPO_REPLICA, replica.getId(), estadoAnterior, replica.getEstado(), UtilTexto.VACIO);
    }

    @Override
    public void vetarKudo(UUID idActor, UUID idKudo, String motivo) {
        asegurarModerador(idActor);
        var kudo = consultarKudo(idKudo);
        AsegurarKudoExisteRule.obtenerInstancia().ejecutar(kudo);
        AsegurarContenidoNoVetadoRule.obtenerInstancia().ejecutar(kudo.getEstado());
        ValidarMotivoDecisionRule.obtenerInstancia().ejecutar(motivo);

        var estadoAnterior = kudo.getEstado();
        kudo.vetar();
        guardarKudo(kudo);
        var moderacion = cerrarModeracion(idActor, kudo, null, DecisionModeracion.VETAR, motivo);
        resolverReportes(kudo, null, moderacion);
        notificar(kudo.getAutor().getId(), TipoNotificacion.CONTENIDO_VETADO,
                String.format(CatalogoMensajes.Moderacion.MENSAJE_KUDO_VETADO, kudo.getDestinatario().nombreCompleto(), moderacion.getMotivoDecision()),
                kudo.getId());
        registrarAuditoria(idActor, ACCION_KUDO_VETADO, TIPO_KUDO, kudo.getId(), estadoAnterior, kudo.getEstado(), moderacion.getMotivoDecision());
    }

    @Override
    public void vetarReplica(UUID idActor, UUID idReplica, String motivo) {
        asegurarModerador(idActor);
        var replica = consultarReplica(idReplica);
        AsegurarReplicaExisteRule.obtenerInstancia().ejecutar(replica);
        AsegurarContenidoNoVetadoRule.obtenerInstancia().ejecutar(replica.getEstado());
        ValidarMotivoDecisionRule.obtenerInstancia().ejecutar(motivo);

        var estadoAnterior = replica.getEstado();
        replica.vetar();
        guardarReplica(replica);
        var moderacion = cerrarModeracion(idActor, null, replica, DecisionModeracion.VETAR, motivo);
        resolverReportes(null, replica, moderacion);
        var kudo = consultarKudo(replica.getKudo().getId());
        notificar(replica.getAutor().getId(), TipoNotificacion.CONTENIDO_VETADO,
                String.format(CatalogoMensajes.Moderacion.MENSAJE_REPLICA_VETADA, kudo.getDestinatario().nombreCompleto(), moderacion.getMotivoDecision()),
                kudo.getId());
        registrarAuditoria(idActor, ACCION_REPLICA_VETADA, TIPO_REPLICA, replica.getId(), estadoAnterior, replica.getEstado(),
                moderacion.getMotivoDecision());
    }

    @Override
    public void liberarKudo(UUID idActor, UUID idKudo, String motivo) {
        asegurarModerador(idActor);
        var kudo = consultarKudo(idKudo);
        AsegurarKudoExisteRule.obtenerInstancia().ejecutar(kudo);
        AsegurarContenidoEnRevisionRule.obtenerInstancia().ejecutar(kudo.getEstado());
        ValidarMotivoDecisionRule.obtenerInstancia().ejecutar(motivo);

        var estadoAnterior = kudo.getEstado();
        kudo.liberar();
        guardarKudo(kudo);
        var moderacion = cerrarModeracion(idActor, kudo, null, DecisionModeracion.LIBERAR, motivo);
        avisarReportantes(resolverReportes(kudo, null, moderacion), kudo, moderacion);
        registrarAuditoria(idActor, ACCION_CONTENIDO_LIBERADO, TIPO_KUDO, kudo.getId(), estadoAnterior, kudo.getEstado(), moderacion.getMotivoDecision());
    }

    @Override
    public void liberarReplica(UUID idActor, UUID idReplica, String motivo) {
        asegurarModerador(idActor);
        var replica = consultarReplica(idReplica);
        AsegurarReplicaExisteRule.obtenerInstancia().ejecutar(replica);
        AsegurarContenidoEnRevisionRule.obtenerInstancia().ejecutar(replica.getEstado());
        ValidarMotivoDecisionRule.obtenerInstancia().ejecutar(motivo);

        var estadoAnterior = replica.getEstado();
        replica.liberar();
        guardarReplica(replica);
        var moderacion = cerrarModeracion(idActor, null, replica, DecisionModeracion.LIBERAR, motivo);
        avisarReportantes(resolverReportes(null, replica, moderacion), consultarKudo(replica.getKudo().getId()), moderacion);
        registrarAuditoria(idActor, ACCION_CONTENIDO_LIBERADO, TIPO_REPLICA, replica.getId(), estadoAnterior, replica.getEstado(),
                moderacion.getMotivoDecision());
    }

    @Override
    public List<ModeracionDominio> consultarVetos(UUID idActor) {
        asegurarModerador(idActor);
        var filtro = new ModeracionEntidad.Builder()
                .estado(EstadoModeracion.CERRADA.name())
                .decision(DecisionModeracion.VETAR.name())
                .build();
        var vetos = new ArrayList<ModeracionDominio>();
        for (var moderacion : ModeracionEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerModeracionDAO().consultarPorFiltro(filtro))) {
            vetos.add(new ModeracionDominio.Builder()
                    .id(moderacion.getId())
                    .kudo(moderacion.esSobreKudo() ? consultarKudo(moderacion.getKudo().getId()) : null)
                    .replica(moderacion.esSobreKudo() ? null : consultarReplica(moderacion.getReplica().getId()))
                    .moderador(moderacion.getModerador())
                    .estado(moderacion.getEstado())
                    .fechaInicio(moderacion.getFechaInicio())
                    .fechaCierre(moderacion.getFechaCierre())
                    .decision(moderacion.getDecision())
                    .motivoDecision(moderacion.getMotivoDecision())
                    .build());
        }
        vetos.sort((primero, segundo) -> segundo.getFechaCierre().compareTo(primero.getFechaCierre()));
        return vetos;
    }

    @Override
    public List<ReporteDominio> consultarReportesResueltos(UUID idModeracion) {
        var filtro = new ReporteEntidad.Builder()
                .moderacion(new ModeracionEntidad.Builder().id(idModeracion).build())
                .build();
        return ReporteEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerReporteDAO().consultarPorFiltro(filtro));
    }

    private void asegurarModerador(UUID idActor) {
        AsegurarActorTieneRolRule.obtenerInstancia().ejecutar(idActor, daoFactory, ROLES_MODERADORES,
                CatalogoMensajes.Moderacion.ACTOR_NO_ES_MODERADOR);
    }

    private ModeracionDominio abrirModeracion(UUID idActor, KudoDominio kudo, ReplicaDominio replica) {
        var moderacion = new ModeracionDominio.Builder()
                .id(UtilId.generarId())
                .kudo(kudo)
                .replica(replica)
                .moderador(new UsuarioDominio.Builder().id(idActor).build())
                .estado(EstadoModeracion.EN_CURSO)
                .fechaInicio(UtilFecha.fechaActual())
                .build();
        daoFactory.obtenerModeracionDAO().crear(ModeracionEntidadAssembler.getInstance().convertirAEntidad(moderacion));
        return moderacion;
    }

    private ModeracionDominio cerrarModeracion(UUID idActor, KudoDominio kudo, ReplicaDominio replica, DecisionModeracion decision, String motivo) {
        var filtro = new ModeracionEntidad.Builder()
                .kudo(KudoEntidadAssembler.getInstance().convertirAEntidad(kudo))
                .replica(ReplicaEntidadAssembler.getInstance().convertirAEntidad(replica))
                .estado(EstadoModeracion.EN_CURSO.name())
                .build();
        var enCurso = daoFactory.obtenerModeracionDAO().consultarPorFiltro(filtro);
        var esNueva = enCurso.isEmpty();
        var moderacion = new ModeracionDominio.Builder()
                .id(esNueva ? UtilId.generarId() : enCurso.getFirst().getId())
                .kudo(kudo)
                .replica(replica)
                .moderador(new UsuarioDominio.Builder().id(idActor).build())
                .estado(EstadoModeracion.EN_CURSO)
                .fechaInicio(esNueva ? UtilFecha.fechaActual() : enCurso.getFirst().getFechaInicio())
                .build();
        if (decision == DecisionModeracion.VETAR) {
            moderacion.vetar(motivo);
        } else {
            moderacion.liberar(motivo);
        }

        var entidad = ModeracionEntidadAssembler.getInstance().convertirAEntidad(moderacion);
        if (esNueva) {
            daoFactory.obtenerModeracionDAO().crear(entidad);
        } else {
            daoFactory.obtenerModeracionDAO().actualizar(moderacion.getId(), entidad);
        }
        return moderacion;
    }

    private void pasarReportesARevision(KudoDominio kudo, ReplicaDominio replica, UUID idModeracion) {
        for (var reporte : consultarReportes(kudo, replica)) {
            if (reporte.getEstado() == EstadoReporte.PENDIENTE) {
                reporte.pasarARevision(idModeracion);
                guardarReporte(reporte);
            }
        }
    }

    private List<UUID> resolverReportes(KudoDominio kudo, ReplicaDominio replica, ModeracionDominio moderacion) {
        var reportantes = new ArrayList<UUID>();
        for (var reporte : consultarReportes(kudo, replica)) {
            if (!reporte.estaAbierto()) {
                continue;
            }
            if (moderacion.terminoEnVeto()) {
                reporte.aceptar(moderacion.getId());
            } else {
                reporte.rechazar(moderacion.getId());
            }
            guardarReporte(reporte);
            if (!reportantes.contains(reporte.getReportante().getId())) {
                reportantes.add(reporte.getReportante().getId());
            }
        }
        return reportantes;
    }

    private void avisarReportantes(List<UUID> reportantes, KudoDominio kudo, ModeracionDominio moderacion) {
        var mensaje = String.format(CatalogoMensajes.Moderacion.MENSAJE_REPORTE_RECHAZADO, kudo.getDestinatario().nombreCompleto(),
                moderacion.getMotivoDecision());
        for (var idReportante : reportantes) {
            notificar(idReportante, TipoNotificacion.REPORTE_RECHAZADO, mensaje, kudo.getId());
        }
    }

    private List<ReporteDominio> consultarReportes(KudoDominio kudo, ReplicaDominio replica) {
        var filtro = new ReporteEntidad.Builder()
                .kudo(KudoEntidadAssembler.getInstance().convertirAEntidad(kudo))
                .replica(ReplicaEntidadAssembler.getInstance().convertirAEntidad(replica))
                .build();
        return ReporteEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerReporteDAO().consultarPorFiltro(filtro));
    }

    private void guardarReporte(ReporteDominio reporte) {
        daoFactory.obtenerReporteDAO().actualizar(reporte.getId(), ReporteEntidadAssembler.getInstance().convertirAEntidad(reporte));
    }

    private void guardarKudo(KudoDominio kudo) {
        daoFactory.obtenerKudoDAO().actualizar(kudo.getId(), KudoEntidadAssembler.getInstance().convertirAEntidad(kudo));
    }

    private void guardarReplica(ReplicaDominio replica) {
        daoFactory.obtenerReplicaDAO().actualizar(replica.getId(), ReplicaEntidadAssembler.getInstance().convertirAEntidad(replica));
    }

    private KudoDominio consultarKudo(UUID idKudo) {
        return KudoEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerKudoDAO().consultarPorId(UtilId.valorDefecto(idKudo)));
    }

    private ReplicaDominio consultarReplica(UUID idReplica) {
        return ReplicaEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerReplicaDAO().consultarPorId(UtilId.valorDefecto(idReplica)));
    }

    private void notificar(UUID idDestinatario, TipoNotificacion tipo, String mensaje, UUID idKudo) {
        var notificacion = new NotificacionDominio.Builder()
                .destinatario(new UsuarioDominio.Builder().id(idDestinatario).build())
                .tipo(tipo)
                .mensaje(mensaje)
                .tipoReferencia(TIPO_KUDO)
                .idReferencia(idKudo)
                .build();
        notificacionNegocio.notificar(notificacion);
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
