package co.edu.uco.kudos.dao.datos.entidad;

import co.edu.uco.kudos.dao.datos.ActualizarDAO;
import co.edu.uco.kudos.dao.datos.ConsultarDAO;
import co.edu.uco.kudos.dao.datos.CrearDAO;
import co.edu.uco.kudos.entidad.FacultadEntidad;

import java.util.UUID;

public interface FacultadDAO extends CrearDAO<FacultadEntidad>, ConsultarDAO<FacultadEntidad, UUID>, ActualizarDAO<FacultadEntidad, UUID> {
}
