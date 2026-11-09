package co.edu.uco.kudos.transversal.catalogo;

public final class CatalogoMensajes {

    private static final String INTENTE_DE_NUEVO = " Por favor intente de nuevo y, si el problema persiste, contacte al administrador de la plataforma.";

    private CatalogoMensajes() {
    }

    public static final class UtilSql {

        private UtilSql() {
        }

        public static final String USUARIO_ERROR_VALIDANDO_SI_CONEXION_ESTA_ABIERTA = "Se presentó un problema validando si la conexión con la base de datos está abierta." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_VALIDANDO_SI_CONEXION_ESTA_ABIERTA = "Se presentó un problema NO CONTROLADO validando si la conexión con la base de datos está abierta." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_VALIDANDO_SI_TRANSACCION_ESTA_INICIADA = "Se presentó un problema validando si la conexión con la base de datos está en un estado consistente." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_VALIDANDO_SI_TRANSACCION_ESTA_INICIADA = "Se presentó un problema NO CONTROLADO validando si la conexión con la base de datos está en un estado consistente." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONEXION_NO_ESTA_ABIERTA = "No es posible continuar porque la conexión con la base de datos está vacía o cerrada." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_ES_POSIBLE_INICIAR_TRANSACCION = "No es posible iniciar la operación porque la conexión con la base de datos está cerrada, vacía o ya tiene una transacción iniciada." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_INICIANDO_TRANSACCION = "Se presentó un problema iniciando la transacción en la base de datos." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_INICIANDO_TRANSACCION = "Se presentó un problema NO CONTROLADO iniciando la transacción en la base de datos." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_ES_POSIBLE_CONFIRMAR_TRANSACCION = "No es posible confirmar los cambios porque la conexión con la base de datos está cerrada, vacía o no tiene una transacción iniciada." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONFIRMANDO_TRANSACCION = "Se presentó un problema confirmando los cambios en la base de datos." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONFIRMANDO_TRANSACCION = "Se presentó un problema NO CONTROLADO confirmando los cambios en la base de datos." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_ES_POSIBLE_CANCELAR_TRANSACCION = "No es posible deshacer los cambios porque la conexión con la base de datos está cerrada, vacía o no tiene una transacción iniciada." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CANCELANDO_TRANSACCION = "Se presentó un problema deshaciendo los cambios en la base de datos." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CANCELANDO_TRANSACCION = "Se presentó un problema NO CONTROLADO deshaciendo los cambios en la base de datos." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_ES_POSIBLE_CERRAR_CONEXION = "No es posible cerrar la conexión con la base de datos porque está vacía o ya está cerrada." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CERRANDO_CONEXION = "Se presentó un problema cerrando la conexión con la base de datos." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CERRANDO_CONEXION = "Se presentó un problema NO CONTROLADO cerrando la conexión con la base de datos." + INTENTE_DE_NUEVO;
    }


    public static final class SqlServerDAOFactory {

        private SqlServerDAOFactory() {
        }

        public static final String USUARIO_ERROR_ABRIENDO_CONEXION_SQL_SERVER = "Se presentó un problema abriendo la conexión con la base de datos. Verifique que SQL Server esté encendido." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_ABRIENDO_CONEXION_SQL_SERVER = "Se presentó un problema NO CONTROLADO abriendo la conexión con la base de datos." + INTENTE_DE_NUEVO;
    }

    public static final class DAOFactory {

        private DAOFactory() {
        }

        public static final String USUARIO_ERROR_FUENTE_INFORMACION_NO_HABILITADA = "La fuente de información solicitada no está habilitada. Contacte al administrador de la plataforma.";
    }

    public static final class Dominio {

        private Dominio() {
        }

