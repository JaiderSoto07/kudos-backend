package co.edu.uco.kudos.negocio.negocio;

import co.edu.uco.kudos.dominio.AsignacionRolDominio;
import co.edu.uco.kudos.dominio.ProgramaAcademicoDominio;

import java.util.List;
import java.util.UUID;

public interface ProgramaNegocio {

    List<ProgramaAcademicoDominio> consultar(UUID idActor);

    ProgramaAcademicoDominio consultarPorId(UUID idActor, UUID idPrograma);

    void registrar(UUID idActor, ProgramaAcademicoDominio datos);

    void modificar(UUID idActor, UUID idPrograma, ProgramaAcademicoDominio datos);

    void inactivar(UUID idActor, UUID idPrograma);

    List<AsignacionRolDominio> consultarCoordinadores(UUID idActor, UUID idPrograma);
}
