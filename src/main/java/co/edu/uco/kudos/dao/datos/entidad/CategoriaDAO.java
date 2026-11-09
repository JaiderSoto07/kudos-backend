package co.edu.uco.kudos.dao.datos.entidad;

import co.edu.uco.kudos.dao.datos.ActualizarDAO;
import co.edu.uco.kudos.dao.datos.ConsultarActivosDAO;
import co.edu.uco.kudos.dao.datos.ConsultarDAO;
import co.edu.uco.kudos.dao.datos.CrearDAO;
import co.edu.uco.kudos.entidad.CategoriaEntidad;

import java.util.UUID;

public interface CategoriaDAO extends CrearDAO<CategoriaEntidad>, ConsultarDAO<CategoriaEntidad, UUID>, ConsultarActivosDAO<CategoriaEntidad>, ActualizarDAO<CategoriaEntidad, UUID> {
}