        public static final String CONTENIDO_CAMBIO_NO_PERMITIDO = "No es posible %s un contenido que está en estado %s.";
        public static final String USUARIO_NO_ESTA_PENDIENTE_DE_APROBACION = "La cuenta no está pendiente de aprobación.";
        public static final String USUARIO_YA_SUSPENDIDO_O_DESACTIVADO = "La cuenta ya está suspendida o desactivada.";
        public static final String USUARIO_NO_ESTA_SUSPENDIDO = "Solo se puede reactivar una cuenta suspendida.";
        public static final String USUARIO_YA_DESACTIVADO = "La cuenta ya está desactivada.";
        public static final String PARAMETRO_NO_ES_ENTERO = "El parámetro es de tipo entero: su valor debe ser un número entero sin signos.";
        public static final String FACULTAD_YA_INACTIVA = "La Facultad ya está inactiva.";
        public static final String PROGRAMA_YA_INACTIVO = "El programa ya está inactivo.";
        public static final String CATEGORIA_YA_ACTIVA = "La categoría ya está activa.";
        public static final String CATEGORIA_YA_INACTIVA = "La categoría ya está inactiva.";
        public static final String ASIGNACION_YA_CERRADA = "La asignación de rol ya está cerrada.";
        public static final String SOLICITUD_YA_DECIDIDA = "La solicitud ya fue decidida.";
        public static final String VINCULO_YA_CERRADO = "El vínculo ya está cerrado.";
        public static final String REPORTE_NO_PENDIENTE = "Solo un reporte pendiente puede pasar a revisión.";
        public static final String REPORTE_YA_RESUELTO = "El reporte ya fue resuelto.";
        public static final String MODERACION_YA_CERRADA = "La revisión ya fue cerrada.";
    }

    public static final class UsuarioSqlServerDAO {

        private UsuarioSqlServerDAO() {
        }

        public static final String USUARIO_ERROR_CREANDO = "Se presentó un problema registrando el usuario." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CREANDO = "Se presentó un problema NO CONTROLADO registrando el usuario." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_ID = "Se presentó un problema consultando el usuario." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID = "Se presentó un problema NO CONTROLADO consultando el usuario." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_FILTRO = "Se presentó un problema consultando los usuarios." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO = "Se presentó un problema NO CONTROLADO consultando los usuarios." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_TODOS = "Se presentó un problema consultando los usuarios." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS = "Se presentó un problema NO CONTROLADO consultando los usuarios." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_ACTUALIZANDO = "Se presentó un problema actualizando el usuario." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO = "Se presentó un problema NO CONTROLADO actualizando el usuario." + INTENTE_DE_NUEVO;
    }

    public static final class ParametroGlobalSqlServerDAO {

        private ParametroGlobalSqlServerDAO() {
        }

        public static final String USUARIO_ERROR_CREANDO = "Se presentó un problema registrando el parámetro global." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CREANDO = "Se presentó un problema NO CONTROLADO registrando el parámetro global." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_ID = "Se presentó un problema consultando el parámetro global." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID = "Se presentó un problema NO CONTROLADO consultando el parámetro global." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_FILTRO = "Se presentó un problema consultando los parámetros globales." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO = "Se presentó un problema NO CONTROLADO consultando los parámetros globales." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_TODOS = "Se presentó un problema consultando los parámetros globales." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS = "Se presentó un problema NO CONTROLADO consultando los parámetros globales." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_ACTUALIZANDO = "Se presentó un problema actualizando el parámetro global." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO = "Se presentó un problema NO CONTROLADO actualizando el parámetro global." + INTENTE_DE_NUEVO;
    }

    public static final class FacultadSqlServerDAO {

        private FacultadSqlServerDAO() {
        }

        public static final String USUARIO_ERROR_CREANDO = "Se presentó un problema registrando la Facultad." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CREANDO = "Se presentó un problema NO CONTROLADO registrando la Facultad." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_ID = "Se presentó un problema consultando la Facultad." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID = "Se presentó un problema NO CONTROLADO consultando la Facultad." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_FILTRO = "Se presentó un problema consultando las Facultades." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO = "Se presentó un problema NO CONTROLADO consultando las Facultades." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_TODOS = "Se presentó un problema consultando las Facultades." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS = "Se presentó un problema NO CONTROLADO consultando las Facultades." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_ACTUALIZANDO = "Se presentó un problema actualizando la Facultad." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO = "Se presentó un problema NO CONTROLADO actualizando la Facultad." + INTENTE_DE_NUEVO;
    }

