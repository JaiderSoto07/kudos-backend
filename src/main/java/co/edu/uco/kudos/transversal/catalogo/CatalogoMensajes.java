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
}
