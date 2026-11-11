package co.edu.uco.kudos.negocio.negocio;

import co.edu.uco.kudos.dominio.ReporteDominio;

import java.util.List;
import java.util.UUID;

public interface ReporteNegocio {

    void reportarKudo(UUID idActor, UUID idKudo, String motivo, String descripcion);

    void reportarReplica(UUID idActor, UUID idReplica, String motivo, String descripcion);

    List<ReporteDominio> consultarMisReportes(UUID idActor);

    List<ReporteDominio> consultarReportesAbiertos(UUID idActor);
}
