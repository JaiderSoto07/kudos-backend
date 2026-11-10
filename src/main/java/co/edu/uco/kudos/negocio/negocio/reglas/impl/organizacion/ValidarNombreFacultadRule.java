package co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion;

import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

public final class ValidarNombreFacultadRule implements Rule<String> {

    private static final ValidarNombreFacultadRule INSTANCIA = new ValidarNombreFacultadRule();

    private ValidarNombreFacultadRule() {
    }

    public static ValidarNombreFacultadRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(String... datos) {
        var nombre = datos[0];

        if (UtilTexto.getUtilTexto().esVacia(nombre)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Organizacion.NOMBRE_FACULTAD_OBLIGATORIO);
        }
        if (!UtilTexto.getUtilTexto().longitudCadenaEsValida(nombre, 3, 80, true)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Organizacion.NOMBRE_FACULTAD_LONGITUD_INVALIDA);
        }
    }
}
