package co.edu.uco.kudos.negocio.negocio;

import co.edu.uco.kudos.dominio.KudoDominio;

import java.util.List;
import java.util.UUID;

public interface KudoNegocio {

    void crear(UUID idActor, KudoDominio datos);

    List<KudoDominio> consultarMuro(UUID idCategoria, UUID idPersona);

    KudoDominio consultar(UUID idActor, UUID idKudo);

    List<KudoDominio> consultarHistorial(UUID idPersona, String tipo);
}
