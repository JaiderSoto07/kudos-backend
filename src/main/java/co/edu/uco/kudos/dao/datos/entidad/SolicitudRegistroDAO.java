package co.edu.uco.kudos.dao.datos.entidad;

import co.edu.uco.kudos.dao.datos.ActualizarDAO;
import co.edu.uco.kudos.dao.datos.ConsultarDAO;
import co.edu.uco.kudos.dao.datos.CrearDAO;
import co.edu.uco.kudos.entidad.SolicitudRegistroEntidad;

import java.util.UUID;

public interface SolicitudRegistroDAO extends CrearDAO<SolicitudRegistroEntidad>, ConsultarDAO<SolicitudRegistroEntidad, UUID>, ActualizarDAO<SolicitudRegistroEntidad, UUID> {
}
