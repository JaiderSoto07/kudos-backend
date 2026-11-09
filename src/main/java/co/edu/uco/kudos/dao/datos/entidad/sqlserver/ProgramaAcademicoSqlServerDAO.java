package co.edu.uco.kudos.dao.datos.entidad.sqlserver;

import co.edu.uco.kudos.dao.datos.entidad.ProgramaAcademicoDAO;
import co.edu.uco.kudos.dao.datos.entidad.SqlDAO;
import co.edu.uco.kudos.entidad.FacultadEntidad;
import co.edu.uco.kudos.entidad.ProgramaAcademicoEntidad;
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

public class ProgramaAcademicoSqlServerDAO extends SqlDAO implements ProgramaAcademicoDAO {

    private static final String CONSULTA_BASE = """
            select t.id_programa_academico,
                   t.nombre,
                   t.id_facultad,
                   t.activo,
                   t.fecha_creacion,
                   facultad.nombre as facultad_nombre,
                   facultad.estado as facultad_estado
            from programa_academico as t
            left join facultad as facultad on facultad.id_facultad = t.id_facultad
            """;
    private static final String ORDEN = " order by t.nombre";

    public ProgramaAcademicoSqlServerDAO(Connection conexion) {
        super(conexion);
    }

    @Override
    public void crear(ProgramaAcademicoEntidad entidad) {
        var sentenciaSql = """
                insert into programa_academico (id_programa_academico, nombre, id_facultad, activo, fecha_creacion)
                values (?, ?, ?, ?, ?)
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, entidad.getId());
            sentencia.setString(2, entidad.getNombre());
            sentencia.setObject(3, entidad.getFacultad().getId());
            sentencia.setBoolean(4, entidad.isActivo());
            sentencia.setObject(5, entidad.getFechaCreacion());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ProgramaAcademicoSqlServerDAO.USUARIO_ERROR_CREANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ProgramaAcademicoSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CREANDO, excepcion.getMessage(), excepcion);
        }
    }

    @Override
    public ProgramaAcademicoEntidad consultarPorId(UUID id) {
        var sentenciaSql = CONSULTA_BASE + " where t.id_programa_academico = ?";
        var encontrado = new ProgramaAcademicoEntidad.Builder().build();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, id);
            var resultado = sentencia.executeQuery();
            if (resultado.next()) {
                encontrado = armarProgramaAcademico(resultado);
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ProgramaAcademicoSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ProgramaAcademicoSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        }
        return encontrado;
    }

    @Override
    public List<ProgramaAcademicoEntidad> consultarPorFiltro(ProgramaAcademicoEntidad filtro) {
        var condiciones = new ArrayList<String>();
        var parametros = new ArrayList<Object>();
        if (!UtilId.esValorDefecto(filtro.getId())) {
            condiciones.add("t.id_programa_academico = ?");
            parametros.add(filtro.getId());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtro.getNombre())) {
            condiciones.add("t.nombre = ?");
            parametros.add(filtro.getNombre());
        }
        if (!UtilId.esValorDefecto(filtro.getFacultad().getId())) {
            condiciones.add("t.id_facultad = ?");
            parametros.add(filtro.getFacultad().getId());
        }
        var sentenciaSql = CONSULTA_BASE + (condiciones.isEmpty() ? "" : " where " + String.join(" and ", condiciones)) + ORDEN;
        return consultar(sentenciaSql, parametros, CatalogoMensajes.ProgramaAcademicoSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_FILTRO, CatalogoMensajes.ProgramaAcademicoSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO);
    }

    @Override
    public List<ProgramaAcademicoEntidad> consultarTodos() {
        return consultar(CONSULTA_BASE + ORDEN, List.of(), CatalogoMensajes.ProgramaAcademicoSqlServerDAO.USUARIO_ERROR_CONSULTANDO_TODOS, CatalogoMensajes.ProgramaAcademicoSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS);
    }

    @Override
    public List<ProgramaAcademicoEntidad> consultarActivos() {
        return consultar(CONSULTA_BASE + " where t.activo = 1" + ORDEN, List.of(), CatalogoMensajes.ProgramaAcademicoSqlServerDAO.USUARIO_ERROR_CONSULTANDO_ACTIVOS, CatalogoMensajes.ProgramaAcademicoSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_ACTIVOS);
    }

    @Override
    public void actualizar(UUID id, ProgramaAcademicoEntidad entidad) {
        var sentenciaSql = """
                update programa_academico set nombre = ?, id_facultad = ?, activo = ?, fecha_creacion = ?
                where id_programa_academico = ?
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setString(1, entidad.getNombre());
            sentencia.setObject(2, entidad.getFacultad().getId());
            sentencia.setBoolean(3, entidad.isActivo());
            sentencia.setObject(4, entidad.getFechaCreacion());
            sentencia.setObject(5, id);
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ProgramaAcademicoSqlServerDAO.USUARIO_ERROR_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.ProgramaAcademicoSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        }
    }

    private List<ProgramaAcademicoEntidad> consultar(String sentenciaSql, List<Object> parametros, String mensajeError, String mensajeErrorNoControlado) {
        var encontrados = new ArrayList<ProgramaAcademicoEntidad>();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            asignarParametros(sentencia, parametros);
            var resultado = sentencia.executeQuery();
            while (resultado.next()) {
                encontrados.add(armarProgramaAcademico(resultado));
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(mensajeError, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(mensajeErrorNoControlado, excepcion.getMessage(), excepcion);
        }
        return encontrados;
    }

    private ProgramaAcademicoEntidad armarProgramaAcademico(ResultSet resultado) throws SQLException {
        var facultad = new FacultadEntidad.Builder()
                .id(leerId(resultado, "id_facultad"))
                .nombre(resultado.getString("facultad_nombre"))
                .estado(resultado.getString("facultad_estado"))
                .build();
        return new ProgramaAcademicoEntidad.Builder()
                .id(leerId(resultado, "id_programa_academico"))
                .nombre(resultado.getString("nombre"))
                .facultad(facultad)
                .activo(resultado.getBoolean("activo"))
                .fechaCreacion(leerFecha(resultado, "fecha_creacion"))
                .build();
    }
}
