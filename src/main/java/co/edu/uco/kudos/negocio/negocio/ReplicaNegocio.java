package co.edu.uco.kudos.negocio.negocio;

import co.edu.uco.kudos.dominio.ReplicaDominio;

import java.util.List;
import java.util.UUID;

public interface ReplicaNegocio {

    void responderKudo(UUID idActor, UUID idKudo, String contenido);

    void responderReplica(UUID idActor, UUID idReplica, String contenido);

    List<ReplicaDominio> consultarConversacion(UUID idKudo);
}
