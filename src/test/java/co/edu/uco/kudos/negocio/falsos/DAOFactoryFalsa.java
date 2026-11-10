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
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.entidad.AsignacionRolEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

import java.time.LocalDateTime;
import java.util.UUID;

public class DAOFactoryFalsa extends DAOFactory {

    private final CategoriaDAOFalso categoriaDAO = new CategoriaDAOFalso();
    private final AsignacionRolDAOFalso asignacionRolDAO = new AsignacionRolDAOFalso();
    private final AuditoriaDAOFalso auditoriaDAO = new AuditoriaDAOFalso();
    private final UsuarioDAOFalso usuarioDAO = new UsuarioDAOFalso();

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

    public CategoriaDAOFalso getCategoriaDAOFalso() {
        return categoriaDAO;
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
        return null;
    }

    @Override
    public FacultadDAO obtenerFacultadDAO() {
        return null;
    }

    @Override
    public ProgramaAcademicoDAO obtenerProgramaAcademicoDAO() {
        return null;
    }

    @Override
    public CodigoVerificacionDAO obtenerCodigoVerificacionDAO() {
        return null;
    }

    @Override
    public SolicitudRegistroDAO obtenerSolicitudRegistroDAO() {
        return null;
    }

    @Override
    public VinculacionProgramaDAO obtenerVinculacionProgramaDAO() {
        return null;
    }

    @Override
    public KudoDAO obtenerKudoDAO() {
        return null;
    }

    @Override
    public ReplicaDAO obtenerReplicaDAO() {
        return null;
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
        return null;
    }

    @Override
    public NotificacionDAO obtenerNotificacionDAO() {
        return null;
    }
}
