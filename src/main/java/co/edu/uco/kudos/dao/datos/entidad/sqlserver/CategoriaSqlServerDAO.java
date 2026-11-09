package co.edu.uco.kudos.dao.datos.entidad.sqlserver;

import co.edu.uco.kudos.dao.datos.entidad.CategoriaDAO;
import co.edu.uco.kudos.dao.datos.entidad.SqlDAO;
import co.edu.uco.kudos.entidad.CategoriaEntidad;
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

public class CategoriaSqlServerDAO extends SqlDAO implements CategoriaDAO {

    private static final String CONSULTA_BASE = """
            select t.id_categoria,
                   t.nombre,
                   t.descripcion,
                   t.activa,
                   t.fecha_creacion
            from categoria as t
            """;
    private static final String ORDEN = " order by t.nombre";

    public CategoriaSqlServerDAO(Connection conexion) {
        super(conexion);
    }

    @Override
    public void crear(CategoriaEntidad entidad) {
        var sentenciaSql = """
                insert into categoria (id_categoria, nombre, descripcion, activa, fecha_creacion)
                values (?, ?, ?, ?, ?)
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, entidad.getId());
            sentencia.setString(2, entidad.getNombre());
            sentencia.setString(3, entidad.getDescripcion());
            sentencia.setBoolean(4, entidad.isActiva());
            sentencia.setObject(5, entidad.getFechaCreacion());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.CategoriaSqlServerDAO.USUARIO_ERROR_CREANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.CategoriaSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CREANDO, excepcion.getMessage(), excepcion);
        }
    }

    @Override
    public CategoriaEntidad consultarPorId(UUID id) {
        var sentenciaSql = CONSULTA_BASE + " where t.id_categoria = ?";
        var encontrado = new CategoriaEntidad.Builder().build();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, id);
            var resultado = sentencia.executeQuery();
            if (resultado.next()) {
                encontrado = armarCategoria(resultado);
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.CategoriaSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.CategoriaSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        }
        return encontrado;
    }

    @Override
    public List<CategoriaEntidad> consultarPorFiltro(CategoriaEntidad filtro) {
        var condiciones = new ArrayList<String>();
        var parametros = new ArrayList<Object>();
        if (!UtilId.esValorDefecto(filtro.getId())) {
            condiciones.add("t.id_categoria = ?");
            parametros.add(filtro.getId());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtro.getNombre())) {
            condiciones.add("t.nombre = ?");
            parametros.add(filtro.getNombre());
        }
        var sentenciaSql = CONSULTA_BASE + (condiciones.isEmpty() ? "" : " where " + String.join(" and ", condiciones)) + ORDEN;
        return consultar(sentenciaSql, parametros, CatalogoMensajes.CategoriaSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_FILTRO, CatalogoMensajes.CategoriaSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO);
    }

    @Override
    public List<CategoriaEntidad> consultarTodos() {
        return consultar(CONSULTA_BASE + ORDEN, List.of(), CatalogoMensajes.CategoriaSqlServerDAO.USUARIO_ERROR_CONSULTANDO_TODOS, CatalogoMensajes.CategoriaSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS);
    }

    @Override
    public List<CategoriaEntidad> consultarActivos() {
        return consultar(CONSULTA_BASE + " where t.activa = 1" + ORDEN, List.of(), CatalogoMensajes.CategoriaSqlServerDAO.USUARIO_ERROR_CONSULTANDO_ACTIVOS, CatalogoMensajes.CategoriaSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_ACTIVOS);
    }

    @Override
    public void actualizar(UUID id, CategoriaEntidad entidad) {
        var sentenciaSql = """
                update categoria set nombre = ?, descripcion = ?, activa = ?, fecha_creacion = ?
                where id_categoria = ?
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setString(1, entidad.getNombre());
            sentencia.setString(2, entidad.getDescripcion());
            sentencia.setBoolean(3, entidad.isActiva());
            sentencia.setObject(4, entidad.getFechaCreacion());
            sentencia.setObject(5, id);
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.CategoriaSqlServerDAO.USUARIO_ERROR_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.CategoriaSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        }
    }

    private List<CategoriaEntidad> consultar(String sentenciaSql, List<Object> parametros, String mensajeError, String mensajeErrorNoControlado) {
        var encontrados = new ArrayList<CategoriaEntidad>();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            asignarParametros(sentencia, parametros);
            var resultado = sentencia.executeQuery();
            while (resultado.next()) {
                encontrados.add(armarCategoria(resultado));
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(mensajeError, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(mensajeErrorNoControlado, excepcion.getMessage(), excepcion);
        }
        return encontrados;
    }

    private CategoriaEntidad armarCategoria(ResultSet resultado) throws SQLException {
        return new CategoriaEntidad.Builder()
                .id(leerId(resultado, "id_categoria"))
                .nombre(resultado.getString("nombre"))
                .descripcion(resultado.getString("descripcion"))
                .activa(resultado.getBoolean("activa"))
                .fechaCreacion(leerFecha(resultado, "fecha_creacion"))
                .build();
    }
}
