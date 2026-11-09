package co.edu.uco.kudos.negocio.negocio.reglas.impl.categoria;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

import java.util.UUID;

public final class AsegurarIdCategoriaNoExisteRule implements Rule<Object> {

    private static final Rule<Object> INSTANCIA = new AsegurarIdCategoriaNoExisteRule();

    private AsegurarIdCategoriaNoExisteRule() {
    }

    public static Rule<Object> obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        var idCategoria = (UUID) datos[0];
        var daoFactory = (DAOFactory) datos[1];

        var categoriaEncontrada = daoFactory.obtenerCategoriaDAO().consultarPorId(idCategoria);

        if (!UtilId.esValorDefecto(categoriaEncontrada.getId())) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.CategoriaNegocioImpl.ID_CATEGORIA_YA_EXISTE);
        }
    }
}
