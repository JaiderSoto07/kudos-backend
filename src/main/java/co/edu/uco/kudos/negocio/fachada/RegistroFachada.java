package co.edu.uco.kudos.negocio.fachada;

import co.edu.uco.kudos.dto.OpcionesRegistroDto;
import co.edu.uco.kudos.dto.RegistroDto;

public interface RegistroFachada {

    void registrarSolicitud(String correo, RegistroDto datos);

    OpcionesRegistroDto consultarOpciones();
}
