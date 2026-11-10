package co.edu.uco.kudos.negocio.negocio;

import co.edu.uco.kudos.dominio.CalificacionDominio;

import java.util.List;
import java.util.UUID;

public interface CalificacionNegocio {

    void calificarKudo(UUID idActor, UUID idKudo, Integer estrellas);

    void calificarReplica(UUID idActor, UUID idReplica, Integer estrellas);

    List<CalificacionDominio> consultarValoracionesKudo(UUID idKudo);

    List<CalificacionDominio> consultarValoracionesReplica(UUID idReplica);
}
