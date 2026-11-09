package co.edu.uco.kudos.dao.datos.entidad.sqlserver;

import co.edu.uco.kudos.dao.datos.entidad.ParametroGlobalDAO;
import co.edu.uco.kudos.dao.datos.entidad.SqlDAO;
import co.edu.uco.kudos.entidad.ParametroGlobalEntidad;
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

public class ParametroGlobalSqlServerDAO extends SqlDAO implements ParametroGlobalDAO {

    private static final String CONSULTA_BASE = """
            select t.id_parametro_global,
                   t.clave,
                   t.valor,
                   t.tipo_dato,
                   t.descripcion,
                   t.fecha_modificacion
            from parametro_global as t
            """;
    private static final String ORDEN = " order by t.clave";

    public ParametroGlobalSqlServerDAO(Connection conexion) {
        super(conexion);
    }

    @Override
    public void crear(ParametroGlobalEntidad entidad) {
        var sentenciaSql = """
                insert into parametro_global (id_parametro_global, clave, valor, tipo_dato, descripcion, fecha_modificacion)
                values (?, ?, ?, ?, ?, ?)
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, entidad.getId());
            sentencia.setString(2, entidad.getClave());
            sentencia.setString(3, entidad.getValor());
            sentencia.setString(4, entidad.getTipoDato());
            sentencia.setString(5, entidad.getDescripcion());
            sentencia.setObject(6, entidad.getFechaModificacion());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ParametroGlobalSqlServerDAO.USUARIO_ERROR_CREANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ParametroGlobalSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CREANDO, excepcion.getMessage(), excepcion);
        }
    }

    @Override
    public ParametroGlobalEntidad consultarPorId(UUID id) {
        var sentenciaSql = CONSULTA_BASE + " where t.id_parametro_global = ?";
        var encontrado = new ParametroGlobalEntidad.Builder().build();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, id);
            var resultado = sentencia.executeQuery();
            if (resultado.next()) {
                encontrado = armarParametroGlobal(resultado);
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ParametroGlobalSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ParametroGlobalSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        }
        return encontrado;
    }

    @Override
    public List<ParametroGlobalEntidad> consultarPorFiltro(ParametroGlobalEntidad filtro) {
        var condiciones = new ArrayList<String>();
        var parametros = new ArrayList<Object>();
        if (!UtilId.esValorDefecto(filtro.getId())) {
            condiciones.add("t.id_parametro_global = ?");
            parametros.add(filtro.getId());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtro.getClave())) {
            condiciones.add("t.clave = ?");
            parametros.add(filtro.getClave());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtro.getValor())) {
            condiciones.add("t.valor = ?");
            parametros.add(filtro.getValor());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtro.getTipoDato())) {
            condiciones.add("t.tipo_dato = ?");
            parametros.add(filtro.getTipoDato());
        }
        var sentenciaSql = CONSULTA_BASE + (condiciones.isEmpty() ? "" : " where " + String.join(" and ", condiciones)) + ORDEN;
        return consultar(sentenciaSql, parametros, CatalogoMensajes.ParametroGlobalSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_FILTRO, CatalogoMensajes.ParametroGlobalSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO);
    }

    @Override
    public List<ParametroGlobalEntidad> consultarTodos() {
        return consultar(CONSULTA_BASE + ORDEN, List.of(), CatalogoMensajes.ParametroGlobalSqlServerDAO.USUARIO_ERROR_CONSULTANDO_TODOS, CatalogoMensajes.ParametroGlobalSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS);
    }

    @Override
    public void actualizar(UUID id, ParametroGlobalEntidad entidad) {
        var sentenciaSql = """
                update parametro_global set clave = ?, valor = ?, tipo_dato = ?, descripcion = ?, fecha_modificacion = ?
                where id_parametro_global = ?
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setString(1, entidad.getClave());
            sentencia.setString(2, entidad.getValor());
            sentencia.setString(3, entidad.getTipoDato());
            sentencia.setString(4, entidad.getDescripcion());
            sentencia.setObject(5, entidad.getFechaModificacion());
            sentencia.setObject(6, id);
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ParametroGlobalSqlServerDAO.USUARIO_ERROR_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ParametroGlobalSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        }
    }

    private List<ParametroGlobalEntidad> consultar(String sentenciaSql, List<Object> parametros, String mensajeError, String mensajeErrorNoControlado) {
        var encontrados = new ArrayList<ParametroGlobalEntidad>();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            asignarParametros(sentencia, parametros);
            var resultado = sentencia.executeQuery();
            while (resultado.next()) {
                encontrados.add(armarParametroGlobal(resultado));
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(mensajeError, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(mensajeErrorNoControlado, excepcion.getMessage(), excepcion);
        }
        return encontrados;
    }

    private ParametroGlobalEntidad armarParametroGlobal(ResultSet resultado) throws SQLException {
        return new ParametroGlobalEntidad.Builder()
                .id(leerId(resultado, "id_parametro_global"))
                .clave(resultado.getString("clave"))
                .valor(resultado.getString("valor"))
                .tipoDato(resultado.getString("tipo_dato"))
                .descripcion(resultado.getString("descripcion"))
                .fechaModificacion(leerFecha(resultado, "fecha_modificacion"))
                .build();
    }
}
