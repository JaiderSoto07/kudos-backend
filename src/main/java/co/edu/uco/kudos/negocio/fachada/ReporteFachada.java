package co.edu.uco.kudos.negocio.fachada;

import co.edu.uco.kudos.dto.ContenidoReportadoDto;
import co.edu.uco.kudos.dto.ReporteDto;

import java.util.List;
import java.util.UUID;

public interface ReporteFachada {

    void reportarKudo(UUID idActor, UUID idKudo, ReporteDto datos);

    void reportarReplica(UUID idActor, UUID idReplica, ReporteDto datos);

    List<ReporteDto> consultarMisReportes(UUID idActor);

    List<ContenidoReportadoDto> consultarReportesAbiertos(UUID idActor);
}
