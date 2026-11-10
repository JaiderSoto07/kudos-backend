package co.edu.uco.kudos.negocio.falsos;

import co.edu.uco.kudos.dao.datos.entidad.AsignacionRolDAO;
import co.edu.uco.kudos.dao.datos.entidad.AuditoriaDAO;
import co.edu.uco.kudos.dao.datos.entidad.CalificacionDAO;
import co.edu.uco.kudos.dao.datos.entidad.CategoriaDAO;
import co.edu.uco.kudos.dao.datos.entidad.CodigoVerificacionDAO;
import co.edu.uco.kudos.dao.datos.entidad.FacultadDAO;
import co.edu.uco.kudos.dao.datos.entidad.KudoDAO;
import co.edu.uco.kudos.dao.datos.entidad.ModeracionDAO;
import co.edu.uco.kudos.dao.datos.entidad.NotificacionDAO;
import co.edu.uco.kudos.dao.datos.entidad.ParametroGlobalDAO;
import co.edu.uco.kudos.dao.datos.entidad.ProgramaAcademicoDAO;
import co.edu.uco.kudos.dao.datos.entidad.ReplicaDAO;
import co.edu.uco.kudos.dao.datos.entidad.ReporteDAO;
import co.edu.uco.kudos.dao.datos.entidad.SolicitudRegistroDAO;
import co.edu.uco.kudos.dao.datos.entidad.UsuarioDAO;
import co.edu.uco.kudos.dao.datos.entidad.VinculacionProgramaDAO;
import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.EstadoCuenta;
import co.edu.uco.kudos.dominio.EstadoFacultad;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.entidad.AsignacionRolEntidad;
import co.edu.uco.kudos.entidad.CategoriaEntidad;
import co.edu.uco.kudos.entidad.FacultadEntidad;
import co.edu.uco.kudos.entidad.ParametroGlobalEntidad;
import co.edu.uco.kudos.entidad.ProgramaAcademicoEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.entidad.VinculacionProgramaEntidad;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

import java.time.LocalDateTime;
import java.util.UUID;

public class DAOFactoryFalsa extends DAOFactory {

    private final CategoriaDAOFalso categoriaDAO = new CategoriaDAOFalso();
    private final AsignacionRolDAOFalso asignacionRolDAO = new AsignacionRolDAOFalso();
    private final AuditoriaDAOFalso auditoriaDAO = new AuditoriaDAOFalso();
    private final UsuarioDAOFalso usuarioDAO = new UsuarioDAOFalso();
    private final ParametroGlobalDAOFalso parametroGlobalDAO = new ParametroGlobalDAOFalso();
    private final FacultadDAOFalso facultadDAO = new FacultadDAOFalso();
    private final ProgramaAcademicoDAOFalso programaAcademicoDAO = new ProgramaAcademicoDAOFalso();
    private final SolicitudRegistroDAOFalso solicitudRegistroDAO = new SolicitudRegistroDAOFalso();
    private final CodigoVerificacionDAOFalso codigoVerificacionDAO = new CodigoVerificacionDAOFalso();
    private final NotificacionDAOFalso notificacionDAO = new NotificacionDAOFalso();
    private final VinculacionProgramaDAOFalso vinculacionProgramaDAO = new VinculacionProgramaDAOFalso();
    private final KudoDAOFalso kudoDAO = new KudoDAOFalso();
    private final ReplicaDAOFalso replicaDAO = new ReplicaDAOFalso();
    private final CalificacionDAOFalso calificacionDAO = new CalificacionDAOFalso();

    @Override
    protected void abrirConexion() {
    }

    public UUID registrarCuenta(String correo, EstadoCuenta estadoCuenta, Rol rol) {
        var idUsuario = registrarUsuarioConRol(rol);
        usuarioDAO.crear(new UsuarioEntidad.Builder()
                .id(idUsuario)
                .nombres("Ana")
                .apellidos("Pérez")
                .correoInstitucional(correo)
                .correoVerificado(true)
                .celularVerificado(estadoCuenta != EstadoCuenta.PENDIENTE_VERIFICACION)
                .aprobada(estadoCuenta != EstadoCuenta.PENDIENTE_APROBACION)
                .estadoCuenta(estadoCuenta.name())
                .build());
        return idUsuario;
    }

    public UUID registrarUsuarioConRol(Rol rol) {
        return registrarUsuario(rol, UtilFecha.FECHA_HORA_POR_DEFECTO);
    }

