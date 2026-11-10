package co.edu.uco.kudos.negocio.negocio;

import co.edu.uco.kudos.dominio.FacultadDominio;
import co.edu.uco.kudos.dominio.ProgramaAcademicoDominio;
import co.edu.uco.kudos.dominio.SolicitudRegistroDominio;

import java.util.List;
import java.util.UUID;

public interface RegistroNegocio {

    void registrarSolicitud(String correo, String rolSolicitado, SolicitudRegistroDominio datos);

    void registrarNuevaSolicitud(UUID idUsuario, String rolSolicitado, SolicitudRegistroDominio datos);

    List<FacultadDominio> consultarFacultadesDisponibles();

    List<ProgramaAcademicoDominio> consultarProgramasDisponibles();
}
