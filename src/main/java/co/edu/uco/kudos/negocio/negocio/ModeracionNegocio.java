package co.edu.uco.kudos.negocio.negocio;

import co.edu.uco.kudos.dominio.ModeracionDominio;
import co.edu.uco.kudos.dominio.ReporteDominio;

import java.util.List;
import java.util.UUID;

public interface ModeracionNegocio {

    void iniciarRevisionKudo(UUID idActor, UUID idKudo);

    void iniciarRevisionReplica(UUID idActor, UUID idReplica);

    void vetarKudo(UUID idActor, UUID idKudo, String motivo);

    void vetarReplica(UUID idActor, UUID idReplica, String motivo);

    void liberarKudo(UUID idActor, UUID idKudo, String motivo);

    void liberarReplica(UUID idActor, UUID idReplica, String motivo);

    List<ModeracionDominio> consultarVetos(UUID idActor);

    List<ReporteDominio> consultarReportesResueltos(UUID idModeracion);
}
