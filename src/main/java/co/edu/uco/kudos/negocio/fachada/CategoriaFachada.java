package co.edu.uco.kudos.negocio.fachada;

import co.edu.uco.kudos.dto.CategoriaDto;

import java.util.List;
import java.util.UUID;

public interface CategoriaFachada {

    void registrarNuevaCategoria(UUID idActor, CategoriaDto datos);

    void modificarCategoria(UUID idActor, UUID id, CategoriaDto datos);

    void activarCategoria(UUID idActor, UUID id);

    void desactivarCategoria(UUID idActor, UUID id);

    List<CategoriaDto> consultarActivas();

    List<CategoriaDto> consultarTodas(UUID idActor);
}
