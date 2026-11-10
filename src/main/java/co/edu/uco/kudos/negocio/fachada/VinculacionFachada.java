package co.edu.uco.kudos.negocio.fachada;

import co.edu.uco.kudos.dto.CargoDto;
import co.edu.uco.kudos.dto.DesvinculacionDto;
import co.edu.uco.kudos.dto.VinculoDto;

import java.util.List;
import java.util.UUID;

public interface VinculacionFachada {

    List<VinculoDto> consultarDePrograma(UUID idActor, UUID idPrograma);

    List<VinculoDto> consultarDeUsuario(UUID idActor, UUID idUsuario);

    List<VinculoDto> consultarPropios(UUID idUsuario);

    void vincular(UUID idActor, UUID idPrograma, VinculoDto datos);

    void desvincular(UUID idActor, UUID idVinculo, DesvinculacionDto datos);

    List<CargoDto> consultarEstructura();
}
