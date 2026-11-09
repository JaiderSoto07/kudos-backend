package co.edu.uco.kudos.dao.datos.entidad;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilSql;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public abstract class SqlDAO {

    private final Connection conexion;

    protected SqlDAO(Connection conexion) {
        UtilSql.asegurarConexionAbierta(conexion);
        this.conexion = conexion;
    }

    protected Connection getConexion() {
        return conexion;
    }

    protected void agregarCondicionId(List<String> condiciones, List<Object> parametros, String columna, UUID valor) {
        if (!UtilId.esValorDefecto(valor)) {
            condiciones.add(columna + " = ?");
            parametros.add(valor);
        }
    }

    protected void asignarParametros(PreparedStatement sentencia, List<Object> parametros) throws SQLException {
        for (var indice = 0; indice < parametros.size(); indice++) {
            sentencia.setObject(indice + 1, parametros.get(indice));
        }
    }

    protected void asignarIdOpcional(PreparedStatement sentencia, int posicion, UUID id) throws SQLException {
        if (UtilId.esValorDefecto(id)) {
            sentencia.setNull(posicion, Types.VARCHAR);
        } else {
            sentencia.setObject(posicion, id);
        }
    }

    protected void asignarTextoOpcional(PreparedStatement sentencia, int posicion, String texto) throws SQLException {
        if (UtilTexto.getUtilTexto().esVacia(texto)) {
            sentencia.setNull(posicion, Types.VARCHAR);
        } else {
            sentencia.setString(posicion, texto);
        }
    }

    protected void asignarFechaOpcional(PreparedStatement sentencia, int posicion, LocalDateTime fecha) throws SQLException {
        if (UtilFecha.esValorDefecto(fecha)) {
            sentencia.setNull(posicion, Types.VARCHAR);
        } else {
            sentencia.setObject(posicion, fecha);
        }
    }

    protected UUID leerId(ResultSet resultado, String columna) throws SQLException {
        var valor = resultado.getString(columna);
        return valor == null ? UtilId.VALOR_DEFECTO : UUID.fromString(valor);
    }

    protected LocalDateTime leerFecha(ResultSet resultado, String columna) throws SQLException {
        return resultado.getObject(columna, LocalDateTime.class);
    }
}
