package co.edu.uco.kudos.negocio.negocio;

import co.edu.uco.kudos.dominio.AsignacionRolDominio;
import co.edu.uco.kudos.dominio.FacultadDominio;

import java.util.List;
import java.util.UUID;

public interface FacultadNegocio {

    List<FacultadDominio> consultar(UUID idActor);

    void registrar(UUID idActor, FacultadDominio datos);

    void modificar(UUID idActor, UUID idFacultad, FacultadDominio datos);

    void inactivar(UUID idActor, UUID idFacultad);

    List<AsignacionRolDominio> consultarCoordinadores(UUID idActor, UUID idFacultad);

    int contarProgramasActivos(UUID idFacultad);
}
