package co.edu.uco.kudos.negocio.fachada;

import co.edu.uco.kudos.dto.RechazoDto;
import co.edu.uco.kudos.dto.SolicitudDto;

import java.util.List;
import java.util.UUID;

public interface SolicitudFachada {

    List<SolicitudDto> consultarPendientes(UUID idActor);

    void aprobar(UUID idActor, UUID idSolicitud);

    void rechazar(UUID idActor, UUID idSolicitud, RechazoDto datos);
}
