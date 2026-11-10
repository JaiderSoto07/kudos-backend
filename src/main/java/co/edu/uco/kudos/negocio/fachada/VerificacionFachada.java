package co.edu.uco.kudos.negocio.fachada;

import co.edu.uco.kudos.dto.CodigoVerificacionDto;

import java.util.UUID;

public interface VerificacionFachada {

    boolean verificarCelular(UUID idUsuario, CodigoVerificacionDto datos);

    void reenviarCodigoCelular(UUID idUsuario);
}
