package co.edu.uco.kudos.negocio.negocio.reglas.impl.categoria;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.entidad.CategoriaEntidad;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;

import java.util.UUID;

public final class AsegurarNombreCategoriaNoRepetidoRule implements Rule<Object> {

    private static final AsegurarNombreCategoriaNoRepetidoRule INSTANCIA = new AsegurarNombreCategoriaNoRepetidoRule();

    private AsegurarNombreCategoriaNoRepetidoRule() {
    }

    public static AsegurarNombreCategoriaNoRepetidoRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        var nombre = (String) datos[0];
        var idCategoriaPropia = (UUID) datos[1];
        var daoFactory = (DAOFactory) datos[2];

        var filtro = new CategoriaEntidad.Builder().nombre(nombre).build();
        var categoriasConElMismoNombre = daoFactory.obtenerCategoriaDAO().consultarPorFiltro(filtro);

        for (var categoria : categoriasConElMismoNombre) {
            if (!categoria.getId().equals(idCategoriaPropia)) {
                throw KudosNegocioExcepcion.crear(CatalogoMensajes.CategoriaNegocioImpl.NOMBRE_CATEGORIA_YA_EXISTE);
            }
        }
    }
}
