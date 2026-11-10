package co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad;

import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

public final class ValidarCelularRule implements Rule<String> {

    private static final String PATRON_CELULAR_COLOMBIANO = "^3\\d{9}$";

    private static final ValidarCelularRule INSTANCIA = new ValidarCelularRule();

    private ValidarCelularRule() {
    }

    public static ValidarCelularRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(String... datos) {
        var celular = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(datos[0]);

        if (UtilTexto.getUtilTexto().esVacia(celular)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Identidad.CELULAR_OBLIGATORIO);
        }
        if (!UtilTexto.getUtilTexto().formatoEsValido(celular, PATRON_CELULAR_COLOMBIANO)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Identidad.CELULAR_FORMATO_INVALIDO);
        }
    }
}
