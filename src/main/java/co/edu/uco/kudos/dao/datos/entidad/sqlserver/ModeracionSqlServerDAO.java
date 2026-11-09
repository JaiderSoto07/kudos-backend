package co.edu.uco.kudos.dao.datos.entidad.sqlserver;

import co.edu.uco.kudos.dao.datos.entidad.ModeracionDAO;
import co.edu.uco.kudos.dao.datos.entidad.SqlDAO;
import co.edu.uco.kudos.entidad.KudoEntidad;
import co.edu.uco.kudos.entidad.ModeracionEntidad;
import co.edu.uco.kudos.entidad.ReplicaEntidad;
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

public class ModeracionSqlServerDAO extends SqlDAO implements ModeracionDAO {

    private static final String CONSULTA_BASE = """
            select t.id_moderacion,
                   t.id_kudo,
                   t.id_replica,
                   t.id_moderador,
                   t.estado,
                   t.fecha_inicio,
                   t.fecha_cierre,
                   t.decision,
                   t.motivo_decision,
                   moderador.nombres as moderador_nombres,
                   moderador.apellidos as moderador_apellidos,
                   moderador.correo_institucional as moderador_correo_institucional,
                   moderador.estado_cuenta as moderador_estado_cuenta
            from moderacion as t
            left join usuario as moderador on moderador.id_usuario = t.id_moderador
            """;
    private static final String ORDEN = " order by t.fecha_inicio desc";

    public ModeracionSqlServerDAO(Connection conexion) {
        super(conexion);
    }

    @Override
    public void crear(ModeracionEntidad entidad) {
        var sentenciaSql = """
                insert into moderacion (id_moderacion, id_kudo, id_replica, id_moderador, estado, fecha_inicio, fecha_cierre, decision, motivo_decision)
                values (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, entidad.getId());
            asignarIdOpcional(sentencia, 2, entidad.getKudo().getId());
            asignarIdOpcional(sentencia, 3, entidad.getReplica().getId());
            sentencia.setObject(4, entidad.getModerador().getId());
            sentencia.setString(5, entidad.getEstado());
            sentencia.setObject(6, entidad.getFechaInicio());
            asignarFechaOpcional(sentencia, 7, entidad.getFechaCierre());
            asignarTextoOpcional(sentencia, 8, entidad.getDecision());
            asignarTextoOpcional(sentencia, 9, entidad.getMotivoDecision());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ModeracionSqlServerDAO.USUARIO_ERROR_CREANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ModeracionSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CREANDO, excepcion.getMessage(), excepcion);
        }
    }

    @Override
    public ModeracionEntidad consultarPorId(UUID id) {
        var sentenciaSql = CONSULTA_BASE + " where t.id_moderacion = ?";
        var encontrado = new ModeracionEntidad.Builder().build();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, id);
            var resultado = sentencia.executeQuery();
            if (resultado.next()) {
                encontrado = armarModeracion(resultado);
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ModeracionSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ModeracionSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        }
        return encontrado;
    }

    @Override
    public List<ModeracionEntidad> consultarPorFiltro(ModeracionEntidad filtro) {
        var condiciones = new ArrayList<String>();
        var parametros = new ArrayList<Object>();
        if (!UtilId.esValorDefecto(filtro.getId())) {
            condiciones.add("t.id_moderacion = ?");
            parametros.add(filtro.getId());
        }
        if (!UtilId.esValorDefecto(filtro.getKudo().getId())) {
            condiciones.add("t.id_kudo = ?");
            parametros.add(filtro.getKudo().getId());
        }
        if (!UtilId.esValorDefecto(filtro.getReplica().getId())) {
            condiciones.add("t.id_replica = ?");
            parametros.add(filtro.getReplica().getId());
        }
        if (!UtilId.esValorDefecto(filtro.getModerador().getId())) {
            condiciones.add("t.id_moderador = ?");
            parametros.add(filtro.getModerador().getId());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtro.getEstado())) {
            condiciones.add("t.estado = ?");
            parametros.add(filtro.getEstado());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtro.getDecision())) {
            condiciones.add("t.decision = ?");
            parametros.add(filtro.getDecision());
        }
        var sentenciaSql = CONSULTA_BASE + (condiciones.isEmpty() ? "" : " where " + String.join(" and ", condiciones)) + ORDEN;
        return consultar(sentenciaSql, parametros, CatalogoMensajes.ModeracionSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_FILTRO, CatalogoMensajes.ModeracionSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO);
    }

    @Override
    public List<ModeracionEntidad> consultarTodos() {
        return consultar(CONSULTA_BASE + ORDEN, List.of(), CatalogoMensajes.ModeracionSqlServerDAO.USUARIO_ERROR_CONSULTANDO_TODOS, CatalogoMensajes.ModeracionSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS);
    }

    @Override
    public void actualizar(UUID id, ModeracionEntidad entidad) {
        var sentenciaSql = """
                update moderacion set id_kudo = ?, id_replica = ?, id_moderador = ?, estado = ?, fecha_inicio = ?, fecha_cierre = ?, decision = ?, motivo_decision = ?
                where id_moderacion = ?
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            asignarIdOpcional(sentencia, 1, entidad.getKudo().getId());
            asignarIdOpcional(sentencia, 2, entidad.getReplica().getId());
            sentencia.setObject(3, entidad.getModerador().getId());
            sentencia.setString(4, entidad.getEstado());
            sentencia.setObject(5, entidad.getFechaInicio());
            asignarFechaOpcional(sentencia, 6, entidad.getFechaCierre());
            asignarTextoOpcional(sentencia, 7, entidad.getDecision());
            asignarTextoOpcional(sentencia, 8, entidad.getMotivoDecision());
            sentencia.setObject(9, id);
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ModeracionSqlServerDAO.USUARIO_ERROR_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ModeracionSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        }
    }

    private List<ModeracionEntidad> consultar(String sentenciaSql, List<Object> parametros, String mensajeError, String mensajeErrorNoControlado) {
        var encontrados = new ArrayList<ModeracionEntidad>();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            asignarParametros(sentencia, parametros);
            var resultado = sentencia.executeQuery();
            while (resultado.next()) {
                encontrados.add(armarModeracion(resultado));
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(mensajeError, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(mensajeErrorNoControlado, excepcion.getMessage(), excepcion);
        }
        return encontrados;
    }

    private ModeracionEntidad armarModeracion(ResultSet resultado) throws SQLException {
        var kudo = new KudoEntidad.Builder()
                .id(leerId(resultado, "id_kudo"))
                .build();
        var replica = new ReplicaEntidad.Builder()
                .id(leerId(resultado, "id_replica"))
                .build();
        var moderador = new UsuarioEntidad.Builder()
                .id(leerId(resultado, "id_moderador"))
                .nombres(resultado.getString("moderador_nombres"))
                .apellidos(resultado.getString("moderador_apellidos"))
                .correoInstitucional(resultado.getString("moderador_correo_institucional"))
                .estadoCuenta(resultado.getString("moderador_estado_cuenta"))
                .build();
        return new ModeracionEntidad.Builder()
                .id(leerId(resultado, "id_moderacion"))
                .kudo(kudo)
                .replica(replica)
                .moderador(moderador)
                .estado(resultado.getString("estado"))
                .fechaInicio(leerFecha(resultado, "fecha_inicio"))
                .fechaCierre(leerFecha(resultado, "fecha_cierre"))
                .decision(resultado.getString("decision"))
                .motivoDecision(resultado.getString("motivo_decision"))
                .build();
    }
}
