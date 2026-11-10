package co.edu.uco.kudos.negocio.negocio;

import co.edu.uco.kudos.dominio.SolicitudRegistroDominio;

import java.util.List;
import java.util.UUID;

public interface SolicitudNegocio {

    List<SolicitudRegistroDominio> consultarPendientes(UUID idActor);

    void aprobar(UUID idActor, UUID idSolicitud);

    void rechazar(UUID idActor, UUID idSolicitud, String motivo);

    void notificarAutoridades(UUID idSolicitud);
}
