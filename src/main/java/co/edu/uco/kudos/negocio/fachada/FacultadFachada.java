package co.edu.uco.kudos.negocio.fachada;

import co.edu.uco.kudos.dto.CargoDto;
import co.edu.uco.kudos.dto.FacultadDto;

import java.util.List;
import java.util.UUID;

public interface FacultadFachada {

    List<FacultadDto> consultar(UUID idActor);

    void registrar(UUID idActor, FacultadDto datos);

    void modificar(UUID idActor, UUID idFacultad, FacultadDto datos);

    void inactivar(UUID idActor, UUID idFacultad);

    List<CargoDto> consultarCoordinadores(UUID idActor, UUID idFacultad);
}
