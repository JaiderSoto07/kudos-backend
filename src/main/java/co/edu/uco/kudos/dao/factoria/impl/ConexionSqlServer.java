package co.edu.uco.kudos.dao.factoria.impl;

import co.edu.uco.kudos.dao.factoria.DAOFactory;

public class ConexionSqlServer {

    public static void main(String[] args) {
        var factoria = DAOFactory.obtenerFactoria();
        var usuarios = factoria.obtenerUsuarioDAO().consultarTodos().size();
        System.out.println("¡Conexión exitosa! Usuarios registrados: " + usuarios);
        factoria.cerrarConexion();
    }
}
