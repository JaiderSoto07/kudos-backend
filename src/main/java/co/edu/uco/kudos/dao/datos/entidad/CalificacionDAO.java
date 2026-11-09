package co.edu.uco.kudos.dao.datos.entidad;

import co.edu.uco.kudos.dao.datos.ActualizarDAO;
import co.edu.uco.kudos.dao.datos.ConsultarDAO;
import co.edu.uco.kudos.dao.datos.CrearDAO;
import co.edu.uco.kudos.entidad.CalificacionEntidad;

import java.util.UUID;

public interface CalificacionDAO extends CrearDAO<CalificacionEntidad>, ConsultarDAO<CalificacionEntidad, UUID>, ActualizarDAO<CalificacionEntidad, UUID> {
}
