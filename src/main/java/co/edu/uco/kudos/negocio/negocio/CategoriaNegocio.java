package co.edu.uco.kudos.negocio.negocio;

import co.edu.uco.kudos.dominio.CategoriaDominio;

import java.util.List;
import java.util.UUID;

public interface CategoriaNegocio {

    void registrarNuevaCategoria(UUID idActor, CategoriaDominio datos);

    void modificarCategoria(UUID idActor, UUID id, CategoriaDominio datos);

    void activarCategoria(UUID idActor, UUID id);

    void desactivarCategoria(UUID idActor, UUID id);

    List<CategoriaDominio> consultarActivas();

    List<CategoriaDominio> consultarTodas(UUID idActor);
}
