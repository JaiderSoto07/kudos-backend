package co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad;

import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

public final class ValidarDatosPersonalesRule implements Rule<String> {

    private static final int LONGITUD_MINIMA = 1;
    private static final int LONGITUD_MAXIMA = 60;

    private static final ValidarDatosPersonalesRule INSTANCIA = new ValidarDatosPersonalesRule();

    private ValidarDatosPersonalesRule() {
    }

    public static ValidarDatosPersonalesRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(String... datos) {
        var nombres = datos[0];
        var apellidos = datos[1];

        if (UtilTexto.getUtilTexto().esVacia(nombres)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Identidad.NOMBRES_OBLIGATORIOS);
        }
        if (!UtilTexto.getUtilTexto().longitudCadenaEsValida(nombres, LONGITUD_MINIMA, LONGITUD_MAXIMA, true)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Identidad.NOMBRES_LONGITUD_INVALIDA);
        }
        if (UtilTexto.getUtilTexto().esVacia(apellidos)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Identidad.APELLIDOS_OBLIGATORIOS);
        }
        if (!UtilTexto.getUtilTexto().longitudCadenaEsValida(apellidos, LONGITUD_MINIMA, LONGITUD_MAXIMA, true)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Identidad.APELLIDOS_LONGITUD_INVALIDA);
        }
    }
}
