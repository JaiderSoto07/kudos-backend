package co.edu.uco.kudos.negocio.fachada;

import co.edu.uco.kudos.dto.PaginaAuditoriaDto;

import java.time.LocalDate;
import java.util.UUID;

public interface AuditoriaFachada {

    PaginaAuditoriaDto consultar(UUID idActor, LocalDate desde, LocalDate hasta, String actor, String accion, String tipoObjeto, int pagina);
}
