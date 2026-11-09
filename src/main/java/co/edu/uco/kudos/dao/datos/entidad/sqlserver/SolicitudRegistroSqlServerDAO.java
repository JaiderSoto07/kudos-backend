package co.edu.uco.kudos.dao.datos.entidad.sqlserver;

import co.edu.uco.kudos.dao.datos.entidad.SolicitudRegistroDAO;
import co.edu.uco.kudos.dao.datos.entidad.SqlDAO;
import co.edu.uco.kudos.entidad.FacultadEntidad;
import co.edu.uco.kudos.entidad.ProgramaAcademicoEntidad;
import co.edu.uco.kudos.entidad.SolicitudRegistroEntidad;
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

public class SolicitudRegistroSqlServerDAO extends SqlDAO implements SolicitudRegistroDAO {

    private static final String CONSULTA_BASE = """
            select t.id_solicitud_registro,
                   t.id_solicitante,
                   t.rol_solicitado,
                   t.id_facultad,
                   t.id_programa,
                   t.estado,
                   t.fecha_solicitud,
                   t.id_decidida_por,
                   t.fecha_decision,
                   t.motivo_rechazo,
                   solicitante.nombres as solicitante_nombres,
                   solicitante.apellidos as solicitante_apellidos,
                   solicitante.correo_institucional as solicitante_correo_institucional,
                   solicitante.estado_cuenta as solicitante_estado_cuenta,
                   facultad.nombre as facultad_nombre,
                   facultad.estado as facultad_estado,
                   programa.nombre as programa_nombre,
                   programa.activo as programa_activo,
                   programa.id_facultad as programa_id_facultad,
                   decididapor.nombres as decididapor_nombres,
                   decididapor.apellidos as decididapor_apellidos,
                   decididapor.correo_institucional as decididapor_correo_institucional,
                   decididapor.estado_cuenta as decididapor_estado_cuenta
            from solicitud_registro as t
            left join usuario as solicitante on solicitante.id_usuario = t.id_solicitante
            left join facultad as facultad on facultad.id_facultad = t.id_facultad
            left join programa_academico as programa on programa.id_programa_academico = t.id_programa
            left join usuario as decididapor on decididapor.id_usuario = t.id_decidida_por
            """;
    private static final String ORDEN = " order by t.fecha_solicitud desc";

    public SolicitudRegistroSqlServerDAO(Connection conexion) {
        super(conexion);
    }

