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
        public static final String CUENTA_PENDIENTE_APROBACION = "Tu cuenta está esperando la aprobación de la autoridad que te corresponde. Podrás ingresar cuando sea aprobada.";
        public static final String CUENTA_FALTA_VERIFICAR_CORREO_Y_CELULAR = "Tu cuenta ya fue aprobada, pero aún debes verificar tu correo y tu celular con los códigos que te enviamos.";
        public static final String CUENTA_FALTA_VERIFICAR_CORREO = "Tu cuenta ya fue aprobada, pero aún debes verificar tu correo con el código que te enviamos.";
        public static final String CUENTA_FALTA_VERIFICAR_CELULAR = "Tu cuenta ya fue aprobada, pero aún debes verificar tu celular con el código que te enviamos.";
        public static final String CUENTA_SUSPENDIDA = "Tu cuenta está suspendida. Comunícate con la autoridad que gestiona tu cuenta para reactivarla.";
        public static final String CUENTA_DESACTIVADA = "Tu cuenta está desactivada y no puede ingresar a la plataforma.";
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

    public static final class CategoriaNegocioImpl {

        private CategoriaNegocioImpl() {
        }

        public static final String ACTOR_NO_PUEDE_GESTIONAR_CATEGORIAS = "No tiene permiso para gestionar categorías. Solo el Administrador, el Rector y el Coordinador Académico pueden crear, modificar, activar, desactivar o ver las categorías inactivas.";
        public static final String DATOS_CATEGORIA_OBLIGATORIOS = "No es posible guardar la categoría porque no se recibió su información. Por favor diligencie los datos de la categoría e intente de nuevo.";
        public static final String NOMBRE_CATEGORIA_OBLIGATORIO = "El nombre de la categoría es obligatorio. Por favor ingrese un nombre e intente de nuevo.";
        public static final String NOMBRE_CATEGORIA_LONGITUD_INVALIDA = "El nombre de la categoría debe tener entre 3 y 50 caracteres. Por favor corrija el nombre e intente de nuevo.";
        public static final String DESCRIPCION_CATEGORIA_OBLIGATORIA = "La descripción de la categoría es obligatoria. Por favor describa qué reconoce la categoría e intente de nuevo.";
        public static final String DESCRIPCION_CATEGORIA_LONGITUD_INVALIDA = "La descripción de la categoría debe tener entre 10 y 250 caracteres. Por favor corrija la descripción e intente de nuevo.";
        public static final String NOMBRE_CATEGORIA_YA_EXISTE = "Ya existe una categoría con ese nombre. Por favor elija un nombre diferente e intente de nuevo.";
        public static final String ID_CATEGORIA_YA_EXISTE = "No es posible registrar la categoría porque el identificador generado ya está en uso." + INTENTE_DE_NUEVO;
        public static final String CATEGORIA_NO_EXISTE = "No es posible realizar la operación porque la categoría indicada no existe. Por favor verifique la categoría e intente de nuevo.";
    }

    public static final class CategoriaFachadaImpl {

        private CategoriaFachadaImpl() {
        }

        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_REGISTRANDO_CATEGORIA = "Se presentó un problema inesperado registrando la categoría." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_MODIFICANDO_CATEGORIA = "Se presentó un problema inesperado modificando la categoría." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_ACTIVANDO_CATEGORIA = "Se presentó un problema inesperado activando la categoría." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_DESACTIVANDO_CATEGORIA = "Se presentó un problema inesperado desactivando la categoría." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_CATEGORIAS = "Se presentó un problema inesperado consultando las categorías." + INTENTE_DE_NUEVO;
    }

    public static final class CategoriaControlador {

        private CategoriaControlador() {
        }

        public static final String CATEGORIA_REGISTRADA = "La categoría se registró exitosamente.";
        public static final String CATEGORIA_MODIFICADA = "La categoría se modificó exitosamente.";
        public static final String CATEGORIA_ACTIVADA = "La categoría se activó exitosamente.";
        public static final String CATEGORIA_DESACTIVADA = "La categoría se desactivó exitosamente.";
    }

    public static final class AutenticacionNegocioImpl {

        private AutenticacionNegocioImpl() {
        }

        public static final String USUARIO_NO_REGISTRADO = "Aún no tienes una cuenta en UCO Kudos con este correo. Regístrate para solicitar tu cuenta.";
    }

    public static final class AutenticacionFachadaImpl {

        private AutenticacionFachadaImpl() {
        }

        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_USUARIO_ACTUAL = "Se presentó un problema inesperado consultando la información de tu cuenta." + INTENTE_DE_NUEVO;
    }

    public static final class Identidad {

        private Identidad() {
        }

        public static final String CORREO_OBLIGATORIO = "El correo institucional es obligatorio. Por favor ingresa tu correo e intenta de nuevo.";
        public static final String CORREO_FORMATO_INVALIDO = "El correo institucional no tiene un formato válido. Por favor revísalo e intenta de nuevo.";
        public static final String CORREO_DOMINIO_NO_PERMITIDO = "El correo debe pertenecer a un dominio institucional permitido (%s). Por favor usa tu correo institucional.";
        public static final String CORREO_YA_REGISTRADO = "Ya existe una cuenta registrada con ese correo. Si es tuyo, ingresa con él en lugar de registrarte de nuevo.";
        public static final String CELULAR_OBLIGATORIO = "El número celular es obligatorio. Por favor ingresa tu celular e intenta de nuevo.";
        public static final String CELULAR_FORMATO_INVALIDO = "El número celular debe ser colombiano: 10 dígitos que empiezan por 3, sin espacios ni signos. Por favor corrígelo e intenta de nuevo.";
        public static final String CELULAR_YA_REGISTRADO = "Ya existe una cuenta registrada con ese número celular. Por favor usa otro número.";
        public static final String NOMBRES_OBLIGATORIOS = "Los nombres son obligatorios. Por favor ingrésalos e intenta de nuevo.";
        public static final String NOMBRES_LONGITUD_INVALIDA = "Los nombres pueden tener máximo 60 caracteres. Por favor corrígelos e intenta de nuevo.";
        public static final String APELLIDOS_OBLIGATORIOS = "Los apellidos son obligatorios. Por favor ingrésalos e intenta de nuevo.";
        public static final String APELLIDOS_LONGITUD_INVALIDA = "Los apellidos pueden tener máximo 60 caracteres. Por favor corrígelos e intenta de nuevo.";
        public static final String ROL_OBLIGATORIO = "Debes elegir el rol que solicitas. Por favor elige uno e intenta de nuevo.";
        public static final String ROL_NO_VALIDO = "El rol solicitado no existe. Por favor elige uno de los roles de la lista.";
        public static final String ROL_NO_AUTORREGISTRABLE = "El Rector y el Administrador no se pueden autorregistrar: los registra un Administrador. Por favor elige otro rol.";
        public static final String PROGRAMA_OBLIGATORIO = "Para el rol que solicitas debes elegir un Programa académico. Por favor elige uno e intenta de nuevo.";
        public static final String PROGRAMA_NO_EXISTE = "El Programa académico elegido no existe. Por favor elige otro e intenta de nuevo.";
        public static final String PROGRAMA_INACTIVO = "El Programa académico elegido está inactivo y no recibe solicitudes. Por favor elige otro.";
        public static final String FACULTAD_OBLIGATORIA = "Para el rol de Coordinador de Facultad debes elegir una Facultad. Por favor elige una e intenta de nuevo.";
        public static final String FACULTAD_NO_EXISTE = "La Facultad elegida no existe. Por favor elige otra e intenta de nuevo.";
        public static final String FACULTAD_NO_ADMITE_COORDINADOR = "La Facultad elegida no está activa, así que todavía no puede tener Coordinador. Por favor elige otra.";
        public static final String CODIGO_NO_SOLICITADO = "No tienes un código de verificación pendiente. Por favor solicita un código nuevo.";
        public static final String CODIGO_YA_USADO = "Este código ya fue usado. Por favor solicita un código nuevo.";
        public static final String CODIGO_VENCIDO = "El código de verificación ya venció. Por favor solicita un código nuevo.";
        public static final String CODIGO_INTENTOS_AGOTADOS = "Superaste el número de intentos permitidos para este código. Por favor solicita un código nuevo.";
        public static final String CODIGO_INCORRECTO = "El código no es correcto. Revisa el último código que te enviamos e intenta de nuevo.";
        public static final String CELULAR_YA_VERIFICADO = "Tu celular ya está verificado, así que no necesitas un código nuevo.";
        public static final String CELULAR_IGUAL_AL_ACTUAL = "El número celular nuevo es igual al que ya tienes registrado. Por favor escribe un número diferente.";
        public static final String CORREO_IGUAL_AL_ACTUAL = "El correo nuevo es igual al que ya tienes registrado. Por favor escribe un correo diferente.";
        public static final String MENSAJE_CODIGO_VERIFICACION = "Tu código de verificación de UCO Kudos es %s. Vence en %d minutos.";
    }

    public static final class IdentidadFachada {

        private IdentidadFachada() {
        }

        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_REGISTRANDO = "Se presentó un problema inesperado registrando tu solicitud de cuenta." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_OPCIONES = "Se presentó un problema inesperado consultando las Facultades y los Programas disponibles." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_VERIFICANDO = "Se presentó un problema inesperado verificando tu código." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_ENVIANDO_CODIGO = "Se presentó un problema inesperado enviando tu código de verificación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_CUENTA = "Se presentó un problema inesperado consultando la información de tu cuenta." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_ACTUALIZANDO_CUENTA = "Se presentó un problema inesperado actualizando la información de tu cuenta." + INTENTE_DE_NUEVO;
    }

    public static final class IdentidadControlador {

        private IdentidadControlador() {
        }

        public static final String SOLICITUD_REGISTRADA = "Tu solicitud de cuenta quedó enviada. Te enviamos un código para verificar tu celular.";
        public static final String CELULAR_VERIFICADO = "Tu celular quedó verificado.";
        public static final String CODIGO_REENVIADO = "Te enviamos un código nuevo. Los códigos anteriores ya no sirven.";
        public static final String DATOS_PERSONALES_ACTUALIZADOS = "Tus nombres y apellidos quedaron actualizados.";
        public static final String CELULAR_CAMBIADO = "Tu celular quedó actualizado. Te enviamos un código para verificarlo.";
        public static final String CORREO_CAMBIADO = "Tu correo quedó actualizado. Para verificarlo, cierra sesión y entra con Google usando el correo nuevo.";
        public static final String NUEVA_SOLICITUD_REGISTRADA = "Tu nueva solicitud quedó enviada a la autoridad que te corresponde.";
    }

    public static final class Aprobacion {

        private Aprobacion() {
        }

        public static final String ACTOR_NO_APRUEBA_SOLICITUDES = "No tienes permiso para revisar solicitudes. Solo el Rector y los coordinadores aprueban o rechazan solicitudes de cuenta.";
        public static final String SOLICITUD_NO_EXISTE = "La solicitud indicada no existe. Por favor actualiza la lista e intenta de nuevo.";
        public static final String SOLICITUD_YA_DECIDIDA = "Esta solicitud ya fue aprobada o rechazada. Por favor actualiza la lista.";
        public static final String AUTORIDAD_NO_COMPETENTE = "No te corresponde decidir esta solicitud: está fuera de tu rol o de tu ámbito.";
        public static final String SOLICITUD_PROPIA = "Nadie puede decidir sobre su propia solicitud.";
        public static final String MOTIVO_RECHAZO_OBLIGATORIO = "El motivo del rechazo es obligatorio. Por favor explica por qué se rechaza la solicitud.";
        public static final String MOTIVO_RECHAZO_LONGITUD_INVALIDA = "El motivo del rechazo debe tener entre 10 y 250 caracteres.";
        public static final String ROL_NO_ES_AUTORIDAD = "Desde aquí solo se registran el Rector y los Administradores.";
        public static final String RECTOR_YA_EXISTE = "Ya hay un Rector vigente. Solo puede haber uno a la vez.";
        public static final String ACTOR_NO_REGISTRA_AUTORIDADES = "Solo un Administrador puede registrar al Rector o a otros Administradores.";
        public static final String SOLICITUD_NO_RECHAZADA = "Solo puedes enviar una nueva solicitud si la anterior fue rechazada.";
        public static final String MOTIVO_SUSTITUCION = "Sustituido al aprobarse un nuevo titular del cargo.";
        public static final String MENSAJE_SOLICITUD_PENDIENTE = "%s solicitó el rol %s y espera tu aprobación.";
        public static final String MENSAJE_SOLICITUD_APROBADA = "Tu solicitud como %s fue aprobada.";
        public static final String MENSAJE_SOLICITUD_RECHAZADA = "Tu solicitud como %s fue rechazada. Motivo: %s";
        public static final String MENSAJE_CARGO_SUSTITUIDO = "Tu cargo como %s terminó porque se aprobó un nuevo titular.";
    }

    public static final class Organizacion {

        private Organizacion() {
        }

        public static final String ACTOR_NO_CONSULTA_ORGANIZACION = "No tienes permiso para consultar Facultades y Programas desde aquí. Puedes ver la estructura de la universidad en la opción Estructura.";
        public static final String ACTOR_NO_GESTIONA_ORGANIZACION = "Solo el Coordinador Académico crea, modifica o inactiva Facultades y Programas.";
        public static final String NOMBRE_FACULTAD_OBLIGATORIO = "El nombre de la Facultad es obligatorio.";
        public static final String NOMBRE_FACULTAD_LONGITUD_INVALIDA = "El nombre de la Facultad debe tener entre 3 y 80 caracteres.";
        public static final String NOMBRE_FACULTAD_YA_EXISTE = "Ya existe una Facultad con ese nombre. Por favor elige otro.";
        public static final String NOMBRE_PROGRAMA_OBLIGATORIO = "El nombre del Programa es obligatorio.";
        public static final String NOMBRE_PROGRAMA_LONGITUD_INVALIDA = "El nombre del Programa debe tener entre 3 y 100 caracteres.";
        public static final String NOMBRE_PROGRAMA_YA_EXISTE = "Ya existe un Programa con ese nombre. Por favor elige otro.";
        public static final String FACULTAD_NO_EXISTE = "La Facultad indicada no existe.";
        public static final String PROGRAMA_NO_EXISTE = "El Programa indicado no existe.";
        public static final String FACULTAD_NO_ADMITE_PROGRAMAS = "La Facultad está inactiva y no admite Programas nuevos.";
        public static final String FACULTAD_YA_INACTIVA = "La Facultad ya está inactiva.";
        public static final String FACULTAD_CON_PROGRAMAS_ACTIVOS = "No es posible inactivar la Facultad porque tiene Programas activos. Primero inactiva sus Programas.";
        public static final String PROGRAMA_INACTIVO = "El Programa está inactivo.";
        public static final String PROGRAMA_CON_VINCULOS = "No es posible inactivar el Programa porque tiene estudiantes o docentes vinculados. Primero desvincúlalos.";
        public static final String FACULTAD_FUERA_DE_AMBITO = "No puedes consultar esa Facultad: está fuera de tu ámbito.";
        public static final String PROGRAMA_FUERA_DE_AMBITO = "No puedes consultar ese Programa: está fuera de tu ámbito.";
        public static final String ACTOR_NO_COORDINA_PROGRAMA = "Solo el Coordinador de este Programa puede vincular o desvincular personas.";
        public static final String PERSONA_NO_REGISTRADA = "No hay una cuenta registrada con ese correo.";
        public static final String TIPO_VINCULO_INVALIDO = "El tipo de vínculo debe ser Estudiante o Docente.";
        public static final String ROL_NO_COINCIDE_CON_VINCULO = "El rol vigente de esa persona no corresponde al tipo de vínculo elegido. Un Docente solo se vincula como Docente y un Estudiante como Estudiante.";
        public static final String VINCULO_YA_EXISTE = "Esa persona ya está vinculada a este Programa.";
        public static final String VINCULO_NO_EXISTE = "El vínculo indicado no existe.";
        public static final String VINCULO_YA_CERRADO = "Ese vínculo ya fue cerrado.";
        public static final String MOTIVO_DESVINCULACION_OBLIGATORIO = "El motivo de la desvinculación es obligatorio.";
        public static final String MOTIVO_DESVINCULACION_LONGITUD_INVALIDA = "El motivo de la desvinculación debe tener entre 10 y 250 caracteres.";
        public static final String MOTIVO_FACULTAD_INACTIVADA = "La Facultad fue inactivada.";
        public static final String MOTIVO_PROGRAMA_INACTIVADO = "El Programa fue inactivado.";
        public static final String MENSAJE_CUENTA_SUSPENDIDA = "Tu cuenta quedó suspendida porque ya no estás vinculado a ningún Programa. Motivo: %s";
        public static final String MENSAJE_CUENTA_REACTIVADA = "Tu cuenta se reactivó porque fuiste vinculado al Programa %s.";
    }

    public static final class Kudo {

        private Kudo() {
        }

        public static final String DESTINATARIO_OBLIGATORIO = "Elige a la persona que quieres reconocer.";
        public static final String DESTINATARIO_ES_EL_AUTOR = "No puedes darte un Kudo a ti mismo. Elige a otra persona.";
        public static final String DESTINATARIO_NO_ACTIVO = "Solo puedes reconocer a personas con la cuenta activa.";
        public static final String CATEGORIA_OBLIGATORIA = "Elige la categoría del Kudo.";
        public static final String CATEGORIA_NO_DISPONIBLE = "La categoría elegida no existe o está inactiva. Elige otra.";
        public static final String CONTENIDO_OBLIGATORIO = "Escribe el mensaje antes de publicar.";
        public static final String KUDO_NO_EXISTE = "El Kudo indicado no existe.";
        public static final String MENSAJE_KUDO_RECIBIDO = "%s te dio un Kudo en la categoría %s.";
    }

    public static final class KudoFachada {

        private KudoFachada() {
        }

        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_CREANDO = "Se presentó un problema inesperado publicando el Kudo." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO = "Se presentó un problema inesperado consultando los Kudos." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_PERSONAS = "Se presentó un problema inesperado buscando personas." + INTENTE_DE_NUEVO;
    }

    public static final class KudoControlador {

        private KudoControlador() {
        }

        public static final String KUDO_PUBLICADO = "Tu Kudo quedó publicado en el muro.";
    }

    public static final class Replica {

        private Replica() {
        }

        public static final String REPLICA_NO_EXISTE = "La réplica indicada no existe.";
        public static final String KUDO_VETADO = "Este Kudo fue vetado por moderación: la conversación está cerrada y ya no admite réplicas ni calificaciones.";
        public static final String REPLICA_VETADA = "Esta réplica fue vetada por moderación y ya no admite respuestas ni calificaciones.";
        public static final String CONTENIDO_VETADO = "Contenido vetado por moderación";
        public static final String MENSAJE_RESPUESTA_KUDO = "%s respondió el Kudo de %s para %s.";
        public static final String MENSAJE_RESPUESTA_REPLICA = "%s respondió tu réplica en el Kudo para %s.";
    }

    public static final class Calificacion {

        private Calificacion() {
        }

        public static final String ESTRELLAS_FUERA_DE_RANGO = "La calificación debe ser de 1 a 5 estrellas.";
        public static final String CONTENIDO_PROPIO = "No puedes calificar lo que tú mismo escribiste.";
    }

    public static final class ReplicaFachada {

        private ReplicaFachada() {
        }

        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_RESPONDIENDO = "Se presentó un problema inesperado publicando la réplica." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO = "Se presentó un problema inesperado consultando la conversación." + INTENTE_DE_NUEVO;
    }

    public static final class CalificacionFachada {

        private CalificacionFachada() {
        }

        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_CALIFICANDO = "Se presentó un problema inesperado guardando la calificación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO = "Se presentó un problema inesperado consultando las valoraciones." + INTENTE_DE_NUEVO;
    }

    public static final class ReplicaControlador {

        private ReplicaControlador() {
        }

        public static final String REPLICA_PUBLICADA = "Tu réplica quedó publicada en la conversación.";
    }

    public static final class CalificacionControlador {

        private CalificacionControlador() {
        }

        public static final String CALIFICACION_GUARDADA = "Tu calificación quedó guardada.";
    }

    public static final class Moderacion {

        private Moderacion() {
        }

        public static final String CONTENIDO_YA_VETADO = "Este contenido ya fue vetado por moderación.";
        public static final String CONTENIDO_PROPIO = "No puedes reportar lo que tú mismo escribiste.";
        public static final String MOTIVO_OBLIGATORIO = "Elige el motivo del reporte.";
        public static final String DESCRIPCION_OBLIGATORIA = "Cuando el motivo es \"Otro\", describe qué problema tiene el contenido.";
        public static final String DESCRIPCION_MUY_LARGA = "La descripción del reporte admite máximo 500 caracteres.";
        public static final String REPORTE_ABIERTO = "Ya reportaste este contenido. Espera la decisión del moderador.";
        public static final String ACTOR_NO_ES_MODERADOR = "Solo el Rector o el Coordinador Académico pueden moderar contenido.";
        public static final String MOTIVO_DECISION_OBLIGATORIO = "Escribe el motivo de la decisión.";
        public static final String MOTIVO_DECISION_LONGITUD_INVALIDA = "El motivo de la decisión debe tener entre 10 y 500 caracteres.";
        public static final String CONTENIDO_SIN_REPORTES = "Solo se puede revisar un contenido que tiene reportes pendientes.";
        public static final String REVISION_EN_CURSO = "Este contenido ya tiene una revisión en curso.";
        public static final String CONTENIDO_NO_EN_REVISION = "Solo se puede liberar un contenido que está en revisión.";
        public static final String MENSAJE_KUDO_VETADO = "Tu Kudo para %s fue vetado por moderación. Motivo: %s";
        public static final String MENSAJE_REPLICA_VETADA = "Tu réplica en el Kudo para %s fue vetada por moderación. Motivo: %s";
        public static final String MENSAJE_REPORTE_RECHAZADO = "Revisamos el contenido que reportaste en el Kudo para %s y sigue visible. Motivo: %s";
    }

    public static final class ModeracionFachada {

        private ModeracionFachada() {
        }

        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_REPORTANDO = "Se presentó un problema inesperado registrando el reporte." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_MODERANDO = "Se presentó un problema inesperado guardando la decisión de moderación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO = "Se presentó un problema inesperado consultando los reportes." + INTENTE_DE_NUEVO;
    }

    public static final class ModeracionControlador {

        private ModeracionControlador() {
        }

        public static final String REPORTE_REGISTRADO = "Tu reporte quedó registrado. Un moderador lo revisará.";
        public static final String REVISION_INICIADA = "La revisión quedó iniciada.";
        public static final String CONTENIDO_VETADO = "El contenido quedó vetado y se avisó a su autor.";
        public static final String CONTENIDO_LIBERADO = "El contenido quedó liberado y vuelve a estar visible.";
    }

    public static final class OrganizacionFachada {

        private OrganizacionFachada() {
        }

        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO = "Se presentó un problema inesperado consultando la estructura de la universidad." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_GUARDANDO_FACULTAD = "Se presentó un problema inesperado guardando la Facultad." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_GUARDANDO_PROGRAMA = "Se presentó un problema inesperado guardando el Programa." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_GUARDANDO_VINCULO = "Se presentó un problema inesperado guardando el vínculo." + INTENTE_DE_NUEVO;
    }

    public static final class OrganizacionControlador {

        private OrganizacionControlador() {
        }

        public static final String FACULTAD_REGISTRADA = "La Facultad quedó registrada en conformación. Pasará a activa cuando tenga su primer Programa.";
        public static final String FACULTAD_MODIFICADA = "El nombre de la Facultad quedó actualizado.";
        public static final String FACULTAD_INACTIVADA = "La Facultad quedó inactiva.";
        public static final String PROGRAMA_REGISTRADO = "El Programa quedó registrado.";
        public static final String PROGRAMA_MODIFICADO = "El nombre del Programa quedó actualizado.";
        public static final String PROGRAMA_INACTIVADO = "El Programa quedó inactivo.";
        public static final String PERSONA_VINCULADA = "La persona quedó vinculada al Programa.";
        public static final String PERSONA_DESVINCULADA = "La persona quedó desvinculada del Programa.";
    }

    public static final class AprobacionFachada {

        private AprobacionFachada() {
        }

        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_SOLICITUDES = "Se presentó un problema inesperado consultando las solicitudes." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_APROBANDO = "Se presentó un problema inesperado aprobando la solicitud." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_RECHAZANDO = "Se presentó un problema inesperado rechazando la solicitud." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_REGISTRANDO_AUTORIDAD = "Se presentó un problema inesperado registrando la cuenta." + INTENTE_DE_NUEVO;
    }

    public static final class AprobacionControlador {

        private AprobacionControlador() {
        }

        public static final String SOLICITUD_APROBADA = "La solicitud quedó aprobada.";
        public static final String SOLICITUD_RECHAZADA = "La solicitud quedó rechazada.";
        public static final String AUTORIDAD_REGISTRADA = "La cuenta quedó registrada. La persona debe entrar con Google con ese correo y verificar su celular.";
    }

    public static final class AbstractRestController {

        private AbstractRestController() {
        }

        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_EN_CONTROLADOR = "Se presentó un problema inesperado llevando a cabo la operación." + INTENTE_DE_NUEVO;
        public static final String USUARIO_ERROR_RECURSO_NO_EXISTE = "Este recurso no existe: ";
    }
}
