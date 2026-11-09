package co.edu.uco.kudos.transversal.catalogo;

/**
 * Mensajes para el usuario, agrupados por tema en clases internas.
 * Un buen mensaje dice qué falló, por qué y qué hacer.
 */
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
}
