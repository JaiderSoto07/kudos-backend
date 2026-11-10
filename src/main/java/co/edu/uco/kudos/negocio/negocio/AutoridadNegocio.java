package co.edu.uco.kudos.negocio.negocio;

import co.edu.uco.kudos.dominio.UsuarioDominio;

import java.util.UUID;

public interface AutoridadNegocio {

    void registrarAutoridad(UUID idActor, String rol, UsuarioDominio datos);
}
