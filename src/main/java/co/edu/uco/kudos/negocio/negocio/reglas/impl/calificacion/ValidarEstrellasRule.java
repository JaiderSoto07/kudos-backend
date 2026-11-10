package co.edu.uco.kudos.negocio.negocio.reglas.impl.calificacion;

import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;

public final class ValidarEstrellasRule implements Rule<Integer> {

    public static final int ESTRELLAS_MINIMAS = 1;
    public static final int ESTRELLAS_MAXIMAS = 5;
    private static final ValidarEstrellasRule INSTANCIA = new ValidarEstrellasRule();

    private ValidarEstrellasRule() {
    }

    public static ValidarEstrellasRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Integer... datos) {
        if (datos[0] == null || datos[0] < ESTRELLAS_MINIMAS || datos[0] > ESTRELLAS_MAXIMAS) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Calificacion.ESTRELLAS_FUERA_DE_RANGO);
        }
    }
}
