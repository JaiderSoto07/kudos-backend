package co.edu.uco.kudos.negocio.negocio;

import co.edu.uco.kudos.dominio.SolicitudRegistroDominio;
import co.edu.uco.kudos.dominio.UsuarioDominio;

import java.util.UUID;

public interface CuentaNegocio {

    UsuarioDominio consultarUsuario(UUID idUsuario);

    SolicitudRegistroDominio consultarUltimaSolicitud(UUID idUsuario);

    String consultarUltimoCodigoEnviado(UUID idUsuario);

    void actualizarDatosPersonales(UUID idUsuario, UsuarioDominio datos);

    void cambiarCelular(UUID idUsuario, UsuarioDominio datos);

    void cambiarCorreo(UUID idUsuario, UsuarioDominio datos);
}
