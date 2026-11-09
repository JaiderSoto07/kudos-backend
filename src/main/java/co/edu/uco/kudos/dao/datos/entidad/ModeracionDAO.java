package co.edu.uco.kudos.dao.datos.entidad;

import co.edu.uco.kudos.dao.datos.ActualizarDAO;
import co.edu.uco.kudos.dao.datos.ConsultarDAO;
import co.edu.uco.kudos.dao.datos.CrearDAO;
import co.edu.uco.kudos.entidad.ModeracionEntidad;

import java.util.UUID;

public interface ModeracionDAO extends CrearDAO<ModeracionEntidad>, ConsultarDAO<ModeracionEntidad, UUID>, ActualizarDAO<ModeracionEntidad, UUID> {
}
