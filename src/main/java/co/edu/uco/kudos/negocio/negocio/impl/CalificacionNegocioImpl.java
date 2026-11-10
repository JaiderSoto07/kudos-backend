package co.edu.uco.kudos.negocio.negocio.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.AuditoriaDominio;
import co.edu.uco.kudos.dominio.CalificacionDominio;
import co.edu.uco.kudos.dominio.KudoDominio;
import co.edu.uco.kudos.dominio.ReplicaDominio;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.entidad.CalificacionEntidad;
import co.edu.uco.kudos.entidad.KudoEntidad;
import co.edu.uco.kudos.entidad.ReplicaEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.negocio.negocio.AuditoriaNegocio;
import co.edu.uco.kudos.negocio.negocio.CalificacionNegocio;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.CalificacionEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.KudoEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.ReplicaEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.calificacion.AsegurarContenidoAjenoRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.calificacion.ValidarEstrellasRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.conversacion.AsegurarKudoAdmiteInteraccionRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.conversacion.AsegurarReplicaAdmiteInteraccionRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.conversacion.AsegurarReplicaExisteRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.kudo.AsegurarKudoExisteRule;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CalificacionNegocioImpl implements CalificacionNegocio {

    private static final String TIPO_OBJETO = "Calificacion";
    private static final String TIPO_KUDO = "Kudo ";
    private static final String TIPO_REPLICA = "Replica ";
    private static final String ACCION_REGISTRADA = "CalificacionRegistrada";
    private static final String ACCION_ACTUALIZADA = "CalificacionActualizada";

    private final DAOFactory daoFactory;
    private final AuditoriaNegocio auditoriaNegocio;

    public CalificacionNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.auditoriaNegocio = new AuditoriaNegocioImpl(daoFactory);
    }

    @Override
    public void calificarKudo(UUID idActor, UUID idKudo, Integer estrellas) {
        var kudo = consultarKudo(idKudo);
        AsegurarKudoExisteRule.obtenerInstancia().ejecutar(kudo);
        AsegurarKudoAdmiteInteraccionRule.obtenerInstancia().ejecutar(kudo);
        AsegurarContenidoAjenoRule.obtenerInstancia().ejecutar(idActor, kudo.getAutor().getId());
        ValidarEstrellasRule.obtenerInstancia().ejecutar(estrellas);

        var filtro = filtro(idActor, kudo.getId(), null);
        var calificacion = new CalificacionDominio.Builder()
                .autor(new UsuarioDominio.Builder().id(idActor).build())
                .kudo(kudo)
                .build();
        guardar(idActor, filtro, calificacion, estrellas, TIPO_KUDO + kudo.getId());

        kudo.actualizarResumenCalificaciones(estrellasActivas(filtro(null, kudo.getId(), null)));
        daoFactory.obtenerKudoDAO().actualizar(kudo.getId(), KudoEntidadAssembler.getInstance().convertirAEntidad(kudo));
    }

    @Override
    public void calificarReplica(UUID idActor, UUID idReplica, Integer estrellas) {
        var replica = consultarReplica(idReplica);
        AsegurarReplicaExisteRule.obtenerInstancia().ejecutar(replica);
        AsegurarKudoAdmiteInteraccionRule.obtenerInstancia().ejecutar(consultarKudo(replica.getKudo().getId()));
        AsegurarReplicaAdmiteInteraccionRule.obtenerInstancia().ejecutar(replica);
        AsegurarContenidoAjenoRule.obtenerInstancia().ejecutar(idActor, replica.getAutor().getId());
        ValidarEstrellasRule.obtenerInstancia().ejecutar(estrellas);

        var filtro = filtro(idActor, null, replica.getId());
        var calificacion = new CalificacionDominio.Builder()
                .autor(new UsuarioDominio.Builder().id(idActor).build())
                .replica(replica)
                .build();
        guardar(idActor, filtro, calificacion, estrellas, TIPO_REPLICA + replica.getId());

        replica.actualizarResumenCalificaciones(estrellasActivas(filtro(null, null, replica.getId())));
        daoFactory.obtenerReplicaDAO().actualizar(replica.getId(), ReplicaEntidadAssembler.getInstance().convertirAEntidad(replica));
    }

    @Override
    public List<CalificacionDominio> consultarValoracionesKudo(UUID idKudo) {
        var kudo = consultarKudo(idKudo);
        AsegurarKudoExisteRule.obtenerInstancia().ejecutar(kudo);
        return consultarActivas(filtro(null, kudo.getId(), null));
    }

    @Override
    public List<CalificacionDominio> consultarValoracionesReplica(UUID idReplica) {
        var replica = consultarReplica(idReplica);
        AsegurarReplicaExisteRule.obtenerInstancia().ejecutar(replica);
        return consultarActivas(filtro(null, null, replica.getId()));
    }

    private void guardar(UUID idActor, CalificacionEntidad filtro, CalificacionDominio nueva, Integer estrellas, String detalle) {
        var existentes = consultarActivas(filtro);
        if (existentes.isEmpty()) {
            var calificacion = new CalificacionDominio.Builder()
                    .id(UtilId.generarId())
                    .autor(nueva.getAutor())
                    .kudo(nueva.getKudo())
                    .replica(nueva.getReplica())
                    .estrellas(estrellas)
                    .fechaCalificacion(UtilFecha.fechaActual())
                    .activa(true)
                    .build();
            daoFactory.obtenerCalificacionDAO().crear(CalificacionEntidadAssembler.getInstance().convertirAEntidad(calificacion));
            registrarAuditoria(idActor, ACCION_REGISTRADA, calificacion.getId(), UtilTexto.VACIO, String.valueOf(estrellas), detalle);
            return;
        }

        var calificacion = existentes.getFirst();
        var estrellasAnteriores = calificacion.getEstrellas();
        calificacion.modificarEstrellas(estrellas);
        daoFactory.obtenerCalificacionDAO().actualizar(calificacion.getId(), CalificacionEntidadAssembler.getInstance().convertirAEntidad(calificacion));
        registrarAuditoria(idActor, ACCION_ACTUALIZADA, calificacion.getId(), String.valueOf(estrellasAnteriores), String.valueOf(estrellas), detalle);
    }

    private List<Integer> estrellasActivas(CalificacionEntidad filtro) {
        var estrellas = new ArrayList<Integer>();
        for (var calificacion : consultarActivas(filtro)) {
            estrellas.add(calificacion.getEstrellas());
        }
        return estrellas;
    }

    private List<CalificacionDominio> consultarActivas(CalificacionEntidad filtro) {
        var activas = new ArrayList<CalificacionDominio>();
        for (var calificacion : CalificacionEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerCalificacionDAO().consultarPorFiltro(filtro))) {
            if (calificacion.isActiva()) {
                activas.add(calificacion);
            }
        }
        return activas;
    }

    private CalificacionEntidad filtro(UUID idAutor, UUID idKudo, UUID idReplica) {
        return new CalificacionEntidad.Builder()
                .autor(new UsuarioEntidad.Builder().id(idAutor).build())
                .kudo(new KudoEntidad.Builder().id(idKudo).build())
                .replica(new ReplicaEntidad.Builder().id(idReplica).build())
                .build();
    }

    private KudoDominio consultarKudo(UUID idKudo) {
        return KudoEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerKudoDAO().consultarPorId(UtilId.valorDefecto(idKudo)));
    }

    private ReplicaDominio consultarReplica(UUID idReplica) {
        return ReplicaEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerReplicaDAO().consultarPorId(UtilId.valorDefecto(idReplica)));
    }

    private void registrarAuditoria(UUID idActor, String accion, UUID idCalificacion, String estrellasAnteriores, String estrellasNuevas, String detalle) {
        var auditoria = new AuditoriaDominio.Builder()
                .actor(new UsuarioDominio.Builder().id(idActor).build())
                .accion(accion)
                .tipoObjeto(TIPO_OBJETO)
                .idObjeto(idCalificacion)
                .estadoAnterior(estrellasAnteriores)
                .estadoNuevo(estrellasNuevas)
                .detalle(detalle)
                .build();
        auditoriaNegocio.registrar(auditoria);
    }
}
