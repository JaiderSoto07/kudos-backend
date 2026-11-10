package co.edu.uco.kudos.negocio.negocio;

import co.edu.uco.kudos.dominio.UsuarioDominio;

import java.util.List;
import java.util.UUID;

public interface PersonaNegocio {

    List<UsuarioDominio> buscarActivas(UUID idActor, String texto);
}
