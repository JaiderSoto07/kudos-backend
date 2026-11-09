package co.edu.uco.kudos.dao.factoria;

import co.edu.uco.kudos.dao.datos.entidad.UsuarioDAO;
import co.edu.uco.kudos.dao.datos.entidad.ParametroGlobalDAO;
import co.edu.uco.kudos.dao.datos.entidad.FacultadDAO;
import co.edu.uco.kudos.dao.datos.entidad.ProgramaAcademicoDAO;
import co.edu.uco.kudos.dao.datos.entidad.CategoriaDAO;
import co.edu.uco.kudos.dao.datos.entidad.AsignacionRolDAO;
import co.edu.uco.kudos.dao.datos.entidad.CodigoVerificacionDAO;
import co.edu.uco.kudos.dao.datos.entidad.SolicitudRegistroDAO;
import co.edu.uco.kudos.dao.datos.entidad.VinculacionProgramaDAO;
import co.edu.uco.kudos.dao.datos.entidad.KudoDAO;
import co.edu.uco.kudos.dao.datos.entidad.ReplicaDAO;
import co.edu.uco.kudos.dao.datos.entidad.ModeracionDAO;
import co.edu.uco.kudos.dao.datos.entidad.ReporteDAO;
import co.edu.uco.kudos.dao.datos.entidad.CalificacionDAO;
import co.edu.uco.kudos.dao.datos.entidad.AuditoriaDAO;
import co.edu.uco.kudos.dao.datos.entidad.NotificacionDAO;
import co.edu.uco.kudos.dao.factoria.enums.FactoriaEnum;
import co.edu.uco.kudos.dao.factoria.impl.SqlServerDAOFactory;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosDatosExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilSql;

import java.sql.Connection;

public abstract class DAOFactory {

    private static final FactoriaEnum FACTORIA = FactoriaEnum.SQLSERVER;

    private Connection conexion;

    protected DAOFactory() {
        abrirConexion();
    }

    public static DAOFactory obtenerFactoria() {
        if (FACTORIA == FactoriaEnum.SQLSERVER) {
            return new SqlServerDAOFactory();
        }
        throw KudosDatosExcepcion.crear(CatalogoMensajes.DAOFactory.USUARIO_ERROR_FUENTE_INFORMACION_NO_HABILITADA);
    }

    protected Connection getConexion() {
        return conexion;
    }

    protected void setConexion(Connection conexion) {
        UtilSql.asegurarConexionAbierta(conexion);
        this.conexion = conexion;
    }

    protected abstract void abrirConexion();

    public void cerrarConexion() {
        UtilSql.cerrarConexion(conexion);
    }

    public void iniciarTransaccion() {
        UtilSql.iniciarTransaccion(conexion);
    }

    public void confirmarTransaccion() {
        UtilSql.confirmarTransaccion(conexion);
    }

    public void cancelarTransaccion() {
        UtilSql.cancelarTransaccion(conexion);
    }

    public abstract UsuarioDAO obtenerUsuarioDAO();

    public abstract ParametroGlobalDAO obtenerParametroGlobalDAO();

    public abstract FacultadDAO obtenerFacultadDAO();

    public abstract ProgramaAcademicoDAO obtenerProgramaAcademicoDAO();

    public abstract CategoriaDAO obtenerCategoriaDAO();

    public abstract AsignacionRolDAO obtenerAsignacionRolDAO();

    public abstract CodigoVerificacionDAO obtenerCodigoVerificacionDAO();

    public abstract SolicitudRegistroDAO obtenerSolicitudRegistroDAO();

    public abstract VinculacionProgramaDAO obtenerVinculacionProgramaDAO();

    public abstract KudoDAO obtenerKudoDAO();

    public abstract ReplicaDAO obtenerReplicaDAO();

    public abstract ModeracionDAO obtenerModeracionDAO();

    public abstract ReporteDAO obtenerReporteDAO();

    public abstract CalificacionDAO obtenerCalificacionDAO();

    public abstract AuditoriaDAO obtenerAuditoriaDAO();

    public abstract NotificacionDAO obtenerNotificacionDAO();
}
