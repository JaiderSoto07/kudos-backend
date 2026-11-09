package co.edu.uco.kudos.dao.datos.entidad;

import co.edu.uco.kudos.dao.datos.ActualizarDAO;
import co.edu.uco.kudos.dao.datos.ConsultarDAO;
import co.edu.uco.kudos.dao.datos.CrearDAO;
import co.edu.uco.kudos.entidad.AsignacionRolEntidad;

import java.util.UUID;

public interface AsignacionRolDAO extends CrearDAO<AsignacionRolEntidad>, ConsultarDAO<AsignacionRolEntidad, UUID>, ActualizarDAO<AsignacionRolEntidad, UUID> {
}
