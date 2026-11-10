package co.edu.uco.kudos.negocio.negocio;

import co.edu.uco.kudos.dominio.VinculacionProgramaDominio;

import java.util.List;
import java.util.UUID;

public interface VinculacionNegocio {

    List<VinculacionProgramaDominio> consultarDePrograma(UUID idActor, UUID idPrograma);

    List<VinculacionProgramaDominio> consultarDeUsuario(UUID idActor, UUID idUsuario);

    List<VinculacionProgramaDominio> consultarPropios(UUID idUsuario);

    void vincular(UUID idActor, UUID idPrograma, String correo, String tipoVinculo);

    void desvincular(UUID idActor, UUID idVinculo, String motivo);
}
