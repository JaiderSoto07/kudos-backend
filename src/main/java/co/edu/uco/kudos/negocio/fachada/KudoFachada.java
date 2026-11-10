package co.edu.uco.kudos.negocio.fachada;

import co.edu.uco.kudos.dto.KudoDto;
import co.edu.uco.kudos.dto.PaginaKudosDto;
import co.edu.uco.kudos.dto.PersonaDto;

import java.util.List;
import java.util.UUID;

public interface KudoFachada {

    void crear(UUID idActor, KudoDto datos);

    PaginaKudosDto consultarMuro(UUID idCategoria, UUID idPersona, int pagina);

    KudoDto consultar(UUID idActor, UUID idKudo);

    List<KudoDto> consultarHistorial(UUID idPersona, String tipo);

    List<PersonaDto> buscarPersonas(UUID idActor, String texto);
}
