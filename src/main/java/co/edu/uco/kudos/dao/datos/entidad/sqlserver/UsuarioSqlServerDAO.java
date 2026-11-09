package co.edu.uco.kudos.dao.datos.entidad.sqlserver;

import co.edu.uco.kudos.dao.datos.entidad.SqlDAO;
import co.edu.uco.kudos.dao.datos.entidad.UsuarioDAO;
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

public class UsuarioSqlServerDAO extends SqlDAO implements UsuarioDAO {

    private static final String CONSULTA_BASE = """
            select t.id_usuario,
                   t.nombres,
                   t.apellidos,
                   t.correo_institucional,
                   t.correo_verificado,
                   t.numero_celular,
                   t.celular_verificado,
                   t.contrasena,
                   t.aprobada,
                   t.estado_cuenta,
                   t.fecha_registro
            from usuario as t
            """;
    private static final String ORDEN = " order by t.apellidos, t.nombres";

    public UsuarioSqlServerDAO(Connection conexion) {
        super(conexion);
    }

    @Override
    public void crear(UsuarioEntidad entidad) {
        var sentenciaSql = """
                insert into usuario (id_usuario, nombres, apellidos, correo_institucional, correo_verificado, numero_celular, celular_verificado, contrasena, aprobada, estado_cuenta, fecha_registro)
                values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, entidad.getId());
            sentencia.setString(2, entidad.getNombres());
            sentencia.setString(3, entidad.getApellidos());
            sentencia.setString(4, entidad.getCorreoInstitucional());
            sentencia.setBoolean(5, entidad.isCorreoVerificado());
            sentencia.setString(6, entidad.getNumeroCelular());
            sentencia.setBoolean(7, entidad.isCelularVerificado());
            sentencia.setString(8, entidad.getContrasena());
            sentencia.setBoolean(9, entidad.isAprobada());
            sentencia.setString(10, entidad.getEstadoCuenta());
            sentencia.setObject(11, entidad.getFechaRegistro());
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.UsuarioSqlServerDAO.USUARIO_ERROR_CREANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.UsuarioSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CREANDO, excepcion.getMessage(), excepcion);
        }
    }

    @Override
    public UsuarioEntidad consultarPorId(UUID id) {
        var sentenciaSql = CONSULTA_BASE + " where t.id_usuario = ?";
        var encontrado = new UsuarioEntidad.Builder().build();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setObject(1, id);
            var resultado = sentencia.executeQuery();
            if (resultado.next()) {
                encontrado = armarUsuario(resultado);
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.UsuarioSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.UsuarioSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_ID, excepcion.getMessage(), excepcion);
        }
        return encontrado;
    }

    @Override
    public List<UsuarioEntidad> consultarPorFiltro(UsuarioEntidad filtro) {
        var condiciones = new ArrayList<String>();
        var parametros = new ArrayList<Object>();
        if (!UtilId.esValorDefecto(filtro.getId())) {
            condiciones.add("t.id_usuario = ?");
            parametros.add(filtro.getId());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtro.getNombres())) {
            condiciones.add("t.nombres = ?");
            parametros.add(filtro.getNombres());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtro.getApellidos())) {
            condiciones.add("t.apellidos = ?");
            parametros.add(filtro.getApellidos());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtro.getCorreoInstitucional())) {
            condiciones.add("t.correo_institucional = ?");
            parametros.add(filtro.getCorreoInstitucional());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtro.getNumeroCelular())) {
            condiciones.add("t.numero_celular = ?");
            parametros.add(filtro.getNumeroCelular());
        }
        if (!UtilTexto.getUtilTexto().esVacia(filtro.getEstadoCuenta())) {
            condiciones.add("t.estado_cuenta = ?");
            parametros.add(filtro.getEstadoCuenta());
        }
        var sentenciaSql = CONSULTA_BASE + (condiciones.isEmpty() ? "" : " where " + String.join(" and ", condiciones)) + ORDEN;
        return consultar(sentenciaSql, parametros, CatalogoMensajes.UsuarioSqlServerDAO.USUARIO_ERROR_CONSULTANDO_POR_FILTRO, CatalogoMensajes.UsuarioSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_POR_FILTRO);
    }

    @Override
    public List<UsuarioEntidad> consultarTodos() {
        return consultar(CONSULTA_BASE + ORDEN, List.of(), CatalogoMensajes.UsuarioSqlServerDAO.USUARIO_ERROR_CONSULTANDO_TODOS, CatalogoMensajes.UsuarioSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_CONSULTANDO_TODOS);
    }

    @Override
    public void actualizar(UUID id, UsuarioEntidad entidad) {
        var sentenciaSql = """
                update usuario set nombres = ?, apellidos = ?, correo_institucional = ?, correo_verificado = ?, numero_celular = ?, celular_verificado = ?, contrasena = ?, aprobada = ?, estado_cuenta = ?, fecha_registro = ?
                where id_usuario = ?
                """;
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            sentencia.setString(1, entidad.getNombres());
            sentencia.setString(2, entidad.getApellidos());
            sentencia.setString(3, entidad.getCorreoInstitucional());
            sentencia.setBoolean(4, entidad.isCorreoVerificado());
            sentencia.setString(5, entidad.getNumeroCelular());
            sentencia.setBoolean(6, entidad.isCelularVerificado());
            sentencia.setString(7, entidad.getContrasena());
            sentencia.setBoolean(8, entidad.isAprobada());
            sentencia.setString(9, entidad.getEstadoCuenta());
            sentencia.setObject(10, entidad.getFechaRegistro());
            sentencia.setObject(11, id);
            sentencia.executeUpdate();
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.UsuarioSqlServerDAO.USUARIO_ERROR_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(CatalogoMensajes.UsuarioSqlServerDAO.USUARIO_ERROR_NO_CONTROLADO_ACTUALIZANDO, excepcion.getMessage(), excepcion);
        }
    }

    private List<UsuarioEntidad> consultar(String sentenciaSql, List<Object> parametros, String mensajeError, String mensajeErrorNoControlado) {
        var encontrados = new ArrayList<UsuarioEntidad>();
        try (var sentencia = getConexion().prepareStatement(sentenciaSql)) {
            asignarParametros(sentencia, parametros);
            var resultado = sentencia.executeQuery();
            while (resultado.next()) {
                encontrados.add(armarUsuario(resultado));
            }
        } catch (SQLException excepcion) {
            throw KudosDatosExcepcion.crear(mensajeError, excepcion.getMessage(), excepcion);
        } catch (Exception excepcion) {
            throw KudosDatosExcepcion.crear(mensajeErrorNoControlado, excepcion.getMessage(), excepcion);
        }
        return encontrados;
    }

    private UsuarioEntidad armarUsuario(ResultSet resultado) throws SQLException {
        return new UsuarioEntidad.Builder()
                .id(leerId(resultado, "id_usuario"))
                .nombres(resultado.getString("nombres"))
                .apellidos(resultado.getString("apellidos"))
                .correoInstitucional(resultado.getString("correo_institucional"))
                .correoVerificado(resultado.getBoolean("correo_verificado"))
                .numeroCelular(resultado.getString("numero_celular"))
                .celularVerificado(resultado.getBoolean("celular_verificado"))
                .contrasena(resultado.getString("contrasena"))
                .aprobada(resultado.getBoolean("aprobada"))
                .estadoCuenta(resultado.getString("estado_cuenta"))
                .fechaRegistro(leerFecha(resultado, "fecha_registro"))
                .build();
    }
}
