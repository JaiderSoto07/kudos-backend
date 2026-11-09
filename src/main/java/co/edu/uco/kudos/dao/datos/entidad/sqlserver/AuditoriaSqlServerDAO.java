package co.edu.uco.kudos.dao.datos.entidad.sqlserver;

import co.edu.uco.kudos.dao.datos.entidad.AuditoriaDAO;
import co.edu.uco.kudos.dao.datos.entidad.SqlDAO;
import co.edu.uco.kudos.entidad.AuditoriaEntidad;
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

public class AuditoriaSqlServerDAO extends SqlDAO implements AuditoriaDAO {

    private static final String CONSULTA_BASE = """
            select t.id_auditoria,
                   t.fecha_hora,
                   t.id_actor,
                   t.accion,
                   t.tipo_objeto,
                   t.id_objeto,
                   t.estado_anterior,
                   t.estado_nuevo,
                   t.motivo,
                   t.detalle,
                   actor.nombres as actor_nombres,
                   actor.apellidos as actor_apellidos,
                   actor.correo_institucional as actor_correo_institucional,
                   actor.estado_cuenta as actor_estado_cuenta
            from auditoria as t
            left join usuario as actor on actor.id_usuario = t.id_actor
            """;
    private static final String ORDEN = " order by t.fecha_hora desc";

    public AuditoriaSqlServerDAO(Connection conexion) {
        super(conexion);
    }

    @Override
    public void crear(AuditoriaEntidad entidad) {
        var sentenciaSql = """
                insert into auditoria (id_auditoria, fecha_hora, id_actor, accion, tipo_objeto, id_objeto, estado_anterior, estado_nuevo, motivo, detalle)
                values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, entidad.getId());
            sentencia.setObject(2, entidad.getFechaHora());
            asignarIdOpcional(sentencia, 3, entidad.getActor().getId());
            sentencia.setString(4, entidad.getAccion());
            sentencia.setString(5, entidad.getTipoObjeto());
            sentencia.setObject(6, entidad.getIdObjeto());
            asignarTextoOpcional(sentencia, 7, entidad.getEstadoAnterior());
            asignarTextoOpcional(sentencia, 8, entidad.getEstadoNuevo());
            asignarTextoOpcional(sentencia, 9, entidad.getMotivo());
            asignarTextoOpcional(sentencia, 10, entidad.getDetalle());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.AuditoriaSqlServerDAO.USUARIO_ERROR_CREANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.AuditoriaSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CREANDO, excepcion.getMessage(), excepcion);
        }
    }

    @Override
    public AuditoriaEntidad consultarPorId(UUID id) {
        var sentenciaSql = CONSULTA_BASE + " where t.id_auditoria = ?";
        var encontrado = new AuditoriaEntidad.Builder().build();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, id);
            var resultado = sentencia.executeQuery();
            if (resultado.next()) {
                encontrado = armarAuditoria(resultado);
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.AuditoriaSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.AuditoriaSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        }
        return encontrado;
    }

    @Override
    public List<AuditoriaEntidad> consultarPorFiltro(AuditoriaEntidad filtro) {
        var condiciones = new ArrayList<String>();
        var parametros = new ArrayList<Object>();
        if (!UtilId.esValorDefecto(filtro.getId())) {
            condiciones.add("t.id_auditoria = ?");
            parametros.add(filtro.getId());
        }
        if (!UtilId.esValorDefecto(filtro.getActor().getId())) {
            condiciones.add("t.id_actor = ?");
            parametros.add(filtro.getActor().getId());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtro.getAccion())) {
            condiciones.add("t.accion = ?");
            parametros.add(filtro.getAccion());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtro.getTipoObjeto())) {
            condiciones.add("t.tipo_objeto = ?");
            parametros.add(filtro.getTipoObjeto());
        }
        if (!UtilId.esValorDefecto(filtro.getIdObjeto())) {
            condiciones.add("t.id_objeto = ?");
            parametros.add(filtro.getIdObjeto());
        }
        var sentenciaSql = CONSULTA_BASE + (condiciones.isEmpty() ? "" : " where " + String.join(" and ", condiciones)) + ORDEN;
        return consultar(sentenciaSql, parametros, CatalogoMensajes.AuditoriaSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_FILTRO, CatalogoMensajes.AuditoriaSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO);
    }

    @Override
    public List<AuditoriaEntidad> consultarTodos() {
        return consultar(CONSULTA_BASE + ORDEN, List.of(), CatalogoMensajes.AuditoriaSqlServerDAO.USUARIO_ERROR_CONSULTANDO_TODOS, CatalogoMensajes.AuditoriaSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS);
    }

    private List<AuditoriaEntidad> consultar(String sentenciaSql, List<Object> parametros, String mensajeError, String mensajeErrorNoControlado) {
        var encontrados = new ArrayList<AuditoriaEntidad>();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            asignarParametros(sentencia, parametros);
            var resultado = sentencia.executeQuery();
            while (resultado.next()) {
                encontrados.add(armarAuditoria(resultado));
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(mensajeError, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(mensajeErrorNoControlado, excepcion.getMessage(), excepcion);
        }
        return encontrados;
    }

    private AuditoriaEntidad armarAuditoria(ResultSet resultado) throws SQLException {
        var actor = new UsuarioEntidad.Builder()
                .id(leerId(resultado, "id_actor"))
                .nombres(resultado.getString("actor_nombres"))
                .apellidos(resultado.getString("actor_apellidos"))
                .correoInstitucional(resultado.getString("actor_correo_institucional"))
                .estadoCuenta(resultado.getString("actor_estado_cuenta"))
                .build();
        return new AuditoriaEntidad.Builder()
                .id(leerId(resultado, "id_auditoria"))
                .fechaHora(leerFecha(resultado, "fecha_hora"))
                .actor(actor)
                .accion(resultado.getString("accion"))
                .tipoObjeto(resultado.getString("tipo_objeto"))
                .idObjeto(leerId(resultado, "id_objeto"))
                .estadoAnterior(resultado.getString("estado_anterior"))
                .estadoNuevo(resultado.getString("estado_nuevo"))
                .motivo(resultado.getString("motivo"))
                .detalle(resultado.getString("detalle"))
                .build();
    }
}