    public static final class ProgramaAcademicoSqlServerDAO {

        private ProgramaAcademicoSqlServerDAO() {
        }

        public static final String USUARIO_ERROR_CREANDO = "Se presentó un problema registrando el programa académico." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CREANDO = "Se presentó un problema NO CONTROLADO registrando el programa académico." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_ID = "Se presentó un problema consultando el programa académico." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID = "Se presentó un problema NO CONTROLADO consultando el programa académico." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_FILTRO = "Se presentó un problema consultando los programas académicos." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO = "Se presentó un problema NO CONTROLADO consultando los programas académicos." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_TODOS = "Se presentó un problema consultando los programas académicos." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS = "Se presentó un problema NO CONTROLADO consultando los programas académicos." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_ACTIVOS = "Se presentó un problema consultando los programas académicos activos." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_ACTIVOS = "Se presentó un problema NO CONTROLADO consultando los programas académicos activos." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_ACTUALIZANDO = "Se presentó un problema actualizando el programa académico." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO = "Se presentó un problema NO CONTROLADO actualizando el programa académico." + INTENTE_DE_NUEVO;
    }

    public static final class CategoriaSqlServerDAO {

        private CategoriaSqlServerDAO() {
        }

        public static final String USUARIO_ERROR_CREANDO = "Se presentó un problema registrando la categoría." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CREANDO = "Se presentó un problema NO CONTROLADO registrando la categoría." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_ID = "Se presentó un problema consultando la categoría." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID = "Se presentó un problema NO CONTROLADO consultando la categoría." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_FILTRO = "Se presentó un problema consultando las categorías." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO = "Se presentó un problema NO CONTROLADO consultando las categorías." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_TODOS = "Se presentó un problema consultando las categorías." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS = "Se presentó un problema NO CONTROLADO consultando las categorías." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_ACTIVOS = "Se presentó un problema consultando las categorías activas." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_ACTIVOS = "Se presentó un problema NO CONTROLADO consultando las categorías activas." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_ACTUALIZANDO = "Se presentó un problema actualizando la categoría." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO = "Se presentó un problema NO CONTROLADO actualizando la categoría." + INTENTE_DE_NUEVO;
    }

    public static final class AsignacionRolSqlServerDAO {

        private AsignacionRolSqlServerDAO() {
        }

        public static final String USUARIO_ERROR_CREANDO = "Se presentó un problema registrando la asignación de rol." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CREANDO = "Se presentó un problema NO CONTROLADO registrando la asignación de rol." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_ID = "Se presentó un problema consultando la asignación de rol." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID = "Se presentó un problema NO CONTROLADO consultando la asignación de rol." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_FILTRO = "Se presentó un problema consultando las asignaciones de rol." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO = "Se presentó un problema NO CONTROLADO consultando las asignaciones de rol." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_TODOS = "Se presentó un problema consultando las asignaciones de rol." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS = "Se presentó un problema NO CONTROLADO consultando las asignaciones de rol." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_ACTUALIZANDO = "Se presentó un problema actualizando la asignación de rol." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO = "Se presentó un problema NO CONTROLADO actualizando la asignación de rol." + INTENTE_DE_NUEVO;
    }

    public static final class CodigoVerificacionSqlServerDAO {

        private CodigoVerificacionSqlServerDAO() {
        }

        public static final String USUARIO_ERROR_CREANDO = "Se presentó un problema registrando el código de verificación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CREANDO = "Se presentó un problema NO CONTROLADO registrando el código de verificación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_ID = "Se presentó un problema consultando el código de verificación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID = "Se presentó un problema NO CONTROLADO consultando el código de verificación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_FILTRO = "Se presentó un problema consultando los códigos de verificación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO = "Se presentó un problema NO CONTROLADO consultando los códigos de verificación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_TODOS = "Se presentó un problema consultando los códigos de verificación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS = "Se presentó un problema NO CONTROLADO consultando los códigos de verificación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_ACTUALIZANDO = "Se presentó un problema actualizando el código de verificación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO = "Se presentó un problema NO CONTROLADO actualizando el código de verificación." + INTENTE_DE_NUEVO;
    }

