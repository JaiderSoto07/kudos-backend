package co.edu.uco.kudos.negocio.negocio;

import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dominio.UsuarioDominio;

import java.util.UUID;

public interface AutenticacionNegocio {

    UsuarioDominio consultarUsuarioPorCorreo(String correo);

    Rol consultarRolVigente(UUID idUsuario);

    UUID obtenerIdActorActivo(String correo);
}
