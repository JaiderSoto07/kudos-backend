package co.edu.uco.kudos.negocio.negocio.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.AuditoriaDominio;
import co.edu.uco.kudos.dominio.EstadoCuenta;
import co.edu.uco.kudos.dominio.EstadoSolicitud;
import co.edu.uco.kudos.dominio.FacultadDominio;
import co.edu.uco.kudos.dominio.ProgramaAcademicoDominio;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dominio.SolicitudRegistroDominio;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.entidad.SolicitudRegistroEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.negocio.negocio.AuditoriaNegocio;
import co.edu.uco.kudos.negocio.negocio.RegistroNegocio;
import co.edu.uco.kudos.negocio.negocio.SolicitudNegocio;
import co.edu.uco.kudos.negocio.negocio.VerificacionNegocio;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.FacultadEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.ProgramaAcademicoEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.SolicitudRegistroEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.assembler.impl.UsuarioEntidadAssembler;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.aprobacion.AsegurarSolicitudRechazadaRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.autenticacion.AsegurarUsuarioRegistradoRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.AsegurarAmbitoSegunRolRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.AsegurarCelularNoRepetidoRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.AsegurarCorreoNoRepetidoRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.AsegurarRolAutorregistrableRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.ValidarCelularRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.ValidarCorreoInstitucionalRule;
import co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad.ValidarDatosPersonalesRule;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RegistroNegocioImpl implements RegistroNegocio {

    private static final String TIPO_OBJETO = "SolicitudRegistro";
    private static final String ACCION_SOLICITUD_CREADA = "SolicitudRegistroCreada";

    private final DAOFactory daoFactory;
    private final VerificacionNegocio verificacionNegocio;
    private final SolicitudNegocio solicitudNegocio;
    private final AuditoriaNegocio auditoriaNegocio;

    public RegistroNegocioImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.verificacionNegocio = new VerificacionNegocioImpl(daoFactory);
        this.solicitudNegocio = new SolicitudNegocioImpl(daoFactory);
        this.auditoriaNegocio = new AuditoriaNegocioImpl(daoFactory);
    }

    @Override
    public void registrarSolicitud(String correo, String rolSolicitado, SolicitudRegistroDominio datos) {
        var solicitante = datos.getSolicitante();
        var correoLimpio = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(correo).toLowerCase();
        var celularLimpio = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(solicitante.getNumeroCelular());

        ValidarCorreoInstitucionalRule.obtenerInstancia().ejecutar(correoLimpio, daoFactory);
        AsegurarCorreoNoRepetidoRule.obtenerInstancia().ejecutar(correoLimpio, UtilId.VALOR_DEFECTO, daoFactory);
        ValidarDatosPersonalesRule.obtenerInstancia().ejecutar(solicitante.getNombres(), solicitante.getApellidos());
        ValidarCelularRule.obtenerInstancia().ejecutar(celularLimpio);
        AsegurarCelularNoRepetidoRule.obtenerInstancia().ejecutar(celularLimpio, UtilId.VALOR_DEFECTO, daoFactory);
        AsegurarRolAutorregistrableRule.obtenerInstancia().ejecutar(rolSolicitado);
        var rol = UtilEnum.obtenerValor(Rol.class, rolSolicitado, null);
        AsegurarAmbitoSegunRolRule.obtenerInstancia().ejecutar(rol, datos.getFacultad().getId(), datos.getPrograma().getId(), daoFactory);

        var usuarioNuevo = new UsuarioDominio.Builder()
                .id(UtilId.generarId())
                .nombres(solicitante.getNombres())
                .apellidos(solicitante.getApellidos())
                .correoInstitucional(correoLimpio)
                .correoVerificado(true)
                .numeroCelular(celularLimpio)
                .celularVerificado(false)
                .aprobada(false)
                .estadoCuenta(EstadoCuenta.PENDIENTE_APROBACION)
                .fechaRegistro(UtilFecha.fechaActual())
                .build();
        daoFactory.obtenerUsuarioDAO().crear(UsuarioEntidadAssembler.getInstance().convertirAEntidad(usuarioNuevo));

        crearSolicitud(usuarioNuevo, rol, datos);
        verificacionNegocio.enviarCodigoCelular(usuarioNuevo);
    }

    @Override
    public void registrarNuevaSolicitud(UUID idUsuario, String rolSolicitado, SolicitudRegistroDominio datos) {
        var usuario = UsuarioEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerUsuarioDAO().consultarPorId(idUsuario));
        AsegurarUsuarioRegistradoRule.obtenerInstancia().ejecutar(usuario);
        AsegurarSolicitudRechazadaRule.obtenerInstancia().ejecutar(consultarUltimaSolicitud(idUsuario));
        AsegurarRolAutorregistrableRule.obtenerInstancia().ejecutar(rolSolicitado);
        var rol = UtilEnum.obtenerValor(Rol.class, rolSolicitado, null);
        AsegurarAmbitoSegunRolRule.obtenerInstancia().ejecutar(rol, datos.getFacultad().getId(), datos.getPrograma().getId(), daoFactory);

        crearSolicitud(usuario, rol, datos);
    }

    private void crearSolicitud(UsuarioDominio solicitante, Rol rol, SolicitudRegistroDominio datos) {
        var solicitudNueva = new SolicitudRegistroDominio.Builder()
                .id(UtilId.generarId())
                .solicitante(solicitante)
                .rolSolicitado(rol)
                .facultad(rol == Rol.COORDINADOR_FACULTAD ? soloId(datos.getFacultad()) : new FacultadDominio.Builder().build())
                .programa(necesitaPrograma(rol) ? soloId(datos.getPrograma()) : new ProgramaAcademicoDominio.Builder().build())
                .estado(EstadoSolicitud.PENDIENTE)
                .fechaSolicitud(UtilFecha.fechaActual())
                .build();
        daoFactory.obtenerSolicitudRegistroDAO().crear(SolicitudRegistroEntidadAssembler.getInstance().convertirAEntidad(solicitudNueva));

        solicitudNegocio.notificarAutoridades(solicitudNueva.getId());
        registrarAuditoria(solicitante.getId(), solicitudNueva.getId(), rol);
    }

    private SolicitudRegistroDominio consultarUltimaSolicitud(UUID idUsuario) {
        var filtro = new SolicitudRegistroEntidad.Builder()
                .solicitante(new UsuarioEntidad.Builder().id(idUsuario).build())
                .build();
        var solicitudes = daoFactory.obtenerSolicitudRegistroDAO().consultarPorFiltro(filtro);
        if (solicitudes.isEmpty()) {
            return new SolicitudRegistroDominio.Builder().build();
        }
        return SolicitudRegistroEntidadAssembler.getInstance().convertirADominio(solicitudes.getFirst());
    }

    @Override
    public List<FacultadDominio> consultarFacultadesDisponibles() {
        var facultades = FacultadEntidadAssembler.getInstance().convertirADominio(daoFactory.obtenerFacultadDAO().consultarTodos());
        var disponibles = new ArrayList<FacultadDominio>();
        for (var facultad : facultades) {
            if (facultad.admiteCoordinador()) {
                disponibles.add(facultad);
            }
        }
        return disponibles;
    }

    @Override
    public List<ProgramaAcademicoDominio> consultarProgramasDisponibles() {
        var programas = daoFactory.obtenerProgramaAcademicoDAO().consultarActivos();
        return ProgramaAcademicoEntidadAssembler.getInstance().convertirADominio(programas);
    }

    private boolean necesitaPrograma(Rol rol) {
        return rol == Rol.ESTUDIANTE || rol == Rol.DOCENTE || rol == Rol.COORDINADOR_PROGRAMA;
    }

    private FacultadDominio soloId(FacultadDominio facultad) {
        return new FacultadDominio.Builder().id(facultad.getId()).build();
    }

    private ProgramaAcademicoDominio soloId(ProgramaAcademicoDominio programa) {
        return new ProgramaAcademicoDominio.Builder().id(programa.getId()).build();
    }

    private void registrarAuditoria(UUID idSolicitante, UUID idSolicitud, Rol rol) {
        var auditoria = new AuditoriaDominio.Builder()
                .actor(new UsuarioDominio.Builder().id(idSolicitante).build())
                .accion(ACCION_SOLICITUD_CREADA)
                .tipoObjeto(TIPO_OBJETO)
                .idObjeto(idSolicitud)
                .estadoAnterior(UtilTexto.VACIO)
                .estadoNuevo(EstadoSolicitud.PENDIENTE.name())
                .detalle(rol.name())
                .build();
        auditoriaNegocio.registrar(auditoria);
    }
}
