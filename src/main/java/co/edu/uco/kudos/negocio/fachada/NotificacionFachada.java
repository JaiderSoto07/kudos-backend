package co.edu.uco.kudos.negocio.fachada;

import co.edu.uco.kudos.dto.NotificacionDto;

import java.util.List;
import java.util.UUID;

public interface NotificacionFachada {

    List<NotificacionDto> consultarMias(UUID idActor);

    void marcarLeida(UUID idActor, UUID idNotificacion);

    void marcarTodasLeidas(UUID idActor);
}
