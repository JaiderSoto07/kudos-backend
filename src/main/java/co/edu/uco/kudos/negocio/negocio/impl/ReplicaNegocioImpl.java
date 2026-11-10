package co.edu.uco.kudos.negocio.negocio.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.AuditoriaDominio;
import co.edu.uco.kudos.dominio.EstadoContenido;
import co.edu.uco.kudos.dominio.KudoDominio;
import co.edu.uco.kudos.dominio.NotificacionDominio;
import co.edu.uco.kudos.dominio.ReplicaDominio;
import co.edu.uco.kudos.dominio.TipoNotificacion;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.entidad.KudoEntidad;
import co.edu.uco.kudos.entidad.ReplicaEntidad;
import co.edu.uco.kudos.negocio.negocio.AuditoriaNegocio;
import co.edu.uco.kudos.negocio.negocio.NotificacionNegocio;
import co.edu.uco.kudos.negocio.negocio.ReplicaNegocio;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.KudoEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.ReplicaEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.UsuarioEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.conversacion.AsegurarKudoAdmiteInteraccionRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.conversacion.AsegurarReplicaAdmiteInteraccionRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.conversacion.AsegurarReplicaExisteRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.kudo.AsegurarKudoExisteRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.kudo.ValidarContenidoRule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.List;
import java.util.UUID;

public class ReplicaNegocioImpl implements ReplicaNegocio {

    private static final String TIPO_OBJETO = "Replica";
    private static final String TIPO_KUDO = "Kudo";
    private static final String ACCION_PUBLICADA = "ReplicaPublicada";

    private final DAOFactory daoFactory;
    private final NotificacionNegocio notificacionNegocio;
    private final AuditoriaNegocio auditoriaNegocio;

    public ReplicaNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.notificacionNegocio = new NotificacionNegocioImpl(daoFactory);
        this.auditoriaNegocio = new AuditoriaNegocioImpl(daoFactory);
    }

    @Override
    public void responderKudo(UUID idActor, UUID idKudo, String contenido) {
        var kudo = consultarKudo(idKudo);
        AsegurarKudoExisteRule.obtenerInstancia().ejecutar(kudo);
        AsegurarKudoAdmiteInteraccionRule.obtenerInstancia().ejecutar(kudo);
        ValidarContenidoRule.obtenerInstancia().ejecutar(contenido);

        var replica = publicar(idActor, kudo, null, contenido);
        var autor = consultarUsuario(idActor);
        var mensaje = String.format(CatalogoMensajes.Replica.MENSAJE_RESPUESTA_KUDO, autor.nombreCompleto(),
                kudo.getAutor().nombreCompleto(), kudo.getDestinatario().nombreCompleto());
        notificar(idActor, kudo.getAutor(), mensaje, kudo.getId());
        notificar(idActor, kudo.getDestinatario(), mensaje, kudo.getId());
        registrarAuditoria(idActor, replica, kudo.getId().toString());
    }

    @Override
    public void responderReplica(UUID idActor, UUID idReplica, String contenido) {
        var padre = ReplicaEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerReplicaDAO().consultarPorId(idReplica));
        AsegurarReplicaExisteRule.obtenerInstancia().ejecutar(padre);
        var kudo = consultarKudo(padre.getKudo().getId());
        AsegurarKudoAdmiteInteraccionRule.obtenerInstancia().ejecutar(kudo);
        AsegurarReplicaAdmiteInteraccionRule.obtenerInstancia().ejecutar(padre);
        ValidarContenidoRule.obtenerInstancia().ejecutar(contenido);

        var replica = publicar(idActor, kudo, padre.getId(), contenido);
        var autor = consultarUsuario(idActor);
        var mensaje = String.format(CatalogoMensajes.Replica.MENSAJE_RESPUESTA_REPLICA, autor.nombreCompleto(),
                kudo.getDestinatario().nombreCompleto());
        notificar(idActor, padre.getAutor(), mensaje, kudo.getId());
        registrarAuditoria(idActor, replica, padre.getId().toString());
    }

    @Override
    public List<ReplicaDominio> consultarConversacion(UUID idKudo) {
        var kudo = consultarKudo(idKudo);
        AsegurarKudoExisteRule.obtenerInstancia().ejecutar(kudo);
        var filtro = new ReplicaEntidad.Builder()
                .kudo(new KudoEntidad.Builder().id(kudo.getId()).build())
                .build();
        var replicas = ReplicaEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerReplicaDAO().consultarPorFiltro(filtro));
        replicas.sort((primera, segunda) -> primera.getFechaCreacion().compareTo(segunda.getFechaCreacion()));
        return replicas;
    }

    private ReplicaDominio publicar(UUID idActor, KudoDominio kudo, UUID idReplicaPadre, String contenido) {
        var replica = new ReplicaDominio.Builder()
                .id(UtilId.generarId())
                .autor(new UsuarioDominio.Builder().id(idActor).build())
                .kudo(kudo)
                .idReplicaPadre(idReplicaPadre)
                .contenido(contenido)
                .fechaCreacion(UtilFecha.fechaActual())
                .estado(EstadoContenido.CREADO)
                .build();
        replica.publicar();
        daoFactory.obtenerReplicaDAO().crear(ReplicaEntidadAssembler.getInstance().convertirAEntidad(replica));
        return replica;
    }

    private KudoDominio consultarKudo(UUID idKudo) {
        return KudoEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerKudoDAO().consultarPorId(UtilId.valorDefecto(idKudo)));
    }

    private UsuarioDominio consultarUsuario(UUID idUsuario) {
        return UsuarioEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerUsuarioDAO().consultarPorId(idUsuario));
    }

    private void notificar(UUID idActor, UsuarioDominio destinatario, String mensaje, UUID idKudo) {
        if (UtilId.esValorDefecto(destinatario.getId()) || destinatario.getId().equals(idActor)) {
            return;
        }
        var notificacion = new NotificacionDominio.Builder()
                .destinatario(destinatario)
                .tipo(TipoNotificacion.REPLICA_RECIBIDA)
                .mensaje(mensaje)
                .tipoReferencia(TIPO_KUDO)
                .idReferencia(idKudo)
                .build();
        notificacionNegocio.notificar(notificacion);
    }

    private void registrarAuditoria(UUID idActor, ReplicaDominio replica, String detalle) {
        var auditoria = new AuditoriaDominio.Builder()
                .actor(new UsuarioDominio.Builder().id(idActor).build())
                .accion(ACCION_PUBLICADA)
                .tipoObjeto(TIPO_OBJETO)
                .idObjeto(replica.getId())
                .estadoAnterior(EstadoContenido.CREADO.name())
                .estadoNuevo(replica.getEstado().name())
                .detalle(UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(detalle))
                .build();
        auditoriaNegocio.registrar(auditoria);
    }
}
