package co.edu.uco.kudos.dao.datos.entidad;

import co.edu.uco.kudos.dao.datos.ActualizarDAO;
import co.edu.uco.kudos.dao.datos.ConsultarDAO;
import co.edu.uco.kudos.dao.datos.CrearDAO;
import co.edu.uco.kudos.entidad.ReporteEntidad;

import java.util.UUID;

public interface ReporteDAO extends CrearDAO<ReporteEntidad>, ConsultarDAO<ReporteEntidad, UUID>, ActualizarDAO<ReporteEntidad, UUID> {
}
