package co.edu.uco.kudos.dao.datos.entidad.sqlserver;

import co.edu.uco.kudos.dao.datos.entidad.SqlDAO;
import co.edu.uco.kudos.dao.datos.entidad.VinculacionProgramaDAO;
import co.edu.uco.kudos.entidad.FacultadEntidad;
import co.edu.uco.kudos.entidad.ProgramaAcademicoEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.entidad.VinculacionProgramaEntidad;
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

public class VinculacionProgramaSqlServerDAO extends SqlDAO implements VinculacionProgramaDAO {

    private static final String CONSULTA_BASE = """
            select t.id_vinculacion_programa,
                   t.id_usuario,
                   t.id_programa,
                   t.tipo_vinculo,
                   t.fecha_inicio,
                   t.fecha_fin,
                   t.motivo_desvinculacion,
                   usuario.nombres as usuario_nombres,
                   usuario.apellidos as usuario_apellidos,
                   usuario.correo_institucional as usuario_correo_institucional,
                   usuario.estado_cuenta as usuario_estado_cuenta,
                   programa.nombre as programa_nombre,
                   programa.activo as programa_activo,
                   programa.id_facultad as programa_id_facultad
            from vinculacion_programa as t
            left join usuario as usuario on usuario.id_usuario = t.id_usuario
            left join programa_academico as programa on programa.id_programa_academico = t.id_programa
            """;
    private static final String ORDEN = " order by t.fecha_inicio desc";

    public VinculacionProgramaSqlServerDAO(Connection conexion) {
        super(conexion);
    }

    @Override
    public void crear(VinculacionProgramaEntidad entidad) {
        var sentenciaSql = """
                insert into vinculacion_programa (id_vinculacion_programa, id_usuario, id_programa, tipo_vinculo, fecha_inicio, fecha_fin, motivo_desvinculacion)
                values (?, ?, ?, ?, ?, ?, ?)
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, entidad.getId());
            sentencia.setObject(2, entidad.getUsuario().getId());
            sentencia.setObject(3, entidad.getPrograma().getId());
            sentencia.setString(4, entidad.getTipoVinculo());
            sentencia.setObject(5, entidad.getFechaInicio());
            asignarFechaOpcional(sentencia, 6, entidad.getFechaFin());
            asignarTextoOpcional(sentencia, 7, entidad.getMotivoDesvinculacion());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.VinculacionProgramaSqlServerDAO.USUARIO_ERROR_CREANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.VinculacionProgramaSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CREANDO, excepcion.getMessage(), excepcion);
        }
    }

    @Override
    public VinculacionProgramaEntidad consultarPorId(UUID id) {
        var sentenciaSql = CONSULTA_BASE + " where t.id_vinculacion_programa = ?";
        var encontrado = new VinculacionProgramaEntidad.Builder().build();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, id);
            var resultado = sentencia.executeQuery();
            if (resultado.next()) {
                encontrado = armarVinculacionPrograma(resultado);
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.VinculacionProgramaSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.VinculacionProgramaSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        }
        return encontrado;
    }

    @Override
    public List<VinculacionProgramaEntidad> consultarPorFiltro(VinculacionProgramaEntidad filtro) {
        var condiciones = new ArrayList<String>();
        var parametros = new ArrayList<Object>();
        if (!UtilId.esValorDefecto(filtro.getId())) {
            condiciones.add("t.id_vinculacion_programa = ?");
            parametros.add(filtro.getId());
        }
        if (!UtilId.esValorDefecto(filtro.getUsuario().getId())) {
            condiciones.add("t.id_usuario = ?");
            parametros.add(filtro.getUsuario().getId());
        }
        if (!UtilId.esValorDefecto(filtro.getPrograma().getId())) {
            condiciones.add("t.id_programa = ?");
            parametros.add(filtro.getPrograma().getId());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtro.getTipoVinculo())) {
            condiciones.add("t.tipo_vinculo = ?");
            parametros.add(filtro.getTipoVinculo());
        }
        var sentenciaSql = CONSULTA_BASE + (condiciones.isEmpty() ? "" : " where " + String.join(" and ", condiciones)) + ORDEN;
        return consultar(sentenciaSql, parametros, CatalogoMensajes.VinculacionProgramaSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_FILTRO, CatalogoMensajes.VinculacionProgramaSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO);
    }

    @Override
    public List<VinculacionProgramaEntidad> consultarTodos() {
        return consultar(CONSULTA_BASE + ORDEN, List.of(), CatalogoMensajes.VinculacionProgramaSqlServerDAO.USUARIO_ERROR_CONSULTANDO_TODOS, CatalogoMensajes.VinculacionProgramaSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS);
    }

    @Override
    public void actualizar(UUID id, VinculacionProgramaEntidad entidad) {
        var sentenciaSql = """
                update vinculacion_programa set id_usuario = ?, id_programa = ?, tipo_vinculo = ?, fecha_inicio = ?, fecha_fin = ?, motivo_desvinculacion = ?
                where id_vinculacion_programa = ?
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, entidad.getUsuario().getId());
            sentencia.setObject(2, entidad.getPrograma().getId());
            sentencia.setString(3, entidad.getTipoVinculo());
            sentencia.setObject(4, entidad.getFechaInicio());
            asignarFechaOpcional(sentencia, 5, entidad.getFechaFin());
            asignarTextoOpcional(sentencia, 6, entidad.getMotivoDesvinculacion());
            sentencia.setObject(7, id);
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.VinculacionProgramaSqlServerDAO.USUARIO_ERROR_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.VinculacionProgramaSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        }
    }

    private List<VinculacionProgramaEntidad> consultar(String sentenciaSql, List<Object> parametros, String mensajeError, String mensajeErrorNoControlado) {
        var encontrados = new ArrayList<VinculacionProgramaEntidad>();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            asignarParametros(sentencia, parametros);
            var resultado = sentencia.executeQuery();
            while (resultado.next()) {
                encontrados.add(armarVinculacionPrograma(resultado));
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(mensajeError, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(mensajeErrorNoControlado, excepcion.getMessage(), excepcion);
        }
        return encontrados;
    }

    private VinculacionProgramaEntidad armarVinculacionPrograma(ResultSet resultado) throws SQLException {
        var usuario = new UsuarioEntidad.Builder()
                .id(leerId(resultado, "id_usuario"))
                .nombres(resultado.getString("usuario_nombres"))
                .apellidos(resultado.getString("usuario_apellidos"))
                .correoInstitucional(resultado.getString("usuario_correo_institucional"))
                .estadoCuenta(resultado.getString("usuario_estado_cuenta"))
                .build();
        var programa = new ProgramaAcademicoEntidad.Builder()
                .id(leerId(resultado, "id_programa"))
                .nombre(resultado.getString("programa_nombre"))
                .activo(resultado.getBoolean("programa_activo"))
                .facultad(new FacultadEntidad.Builder().id(leerId(resultado, "programa_id_facultad")).build())
                .build();
        return new VinculacionProgramaEntidad.Builder()
                .id(leerId(resultado, "id_vinculacion_programa"))
                .usuario(usuario)
                .programa(programa)
                .tipoVinculo(resultado.getString("tipo_vinculo"))
                .fechaInicio(leerFecha(resultado, "fecha_inicio"))
                .fechaFin(leerFecha(resultado, "fecha_fin"))
                .motivoDesvinculacion(resultado.getString("motivo_desvinculacion"))
                .build();
    }
}
