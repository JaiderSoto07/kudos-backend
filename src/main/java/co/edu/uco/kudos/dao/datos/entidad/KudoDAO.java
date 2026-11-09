package co.edu.uco.kudos.dao.datos.entidad;

import co.edu.uco.kudos.dao.datos.ActualizarDAO;
import co.edu.uco.kudos.dao.datos.ConsultarDAO;
import co.edu.uco.kudos.dao.datos.CrearDAO;
import co.edu.uco.kudos.entidad.KudoEntidad;

import java.util.UUID;

public interface KudoDAO extends CrearDAO<KudoEntidad>, ConsultarDAO<KudoEntidad, UUID>, ActualizarDAO<KudoEntidad, UUID> {
}
