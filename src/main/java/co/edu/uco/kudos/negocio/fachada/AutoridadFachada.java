package co.edu.uco.kudos.negocio.fachada;

import co.edu.uco.kudos.dto.AutoridadDto;

import java.util.UUID;

public interface AutoridadFachada {

    void registrarAutoridad(UUID idActor, AutoridadDto datos);
}
