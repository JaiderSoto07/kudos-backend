package co.edu.uco.kudos.dao.datos.entidad.sqlserver;

import co.edu.uco.kudos.dao.datos.entidad.ReplicaDAO;
import co.edu.uco.kudos.dao.datos.entidad.SqlDAO;
import co.edu.uco.kudos.entidad.KudoEntidad;
import co.edu.uco.kudos.entidad.ReplicaEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosDatosExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReplicaSqlServerDAO extends SqlDAO implements ReplicaDAO {

    private static final String CONSULTA_BASE = """
            select t.id_replica,
                   t.id_autor,
                   t.id_kudo,
                   t.id_replica_padre,
                   t.contenido,
                   t.fecha_creacion,
                   t.estado,
                   t.promedio_calificacion,
                   t.cantidad_calificaciones,
                   autor.nombres as autor_nombres,
                   autor.apellidos as autor_apellidos,
                   autor.correo_institucional as autor_correo_institucional,
                   autor.estado_cuenta as autor_estado_cuenta
            from replica as t
            left join usuario as autor on autor.id_usuario = t.id_autor
            """;
    private static final String ORDEN = " order by t.fecha_creacion asc";

    public ReplicaSqlServerDAO(Connection conexion) {
        super(conexion);
    }

    @Override
    public void crear(ReplicaEntidad entidad) {
        var sentenciaSql = """
                insert into replica (id_replica, id_autor, id_kudo, id_replica_padre, contenido, fecha_creacion, estado, promedio_calificacion, cantidad_calificaciones)
                values (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, entidad.getId());
            sentencia.setObject(2, entidad.getAutor().getId());
            sentencia.setObject(3, entidad.getKudo().getId());
            asignarIdOpcional(sentencia, 4, entidad.getIdReplicaPadre());
            sentencia.setString(5, entidad.getContenido());
            sentencia.setObject(6, entidad.getFechaCreacion());
            sentencia.setString(7, entidad.getEstado());
            sentencia.setBigDecimal(8, entidad.getPromedioCalificacion());
            sentencia.setInt(9, entidad.getCantidadCalificaciones());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ReplicaSqlServerDAO.USUARIO_ERROR_CREANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ReplicaSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CREANDO, excepcion.getMessage(), excepcion);
        }
    }

    @Override
    public ReplicaEntidad consultarPorId(UUID id) {
        var sentenciaSql = CONSULTA_BASE + " where t.id_replica = ?";
        var encontrado = new ReplicaEntidad.Builder().build();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, id);
            var resultado = sentencia.executeQuery();
            if (resultado.next()) {
                encontrado = armarReplica(resultado);
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ReplicaSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ReplicaSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        }
        return encontrado;
    }

    @Override
    public List<ReplicaEntidad> consultarPorFiltro(ReplicaEntidad filtro) {
        var condiciones = new ArrayList<String>();
        var parametros = new ArrayList<Object>();
        agregarCondicionId(condiciones, parametros, "t.id_replica", filtro.getId());
        agregarCondicionId(condiciones, parametros, "t.id_autor", filtro.getAutor().getId());
        agregarCondicionId(condiciones, parametros, "t.id_kudo", filtro.getKudo().getId());
        agregarCondicionId(condiciones, parametros, "t.id_replica_padre", filtro.getIdReplicaPadre());
        if (!UtilTexto.getUtilTexto().esVacia(filtro.getEstado())) {
            condiciones.add("t.estado = ?");
            parametros.add(filtro.getEstado());
        }
        var sentenciaSql = CONSULTA_BASE + (condiciones.isEmpty() ? "" : " where " + String.join(" and ", condiciones)) + ORDEN;
        return consultar(sentenciaSql, parametros, CatalogoMensajes.ReplicaSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_FILTRO, CatalogoMensajes.ReplicaSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO);
    }

    @Override
    public List<ReplicaEntidad> consultarTodos() {
        return consultar(CONSULTA_BASE + ORDEN, List.of(), CatalogoMensajes.ReplicaSqlServerDAO.USUARIO_ERROR_CONSULTANDO_TODOS, CatalogoMensajes.ReplicaSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS);
    }

    @Override
    public void actualizar(UUID id, ReplicaEntidad entidad) {
        var sentenciaSql = """
                update replica set id_autor = ?, id_kudo = ?, id_replica_padre = ?, contenido = ?, fecha_creacion = ?, estado = ?, promedio_calificacion = ?, cantidad_calificaciones = ?
                where id_replica = ?
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, entidad.getAutor().getId());
            sentencia.setObject(2, entidad.getKudo().getId());
            asignarIdOpcional(sentencia, 3, entidad.getIdReplicaPadre());
            sentencia.setString(4, entidad.getContenido());
            sentencia.setObject(5, entidad.getFechaCreacion());
            sentencia.setString(6, entidad.getEstado());
            sentencia.setBigDecimal(7, entidad.getPromedioCalificacion());
            sentencia.setInt(8, entidad.getCantidadCalificaciones());
            sentencia.setObject(9, id);
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ReplicaSqlServerDAO.USUARIO_ERROR_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ReplicaSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        }
    }

    private List<ReplicaEntidad> consultar(String sentenciaSql, List<Object> parametros, String mensajeError, String mensajeErrorNoControlado) {
        var encontrados = new ArrayList<ReplicaEntidad>();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            asignarParametros(sentencia, parametros);
            var resultado = sentencia.executeQuery();
            while (resultado.next()) {
                encontrados.add(armarReplica(resultado));
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(mensajeError, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(mensajeErrorNoControlado, excepcion.getMessage(), excepcion);
        }
        return encontrados;
    }

    private ReplicaEntidad armarReplica(ResultSet resultado) throws SQLException {
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
        return new ReplicaEntidad.Builder()
                .id(leerId(resultado, "id_replica"))
                .autor(autor)
                .kudo(kudo)
                .idReplicaPadre(leerId(resultado, "id_replica_padre"))
                .contenido(resultado.getString("contenido"))
                .fechaCreacion(leerFecha(resultado, "fecha_creacion"))
                .estado(resultado.getString("estado"))
                .promedioCalificacion(resultado.getBigDecimal("promedio_calificacion"))
                .cantidadCalificaciones(resultado.getInt("cantidad_calificaciones"))
                .build();
    }
}
