package co.edu.uco.kudos.dao.factoria.impl;

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
import co.edu.uco.kudos.dao.datos.entidad.sqlserver.UsuarioSqlServerDAO;
import co.edu.uco.kudos.dao.datos.entidad.sqlserver.ParametroGlobalSqlServerDAO;
import co.edu.uco.kudos.dao.datos.entidad.sqlserver.FacultadSqlServerDAO;
import co.edu.uco.kudos.dao.datos.entidad.sqlserver.ProgramaAcademicoSqlServerDAO;
import co.edu.uco.kudos.dao.datos.entidad.sqlserver.CategoriaSqlServerDAO;
import co.edu.uco.kudos.dao.datos.entidad.sqlserver.AsignacionRolSqlServerDAO;
import co.edu.uco.kudos.dao.datos.entidad.sqlserver.CodigoVerificacionSqlServerDAO;
import co.edu.uco.kudos.dao.datos.entidad.sqlserver.SolicitudRegistroSqlServerDAO;
import co.edu.uco.kudos.dao.datos.entidad.sqlserver.VinculacionProgramaSqlServerDAO;
import co.edu.uco.kudos.dao.datos.entidad.sqlserver.KudoSqlServerDAO;
import co.edu.uco.kudos.dao.datos.entidad.sqlserver.ReplicaSqlServerDAO;
import co.edu.uco.kudos.dao.datos.entidad.sqlserver.ModeracionSqlServerDAO;
import co.edu.uco.kudos.dao.datos.entidad.sqlserver.ReporteSqlServerDAO;
import co.edu.uco.kudos.dao.datos.entidad.sqlserver.CalificacionSqlServerDAO;
import co.edu.uco.kudos.dao.datos.entidad.sqlserver.AuditoriaSqlServerDAO;
import co.edu.uco.kudos.dao.datos.entidad.sqlserver.NotificacionSqlServerDAO;
import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosDatosExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class SqlServerDAOFactory extends DAOFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlServerDAOFactory.class);
    private static final String ARCHIVO_CONFIGURACION = "basedatos.properties";
    private static final String URL_POR_DEFECTO =
            "jdbc:sqlserver://localhost:1433;databaseName=Kudos;encrypt=true;trustServerCertificate=true";
    private static final int TAMANO_MAXIMO_POOL = 10;

    private static final Properties CONFIGURACION = cargarConfiguracion();
    private static final HikariDataSource POOL = crearPool();

    private static Properties cargarConfiguracion() {
        var configuracion = new Properties();
        try (var archivo = SqlServerDAOFactory.class.getClassLoader().getResourceAsStream(ARCHIVO_CONFIGURACION)) {
            if (archivo != null) {
                configuracion.load(archivo);
            } else {
                LOGGER.warn("No se encontró el archivo {} en src/main/resources; se usan las variables de entorno", ARCHIVO_CONFIGURACION);
            }
        } catch (IOException excepcion) {
            LOGGER.error("No fue posible leer el archivo {}", ARCHIVO_CONFIGURACION, excepcion);
        }
        return configuracion;
    }

    private static String leerValor(String variableEntorno, String propiedad, String valorPorDefecto) {
        var desdeEntorno = System.getenv(variableEntorno);
        if (!UtilTexto.getUtilTexto().esVacia(desdeEntorno)) {
            return desdeEntorno;
        }
        return CONFIGURACION.getProperty(propiedad, valorPorDefecto);
    }

    private static HikariDataSource crearPool() {
        var configuracion = new HikariConfig();
        configuracion.setJdbcUrl(leerValor("KUDOS_BD_URL", "basedatos.url", URL_POR_DEFECTO));
        configuracion.setUsername(leerValor("KUDOS_BD_USUARIO", "basedatos.usuario", "kudosUser"));
        configuracion.setPassword(leerValor("KUDOS_BD_CLAVE", "basedatos.clave", UtilTexto.VACIO));
        configuracion.setMaximumPoolSize(TAMANO_MAXIMO_POOL);
        configuracion.setInitializationFailTimeout(-1);
        return new HikariDataSource(configuracion);
    }

    @Override
    protected void abrirConexion() {
        Connection conexion;
        try {
            conexion = POOL.getConnection();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.SqlServerDAOFactory.USUARIO_ERROR_ABRIENDO_CONEXION_SQL_SERVER,
                    excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.SqlServerDAOFactory.USUARIO_ERROR_NO_CONTROLADO_ABRIENDO_CONEXION_SQL_SERVER,
                    excepcion.getMessage(), excepcion);
        }
        setConexion(conexion);
    }

    @Override
    public UsuarioDAO obtenerUsuarioDAO() {
        return new UsuarioSqlServerDAO(getConexion());
    }

    @Override
    public ParametroGlobalDAO obtenerParametroGlobalDAO() {
        return new ParametroGlobalSqlServerDAO(getConexion());
    }

    @Override
    public FacultadDAO obtenerFacultadDAO() {
        return new FacultadSqlServerDAO(getConexion());
    }

    @Override
    public ProgramaAcademicoDAO obtenerProgramaAcademicoDAO() {
        return new ProgramaAcademicoSqlServerDAO(getConexion());
    }

    @Override
    public CategoriaDAO obtenerCategoriaDAO() {
        return new CategoriaSqlServerDAO(getConexion());
    }

    @Override
    public AsignacionRolDAO obtenerAsignacionRolDAO() {
        return new AsignacionRolSqlServerDAO(getConexion());
    }

    @Override
    public CodigoVerificacionDAO obtenerCodigoVerificacionDAO() {
        return new CodigoVerificacionSqlServerDAO(getConexion());
    }

    @Override
    public SolicitudRegistroDAO obtenerSolicitudRegistroDAO() {
        return new SolicitudRegistroSqlServerDAO(getConexion());
    }

    @Override
    public VinculacionProgramaDAO obtenerVinculacionProgramaDAO() {
        return new VinculacionProgramaSqlServerDAO(getConexion());
    }

    @Override
    public KudoDAO obtenerKudoDAO() {
        return new KudoSqlServerDAO(getConexion());
    }

    @Override
    public ReplicaDAO obtenerReplicaDAO() {
        return new ReplicaSqlServerDAO(getConexion());
    }

    @Override
    public ModeracionDAO obtenerModeracionDAO() {
        return new ModeracionSqlServerDAO(getConexion());
    }

    @Override
    public ReporteDAO obtenerReporteDAO() {
        return new ReporteSqlServerDAO(getConexion());
    }

    @Override
    public CalificacionDAO obtenerCalificacionDAO() {
        return new CalificacionSqlServerDAO(getConexion());
    }

    @Override
    public AuditoriaDAO obtenerAuditoriaDAO() {
        return new AuditoriaSqlServerDAO(getConexion());
    }

    @Override
    public NotificacionDAO obtenerNotificacionDAO() {
        return new NotificacionSqlServerDAO(getConexion());
    }
}
