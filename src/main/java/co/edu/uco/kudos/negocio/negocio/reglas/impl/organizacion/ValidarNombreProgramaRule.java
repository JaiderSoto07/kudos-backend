package co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion;

import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

public final class ValidarNombreProgramaRule implements Rule<String> {

    private static final ValidarNombreProgramaRule INSTANCIA = new ValidarNombreProgramaRule();

    private ValidarNombreProgramaRule() {
    }

    public static ValidarNombreProgramaRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(String... datos) {
        var nombre = datos[0];

        if (UtilTexto.getUtilTexto().esVacia(nombre)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Organizacion.NOMBRE_PROGRAMA_OBLIGATORIO);
        }
        if (!UtilTexto.getUtilTexto().longitudCadenaEsValida(nombre, 3, 100, true)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Organizacion.NOMBRE_PROGRAMA_LONGITUD_INVALIDA);
        }
    }
}
