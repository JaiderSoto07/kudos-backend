package co.edu.uco.kudos.negocio.fachada;

import co.edu.uco.kudos.dto.ReplicaDto;

import java.util.List;
import java.util.UUID;

public interface ReplicaFachada {

    void responderKudo(UUID idActor, UUID idKudo, ReplicaDto datos);

    void responderReplica(UUID idActor, UUID idReplica, ReplicaDto datos);

    List<ReplicaDto> consultarConversacion(UUID idKudo);
}
