package co.edu.uco.kudos.negocio.fachada;

import co.edu.uco.kudos.dto.DecisionDto;
import co.edu.uco.kudos.dto.VetoDto;

import java.util.List;
import java.util.UUID;

public interface ModeracionFachada {

    void iniciarRevisionKudo(UUID idActor, UUID idKudo);

    void iniciarRevisionReplica(UUID idActor, UUID idReplica);

    void vetarKudo(UUID idActor, UUID idKudo, DecisionDto datos);

    void vetarReplica(UUID idActor, UUID idReplica, DecisionDto datos);

    void liberarKudo(UUID idActor, UUID idKudo, DecisionDto datos);

    void liberarReplica(UUID idActor, UUID idReplica, DecisionDto datos);

    List<VetoDto> consultarVetos(UUID idActor);
}
