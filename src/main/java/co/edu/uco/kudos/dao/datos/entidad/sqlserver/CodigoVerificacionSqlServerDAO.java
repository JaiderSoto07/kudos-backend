package co.edu.uco.kudos.dao.datos.entidad.sqlserver;

import co.edu.uco.kudos.dao.datos.entidad.CodigoVerificacionDAO;
import co.edu.uco.kudos.dao.datos.entidad.SqlDAO;
import co.edu.uco.kudos.entidad.CodigoVerificacionEntidad;
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

public class CodigoVerificacionSqlServerDAO extends SqlDAO implements CodigoVerificacionDAO {

    private static final String CONSULTA_BASE = """
            select t.id_codigo_verificacion,
                   t.id_usuario,
                   t.canal,
                   t.destino,
                   t.codigo,
                   t.fecha_generacion,
                   t.fecha_expiracion,
                   t.intentos_fallidos,
                   t.usado,
                   usuario.nombres as usuario_nombres,
                   usuario.apellidos as usuario_apellidos,
                   usuario.correo_institucional as usuario_correo_institucional,
                   usuario.estado_cuenta as usuario_estado_cuenta
            from codigo_verificacion as t
            left join usuario as usuario on usuario.id_usuario = t.id_usuario
            """;
    private static final String ORDEN = " order by t.fecha_generacion desc";

    public CodigoVerificacionSqlServerDAO(Connection conexion) {
        super(conexion);
    }

    @Override
    public void crear(CodigoVerificacionEntidad entidad) {
        var sentenciaSql = """
                insert into codigo_verificacion (id_codigo_verificacion, id_usuario, canal, destino, codigo, fecha_generacion, fecha_expiracion, intentos_fallidos, usado)
                values (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, entidad.getId());
            sentencia.setObject(2, entidad.getUsuario().getId());
            sentencia.setString(3, entidad.getCanal());
            sentencia.setString(4, entidad.getDestino());
            sentencia.setString(5, entidad.getCodigo());
            sentencia.setObject(6, entidad.getFechaGeneracion());
            sentencia.setObject(7, entidad.getFechaExpiracion());
            sentencia.setInt(8, entidad.getIntentosFallidos());
            sentencia.setBoolean(9, entidad.isUsado());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.CodigoVerificacionSqlServerDAO.USUARIO_ERROR_CREANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.CodigoVerificacionSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CREANDO, excepcion.getMessage(), excepcion);
        }
    }

    @Override
    public CodigoVerificacionEntidad consultarPorId(UUID id) {
        var sentenciaSql = CONSULTA_BASE + " where t.id_codigo_verificacion = ?";
        var encontrado = new CodigoVerificacionEntidad.Builder().build();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, id);
            var resultado = sentencia.executeQuery();
            if (resultado.next()) {
                encontrado = armarCodigoVerificacion(resultado);
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.CodigoVerificacionSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.CodigoVerificacionSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        }
        return encontrado;
    }

    @Override
    public List<CodigoVerificacionEntidad> consultarPorFiltro(CodigoVerificacionEntidad filtro) {
        var condiciones = new ArrayList<String>();
        var parametros = new ArrayList<Object>();
        if (!UtilId.esValorDefecto(filtro.getId())) {
            condiciones.add("t.id_codigo_verificacion = ?");
            parametros.add(filtro.getId());
        }
        if (!UtilId.esValorDefecto(filtro.getUsuario().getId())) {
            condiciones.add("t.id_usuario = ?");
            parametros.add(filtro.getUsuario().getId());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtro.getCanal())) {
            condiciones.add("t.canal = ?");
            parametros.add(filtro.getCanal());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtro.getDestino())) {
            condiciones.add("t.destino = ?");
            parametros.add(filtro.getDestino());
        }
        var sentenciaSql = CONSULTA_BASE + (condiciones.isEmpty() ? "" : " where " + String.join(" and ", condiciones)) + ORDEN;
        return consultar(sentenciaSql, parametros, CatalogoMensajes.CodigoVerificacionSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_FILTRO, CatalogoMensajes.CodigoVerificacionSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO);
    }

    @Override
    public List<CodigoVerificacionEntidad> consultarTodos() {
        return consultar(CONSULTA_BASE + ORDEN, List.of(), CatalogoMensajes.CodigoVerificacionSqlServerDAO.USUARIO_ERROR_CONSULTANDO_TODOS, CatalogoMensajes.CodigoVerificacionSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS);
    }

    @Override
    public void actualizar(UUID id, CodigoVerificacionEntidad entidad) {
        var sentenciaSql = """
                update codigo_verificacion set id_usuario = ?, canal = ?, destino = ?, codigo = ?, fecha_generacion = ?, fecha_expiracion = ?, intentos_fallidos = ?, usado = ?
                where id_codigo_verificacion = ?
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, entidad.getUsuario().getId());
            sentencia.setString(2, entidad.getCanal());
            sentencia.setString(3, entidad.getDestino());
            sentencia.setString(4, entidad.getCodigo());
            sentencia.setObject(5, entidad.getFechaGeneracion());
            sentencia.setObject(6, entidad.getFechaExpiracion());
            sentencia.setInt(7, entidad.getIntentosFallidos());
            sentencia.setBoolean(8, entidad.isUsado());
            sentencia.setObject(9, id);
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.CodigoVerificacionSqlServerDAO.USUARIO_ERROR_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.CodigoVerificacionSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        }
    }

    private List<CodigoVerificacionEntidad> consultar(String sentenciaSql, List<Object> parametros, String mensajeError, String mensajeErrorNoControlado) {
        var encontrados = new ArrayList<CodigoVerificacionEntidad>();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            asignarParametros(sentencia, parametros);
            var resultado = sentencia.executeQuery();
            while (resultado.next()) {
                encontrados.add(armarCodigoVerificacion(resultado));
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(mensajeError, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(mensajeErrorNoControlado, excepcion.getMessage(), excepcion);
        }
        return encontrados;
    }

    private CodigoVerificacionEntidad armarCodigoVerificacion(ResultSet resultado) throws SQLException {
        var usuario = new UsuarioEntidad.Builder()
                .id(leerId(resultado, "id_usuario"))
                .nombres(resultado.getString("usuario_nombres"))
                .apellidos(resultado.getString("usuario_apellidos"))
                .correoInstitucional(resultado.getString("usuario_correo_institucional"))
                .estadoCuenta(resultado.getString("usuario_estado_cuenta"))
                .build();
        return new CodigoVerificacionEntidad.Builder()
                .id(leerId(resultado, "id_codigo_verificacion"))
                .usuario(usuario)
                .canal(resultado.getString("canal"))
                .destino(resultado.getString("destino"))
                .codigo(resultado.getString("codigo"))
                .fechaGeneracion(leerFecha(resultado, "fecha_generacion"))
                .fechaExpiracion(leerFecha(resultado, "fecha_expiracion"))
                .intentosFallidos(resultado.getInt("intentos_fallidos"))
                .usado(resultado.getBoolean("usado"))
                .build();
    }
}
