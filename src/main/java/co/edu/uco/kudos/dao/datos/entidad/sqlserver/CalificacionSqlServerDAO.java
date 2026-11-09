package co.edu.uco.kudos.dao.datos.entidad.sqlserver;

import co.edu.uco.kudos.dao.datos.entidad.CalificacionDAO;
import co.edu.uco.kudos.dao.datos.entidad.SqlDAO;
import co.edu.uco.kudos.entidad.CalificacionEntidad;
import co.edu.uco.kudos.entidad.KudoEntidad;
import co.edu.uco.kudos.entidad.ReplicaEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosDatosExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CalificacionSqlServerDAO extends SqlDAO implements CalificacionDAO {

    private static final String CONSULTA_BASE = """
            select t.id_calificacion,
                   t.id_autor,
                   t.id_kudo,
                   t.id_replica,
                   t.estrellas,
                   t.fecha_calificacion,
                   t.fecha_modificacion,
                   t.activa,
                   autor.nombres as autor_nombres,
                   autor.apellidos as autor_apellidos,
                   autor.correo_institucional as autor_correo_institucional,
                   autor.estado_cuenta as autor_estado_cuenta
            from calificacion as t
            left join usuario as autor on autor.id_usuario = t.id_autor
            """;
    private static final String ORDEN = " order by t.fecha_calificacion desc";

    public CalificacionSqlServerDAO(Connection conexion) {
        super(conexion);
    }

    @Override
    public void crear(CalificacionEntidad entidad) {
        var sentenciaSql = """
                insert into calificacion (id_calificacion, id_autor, id_kudo, id_replica, estrellas, fecha_calificacion, fecha_modificacion, activa)
                values (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, entidad.getId());
            sentencia.setObject(2, entidad.getAutor().getId());
            asignarIdOpcional(sentencia, 3, entidad.getKudo().getId());
            asignarIdOpcional(sentencia, 4, entidad.getReplica().getId());
            sentencia.setInt(5, entidad.getEstrellas());
            sentencia.setObject(6, entidad.getFechaCalificacion());
            asignarFechaOpcional(sentencia, 7, entidad.getFechaModificacion());
            sentencia.setBoolean(8, entidad.isActiva());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.CalificacionSqlServerDAO.USUARIO_ERROR_CREANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.CalificacionSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CREANDO, excepcion.getMessage(), excepcion);
        }
    }

    @Override
    public CalificacionEntidad consultarPorId(UUID id) {
        var sentenciaSql = CONSULTA_BASE + " where t.id_calificacion = ?";
        var encontrado = new CalificacionEntidad.Builder().build();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, id);
            var resultado = sentencia.executeQuery();
            if (resultado.next()) {
                encontrado = armarCalificacion(resultado);
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.CalificacionSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.CalificacionSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        }
        return encontrado;
    }

    @Override
    public List<CalificacionEntidad> consultarPorFiltro(CalificacionEntidad filtro) {
        var condiciones = new ArrayList<String>();
        var parametros = new ArrayList<Object>();
        if (!UtilId.esValorDefecto(filtro.getId())) {
            condiciones.add("t.id_calificacion = ?");
            parametros.add(filtro.getId());
        }
        if (!UtilId.esValorDefecto(filtro.getAutor().getId())) {
            condiciones.add("t.id_autor = ?");
            parametros.add(filtro.getAutor().getId());
        }
        if (!UtilId.esValorDefecto(filtro.getKudo().getId())) {
            condiciones.add("t.id_kudo = ?");
            parametros.add(filtro.getKudo().getId());
        }
        if (!UtilId.esValorDefecto(filtro.getReplica().getId())) {
            condiciones.add("t.id_replica = ?");
            parametros.add(filtro.getReplica().getId());
        }
        var sentenciaSql = CONSULTA_BASE + (condiciones.isEmpty() ? "" : " where " + String.join(" and ", condiciones)) + ORDEN;
        return consultar(sentenciaSql, parametros, CatalogoMensajes.CalificacionSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_FILTRO, CatalogoMensajes.CalificacionSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO);
    }

    @Override
    public List<CalificacionEntidad> consultarTodos() {
        return consultar(CONSULTA_BASE + ORDEN, List.of(), CatalogoMensajes.CalificacionSqlServerDAO.USUARIO_ERROR_CONSULTANDO_TODOS, CatalogoMensajes.CalificacionSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS);
    }

    @Override
    public void actualizar(UUID id, CalificacionEntidad entidad) {
        var sentenciaSql = """
                update calificacion set id_autor = ?, id_kudo = ?, id_replica = ?, estrellas = ?, fecha_calificacion = ?, fecha_modificacion = ?, activa = ?
                where id_calificacion = ?
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, entidad.getAutor().getId());
            asignarIdOpcional(sentencia, 2, entidad.getKudo().getId());
            asignarIdOpcional(sentencia, 3, entidad.getReplica().getId());
            sentencia.setInt(4, entidad.getEstrellas());
            sentencia.setObject(5, entidad.getFechaCalificacion());
            asignarFechaOpcional(sentencia, 6, entidad.getFechaModificacion());
            sentencia.setBoolean(7, entidad.isActiva());
            sentencia.setObject(8, id);
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.CalificacionSqlServerDAO.USUARIO_ERROR_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.CalificacionSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        }
    }

    private List<CalificacionEntidad> consultar(String sentenciaSql, List<Object> parametros, String mensajeError, String mensajeErrorNoControlado) {
        var encontrados = new ArrayList<CalificacionEntidad>();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            asignarParametros(sentencia, parametros);
            var resultado = sentencia.executeQuery();
            while (resultado.next()) {
                encontrados.add(armarCalificacion(resultado));
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(mensajeError, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(mensajeErrorNoControlado, excepcion.getMessage(), excepcion);
        }
        return encontrados;
    }

    private CalificacionEntidad armarCalificacion(ResultSet resultado) throws SQLException {
        var autor = new UsuarioEntidad.Builder()
                .id(leerId(resultado, "id_autor"))
                .nombres(resultado.getString("autor_nombres"))
                .apellidos(resultado.getString("autor_apellidos"))
                .correoInstitucional(resultado.getString("autor_correo_institucional"))
                .estadoCuenta(resultado.getString("autor_estado_cuenta"))
                .build();
        var kudo = new KudoEntidad.Builder()
                .id(leerId(resultado, "id_kudo"))
                .build();
        var replica = new ReplicaEntidad.Builder()
                .id(leerId(resultado, "id_replica"))
                .build();
        return new CalificacionEntidad.Builder()
                .id(leerId(resultado, "id_calificacion"))
                .autor(autor)
                .kudo(kudo)
                .replica(replica)
                .estrellas(resultado.getInt("estrellas"))
                .fechaCalificacion(leerFecha(resultado, "fecha_calificacion"))
                .fechaModificacion(leerFecha(resultado, "fecha_modificacion"))
                .activa(resultado.getBoolean("activa"))
                .build();
    }
}