    public UUID registrarUsuarioConRolCerrado(Rol rol) {
        return registrarUsuario(rol, UtilFecha.fechaActual());
    }

    private UUID registrarUsuario(Rol rol, LocalDateTime fechaFin) {
        var idUsuario = UtilId.generarId();
        asignacionRolDAO.crear(new AsignacionRolEntidad.Builder()
                .id(UtilId.generarId())
                .usuario(new UsuarioEntidad.Builder().id(idUsuario).build())
                .rol(rol.name())
                .fechaInicio(UtilFecha.fechaActual())
                .fechaFin(fechaFin)
                .build());
        return idUsuario;
    }

    public UUID registrarUsuarioConContacto(String correo, String celular) {
        var idUsuario = UtilId.generarId();
        usuarioDAO.crear(new UsuarioEntidad.Builder()
                .id(idUsuario)
                .nombres("Ana")
                .apellidos("Pérez")
                .correoInstitucional(correo)
                .numeroCelular(celular)
                .estadoCuenta(EstadoCuenta.ACTIVA.name())
                .build());
        return idUsuario;
    }

    public void registrarParametro(String clave, String valor) {
        parametroGlobalDAO.crear(new ParametroGlobalEntidad.Builder()
                .id(UtilId.generarId())
                .clave(clave)
                .valor(valor)
                .tipoDato(valor.matches("[0-9]+") ? "ENTERO" : "TEXTO")
                .build());
    }

    public UUID registrarFacultad(EstadoFacultad estado) {
        var idFacultad = UtilId.generarId();
        facultadDAO.crear(new FacultadEntidad.Builder()
                .id(idFacultad)
                .nombre("Facultad " + idFacultad)
                .estado(estado.name())
                .build());
        return idFacultad;
    }

    public UUID registrarPrograma(boolean activo) {
        var idPrograma = UtilId.generarId();
        programaAcademicoDAO.crear(new ProgramaAcademicoEntidad.Builder()
                .id(idPrograma)
                .nombre("Programa " + idPrograma)
                .activo(activo)
                .build());
        return idPrograma;
    }

    public UUID registrarPrograma(UUID idFacultad) {
        var idPrograma = UtilId.generarId();
        programaAcademicoDAO.crear(new ProgramaAcademicoEntidad.Builder()
                .id(idPrograma)
                .nombre("Programa " + idPrograma)
                .facultad(new FacultadEntidad.Builder().id(idFacultad).estado(EstadoFacultad.ACTIVA.name()).build())
                .activo(true)
                .build());
        return idPrograma;
    }

    public ProgramaAcademicoEntidad consultarPrograma(UUID idPrograma) {
        return programaAcademicoDAO.consultarPorId(idPrograma);
    }

    public UUID registrarAutoridad(Rol rol, UUID idFacultad, UUID idPrograma) {
        var idUsuario = UtilId.generarId();
        usuarioDAO.crear(new UsuarioEntidad.Builder()
                .id(idUsuario)
                .nombres("Autoridad")
                .apellidos(rol.name())
                .correoInstitucional(idUsuario + "@gmail.com")
                .correoVerificado(true)
                .celularVerificado(true)
                .aprobada(true)
                .estadoCuenta(EstadoCuenta.ACTIVA.name())
                .build());
        asignacionRolDAO.crear(new AsignacionRolEntidad.Builder()
                .id(UtilId.generarId())
                .usuario(new UsuarioEntidad.Builder().id(idUsuario).build())
                .rol(rol.name())
                .facultad(new FacultadEntidad.Builder().id(idFacultad).build())
                .programa(idPrograma == null ? new ProgramaAcademicoEntidad.Builder().build() : programaAcademicoDAO.consultarPorId(idPrograma))
                .fechaInicio(UtilFecha.fechaActual())
                .fechaFin(UtilFecha.FECHA_HORA_POR_DEFECTO)
                .build());
        return idUsuario;
    }

    public UUID registrarPersona(String correo, Rol rol, EstadoCuenta estado) {
        var idUsuario = registrarAutoridad(rol, null, null);
        var usuario = usuarioDAO.consultarPorId(idUsuario);
        usuarioDAO.actualizar(idUsuario, new UsuarioEntidad.Builder()
                .id(idUsuario)
                .nombres("Persona")
                .apellidos(rol.name())
                .correoInstitucional(correo)
                .correoVerificado(true)
                .celularVerificado(true)
                .aprobada(usuario.isAprobada())
                .estadoCuenta(estado.name())
                .build());
        return idUsuario;
    }