    public static final class SolicitudRegistroSqlServerDAO {

        private SolicitudRegistroSqlServerDAO() {
        }

        public static final String USUARIO_ERROR_CREANDO = "Se presentó un problema registrando la solicitud de registro." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CREANDO = "Se presentó un problema NO CONTROLADO registrando la solicitud de registro." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_ID = "Se presentó un problema consultando la solicitud de registro." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID = "Se presentó un problema NO CONTROLADO consultando la solicitud de registro." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_FILTRO = "Se presentó un problema consultando las solicitudes de registro." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO = "Se presentó un problema NO CONTROLADO consultando las solicitudes de registro." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_TODOS = "Se presentó un problema consultando las solicitudes de registro." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS = "Se presentó un problema NO CONTROLADO consultando las solicitudes de registro." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_ACTUALIZANDO = "Se presentó un problema actualizando la solicitud de registro." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO = "Se presentó un problema NO CONTROLADO actualizando la solicitud de registro." + INTENTE_DE_NUEVO;
    }

    public static final class VinculacionProgramaSqlServerDAO {

        private VinculacionProgramaSqlServerDAO() {
        }

        public static final String USUARIO_ERROR_CREANDO = "Se presentó un problema registrando la vinculación al programa." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CREANDO = "Se presentó un problema NO CONTROLADO registrando la vinculación al programa." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_ID = "Se presentó un problema consultando la vinculación al programa." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID = "Se presentó un problema NO CONTROLADO consultando la vinculación al programa." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_FILTRO = "Se presentó un problema consultando las vinculaciones a programas." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO = "Se presentó un problema NO CONTROLADO consultando las vinculaciones a programas." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_TODOS = "Se presentó un problema consultando las vinculaciones a programas." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS = "Se presentó un problema NO CONTROLADO consultando las vinculaciones a programas." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_ACTUALIZANDO = "Se presentó un problema actualizando la vinculación al programa." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO = "Se presentó un problema NO CONTROLADO actualizando la vinculación al programa." + INTENTE_DE_NUEVO;
    }

    public static final class KudoSqlServerDAO {

        private KudoSqlServerDAO() {
        }

        public static final String USUARIO_ERROR_CREANDO = "Se presentó un problema registrando el Kudo." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CREANDO = "Se presentó un problema NO CONTROLADO registrando el Kudo." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_ID = "Se presentó un problema consultando el Kudo." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID = "Se presentó un problema NO CONTROLADO consultando el Kudo." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_FILTRO = "Se presentó un problema consultando los Kudos." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO = "Se presentó un problema NO CONTROLADO consultando los Kudos." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_TODOS = "Se presentó un problema consultando los Kudos." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS = "Se presentó un problema NO CONTROLADO consultando los Kudos." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_ACTUALIZANDO = "Se presentó un problema actualizando el Kudo." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO = "Se presentó un problema NO CONTROLADO actualizando el Kudo." + INTENTE_DE_NUEVO;
    }

    public static final class ReplicaSqlServerDAO {

        private ReplicaSqlServerDAO() {
        }

        public static final String USUARIO_ERROR_CREANDO = "Se presentó un problema registrando la réplica." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CREANDO = "Se presentó un problema NO CONTROLADO registrando la réplica." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_ID = "Se presentó un problema consultando la réplica." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID = "Se presentó un problema NO CONTROLADO consultando la réplica." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_FILTRO = "Se presentó un problema consultando las réplicas." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO = "Se presentó un problema NO CONTROLADO consultando las réplicas." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_TODOS = "Se presentó un problema consultando las réplicas." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS = "Se presentó un problema NO CONTROLADO consultando las réplicas." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_ACTUALIZANDO = "Se presentó un problema actualizando la réplica." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO = "Se presentó un problema NO CONTROLADO actualizando la réplica." + INTENTE_DE_NUEVO;
    }

    public static final class ModeracionSqlServerDAO {

        private ModeracionSqlServerDAO() {
        }

