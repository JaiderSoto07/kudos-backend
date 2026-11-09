package co.edu.uco.kudos.dao.datos.entidad.sqlserver;

import co.edu.uco.kudos.dao.datos.entidad.KudoDAO;
import co.edu.uco.kudos.dao.datos.entidad.SqlDAO;
import co.edu.uco.kudos.entidad.CategoriaEntidad;
import co.edu.uco.kudos.entidad.KudoEntidad;
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

public class KudoSqlServerDAO extends SqlDAO implements KudoDAO {

    private static final String CONSULTA_BASE = """
            select t.id_kudo,
                   t.id_autor,
                   t.id_destinatario,
                   t.id_categoria,
                   t.contenido,
                   t.fecha_creacion,
                   t.estado,
                   t.cantidad_visualizaciones,
                   t.promedio_calificacion,
                   t.cantidad_calificaciones,
                   autor.nombres as autor_nombres,
                   autor.apellidos as autor_apellidos,
                   autor.correo_institucional as autor_correo_institucional,
                   autor.estado_cuenta as autor_estado_cuenta,
                   destinatario.nombres as destinatario_nombres,
                   destinatario.apellidos as destinatario_apellidos,
                   destinatario.correo_institucional as destinatario_correo_institucional,
                   destinatario.estado_cuenta as destinatario_estado_cuenta,
                   categoria.nombre as categoria_nombre,
                   categoria.activa as categoria_activa
            from kudo as t
            left join usuario as autor on autor.id_usuario = t.id_autor
            left join usuario as destinatario on destinatario.id_usuario = t.id_destinatario
            left join categoria as categoria on categoria.id_categoria = t.id_categoria
            """;
    private static final String ORDEN = " order by t.fecha_creacion desc";

    public KudoSqlServerDAO(Connection conexion) {
        super(conexion);
    }

