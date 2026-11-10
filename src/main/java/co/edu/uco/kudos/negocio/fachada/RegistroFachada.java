package co.edu.uco.kudos.negocio.fachada;

import co.edu.uco.kudos.dto.OpcionesRegistroDto;
import co.edu.uco.kudos.dto.RegistroDto;

import java.util.UUID;

public interface RegistroFachada {

    void registrarSolicitud(String correo, RegistroDto datos);

    void registrarNuevaSolicitud(UUID idUsuario, RegistroDto datos);

    OpcionesRegistroDto consultarOpciones();
}
