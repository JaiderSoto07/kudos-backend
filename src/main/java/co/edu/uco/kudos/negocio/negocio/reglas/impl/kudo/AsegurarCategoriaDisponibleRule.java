package co.edu.uco.kudos.negocio.negocio.reglas.impl.kudo;

import co.edu.uco.kudos.dominio.CategoriaDominio;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

import java.util.UUID;

public final class AsegurarCategoriaDisponibleRule implements Rule<Object> {

    private static final AsegurarCategoriaDisponibleRule INSTANCIA = new AsegurarCategoriaDisponibleRule();

    private AsegurarCategoriaDisponibleRule() {
    }

    public static AsegurarCategoriaDisponibleRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        var idCategoria = (UUID) datos[0];
        var categoria = (CategoriaDominio) datos[1];

        if (UtilId.esValorDefecto(idCategoria)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Kudo.CATEGORIA_OBLIGATORIA);
        }
        if (UtilId.esValorDefecto(categoria.getId()) || !categoria.isActiva()) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Kudo.CATEGORIA_NO_DISPONIBLE);
        }
    }
}
