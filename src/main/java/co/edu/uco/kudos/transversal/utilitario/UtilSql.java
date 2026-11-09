package co.edu.uco.kudos.transversal.utilitario;

import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosTransversalExcepcion;

import java.sql.Connection;
import java.sql.SQLException;

public final class UtilSql {

    private UtilSql() {
    }

    public static boolean conexionEstaVacia(Connection conexion) {
        return UtilObjeto.esNulo(conexion);
    }

    public static boolean conexionEstaAbierta(Connection conexion) {
        try {
            return !conexionEstaVacia(conexion) && !conexion.isClosed();
        } catch (SQLException excepcion) {
            throw KudosTransversalExcepcion.crear(CatalogoMensajes.UtilSql.USUARIO_ERROR_VALIDANDO_SI_CONEXION_ESTA_ABIERTA, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosTransversalExcepcion.crear(CatalogoMensajes.UtilSql.USUARIO_ERROR_NO_CONTROLADO_VALIDANDO_SI_CONEXION_ESTA_ABIERTA, excepcion.getMessage(), excepcion);
        }
    }

    public static void asegurarConexionAbierta(Connection conexion) {
        if (!conexionEstaAbierta(conexion)) {
            throw KudosTransversalExcepcion.crear(CatalogoMensajes.UtilSql.USUARIO_ERROR_CONEXION_NO_ESTA_ABIERTA);
        }
    }

    public static boolean transaccionEstaIniciada(Connection conexion) {
        try {
            return conexionEstaAbierta(conexion) && !conexion.getAutoCommit();
        } catch (SQLException excepcion) {
            throw KudosTransversalExcepcion.crear(CatalogoMensajes.UtilSql.USUARIO_ERROR_VALIDANDO_SI_TRANSACCION_ESTA_INICIADA, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosTransversalExcepcion.crear(CatalogoMensajes.UtilSql.USUARIO_ERROR_NO_CONTROLADO_VALIDANDO_SI_TRANSACCION_ESTA_INICIADA, excepcion.getMessage(), excepcion);
        }
    }

    public static void iniciarTransaccion(Connection conexion) {
        if (transaccionEstaIniciada(conexion) || !conexionEstaAbierta(conexion)) {
            throw KudosTransversalExcepcion.crear(CatalogoMensajes.UtilSql.USUARIO_ERROR_NO_ES_POSIBLE_INICIAR_TRANSACCION);
        }
        try {
            conexion.setAutoCommit(false);
        } catch (SQLException excepcion) {
            throw KudosTransversalExcepcion.crear(CatalogoMensajes.UtilSql.USUARIO_ERROR_INICIANDO_TRANSACCION, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosTransversalExcepcion.crear(CatalogoMensajes.UtilSql.USUARIO_ERROR_NO_CONTROLADO_INICIANDO_TRANSACCION, excepcion.getMessage(), excepcion);
        }
    }

    public static void confirmarTransaccion(Connection conexion) {
        if (!transaccionEstaIniciada(conexion)) {
            throw KudosTransversalExcepcion.crear(CatalogoMensajes.UtilSql.USUARIO_ERROR_NO_ES_POSIBLE_CONFIRMAR_TRANSACCION);
        }
        try {
            conexion.commit();
        } catch (SQLException excepcion) {
            throw KudosTransversalExcepcion.crear(CatalogoMensajes.UtilSql.USUARIO_ERROR_CONFIRMANDO_TRANSACCION, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosTransversalExcepcion.crear(CatalogoMensajes.UtilSql.USUARIO_ERROR_NO_CONTROLADO_CONFIRMANDO_TRANSACCION, excepcion.getMessage(), excepcion);
        }
    }

    public static void cancelarTransaccion(Connection conexion) {
        if (!transaccionEstaIniciada(conexion)) {
            throw KudosTransversalExcepcion.crear(CatalogoMensajes.UtilSql.USUARIO_ERROR_NO_ES_POSIBLE_CANCELAR_TRANSACCION);
        }
        try {
            conexion.rollback();
        } catch (SQLException excepcion) {
            throw KudosTransversalExcepcion.crear(CatalogoMensajes.UtilSql.USUARIO_ERROR_CANCELANDO_TRANSACCION, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosTransversalExcepcion.crear(CatalogoMensajes.UtilSql.USUARIO_ERROR_NO_CONTROLADO_CANCELANDO_TRANSACCION, excepcion.getMessage(), excepcion);
        }
    }

    public static void cerrarConexion(Connection conexion) {
        if (!conexionEstaAbierta(conexion)) {
            throw KudosTransversalExcepcion.crear(CatalogoMensajes.UtilSql.USUARIO_ERROR_NO_ES_POSIBLE_CERRAR_CONEXION);
        }
        try {
            conexion.close();
        } catch (SQLException excepcion) {
            throw KudosTransversalExcepcion.crear(CatalogoMensajes.UtilSql.USUARIO_ERROR_CERRANDO_CONEXION, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosTransversalExcepcion.crear(CatalogoMensajes.UtilSql.USUARIO_ERROR_NO_CONTROLADO_CERRANDO_CONEXION, excepcion.getMessage(), excepcion);
        }
    }
}
