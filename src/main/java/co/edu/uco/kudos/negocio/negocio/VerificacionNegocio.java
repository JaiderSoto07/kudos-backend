package co.edu.uco.kudos.negocio.negocio;

import co.edu.uco.kudos.dominio.CodigoVerificacionDominio;
import co.edu.uco.kudos.dominio.UsuarioDominio;

import java.util.UUID;

public interface VerificacionNegocio {

    void enviarCodigoCelular(UsuarioDominio usuario);

    boolean verificarCelular(UUID idUsuario, String codigoIngresado);

    void reenviarCodigoCelular(UUID idUsuario);

    CodigoVerificacionDominio consultarCodigoPendiente(UUID idUsuario);
}
