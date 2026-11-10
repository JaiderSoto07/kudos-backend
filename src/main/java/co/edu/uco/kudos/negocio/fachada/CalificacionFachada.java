package co.edu.uco.kudos.negocio.fachada;

import co.edu.uco.kudos.dto.CalificacionDto;
import co.edu.uco.kudos.dto.ValoracionesDto;

import java.util.UUID;

public interface CalificacionFachada {

    void calificarKudo(UUID idActor, UUID idKudo, CalificacionDto datos);

    void calificarReplica(UUID idActor, UUID idReplica, CalificacionDto datos);

    ValoracionesDto consultarValoracionesKudo(UUID idActor, UUID idKudo);

    ValoracionesDto consultarValoracionesReplica(UUID idActor, UUID idReplica);
}
