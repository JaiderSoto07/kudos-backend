package co.edu.uco.kudos.negocio.negocio.reglas.impl.kudo;

import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

public final class ValidarContenidoRule implements Rule<String> {

    private static final ValidarContenidoRule INSTANCIA = new ValidarContenidoRule();

    private ValidarContenidoRule() {
    }

    public static ValidarContenidoRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(String... datos) {
        if (UtilTexto.getUtilTexto().esVacia(datos[0])) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Kudo.CONTENIDO_OBLIGATORIO);
        }
    }
}
