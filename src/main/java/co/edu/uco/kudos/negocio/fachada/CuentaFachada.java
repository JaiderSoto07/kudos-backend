package co.edu.uco.kudos.negocio.fachada;

import co.edu.uco.kudos.dto.CuentaDto;

import java.util.UUID;

public interface CuentaFachada {

    CuentaDto consultarCuenta(UUID idUsuario);

    void actualizarDatosPersonales(UUID idUsuario, CuentaDto datos);

    void cambiarCelular(UUID idUsuario, CuentaDto datos);

    void cambiarCorreo(UUID idUsuario, CuentaDto datos);
}
