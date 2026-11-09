package co.edu.uco.kudos.dao.datos.entidad.sqlserver;

import co.edu.uco.kudos.dao.datos.entidad.ReporteDAO;
import co.edu.uco.kudos.dao.datos.entidad.SqlDAO;
import co.edu.uco.kudos.entidad.KudoEntidad;
import co.edu.uco.kudos.entidad.ModeracionEntidad;
import co.edu.uco.kudos.entidad.ReplicaEntidad;
import co.edu.uco.kudos.entidad.ReporteEntidad;
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

public class ReporteSqlServerDAO extends SqlDAO implements ReporteDAO {

    private static final String CONSULTA_BASE = """
            select t.id_reporte,
                   t.id_reportante,
                   t.id_kudo,
                   t.id_replica,
                   t.motivo,
                   t.descripcion,
                   t.fecha_reporte,
                   t.estado,
                   t.id_moderacion,
                   reportante.nombres as reportante_nombres,
                   reportante.apellidos as reportante_apellidos,
                   reportante.correo_institucional as reportante_correo_institucional,
                   reportante.estado_cuenta as reportante_estado_cuenta
            from reporte as t
            left join usuario as reportante on reportante.id_usuario = t.id_reportante
            """;
    private static final String ORDEN = " order by t.fecha_reporte desc";

    public ReporteSqlServerDAO(Connection conexion) {
        super(conexion);
    }

    @Override
    public void crear(ReporteEntidad entidad) {
        var sentenciaSql = """
                insert into reporte (id_reporte, id_reportante, id_kudo, id_replica, motivo, descripcion, fecha_reporte, estado, id_moderacion)
                values (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, entidad.getId());
            sentencia.setObject(2, entidad.getReportante().getId());
            asignarIdOpcional(sentencia, 3, entidad.getKudo().getId());
            asignarIdOpcional(sentencia, 4, entidad.getReplica().getId());
            sentencia.setString(5, entidad.getMotivo());
            asignarTextoOpcional(sentencia, 6, entidad.getDescripcion());
            sentencia.setObject(7, entidad.getFechaReporte());
            sentencia.setString(8, entidad.getEstado());
            asignarIdOpcional(sentencia, 9, entidad.getModeracion().getId());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ReporteSqlServerDAO.USUARIO_ERROR_CREANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ReporteSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CREANDO, excepcion.getMessage(), excepcion);
        }
    }

    @Override
    public ReporteEntidad consultarPorId(UUID id) {
        var sentenciaSql = CONSULTA_BASE + " where t.id_reporte = ?";
        var encontrado = new ReporteEntidad.Builder().build();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, id);
            var resultado = sentencia.executeQuery();
            if (resultado.next()) {
                encontrado = armarReporte(resultado);
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ReporteSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ReporteSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        }
        return encontrado;
    }

    @Override
    public List<ReporteEntidad> consultarPorFiltro(ReporteEntidad filtro) {
        var condiciones = new ArrayList<String>();
        var parametros = new ArrayList<Object>();
        if (!UtilId.esValorDefecto(filtro.getId())) {
            condiciones.add("t.id_reporte = ?");
            parametros.add(filtro.getId());
        }
        if (!UtilId.esValorDefecto(filtro.getReportante().getId())) {
            condiciones.add("t.id_reportante = ?");
            parametros.add(filtro.getReportante().getId());
        }
        if (!UtilId.esValorDefecto(filtro.getKudo().getId())) {
            condiciones.add("t.id_kudo = ?");
            parametros.add(filtro.getKudo().getId());
        }
        if (!UtilId.esValorDefecto(filtro.getReplica().getId())) {
            condiciones.add("t.id_replica = ?");
            parametros.add(filtro.getReplica().getId());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtro.getMotivo())) {
            condiciones.add("t.motivo = ?");
            parametros.add(filtro.getMotivo());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtro.getEstado())) {
            condiciones.add("t.estado = ?");
            parametros.add(filtro.getEstado());
        }
        if (!UtilId.esValorDefecto(filtro.getModeracion().getId())) {
            condiciones.add("t.id_moderacion = ?");
            parametros.add(filtro.getModeracion().getId());
        }
        var sentenciaSql = CONSULTA_BASE + (condiciones.isEmpty() ? "" : " where " + String.join(" and ", condiciones)) + ORDEN;
        return consultar(sentenciaSql, parametros, CatalogoMensajes.ReporteSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_FILTRO, CatalogoMensajes.ReporteSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO);
    }

    @Override
    public List<ReporteEntidad> consultarTodos() {
        return consultar(CONSULTA_BASE + ORDEN, List.of(), CatalogoMensajes.ReporteSqlServerDAO.USUARIO_ERROR_CONSULTANDO_TODOS, CatalogoMensajes.ReporteSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS);
    }

    @Override
    public void actualizar(UUID id, ReporteEntidad entidad) {
        var sentenciaSql = """
                update reporte set id_reportante = ?, id_kudo = ?, id_replica = ?, motivo = ?, descripcion = ?, fecha_reporte = ?, estado = ?, id_moderacion = ?
                where id_reporte = ?
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, entidad.getReportante().getId());
            asignarIdOpcional(sentencia, 2, entidad.getKudo().getId());
            asignarIdOpcional(sentencia, 3, entidad.getReplica().getId());
            sentencia.setString(4, entidad.getMotivo());
            asignarTextoOpcional(sentencia, 5, entidad.getDescripcion());
            sentencia.setObject(6, entidad.getFechaReporte());
            sentencia.setString(7, entidad.getEstado());
            asignarIdOpcional(sentencia, 8, entidad.getModeracion().getId());
            sentencia.setObject(9, id);
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ReporteSqlServerDAO.USUARIO_ERROR_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ReporteSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        }
    }

    private List<ReporteEntidad> consultar(String sentenciaSql, List<Object> parametros, String mensajeError, String mensajeErrorNoControlado) {
        var encontrados = new ArrayList<ReporteEntidad>();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            asignarParametros(sentencia, parametros);
            var resultado = sentencia.executeQuery();
            while (resultado.next()) {
                encontrados.add(armarReporte(resultado));
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(mensajeError, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(mensajeErrorNoControlado, excepcion.getMessage(), excepcion);
        }
        return encontrados;
    }

    private ReporteEntidad armarReporte(ResultSet resultado) throws SQLException {
        var reportante = new UsuarioEntidad.Builder()
                .id(leerId(resultado, "id_reportante"))
                .nombres(resultado.getString("reportante_nombres"))
                .apellidos(resultado.getString("reportante_apellidos"))
                .correoInstitucional(resultado.getString("reportante_correo_institucional"))
                .estadoCuenta(resultado.getString("reportante_estado_cuenta"))
                .build();
        var kudo = new KudoEntidad.Builder()
                .id(leerId(resultado, "id_kudo"))
                .build();
        var replica = new ReplicaEntidad.Builder()
                .id(leerId(resultado, "id_replica"))
                .build();
        var moderacion = new ModeracionEntidad.Builder()
                .id(leerId(resultado, "id_moderacion"))
                .build();
        return new ReporteEntidad.Builder()
                .id(leerId(resultado, "id_reporte"))
                .reportante(reportante)
                .kudo(kudo)
                .replica(replica)
                .motivo(resultado.getString("motivo"))
                .descripcion(resultado.getString("descripcion"))
                .fechaReporte(leerFecha(resultado, "fecha_reporte"))
                .estado(resultado.getString("estado"))
                .moderacion(moderacion)
                .build();
    }
}
