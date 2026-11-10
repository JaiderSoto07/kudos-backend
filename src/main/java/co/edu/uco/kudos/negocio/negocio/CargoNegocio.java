package co.edu.uco.kudos.negocio.negocio;

import co.edu.uco.kudos.dominio.AsignacionRolDominio;
import co.edu.uco.kudos.dominio.Rol;

import java.util.List;
import java.util.UUID;

public interface CargoNegocio {

    AsignacionRolDominio consultarAsignacionVigente(UUID idUsuario);

    List<AsignacionRolDominio> consultarCargosVigentes();

    List<AsignacionRolDominio> consultarHistorial(Rol rol, UUID idFacultad, UUID idPrograma);

    void cerrarCargoVigente(UUID idActor, Rol rol, UUID idFacultad, UUID idPrograma, String motivo);
}