        public static final String USUARIO_ERROR_CREANDO = "Se presentó un problema registrando la moderación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CREANDO = "Se presentó un problema NO CONTROLADO registrando la moderación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_ID = "Se presentó un problema consultando la moderación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID = "Se presentó un problema NO CONTROLADO consultando la moderación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_FILTRO = "Se presentó un problema consultando las moderaciones." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO = "Se presentó un problema NO CONTROLADO consultando las moderaciones." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_TODOS = "Se presentó un problema consultando las moderaciones." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS = "Se presentó un problema NO CONTROLADO consultando las moderaciones." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_ACTUALIZANDO = "Se presentó un problema actualizando la moderación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO = "Se presentó un problema NO CONTROLADO actualizando la moderación." + INTENTE_DE_NUEVO;
    }

    public static final class ReporteSqlServerDAO {

        private ReporteSqlServerDAO() {
        }

        public static final String USUARIO_ERROR_CREANDO = "Se presentó un problema registrando el reporte." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CREANDO = "Se presentó un problema NO CONTROLADO registrando el reporte." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_ID = "Se presentó un problema consultando el reporte." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID = "Se presentó un problema NO CONTROLADO consultando el reporte." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_FILTRO = "Se presentó un problema consultando los reportes." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO = "Se presentó un problema NO CONTROLADO consultando los reportes." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_TODOS = "Se presentó un problema consultando los reportes." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS = "Se presentó un problema NO CONTROLADO consultando los reportes." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_ACTUALIZANDO = "Se presentó un problema actualizando el reporte." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO = "Se presentó un problema NO CONTROLADO actualizando el reporte." + INTENTE_DE_NUEVO;
    }

    public static final class CalificacionSqlServerDAO {

        private CalificacionSqlServerDAO() {
        }

        public static final String USUARIO_ERROR_CREANDO = "Se presentó un problema registrando la calificación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CREANDO = "Se presentó un problema NO CONTROLADO registrando la calificación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_ID = "Se presentó un problema consultando la calificación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID = "Se presentó un problema NO CONTROLADO consultando la calificación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_FILTRO = "Se presentó un problema consultando las calificaciones." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO = "Se presentó un problema NO CONTROLADO consultando las calificaciones." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_TODOS = "Se presentó un problema consultando las calificaciones." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS = "Se presentó un problema NO CONTROLADO consultando las calificaciones." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_ACTUALIZANDO = "Se presentó un problema actualizando la calificación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO = "Se presentó un problema NO CONTROLADO actualizando la calificación." + INTENTE_DE_NUEVO;
    }

    public static final class AuditoriaSqlServerDAO {

        private AuditoriaSqlServerDAO() {
        }

        public static final String USUARIO_ERROR_CREANDO = "Se presentó un problema registrando la auditoría." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CREANDO = "Se presentó un problema NO CONTROLADO registrando la auditoría." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_ID = "Se presentó un problema consultando la auditoría." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID = "Se presentó un problema NO CONTROLADO consultando la auditoría." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_FILTRO = "Se presentó un problema consultando los registros de auditoría." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO = "Se presentó un problema NO CONTROLADO consultando los registros de auditoría." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_TODOS = "Se presentó un problema consultando los registros de auditoría." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS = "Se presentó un problema NO CONTROLADO consultando los registros de auditoría." + INTENTE_DE_NUEVO;
    }

    public static final class NotificacionSqlServerDAO {

        private NotificacionSqlServerDAO() {
        }

        public static final String USUARIO_ERROR_CREANDO = "Se presentó un problema registrando la notificación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CREANDO = "Se presentó un problema NO CONTROLADO registrando la notificación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_ID = "Se presentó un problema consultando la notificación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID = "Se presentó un problema NO CONTROLADO consultando la notificación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_POR_FILTRO = "Se presentó un problema consultando las notificaciones." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO = "Se presentó un problema NO CONTROLADO consultando las notificaciones." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_CONSULTANDO_TODOS = "Se presentó un problema consultando las notificaciones." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS = "Se presentó un problema NO CONTROLADO consultando las notificaciones." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_ACTUALIZANDO = "Se presentó un problema actualizando la notificación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO = "Se presentó un problema NO CONTROLADO actualizando la notificación." + INTENTE_DE_NUEVO;
    }
}
