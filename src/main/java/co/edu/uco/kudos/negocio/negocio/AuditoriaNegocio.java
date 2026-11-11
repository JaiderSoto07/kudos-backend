package co.edu.uco.kudos.negocio.negocio;

import co.edu.uco.kudos.dominio.AuditoriaDominio;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AuditoriaNegocio {

    void registrar(AuditoriaDominio datos);

    List<AuditoriaDominio> consultar(UUID idActor, LocalDate desde, LocalDate hasta, String actor, String accion, String tipoObjeto);
}