    public UUID registrarVinculo(UUID idUsuario, UUID idPrograma, String tipoVinculo) {
        var idVinculo = UtilId.generarId();
        vinculacionProgramaDAO.crear(new VinculacionProgramaEntidad.Builder()
                .id(idVinculo)
                .usuario(usuarioDAO.consultarPorId(idUsuario))
                .programa(programaAcademicoDAO.consultarPorId(idPrograma))
                .tipoVinculo(tipoVinculo)
                .fechaInicio(UtilFecha.fechaActual())
                .fechaFin(UtilFecha.FECHA_HORA_POR_DEFECTO)
                .build());
        return idVinculo;
    }

    public CategoriaDAOFalso getCategoriaDAOFalso() {
        return categoriaDAO;
    }

    public KudoDAOFalso getKudoDAOFalso() {
        return kudoDAO;
    }

    public ReplicaDAOFalso getReplicaDAOFalso() {
        return replicaDAO;
    }

    public CalificacionDAOFalso getCalificacionDAOFalso() {
        return calificacionDAO;
    }

    public UUID registrarCategoria(String nombre, boolean activa) {
        var idCategoria = UtilId.generarId();
        categoriaDAO.crear(new CategoriaEntidad.Builder()
                .id(idCategoria)
                .nombre(nombre)
                .descripcion("Categoría de prueba para Kudos.")
                .activa(activa)
                .build());
        return idCategoria;
    }

    public FacultadDAOFalso getFacultadDAOFalso() {
        return facultadDAO;
    }

    public UsuarioDAOFalso getUsuarioDAOFalso() {
        return usuarioDAO;
    }

    public SolicitudRegistroDAOFalso getSolicitudRegistroDAOFalso() {
        return solicitudRegistroDAO;
    }

    public CodigoVerificacionDAOFalso getCodigoVerificacionDAOFalso() {
        return codigoVerificacionDAO;
    }

    public AsignacionRolDAOFalso getAsignacionRolDAOFalso() {
        return asignacionRolDAO;
    }

    public VinculacionProgramaDAOFalso getVinculacionProgramaDAOFalso() {
        return vinculacionProgramaDAO;
    }

    public NotificacionDAOFalso getNotificacionDAOFalso() {
        return notificacionDAO;
    }

    public AuditoriaDAOFalso getAuditoriaDAOFalso() {
        return auditoriaDAO;
    }

    @Override
    public CategoriaDAO obtenerCategoriaDAO() {
        return categoriaDAO;
    }

    @Override
    public AsignacionRolDAO obtenerAsignacionRolDAO() {
        return asignacionRolDAO;
    }

    @Override
    public AuditoriaDAO obtenerAuditoriaDAO() {
        return auditoriaDAO;
    }

    @Override
    public UsuarioDAO obtenerUsuarioDAO() {
        return usuarioDAO;
    }

    @Override
    public ParametroGlobalDAO obtenerParametroGlobalDAO() {
        return parametroGlobalDAO;
    }

    @Override
    public FacultadDAO obtenerFacultadDAO() {
        return facultadDAO;
    }

    @Override
    public ProgramaAcademicoDAO obtenerProgramaAcademicoDAO() {
        return programaAcademicoDAO;
    }

    @Override
    public CodigoVerificacionDAO obtenerCodigoVerificacionDAO() {
        return codigoVerificacionDAO;
    }

    @Override
    public SolicitudRegistroDAO obtenerSolicitudRegistroDAO() {
        return solicitudRegistroDAO;
    }

    @Override
    public VinculacionProgramaDAO obtenerVinculacionProgramaDAO() {
        return vinculacionProgramaDAO;
    }

    @Override
    public KudoDAO obtenerKudoDAO() {
        return kudoDAO;
    }

    @Override
    public ReplicaDAO obtenerReplicaDAO() {
        return replicaDAO;
    }

    @Override
    public ModeracionDAO obtenerModeracionDAO() {
        return null;
    }

    @Override
    public ReporteDAO obtenerReporteDAO() {
        return null;
    }

    @Override
    public CalificacionDAO obtenerCalificacionDAO() {
        return calificacionDAO;
    }

    @Override
    public NotificacionDAO obtenerNotificacionDAO() {
        return notificacionDAO;
    }
}