    @Override
    public void crear(SolicitudRegistroEntidad entidad) {
        var sentenciaSql = """
                insert into solicitud_registro (id_solicitud_registro, id_solicitante, rol_solicitado, id_facultad, id_programa, estado, fecha_solicitud, id_decidida_por, fecha_decision, motivo_rechazo)
                values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, entidad.getId());
            sentencia.setObject(2, entidad.getSolicitante().getId());
            sentencia.setString(3, entidad.getRolSolicitado());
            asignarIdOpcional(sentencia, 4, entidad.getFacultad().getId());
            asignarIdOpcional(sentencia, 5, entidad.getPrograma().getId());
            sentencia.setString(6, entidad.getEstado());
            sentencia.setObject(7, entidad.getFechaSolicitud());
            asignarIdOpcional(sentencia, 8, entidad.getDecididaPor().getId());
            asignarFechaOpcional(sentencia, 9, entidad.getFechaDecision());
            asignarTextoOpcional(sentencia, 10, entidad.getMotivoRechazo());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.SolicitudRegistroSqlServerDAO.USUARIO_ERROR_CREANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.SolicitudRegistroSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CREANDO, excepcion.getMessage(), excepcion);
        }
    }

    @Override
    public SolicitudRegistroEntidad consultarPorId(UUID id) {
        var sentenciaSql = CONSULTA_BASE + " where t.id_solicitud_registro = ?";
        var encontrado = new SolicitudRegistroEntidad.Builder().build();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, id);
            var resultado = sentencia.executeQuery();
            if (resultado.next()) {
                encontrado = armarSolicitudRegistro(resultado);
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.SolicitudRegistroSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.SolicitudRegistroSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        }
        return encontrado;
    }

    @Override
    public List<SolicitudRegistroEntidad> consultarPorFiltro(SolicitudRegistroEntidad filtro) {
        var condiciones = new ArrayList<String>();
        var parametros = new ArrayList<Object>();
        if (!UtilId.esValorDefecto(filtro.getId())) {
            condiciones.add("t.id_solicitud_registro = ?");
            parametros.add(filtro.getId());
        }
        if (!UtilId.esValorDefecto(filtro.getSolicitante().getId())) {
            condiciones.add("t.id_solicitante = ?");
            parametros.add(filtro.getSolicitante().getId());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtro.getRolSolicitado())) {
            condiciones.add("t.rol_solicitado = ?");
            parametros.add(filtro.getRolSolicitado());
        }
        if (!UtilId.esValorDefecto(filtro.getFacultad().getId())) {
            condiciones.add("t.id_facultad = ?");
            parametros.add(filtro.getFacultad().getId());
        }
        if (!UtilId.esValorDefecto(filtro.getPrograma().getId())) {
            condiciones.add("t.id_programa = ?");
            parametros.add(filtro.getPrograma().getId());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtro.getEstado())) {
            condiciones.add("t.estado = ?");
            parametros.add(filtro.getEstado());
        }
        if (!UtilId.esValorDefecto(filtro.getDecididaPor().getId())) {
            condiciones.add("t.id_decidida_por = ?");
            parametros.add(filtro.getDecididaPor().getId());
        }
        var sentenciaSql = CONSULTA_BASE + (condiciones.isEmpty() ? "" : " where " + String.join(" and ", condiciones)) + ORDEN;
        return consultar(sentenciaSql, parametros, CatalogoMensajes.SolicitudRegistroSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_FILTRO, CatalogoMensajes.SolicitudRegistroSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO);
    }

    @Override
    public List<SolicitudRegistroEntidad> consultarTodos() {
        return consultar(CONSULTA_BASE + ORDEN, List.of(), CatalogoMensajes.SolicitudRegistroSqlServerDAO.USUARIO_ERROR_CONSULTANDO_TODOS, CatalogoMensajes.SolicitudRegistroSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS);
    }

    @Override
    public void actualizar(UUID id, SolicitudRegistroEntidad entidad) {
        var sentenciaSql = """
                update solicitud_registro set id_solicitante = ?, rol_solicitado = ?, id_facultad = ?, id_programa = ?, estado = ?, fecha_solicitud = ?, id_decidida_por = ?, fecha_decision = ?, motivo_rechazo = ?
                where id_solicitud_registro = ?
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, entidad.getSolicitante().getId());
            sentencia.setString(2, entidad.getRolSolicitado());
            asignarIdOpcional(sentencia, 3, entidad.getFacultad().getId());
            asignarIdOpcional(sentencia, 4, entidad.getPrograma().getId());
            sentencia.setString(5, entidad.getEstado());
            sentencia.setObject(6, entidad.getFechaSolicitud());
            asignarIdOpcional(sentencia, 7, entidad.getDecididaPor().getId());
            asignarFechaOpcional(sentencia, 8, entidad.getFechaDecision());
            asignarTextoOpcional(sentencia, 9, entidad.getMotivoRechazo());
            sentencia.setObject(10, id);
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.SolicitudRegistroSqlServerDAO.USUARIO_ERROR_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.SolicitudRegistroSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        }
    }

    private List<SolicitudRegistroEntidad> consultar(String sentenciaSql, List<Object> parametros, String mensajeError, String mensajeErrorNoControlado) {
        var encontrados = new ArrayList<SolicitudRegistroEntidad>();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            asignarParametros(sentencia, parametros);
            var resultado = sentencia.executeQuery();
            while (resultado.next()) {
                encontrados.add(armarSolicitudRegistro(resultado));
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(mensajeError, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(mensajeErrorNoControlado, excepcion.getMessage(), excepcion);
        }
        return encontrados;
    }

    private SolicitudRegistroEntidad armarSolicitudRegistro(ResultSet resultado) throws SQLException {
        var solicitante = new UsuarioEntidad.Builder()
                .id(leerId(resultado, "id_solicitante"))
                .nombres(resultado.getString("solicitante_nombres"))
                .apellidos(resultado.getString("solicitante_apellidos"))
                .correoInstitucional(resultado.getString("solicitante_correo_institucional"))
                .estadoCuenta(resultado.getString("solicitante_estado_cuenta"))
                .build();
        var facultad = new FacultadEntidad.Builder()
                .id(leerId(resultado, "id_facultad"))
                .nombre(resultado.getString("facultad_nombre"))
                .estado(resultado.getString("facultad_estado"))
                .build();
        var programa = new ProgramaAcademicoEntidad.Builder()
                .id(leerId(resultado, "id_programa"))
                .nombre(resultado.getString("programa_nombre"))
                .activo(resultado.getBoolean("programa_activo"))
                .facultad(new FacultadEntidad.Builder().id(leerId(resultado, "programa_id_facultad")).build())
                .build();
        var decididaPor = new UsuarioEntidad.Builder()
                .id(leerId(resultado, "id_decidida_por"))
                .nombres(resultado.getString("decididapor_nombres"))
                .apellidos(resultado.getString("decididapor_apellidos"))
                .correoInstitucional(resultado.getString("decididapor_correo_institucional"))
                .estadoCuenta(resultado.getString("decididapor_estado_cuenta"))
                .build();
        return new SolicitudRegistroEntidad.Builder()
                .id(leerId(resultado, "id_solicitud_registro"))
                .solicitante(solicitante)
                .rolSolicitado(resultado.getString("rol_solicitado"))
                .facultad(facultad)
                .programa(programa)
                .estado(resultado.getString("estado"))
                .fechaSolicitud(leerFecha(resultado, "fecha_solicitud"))
                .decididaPor(decididaPor)
                .fechaDecision(leerFecha(resultado, "fecha_decision"))
                .motivoRechazo(resultado.getString("motivo_rechazo"))
                .build();
    }
}
