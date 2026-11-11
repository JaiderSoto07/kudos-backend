package co.edu.uco.kudos.negocio.negocio;

import co.edu.uco.kudos.dominio.NotificacionDominio;
import co.edu.uco.kudos.dominio.TipoNotificacion;

import java.util.List;
import java.util.UUID;

public interface NotificacionNegocio {

    void notificar(NotificacionDominio datos);

    List<NotificacionDominio> consultarPorTipo(UUID idDestinatario, TipoNotificacion tipo);

    List<NotificacionDominio> consultarMias(UUID idActor);

    void marcarLeida(UUID idActor, UUID idNotificacion);

    void marcarTodasLeidas(UUID idActor);
}
