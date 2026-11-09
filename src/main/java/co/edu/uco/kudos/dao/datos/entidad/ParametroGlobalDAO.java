package co.edu.uco.kudos.dao.datos.entidad;

import co.edu.uco.kudos.dao.datos.ActualizarDAO;
import co.edu.uco.kudos.dao.datos.ConsultarDAO;
import co.edu.uco.kudos.dao.datos.CrearDAO;
import co.edu.uco.kudos.entidad.ParametroGlobalEntidad;

import java.util.UUID;

public interface ParametroGlobalDAO extends CrearDAO<ParametroGlobalEntidad>, ConsultarDAO<ParametroGlobalEntidad, UUID>, ActualizarDAO<ParametroGlobalEntidad, UUID> {
}
