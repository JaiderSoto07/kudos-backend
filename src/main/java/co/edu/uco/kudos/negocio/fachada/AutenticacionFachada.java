package co.edu.uco.kudos.negocio.fachada;

import co.edu.uco.kudos.dto.UsuarioDto;

import java.util.UUID;

public interface AutenticacionFachada {

    UsuarioDto consultarUsuarioActual(String correo);

    UUID obtenerIdActorActivo(String correo);

    UUID obtenerIdUsuarioRegistrado(String correo);
}