    @Override
    public void crear(KudoEntidad entidad) {
        var sentenciaSql = """
                insert into kudo (id_kudo, id_autor, id_destinatario, id_categoria, contenido, fecha_creacion, estado, cantidad_visualizaciones, promedio_calificacion, cantidad_calificaciones)
                values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, entidad.getId());
            sentencia.setObject(2, entidad.getAutor().getId());
            sentencia.setObject(3, entidad.getDestinatario().getId());
            sentencia.setObject(4, entidad.getCategoria().getId());
            sentencia.setString(5, entidad.getContenido());
            sentencia.setObject(6, entidad.getFechaCreacion());
            sentencia.setString(7, entidad.getEstado());
            sentencia.setInt(8, entidad.getCantidadVisualizaciones());
            sentencia.setBigDecimal(9, entidad.getPromedioCalificacion());
            sentencia.setInt(10, entidad.getCantidadCalificaciones());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.KudoSqlServerDAO.USUARIO_ERROR_CREANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.KudoSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CREANDO, excepcion.getMessage(), excepcion);
        }
    }

    @Override
    public KudoEntidad consultarPorId(UUID id) {
        var sentenciaSql = CONSULTA_BASE + " where t.id_kudo = ?";
        var encontrado = new KudoEntidad.Builder().build();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, id);
            var resultado = sentencia.executeQuery();
            if (resultado.next()) {
                encontrado = armarKudo(resultado);
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.KudoSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.KudoSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        }
        return encontrado;
    }

    @Override
    public List<KudoEntidad> consultarPorFiltro(KudoEntidad filtro) {
        var condiciones = new ArrayList<String>();
        var parametros = new ArrayList<Object>();
        if (!UtilId.esValorDefecto(filtro.getId())) {
            condiciones.add("t.id_kudo = ?");
            parametros.add(filtro.getId());
        }
        if (!UtilId.esValorDefecto(filtro.getAutor().getId())) {
            condiciones.add("t.id_autor = ?");
            parametros.add(filtro.getAutor().getId());
        }
        if (!UtilId.esValorDefecto(filtro.getDestinatario().getId())) {
            condiciones.add("t.id_destinatario = ?");
            parametros.add(filtro.getDestinatario().getId());
        }
        if (!UtilId.esValorDefecto(filtro.getCategoria().getId())) {
            condiciones.add("t.id_categoria = ?");
            parametros.add(filtro.getCategoria().getId());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtro.getEstado())) {
            condiciones.add("t.estado = ?");
            parametros.add(filtro.getEstado());
        }
        var sentenciaSql = CONSULTA_BASE + (condiciones.isEmpty() ? "" : " where " + String.join(" and ", condiciones)) + ORDEN;
        return consultar(sentenciaSql, parametros, CatalogoMensajes.KudoSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_FILTRO, CatalogoMensajes.KudoSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO);
    }

    @Override
    public List<KudoEntidad> consultarTodos() {
        return consultar(CONSULTA_BASE + ORDEN, List.of(), CatalogoMensajes.KudoSqlServerDAO.USUARIO_ERROR_CONSULTANDO_TODOS, CatalogoMensajes.KudoSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS);
    }

    @Override
    public void actualizar(UUID id, KudoEntidad entidad) {
        var sentenciaSql = """
                update kudo set id_autor = ?, id_destinatario = ?, id_categoria = ?, contenido = ?, fecha_creacion = ?, estado = ?, cantidad_visualizaciones = ?, promedio_calificacion = ?, cantidad_calificaciones = ?
                where id_kudo = ?
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, entidad.getAutor().getId());
            sentencia.setObject(2, entidad.getDestinatario().getId());
            sentencia.setObject(3, entidad.getCategoria().getId());
            sentencia.setString(4, entidad.getContenido());
            sentencia.setObject(5, entidad.getFechaCreacion());
            sentencia.setString(6, entidad.getEstado());
            sentencia.setInt(7, entidad.getCantidadVisualizaciones());
            sentencia.setBigDecimal(8, entidad.getPromedioCalificacion());
            sentencia.setInt(9, entidad.getCantidadCalificaciones());
            sentencia.setObject(10, id);
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.KudoSqlServerDAO.USUARIO_ERROR_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.KudoSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        }
    }

    private List<KudoEntidad> consultar(String sentenciaSql, List<Object> parametros, String mensajeError, String mensajeErrorNoControlado) {
        var encontrados = new ArrayList<KudoEntidad>();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            asignarParametros(sentencia, parametros);
            var resultado = sentencia.executeQuery();
            while (resultado.next()) {
                encontrados.add(armarKudo(resultado));
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(mensajeError, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(mensajeErrorNoControlado, excepcion.getMessage(), excepcion);
        }
        return encontrados;
    }

    private KudoEntidad armarKudo(ResultSet resultado) throws SQLException {
        var autor = new UsuarioEntidad.Builder()
                .id(leerId(resultado, "id_autor"))
                .nombres(resultado.getString("autor_nombres"))
                .apellidos(resultado.getString("autor_apellidos"))
                .correoInstitucional(resultado.getString("autor_correo_institucional"))
                .estadoCuenta(resultado.getString("autor_estado_cuenta"))
                .build();
        var destinatario = new UsuarioEntidad.Builder()
                .id(leerId(resultado, "id_destinatario"))
                .nombres(resultado.getString("destinatario_nombres"))
                .apellidos(resultado.getString("destinatario_apellidos"))
                .correoInstitucional(resultado.getString("destinatario_correo_institucional"))
                .estadoCuenta(resultado.getString("destinatario_estado_cuenta"))
                .build();
        var categoria = new CategoriaEntidad.Builder()
                .id(leerId(resultado, "id_categoria"))
                .nombre(resultado.getString("categoria_nombre"))
                .activa(resultado.getBoolean("categoria_activa"))
                .build();
        return new KudoEntidad.Builder()
                .id(leerId(resultado, "id_kudo"))
                .autor(autor)
                .destinatario(destinatario)
                .categoria(categoria)
                .contenido(resultado.getString("contenido"))
                .fechaCreacion(leerFecha(resultado, "fecha_creacion"))
                .estado(resultado.getString("estado"))
                .cantidadVisualizaciones(resultado.getInt("cantidad_visualizaciones"))
                .promedioCalificacion(resultado.getBigDecimal("promedio_calificacion"))
                .cantidadCalificaciones(resultado.getInt("cantidad_calificaciones"))
                .build();
    }
}
