package co.edu.uco.kudos.negocio.fachada;

import co.edu.uco.kudos.dto.CargoDto;
import co.edu.uco.kudos.dto.ProgramaAcademicoDto;

import java.util.List;
import java.util.UUID;

public interface ProgramaFachada {

    List<ProgramaAcademicoDto> consultar(UUID idActor);

    ProgramaAcademicoDto consultarPorId(UUID idActor, UUID idPrograma);

    void registrar(UUID idActor, ProgramaAcademicoDto datos);

    void modificar(UUID idActor, UUID idPrograma, ProgramaAcademicoDto datos);

    void inactivar(UUID idActor, UUID idPrograma);

    List<CargoDto> consultarCoordinadores(UUID idActor, UUID idPrograma);
}
