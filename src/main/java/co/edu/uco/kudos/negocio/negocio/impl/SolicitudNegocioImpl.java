package co.edu.uco.kudos.negocio.negocio.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.AsignacionRolDominio;
import co.edu.uco.kudos.dominio.AuditoriaDominio;
import co.edu.uco.kudos.dominio.EstadoCuenta;
import co.edu.uco.kudos.dominio.EstadoSolicitud;
import co.edu.uco.kudos.dominio.FacultadDominio;
import co.edu.uco.kudos.dominio.NotificacionDominio;
import co.edu.uco.kudos.dominio.ProgramaAcademicoDominio;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dominio.SolicitudRegistroDominio;
import co.edu.uco.kudos.dominio.TipoNotificacion;
import co.edu.uco.kudos.dominio.TipoVinculo;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.dominio.VinculacionProgramaDominio;
import co.edu.uco.kudos.entidad.AsignacionRolEntidad;
import co.edu.uco.kudos.entidad.FacultadEntidad;
import co.edu.uco.kudos.entidad.ProgramaAcademicoEntidad;
import co.edu.uco.kudos.entidad.SolicitudRegistroEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.negocio.negocio.AuditoriaNegocio;
import co.edu.uco.kudos.negocio.negocio.NotificacionNegocio;
import co.edu.uco.kudos.negocio.negocio.SolicitudNegocio;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.AsignacionRolEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.SolicitudRegistroEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.UsuarioEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.VinculacionProgramaEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.aprobacion.AsegurarAutoridadCompetenteRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.aprobacion.AsegurarNoEsPropiaSolicitudRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.aprobacion.AsegurarSolicitudExisteRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.aprobacion.AsegurarSolicitudPendienteRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.aprobacion.ValidarMotivoRechazoRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.autorizacion.AsegurarActorTieneRolRule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SolicitudNegocioImpl implements SolicitudNegocio {

    private static final List<Rol> ROLES_APROBADORES = List.of(Rol.RECTOR, Rol.COORDINADOR_ACADEMICO,
            Rol.COORDINADOR_FACULTAD, Rol.COORDINADOR_PROGRAMA);
    private static final String TIPO_SOLICITUD = "SolicitudRegistro";
    private static final String TIPO_ASIGNACION = "AsignacionRol";
    private static final String ACCION_APROBADA = "SolicitudAprobada";
    private static final String ACCION_RECHAZADA = "SolicitudRechazada";
    private static final String ACCION_SUSTITUIDO = "CargoSustituido";
    private static final String ESTADO_VIGENTE = "VIGENTE";
    private static final String ESTADO_CERRADA = "CERRADA";

    private final DAOFactory daoFactory;
    private final NotificacionNegocio notificacionNegocio;
    private final AuditoriaNegocio auditoriaNegocio;

    public SolicitudNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.notificacionNegocio = new NotificacionNegocioImpl(daoFactory);
        this.auditoriaNegocio = new AuditoriaNegocioImpl(daoFactory);
    }

    @Override
    public List<SolicitudRegistroDominio> consultarPendientes(UUID idActor) {
        AsegurarActorTieneRolRule.obtenerInstancia().ejecutar(idActor, daoFactory, ROLES_APROBADORES,
                CatalogoMensajes.Aprobacion.ACTOR_NO_APRUEBA_SOLICITUDES);
        var asignacionDelActor = consultarAsignacionVigente(idActor);

        var filtro = new SolicitudRegistroEntidad.Builder().estado(EstadoSolicitud.PENDIENTE.name()).build();
        var pendientes = SolicitudRegistroEntidadAssembler.getInstance()
                .convertirADominio(daoFactory.obtenerSolicitudRegistroDAO().consultarPorFiltro(filtro));

        var deMiAmbito = new ArrayList<SolicitudRegistroDominio>();
        for (var solicitud : pendientes) {
            if (asignacionDelActor.puedeDecidir(solicitud) && !solicitud.getSolicitante().getId().equals(idActor)) {
                deMiAmbito.add(solicitud);
            }
        }
        return deMiAmbito;
    }

    @Override
    public void aprobar(UUID idActor, UUID idSolicitud) {
        var solicitud = consultarSolicitud(idSolicitud);
        validarDecision(idActor, solicitud);

        solicitud.aprobar(new UsuarioDominio.Builder().id(idActor).build());
        guardarSolicitud(solicitud);

        var rol = solicitud.getRolSolicitado();
        if (rol.esCoordinador()) {
            sustituirTitularActual(idActor, solicitud);
        }
        crearAsignacion(solicitud);
        if (rol == Rol.ESTUDIANTE || rol == Rol.DOCENTE) {
            crearVinculacion(solicitud);
        }

        var solicitante = consultarUsuario(solicitud.getSolicitante().getId());
        solicitante.aprobar();
        guardarUsuario(solicitante);

        notificar(solicitante.getId(), TipoNotificacion.SOLICITUD_APROBADA,
                String.format(CatalogoMensajes.Aprobacion.MENSAJE_SOLICITUD_APROBADA, rol.nombreLegible()), idSolicitud);
        registrarAuditoria(idActor, ACCION_APROBADA, TIPO_SOLICITUD, idSolicitud, EstadoSolicitud.PENDIENTE.name(), EstadoSolicitud.APROBADA.name(), rol.name());
    }

    @Override
    public void rechazar(UUID idActor, UUID idSolicitud, String motivo) {
        var solicitud = consultarSolicitud(idSolicitud);
        validarDecision(idActor, solicitud);
        ValidarMotivoRechazoRule.obtenerInstancia().ejecutar(motivo);

        solicitud.rechazar(new UsuarioDominio.Builder().id(idActor).build(), motivo);
        guardarSolicitud(solicitud);

        var rol = solicitud.getRolSolicitado();
        notificar(solicitud.getSolicitante().getId(), TipoNotificacion.SOLICITUD_RECHAZADA,
                String.format(CatalogoMensajes.Aprobacion.MENSAJE_SOLICITUD_RECHAZADA, rol.nombreLegible(), solicitud.getMotivoRechazo()),
                idSolicitud);
        registrarAuditoria(idActor, ACCION_RECHAZADA, TIPO_SOLICITUD, idSolicitud, EstadoSolicitud.PENDIENTE.name(), EstadoSolicitud.RECHAZADA.name(), solicitud.getMotivoRechazo());
    }

    @Override
    public void notificarAutoridades(UUID idSolicitud) {
        var solicitud = consultarSolicitud(idSolicitud);
        var rolSolicitado = solicitud.getRolSolicitado();
        var filtro = new AsignacionRolEntidad.Builder().rol(rolSolicitado.rolAprobador().name()).build();
        var autoridades = AsignacionRolEntidadAssembler.getInstance()
                .convertirADominio(daoFactory.obtenerAsignacionRolDAO().consultarPorFiltro(filtro));

        var solicitante = consultarUsuario(solicitud.getSolicitante().getId());
        var mensaje = String.format(CatalogoMensajes.Aprobacion.MENSAJE_SOLICITUD_PENDIENTE, solicitante.nombreCompleto(),
                rolSolicitado.nombreLegible());
        for (var autoridad : autoridades) {
            if (autoridad.puedeDecidir(solicitud)) {
                notificar(autoridad.getUsuario().getId(), TipoNotificacion.SOLICITUD_PENDIENTE, mensaje, idSolicitud);
            }
        }
    }

    private void validarDecision(UUID idActor, SolicitudRegistroDominio solicitud) {
        AsegurarSolicitudExisteRule.obtenerInstancia().ejecutar(solicitud);
        AsegurarSolicitudPendienteRule.obtenerInstancia().ejecutar(solicitud);
        AsegurarNoEsPropiaSolicitudRule.obtenerInstancia().ejecutar(idActor, solicitud);
        AsegurarAutoridadCompetenteRule.obtenerInstancia().ejecutar(consultarAsignacionVigente(idActor), solicitud);
    }

    private void sustituirTitularActual(UUID idActor, SolicitudRegistroDominio solicitud) {
        var rol = solicitud.getRolSolicitado();
        var filtro = new AsignacionRolEntidad.Builder()
                .rol(rol.name())
                .facultad(new FacultadEntidad.Builder().id(rol == Rol.COORDINADOR_FACULTAD ? solicitud.getFacultad().getId() : UtilId.VALOR_DEFECTO).build())
                .programa(new ProgramaAcademicoEntidad.Builder().id(rol == Rol.COORDINADOR_PROGRAMA ? solicitud.getPrograma().getId() : UtilId.VALOR_DEFECTO).build())
                .build();
        var asignaciones = AsignacionRolEntidadAssembler.getInstance()
                .convertirADominio(daoFactory.obtenerAsignacionRolDAO().consultarPorFiltro(filtro));

        for (var asignacion : asignaciones) {
            if (asignacion.estaVigente()) {
                asignacion.cerrar(CatalogoMensajes.Aprobacion.MOTIVO_SUSTITUCION);
                daoFactory.obtenerAsignacionRolDAO().actualizar(asignacion.getId(), AsignacionRolEntidadAssembler.getInstance().convertirAEntidad(asignacion));

                var titularSaliente = consultarUsuario(asignacion.getUsuario().getId());
                if (titularSaliente.getEstadoCuenta() != EstadoCuenta.DESACTIVADA) {
                    titularSaliente.desactivar();
                    guardarUsuario(titularSaliente);
                }
                registrarAuditoria(idActor, ACCION_SUSTITUIDO, TIPO_ASIGNACION, asignacion.getId(), ESTADO_VIGENTE, ESTADO_CERRADA, asignacion.getMotivoFin());
            }
        }
    }

    private void crearAsignacion(SolicitudRegistroDominio solicitud) {
        var rol = solicitud.getRolSolicitado();
        var asignacion = new AsignacionRolDominio.Builder()
                .id(UtilId.generarId())
                .usuario(solicitud.getSolicitante())
                .rol(rol)
                .facultad(rol == Rol.COORDINADOR_FACULTAD ? solicitud.getFacultad() : new FacultadDominio.Builder().build())
                .programa(rol == Rol.COORDINADOR_PROGRAMA ? solicitud.getPrograma() : new ProgramaAcademicoDominio.Builder().build())
                .fechaInicio(UtilFecha.fechaActual())
                .build();
        daoFactory.obtenerAsignacionRolDAO().crear(AsignacionRolEntidadAssembler.getInstance().convertirAEntidad(asignacion));
    }

    private void crearVinculacion(SolicitudRegistroDominio solicitud) {
        var vinculacion = new VinculacionProgramaDominio.Builder()
                .id(UtilId.generarId())
                .usuario(solicitud.getSolicitante())
                .programa(solicitud.getPrograma())
                .tipoVinculo(UtilEnum.obtenerValor(TipoVinculo.class, solicitud.getRolSolicitado().name(), TipoVinculo.ESTUDIANTE))
                .fechaInicio(UtilFecha.fechaActual())
                .build();
        daoFactory.obtenerVinculacionProgramaDAO().crear(VinculacionProgramaEntidadAssembler.getInstance().convertirAEntidad(vinculacion));
    }

    private AsignacionRolDominio consultarAsignacionVigente(UUID idUsuario) {
        var filtro = new AsignacionRolEntidad.Builder().usuario(new UsuarioEntidad.Builder().id(idUsuario).build()).build();
        var asignaciones = AsignacionRolEntidadAssembler.getInstance()
                .convertirADominio(daoFactory.obtenerAsignacionRolDAO().consultarPorFiltro(filtro));
        for (var asignacion : asignaciones) {
            if (asignacion.estaVigente()) {
                return asignacion;
            }
        }
        return new AsignacionRolDominio.Builder().build();
    }

    private SolicitudRegistroDominio consultarSolicitud(UUID idSolicitud) {
        var solicitud = daoFactory.obtenerSolicitudRegistroDAO().consultarPorId(idSolicitud);
        return SolicitudRegistroEntidadAssembler.getInstance().convertirADominio(solicitud);
    }

    private void guardarSolicitud(SolicitudRegistroDominio solicitud) {
        daoFactory.obtenerSolicitudRegistroDAO().actualizar(solicitud.getId(), SolicitudRegistroEntidadAssembler.getInstance().convertirAEntidad(solicitud));
    }

    private UsuarioDominio consultarUsuario(UUID idUsuario) {
        return UsuarioEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerUsuarioDAO().consultarPorId(idUsuario));
    }

    private void guardarUsuario(UsuarioDominio usuario) {
        daoFactory.obtenerUsuarioDAO().actualizar(usuario.getId(), UsuarioEntidadAssembler.getInstance().convertirAEntidad(usuario));
    }

    private void notificar(UUID idDestinatario, TipoNotificacion tipo, String mensaje, UUID idSolicitud) {
        var notificacion = new NotificacionDominio.Builder()
                .destinatario(new UsuarioDominio.Builder().id(idDestinatario).build())
                .tipo(tipo)
                .mensaje(mensaje)
                .tipoReferencia(TIPO_SOLICITUD)
                .idReferencia(idSolicitud)
                .build();
        notificacionNegocio.notificar(notificacion);
    }

    private void registrarAuditoria(UUID idActor, String accion, String tipoObjeto, UUID idObjeto, String estadoAnterior, String estadoNuevo, String detalle) {
        var auditoria = new AuditoriaDominio.Builder()
                .actor(new UsuarioDominio.Builder().id(idActor).build())
                .accion(accion)
                .tipoObjeto(tipoObjeto)
                .idObjeto(idObjeto)
                .estadoAnterior(estadoAnterior)
                .estadoNuevo(estadoNuevo)
                .detalle(detalle)
                .build();
        auditoriaNegocio.registrar(auditoria);
    }
}
