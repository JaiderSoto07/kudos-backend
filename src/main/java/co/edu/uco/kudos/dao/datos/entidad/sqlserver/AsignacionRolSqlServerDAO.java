package co.edu.uco.kudos.dao.datos.entidad.sqlserver;

import co.edu.uco.kudos.dao.datos.entidad.AsignacionRolDAO;
import co.edu.uco.kudos.dao.datos.entidad.SqlDAO;
import co.edu.uco.kudos.entidad.AsignacionRolEntidad;
import co.edu.uco.kudos.entidad.FacultadEntidad;
import co.edu.uco.kudos.entidad.ProgramaAcademicoEntidad;
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

public class AsignacionRolSqlServerDAO extends SqlDAO implements AsignacionRolDAO {

    private static final String CONSULTA_BASE = """
            select t.id_asignacion_rol,
                   t.id_usuario,
                   t.rol,
                   t.id_facultad,
                   t.id_programa,
                   t.fecha_inicio,
                   t.fecha_fin,
                   t.motivo_fin,
                   usuario.nombres as usuario_nombres,
                   usuario.apellidos as usuario_apellidos,
                   usuario.correo_institucional as usuario_correo_institucional,
                   usuario.estado_cuenta as usuario_estado_cuenta,
                   facultad.nombre as facultad_nombre,
                   facultad.estado as facultad_estado,
                   programa.nombre as programa_nombre,
                   programa.activo as programa_activo,
                   programa.id_facultad as programa_id_facultad
            from asignacion_rol as t
            left join usuario as usuario on usuario.id_usuario = t.id_usuario
            left join facultad as facultad on facultad.id_facultad = t.id_facultad
            left join programa_academico as programa on programa.id_programa_academico = t.id_programa
            """;
    private static final String ORDEN = " order by t.fecha_inicio desc";

    public AsignacionRolSqlServerDAO(Connection conexion) {
        super(conexion);
    }

    @Override
    public void crear(AsignacionRolEntidad entidad) {
        var sentenciaSql = """
                insert into asignacion_rol (id_asignacion_rol, id_usuario, rol, id_facultad, id_programa, fecha_inicio, fecha_fin, motivo_fin)
                values (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, entidad.getId());
            sentencia.setObject(2, entidad.getUsuario().getId());
            sentencia.setString(3, entidad.getRol());
            asignarIdOpcional(sentencia, 4, entidad.getFacultad().getId());
            asignarIdOpcional(sentencia, 5, entidad.getPrograma().getId());
            sentencia.setObject(6, entidad.getFechaInicio());
            asignarFechaOpcional(sentencia, 7, entidad.getFechaFin());
            asignarTextoOpcional(sentencia, 8, entidad.getMotivoFin());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.AsignacionRolSqlServerDAO.USUARIO_ERROR_CREANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.AsignacionRolSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CREANDO, excepcion.getMessage(), excepcion);
        }
    }

    @Override
    public AsignacionRolEntidad consultarPorId(UUID id) {
        var sentenciaSql = CONSULTA_BASE + " where t.id_asignacion_rol = ?";
        var encontrado = new AsignacionRolEntidad.Builder().build();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, id);
            var resultado = sentencia.executeQuery();
            if (resultado.next()) {
                encontrado = armarAsignacionRol(resultado);
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.AsignacionRolSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.AsignacionRolSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        }
        return encontrado;
    }

    @Override
    public List<AsignacionRolEntidad> consultarPorFiltro(AsignacionRolEntidad filtro) {
        var condiciones = new ArrayList<String>();
        var parametros = new ArrayList<Object>();
        if (!UtilId.esValorDefecto(filtro.getId())) {
            condiciones.add("t.id_asignacion_rol = ?");
            parametros.add(filtro.getId());
        }
        if (!UtilId.esValorDefecto(filtro.getUsuario().getId())) {
            condiciones.add("t.id_usuario = ?");
            parametros.add(filtro.getUsuario().getId());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtro.getRol())) {
            condiciones.add("t.rol = ?");
            parametros.add(filtro.getRol());
        }
        if (!UtilId.esValorDefecto(filtro.getFacultad().getId())) {
            condiciones.add("t.id_facultad = ?");
            parametros.add(filtro.getFacultad().getId());
        }
        if (!UtilId.esValorDefecto(filtro.getPrograma().getId())) {
            condiciones.add("t.id_programa = ?");
            parametros.add(filtro.getPrograma().getId());
        }
        var sentenciaSql = CONSULTA_BASE + (condiciones.isEmpty() ? "" : " where " + String.join(" and ", condiciones)) + ORDEN;
        return consultar(sentenciaSql, parametros, CatalogoMensajes.AsignacionRolSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_FILTRO, CatalogoMensajes.AsignacionRolSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO);
    }

    @Override
    public List<AsignacionRolEntidad> consultarTodos() {
        return consultar(CONSULTA_BASE + ORDEN, List.of(), CatalogoMensajes.AsignacionRolSqlServerDAO.USUARIO_ERROR_CONSULTANDO_TODOS, CatalogoMensajes.AsignacionRolSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS);
    }

    @Override
    public void actualizar(UUID id, AsignacionRolEntidad entidad) {
        var sentenciaSql = """
                update asignacion_rol set id_usuario = ?, rol = ?, id_facultad = ?, id_programa = ?, fecha_inicio = ?, fecha_fin = ?, motivo_fin = ?
                where id_asignacion_rol = ?
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, entidad.getUsuario().getId());
            sentencia.setString(2, entidad.getRol());
            asignarIdOpcional(sentencia, 3, entidad.getFacultad().getId());
            asignarIdOpcional(sentencia, 4, entidad.getPrograma().getId());
            sentencia.setObject(5, entidad.getFechaInicio());
            asignarFechaOpcional(sentencia, 6, entidad.getFechaFin());
            asignarTextoOpcional(sentencia, 7, entidad.getMotivoFin());
            sentencia.setObject(8, id);
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.AsignacionRolSqlServerDAO.USUARIO_ERROR_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.AsignacionRolSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        }
    }

    private List<AsignacionRolEntidad> consultar(String sentenciaSql, List<Object> parametros, String mensajeError, String mensajeErrorNoControlado) {
        var encontrados = new ArrayList<AsignacionRolEntidad>();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            asignarParametros(sentencia, parametros);
            var resultado = sentencia.executeQuery();
            while (resultado.next()) {
                encontrados.add(armarAsignacionRol(resultado));
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(mensajeError, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(mensajeErrorNoControlado, excepcion.getMessage(), excepcion);
        }
        return encontrados;
    }

    private AsignacionRolEntidad armarAsignacionRol(ResultSet resultado) throws SQLException {
        var usuario = new UsuarioEntidad.Builder()
                .id(leerId(resultado, "id_usuario"))
                .nombres(resultado.getString("usuario_nombres"))
                .apellidos(resultado.getString("usuario_apellidos"))
                .correoInstitucional(resultado.getString("usuario_correo_institucional"))
                .estadoCuenta(resultado.getString("usuario_estado_cuenta"))
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
        return new AsignacionRolEntidad.Builder()
                .id(leerId(resultado, "id_asignacion_rol"))
                .usuario(usuario)
                .rol(resultado.getString("rol"))
                .facultad(facultad)
                .programa(programa)
                .fechaInicio(leerFecha(resultado, "fecha_inicio"))
                .fechaFin(leerFecha(resultado, "fecha_fin"))
                .motivoFin(resultado.getString("motivo_fin"))
                .build();
    }
}
