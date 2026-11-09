package co.edu.uco.kudos.dao.datos.entidad.sqlserver;

import co.edu.uco.kudos.dao.datos.entidad.NotificacionDAO;
import co.edu.uco.kudos.dao.datos.entidad.SqlDAO;
import co.edu.uco.kudos.entidad.NotificacionEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosDatosExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotificacionSqlServerDAO extends SqlDAO implements NotificacionDAO {

    private static final String CONSULTA_BASE = """
            select t.id_notificacion,
                   t.id_destinatario,
                   t.tipo,
                   t.mensaje,
                   t.tipo_referencia,
                   t.id_referencia,
                   t.fecha_creacion,
                   t.leida,
                   t.fecha_lectura,
                   destinatario.nombres as destinatario_nombres,
                   destinatario.apellidos as destinatario_apellidos,
                   destinatario.correo_institucional as destinatario_correo_institucional,
                   destinatario.estado_cuenta as destinatario_estado_cuenta
            from notificacion as t
            left join usuario as destinatario on destinatario.id_usuario = t.id_destinatario
            """;
    private static final String ORDEN = " order by t.fecha_creacion desc";

    public NotificacionSqlServerDAO(Connection conexion) {
        super(conexion);
    }

    @Override
    public void crear(NotificacionEntidad entidad) {
        var sentenciaSql = """
                insert into notificacion (id_notificacion, id_destinatario, tipo, mensaje, tipo_referencia, id_referencia, fecha_creacion, leida, fecha_lectura)
                values (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, entidad.getId());
            sentencia.setObject(2, entidad.getDestinatario().getId());
            sentencia.setString(3, entidad.getTipo());
            sentencia.setString(4, entidad.getMensaje());
            asignarTextoOpcional(sentencia, 5, entidad.getTipoReferencia());
            asignarIdOpcional(sentencia, 6, entidad.getIdReferencia());
            sentencia.setObject(7, entidad.getFechaCreacion());
            sentencia.setBoolean(8, entidad.isLeida());
            asignarFechaOpcional(sentencia, 9, entidad.getFechaLectura());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.NotificacionSqlServerDAO.USUARIO_ERROR_CREANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.NotificacionSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CREANDO, excepcion.getMessage(), excepcion);
        }
    }

    @Override
    public NotificacionEntidad consultarPorId(UUID id) {
        var sentenciaSql = CONSULTA_BASE + " where t.id_notificacion = ?";
        var encontrado = new NotificacionEntidad.Builder().build();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, id);
            var resultado = sentencia.executeQuery();
            if (resultado.next()) {
                encontrado = armarNotificacion(resultado);
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.NotificacionSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.NotificacionSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        }
        return encontrado;
    }

    @Override
    public List<NotificacionEntidad> consultarPorFiltro(NotificacionEntidad filtro) {
        var condiciones = new ArrayList<String>();
        var parametros = new ArrayList<Object>();
        if (!UtilId.esValorDefecto(filtro.getId())) {
            condiciones.add("t.id_notificacion = ?");
            parametros.add(filtro.getId());
        }
        if (!UtilId.esValorDefecto(filtro.getDestinatario().getId())) {
            condiciones.add("t.id_destinatario = ?");
            parametros.add(filtro.getDestinatario().getId());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtro.getTipo())) {
            condiciones.add("t.tipo = ?");
            parametros.add(filtro.getTipo());
        }
        if (!UtilId.esValorDefecto(filtro.getIdReferencia())) {
            condiciones.add("t.id_referencia = ?");
            parametros.add(filtro.getIdReferencia());
        }
        var sentenciaSql = CONSULTA_BASE + (condiciones.isEmpty() ? "" : " where " + String.join(" and ", condiciones)) + ORDEN;
        return consultar(sentenciaSql, parametros, CatalogoMensajes.NotificacionSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_FILTRO, CatalogoMensajes.NotificacionSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO);
    }

    @Override
    public List<NotificacionEntidad> consultarTodos() {
        return consultar(CONSULTA_BASE + ORDEN, List.of(), CatalogoMensajes.NotificacionSqlServerDAO.USUARIO_ERROR_CONSULTANDO_TODOS, CatalogoMensajes.NotificacionSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS);
    }

    @Override
    public void actualizar(UUID id, NotificacionEntidad entidad) {
        var sentenciaSql = """
                update notificacion set id_destinatario = ?, tipo = ?, mensaje = ?, tipo_referencia = ?, id_referencia = ?, fecha_creacion = ?, leida = ?, fecha_lectura = ?
                where id_notificacion = ?
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, entidad.getDestinatario().getId());
            sentencia.setString(2, entidad.getTipo());
            sentencia.setString(3, entidad.getMensaje());
            asignarTextoOpcional(sentencia, 4, entidad.getTipoReferencia());
            asignarIdOpcional(sentencia, 5, entidad.getIdReferencia());
            sentencia.setObject(6, entidad.getFechaCreacion());
            sentencia.setBoolean(7, entidad.isLeida());
            asignarFechaOpcional(sentencia, 8, entidad.getFechaLectura());
            sentencia.setObject(9, id);
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.NotificacionSqlServerDAO.USUARIO_ERROR_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.NotificacionSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        }
    }

    private List<NotificacionEntidad> consultar(String sentenciaSql, List<Object> parametros, String mensajeError, String mensajeErrorNoControlado) {
        var encontrados = new ArrayList<NotificacionEntidad>();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            asignarParametros(sentencia, parametros);
            var resultado = sentencia.executeQuery();
            while (resultado.next()) {
                encontrados.add(armarNotificacion(resultado));
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(mensajeError, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(mensajeErrorNoControlado, excepcion.getMessage(), excepcion);
        }
        return encontrados;
    }

    private NotificacionEntidad armarNotificacion(ResultSet resultado) throws SQLException {
        var destinatario = new UsuarioEntidad.Builder()
                .id(leerId(resultado, "id_destinatario"))
                .nombres(resultado.getString("destinatario_nombres"))
                .apellidos(resultado.getString("destinatario_apellidos"))
                .correoInstitucional(resultado.getString("destinatario_correo_institucional"))
                .estadoCuenta(resultado.getString("destinatario_estado_cuenta"))
                .build();
        return new NotificacionEntidad.Builder()
                .id(leerId(resultado, "id_notificacion"))
                .destinatario(destinatario)
                .tipo(resultado.getString("tipo"))
                .mensaje(resultado.getString("mensaje"))
                .tipoReferencia(resultado.getString("tipo_referencia"))
                .idReferencia(leerId(resultado, "id_referencia"))
                .fechaCreacion(leerFecha(resultado, "fecha_creacion"))
                .leida(resultado.getBoolean("leida"))
                .fechaLectura(leerFecha(resultado, "fecha_lectura"))
                .build();
    }
}
